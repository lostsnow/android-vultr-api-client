package com.ohmcoe.vultr


import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList


class ServerList {

    var serverLists: ArrayList<Server>? = null
    var body: String? = null

    constructor() {

    }

    fun toList(): List<Server> {
        val list = serverLists

        return list!!
    }


    constructor(response: String) {
        body = response
        serverLists = ArrayList<Server>()

        try {
            val jObj = JSONObject(response)
            val keys = jObj.keys()

            while (keys.hasNext()) {
                val result = jObj.getString(keys.next())

                val serverJObj = JSONObject(result)

                val server = Server()
                server.loadAttribute(serverJObj)

                serverLists!!.add(server)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
}