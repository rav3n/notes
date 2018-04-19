package sonder.notes.presentation.screens.notes.data.entity

import android.os.Parcel
import android.os.Parcelable

class Input(var title: String) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
        title = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Input> {
        override fun createFromParcel(parcel: Parcel): Input {
            return Input(parcel)
        }

        override fun newArray(size: Int): Array<Input?> {
            return arrayOfNulls(size)
        }
    }

}
