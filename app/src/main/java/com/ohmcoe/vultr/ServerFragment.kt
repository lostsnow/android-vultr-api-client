package com.ohmcoe.vultr

import android.app.Fragment
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.DecimalFormat


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ServerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ServerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ServerFragment : Fragment() {
    var serverView:View? =null
    private var progressDialog: ProgressDialog? = null
    var serverList: ServerList? = null

    var txtIP: TextView? = null
    var txtRam: TextView? = null
    var txtLabel: TextView? = null
    var txtOS: TextView? = null
    var txtState: TextView? = null
    var txtPendingCharges: TextView? = null
    var txtBandwidth: TextView? = null
    var txtBandwidthHistory: TextView? = null
    var txtInbound: TextView? = null
    var txtOutbound: TextView? = null
    var btnReload: Button? = null
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

        txtIP = serverView.findViewById(R.id.txtIP) as TextView
        txtRam = serverView.findViewById(R.id.txtRam) as TextView
        txtLabel = serverView.findViewById(R.id.txtLabel) as TextView
        txtOS = serverView.findViewById(R.id.txtOs) as TextView
        txtState = serverView.findViewById(R.id.txtState) as TextView
        txtPendingCharges = serverView.findViewById(R.id.txtPendingCharges) as TextView
        txtBandwidth = serverView.findViewById(R.id.txtBandwidth) as TextView
        btnReload = serverView.findViewById(R.id.btnReload) as Button

        //bandwidth view
        txtInbound = serverView.findViewById(R.id.txtInbound) as TextView
        txtOutbound = serverView.findViewById(R.id.txtOutbound) as TextView
        txtBandwidthHistory = serverView.findViewById(R.id.txtBandwidthHistory) as TextView
        /* bandwidthGraph = (LinearLayout) serverView.findViewById(R.id.bandwidhGraph);*/

        btnReload!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                getServerList()
            }
        })

        txtBandwidthHistory!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                showBandwidthGraph()
            }
        })

        val bundle = arguments
        APIKey = bundle.getString("API-Key")
        SUBID = bundle.getString("SUBID")

        getServerList()

        this.serverView = serverView

        return serverView
    }

    protected fun updateUI() {
        if (serverList != null) {
            for (server in serverList!!.serverLists!!) {
                if (server.subid.equals(SUBID)) {
                    txtIP!!.text = server.main_ip
                    txtRam!!.text = server.ram
                    txtLabel!!.text = server.label
                    txtOS!!.text = server.os
                    txtState!!.text = server.server_state
                    txtPendingCharges!!.text = server.strPendingCharges
                    txtBandwidth!!.text = server.bandwidth
                    btnReload = serverView!!.findViewById(R.id.btnReload) as Button
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
        if (bandwidth != null) {
            //update inbound
            txtInbound!!.setText(this.humanByte(bandwidth!!.sumInbound))
            txtOutbound!!.setText(this.humanByte(bandwidth!!.sumOutbound))
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
