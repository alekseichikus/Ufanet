package com.example.ufanet.Json;

import com.example.ufanet.templates.Config;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi3 {

    @POST("restart")
    Call<String> getMyJSON();
}
