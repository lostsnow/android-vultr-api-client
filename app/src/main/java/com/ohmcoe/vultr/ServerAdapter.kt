package com.ohmcoe.vultr

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.ohmcoe.vultr.R.id.txtPendingCharges
import com.ohmcoe.vultr.R.id.txtServerName
import kotlinx.android.synthetic.main.server_list.view.*

class ServerAdapter : ArrayAdapter<Server> {

    constructor(context: Context, resource: Int) : super(context, resource) {}

    constructor(context: Context, resource: Int, objects: List<Server>) : super(context, resource, objects) {}

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        val server = getItem(position)

        if (view == null) {
            val layoutInflater: LayoutInflater
            layoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.server_list, null)
        }

        if (server != null) {
            val txtServerName = view!!.txtServerName
            val txtPendingCharges = view.txtPendingCharges;
            val txtIP = view.txtIP

            if (txtServerName != null) {
                txtServerName.text = server.label
            }

            if (txtPendingCharges != null) {
                val pendingCharges = "$" + server.pending_charges!!
                txtPendingCharges.text = pendingCharges
            }

            if (txtIP != null) {
                txtIP.text = server.main_ip
            }
        }

        return view!!
    }
}
