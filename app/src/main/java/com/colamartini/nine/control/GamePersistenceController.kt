package com.colamartini.nine.control

import android.content.Context
import com.colamartini.nine.beans.GameBean
import com.colamartini.nine.db.DBRepository
import com.colamartini.nine.db.GameDB
import com.colamartini.nine.model.Game
import java.time.LocalDate
import java.time.LocalTime

//controller per gestire salvataggio e recupero delle informazioni dal database. Per ogni operazione da eseguire sul db viene sfruttata la repository

class GamePersistenceController(context: Context){

    private val gameDB = GameDB.getInstance(context)
    private val repo = DBRepository(gameDB.getDAO())

    //metodo per il salvataggio di una partita, per separare modello e interfaccia accetta un bean che viene convertito nella entity
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

    //metodo per recuperare la partita con il miglior tempo per difficoltà. All'interfaccia viene restituito un bean che incapsula le info recuperate
    fun getBestTime(difficulty: Int): GameBean?{
        val toReturn = if(difficulty == 0) repo.getEasyBestTime() else if(difficulty == 1) repo.getMediumBestTime() else repo.getHardBestTime()
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

    //metodo per recuperare la lista delle partite giocate per una determinata difficoltà. Anche qui i dati ottenuti vengono incapsulati in un bean
    fun getGamesByDifficulty(difficulty: Int): List<GameBean>{
        val toReturn = if(difficulty == 0) repo.getAllEasyGames() else if (difficulty == 1) repo.getAllMediumGames() else repo.getAllHardGames()
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