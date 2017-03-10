package com.ohmcoe.vultr;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SetAPIKeyFragment extends Fragment {
    View setAPIKeyView;
    EditText txtAPIKey;
    Button btnOK;
    private String APIKey;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setAPIKeyView = inflater.inflate(R.layout.fragment_set_apikey, container, false);

        txtAPIKey = (EditText)setAPIKeyView.findViewById(R.id.txtAPIKey);
        btnOK = (Button)setAPIKeyView.findViewById(R.id.btnOK);

        btnOK.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                updateAPIKey();
            }
        });

        Bundle bundle = getArguments();
        APIKey = bundle.getString("API-Key");
        txtAPIKey.setText(APIKey);

        return setAPIKeyView;
    }


    public void updateAPIKey()
    {
        String fileName = NavigationActivity.configFile;
        FileOutputStream outputStream;

        try {
            outputStream = getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(txtAPIKey.getText().toString().getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent in = new Intent(getContext(), NavigationActivity.class);
        startActivity(in);
    }
}
