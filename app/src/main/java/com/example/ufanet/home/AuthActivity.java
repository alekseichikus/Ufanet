package com.example.ufanet.home;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ufanet.BeaconBluetooth;
import com.example.ufanet.R;
import com.example.ufanet.WifiReceiver;
import com.example.ufanet.utils.MemoryOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AuthActivity extends AppCompatActivity {

    MemoryOperation memoryOperation;
    ConstraintLayout imageView;
    CardView circleCV;
    CardView rectangleCV;
    CardView closeCV;
    CardView buttonCV;
    TextView titleTV;
    TextView postTitleTV;
    ImageView logoIV;
    LinearLayout contentLL;

    EditText loginUserET;
    EditText passwordUserET;

    ImageView saveButtonImageIV;
    TextView saveButtonTextTV;

    private BeaconBluetooth beaconBluetooth;
    public WifiReceiver wifiResiver;

    WifiManager wifiManager;

    public static final String APP_PREFERENCES_LOGIN_USER = "login_user";
    public static final String APP_PREFERENCES_PASSWORD_USER = "password_user";

    public final Integer REQUEST_PERMISSION_CODE = 1;

    IntentFilter intentFilter;

    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        imageView = findViewById(R.id.image);
        circleCV = findViewById(R.id.circle);
        rectangleCV = findViewById(R.id.rectangle);
        contentLL = findViewById(R.id.ll_content);
        closeCV = findViewById(R.id.cv_close_button);
        logoIV = findViewById(R.id.imageView);
        buttonCV = findViewById(R.id.cv_button);
        titleTV = findViewById(R.id.tv_title);
        postTitleTV = findViewById(R.id.tv_posttitle);

        loginUserET = findViewById(R.id.et_login);
        passwordUserET = findViewById(R.id.et_password);

        saveButtonImageIV = findViewById(R.id.iv_loading);
        saveButtonTextTV = findViewById(R.id.tv_loading);

        beaconBluetooth = new BeaconBluetooth(this);
        wifiResiver = new WifiReceiver(this);

        memoryOperation = new MemoryOperation(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        setSSIDDeviceET();
        setPasswordDeviceET();

        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        window.setAttributes(winParams);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setListeners();
        initWifi();

        logoIV.setImageResource(R.drawable.ic_user_alt);

        blockAnimate();
    }

    public void setSSIDDeviceET(){
        loginUserET.setText(memoryOperation.getLoginUser());
    }

    public void setPasswordDeviceET(){
        passwordUserET.setText(memoryOperation.getPasswordUser());
    }

    private void initWifi(){
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
    }

    private void setListeners(){
        closeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGeoDisabled(AuthActivity.this)) {
                    beaconBluetooth.initBluetooth();
                    if(isBluetoothEnabled()){
                        if(isWifiEnabled(AuthActivity.this)){
                            if(!isEmptyString(loginUserET.getText().toString())){
                                if(!isEmptyString(passwordUserET.getText().toString())){
                                    updateDeviceLogPass();
                                    beaconBluetooth.startBeacon();
                                    setLoadingButton();
                                }
                                else{
                                    onResponse("Введите пароль");
                                }
                            }
                            else{
                                onResponse("Введите логин");
                            }
                        }
                        else{
                            onResponse("Включите Wifi");
                        }
                    }
                    else{
                        onResponse("Вы отключили Bluetooth, включите)");
                    }
                }
                else{
                    onResponse("Предоставьте доступ к геолокации");
                }
            }
        });
    }

    public void updateDeviceLogPass(){
        memoryOperation.setLoginUser(loginUserET.getText().toString());
        memoryOperation.setPasswordUser(passwordUserET.getText().toString());
    }

    Boolean isEmptyString(String text){
        if(text.isEmpty())
            return true;
        return false;
    }

    public void onResponse(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    public static boolean isGeoDisabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGeoDisabled = !isGPSEnabled && !isNetworkEnabled;
        return isGeoDisabled;
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            unregisterReceiver(wifiResiver);
        }catch (Exception e){
        }
        wifiResiver.stopHandler();
    }

    public static boolean isBluetoothEnabled(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return false;
        } else if (!mBluetoothAdapter.isEnabled()) {
            return false;
        }
        return true;
    }

    public static boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            if (wifiManager.isWifiEnabled()) {
                return true;
            }
        }
        return false;
    }

    public void setLoadingButton(){
        saveButtonImageIV.setVisibility(View.VISIBLE);
        Animation rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_infinity);
        saveButtonImageIV.startAnimation(rotationAnimation);
        saveButtonTextTV.setText("Пытаюсь подключиться");
        buttonCV.setEnabled(false);
    }

    public WifiManager getWifiManager(){
        return wifiManager;
    }

    public String getSSIDDeviceET(){
        return loginUserET.getText().toString();
    }

    public String getPasswordDeviceET(){
        return passwordUserET.getText().toString();
    }

    public void setClickableButton(){
        saveButtonTextTV.setText("Начать настройку");
        buttonCV.setEnabled(true);
        saveButtonImageIV.clearAnimation();
        saveButtonImageIV.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            registerReceiver(wifiResiver, intentFilter);
        }catch (Exception e){
        }

        wifiResiver.setCountAttemptsToConnect(0);
        setClickableButton();
    }

    private void rectangleAnimate(){
        final int bbnewLeftMargin = 300;
        Animation ab = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rectangleCV.getLayoutParams();
                params.leftMargin = (int)(bbnewLeftMargin * interpolatedTime - 600);
                rectangleCV.setLayoutParams(params);
            }


        };
        ab.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
                animation1.setDuration(400);
                animation1.setFillAfter(true);
                contentLL.startAnimation(animation1);
                contentLL.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ab.setDuration(600); // in ms
        rectangleCV.startAnimation(ab);
    }

    private void circleAnimate(){
        final int anewLeftMargin = 200;
        Animation aa = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) circleCV.getLayoutParams();
                params.rightMargin = (int)(anewLeftMargin * interpolatedTime + -140);
                circleCV.setLayoutParams(params);
            }


        };
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        aa.setDuration(300); // in ms
        circleCV.startAnimation(aa);
    }

    private void blockAnimate(){
        final int newLeftMargin = 300;
        final int newLeftMargin2 = 1600;
        Animation a = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
                params.leftMargin = (int)(newLeftMargin * interpolatedTime);
                params.width  = (int)(newLeftMargin2 * interpolatedTime) + 100;
                params.height = (int)(newLeftMargin2 * interpolatedTime) + 100;
                imageView.setLayoutParams(params);
            }


        };
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rectangleAnimate();
                circleAnimate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        a.setDuration(600); // in ms
        imageView.startAnimation(a);
    }
}
