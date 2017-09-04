package com.ohmcoe.vultr


import org.json.JSONException
import org.json.JSONObject


class SnapshotList(response: String) {

    var snapshotList: ArrayList<Snapshot>? = null

    init {
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

    fun toList(): List<Snapshot> {
        val list = snapshotList

        return list!!
    }


}