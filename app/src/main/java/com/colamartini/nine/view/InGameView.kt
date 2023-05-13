package com.colamartini.nine.view

import android.annotation.SuppressLint
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.colamartini.nine.R
import com.colamartini.nine.control.NineController
import com.colamartini.nine.control.toZeroTrailedString
import com.colamartini.nine.ui.theme.*
import com.colamartini.nine.widgets.*
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

val controller = NineController()

@SuppressLint("MutableCollectionMutableState")
@Composable
fun InGameView() {

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
    var liveInput by rememberSaveable {
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
    var sequenceIsGuessed by rememberSaveable {
        mutableStateOf(false)
    }
    var previousInputSequences by rememberSaveable {
        mutableStateOf(listOf<String>())
    }

    if (!sessionStarted) {
        sequence = controller.refreshSequence()
        charsExtracted = sequence.toCharArray()
        charsExtracted.shuffle()
        sessionStarted = true
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
            AlertDialog(
                backgroundColor = background,
                onDismissRequest = { confirmationNotAllowed = false },
                title = { Text(text = stringResource(R.string.warning), color = background_text) },
                text = {
                    Text(
                        text = stringResource(R.string.missing_symbols),
                        color = background_text
                    )
                },
                confirmButton = {
                    StyledButton(
                        onClick = { confirmationNotAllowed = false },
                        text = stringResource(R.string.ok)
                    )
                })
        }

        //logo e info
        Row(
            modifier = Modifier
                .padding(generalPadding)
                .fillMaxWidth()
                .background(background),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LogoView()
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
                        sequence = controller.refreshSequence()
                        charsExtracted = sequence.toCharArray()
                        charsExtracted.shuffle()
                        guessedIndexes = listOf()
                        guessedChars = CharArray(9)
                        distance = "?????????"
                        liveInput = CharArray(9)
                        userInput = ""
                        attempts = 0
                        timerIsRunning = false
                        startTime = 0
                        elapsedTime = 0
                        sequenceIsGuessed = false
                        previousInputSequences = listOf()
                        selectedInputCell = 0
                    },
                    text = stringResource(id = R.string.restart)
                )


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
                    text = stringResource(R.string.attempts) + " " + attempts,
                    color = background_text
                )
            }

        }

        //sequenza nascosta + distanza
        SequenceBar(sequence, guessedIndexes = guessedIndexes)
        DistanceBar(text = (if (userInput.isNotEmpty()) distance else stringResource(R.string.unknown_distance)))
        InputBar(sequence = userInput, hideIndexes = guessedIndexes, blurred = false)

        LazyColumn(modifier = Modifier
            .padding(generalPadding)
            .weight(fullWeight)
            .fillMaxWidth()
            .border(borderSize, cells_background)
            .background(background)
        ) {

            item{
                Text(text = " " + stringResource(R.string.seq_chronology), color = background_text, modifier = Modifier.padding(generalPadding))
            }

            previousInputSequences.forEach{
                item{
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
                    Spacer(modifier = Modifier.size(hideIndexSpacerSize))
                } else {
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
                    Box(
                        modifier = Modifier
                            .padding(generalPadding)
                            .size(cellSize)
                            .border(borderSize, black, RoundedCornerShape(cornerDpShape))
                            .background(cells_background, RoundedCornerShape(cornerDpShape))
                            .clickable {
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
                                }
                                else if (selectedInputCell == 8 && liveInput.indexOf('\u0000') == -1){
                                    selectedInputCell = liveInput.indexOfFirst { icon -> !guessedIndexes.contains(sequence.indexOf(icon)) }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(charsExtracted[i].toString(), color = black)
                    }
                } else {
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
                            liveInput[selectedInputCell] = '\u0000'
                            selectedInputCell += 1
                            selectedInputCell -= 1
                        } else {
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
                    if(sequenceIsGuessed) return@StyledButton

                    var guessedCount = 0

                    if (liveInput.contains('\u0000')) {
                        confirmationNotAllowed = true
                    } else {
                        attempts += 1
                        if (attempts == 1) {
                            startTime = System.currentTimeMillis()
                            timerIsRunning = true
                        }

                        if(attempts >= 2) previousInputSequences = previousInputSequences.plus(userInput)

                        userInput = ""
                        distance = ""
                        liveInput.forEach {
                            userInput += it
                        }

                        distance = controller.calculateDistance(sequence, userInput)

                        distance.forEachIndexed { index, c ->
                            if (c.digitToInt() == 0) {
                                guessedIndexes = guessedIndexes.plus(index)
                                guessedChars = guessedChars.plus(sequence[index])
                                guessedCount++
                            }
                        }

                        if (guessedCount == 9) {
                            sequenceIsGuessed = true
                            timerIsRunning = false
                        } else {
                            //focus sulla prima casella relativa a un simbolo non ancora indovinato
                            selectedInputCell =
                                liveInput.indexOfFirst { !guessedChars.contains(it) }.or(0)

                            //la barra dell'input viene svuotata negli indici dei simboli da indovinare
                            liveInput.forEachIndexed { index, c ->
                                if (!guessedChars.contains(c)) liveInput[index] = '\u0000'
                            }
                        }

                    }
                },
                text = stringResource(id = R.string.confirm)
            )
        }

    }

}