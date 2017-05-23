package com.ohmcoe.vultr

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class SetAPIKeyFragment : Fragment() {
    var setAPIKeyView: View? = null
    var txtAPIKey: EditText? = null
    var btnOK: Button?= null
    private var APIKey: String? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setAPIKeyView = inflater!!.inflate(R.layout.fragment_set_apikey, container, false)

        txtAPIKey = setAPIKeyView!!.findViewById(R.id.txtAPIKey) as EditText
        btnOK = setAPIKeyView!!.findViewById(R.id.btnOK) as Button
        btnOK!!.setOnClickListener (object: View.OnClickListener  {
            override fun onClick(v: View?) {
                updateAPIKey()
            }
        })

        val bundle = arguments
        APIKey = bundle.getString("API-Key")
        txtAPIKey!!.setText(APIKey)

        return setAPIKeyView as View
    }

    fun updateAPIKey():Unit
    {
        var fileName:String = MainActivity.Config.configFile
        var outputStream:FileOutputStream

        try {
            outputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE)
            outputStream.write(txtAPIKey!!.getText().toString().toByteArray())
            outputStream.close()
        }
        catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val intent = Intent(activity, MainActivity::class.java)
        activity.finish()
        startActivity(intent)
    }
}
