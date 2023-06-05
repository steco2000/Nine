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

    fun getMediumBestTime(): Game? {
        return dao.getMediumBestTime()
    }

    fun getHardBestTime(): Game? {
        return dao.getHardBestTime()
    }

    fun getAllEasyGames(): List<Game> {
        return dao.getEasyGames()
    }

    fun getAllMediumGames(): List<Game> {
        return dao.getMediumGames()
    }

    fun getAllHardGames(): List<Game> {
        return dao.getHardGames()
    }

}