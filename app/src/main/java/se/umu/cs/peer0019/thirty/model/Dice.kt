package se.umu.cs.peer0019.thirty.model

import kotlin.random.Random

class Dice (
    var value: Int?,
    var picked: Boolean
    ) {
    fun throwDice() {
        value = Random.nextInt(1, 6)
    }
}