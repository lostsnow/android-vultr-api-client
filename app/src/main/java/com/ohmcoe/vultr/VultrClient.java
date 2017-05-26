package com.ohmcoe.vultr;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface VultrClient {
    @GET("/v1/account/info")
    Call<Account> getAccount(
            @Header("API-Key") String api_key
    );

    //server
    @GET("/v1/server/list")
    Call<ResponseBody> getServerList(
            @Header("API-Key") String api_key
    );

    //bandwidth
    @GET("/v1/server/bandwidth")
    Call<Bandwidth> getServerBandwidth(
            @Header("API-Key") String api_key,
            @Query("SUBID") String subid
    );

    //snapshot
    @GET("/v1/snapshot/list")
    Call<ResponseBody> getSnapshotList(
            @Header("API-Key") String api_key
    );

}
