package com.colamartini.nine.db

import com.colamartini.nine.model.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DBRepository(private val dao: GameDAO) {

    fun insertGame(game: Game){
        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(game)
        }
    }

    fun getEasyBestTime(): Game? {
        return dao.getEasyBestTime()
    }

    fun getAllEasyGames(): List<Game> {
        return dao.getEasyGames()
    }

}