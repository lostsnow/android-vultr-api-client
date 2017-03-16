package com.ohmcoe.vultr;


import android.content.Context;
import android.widget.Toast;

class MyToast {
    private Context toastContext;
    private CharSequence text;

    MyToast(Context context, String str) {
        toastContext = context;
        text = str;
    }

    public void setText(String str)
    {
        text = str;
    }

    public void show() {
        Toast toast = Toast.makeText(toastContext, text, Toast.LENGTH_LONG);
        toast.show();
    }
}
