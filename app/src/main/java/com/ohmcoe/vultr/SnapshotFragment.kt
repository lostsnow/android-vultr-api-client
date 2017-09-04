package com.ohmcoe.vultr

import android.app.Fragment
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
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

    var progressDialog: ProgressDialog? = null
    private var APIKey: String? = null
    private var txtSnapshotList: ListView? = null
    var snapshotView: View? = null
    private var SnapshotList: SnapshotList? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        snapshotView = inflater!!.inflate(R.layout.fragment_snapshot, container, false)

        progressDialog = ProgressDialog(activity)
        progressDialog!!.setTitle("Loading")
        progressDialog!!.setMessage("Wait while loading...")
        progressDialog!!.setCancelable(false)

        val bundle = arguments
        APIKey = bundle.getString("API-Key")

        txtSnapshotList = snapshotView!!.txtSnapshotList

        getSnapshotList()

        return snapshotView!!
    }

    protected fun updateUI() {
        if (activity != null) {
            val snapshotAdapter = SnapshotAdapter(activity, R.layout.snapshot_list, SnapshotList!!.toList())
            txtSnapshotList!!.adapter = snapshotAdapter
        }
    }

    protected fun getSnapshotList() {
        progressDialog!!.show()

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
                        Log.e("Test", body)
                        SnapshotList = SnapshotList(body)
                        updateUI()
                    } else {
                        val text = "Loading Failure response code " + response.code()
                        val myToast = MyToast(activity, text)
                        myToast.show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                progressDialog!!.dismiss()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog!!.dismiss()
            }
        })
    }

}// Required empty public constructor
