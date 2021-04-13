package com.example.ufanet.Json;

import com.example.ufanet.templates.ResponseCode;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi3 {

    @POST("restart")
    @Headers({"Content-Type: application/json"})
    Call<ResponseCode> getMyJSON();
}
