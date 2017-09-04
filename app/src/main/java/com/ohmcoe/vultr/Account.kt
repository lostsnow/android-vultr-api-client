package com.ohmcoe.vultr

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Account() : Parcelable {

    @SerializedName("balance")
    @Expose
    var balance: String? = null

    @SerializedName("pending_charges")
    @Expose
    var pendingCharges: String? = null
    @SerializedName("last_payment_date")
    @Expose
    var lastPaymentDate: String? = null
    @SerializedName("last_payment_amount")
    @Expose
    var lastPaymentAmount: String? = null

    constructor(parcel: Parcel) : this() {
        balance = parcel.readString()
        pendingCharges = parcel.readString()
        lastPaymentDate = parcel.readString()
        lastPaymentAmount = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(balance)
        parcel.writeString(pendingCharges)
        parcel.writeString(lastPaymentDate)
        parcel.writeString(lastPaymentAmount)
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