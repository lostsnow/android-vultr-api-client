package com.ohmcoe.vultr

import android.app.Fragment
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_server_list.view.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ServerListFragment : Fragment() {

    var progressDialog:ProgressDialog? = null
    var serverListLayout:View? = null
    private var APIKey: String? = null
    private var serverList: ServerList? = null
    private var txtServerList: ListView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        serverListLayout = inflater!!.inflate(R.layout.fragment_server_list, container, false)

        progressDialog = ProgressDialog(activity)
        progressDialog!!.setTitle("Loading")
        progressDialog!!.setMessage("Wait while loading...")
        progressDialog!!.setCancelable(false)

        val bundle = arguments
        APIKey = bundle.getString("API-Key")

        prepareServerList()

        return serverListLayout!!
    }

    protected fun prepareServerList() {
        serverList = ServerList()
        txtServerList = serverListLayout!!.txtServerList
        getServerList()
    }


    protected fun updateUI() {
        val serverAdapter = ServerAdapter(activity, R.layout.server_list, serverList!!.toList())
        txtServerList!!.adapter = serverAdapter
        txtServerList!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val server = parent.getItemAtPosition(position) as Server
            val bundle = Bundle()
            bundle.putString("API-Key", APIKey)
            bundle.putString("SUBID", server.subid)
            val serverFragment = ServerFragment()
            serverFragment.setArguments(bundle)
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, serverFragment)
                    .addToBackStack(null)
                    .commit()
        }
    }


    protected fun getServerList() {
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
        val call = client.getServerList(APIKey)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.code() == 200) {
                        val body = response.body().string()
                        serverList = ServerList(body)
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
