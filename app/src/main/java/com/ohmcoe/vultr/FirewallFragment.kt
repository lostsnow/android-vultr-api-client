package com.ohmcoe.vultr

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FirewallFragment : Fragment() {
    private lateinit var APIKey: String
    private lateinit var firewallView: View
    private lateinit var waitDialog: WaitDialog

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        firewallView = inflater!!.inflate(R.layout.fragment_firewall, container, false)
        waitDialog = WaitDialog.newInstance()
        val bundle = arguments
        APIKey = bundle.getString("API-Key")

        return firewallView
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
    }
}
