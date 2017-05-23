package com.ohmcoe.vultr


import android.content.Context
import android.widget.Toast

internal class MyToast(private val toastContext: Context, str: String) {

    private var text: CharSequence? = null

    init {
        text = str
    }

    fun setText(str: String) {
        text = str
    }

    fun show() {
        val toast = Toast.makeText(toastContext, text, Toast.LENGTH_LONG)
        toast.show()
    }
}
