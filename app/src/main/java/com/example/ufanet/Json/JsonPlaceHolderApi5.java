package com.example.ufanet.Json;

import com.example.ufanet.settings.IKey;
import com.example.ufanet.templates.Config;
import com.example.ufanet.templates.ResponseCode;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi5 {

    @POST("keys")
    @Headers({"Content-Type: text/plain"})
    Call<ResponseCode> getMyJSON(@Body String keys);
}
