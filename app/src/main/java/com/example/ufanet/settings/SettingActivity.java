package com.example.ufanet.settings;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.example.ufanet.R;
import com.example.ufanet.WifiConnectAppCompatActivity;
import com.example.ufanet.addConfig.AddConfigActivity;
import com.example.ufanet.configList.ConfigListActivity;
import com.example.ufanet.edit.LoadConfigActivity;
import com.example.ufanet.home.PermActivity;
import com.example.ufanet.home.addKey.AddKeyActivity;
import com.example.ufanet.keyss.KeyListActivity;
import com.example.ufanet.settings.presenter.ISettingPresenter;
import com.example.ufanet.settings.presenter.SettingPresenter;
import com.example.ufanet.utils.MemoryOperation;

import java.util.ArrayList;
import java.util.List;

import static com.example.ufanet.utils.Constants.APP_PREFERENCES_PASSWORD_DEVICES;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_SSID_DEVICES;

public class SettingActivity extends WifiConnectAppCompatActivity implements ISettingView {

    CardView settingButtonCV;
    CardView updateButtonCV;
    CardView addKeyButtonCV;
    CardView keyListButtonCV;
    CardView addConfigButtonCV;
    CardView configListButtonCV;

    ISettingPresenter presenter;

    Toolbar toolbar;

    MemoryOperation memoryOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUI();
        setListeners();

        presenter = new SettingPresenter(this);

        memoryOperation = new MemoryOperation(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Настройки");
        toolbar.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance);

        addBackButton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if(wifiManager != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // ???
            }
            else{
                wifiManager.disconnect();
            }
        }
    }

    private void addBackButton(){
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_r);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void initUI(){
        settingButtonCV = findViewById(R.id.cv_setting_button);
        updateButtonCV = findViewById(R.id.cv_update_device_button);
        keyListButtonCV = findViewById(R.id.cv_keys_list_button);
        addKeyButtonCV = findViewById(R.id.cv_add_key_button);
        addConfigButtonCV = findViewById(R.id.cv_add_config_button);
        configListButtonCV = findViewById(R.id.cv_config_list_button);
    }

    void setListeners(){
        settingButtonCV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, LoadConfigActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
            }
        });

        keyListButtonCV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, KeyListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
            }
        });

        addConfigButtonCV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, AddConfigActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
            }
        });

        configListButtonCV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ConfigListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
            }
        });

        addKeyButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, AddKeyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_reload) {
            getPresenter().requestReloadDevice();
            finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }
}
