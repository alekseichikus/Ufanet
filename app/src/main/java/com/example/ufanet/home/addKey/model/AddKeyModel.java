package com.example.ufanet.home.addKey.model;

import android.util.Log;

import com.example.ufanet.Json.JsonPlaceHolderApi2;
import com.example.ufanet.Json.JsonPlaceHolderApi3;
import com.example.ufanet.Json.JsonPlaceHolderApi5;
import com.example.ufanet.edit.model.IEditModel;
import com.example.ufanet.settings.IKey;
import com.example.ufanet.templates.Config;
import com.example.ufanet.templates.ResponseCode;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.ufanet.utils.Constants.APP_PREFERENCES_IP_DEVICES;

public class AddKeyModel implements IAddKeyModel {

    @Override
    public void sendKeys(OnFinishedListener onFinishedListener, String keys) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APP_PREFERENCES_IP_DEVICES)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi5 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi5.class);
        Call<ResponseCode> call1 = jsonPlaceHolderApi.getMyJSON(keys);
        call1.enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.code() == 200) {
                    Log.d("config", "save success");
                    onFinishedListener.onFinished(keys);
                }
            }
            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d("failture_config", t.toString());
                onFinishedListener.onFailure(t);
            }
        });
    }
}
