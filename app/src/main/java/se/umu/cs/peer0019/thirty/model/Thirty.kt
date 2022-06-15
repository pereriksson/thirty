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
    var remainingRatings = listOf<String>(
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
            if (!pickedDices.contains(index)) {
                dices[index] = Random.nextInt(1, 6)
            }
        }

        if (currentThrow == 3) {
            isThrowing = false
            isGrading = true
            pickedDices = mutableListOf<Int>()
        }
    }
    fun startGame() {
        isThrowing = true
    }

    fun saveRound() {
        // todo: real data
        val newRound = Round("low", 10)
        rounds.add(newRound)
        rounds.add(newRound)
        rounds.add(newRound)
        rounds.add(newRound)
        rounds.add(newRound)
        rounds.add(newRound)
        rounds.add(newRound)
        rounds.add(newRound)
        rounds.add(newRound)
        rounds.add(newRound)
    }

    fun nextRound() {
        if (round == totalRounds) return
        round?.let {
            round = round?.inc()
        }
        pickedDices = mutableListOf<Int>()
        currentThrow = null
        isThrowing = true
        isGrading = false
        dices = mutableListOf<Int?>(null, null, null, null, null, null)
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