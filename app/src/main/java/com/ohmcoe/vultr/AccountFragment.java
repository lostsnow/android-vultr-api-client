package com.ohmcoe.vultr;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountFragment extends Fragment {
    View accountView;

    private TextView txtCurrentBalance;
    private TextView txtPendingCharges;
    private TextView txtRemainingBalance;
    private ProgressDialog progressDialog;
    private Button btnReload;

    private String APIKey;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //setView
        accountView = inflater.inflate(R.layout.account_layout, container, false);

        //bind xml
        txtCurrentBalance = (TextView) accountView.findViewById(R.id.txtCurrentBalance);
        txtPendingCharges = (TextView) accountView.findViewById(R.id.txtPendingCharges);
        txtRemainingBalance = (TextView) accountView.findViewById(R.id.txtRemainBalance);
        btnReload = (Button) accountView.findViewById(R.id.btnReload);

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newOnClickReload();
            }
        });

        //create waiting dialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait while loading...");
        progressDialog.setCancelable(false);

        Bundle bundle = getArguments();
        APIKey = bundle.getString("API-Key");

        getAccount();

        return accountView;
    }

    protected void newOnClickReload() {
        getAccount();
    }

    protected void refreshAccountUI(Response<Account> response) {
        Account account = response.body();


        double currentBalance = Double.parseDouble(account.getBalance());
        double pendingCharge = Double.parseDouble(account.getPendingCharges());
        double remainBalance = currentBalance + pendingCharge;


        DecimalFormat df = new DecimalFormat("0.00");

        String stringCurrentBalance = "$" + df.format(currentBalance * -1);
        String stringPendingCharges = "$" + df.format(pendingCharge);
        String stringRemainBalance = "$" + df.format(remainBalance * -1);


        txtCurrentBalance.setText(stringCurrentBalance);
        txtPendingCharges.setText(stringPendingCharges);
        txtRemainingBalance.setText(stringRemainBalance);

        progressDialog.dismiss();
    }

    protected void getAccount() {

        progressDialog.show();

        RetrofitClient retrofitClient = new RetrofitClient(getString(R.string.base_uri));
        Retrofit retrofit = retrofitClient.getRetrofit();

        VultrClient client = retrofit.create(VultrClient.class);
        Call<Account> call = client.getAccount(this.APIKey);

        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.code() == 200) {
                    refreshAccountUI(response);
                }
                else {
                    progressDialog.dismiss();
                    CharSequence text = "Loading Failure";
                    Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                txtCurrentBalance.setText(t.getMessage());
            }
        });
    }
}
