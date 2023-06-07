package com.colamartini.nine.beans

//bean per incapsulare i dati della entity "Game", in modo da separare il modello dall'interfaccia

data class GameBean(
    var difficulty: Int,
    var time: Long,
    var date: String = "",
    var datetime: String = "",
    var attempts: Int,
    var win: Boolean,
)
