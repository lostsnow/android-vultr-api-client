package com.ohmcoe.vultr;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServerFragment extends Fragment {

    View serverView;
    private ProgressDialog progressDialog;
    ServerList serverList;

    TextView txtIP;
    TextView txtRam;
    TextView txtLabel;
    TextView txtOS;
    TextView txtState;
    TextView txtPendingCharges;
    TextView txtBandwidth;
    TextView txtBandwidthHistory;
    TextView txtInbound;
    TextView txtOutbound;
    Button btnReload;
    Bandwidth bandwidth;

    private String APIKey;
    private String SUBID;

    private MyToast myToast;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        serverView = inflater.inflate(R.layout.server_layout, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait while loading...");
        progressDialog.setCancelable(false);

        myToast = new MyToast(getContext(), "");

        txtIP = (TextView) serverView.findViewById(R.id.txtIP);
        txtRam = (TextView) serverView.findViewById(R.id.txtRam);
        txtLabel = (TextView) serverView.findViewById(R.id.txtLabel);
        txtOS = (TextView) serverView.findViewById(R.id.txtOs);
        txtState = (TextView) serverView.findViewById(R.id.txtState);
        txtPendingCharges = (TextView) serverView.findViewById(R.id.txtPendingCharges);
        txtBandwidth = (TextView) serverView.findViewById(R.id.txtBandwidth);
        btnReload = (Button) serverView.findViewById(R.id.btnReload);

        //bandwidth view
        txtInbound = (TextView) serverView.findViewById(R.id.txtInbound);
        txtOutbound = (TextView) serverView.findViewById(R.id.txtOutbound);
        txtBandwidthHistory = (TextView) serverView.findViewById(R.id.txtBandwidthHistory);
        /* bandwidthGraph = (LinearLayout) serverView.findViewById(R.id.bandwidhGraph);*/

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getServerList();
            }
        });

        txtBandwidthHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBandwidthGraph();
            }
        });

        Bundle bundle = getArguments();
        APIKey = bundle.getString("API-Key");
        SUBID = bundle.getString("SUBID");

        getServerList();

        return serverView;
    }

    protected void updateUI() {
        if (serverList != null) {
            for (Server server : serverList.getServerLists()) {
                if (server.getSUBID().equals(SUBID)) {
                    txtIP.setText(server.getMain_ip());
                    txtRam.setText(server.getRam());
                    txtLabel.setText(server.getLabel());
                    txtOS.setText(server.getOs());
                    txtState.setText(server.getServer_state());
                    txtPendingCharges.setText(server.getStrPendingCharges());
                    txtBandwidth.setText(server.getBandwidth());
                    btnReload = (Button) serverView.findViewById(R.id.btnReload);
                }
            }
        }

        updateBandwidthUI();
    }

    protected void showBandwidthGraph() {
        if (bandwidth == null)
            updateBandwidthUI();

        Intent intent = new Intent(getContext(), BandwidthGraphActivity.class);
        intent.putExtra("x", bandwidth.getXGraph());
        intent.putExtra("dates", bandwidth.getDates());
        intent.putExtra("inbound", bandwidth.getInboundGraph());
        intent.putExtra("outbound", bandwidth.getOutboundGraph());

        startActivity(intent);
    }

    protected void updateBandwidthUI() {
        if (bandwidth != null) {
            //update inbound
            txtInbound.setText(this.humanByte(bandwidth.getSumInbound()));
            txtOutbound.setText(this.humanByte(bandwidth.getSumOutbound()));
        }
    }


    private String humanByte(Double bytes) {
        String postfix = "B";

        if (bytes > 1024.0) {
            bytes /= 1024.0;
            postfix = "KB";
            if (bytes > 1024.0) {
                bytes /= 1024.0;
                postfix = "MB";
                if (bytes > 1024.0) {
                    bytes /= 1024.0;
                    postfix = "GB";
                }
            }
        }

        DecimalFormat df = new DecimalFormat("0.00");
        String strByte = df.format(bytes);
        return strByte + " " + postfix;
    }

    protected void getBandwidth() {
        progressDialog.show();

        RetrofitClient retrofitClient = new RetrofitClient(getString(R.string.base_uri));
        Retrofit retrofit = retrofitClient.getRetrofit();

        VultrClient client = retrofit.create(VultrClient.class);
        Call<Bandwidth> call = client.getServerBandwidth(APIKey, SUBID);

        call.enqueue(new Callback<Bandwidth>() {
            @Override
            public void onResponse(Call<Bandwidth> call, Response<Bandwidth> response) {
                bandwidth = response.body();
                updateUI();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Bandwidth> call, Throwable t) {
                progressDialog.dismiss();

                myToast.setText("Connection failure");
                myToast.show();
            }
        });

    }

    protected void getServerList() {
        progressDialog.show();

        RetrofitClient retrofitClient = new RetrofitClient(getString(R.string.base_uri));
        Retrofit retrofit = retrofitClient.getRetrofit();

        VultrClient client = retrofit.create(VultrClient.class);
        Call<ResponseBody> call = client.getServerList(APIKey);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200) {
                        String body = response.body().string();
                        serverList = new ServerList(body);
                        updateUI();
                    } else {
                        String text = "Loading Failure response code " + response.code();
                        myToast.setText(text);
                        myToast.show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                myToast.setText("Connection failure");
                myToast.show();
            }
        });

        getBandwidth();
    }
}
