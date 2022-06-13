package se.umu.cs.peer0019.thirty

import kotlin.random.Random

class Thirty {
    var gradingSetting: Int? = null
    var score: Int = 0
        private set
    var stopped: Boolean = false
        private set
    var round: Int? = null
        private set
    var currentThrow: Int? = null
        private set
    var dices = mutableListOf<Int?>(
        null,
        null,
        null,
        null,
        null,
        null
    )
        private set
    var pickedDices = mutableListOf<Int>()

    fun throwDice() {
        if (stopped) {
            return
        }
        if (currentThrow == 2) {
            stopped = true
        }
        pickedDices = mutableListOf()
        currentThrow = currentThrow?.inc() ?: 1
        dices[0] = Random.nextInt(1, 6)
        dices[1] = Random.nextInt(1, 6)
        dices[2] = Random.nextInt(1, 6)
        dices[3] = Random.nextInt(1, 6)
        dices[4] = Random.nextInt(1, 6)
        dices[5] = Random.nextInt(1, 6)
    }
    fun startGame() {
        round = 1
    }
}