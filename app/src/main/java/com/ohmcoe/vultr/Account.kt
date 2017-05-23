package com.ohmcoe.vultr

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Account {

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

}