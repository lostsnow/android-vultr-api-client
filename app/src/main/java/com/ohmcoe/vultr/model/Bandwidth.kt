package com.ohmcoe.vultr.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.text.DecimalFormat

class Bandwidth() : Parcelable {

    @SerializedName("incoming_bytes")
    @Expose
    var incomingBytes: List<List<String>>? = null
    @SerializedName("outgoing_bytes")
    @Expose
    var outgoingBytes: List<List<String>>? = null

    val sumInbound: Double
        get() {
            var totalByte = 0.0

            for (inbound in incomingBytes!!) {
                totalByte += java.lang.Double.parseDouble(inbound[1])
            }

            return totalByte
        }

    val sumOutbound: Double
        get() {
            var totalByte = 0.0

            for (inbound in outgoingBytes!!) {
                totalByte += java.lang.Double.parseDouble(inbound[1])
            }

            return totalByte
        }

    val dates: Array<String?>
        get() {
            val size = incomingBytes!!.size
            val dates = arrayOfNulls<String>(size)
            var count = 0

            for (inbound in incomingBytes!!) {
                dates[count] = inbound[0]
                count++
            }

            return dates
        }

    val xGraph: IntArray
        get() {
            val size = incomingBytes!!.size
            val x = IntArray(size)

            for (i in 0..size - 1) {
                x[i] = i + 1
            }

            return x
        }

    val inboundGraph: DoubleArray
        get() {
            val size = incomingBytes!!.size
            val inbound = DoubleArray(size)
            var count = 0

            val df = DecimalFormat("0.00")

            for (incoming in incomingBytes!!) {
                var dIncoming: Double? = java.lang.Double.parseDouble(incoming[1]) / 1048576
                dIncoming = java.lang.Double.parseDouble(df.format(dIncoming))
                inbound[count] = dIncoming
                count++
            }

            return inbound
        }

    val outboundGraph: DoubleArray
        get() {
            val size = outgoingBytes!!.size
            val outbound = DoubleArray(size)
            var count = 0
            val df = DecimalFormat("0.00")

            for (outgoing in outgoingBytes!!) {
                var dOutgoing: Double? = java.lang.Double.parseDouble(outgoing[1]) / 1048576
                dOutgoing = java.lang.Double.parseDouble(df.format(dOutgoing))
                outbound[count] = dOutgoing
                count++
            }

            return outbound
        }

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Bandwidth> {
        override fun createFromParcel(parcel: Parcel): Bandwidth {
            return Bandwidth(parcel)
        }

        override fun newArray(size: Int): Array<Bandwidth?> {
            return arrayOfNulls(size)
        }
    }
}