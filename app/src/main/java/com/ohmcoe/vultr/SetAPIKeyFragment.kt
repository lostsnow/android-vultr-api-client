package com.ohmcoe.vultr

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_set_apikey.view.*
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class SetAPIKeyFragment : Fragment() {
    private var APIKey: String? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val setAPIKeyView = inflater!!.inflate(R.layout.fragment_set_apikey, container, false)

        setAPIKeyView.btnOK.setOnClickListener { updateAPIKey() }

        val bundle = arguments
        APIKey = bundle.getString("API-Key")
        setAPIKeyView.txtAPIKey.setText(APIKey)

        return setAPIKeyView
    }

    fun updateAPIKey(): Unit {
        val fileName: String = MainActivity.Config.configFile
        val outputStream: FileOutputStream

        try {
            outputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE)
            outputStream.write(view.txtAPIKey.getText().toString().toByteArray())
            outputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val intent = Intent(activity, MainActivity::class.java)
        activity.finish()
        startActivity(intent)
    }
}
