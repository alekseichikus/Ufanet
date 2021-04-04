package com.example.ufanet.settings;

import android.content.Context;
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
import com.example.ufanet.edit.presenter.IEditPresenter;
import com.example.ufanet.settings.presenter.ISettingPresenter;
import com.example.ufanet.settings.presenter.SettingPresenter;
import com.example.ufanet.utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingActivity extends AppCompatActivity implements ISettingView {

    CardView settingButtonCV;
    CardView reloadButtonCV;
    CardView updateButtonCV;
    CardView addKeyButtonCV;
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
        settingButtonCV = findViewById(R.id.cv_setting_button);
        reloadButtonCV = findViewById(R.id.cv_reload_button);
        updateButtonCV = findViewById(R.id.cv_update_device_button);
        addKeyButtonCV = findViewById(R.id.cv_add_key_button);
        closeButtonCV = findViewById(R.id.cv_close_button);
    }

    void setListeners(){
        settingButtonCV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, EditActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
        reloadButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().requestReloadDevice();
            }
        });

        addKeyButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    read_file("keys.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        closeButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = new File("вот тут директория");
                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

                JsonPlaceHolderApi4 getResponse = AppConfig.getRetrofit().create(JsonPlaceHolderApi4.class);
                Call call = getResponse.upload(fileToUpload);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        ServerResponse serverResponse = (ServerResponse) response.body();
                        if (serverResponse != null) {
                            Toast.makeText(getApplicationContext(), serverResponse.getStatus(),Toast.LENGTH_SHORT).show();
                        } else {
                            assert serverResponse != null;
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.d("fwefwefw", t.toString());
                    }
                });
            }
        });
    }

    public void read_file(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                getAssets().open(filename)));

        int i=0;
        String line = reader.readLine();
        while (line != null) {
            Log.d("key", line);
            line = reader.readLine();
        }

        reader.close();
    }

    class ServerResponse {
        @SerializedName("status")
        String status;

        String getStatus() {
            return status;
        }
    }

    static class AppConfig {

        private static String BASE_URL = Constants.APP_PREFERENCES_IP_DEVICES;
        static Retrofit getRetrofit() {
            return new Retrofit.Builder()
                    .baseUrl(AppConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
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
