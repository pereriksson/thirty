package se.umu.cs.peer0019.thirty.model

import android.os.Parcel
import android.os.Parcelable
import kotlin.random.Random

/**
 * Game logic for the game Thirty.
 */
class Thirty(
    var rounds: MutableList<Round>,
    var score: Int,
    var isGrading: Boolean,
    var isThrowing: Boolean,
    var round: Int?,
    var totalRounds: Int,
    var currentThrow: Int?
    ) : Parcelable {
    var remainingCategories = mutableListOf<String>(
        "low",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "11",
        "12"
    )
    var dices = mutableListOf<Dice>()
        private set

    constructor(parcel: Parcel) : this (
        arrayListOf<Round>().apply {
            parcel.readList(this, Round::class.java.classLoader)
        },
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    /**
     * Allows the user, if allowed, to throw all dices not picked by the user.
     */
    fun throwDice() {
        if (isGrading) return
        if (round == totalRounds + 1) return

        // Round
        round?.inc()
        if (round == null) round = 1

        // The actual throwing logic
        currentThrow = currentThrow?.inc() ?: 1
        dices.forEachIndexed { index, i ->
            if (!i.picked) {
                dices[index].value = Random.nextInt(1, 6)
            }
        }

        // Switch to grading mode
        if (currentThrow == 3) {
            isThrowing = false
            isGrading = true
            dices.forEach {
                it.picked = false
            }

        }
    }

    /**
     * Initialize the game.
     */
    fun startGame() {
        // Make sure this is a new uninitialized game
        if (isThrowing || isGrading) return
        isThrowing = true
    }

    /**
     * Finalize the current round and calculate its score
     * depending on the category selected.
     */
    fun saveRound(category: String): Boolean {
        if (!remainingCategories.contains(category)) return false

        // Get total dice count
        var pickedDiceSum = 0
        dices.forEach { dice ->
            dice.value?.let { value ->
                if (dice.picked) pickedDiceSum += value
            }

        }

        if (category == "low") {
            // Unpick dices higher than 3
            // TODO: Maybe introduce a temporary score variable
            dices.forEach { dice ->
                dice.value?.let { value ->
                    if (value > 3) dice.picked = false
                }
            }
        } else {
            // Check if divisible by category
            val categoryModulo = category.toString().toInt()

            // User's selections are not coherent with the chosen category
            // TODO: Notify user
            if (pickedDiceSum % categoryModulo > 0) return false
        }

        // Create the round object and increase score
        val newRound = Round(category, pickedDiceSum)
        rounds.add(newRound)
        score += pickedDiceSum
        // This category is now used
        remainingCategories.remove(category)
        return true
    }

    /**
     * Resets the game to prepare for a new round.
     */
    fun nextRound() {
        // Only allow if this is not the last round
        if (round == totalRounds) return

        round?.let {
            round = round?.inc()
        }
        // Reset game state
        currentThrow = null
        isThrowing = true
        isGrading = false

        // Reset dices
        dices.forEach {
            it.value = null
            it.picked = false
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(rounds)
        parcel.writeInt(score)
        parcel.writeBoolean(isGrading)
        parcel.writeBoolean(isThrowing)
        round?.let {
            parcel.writeInt(it)
        }
        parcel.writeInt(totalRounds)
        currentThrow?.let {
            parcel.writeInt(it)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Thirty> {
        override fun createFromParcel(parcel: Parcel): Thirty {
            return Thirty(parcel)
        }

        override fun newArray(size: Int): Array<Thirty?> {
            return arrayOfNulls(size)
        }
    }

}