package com.colamartini.nine.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.colamartini.nine.R
import com.colamartini.nine.navigation.Screen
import com.colamartini.nine.ui.theme.*
import com.colamartini.nine.widgets.LogoView
import com.colamartini.nine.widgets.StyledButton

/*
schermata di selezione della difficoltà. A ognuna delle tre difficoltà, facile, media e difficile, è associato un id, rispettivamente 0, 1 e 2. Quando la difficoltà è impostata su facile
l'utente ha a disposizione 4 tentativi per indovinare la sequenza. Per la difficoltà media l'utente ha a disposizione 3 tentativi e invece, per difficile, ne ha a disposizione 2 (standard)
 */

@Composable
fun DifficultyView(navController: NavController){

    //alla pressione del tasto back si torna al menu
    BackHandler(enabled = true, onBack = { navController.navigate("menu_view") })

    ConstraintLayout(modifier = Modifier.fillMaxSize().padding(generalPadding)) {
        val(upbar, easyButton, mediumButton, hardButton) = createRefs()

        //in base al tasto premuto si avvia la schermata di gioco comunicandole la difficoltà scelta
        Row (
            modifier = Modifier
                .padding(generalPadding)
                .fillMaxWidth()
                .background(background)
                .constrainAs(upbar){
                                   top.linkTo(parent.top)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            LogoView(size = internalLogoSize)
            Text(text = stringResource(R.string.select_difficulty), color = background_text)
        }

        StyledButton(
            onClick = {
                navController.navigate(Screen.InGameView.withArgs("0"))
            },
            text = stringResource(R.string.easy),
            modifier = Modifier
                .constrainAs(easyButton){
                    bottom.linkTo(mediumButton.top, margin = menuItemsMargin)
                    start.linkTo(mediumButton.start)
                    end.linkTo(mediumButton.end)
                }
        )
        StyledButton(
            onClick = {
                navController.navigate(Screen.InGameView.withArgs("1"))
            },
            text = stringResource(R.string.medium),
            modifier = Modifier
                .constrainAs(mediumButton){
                    centerVerticallyTo(parent)
                    centerHorizontallyTo(parent)
                }
        )
        StyledButton(
            onClick = {
                navController.navigate(Screen.InGameView.withArgs("2"))
            },
            text = stringResource(R.string.hard),
            modifier = Modifier
                .constrainAs(hardButton){
                    top.linkTo(mediumButton.bottom, margin = menuItemsMargin)
                    start.linkTo(mediumButton.start)
                    end.linkTo(mediumButton.end)
                }
        )
    }


}