package com.example.ufanet.home;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
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

import static android.app.Activity.RESULT_OK;
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

        initUI();
        setListeners();
        initRunnable();
        initBluetooth();

        bluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        wifiManager.disconnect();

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
        bluetoothAdvertiser.startAdvertising(settingsBuilder.build(), dataBuilder.build(), advertiseCallback);
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

                if(wifiManager.isWifiEnabled()){
                }
                else{
                    wifiManager.setWifiEnabled(true);
                }
                stopBeacon();
                connectToWifi();
            }
        };

        wifiCheckConnectionRunnable = new Runnable() {
            public void run() {
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
        String networkSSID = APP_PREFERENCES_SSID_DEVICES;
        String networkPass = APP_PREFERENCES_PASSWORD_DEVICES;

        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        wifiConfig.priority = 1;
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

        int netId = wifiManager.addNetwork(wifiConfig);

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
        bluetoothAdvertiser.stopAdvertising(advertiseCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        loginUserET.setText(memoryOperation.getLoginUser());
        passwordUserET.setText(memoryOperation.getPasswordUser());
    }

    void setListeners(){
        saveButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                else{
                    if(!isEmptyString(loginUserET.getText().toString())){
                        if(!isEmptyString(passwordUserET.getText().toString())){
                            memoryOperation.setLoginUser(loginUserET.getText().toString());
                            memoryOperation.setPasswordUser(passwordUserET.getText().toString());
                            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(intent, 1);
                        }
                        else{
                            Toast.makeText(getContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "Введите логин", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    Boolean isEmptyString(String text){
        if(text.isEmpty())
            return true;
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK && bluetoothAdapter.isEnabled()){
                startBeacon();
                bluetoothHandler.postDelayed(bluetoothRunnable, BEACON_BLUETOOTH_DELAY);
            }
        }
    }
}