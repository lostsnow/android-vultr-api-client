package com.ohmcoe.vultr.model

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

class ServerList() : Parcelable{

    var serverLists: ArrayList<Server>? = null
    var body: String? = null

    constructor(parcel: Parcel) : this() {
        body = parcel.readString()
    }

    fun toList(): List<Server>? {
        val list = serverLists
        return list
    }

    constructor(response: String) : this() {
        body = response
        serverLists = ArrayList<Server>()

        try {
            val jObj = JSONObject(response)
            val keys = jObj.keys()

            while (keys.hasNext()) {
                val result = jObj.getString(keys.next())

                val serverJObj = JSONObject(result)

                val server = Server(serverJObj)

                serverLists!!.add(server)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(body)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ServerList> {
        override fun createFromParcel(parcel: Parcel): ServerList {
            return ServerList(parcel)
        }

        override fun newArray(size: Int): Array<ServerList?> {
            return arrayOfNulls(size)
        }
    }
}