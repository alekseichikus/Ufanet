package com.example.ufanet.home.addKey.model;

import android.util.Log;

import com.example.ufanet.Json.JsonPlaceHolderApi5;
import com.example.ufanet.templates.ResponseCode;
import okhttp3.MediaType;
import okhttp3.RequestBody;
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

        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), keys.getBytes());

        Call<ResponseCode> call = jsonPlaceHolderApi.getMyJSON(body);
        call.enqueue(new Callback<ResponseCode>() {
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
                Log.d("failture_config", t.toString());
                onFinishedListener.onFailure(t);
            }
        });
    }
}
