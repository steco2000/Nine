package com.colamartini.nine.control

class NineController {

    var difficulty: Int = 2
    var attempts = 0
    var sequenceIsGuessed = false
    private var maxAttempts = 2
    var gameLost = false

    companion object{
        const val symbols = "ღ•⁂€∞▲●☀☁☂☃★☆☐☎☢☣☮☯☸☼☽☾♚♛♜♝♞♟♨♩♪♫♬✈✉✿❀❁❄❦♣♦♥♠֍$♓♒♑♐♏♎♍♌♋♊♉♈0123456789QWERTYUIOPASDFGHJKLZXCVBNM"
    }

    fun setGameDifficulty(difficulty: Int){
        this.difficulty = difficulty
        this.attempts = 0
        sequenceIsGuessed = false
        maxAttempts = if(difficulty == 0) 4 else if(difficulty == 1) 3 else 2
    }

    fun refreshSequence(): String{
        val chars = symbols.toList().shuffled().take(9).joinToString("")
        attempts = 0
        sequenceIsGuessed = false
        gameLost = false
        return String(chars.toCharArray())
    }

    fun isGuessed(): Boolean { return sequenceIsGuessed }
    fun gameIsLost(): Boolean { return gameLost }

    fun calculateDistance(sequence: String, userInput: String): String{
        var distance = ""
        var charIndex = 0
        var absDistance: Int

        attempts++

        userInput.forEachIndexed {idxIn ,inChar ->
            sequence.forEachIndexed{ i, seqChar ->
                if(seqChar == inChar){
                    charIndex = i
                    return@forEachIndexed
                }
            }

            absDistance = kotlin.math.abs((charIndex - idxIn))

            distance += if(absDistance <= 4) "$absDistance" else "${sequence.length - absDistance}"
        }

        if(distance == "000000000") sequenceIsGuessed = true
        gameLost = attempts >= maxAttempts && !sequenceIsGuessed

        return distance
    }

}

fun Long.toZeroTrailedString(): String{
    return if(this < 10) "0$this"
    else "$this"
}