package com.colamartini.nine.control

class NineController {

    companion object{
        const val symbols = "ღ•⁂€∞▲●☀☁☂☃★☆☐☎☢☣☮☯☸☼☽☾♚♛♜♝♞♟♨♩♪♫♬✈✉✿❀❁❄❦♣♦♥♠֍$♓♒♑♐♏♎♍♌♋♊♉♈0123456789QWERTYUIOPASDFGHJKLZXCVBNM"
    }

    fun refreshSequence(): String{
        val chars = symbols.toList().shuffled().take(9).joinToString("")
        return String(chars.toCharArray())
    }

    fun calculateDistance(sequence: String, userInput: String): String{
        var distance = ""
        var charIndex = 0
        var absDistance: Int

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
        return distance
    }

}

fun Long.toZeroTrailedString(): String{
    return if(this < 10) "0$this"
    else "$this"
}