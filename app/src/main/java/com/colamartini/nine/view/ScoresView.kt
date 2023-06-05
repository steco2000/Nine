package com.colamartini.nine.view

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
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
import com.colamartini.nine.control.toZeroTrailedString
import com.colamartini.nine.navigation.Screen
import com.colamartini.nine.ui.theme.*
import com.colamartini.nine.widgets.LogoView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

var persistenceController: GamePersistenceController? = null

@SuppressLint("CoroutineCreationDuringComposition", "SimpleDateFormat")
@Composable
fun ScoresView(navController: NavController, difficulty: Int) {

    BackHandler(enabled = true, onBack = { navController.navigate("menu_view") })
    
    if (persistenceController == null) {
        persistenceController = GamePersistenceController(LocalContext.current)
    }

    val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

    var bestGame: GameBean? = null
    var gamesList: List<GameBean>? = null

    val bgJob = CoroutineScope(Dispatchers.IO).launch {
        bestGame = persistenceController!!.getBestTime(difficulty)
        gamesList = persistenceController!!.getGamesByDifficulty(difficulty)
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
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(generalPadding),
            ) {
                if (bestGame != null) {
                    Text(
                        stringResource(R.string.best_game),
                        color = background_text,
                        modifier = Modifier.padding(generalPadding)
                    )

                    Row(
                        modifier = Modifier
                            .padding(generalPadding)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(generalPadding)
                        ) {
                            val datetime = LocalTime.parse(bestGame!!.datetime)
                            Text(text = bestGame!!.date, color = background_text)
                            Text(text = timeFormat.format(datetime), color = background_text)
                        }
                        Column(
                            modifier = Modifier
                                .padding(generalPadding)
                        ) {
                            val minutes = TimeUnit.MILLISECONDS.toMinutes(bestGame!!.time)
                            val seconds = TimeUnit.MILLISECONDS.toSeconds(bestGame!!.time) % 60
                            val centSeconds = TimeUnit.MILLISECONDS.toMillis(
                                bestGame!!.time - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(
                                    seconds
                                )
                            )
                            Text(
                                text = stringResource(id = R.string.time) + " " + minutes.toZeroTrailedString() + ":" + seconds.toZeroTrailedString() + "." + "%02d".format(
                                    centSeconds / 10
                                ),
                                color = background_text
                            )
                            Text(
                                text = stringResource(id = R.string.attempts) + " " + "${bestGame!!.attempts}",
                                color = background_text
                            )
                        }
                    }
                }else Text(text = stringResource(R.string.never_won_game), color = background_text)
            }
        }
        
        LazyColumn(modifier = Modifier
            .padding(generalPadding)
            .weight(fullWeight)
            .fillMaxWidth()
            .border(borderSize, cells_background)
            .background(background)
        ) {

            item{
                Spacer(modifier = Modifier.size(generalPadding))
            }

            gamesList?.forEach {
                item {
                    Row(
                        modifier = Modifier
                            .padding(vertical = generalPadding, horizontal = cornerDpShape)
                            .fillMaxWidth()
                            .background(background)
                            .border(
                                width = borderSize,
                                shape = RoundedCornerShape(cornerDpShape),
                                color = if (it.win) ok_cell else no_cell
                            ),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(generalPadding)
                        ) {
                            val datetime = LocalTime.parse(it.datetime)
                            Text(text = it.date, color = background_text)
                            Text(text = timeFormat.format(datetime) ?: "", color = background_text)
                        }
                        Column(
                            modifier = Modifier
                                .padding(generalPadding)
                        ) {
                            val minutes = TimeUnit.MILLISECONDS.toMinutes(it.time)
                            val seconds = TimeUnit.MILLISECONDS.toSeconds(it.time) % 60
                            val centSeconds = TimeUnit.MILLISECONDS.toMillis(
                                it.time - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(
                                    seconds
                                )
                            )

                            Text(
                                text = stringResource(id = R.string.time) + " " + minutes.toZeroTrailedString() + ":" + seconds.toZeroTrailedString() + "." + "%02d".format(
                                    centSeconds / 10
                                ),
                                color = background_text
                            )
                            Text(
                                text = stringResource(id = R.string.attempts) + " " + "${it.attempts}",
                                color = background_text
                            )
                        }
                    }
                }
            }
        }

        BottomNavigation (
            modifier = Modifier
                .padding(generalPadding),
            backgroundColor = background
        ){
            BottomNavigationItem(
                modifier = Modifier
                    .border(borderSize, cells_background)
                    .background(if (difficulty == 0) cells_background else background),
                selected = difficulty == 0,
                onClick = {
                    if(difficulty != 0){
                        navController.navigate(Screen.ScoresView.withArgs("0"))
                    }
                },
                icon = {
                    Text(text = stringResource(id = R.string.easy), color = if(difficulty == 0) background else cells_background)
                }
            )
            BottomNavigationItem(
                modifier = Modifier
                    .border(borderSize, cells_background)
                    .background(if (difficulty == 1) cells_background else background),
                selected = difficulty == 1,
                onClick = {
                    if(difficulty != 1){
                        navController.navigate(Screen.ScoresView.withArgs("1"))
                    }
                },
                icon = {
                    Text(text = stringResource(id = R.string.medium), color = if(difficulty == 1) background else cells_background)
                }
            )
            BottomNavigationItem(
                modifier = Modifier
                    .border(borderSize, cells_background)
                    .background(if (difficulty == 2) cells_background else background),
                selected = difficulty == 2,
                onClick = {
                    if(difficulty != 2){
                        navController.navigate(Screen.ScoresView.withArgs("2"))
                    }
                },
                icon = {
                    Text(text = stringResource(id = R.string.hard), color = if(difficulty == 2) background else cells_background)
                }
            )
        }

    }

}