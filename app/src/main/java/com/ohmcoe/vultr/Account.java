
package com.ohmcoe.vultr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Account {

    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("pending_charges")
    @Expose
    private String pendingCharges;
    @SerializedName("last_payment_date")
    @Expose
    private String lastPaymentDate;
    @SerializedName("last_payment_amount")
    @Expose
    private String lastPaymentAmount;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPendingCharges() {
        return pendingCharges;
    }

    public void setPendingCharges(String pendingCharges) {
        this.pendingCharges = pendingCharges;
    }

    public String getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(String lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public String getLastPaymentAmount() {
        return lastPaymentAmount;
    }

    public void setLastPaymentAmount(String lastPaymentAmount) {
        this.lastPaymentAmount = lastPaymentAmount;
    }

}