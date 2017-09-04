package com.ohmcoe.vultr

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_set_apikey.view.*


class SetAPIKeyFragment : Fragment() {
    private var setAPIKeyView:View? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val setAPIKeyView = inflater!!.inflate(R.layout.fragment_set_apikey, container, false)
        val settings = activity.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)
        val api:String? = settings.getString(MainActivity.API_KEY, null)


        setAPIKeyView.btnOK.setOnClickListener { updateAPIKey() }
        setAPIKeyView.txtAPIKey.setText(api)

        this.setAPIKeyView = setAPIKeyView
        return setAPIKeyView
    }

    fun updateAPIKey(): Unit {
        val sharePreferences = activity.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor = sharePreferences.edit()

        editor.putString(MainActivity.API_KEY, setAPIKeyView!!.txtAPIKey.text.toString())
        editor.apply()
        editor.commit()


        val intent = Intent(activity, MainActivity::class.java)
        activity.finish()
        startActivity(intent)
    }
}
