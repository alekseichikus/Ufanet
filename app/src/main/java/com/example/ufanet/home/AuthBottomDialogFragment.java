package com.example.ufanet.home;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.IntentFilter;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.example.ufanet.R;
import com.example.ufanet.settings.SettingActivity;
import com.example.ufanet.utils.Constants;
import com.example.ufanet.utils.MemoryOperation;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.WIFI_SERVICE;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_PASSWORD_DEVICES;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_SSID_DEVICES;

public class AuthBottomDialogFragment extends BottomSheetDialogFragment {

    MemoryOperation memoryOperation;
    CardView saveButtonCV;
    EditText loginUserET;
    EditText passwordUserET;
    View view;
    ImageView saveButtonImageIV;
    TextView saveButtonTextTV;

    Handler bluetoothHandler;
    Runnable bluetoothRunnable;

    Handler preWifiConnectHandler;
    Runnable preWifiConnectRunnable;

    WifiManager wifiManager;
    NetworkRequest networkRequest;

    private BluetoothLeAdvertiser bluetoothAdvertiser;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;

    private AdvertiseData.Builder dataBuilder;
    private AdvertiseSettings.Builder settingsBuilder;

    public static final String APP_PREFERENCES_LOGIN_USER = "login_user";
    public static final String APP_PREFERENCES_PASSWORD_USER = "password_user";
    public static Integer APP_PREFERENCES_COUNT_ATTEMPTS_TO_CONNECT = 0;

    private int BEACON_BLUETOOTH_DELAY = 4500;
    private int WIFI_CONNECT_DELAY = 3000;
    private int NUMBER_CONNECTION_ATTEMPTS = 6;
    private Boolean isPressedButton = false;


    int netId;

    WifiConfiguration wifiConfig;
    private WifiReceiver wifiResiver;

    String networkSSID = APP_PREFERENCES_SSID_DEVICES;
    String networkPass = APP_PREFERENCES_PASSWORD_DEVICES;

    IntentFilter intentFilter;

    byte[] payload = {(byte) 0x55,
            (byte) 0x10, (byte) 0x20, (byte) 0x20, (byte) 0x10, (byte) 0x40, (byte) 0x30, (byte) 0x50, (byte) 0x90, (byte) 0x43, (byte) 0x02};

    public AuthBottomDialogFragment(){

    }

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

        initUI();
        setListeners();
        initRunnable();

        wifiResiver = new WifiReceiver();

        bluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        memoryOperation = new MemoryOperation(getContext());

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        return view;
    }

    void initUI(){
        bluetoothHandler = new Handler();
        preWifiConnectHandler = new Handler();

        saveButtonCV = view.findViewById(R.id.cv_save_button);
        loginUserET = view.findViewById(R.id.et_login);
        passwordUserET = view.findViewById(R.id.et_password);
        saveButtonImageIV = view.findViewById(R.id.iv_loading);
        saveButtonTextTV = view.findViewById(R.id.tv_loading);
    }

    private void initWifi(){
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);

        wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        wifiConfig.priority = 1;
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

        //getActivity().registerReceiver(wifiResiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.saveConfiguration();

        wifiManager.enableNetwork(netId, true);
    }

    void startBeacon(){
        if(bluetoothAdvertiser != null){
            bluetoothAdvertiser.startAdvertising(settingsBuilder.build(), dataBuilder.build(), advertiseCallback);
        }
    }

    private AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartFailure(int errorCode) {
            Log.e("TAG", "Advertisement start failed with code: " + errorCode);
        }

        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            Log.i("TAG", "Advertisement start succeeded.");
        }
    };

    private void initRunnable() {
        bluetoothRunnable = new Runnable() {
            public void run() {
                stopBeacon();
                preWifiConnectHandler.postDelayed(preWifiConnectRunnable, WIFI_CONNECT_DELAY);
            }
        };

        preWifiConnectRunnable = new Runnable() {
            public void run() {
                if(getActivity() != null){
                    scheduleSendLocation();
                }
            }
        };
    }

    void initBluetooth(){
        dataBuilder = new AdvertiseData.Builder();
        dataBuilder.addManufacturerData(0xFFFF, payload);
        dataBuilder.setIncludeDeviceName(true);
        settingsBuilder = new AdvertiseSettings.Builder();

        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        settingsBuilder.setConnectable(false);

        bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);

        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothAdapter.setName("UKEY");
        bluetoothAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
    }

    void stopBeacon(){
        if(bluetoothAdvertiser != null){
            bluetoothAdvertiser.stopAdvertising(advertiseCallback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loginUserET.setText(memoryOperation.getLoginUser());
        passwordUserET.setText(memoryOperation.getPasswordUser());

        try{
            getActivity().registerReceiver(wifiResiver, intentFilter);
        }catch (Exception e){
        }
    }

    void setListeners(){
        saveButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                else{
                    if(isGeoDisabled()){
                        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                    else{
                        initBluetooth();
                        if (bluetoothAdapter != null) {
                            if(bluetoothAdapter.isEnabled()){
                                initWifi();
                                if(wifiManager.isWifiEnabled()){
                                    Log.d("wifi is enable", "true");
                                    if(!isEmptyString(loginUserET.getText().toString())){
                                        if(!isEmptyString(passwordUserET.getText().toString())){
                                            memoryOperation.setLoginUser(loginUserET.getText().toString());
                                            memoryOperation.setPasswordUser(passwordUserET.getText().toString());

                                            startBeacon();
                                            setLoadingButton();
                                            isPressedButton = true;
                                            bluetoothHandler.postDelayed(bluetoothRunnable, BEACON_BLUETOOTH_DELAY);


                                        }
                                        else{
                                            Toast.makeText(getContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else{
                                        Toast.makeText(getContext(), "Введите логин", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                                        startActivityForResult(panelIntent, 0);
                                    } else {
                                        wifiManager.setWifiEnabled(true);
                                    }
                                }

                            }
                            else{
                                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(intent, 1);
                            }

                        }
                        else{
                            AuthBottomDialogFragment.this.onResponse("Ваше устройство не поддерживает Bluetooth");
                        }
                    }
                }
            }
        });
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

    public void scheduleSendLocation() {
        wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);
        wifiConfig.priority = 99999;
        netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.saveConfiguration();

        wifiManager.enableNetwork(netId, true);

        wifiManager.startScan();
        wifiManager.disconnect();
        wifiManager.reconnect();

    }

    public boolean isGeoDisabled() {
        LocationManager mLocationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean mIsGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean mIsNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean mIsGeoDisabled = !mIsGPSEnabled && !mIsNetworkEnabled;
        return mIsGeoDisabled;
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
    }

    public class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context c, Intent intent) {
            if(intent == null || intent.getExtras() == null){
                return;
            }

            ConnectivityManager cm = (ConnectivityManager)
                    getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            String action = intent.getAction();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                switch (action){
                    case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                        if(!checkWifiOnAndConnected()){
                            Log.d(getActivity().getClass().getSimpleName(), "wifi dont connect");
                            if(getCountAttemptsToConnect() < NUMBER_CONNECTION_ATTEMPTS){
                                preWifiConnectHandler.removeCallbacks(preWifiConnectRunnable);
                                preWifiConnectHandler.postDelayed(preWifiConnectRunnable, WIFI_CONNECT_DELAY);

                                setCountAttemptsToConnect(getCountAttemptsToConnect()+1);
                            }
                            else{
                                setClickableButton();
                                Toast.makeText(getContext(), "Не удалось подключиться", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Log.d(getActivity().getClass().getSimpleName(), "wifi will be connect");
                        }
                        break;
                    case WifiManager.WIFI_STATE_CHANGED_ACTION:
                        int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                        if (state == WifiManager.WIFI_STATE_ENABLED) {
                            Log.d("state", "wifi enabled");
                        }
                        else if (state == WifiManager.WIFI_STATE_DISABLED){
                            Log.d("state", "wifi disabled");
                            preWifiConnectHandler.removeCallbacks(preWifiConnectRunnable);
                            setClickableButton();
                        }
                        break;
                    case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                        NetworkInfo networkInfo =
                                intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                        if(networkInfo.isConnected()) {
                            if(checkWifiOnAndConnected()){
                                Log.d(getActivity().getClass().getSimpleName(), "wifi will be connect 2");
                                Intent settingActivityIntent = new Intent(getActivity(), SettingActivity.class);
                                settingActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(settingActivityIntent);
                                dismiss();
                            }
                        }
                        break;
                }
            }
            else{
                switch (action){
                    case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                        if(!checkWifiOnAndConnected()){
                            Log.d(getActivity().getClass().getSimpleName(), "wifi dont connect");
                            if(getCountAttemptsToConnect() < NUMBER_CONNECTION_ATTEMPTS){
                                preWifiConnectHandler.removeCallbacks(preWifiConnectRunnable);
                                preWifiConnectHandler.postDelayed(preWifiConnectRunnable, WIFI_CONNECT_DELAY);

                                setCountAttemptsToConnect(getCountAttemptsToConnect()+1);
                            }
                            else{
                                setClickableButton();
                                Toast.makeText(getContext(), "Не удалось подключиться", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Log.d(getActivity().getClass().getSimpleName(), "wifi will be connect");
                            Intent settingActivityIntent = new Intent(getActivity(), SettingActivity.class);
                            settingActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(settingActivityIntent);
                            dismiss();
                        }
                        break;
                }
            }




//            if(wifiManager != null){
//                List<ScanResult> results = wifiManager.getScanResults();
//                Boolean is_isset_wifi = false;
//                for (final ScanResult scanResult : results) {
//                    if(scanResult.SSID.toString().trim().equals(APP_PREFERENCES_SSID_DEVICES)) {
//                        is_isset_wifi = true;
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//                            ConnectivityManager.NetworkCallback networkCallback = new
//                                    ConnectivityManager.NetworkCallback() {
//                                        @Override
//                                        public void onAvailable(Network network) {
//                                            super.onAvailable(network);
//                                            cm.bindProcessToNetwork(network);
//                                            Intent intent1 = new Intent(getActivity(), SettingActivity.class);
//                                            intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                            startActivity(intent1);
//                                            dismiss();
//                                        }
//                                    };
//                            initNetworkRequest();
//                            cm.requestNetwork(networkRequest, networkCallback);
//                        }
//                        else{
//                            if(wifiManager.getConnectionInfo().getSSID().equals("\"" + APP_PREFERENCES_SSID_DEVICES + "\"")){
//                                Intent intent1 = new Intent(getActivity(), SettingActivity.class);
//                                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                startActivity(intent1);
//                                dismiss();
//                            }
//                        }
//                        break;
//                    }
//                }
//                if(!is_isset_wifi){
//                    Log.d("BroadcastReceiver", "true4");
//                    if(getCountAttemptsToConnect() < 3){
//                        Log.d("BroadcastReceiver", "true5");
//                        wifiManager.reconnect();
//                        preWifiConnectHandler.postDelayed(preWifiConnectRunnable, 4000);
//                        setCountAttemptsToConnect(getCountAttemptsToConnect()+1);
//                    }
//                    else{
//                        Log.d("BroadcastReceiver", "true6");
//                        onResponse("Ошибка подключения к сети");
//                        setClickableButton();
//                        setCountAttemptsToConnect(0);
//                    }
//                }
//            }
        }
    }

    private boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if( wifiInfo.getNetworkId() == -1 ){
                return false; // Not connected to an access point
            }
            else if(!wifiInfo.getSSID().equals(String.format("\"%s\"", networkSSID))){
                return false;
            }
            return true; // Connected to an access point
        }
        else {
            return false; // Wi-Fi adapter is OFF
        }
    }

    private Integer getCountAttemptsToConnect(){
        return  APP_PREFERENCES_COUNT_ATTEMPTS_TO_CONNECT;
    }

    private void setCountAttemptsToConnect(Integer count){
        APP_PREFERENCES_COUNT_ATTEMPTS_TO_CONNECT = count;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void initNetworkRequest(){
        WifiNetworkSpecifier.Builder builder = new WifiNetworkSpecifier.Builder();
        builder.setSsid(APP_PREFERENCES_SSID_DEVICES);
        builder.setWpa2Passphrase(APP_PREFERENCES_PASSWORD_DEVICES);

        WifiNetworkSpecifier wifiNetworkSpecifier = builder.build();

        NetworkRequest.Builder networkRequestBuilder = new NetworkRequest.Builder();
        networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
        networkRequestBuilder.setNetworkSpecifier(wifiNetworkSpecifier);

        networkRequest = networkRequestBuilder.build();
    }
}