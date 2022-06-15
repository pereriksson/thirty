package se.umu.cs.peer0019.thirty.model

import android.os.Parcel
import android.os.Parcelable
import kotlin.random.Random

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

    fun throwDice() {
        if (isGrading) return
        if (round == totalRounds + 1) return

        // Round
        round?.inc()
        if (round == null) round = 1

        currentThrow = currentThrow?.inc() ?: 1
        dices.forEachIndexed { index, i ->
            if (!i.picked) {
                dices[index].value = Random.nextInt(1, 6)
            }
        }

        if (currentThrow == 3) {
            isThrowing = false
            isGrading = true
            dices.forEach {
                it.picked = false
            }

        }
    }
    fun startGame() {
        isThrowing = true
    }

    fun saveRound(category: String) {
        if (!remainingCategories.contains(category)) return
        remainingCategories.remove(category)
        // todo: real data
        val newRound = Round(category, 10)
        rounds.add(newRound)
    }

    fun nextRound() {
        if (round == totalRounds) return
        round?.let {
            round = round?.inc()
        }
        currentThrow = null
        isThrowing = true
        isGrading = false
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