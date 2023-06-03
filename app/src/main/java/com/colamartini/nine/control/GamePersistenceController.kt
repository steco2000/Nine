package com.colamartini.nine.control

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.colamartini.nine.beans.GameBean
import com.colamartini.nine.db.DBRepository
import com.colamartini.nine.db.GameDB
import com.colamartini.nine.model.Game
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class GamePersistenceController(context: Context){

    private val gameDB = GameDB.getInstance(context)
    private val repo = DBRepository(gameDB.getDAO())

    //TODO: continua implementazione salvataggio da qui
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveGame(gameBean: GameBean){

        val game = Game(
            difficulty = gameBean.difficulty,
            date = LocalDate.now().toString(),
            datetime = LocalTime.now().toString(),
            win = if(gameBean.win) 1 else 0,
            time = gameBean.time,
            attempts = gameBean.attempts
        )

        repo.insertGame(game)

    }

    fun getBestTime(difficulty: Int): GameBean?{
        val toReturn = if(difficulty == 0) repo.getEasyBestTime() else null //todo: continua implementazione
        return if(toReturn == null) null
        else{
            GameBean(
                difficulty = toReturn.difficulty,
                time = toReturn.time,
                attempts = toReturn.attempts,
                win = toReturn.win == 1,
                date = toReturn.date,
                datetime = toReturn.datetime
            )
        }
    }

    fun getGamesByDifficulty(difficulty: Int): List<GameBean>{
        val toReturn = if(difficulty == 0) repo.getAllEasyGames() else listOf() //todo: continua implementazione
        var gameBeans: List<GameBean> = listOf()
        toReturn.forEach{
            gameBeans = gameBeans.plus(GameBean(
                difficulty = it.difficulty,
                time = it.time,
                attempts = it.attempts,
                win = it.win == 1,
                date = it.date,
                datetime = it.datetime
            ))
        }
        return gameBeans
    }

}