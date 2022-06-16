package se.umu.cs.peer0019.thirty.model

import android.os.Parcel
import android.os.Parcelable

/**
 * A parcelable round describing a received score and a chosen
 * category (e g "low", "4")
 */
class Round (
    var category: String?,
    var score: Int = 0) : Parcelable {
    constructor(parcel: Parcel) : this (
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeInt(score)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Round> {
        override fun createFromParcel(parcel: Parcel): Round {
            return Round(parcel)
        }

        override fun newArray(size: Int): Array<Round?> {
            return arrayOfNulls(size)
        }
    }

}