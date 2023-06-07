package com.colamartini.nine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.colamartini.nine.navigation.Navigation
import com.colamartini.nine.ui.theme.NineTheme
import com.colamartini.nine.ui.theme.background
import com.colamartini.nine.view.InGameView
import com.colamartini.nine.view.MenuView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NineTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = background
                ) {
                    //all'avvio dell'activity viene subito chiamata la navigation che, attraverso il navHost, caricherà la schermata home
                    Navigation()
                }
            }
        }
    }
}