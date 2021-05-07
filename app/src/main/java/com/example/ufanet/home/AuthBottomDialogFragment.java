package com.example.ufanet.home;

import android.bluetooth.BluetoothAdapter;
import android.content.IntentFilter;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import com.example.ufanet.BeaconBluetooth;
import com.example.ufanet.R;
import com.example.ufanet.WifiReceiver;
import com.example.ufanet.utils.MemoryOperation;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import static android.content.Context.WIFI_SERVICE;

public class AuthBottomDialogFragment extends BottomSheetDialogFragment {

    MemoryOperation memoryOperation;
    CardView saveButtonCV;
    EditText loginUserET;
    EditText passwordUserET;
    View view;
    ImageView saveButtonImageIV;
    TextView saveButtonTextTV;

    WifiManager wifiManager;

    public static final String APP_PREFERENCES_LOGIN_USER = "login_user";
    public static final String APP_PREFERENCES_PASSWORD_USER = "password_user";

    public WifiReceiver wifiResiver;
    private BeaconBluetooth beaconBluetooth;

    IntentFilter intentFilter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_auth, container,
                false);

        memoryOperation = new MemoryOperation(getContext());

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        initUI();
        setListeners();

        initWifi();

        wifiResiver = new WifiReceiver(this);
        beaconBluetooth = new BeaconBluetooth(this);

        setSSIDDeviceET();
        setPasswordDeviceET();
        return view;
    }

    void initUI(){
        saveButtonCV = view.findViewById(R.id.cv_save_button);
        loginUserET = view.findViewById(R.id.et_login);
        passwordUserET = view.findViewById(R.id.et_password);
        saveButtonImageIV = view.findViewById(R.id.iv_loading);
        saveButtonTextTV = view.findViewById(R.id.tv_loading);
    }

    private void initWifi(){
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            getActivity().registerReceiver(wifiResiver, intentFilter);
        }catch (Exception e){
        }

        wifiResiver.setCountAttemptsToConnect(0);
        setClickableButton();
    }

    void setListeners(){
        saveButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGeoDisabled(getContext())) {
                    beaconBluetooth.initBluetooth();
                        if(isBluetoothEnabled()){
                            if(isWifiEnabled(getContext())){
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

    public void setSSIDDeviceET(){
        loginUserET.setText(memoryOperation.getLoginUser());
    }

    public String getSSIDDeviceET(){
        return loginUserET.getText().toString();
    }

    public String getPasswordDeviceET(){
        return passwordUserET.getText().toString();
    }

    public void setPasswordDeviceET(){
        passwordUserET.setText(memoryOperation.getPasswordUser());
    }

    public void setLoadingButton(){
        saveButtonImageIV.setVisibility(View.VISIBLE);
        Animation rotationAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_infinity);
        saveButtonImageIV.startAnimation(rotationAnimation);
        saveButtonTextTV.setText("Пытаюсь подключиться");
        saveButtonCV.setEnabled(false);
    }

    public void setClickableButton(){
        saveButtonTextTV.setText("Начать настройку");
        saveButtonCV.setEnabled(true);
        saveButtonImageIV.clearAnimation();
        saveButtonImageIV.setVisibility(View.GONE);
    }

    public static boolean isGeoDisabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGeoDisabled = !isGPSEnabled && !isNetworkEnabled;
        return isGeoDisabled;
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

    Boolean isEmptyString(String text){
        if(text.isEmpty())
            return true;
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onResponse(String string) {
        Toast.makeText(getContext(), string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            getActivity().unregisterReceiver(wifiResiver);
        }catch (Exception e){
        }
        wifiResiver.stopHandler();
    }

    public WifiManager getWifiManager(){
        return wifiManager;
    }

    public MemoryOperation getMemoryOperation(){
        return memoryOperation;
    }
}