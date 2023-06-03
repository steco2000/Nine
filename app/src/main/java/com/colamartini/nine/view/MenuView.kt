package com.colamartini.nine.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.colamartini.nine.R
import com.colamartini.nine.ui.theme.*
import com.colamartini.nine.widgets.LogoView
import com.colamartini.nine.widgets.StyledButton

@Composable
fun MenuView(navController: NavController){

    BackHandler(enabled = true, onBack = { navController.navigate("menu_view") })

    ConstraintLayout (
        modifier = Modifier
            .fillMaxSize()
            ) {
        val (logo, play, scores) = createRefs()

        LogoView(
            size = 200.dp,
            modifier = Modifier
                .constrainAs(logo){
                    top.linkTo(parent.top, margin = menuLogoTopMargin)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        StyledButton(
            onClick = {
                navController.navigate("difficulty_view")
            },
            text = stringResource(R.string.play),
            modifier = Modifier
                .constrainAs(play){
                    top.linkTo(logo.bottom, margin = menuButtonLogoMargin)
                    start.linkTo(logo.start)
                    end.linkTo(logo.end)
                }
        )

        StyledButton(
            onClick = {
                navController.navigate("scores_view")
            },
            text = stringResource(R.string.scores),
            modifier = Modifier
                .constrainAs(scores){
                    top.linkTo(play.bottom, margin = menuItemsMargin)
                    start.linkTo(play.start)
                    end.linkTo(play.end)
                }
        )
    }

}