package com.colamartini.nine.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.colamartini.nine.R
import com.colamartini.nine.ui.theme.*

//libreria di widget vari per l'interfaccia


/*
barra utilizzata per mostrare la sequenza appena passata in input dall'utente. In ingresso, oltre alla sequenza, vengono passati gli
indici delle casella da nascondere (ad esempio se un carattere è stato indovinato) e il booleano blurred se vogliamo mostrare le caselle
"sfocate" (come nella lazycolumn delle sequenze inserite precedentemente)
 */
@Composable
fun InputBar(sequence: String, hideIndexes: List<Int>, blurred: Boolean) {
    Row(
        modifier = Modifier
            .padding(generalPadding)
            .fillMaxWidth()
            .alpha(if (blurred) halfWeight else fullWeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val text: String = sequence.ifEmpty {
            stringResource(id = R.string.unknown_distance)
        }
        text.forEachIndexed { i, char ->
            if (hideIndexes.contains(i)) {
                Spacer(modifier = Modifier.size(hideIndexSpacerSize))
            } else {
                Box(
                    modifier = Modifier
                        .padding(generalPadding)
                        .size(if (blurred) blurredCellSize else cellSize)
                        .border(borderSize, black, RoundedCornerShape(cornerDpShape))
                        .background(cells_background, RoundedCornerShape(cornerDpShape)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(char.toString(), color = black)
                }
            }
        }
    }

}


/*
barra della sequenza. Simile alla barra dell'input, solo che in questo caso vengono passati gli indici dei simboli indovinati. In questo
modo se una determinata casella corrisponde ad un simbolo già indovinato viene colorata di verde, altrimenti di rosso
 */
@Composable
fun SequenceBar(text: String, guessedIndexes: List<Int>) {
    var borderColor: Color

    Row(
        modifier = Modifier
            .padding(generalPadding)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        text.forEachIndexed { i, char ->
            borderColor = if (guessedIndexes.contains(i)) ok_cell else no_cell
            Box(
                modifier = Modifier
                    .padding(generalPadding)
                    .size(cellSize)
                    .border(borderSize, borderColor, RoundedCornerShape(cornerDpShape))
                    .background(cells_background, RoundedCornerShape(cornerDpShape)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (guessedIndexes.contains(i)) char.toString() else stringResource(R.string.unguessed_letter),
                    color = black
                )
            }
        }
    }
}


//la barra della distanza è simile alle altre due ma prende in input solo il testo
@Composable
fun DistanceBar(text: String) {
    Row(
        modifier = Modifier
            .padding(generalPadding)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        text.forEach { char ->
            Box(
                modifier = Modifier
                    .padding(generalPadding)
                    .size(cellSize)
                    .border(borderSize, black, RoundedCornerShape(cornerDpShape))
                    .background(cells_background, RoundedCornerShape(cornerDpShape)),
                contentAlignment = Alignment.Center
            ) {
                Text(char.toString(), color = black)
            }
        }
    }
}


//composable per mostrare il logo dell'app, passando una certa size in dp e eventualmente un modifier
@Composable
fun LogoView(size: Dp, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "",
        modifier = modifier
            .size(size)
    )
}


//bottone standard usato in tutta l'applicazione
@Composable
fun StyledButton(onClick: () -> Unit, text: String, modifier: Modifier = Modifier) {
    Button(
        modifier = modifier
            .padding(generalPadding)
            .width(buttonWidth)
            .height(buttonHeight),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = cells_background
        ),
        onClick = { onClick() }
    ) {
        Text(text = text, color = background)
    }
}


/*
alert dialog per mostrare semplici messaggi all'utente. È presente solo il tasto "OK" con la possibilità di passare una unit da eseguire
alla pressione
 */
@Composable
fun GeneralInfoAlertDialog(title: String, text: String, onDismissRequest: () -> Unit) {
    AlertDialog(
        backgroundColor = background,
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = title, color = background_text) },
        text = {
            Text(
                text = text,
                color = background_text
            )
        },
        confirmButton = {
            StyledButton(
                onClick = { onDismissRequest() },
                text = stringResource(R.string.ok)
            )
        })
}

/*
alert dialog per fare una domanda all'utente o per fornire due opzioni quando viene mostrato. È possibile passare due unit, una
per la conferma e una per il dismiss. Di default i due tasti saranno "Si" e "No", eventualmente però c'è la possibilità di
passare un testo alternativo
 */
@Composable
fun QuestionAlertDialog(
    title: String,
    text: String,
    onDismissRequest: () -> Unit,
    onAcceptRequest: () -> Unit,
    confirmButtonText: String = stringResource(id = R.string.yes),
    dismissButtonText: String = stringResource(id = R.string.no)
) {
    AlertDialog(
        backgroundColor = background,
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = title, color = background_text) },
        text = {
            Text(
                text = text,
                color = background_text
            )
        },
        confirmButton = {
            StyledButton(
                onClick = {
                    onAcceptRequest()
                },
                text = confirmButtonText
            )
        },
        dismissButton = {
            StyledButton(
                onClick = {
                    onDismissRequest()
                },
                text = dismissButtonText
            )
        })
}