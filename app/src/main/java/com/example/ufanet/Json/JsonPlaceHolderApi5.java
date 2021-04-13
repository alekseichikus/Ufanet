package com.example.ufanet.Json;

import com.example.ufanet.templates.ResponseCode;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi5 {

    @POST("keys")
    @Headers({"Content-Type: application/octet-stream"})
    Call<ResponseCode> getMyJSON(@Body RequestBody keys);
}
