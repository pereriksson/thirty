package se.umu.cs.peer0019.thirty.model

import android.os.Parcel
import android.os.Parcelable
import kotlin.random.Random

class Thirty() : Parcelable {
    var gradingSetting: Int? = null
    var score: Int = 0
        private set
    var ratings = listOf<String>(
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
    var scorePerRating = mutableListOf<Int>()
    var isGrading: Boolean = false
        private set
    var isThrowing: Boolean = false
    var round: Int? = null
        private set
    val maxRounds = 3
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

    constructor(parcel: Parcel) : this() {
        gradingSetting = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    fun throwDice() {
        if (isGrading) return
        if (round == maxRounds+1) return

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

    fun nextRound() {
        if (round == maxRounds) return
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
        parcel.writeValue(gradingSetting)
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