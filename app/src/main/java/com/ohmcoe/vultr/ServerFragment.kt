package com.ohmcoe.vultr

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.server_layout.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ServerFragment : Fragment() {
    private lateinit var serverView: View
    private lateinit var APIKey: String
    private lateinit var SUBID: String
    private lateinit var myToast: MyToast
    private lateinit var  waitDialog: WaitDialog

    private var serverList: ServerList? = null
    private var bandwidth: Bandwidth? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val serverView = inflater!!.inflate(R.layout.server_layout, container, false)

        myToast = MyToast(activity, "")
        waitDialog = WaitDialog.newInstance()


        val bundle = arguments
        APIKey = bundle.getString("API-Key")
        SUBID = bundle.getString("SUBID")


        serverView.btnReload.setOnClickListener { getData() }
        serverView.txtBandwidthHistory.setOnClickListener { showBandwidthGraph() }
        this.serverView = serverView

        if (savedInstanceState == null)
            getData()
        else
        {
            serverList = savedInstanceState.getParcelable("serverList")
            bandwidth = savedInstanceState.getParcelable("bandWidth")

            if (serverList == null || bandwidth == null)
                getData();

            updateUI();
        }

        return serverView
    }


    private fun updateUI() {
        val view = serverView

        if (serverList != null) {
            for (server in serverList!!.serverLists!!) {
                if (server.subid.equals(SUBID)) {
                    view.txtIP.text = server.main_ip
                    view.txtRam.text = server.ram
                    view.txtLabel.text = server.label
                    view.txtOS.text = server.os
                    view.txtState.text = server.server_state
                    view.txtPendingCharges.text = server.strPendingCharges
                    view.txtBandwidth.text = server.bandwidth
                }
            }
        }

        updateBandwidthUI()
    }

    private fun showBandwidthGraph() {
        if (bandwidth != null) {

            val intent = Intent(activity, BandwidthGraphActivity::class.java)
            intent.putExtra("x", bandwidth!!.xGraph)
            intent.putExtra("dates", bandwidth!!.dates)
            intent.putExtra("inbound", bandwidth!!.inboundGraph)
            intent.putExtra("outbound", bandwidth!!.outboundGraph)

            startActivity(intent)
        }
    }

    private fun updateBandwidthUI() {
        val view = serverView

        if (bandwidth != null) {
            //update inbound
            view.txtInbound.text = this.humanByte(bandwidth?.sumInbound)
            view.txtOutbound.text = this.humanByte(bandwidth?.sumOutbound)
        }
    }

    private fun humanByte(inBytes: Double?): String {
        var bytes = inBytes!!
        var postfix = "B"

        if (bytes > 1024.0) {
            bytes /= 1024.0
            postfix = "KB"
            if (bytes > 1024.0) {
                bytes /= 1024.0
                postfix = "MB"
                if (bytes > 1024.0) {
                    bytes /= 1024.0
                    postfix = "GB"
                }
            }
        }

        val strByte = String.format("%.2f", bytes)
        return strByte + " " + postfix
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


    private fun getBandwidth() {
        val retrofitClient = RetrofitClient(getString(R.string.base_uri))
        val retrofit = retrofitClient.retrofit

        val client = retrofit.create(VultrClient::class.java)
        val clientCall = client.getServerBandwidth(APIKey, SUBID)

        clientCall.enqueue(object : Callback<Bandwidth> {
            override fun onResponse(call: Call<Bandwidth>, response: Response<Bandwidth>) {
                bandwidth = response.body()
                updateUI()
                dismissDialog()
            }

            override fun onFailure(call: Call<Bandwidth>, t: Throwable) {
                dismissDialog()

                myToast.setText("Connection failure")
                myToast.show()
            }
        })
    }

    private fun getData() {
        showDialog()

        getServerList()
        getBandwidth()
    }

    private fun getServerList() {
        val retrofitClient = RetrofitClient(getString(R.string.base_uri))
        val retrofit = retrofitClient.retrofit

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
                        myToast.setText(text)
                        myToast.show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                dismissDialog()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dismissDialog()
                myToast.setText("Connection failure")
                myToast.show()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putParcelable("serverList", serverList);
        outState?.putParcelable("bandWidth", bandwidth);
    }
}// Required empty public constructor
