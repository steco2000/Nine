package com.colamartini.nine.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Game")
data class Game(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "difficulty")
    var difficulty: Int,
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "datetime")
    var datetime: String,
    @ColumnInfo(name = "win")
    var win: Int,
    @ColumnInfo(name = "time")
    var time: Long,
    @ColumnInfo(name = "attempts")
    var attempts: Int
)
