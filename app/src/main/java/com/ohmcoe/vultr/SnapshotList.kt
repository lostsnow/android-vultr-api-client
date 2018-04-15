package com.ohmcoe.vultr


import android.os.Parcel
import android.os.Parcelable
import com.ohmcoe.vultr.model.Snapshot
import org.json.JSONException
import org.json.JSONObject


class SnapshotList() : Parcelable{

    var snapshotList: ArrayList<Snapshot>? = null

    constructor(parcel: Parcel) : this() {

    }

    constructor(response: String) : this() {
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

    fun toList(): List<Snapshot>? {
        val list = snapshotList

        return list
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SnapshotList> {
        override fun createFromParcel(parcel: Parcel): SnapshotList {
            return SnapshotList(parcel)
        }

        override fun newArray(size: Int): Array<SnapshotList?> {
            return arrayOfNulls(size)
        }
    }
}