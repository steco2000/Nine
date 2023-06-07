package com.colamartini.nine.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.colamartini.nine.model.Game

//database singleton room

@Database(entities = [Game::class], version = 1)
abstract class GameDB: RoomDatabase() {
    companion object{

        private var db: GameDB? = null

        fun getInstance(context: Context): GameDB{
            if(db == null){
                db = Room.databaseBuilder(
                    context,
                    GameDB::class.java,
                    "nine_db"
                )
                    .build()    //in questo caso non è necessario costruire il db da asset, dato che non si devono precaricare dati
            }
            return db as GameDB
        }
    }
    abstract fun getDAO(): GameDAO
}