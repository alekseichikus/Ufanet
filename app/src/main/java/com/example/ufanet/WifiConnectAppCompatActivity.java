package com.example.ufanet;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.ufanet.utils.Constants.APP_PREFERENCES_SSID_DEVICES;

public class WifiConnectAppCompatActivity extends AppCompatActivity {

    String networkSSID = APP_PREFERENCES_SSID_DEVICES;

    Runnable wifiRunnable;
    Handler wifiHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wifiHandler = new Handler();

        wifiRunnable = new Runnable() {
            public void run() {
                if(checkWifiOnAndConnected()){
                    wifiHandler.postDelayed(wifiRunnable, 500);
                }
                else{
                    Intent data = new Intent();
                    data.putExtra("disconnectWifi", "true");
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        };

        wifiHandler.postDelayed(wifiRunnable, 500);
    }

    public boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(data.hasExtra("disconnectWifi"))
                this.finish();
        }
    }
}
