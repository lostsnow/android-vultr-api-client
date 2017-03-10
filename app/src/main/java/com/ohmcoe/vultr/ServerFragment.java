package com.ohmcoe.vultr;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerFragment extends Fragment {

    View serverView;
    private ProgressDialog progressDialog;
    private String APIKey;
    ArrayList<Server> serverLists;

    TextView txtIP;
    TextView txtRam;
    TextView txtLabel;
    TextView txtOS;
    TextView txtState;
    TextView txtPendingCharges;
    TextView txtBandwidth;
    Button btnReload;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        serverView = inflater.inflate(R.layout.server_layout, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait while loading...");
        progressDialog.setCancelable(false);

        txtIP = (TextView)serverView.findViewById(R.id.txtIP);
        txtRam = (TextView)serverView.findViewById(R.id.txtRam);
        txtLabel = (TextView)serverView.findViewById(R.id.txtLabel);
        txtOS = (TextView)serverView.findViewById(R.id.txtOs);
        txtState = (TextView)serverView.findViewById(R.id.txtState);
        txtPendingCharges = (TextView)serverView.findViewById(R.id.txtPendingCharges);
        txtBandwidth = (TextView)serverView.findViewById(R.id.txtBandwidth);
        btnReload = (Button)serverView.findViewById(R.id.btnReload);

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getServerList();
            }
        });

        serverLists = new ArrayList<Server>();

        Bundle bundle = getArguments();
        APIKey = bundle.getString("API-Key");

        getServerList();

        return serverView;
    }

    protected void updateUI()
    {
/*        Server server = serverLists.get(0);
        txtIP.setText(server.getMain_ip());
        txtOS.setText(server.getOs());
        txtRam.setText(server.getRam());
        txtLabel.setText(server.getLabel());
        txtState.setText(server.getServer_state());

        String addCurrency = "$" + server.getPending_charges();
        txtPendingCharges.setText(addCurrency);

        Double currentBandwidth = Double.parseDouble(server.getCurrent_bandwidth_gb());
        Double allowBandwidth = Double.parseDouble(server.getAllowed_bandwidth_gb());

        String bandwidth = currentBandwidth + " GB";
        bandwidth += " of ";
        bandwidth += allowBandwidth + " GB";
        bandwidth += "( " + (100/allowBandwidth*currentBandwidth) + "%)";
        txtBandwidth.setText(bandwidth);*/
    }

    protected void getServerList() {
        progressDialog.show();

        serverLists.clear();

        String API_BASE_URL = getResources().getString(R.string.base_uri);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder
                .client(httpClient.build())
                .build();

        VultrClient client = retrofit.create(VultrClient.class);
        Call<ResponseBody> call = client.getServerList(APIKey);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
/*                try {
                    if (response.code() == 200) {
                        String body = response.body().string();
                        ServerList serverList = new ServerList(body);
                        serverLists = serverList.getServerList();
                        updateUI();
                    }
                    else
                    {
                        CharSequence text = "Loading Failure response code " + response.code();
                        Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
                        toast.show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
