package com.colamartini.nine.view

import android.annotation.SuppressLint
import android.os.SystemClock.sleep
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.colamartini.nine.R
import com.colamartini.nine.beans.GameBean
import com.colamartini.nine.control.GamePersistenceController
import com.colamartini.nine.ui.theme.*
import com.colamartini.nine.widgets.LogoView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter

var persistenceController: GamePersistenceController? = null

@SuppressLint("CoroutineCreationDuringComposition", "SimpleDateFormat")
@Composable
fun ScoresView(navController: NavController) {

    if (persistenceController == null) {
        persistenceController = GamePersistenceController(LocalContext.current)
    }

    val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

    var bestGame: GameBean? = null
    var gamesList: List<GameBean>? = null

    val bgJob = CoroutineScope(Dispatchers.IO).launch {
        bestGame = persistenceController!!.getBestTime(0)   //todo: perfeziona gestione difficoltà
        gamesList = persistenceController!!.getGamesByDifficulty(0)
    }

    runBlocking {
        bgJob.join()
    }

    Column(
        modifier = Modifier
            .padding(generalPadding)
            .fillMaxWidth()
            .background(background)
    ) {

        Row(
            modifier = Modifier
                .padding(generalPadding)
                .fillMaxWidth()
                .background(background),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LogoView(size = internalLogoSize)
            Text(text = stringResource(id = R.string.scores), color = background_text)
        }

        
        //riga del record
        Row(
            modifier = Modifier
                .padding(generalPadding)
                .fillMaxWidth()
                .background(background)
                .border(
                    width = borderSize,
                    shape = RoundedCornerShape(cornerDpShape),
                    color = gold
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(generalPadding),
            ) {
                Text(
                    stringResource(R.string.best_game),
                    color = background_text,
                    modifier = Modifier.padding(generalPadding)
                )

                Row(
                    modifier = Modifier
                        .padding(generalPadding)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .padding(generalPadding)
                    ) {
                        val datetime = LocalTime.parse(bestGame?.datetime)
                        Text(text = bestGame?.date ?: "", color = background_text)
                        Text(text = timeFormat.format(datetime) ?: "", color = background_text)
                    }
                    Column(
                        modifier = Modifier
                            .padding(generalPadding)
                    ) {
                        Text(
                            text = stringResource(id = R.string.time) + " " + if (bestGame != null) "${bestGame!!.time}" else "",
                            color = background_text
                        )
                        Text(
                            text = stringResource(id = R.string.attempts) + " " + if (bestGame != null) "${bestGame!!.attempts}" else "",
                            color = background_text
                        )
                    }
                }
            }
        }
        
        LazyColumn(modifier = Modifier
            .padding(generalPadding)
            .weight(fullWeight)
            .fillMaxWidth()
            .border(borderSize, cells_background)
            .background(background)
        ) {

            gamesList?.forEach {
                item {
                    Row(
                        modifier = Modifier
                            .padding(generalPadding)
                            .fillMaxWidth()
                            .background(background)
                            .border(
                                width = borderSize,
                                shape = RoundedCornerShape(cornerDpShape),
                                color = if(it.win) ok_cell else no_cell
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(generalPadding)
                        ) {
                            val datetime = LocalTime.parse(it.datetime)
                            Text(text = it.date ?: "", color = background_text)
                            Text(text = timeFormat.format(datetime) ?: "", color = background_text)
                        }
                        Column(
                            modifier = Modifier
                                .padding(generalPadding)
                        ) {
                            Text(
                                text = stringResource(id = R.string.time) + " " + if (it != null) "${it.time}" else "",
                                color = background_text
                            )
                            Text(
                                text = stringResource(id = R.string.attempts) + " " + if (it != null) "${it.attempts}" else "",
                                color = background_text
                            )
                        }
                    }
                }
            }
        }

    }

}