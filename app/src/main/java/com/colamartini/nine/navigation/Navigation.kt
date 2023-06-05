package com.colamartini.nine.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.colamartini.nine.view.DifficultyView
import com.colamartini.nine.view.InGameView
import com.colamartini.nine.view.MenuView
import com.colamartini.nine.view.ScoresView

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MenuView.route){
        composable(route = Screen.MenuView.route){
            MenuView(navController)
        }
        composable(route = Screen.DifficultyView.route){
            DifficultyView(navController)
        }
        composable(
            route = Screen.InGameView.route + "/{difficulty}",
            arguments = listOf(
                navArgument("difficulty"){
                    type = NavType.IntType
                    defaultValue = 2
                }
            )
        ){ entry ->
            InGameView(difficulty = entry.arguments!!.getInt("difficulty"), navController)
        }
        composable(
            route = Screen.ScoresView.route + "/{difficulty}",
            arguments = listOf(
                navArgument("difficulty"){
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ){ entry ->
            ScoresView(navController, difficulty = entry.arguments!!.getInt("difficulty"))
        }
    }
}