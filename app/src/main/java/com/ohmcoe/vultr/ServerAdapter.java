package com.ohmcoe.vultr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by OHM on 10/3/2560.
 */

public class ServerAdapter extends ArrayAdapter<Server> {

    public ServerAdapter(Context context, int resource) {
        super(context, resource);
    }


    public ServerAdapter(Context context, int resource, List<Server> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        Server server = getItem(position);

        if (view == null)
        {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.server_list, null);
        }

        if (server != null)
        {
            TextView txtServerName = (TextView)view.findViewById(R.id.txtServerName);
            TextView txtPendingCharges = (TextView)view.findViewById(R.id.txtPendingCharges);
            TextView txtIP = (TextView)view.findViewById(R.id.txtIP);

            if (txtServerName != null)
            {
                txtServerName.setText(server.getLabel());
            }

            if (txtPendingCharges != null)
            {
                String pendingCharges = "$" + server.getPending_charges();
                txtPendingCharges.setText(pendingCharges);
            }

            if (txtIP != null)
            {
                txtIP.setText(server.getMain_ip());
            }
        }

        return view;
    }
}
