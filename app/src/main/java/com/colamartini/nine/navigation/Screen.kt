package com.colamartini.nine.navigation

//questa classe serve a chiamare, attraverso il navController, le varie interfacce dell'applicazione
sealed class Screen(val route: String){
    object MenuView : Screen("menu_view")
    object DifficultyView : Screen("difficulty_view")
    object InGameView : Screen("in_game_view")
    object ScoresView : Screen("scores_view")

    //questo metodo permette di passare argomenti alla composable chiamata. Tutto ciò che viene passato viene sommato alla route della schermata
    fun withArgs(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
