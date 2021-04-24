package com.example.ufanet;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.ufanet.home.AuthBottomDialogFragment;

public class BeaconBluetooth {

    private BluetoothLeAdvertiser bluetoothAdvertiser;

    public BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;

    private AdvertiseData.Builder dataBuilder;
    private AdvertiseSettings.Builder settingsBuilder;

    public Handler bluetoothHandler;
    public Runnable bluetoothRunnable;

    private int BEACON_BLUETOOTH_DELAY = 4500;

    byte[] payload = {(byte) 0x55,
            (byte) 0x10, (byte) 0x20, (byte) 0x20, (byte) 0x10, (byte) 0x40, (byte) 0x30, (byte) 0x50, (byte) 0x90, (byte) 0x43, (byte) 0x02};

    AuthBottomDialogFragment fragment;

    public BeaconBluetooth(AuthBottomDialogFragment fragment){
        bluetoothHandler = new Handler();
        this.fragment = fragment;
        bluetoothManager = (BluetoothManager) fragment.getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        bluetoothRunnable = new Runnable() {
            public void run() {
                stopBeacon();
                fragment.wifiResiver.startHandler();
            }
        };
    }
    public void initBluetooth(){
        dataBuilder = new AdvertiseData.Builder();
        dataBuilder.addManufacturerData(0xFFFF, payload);
        dataBuilder.setIncludeDeviceName(true);
        settingsBuilder = new AdvertiseSettings.Builder();

        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        settingsBuilder.setConnectable(false);

        bluetoothManager = (BluetoothManager) fragment.getContext().getSystemService(Context.BLUETOOTH_SERVICE);

        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothAdapter.setName("UKEY");
        bluetoothAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
    }

    public void startBeacon(){
        if(bluetoothAdvertiser != null){
            bluetoothAdvertiser.startAdvertising(settingsBuilder.build(), dataBuilder.build(), advertiseCallback);
            bluetoothHandler.postDelayed(bluetoothRunnable, BEACON_BLUETOOTH_DELAY);
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

    void stopBeacon(){
        if(bluetoothAdvertiser != null){
            bluetoothAdvertiser.stopAdvertising(advertiseCallback);
        }
    }
}
