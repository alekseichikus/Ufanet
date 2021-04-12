package com.example.ufanet.Json;

import com.example.ufanet.templates.Config;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi2 {

    @POST("config")
    @Headers({"Content-Type: application/json"})
    Call<Config> getMyJSON(@Body Config config);
}

//wlan_ssid="SCUD"
//        wlan_pass="1234567890"
//        unixtime="1617786779"
//        timezone="CST-5"
//        master="65507"
//        slave_1="65507"
//        slave_2="65507"
//        slave_3="65507"
//        slave_4="65507"
//        slave_5="65507"
//        slave_6="65507"
//        slave_7="65507"