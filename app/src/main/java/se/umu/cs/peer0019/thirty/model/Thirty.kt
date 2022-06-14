package se.umu.cs.peer0019.thirty.model

import android.os.Parcel
import android.os.Parcelable
import kotlin.random.Random

class Thirty() : Parcelable {
    var gradingSetting: Int? = null
    var score: Int = 0
        private set
    var stopped: Boolean = false // todo: refactor
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

    constructor(parcel: Parcel) : this() {
        gradingSetting = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    fun throwDice() {
        if (stopped) {
            return
        }

        // Round
        round?.inc()
        if (round == null) round = 1

        if (currentThrow == 2) {
            stopped = true
        }
        currentThrow = currentThrow?.inc() ?: 1
        dices.forEachIndexed { index, i ->
            if (!pickedDices.contains(index)) {
                dices[index] = Random.nextInt(1, 6)
            }
        }
    }
    fun startGame() {

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