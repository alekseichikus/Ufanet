package com.example.ufanet.keylist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ufanet.Json.JsonPlaceHolderApi4;
import com.example.ufanet.R;
import com.example.ufanet.edit.EditActivity;
import com.example.ufanet.edit.model.EditModel;
import com.example.ufanet.settings.ISettingView;
import com.example.ufanet.settings.presenter.ISettingPresenter;
import com.example.ufanet.settings.presenter.SettingPresenter;
import com.example.ufanet.utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KeyListActivity extends AppCompatActivity implements ISettingView {

    CardView closeButtonCV;

    ISettingPresenter presenter;

    EditModel editModel = new EditModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUI();
        setListeners();

        presenter = new SettingPresenter(this);
    }

    void initUI(){
        closeButtonCV = findViewById(R.id.cv_close_button);
    }

    void setListeners(){
        closeButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onResponse(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.d("failture", throwable.toString());
    }

    @Override
    public ISettingPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void closeView() {
        finish();
    }
}
