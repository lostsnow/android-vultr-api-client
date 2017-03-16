package com.ohmcoe.vultr;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServerListFragment extends Fragment{

    View serverListLayout;
    private ProgressDialog progressDialog;
    private ServerList serverList;
    private String APIKey;
    private ListView txtServerList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        serverListLayout = inflater.inflate(R.layout.fragment_server_list, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait while loading...");
        progressDialog.setCancelable(false);

        Bundle bundle = getArguments();
        APIKey = bundle.getString("API-Key");


        prepareServerList();


        return serverListLayout;
    }

    protected void prepareServerList()
    {
        serverList = new ServerList();
        txtServerList = (ListView)serverListLayout.findViewById(R.id.txtServerList);
        getServerList();
    }

    protected void updateUI()
    {
        ServerAdapter serverAdapter = new ServerAdapter(getContext(), R.layout.server_list, serverList.toList());
        txtServerList.setAdapter(serverAdapter);
        txtServerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Server server = (Server)parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("API-Key", APIKey);
                bundle.putString("SUBID", server.getSUBID());
                Fragment serverFragment = new ServerFragment();
                serverFragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, serverFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    protected void getServerList() {
        progressDialog.show();

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
                try {
                    if (response.code() == 200) {
                        String body = response.body().string();
                        serverList = new ServerList(body);
                        updateUI();
                    }
                    else
                    {
                        CharSequence text = "Loading Failure response code " + response.code();
                        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
                        toast.show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
