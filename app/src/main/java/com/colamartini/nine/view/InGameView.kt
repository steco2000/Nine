package com.colamartini.nine.view

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.colamartini.nine.R
import com.colamartini.nine.beans.GameBean
import com.colamartini.nine.control.GamePersistenceController
import com.colamartini.nine.control.NineController
import com.colamartini.nine.control.toZeroTrailedString
import com.colamartini.nine.navigation.Screen
import com.colamartini.nine.ui.theme.*
import com.colamartini.nine.widgets.*
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

//schermata di gioco

val controller = NineController()
var dbController: GamePersistenceController? = null

@SuppressLint("MutableCollectionMutableState")
@Composable
fun InGameView(difficulty: Int, navController: NavController) {

    if (dbController == null) {
        dbController = GamePersistenceController(LocalContext.current)
    }

    var sequence by rememberSaveable {
        mutableStateOf("")
    }
    var guessedIndexes by rememberSaveable {
        mutableStateOf(listOf<Int>())
    }
    var guessedChars by rememberSaveable {
        mutableStateOf(CharArray(9))
    }
    var userInput by rememberSaveable {
        mutableStateOf("")
    }
    var distance by rememberSaveable {
        mutableStateOf("")
    }
    val liveInput by rememberSaveable {
        mutableStateOf(CharArray(9))
    }
    var charsExtracted by rememberSaveable {
        mutableStateOf(CharArray(9))
    }
    var selectedInputCell by rememberSaveable {
        mutableStateOf(0)
    }
    var sessionStarted by rememberSaveable {
        mutableStateOf(false)
    }
    var confirmationNotAllowed by rememberSaveable {
        mutableStateOf(false)
    }
    var attempts by rememberSaveable {
        mutableStateOf(0)
    }
    var timerIsRunning by rememberSaveable {
        mutableStateOf(false)
    }
    var elapsedTime: Long by rememberSaveable {
        mutableStateOf(0)
    }
    var startTime: Long by rememberSaveable {
        mutableStateOf(0)
    }
    var minutes: Long by rememberSaveable {
        mutableStateOf(0)
    }
    var seconds: Long by rememberSaveable {
        mutableStateOf(0)
    }
    var centSeconds: Long by rememberSaveable {
        mutableStateOf(0)
    }
    var previousInputSequences by rememberSaveable {
        mutableStateOf(listOf<String>())
    }
    var backRequest by rememberSaveable {
        mutableStateOf(false)
    }
    var gameSaved by rememberSaveable {
        mutableStateOf(false)
    }

    //all'avvio della sessione vengono inizializzate tutte le variabili necessarie e impostata la difficoltà al controller applicativo
    if (!sessionStarted) {
        sequence = controller.refreshSequence()

        /*
        al refresh della sequenza vengono estratti 9 simboli casuali dal set dell'app con cui viene generata la sequenza stessa.
        I simboli vengono poi salvati in un array per essere mostrati all'utente dopo essere stati mescolati
         */
        charsExtracted = sequence.toCharArray()
        charsExtracted.shuffle()

        sessionStarted = true
        controller.setGameDifficulty(difficulty)
    }

    //Gestione della pressione del tasto back. Se il timer è partito viene chiesto all'utente se è sicuro di uscire, altrimenti si torna al menu
    BackHandler(enabled = true, onBack = {
        backRequest = true
    })
    if (backRequest && timerIsRunning) {
        QuestionAlertDialog(title = stringResource(id = R.string.warning),
            text = stringResource(id = R.string.quit_question),
            onDismissRequest = { backRequest = false },
            onAcceptRequest = { navController.navigate("menu_view") })
    }else if(backRequest){
        navController.navigate("menu_view")
        backRequest = false
    }


    if (controller.gameIsLost()) {

        //se la partita è persa si mostra un alert dialog che informa l'utente. Alla pressione del tasto ok si ricarica l'interfaccia. Inoltre vengono salvate nel db le informazioni della partita
        if (!gameSaved) {
            dbController!!.saveGame(
                GameBean(
                    difficulty = difficulty,
                    attempts = attempts,
                    time = elapsedTime,
                    win = false
                )
            )
            gameSaved = true
        }

        timerIsRunning = false
        QuestionAlertDialog(
            title = stringResource(id = R.string.game_lost),
            text = stringResource(id = R.string.game_lost_dialog_text),
            onAcceptRequest = {
                navController.navigate("menu_view")
            },
            onDismissRequest = {
                navController.navigate(Screen.InGameView.withArgs("$difficulty"))
            },
            confirmButtonText = stringResource(R.string.back_to_menu),
            dismissButtonText = stringResource(R.string.new_game)
        )

    } else if (controller.isGuessed()) {

        //se la partita è vinta si fa la stessa cosa, mostrando nell'alert dialog anche i risultati
        if (!gameSaved) {
            dbController!!.saveGame(
                GameBean(
                    difficulty = difficulty,
                    attempts = attempts,
                    time = elapsedTime,
                    win = true
                )
            )
            gameSaved = true
        }

        timerIsRunning = false
        QuestionAlertDialog(
            title = stringResource(id = R.string.game_won_title),
            text = stringResource(id = R.string.game_won_basic_text) + "\n"
                    + "\n"
                    + stringResource(R.string.time) + " " + minutes.toZeroTrailedString() + ":" + seconds.toZeroTrailedString() + "." + "%02d".format(
                centSeconds / 10
            ) + "\n"
                    + stringResource(R.string.total_attempts) + " " + attempts,
            onAcceptRequest = {
                navController.navigate("menu_view")
            },
            onDismissRequest = {
                navController.navigate(Screen.InGameView.withArgs("$difficulty"))
            },
            confirmButtonText = stringResource(R.string.back_to_menu),
            dismissButtonText = stringResource(R.string.new_game)
        )
    }

    //colonna generale
    Column(
        modifier = Modifier
            .padding(generalPadding)
            .fillMaxWidth()
            .background(background)
    ) {

        //se alla ricomposizione c'è stato un tentativo di confermare una sequenza incompleta viene mostrato un messaggio di errore
        if (confirmationNotAllowed) {
            GeneralInfoAlertDialog(
                title = stringResource(R.string.warning),
                text = stringResource(R.string.missing_symbols)
            ) {
                confirmationNotAllowed = false
            }
        }

        //definizione logo, info partita (tempo e tentativi) e tasto restart
        Row(
            modifier = Modifier
                .padding(generalPadding)
                .fillMaxWidth()
                .background(background),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LogoView(internalLogoSize)
            Column(
                modifier = Modifier
                    .padding(cornerDpShape)
                    .background(background)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                StyledButton(
                    onClick = {
                        //alla pressione del tasto restart si ricarica l'interfaccia di gioco con la stessa difficoltà
                        navController.navigate(Screen.InGameView.withArgs("$difficulty"))
                    },
                    text = stringResource(id = R.string.restart)
                )

                /*
                il timer funziona grazie a un launched effect (thread dedicato) che calcola istante per istante il tempo trascorso
                in millisecondi. Il totale viene poi convertito nel formato MM:SS.CC e mostrato all'utente
                 */
                LaunchedEffect(attempts >= 1) {
                    while (timerIsRunning) {
                        elapsedTime = System.currentTimeMillis() - startTime
                        delay(50)
                    }
                }
                minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime)
                seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60
                centSeconds = TimeUnit.MILLISECONDS.toMillis(
                    elapsedTime - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(
                        seconds
                    )
                )
                Text(
                    text = stringResource(id = R.string.elapsed_time) + " " + minutes.toZeroTrailedString() + ":" + seconds.toZeroTrailedString() + "." + "%02d".format(
                        centSeconds / 10
                    ), color = background_text
                )

                Text(
                    text = stringResource(R.string.attempts) + " " + attempts + "/" + controller.getMaxAttempts(),
                    color = background_text
                )
            }

        }

        //sequenza nascosta + distanza + barra della sequenza appena inserita
        SequenceBar(sequence, guessedIndexes = guessedIndexes)
        DistanceBar(text = (if (userInput.isNotEmpty()) distance else stringResource(R.string.unknown_distance)))
        InputBar(sequence = userInput, hideIndexes = guessedIndexes, blurred = false)

        //la cronologia delle sequenze inserite dall'utente viene mostrata in una lazycolumn
        LazyColumn(
            modifier = Modifier
                .padding(generalPadding)
                .weight(fullWeight)
                .fillMaxWidth()
                .border(borderSize, cells_background)
                .background(background)
        ) {

            item {
                Text(
                    text = " " + stringResource(R.string.seq_chronology),
                    color = background_text,
                    modifier = Modifier.padding(generalPadding)
                )
            }

            previousInputSequences.forEach {
                item {
                    InputBar(sequence = it, hideIndexes = listOf(), blurred = true)
                }
            }

        }

        //barra dell'input
        Row(
            modifier = Modifier
                .padding(generalPadding)
                .fillMaxWidth()
                .background(background),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 0..8) {
                if (guessedIndexes.contains(i)) {
                    /*
                    se l'indice a cui fa riferimento questa casella corrisponde a un simbolo indovinato si sostituisce la casella con
                    uno spacer di dimensione tale da coprire l'intera grandezza (padding compreso)
                    */
                    Spacer(modifier = Modifier.size(hideIndexSpacerSize))
                } else {
                    /*
                    ogni casella dell'input è un box cliccabile. Si mantiene l'indice della casella selezionata, che viene aggiornato
                    in base al click del relativo box. Tutti i simboli inseriti dall'utente sono salvati in un CharArray (liveInput)
                    e il box conterrà il simbolo relativo al proprio indice
                     */
                    Box(
                        modifier = Modifier
                            .padding(generalPadding)
                            .size(cellSize)
                            .border(
                                if (selectedInputCell != i) borderSize else generalPadding,
                                if (selectedInputCell != i) black else selected_cell,
                                RoundedCornerShape(cornerDpShape)
                            )
                            .background(cells_background, RoundedCornerShape(cornerDpShape))
                            .clickable {
                                selectedInputCell = i
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(liveInput[i].toString(), color = black)
                    }
                }
            }
        }


        //tastiera
        Row(
            modifier = Modifier
                .padding(generalPadding)
                .background(background)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 0..8) {
                if (!liveInput.contains(charsExtracted[i]) && !guessedChars.contains(charsExtracted[i])) {

                    /*
                    In questo caso si mostra la casella del simbolo solo se questo non è stato ancora inserito (non in liveInput)
                    e se il carattere non è stato ancora indovinato
                     */
                    Box(
                        modifier = Modifier
                            .padding(generalPadding)
                            .size(cellSize)
                            .border(borderSize, black, RoundedCornerShape(cornerDpShape))
                            .background(cells_background, RoundedCornerShape(cornerDpShape))
                            .clickable {

                                /*
                                al click, la casella deve sparire e il simbolo che conteneva deve essere inserito nella casella selezionata
                                nella barra dell'input
                                 */
                                liveInput[selectedInputCell] = charsExtracted[i]

                                //si sposta il focus alla prima casella che non corrisponde ad un indice di un simbolo indovinato
                                val myIndex = selectedInputCell
                                for (idx in myIndex + 1..8) {
                                    if (!guessedIndexes.contains(idx)) {
                                        selectedInputCell = idx
                                        break
                                    }
                                }

                                //in questo modo si forza la ricomposizione nel caso in cui la casella selezionata sia l'ultima disponibile
                                selectedInputCell += 1
                                selectedInputCell -= 1

                                /*
                                Se si arriva alla fine della barra dell'input si procede in questo modo: se l'ultima casella è vuota ci si rimane,
                                altrimenti si pone il focus alla prima casella vuota rimasta. Se non c'è nessuna casella vuota (else if), si pone il focus alla prima casella disponibile
                                */
                                if (selectedInputCell == 8 && liveInput.indexOf('\u0000') != -1 && liveInput[selectedInputCell] != '\u0000') {
                                    selectedInputCell = liveInput.indexOf('\u0000')
                                } else if (selectedInputCell == 8 && liveInput.indexOf('\u0000') == -1) {
                                    selectedInputCell = liveInput.indexOfFirst { icon ->
                                        !guessedIndexes.contains(sequence.indexOf(icon))
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(charsExtracted[i].toString(), color = black)
                    }
                } else {
                    //se il simbolo è stato inserito o il carattere è già stato indovinato si sostituisce la casella con uno spacer
                    Spacer(modifier = Modifier.size(hideIndexSpacerSize))
                }
            }
        }

        Spacer(modifier = Modifier.size(30.dp))

        //bottoni cancel e confirm
        Row(
            modifier = Modifier
                .padding(cornerDpShape)
                .background(background)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StyledButton(
                onClick = {
                    if (liveInput.isNotEmpty()) {
                        if (liveInput[selectedInputCell] != '\u0000') {
                            /*
                            se il tasto "cancel" viene premuto quando il focus si trova su una casella dell'input piena, si svuota
                            la casella e si mantiene il focus su di essa (forzando la ricomposizione sommando e sottraendo 1 al focus)
                             */
                            liveInput[selectedInputCell] = '\u0000'
                            selectedInputCell += 1
                            selectedInputCell -= 1
                        } else {
                            /*
                            se invece la casella in cui si trova il focus è vuota, si sposta il focus alla prima casella corrispondente
                            a un simbolo non ancora indovinato, procedendo dunque a svuotarla
                             */
                            val myIndex = selectedInputCell
                            for (i in myIndex - 1 downTo 0) {
                                if (!guessedIndexes.contains(i)) {
                                    selectedInputCell = i
                                    break
                                }
                            }
                            liveInput[selectedInputCell] = '\u0000'
                        }
                    }
                },
                text = stringResource(id = R.string.cancel)
            )

            StyledButton(
                onClick = {
                    if (controller.isGuessed()) return@StyledButton //pulsante disabilitato se la sequenza è indovinata

                    if (liveInput.contains('\u0000')) {
                        //se mancano dei caratteri si mostra il messaggio di errore
                        confirmationNotAllowed = true
                    } else {
                        attempts += 1

                        //al primo tentativo parte il timer
                        if (attempts == 1) {
                            startTime = System.currentTimeMillis()
                            timerIsRunning = true
                        }

                        //se sono già state inserite almeno due sequenze si comincia a mostrare le precedenti nella lazycolumn al centro della schermata
                        if (attempts >= 2) previousInputSequences =
                            previousInputSequences.plus(userInput)

                        //si registra l'input definitivo e si calola la distanza
                        userInput = ""
                        distance = ""
                        liveInput.forEach {
                            userInput += it
                        }

                        distance = controller.calculateDistance(sequence, userInput)

                        //si scorre la distanza alla ricerca di caratteri indovinati (per renderli visibili nella sequenza e per non mostrarli tra gli input possibili)
                        distance.forEachIndexed { index, c ->
                            if (c.digitToInt() == 0) {
                                guessedIndexes = guessedIndexes.plus(index)
                                guessedChars = guessedChars.plus(sequence[index])
                            }
                        }

                        //focus sulla prima casella relativa a un simbolo non ancora indovinato
                        selectedInputCell =
                            liveInput.indexOfFirst { !guessedChars.contains(it) }.or(0)

                        //la barra dell'input viene svuotata negli indici dei simboli da indovinare
                        liveInput.forEachIndexed { index, c ->
                            if (!guessedChars.contains(c)) liveInput[index] = '\u0000'

                        }

                    }
                },
                text = stringResource(id = R.string.confirm)
            )
        }

    }

}