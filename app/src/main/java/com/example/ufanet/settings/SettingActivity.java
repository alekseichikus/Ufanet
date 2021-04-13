package com.example.ufanet.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.ufanet.R;
import com.example.ufanet.addConfig.AddConfigActivity;
import com.example.ufanet.configList.ConfigListActivity;
import com.example.ufanet.edit.LoadConfigActivity;
import com.example.ufanet.home.addKey.AddKeyActivity;
import com.example.ufanet.keyss.KeyListActivity;
import com.example.ufanet.settings.presenter.ISettingPresenter;
import com.example.ufanet.settings.presenter.SettingPresenter;

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

    void setListeners(){
        settingButtonCV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, LoadConfigActivity.class);
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
                finish();
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
