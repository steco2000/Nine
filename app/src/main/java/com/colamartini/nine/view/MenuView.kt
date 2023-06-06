package com.colamartini.nine.view

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.colamartini.nine.R
import com.colamartini.nine.navigation.Screen
import com.colamartini.nine.ui.theme.menuItemsMargin
import com.colamartini.nine.ui.theme.menuLogoSize
import com.colamartini.nine.ui.theme.menuLogoTopMargin
import com.colamartini.nine.widgets.LogoView
import com.colamartini.nine.widgets.StyledButton


//schermata del menu

@Composable
fun MenuView(navController: NavController){

    val context = LocalContext.current

    //alla pressione del tasto back si chiude l'app tornando alla home
    BackHandler(enabled = true, onBack = {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(context, a, null)
    })

    ConstraintLayout (
        modifier = Modifier
            .fillMaxSize()
            ) {
        val (logo, play, scores) = createRefs()

        LogoView(
            size = menuLogoSize,
            modifier = Modifier
                .constrainAs(logo){
                    top.linkTo(parent.top, margin = menuLogoTopMargin)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        //alla pressione di uno dei due tasti si naviga verso la rispettiva schermata grazie al navController

        StyledButton(
            onClick = {
                navController.navigate("difficulty_view")
            },
            text = stringResource(R.string.play),
            modifier = Modifier
                .constrainAs(play){
                    top.linkTo(parent.top)
                    start.linkTo(logo.start)
                    end.linkTo(logo.end)
                    bottom.linkTo(parent.bottom)
                }
        )

        StyledButton(
            onClick = {
                navController.navigate(Screen.ScoresView.withArgs("0"))
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