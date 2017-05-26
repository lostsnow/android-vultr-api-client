package com.ohmcoe.vultr

import org.json.JSONException
import org.json.JSONObject


class Server {
    var subid: String? = null
    var os: String? = null
    var ram: String? = null
    var main_ip: String? = null
    var label: String? = null
    var server_state: String? = null
    var pending_charges: String? = null
    var current_bandwidth_gb: String? = null
    var allowed_bandwidth_gb: String? = null

    @Throws(JSONException::class)
    fun loadAttribute(jObj: JSONObject) {
        this.subid = jObj.getString("SUBID")
        this.os = jObj.getString("os")
        this.ram = jObj.getString("ram")
        this.main_ip = jObj.getString("main_ip")
        this.label = jObj.getString("label")
        this.server_state = jObj.getString("server_state")
        this.pending_charges = jObj.getString("pending_charges")
        this.current_bandwidth_gb = jObj.getString("current_bandwidth_gb")
        this.allowed_bandwidth_gb = jObj.getString("allowed_bandwidth_gb")
    }

    val strPendingCharges: String
        get() = "$" + pending_charges!!

    val bandwidth: String
        get() {
            var bandwidth = ""

            val currentBandwidth = current_bandwidth_gb!!.toDouble()
            val allowedBandwidth = allowed_bandwidth_gb!!.toDouble()

            bandwidth += current_bandwidth_gb + " GB of " + allowed_bandwidth_gb + " GB (" + String.format("%.0f", currentBandwidth / allowedBandwidth * 100) + "%)"
            return bandwidth
        }
}
