package com.ohmcoe.vultr.model

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.Keep

@Keep
data class Account(var balance: String, var pending_charges: String, var last_payment_date:String, var last_payment_amount: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(balance)
        parcel.writeString(pending_charges)
        parcel.writeString(last_payment_date)
        parcel.writeString(last_payment_amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Account> {
        override fun createFromParcel(parcel: Parcel): Account {
            return Account(parcel)
        }

        override fun newArray(size: Int): Array<Account?> {
            return arrayOfNulls(size)
        }
    }

}