package com.colamartini.nine.navigation

sealed class Screen(val route: String){
    object MenuView : Screen("menu_view")
    object DifficultyView : Screen("difficulty_view")
    object InGameView : Screen("in_game_view")

    fun withArgs(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
