package com.ohmcoe.vultr


import org.json.JSONException
import org.json.JSONObject


class SnapshotList {

    var snapshotList: ArrayList<Snapshot>? = null
    var body: String? = null

    constructor() {

    }

    fun toList(): List<Snapshot> {
        val list = snapshotList

        return list!!
    }


    constructor(response: String) {
        body = response
        snapshotList = ArrayList<Snapshot>()

        try {
            val jObj = JSONObject(response)
            val keys = jObj.keys()

            while (keys.hasNext()) {
                val result = jObj.getString(keys.next())

                val snapshotJObj = JSONObject(result)

                val snapshot = Snapshot()
                snapshot.loadAttribute(snapshotJObj)

                snapshotList!!.add(snapshot)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
}