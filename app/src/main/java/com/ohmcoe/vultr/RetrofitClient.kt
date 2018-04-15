package com.ohmcoe.vultr


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient(BASE_URI: String) {

    val retrofit: Retrofit

    companion object {
        val CONNECT_TIMEOUT:Long = 5
        val WRITE_TIMEOUT:Long = 20
        val READ_TIMEOUT:Long= 20
    }

    init {
        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(RetrofitClient.CONNECT_TIMEOUT, TimeUnit.SECONDS)
        httpClient.readTimeout(RetrofitClient.WRITE_TIMEOUT, TimeUnit.SECONDS)
        httpClient.writeTimeout(RetrofitClient.READ_TIMEOUT, TimeUnit.SECONDS)


        val builder = Retrofit.Builder()
                .baseUrl(BASE_URI)
                .addConverterFactory(GsonConverterFactory.create())

        retrofit = builder
                .client(httpClient.build())
                .build()
    }
}
