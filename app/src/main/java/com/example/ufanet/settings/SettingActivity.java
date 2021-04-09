package com.example.ufanet.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ufanet.Json.JsonPlaceHolderApi4;
import com.example.ufanet.R;
import com.example.ufanet.addConfig.AddConfigActivity;
import com.example.ufanet.configList.ConfigListActivity;
import com.example.ufanet.edit.EditActivity;
import com.example.ufanet.edit.model.EditModel;
import com.example.ufanet.home.addKey.AddKeyActivity;
import com.example.ufanet.keyss.KeyListActivity;
import com.example.ufanet.settings.presenter.ISettingPresenter;
import com.example.ufanet.settings.presenter.SettingPresenter;
import com.example.ufanet.utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    CardView keyListButtonCV;
    CardView closeButtonCV;
    CardView addConfigButtonCV;
    CardView configListButtonCV;

    final String FILENAME = "file";

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
        keyListButtonCV = findViewById(R.id.cv_keys_list_button);
        addKeyButtonCV = findViewById(R.id.cv_add_key_button);
        closeButtonCV = findViewById(R.id.cv_close_button);
        addConfigButtonCV = findViewById(R.id.cv_add_config_button);
        configListButtonCV = findViewById(R.id.cv_config_list_button);
    }

    void writeFile() {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_PRIVATE)));
            // пишем данные
            bw.write("Содержимое файла");
            // закрываем поток
            bw.close();
            Log.d("sdas", "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readFile() {
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                Log.d("sdas", str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setListeners(){
        settingButtonCV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, EditActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        keyListButtonCV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, KeyListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        addConfigButtonCV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, AddConfigActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        configListButtonCV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ConfigListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
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
                Intent intent = new Intent(SettingActivity.this, AddKeyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
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
