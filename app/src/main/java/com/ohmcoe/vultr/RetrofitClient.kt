package com.ohmcoe.vultr

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient(BASE_URI: String) {

    val retrofit: Retrofit


    init {
        val httpClient = OkHttpClient.Builder()

        val builder = Retrofit.Builder()
                .baseUrl(BASE_URI)
                .addConverterFactory(GsonConverterFactory.create())

        retrofit = builder
                .client(httpClient.build())
                .build()
    }
}
