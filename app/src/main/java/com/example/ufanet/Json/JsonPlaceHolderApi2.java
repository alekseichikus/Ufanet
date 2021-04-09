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
