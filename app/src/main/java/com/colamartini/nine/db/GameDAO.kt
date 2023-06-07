package com.colamartini.nine.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.colamartini.nine.model.Game

@Dao
interface GameDAO {

    @Insert
    fun insert(game: Game)

    @Query("select * from Game where time = (select Min(time) from Game where win = 1 and difficulty = 0) and difficulty = 0")
    fun getEasyBestTime(): Game?

    @Query("select * from Game where time = (select Min(time) from Game where win = 1 and difficulty = 1) and difficulty = 1")
    fun getMediumBestTime(): Game?

    @Query("select * from Game where time = (select Min(time) from Game where win = 1 and difficulty = 2) and difficulty = 2")
    fun getHardBestTime(): Game?

    @Query("select * from Game where difficulty = 0 order by id DESC")
    fun getEasyGames(): List<Game>

    @Query("select * from Game where difficulty = 1 order by id DESC")
    fun getMediumGames(): List<Game>

    @Query("select * from Game where difficulty = 2 order by id DESC")
    fun getHardGames(): List<Game>

}