package se.umu.cs.peer0019.thirty

import kotlin.random.Random

class Thirty {
    private var gradingSetting: Integer? = null
    private var score: Int = 0
    var dice1: Int? = null
        private set
    var dice2: Int? = null
        private set
    var dice3: Int? = null
        private set
    var dice4: Int? = null
        private set
    var dice5: Int? = null
        private set
    var dice6: Int? = null
        private set

    fun throwDice() {
        dice1 = Random.nextInt(1, 6)
        dice2 = Random.nextInt(1, 6)
        dice3 = Random.nextInt(1, 6)
        dice4 = Random.nextInt(1, 6)
        dice5 = Random.nextInt(1, 6)
        dice6 = Random.nextInt(1, 6)
    }
    fun setGradingSettings() {

    }
}