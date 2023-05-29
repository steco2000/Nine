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

@Composable
fun DifficultyView(navController: NavController){

    BackHandler(enabled = true, onBack = { navController.navigate("menu_view") })

    ConstraintLayout(modifier = Modifier.fillMaxSize().padding(generalPadding)) {
        val(upbar, easyButton, mediumButton, hardButton) = createRefs()

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