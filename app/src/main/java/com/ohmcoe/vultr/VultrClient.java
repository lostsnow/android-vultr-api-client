package com.ohmcoe.vultr;

import com.ohmcoe.vultr.model.Account;
import com.ohmcoe.vultr.model.Bandwidth;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

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

    //start
    @POST("/v1/server/start")
    @FormUrlEncoded
    Call<ResponseBody> startServer(
            @Header("API-Key") String api_key,
            @Field("SUBID") String subid
    );

    //stop
    @POST("/v1/server/halt")
    @FormUrlEncoded
    Call<ResponseBody> stopServer(
            @Header("API-Key") String api_key,
            @Field("SUBID") String subid
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
