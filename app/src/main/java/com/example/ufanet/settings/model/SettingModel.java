package com.example.ufanet.settings.model;

import android.util.Log;

import com.example.ufanet.Json.JsonPlaceHolderApi3;
import com.example.ufanet.templates.ResponseCode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_IP_DEVICES;

public class SettingModel implements ISettingModel {

    @Override
    public void restartDevice(OnFinishedListener onFinishedListener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APP_PREFERENCES_IP_DEVICES)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi3 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi3.class);
        Call<ResponseCode> call1 = jsonPlaceHolderApi.getMyJSON();
        call1.enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.code() == 200) {
                    Log.d("config", "save success");
                    onFinishedListener.onFinished();
                }
            }
            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                onFinishedListener.onFailure(t);
                Log.d("failture", t.toString());
            }
        });
    }
}
