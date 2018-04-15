package com.ohmcoe.vultr

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ohmcoe.vultr.model.Account
import kotlinx.android.synthetic.main.fragment_account.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountFragment : Fragment() {

    private lateinit var APIKey: String
    private lateinit var accountView: View
    private lateinit var waitDialog: WaitDialog
    private var account: Account? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        accountView = inflater!!.inflate(R.layout.fragment_account, container, false)

        waitDialog = WaitDialog.newInstance()

        //read api key from bundle
        val bundle = arguments
        APIKey = bundle.getString("API-Key")

        //check instance state
        if (savedInstanceState == null) {
            getAccount()
        } else {
            this.account = savedInstanceState.getParcelable("account")

            if (this.account != null)
                updateUIFromAccount()
            else
                getAccount()
        }

        //bind reload button
        accountView.btnReload.setOnClickListener { getAccount() }

        return accountView
    }

    private fun refreshAccountUI(response: Response<Account>) {
        account = response.body()
        updateUIFromAccount()
        dismissDialog()
    }

    private fun updateUIFromAccount() {
        val view = accountView

        val currentBalance = account!!.balance.toDouble()
        val pendingCharge = account!!.pending_charges.toDouble()
        val remainBalance = currentBalance + pendingCharge

        val stringCurrentBalance = "$" + String.format("%02.2f", currentBalance * -1)
        val stringPendingCharges = "$" + String.format("%02.2f", pendingCharge)
        val stringRemainBalance = "$" + String.format("%02.2f", remainBalance * -1)

        view.txtCurrentBalance.text = stringCurrentBalance
        view.txtPendingCharges.text = stringPendingCharges
        view.txtRemainBalance.text = stringRemainBalance
    }

    private fun dismissDialog() {
        waitDialog.dismiss()
    }

    private fun showDialog() {
        childFragmentManager.executePendingTransactions()
        waitDialog.isCancelable = false
        if (!waitDialog.isAdded)
        waitDialog.show(childFragmentManager, "waitDialog")
    }

    private fun getAccount() {
        showDialog()

        val retrofitClient = RetrofitClient(getString(R.string.base_uri))
        val retrofit = retrofitClient.retrofit
        val client = retrofit.create(VultrClient::class.java)
        val clientCall = client.getAccount(this.APIKey)

        clientCall.enqueue(object : Callback<Account> {
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                if (response.code() == 200) {
                    refreshAccountUI(response)
                } else {
                    val text = "Loading Failure";
                    val toast = MyToast(activity, text)
                    toast.show()
                    dismissDialog()
                }
            }

            override fun onFailure(call: Call<Account>, t: Throwable) {
                view.txtCurrentBalance.text = t.message
                view.txtPendingCharges.text = t.message
                view.txtRemainBalance.text = t.message
                dismissDialog()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable("account", account)
    }
}
