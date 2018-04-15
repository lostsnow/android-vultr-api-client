package com.ohmcoe.vultr

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_snapshot.view.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class SnapshotFragment : Fragment() {

    private var APIKey: String? = null
    private var txtSnapshotList: ListView? = null
    var snapshotView: View? = null
    private var snapshotList: SnapshotList? = null
    private lateinit var waitDialog: WaitDialog

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        snapshotView = inflater!!.inflate(R.layout.fragment_snapshot, container, false)

        waitDialog = WaitDialog.newInstance()
        val bundle = arguments
        APIKey = bundle.getString("API-Key")

        txtSnapshotList = snapshotView!!.txtSnapshotList

        if (savedInstanceState == null) {
            getSnapshotList()
        }
        else
        {
            snapshotList = savedInstanceState.getParcelable("snapshotList")
            if (snapshotList == null)
                getSnapshotList()

            updateUI()
        }

        return snapshotView!!
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

    private fun updateUI() {
        if (activity != null) {
            if (snapshotList == null)
                return;

            val snapshotAdapter = SnapshotAdapter(activity, R.layout.snapshot_list, snapshotList!!.toList()!!)
            txtSnapshotList!!.adapter = snapshotAdapter
        }
    }

    private fun getSnapshotList() {
        showDialog()

        val API_BASE_URL = resources.getString(R.string.base_uri)

        val httpClient = OkHttpClient.Builder()

        val builder = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder
                .client(httpClient.build())
                .build()

        val client = retrofit.create(VultrClient::class.java)
        val call = client.getSnapshotList(APIKey)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.code() == 200) {
                        val body = response.body()!!.string()
                        snapshotList = SnapshotList(body)
                        updateUI()
                    } else {
                        val text = "Loading Failure response code " + response.code()
                        val myToast = MyToast(activity, text)
                        myToast.show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                dismissDialog()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dismissDialog()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putParcelable("snapshotList", snapshotList)
    }
}// Required empty public constructor
