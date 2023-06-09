package com.colamartini.nine.control

//controller applicativo del gioco

class NineController {

    var difficulty: Int = 2
    private var attempts = 0
    private var sequenceIsGuessed = false
    private var maxAttempts = 2
    private var gameLost = false

    //set dei simboli estraibili dall'app
    companion object{
        const val symbols = "ღ•⁂€∞▲●☀☁☂☃★☆☐☎☢☣☮☯☸☼☽☾♚♛♜♝♞♟♨♩♪♫♬✈✉✿❀❁❄❦♣♦♥♠֍$♓♒♑♐♏♎♍♌♋♊♉♈0123456789QWERTYUIOPASDFGHJKLZXCVBNM"
    }

    //il seguente metodo serve settare la difficoltà della partita, vengono inoltre resettate le variabili relative alla partita
    fun setGameDifficulty(difficulty: Int){
        this.difficulty = difficulty
        this.attempts = 0
        sequenceIsGuessed = false
        maxAttempts = if(difficulty == 0) 4 else if(difficulty == 1) 3 else 2
    }

    //il seguente metodo aggiorna la sequenza, ad esempio al restart di una partita o all'avvio del gioco dalla schermata del selettore della difficoltà
    fun refreshSequence(): String{
        val chars = symbols.toList().shuffled().take(9).joinToString("")
        attempts = 0
        sequenceIsGuessed = false
        gameLost = false
        return String(chars.toCharArray())
    }

    //metodo per accedere al massimo numero di tentativi consentiti per la partita corrente
    fun getMaxAttempts(): Int{
        return maxAttempts
    }

    //i seguenti due metodi servono all'intergfaccia per capire se la partita è stata vinta o persa
    fun isGuessed(): Boolean { return sequenceIsGuessed }
    fun gameIsLost(): Boolean { return gameLost }

    /*
    il seguente metodo calcola la distanza tra i simboli inseriti dall'utente e la sequenza estratta,
    controllando se la sequenza è stata indovinata o se la partita è persa
     */
    fun calculateDistance(sequence: String, userInput: String): String{
        var distance = ""
        var charIndex = 0
        var absDistance: Int

        attempts++

        /*
        per ogni simbolo dell'input si tiene da parte l'indice e si cerca linearmente l'equivalente nella sequenza dell'app.
        Quando viene trovato, si salva l'indice di quello della sequenza, e si effettua la differeza tra questo e l'indice del simbolo
        nell'input. Fatto ciò si prende il valore assoluto della differenza
        */
        userInput.forEachIndexed {idxIn ,inChar ->
            sequence.forEachIndexed{ i, seqChar ->
                if(seqChar == inChar){
                    charIndex = i
                    return@forEachIndexed
                }
            }

            absDistance = kotlin.math.abs((charIndex - idxIn))

            /*
            se il valore assoluto è <= 4 vuol dire che quella che abbiamo trovato è la distanza minima, quindi si può aggiungere
            all'array delle distanze che verrà poi mostrato all'utente. Se invece la distanza trovata è almeno 5 significa che non è
            minima (dato che la sequenza è lunga 9 caratteri). Per trovare la minima basta quindi sottrarre quella trovata alla
            lunghezza della sequenza
             */
            distance += if(absDistance <= 4) "$absDistance" else "${sequence.length - absDistance}"
        }

        //in base all'array ottenuto si controlla se la partita è vinta o persa
        if(distance == "000000000") sequenceIsGuessed = true
        gameLost = attempts >= maxAttempts && !sequenceIsGuessed

        return distance
    }

}

//funzione per ottenere una stringa da un long per il timer. Se il numero è minore di 10 la stringa avrà uno zero davanti
fun Long.toZeroTrailedString(): String{
    return if(this < 10) "0$this"
    else "$this"
}