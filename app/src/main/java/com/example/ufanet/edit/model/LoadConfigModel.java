package com.example.ufanet.edit.model;

import com.example.ufanet.Json.JsonPlaceHolderApi2;
import com.example.ufanet.Json.JsonPlaceHolderApi3;
import com.example.ufanet.templates.ResponseCode;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_IP_DEVICES;

public class LoadConfigModel implements ILoadConfigModel {
    @Override
    public void sendConfig(OnFinishedListener onFinishedListener, String config) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APP_PREFERENCES_IP_DEVICES)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi2 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi2.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), config.getBytes());
        Call<ResponseCode> call = jsonPlaceHolderApi.getMyJSON(body);
        call.enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.code() == 200) {
                    restartDevice(onFinishedListener);
                }
            }
            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }

    @Override
    public void restartDevice(OnFinishedListener onFinishedListener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APP_PREFERENCES_IP_DEVICES)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi3 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi3.class);
        Call<ResponseCode> call = jsonPlaceHolderApi.getMyJSON();
        call.enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.code() == 200) {
                    onFinishedListener.onFinished();
                }
            }
            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }
}
