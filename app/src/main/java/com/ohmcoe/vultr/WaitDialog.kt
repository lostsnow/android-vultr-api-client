package com.ohmcoe.vultr

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class WaitDialog() : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        dismissAllowingStateLoss()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.wait_dialog_layout, container, false)!!

        return view
    }


    companion object {
        fun newInstance(): WaitDialog {
            val fragment = WaitDialog()
            return fragment
        }
    }

}