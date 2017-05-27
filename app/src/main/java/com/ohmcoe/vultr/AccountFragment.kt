package com.ohmcoe.vultr

import android.app.Fragment
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountFragment : Fragment() {

    private var progressDialog: ProgressDialog? = null

    private var APIKey: String? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val accountView = inflater!!.inflate(R.layout.fragment_account, container, false)

        accountView.btnReload.setOnClickListener { getAccount() }

        //create waiting dialog
        progressDialog = ProgressDialog(activity)
        progressDialog!!.setTitle("Loading")
        progressDialog!!.setMessage("Wait while loading...")
        progressDialog!!.setCancelable(false)

        val bundle = arguments
        APIKey = bundle.getString("API-Key")

        getAccount()

        return accountView
    }

    protected fun refreshAccountUI(response: Response<Account>) {
        val account = response.body()


        val currentBalance = account.balance!!.toDouble()
        val pendingCharge = account.pendingCharges!!.toDouble()
        val remainBalance = currentBalance + pendingCharge

        val stringCurrentBalance = "$" + String.format("%02.2f", currentBalance * -1)
        val stringPendingCharges = "$" + String.format("%02.2f", pendingCharge)
        val stringRemainBalance = "$" + String.format("%02.2f", remainBalance * -1)

        txtCurrentBalance.setText(stringCurrentBalance)
        txtPendingCharges.setText(stringPendingCharges)
        txtRemainBalance.setText(stringRemainBalance)

        progressDialog!!.dismiss()
    }

    protected fun getAccount() {

        progressDialog!!.show()

        val retrofitClient = RetrofitClient(getString(R.string.base_uri))
        val retrofit = retrofitClient.retrofit

        val client = retrofit.create(VultrClient::class.java)
        val call = client.getAccount(this.APIKey)

        call.enqueue(object : Callback<Account> {
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                if (response.code() == 200) {
                    refreshAccountUI(response)
                } else {
                    progressDialog!!.dismiss()
                    val text = "Loading Failure"
                    val toast = MyToast(activity, text)
                    toast.show()
                }
            }

            override fun onFailure(call: Call<Account>, t: Throwable) {
                txtCurrentBalance.setText(t.message)
            }
        })
    }
}
