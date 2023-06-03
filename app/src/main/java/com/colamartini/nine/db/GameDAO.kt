package com.colamartini.nine.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.colamartini.nine.model.Game

@Dao
interface GameDAO {

    @Insert
    fun insert(game: Game)

    //todo riprendi da qui
    @Query("select * from Game where time = (select Min(time) from Game where win = 1) and difficulty = 0")
    fun getEasyBestTime(): Game?

    @Query("select * from Game where difficulty = 0")
    fun getEasyGames(): List<Game>

    @Query("select * from Game where difficulty = 1")
    fun getMediumGames(): List<Game>

    @Query("select * from Game where difficulty = 2")
    fun getHardGames(): List<Game>

}