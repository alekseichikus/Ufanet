package com.example.ufanet.edit.model;

import android.util.Log;

import com.example.ufanet.Json.JsonPlaceHolderApi2;
import com.example.ufanet.Json.JsonPlaceHolderApi3;
import com.example.ufanet.templates.Config;
import com.example.ufanet.templates.ResponseCode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.ufanet.utils.Constants.APP_PREFERENCES_IP_DEVICES;

public class EditModel implements IEditModel {
    @Override
    public void sendConfig(OnFinishedListener onFinishedListener, String wlan_ssid, String wlan_pass
            , String timezone, long time, Integer[] config1) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APP_PREFERENCES_IP_DEVICES)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi2 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi2.class);
        Config config = new Config(wlan_ssid, wlan_pass, timezone, time, config1);
        Call<Config> call1 = jsonPlaceHolderApi.getMyJSON(config);
        call1.enqueue(new Callback<Config>() {
            @Override
            public void onResponse(Call<Config> call, Response<Config> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.code() == 200) {
                    Log.d("config_sendConfig", "save success");
                    restartDevice(onFinishedListener);
                }
            }
            @Override
            public void onFailure(Call<Config> call, Throwable t) {
                Log.d("failture_config", t.toString());
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
        Call<ResponseCode> call1 = jsonPlaceHolderApi.getMyJSON();
        call1.enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.code() == 200) {
                    Log.d("config_restartDevice", "save success");
                    onFinishedListener.onFinished();
                }

            }
            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                //onFinishedListener.onFinished(wlan_ssid, wlan_pass, bluetooth, wiegand, dallas, gerkon, button, lock, lock_invert, lock_time, buzzer_case, buzzer_gerkon, buzzer_key, buzzer_lock, time);
                onFinishedListener.onFailure(t);
                Log.d("failture", t.toString());
            }
        });
    }
}
