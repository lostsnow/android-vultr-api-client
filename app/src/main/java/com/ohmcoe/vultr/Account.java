package com.ohmcoe.vultr;

public class Account {
    private String balance;
    private String pending_charges;
    private String last_payment_date;
    private String last_payment_amount;

    public Account() {

    }

    public String getBalance() {
        return balance;
    }

    public String getPending_charges() {
        return pending_charges;
    }

    public String getLast_payment_date() {
        return last_payment_date;
    }

    public String getLast_payment_amount() {
        return last_payment_amount;
    }
}
