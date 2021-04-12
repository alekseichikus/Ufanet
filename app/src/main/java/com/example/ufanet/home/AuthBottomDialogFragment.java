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
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.example.ufanet.R;
import com.example.ufanet.edit.EditActivity;
import com.example.ufanet.settings.SettingActivity;
import com.example.ufanet.utils.MemoryOperation;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
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

    Handler bluetoothHandler;
    Runnable bluetoothRunnable;

    Handler wifiCheckConnectionHandler;
    Runnable wifiCheckConnectionRunnable;

    WifiManager wifiManager;

    private BluetoothLeAdvertiser bluetoothAdvertiser;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;

    private AdvertiseData.Builder dataBuilder;
    private AdvertiseSettings.Builder settingsBuilder;

    public static final String APP_PREFERENCES_LOGIN_USER = "login_user";
    public static final String APP_PREFERENCES_PASSWORD_USER = "password_user";

    private int BEACON_BLUETOOTH_DELAY = 6000;
    private int WIFI_CHECK_CONNECTION_DELAY = 500;
    private int attempts_wifi_connect = 0;

    int netId;

    WifiConfiguration wifiConfig;
    private WifiReceiver wifiResiver;

    String networkSSID = APP_PREFERENCES_SSID_DEVICES;
    String networkPass = APP_PREFERENCES_PASSWORD_DEVICES;

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

        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);

        wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        wifiConfig.priority = 1;
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

        wifiResiver = new WifiReceiver();

        netId = wifiManager.addNetwork(wifiConfig);



        initUI();
        setListeners();
        initRunnable();
        initBluetooth();

        bluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();


        memoryOperation = new MemoryOperation(getContext());

        return view;
    }

    void initUI(){
        bluetoothHandler = new Handler();
        wifiCheckConnectionHandler = new Handler();

        saveButtonCV = view.findViewById(R.id.cv_save_button);
        loginUserET = view.findViewById(R.id.et_login);
        passwordUserET = view.findViewById(R.id.et_password);
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
                //connectToWifi();
            }
        };

        wifiCheckConnectionRunnable = new Runnable() {
            public void run() {
                Log.d("fweswe", wifiManager.getConnectionInfo().getSSID());
                if(wifiManager.getConnectionInfo().getSSID().equals(String.format("\"%s\"",APP_PREFERENCES_SSID_DEVICES))){
                    Intent intent = new Intent(getActivity(), SettingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                else{
                    if(attempts_wifi_connect < 12){
                        attempts_wifi_connect += 1;

                        wifiCheckConnectionHandler.removeCallbacks(wifiCheckConnectionRunnable);
                        connectToWifi();
                    }
                    else{
                        wifiCheckConnectionHandler.removeCallbacks(wifiCheckConnectionRunnable);
                        Log.d("connect to " , "could not connect");
                    }
                }
            }
        };
    }

    public void connectToWifi() {


        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();

        wifiCheckConnectionHandler.postDelayed(wifiCheckConnectionRunnable, WIFI_CHECK_CONNECTION_DELAY);
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
        getActivity().registerReceiver(wifiResiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
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
                        if (bluetoothAdapter != null) {
                            if(bluetoothAdapter.isEnabled()){
                                if(wifiManager.isWifiEnabled()){
                                    Log.d("wifi is enable", "true");
                                    if(!isEmptyString(loginUserET.getText().toString())){
                                        if(!isEmptyString(passwordUserET.getText().toString())){
                                            memoryOperation.setLoginUser(loginUserET.getText().toString());
                                            memoryOperation.setPasswordUser(passwordUserET.getText().toString());

                                            startBeacon();
                                            Log.d("wefwef", "dwadad");
                                            bluetoothHandler.postDelayed(bluetoothRunnable, BEACON_BLUETOOTH_DELAY);
                                            scheduleSendLocation();
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

    public void scheduleSendLocation() {

        getActivity().registerReceiver(wifiResiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
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
        getActivity().unregisterReceiver(wifiResiver);
    }


    public class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context c, Intent intent) {

            //сканируем вайфай точки и узнаем какие доступны
            List<ScanResult> results = wifiManager.getScanResults();
            //проходимся по всем возможным точкам
            for (final ScanResult ap : results) {
                //ищем нужную нам точку с помощью ифа, будет находить то которую вы ввели
                Log.d("nkhnefwef", ap.SSID.toString());
                if(ap.SSID.toString().trim().equals("SCUD")) {
                    Log.d("kjljklkjl", ap.SSID.toString());
                    // дальше получаем ее MAC и передаем для коннекрта, MAC получаем из результата
                    //здесь мы уже начинаем коннектиться
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        WifiNetworkSpecifier.Builder builder = new WifiNetworkSpecifier.Builder();
                        builder.setSsid(APP_PREFERENCES_SSID_DEVICES);
                        builder.setWpa2Passphrase(APP_PREFERENCES_PASSWORD_DEVICES);

                        WifiNetworkSpecifier wifiNetworkSpecifier = builder.build();

                        NetworkRequest.Builder networkRequestBuilder1 = new NetworkRequest.Builder();
                        networkRequestBuilder1.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
                        networkRequestBuilder1.setNetworkSpecifier(wifiNetworkSpecifier);

                        NetworkRequest nr = networkRequestBuilder1.build();
                        ConnectivityManager cm = (ConnectivityManager)
                                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        ConnectivityManager.NetworkCallback networkCallback = new
                                ConnectivityManager.NetworkCallback() {
                                    @Override
                                    public void onAvailable(Network network) {
                                        super.onAvailable(network);
                                        Log.d(TAG, "onAvailable:" + network);
                                        cm.bindProcessToNetwork(network);
                                        Intent intent = new Intent(getActivity(), SettingActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent);
                                    }
                                };
                        cm.requestNetwork(nr, networkCallback);
                    }
                    else{
                        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
                        wifiConfig.priority = 1;
                        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

                        //получаем ID сети и пытаемся к ней подключиться,
                        int netId = wifiManager.addNetwork(wifiConfig);
                        wifiManager.saveConfiguration();

                        Log.d("ergergerger", String.valueOf(netId));
                        //если вайфай выключен то включаем его
                        wifiManager.enableNetwork(netId, true);
                        //если же он включен но подключен к другой сети то перегружаем вайфай.
                        wifiManager.reconnect();
                    }
                    break;
                }
            }
        }
    }
}