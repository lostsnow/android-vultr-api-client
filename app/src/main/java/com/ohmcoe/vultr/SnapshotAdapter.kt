package com.ohmcoe.vultr

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.ohmcoe.vultr.model.Snapshot
import kotlinx.android.synthetic.main.snapshot_list.view.*

class SnapshotAdapter(context: Context, resource: Int, objects: List<Snapshot>) : ArrayAdapter<Snapshot>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        val snapshot = getItem(position)

        if (view == null) {
            val layoutInflater: LayoutInflater
            layoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.snapshot_list, null)
        }

        if (snapshot != null) {
            val txtSnapshotId = view!!.txtSnapshotID
            val txtDateCreate = view.txtDate
            val txtStatus = view.txtStatus
            val txtDescription = view.txtDescription
            val txtSize = view.txtSize

            if (txtSnapshotId != null) {
                txtSnapshotId.text = snapshot.snapshotID
            }

            if (txtDateCreate != null) {
                txtDateCreate.text = snapshot.date_created
            }

            if (txtStatus != null) {
                txtStatus.text = snapshot.status
            }

            if (txtDescription != null) {
                txtDescription.text = snapshot.description
            }

            if (txtSize != null) {
                val size = snapshot.size!!.toDouble()
                txtSize.text = String.format("%.2f GB", size/1024/1024/1024)
            }
        }

        return view!!
    }
}
