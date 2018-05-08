package com.ohmcoe.vultr.model

import android.support.annotation.Keep
import org.json.JSONObject

@Keep
data class Server(var serverJOBJ: JSONObject) {

    var subid: String? = null
    var os: String? = null
    var ram: String? = null
    var main_ip: String? = null
    var label: String? = null
    var server_state: String? = null
    var power_status: String? = null
    var pending_charges: String? = null
    var current_bandwidth_gb: String? = null
    var allowed_bandwidth_gb: String? = null

    init {
        loadAttribute()
    }

   internal fun loadAttribute() {
        this.subid = serverJOBJ.getString("SUBID")
        this.os = serverJOBJ.getString("os")
        this.ram = serverJOBJ.getString("ram")
        this.main_ip = serverJOBJ.getString("main_ip")
        this.label = serverJOBJ.getString("label")
        this.server_state = serverJOBJ.getString("server_state")
        this.power_status = serverJOBJ.getString("power_status")
        this.pending_charges = serverJOBJ.getString("pending_charges")
        this.current_bandwidth_gb = serverJOBJ.getString("current_bandwidth_gb")
        this.allowed_bandwidth_gb = serverJOBJ.getString("allowed_bandwidth_gb")
    }

    val strPendingCharges: String
        get() = "$" + pending_charges

    val bandwidth: String
        get() {
            var bandwidth = ""

            val currentBandwidth = current_bandwidth_gb!!.toDouble()
            val allowedBandwidth = allowed_bandwidth_gb!!.toDouble()

            bandwidth += current_bandwidth_gb + " GB of " + allowed_bandwidth_gb + " GB (" + String.format("%.0f", currentBandwidth / allowedBandwidth * 100) + "%)"
            return bandwidth
        }
}
