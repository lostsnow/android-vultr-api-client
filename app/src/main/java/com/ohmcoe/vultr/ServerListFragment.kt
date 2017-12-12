package com.ohmcoe.vultr

import android.app.Fragment
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

    private lateinit var waitDialog: WaitDialog
    private lateinit var serverListLayout: View
    private lateinit var APIKey: String
    private lateinit var txtServerList: ListView
    private var serverList: ServerList? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        serverListLayout = inflater!!.inflate(R.layout.fragment_server_list, container, false)

        waitDialog = WaitDialog.newInstance()

        val bundle = arguments
        APIKey = bundle.getString("API-Key")

        serverList = ServerList()
        txtServerList = serverListLayout.txtServerList


        //restore instance
        if (savedInstanceState == null) {
            getServerList()
        } else {
            serverList = savedInstanceState.getParcelable("serverList")
            if (serverList == null)
                getServerList()
            else
                updateUI()
        }

        return serverListLayout
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
        if (activity == null || serverList == null)
            return;

        val serverAdapter = ServerAdapter(activity, R.layout.server_list, serverList!!.toList()!!)
        txtServerList.adapter = serverAdapter
        txtServerList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val server = parent.getItemAtPosition(position) as Server
            val bundle = Bundle()
            bundle.putString("API-Key", APIKey)
            bundle.putString("SUBID", server.subid)
            val serverFragment = ServerFragment()
            serverFragment.arguments = bundle
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, serverFragment)
                    .addToBackStack(null)
                    .commit()
        }
    }


    private fun getServerList() {
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
        val clientCall = client.getServerList(APIKey)

        clientCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.code() == 200) {
                        val body = response.body()!!.string()
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

                dismissDialog()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dismissDialog()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putParcelable("serverList", serverList)
    }
}// Required empty public constructor
