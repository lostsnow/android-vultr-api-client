package com.ohmcoe.vultr

import android.app.Fragment
import android.app.ProgressDialog
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
    var serverView:View? =null
    private var progressDialog: ProgressDialog? = null
    var serverList: ServerList? = null
    var bandwidth: Bandwidth? = null

    private var APIKey: String? = null
    private var SUBID: String? = null

    private var myToast: MyToast? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val serverView = inflater!!.inflate(R.layout.server_layout, container, false)

        progressDialog = ProgressDialog(activity)
        progressDialog!!.setTitle("Loading")
        progressDialog!!.setMessage("Wait while loading...")
        progressDialog!!.setCancelable(false)

        myToast = MyToast(activity, "")

        serverView.btnReload.setOnClickListener { getServerList() }

        serverView.txtBandwidthHistory.setOnClickListener { showBandwidthGraph() }

        val bundle = arguments
        APIKey = bundle.getString("API-Key")
        SUBID = bundle.getString("SUBID")

        getServerList()

        this.serverView = serverView

        return serverView
    }

    protected fun updateUI() {

        val view = serverView!!

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

    protected fun showBandwidthGraph() {
        if (bandwidth == null)
            updateBandwidthUI()

        val intent = Intent(activity, BandwidthGraphActivity::class.java)
        intent.putExtra("x", bandwidth!!.xGraph)
        intent.putExtra("dates", bandwidth!!.dates)
        intent.putExtra("inbound", bandwidth!!.inboundGraph)
        intent.putExtra("outbound", bandwidth!!.outboundGraph)

        startActivity(intent)
    }

    protected fun updateBandwidthUI() {
        val view = serverView!!

        if (bandwidth != null) {
            //update inbound
            view.txtInbound.setText(this.humanByte(bandwidth!!.sumInbound))
            view.txtOutbound.setText(this.humanByte(bandwidth!!.sumOutbound))
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

    protected fun getBandwidth() {
        progressDialog!!.show()

        val retrofitClient = RetrofitClient(getString(R.string.base_uri))
        val retrofit = retrofitClient.retrofit

        val client = retrofit.create(VultrClient::class.java)
        val call = client.getServerBandwidth(APIKey, SUBID)

        call.enqueue(object : Callback<Bandwidth> {
            override fun onResponse(call: Call<Bandwidth>, response: Response<Bandwidth>) {
                bandwidth = response.body()
                updateUI()
                progressDialog!!.dismiss()
            }

            override fun onFailure(call: Call<Bandwidth>, t: Throwable) {
                progressDialog!!.dismiss()

                myToast!!.setText("Connection failure")
                myToast!!.show()
            }
        })
    }

    protected fun getServerList() {
        progressDialog!!.show()

        val retrofitClient = RetrofitClient(getString(R.string.base_uri))
        val retrofit = retrofitClient.retrofit

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
                        myToast!!.setText(text)
                        myToast!!.show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                progressDialog!!.dismiss()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog!!.dismiss()
                myToast!!.setText("Connection failure")
                myToast!!.show()
            }
        })

        getBandwidth()
    }

}// Required empty public constructor
