package com.ohmcoe.vultr.model

import org.json.JSONObject

class Snapshot {

    var snapshotID: String? = null
    var date_created: String? = null
    var description: String? = null
    var size: String? = null
    var status: String? = null

    fun loadAttribute(jObj: JSONObject) {
        this.snapshotID = jObj.getString("SNAPSHOTID")
        this.date_created = jObj.getString("date_created")
        this.description = jObj.getString("description")
        this.size = jObj.getString("size")
        this.status = jObj.getString("status")
    }
}