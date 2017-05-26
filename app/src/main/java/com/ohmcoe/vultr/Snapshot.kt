package com.ohmcoe.vultr

import org.json.JSONObject

class Snapshot {


    /*
    example response from api
        "SNAPSHOTID": "5359435d28b9a",
        "date_created": "2014-04-18 12:40:40",
        "description": "Test snapshot",
        "size": "42949672960",
        "status": "complete"
     */

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