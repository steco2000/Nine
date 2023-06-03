package com.colamartini.nine.beans

data class GameBean(
    var difficulty: Int,
    var time: Long,
    var date: String = "",
    var datetime: String = "",
    var attempts: Int,
    var win: Boolean,
)
