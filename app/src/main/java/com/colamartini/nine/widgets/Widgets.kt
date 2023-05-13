package com.colamartini.nine.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.colamartini.nine.R
import com.colamartini.nine.ui.theme.*

@Composable
fun InputBar(sequence: String, hideIndexes: List<Int>, blurred: Boolean) {
    Row (
        modifier = Modifier
            .padding(generalPadding)
            .fillMaxWidth()
            .alpha(if(blurred) halfWeight else fullWeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
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

}

@Composable
fun SequenceBar(text: String, guessedIndexes: List<Int>) {
    var borderColor: Color

    Row (
        modifier = Modifier
            .padding(generalPadding)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
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
                Text(if(guessedIndexes.contains(i)) char.toString() else stringResource(R.string.unguessed_letter), color = black)
            }
        }
    }
}

@Composable
fun DistanceBar(text: String) {
    Row(
        modifier = Modifier
            .padding(generalPadding)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        text.forEach {char ->
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

@Composable
fun LogoView(){
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "",
        modifier = Modifier
            //.size(150.dp)
            .size(100.dp)
    )
}

@Composable
fun StyledButton(onClick: () -> Unit, text: String){
    Button(
        modifier = Modifier
            .padding(generalPadding),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = cells_background
        ),
        onClick = { onClick() }
    ) {
        Text(text = text, color = black)
    }
}