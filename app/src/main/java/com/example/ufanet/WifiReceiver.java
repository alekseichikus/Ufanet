package com.example.ufanet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.ufanet.home.AuthBottomDialogFragment;
import com.example.ufanet.settings.SettingActivity;

public class WifiReceiver extends BroadcastReceiver {

    AuthBottomDialogFragment fragment;
    Handler preWifiConnectHandler;
    Runnable preWifiConnectRunnable;

    WifiManager wifiManager;

    private Integer APP_PREFERENCES_COUNT_ATTEMPTS_TO_CONNECT = 0;
    private int NUMBER_CONNECTION_ATTEMPTS = 6;
    private int WIFI_CONNECT_DELAY = 3000;

    WifiNetworkSpecifier.Builder builder;
    WifiNetworkSpecifier wifiNetworkSpecifier;
    NetworkRequest.Builder networkRequestBuilder;
    NetworkRequest nr;
    ConnectivityManager cm;

    WifiConfiguration wifiConfig;
    int netId;

    public WifiReceiver(AuthBottomDialogFragment fragment){
        this.fragment = fragment;
        preWifiConnectHandler = new Handler();
        wifiManager = fragment.getWifiManager();

        preWifiConnectRunnable = new Runnable() {
            public void run() {
                if (fragment.getActivity() != null) {
                    scheduleSendLocation();
                }
            }
        };

        initWifi();
    }

    public void initWifi(){
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {

        }
        else{

        }
    }

    @Override
    public void onReceive(Context c, Intent intent) {
        if(intent == null || intent.getExtras() == null){
            return;
        }

        String action = intent.getAction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            switch (action){
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                    Log.d(fragment.getActivity().getClass().getSimpleName(), "wifi dont connect");
                    if(getCountAttemptsToConnect() < NUMBER_CONNECTION_ATTEMPTS){
                        preWifiConnectHandler.removeCallbacks(preWifiConnectRunnable);
                        preWifiConnectHandler.postDelayed(preWifiConnectRunnable, WIFI_CONNECT_DELAY);

                        setCountAttemptsToConnect(getCountAttemptsToConnect()+1);
                    }
                    else{
                        fragment.setClickableButton();
                        Toast.makeText(fragment.getContext(), "Не удалось подключиться", Toast.LENGTH_SHORT).show();
                        setCountAttemptsToConnect(0);
                    }
                    break;
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                    if (state == WifiManager.WIFI_STATE_ENABLED) {
                        Log.d("state", "wifi enabled");
                        setCountAttemptsToConnect(0);
                    }
                    else if (state == WifiManager.WIFI_STATE_DISABLED){
                        Log.d("state", "wifi disabled");
                        setCountAttemptsToConnect(0);
                        preWifiConnectHandler.removeCallbacks(preWifiConnectRunnable);
                        fragment.setClickableButton();
                    }
                    break;
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:

                    NetworkInfo networkInfo =
                            intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if(networkInfo.isConnected()) {
                        if(checkWifiOnAndConnected()){
                            preWifiConnectHandler.removeCallbacks(preWifiConnectRunnable);
                            Log.d(fragment.getActivity().getClass().getSimpleName(), "wifi connect 2");
                            Intent settingActivityIntent = new Intent(fragment.getActivity(), SettingActivity.class);
                            settingActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            fragment.startActivityForResult(settingActivityIntent, 0);
                            try{
                                fragment.getActivity().unregisterReceiver(this);
                            }catch (Exception e){
                            }
                            fragment.dismiss();
                        }
                    }
                    break;
            }
        }
        else{
            switch (action){
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                    if(!checkWifiOnAndConnected()){
                        Log.d(fragment.getActivity().getClass().getSimpleName(), "wifi dont connect");
                        if(getCountAttemptsToConnect() < NUMBER_CONNECTION_ATTEMPTS){
                            preWifiConnectHandler.removeCallbacks(preWifiConnectRunnable);
                            preWifiConnectHandler.postDelayed(preWifiConnectRunnable, WIFI_CONNECT_DELAY);

                            setCountAttemptsToConnect(getCountAttemptsToConnect()+1);
                        }
                        else{
                            fragment.setClickableButton();
                            Toast.makeText(fragment.getContext(), "Не удалось подключиться", Toast.LENGTH_SHORT).show();
                            setCountAttemptsToConnect(0);
                        }
                    }
                    else{
                        Log.d(fragment.getActivity().getClass().getSimpleName(), "wifi connect");
                        Intent settingActivityIntent = new Intent(fragment.getActivity(), SettingActivity.class);
                        settingActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        fragment.startActivityForResult(settingActivityIntent, 0);
                        try{
                            fragment.getActivity().unregisterReceiver(this);
                        }catch (Exception e){
                        }
                        fragment.dismiss();
                    }
                    break;
            }
        }
    }

    public boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = fragment.getWifiManager();

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if( wifiInfo.getNetworkId() == -1 ){
                return false; // Not connected to an access point
            }

            if(!wifiInfo.getSSID().equals(String.format("\"%s\"", "SCUD"))){
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

    public void startHandler(){
        preWifiConnectHandler.postDelayed(preWifiConnectRunnable, WIFI_CONNECT_DELAY);
    }
    public void stopHandler(){
        preWifiConnectHandler.removeCallbacks(preWifiConnectRunnable);
    }

    public void scheduleSendLocation() {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q){
            builder = new WifiNetworkSpecifier.Builder();
            builder.setSsid(fragment.getMemoryOperation().getLoginUser());
            builder.setWpa2Passphrase(fragment.getMemoryOperation().getPasswordUser());
            wifiNetworkSpecifier = builder.build();
            networkRequestBuilder = new NetworkRequest.Builder();
            networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            networkRequestBuilder.setNetworkSpecifier(wifiNetworkSpecifier);

            nr = networkRequestBuilder.build();
            cm = (ConnectivityManager)
                    fragment.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            ConnectivityManager.NetworkCallback networkCallback = new
                    ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(Network network) {
                            super.onAvailable(network);
                            cm.bindProcessToNetwork(network);
                        }
                    };
            cm.requestNetwork(nr, networkCallback);
        }
        else{
            wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = String.format("\"%s\"", fragment.getMemoryOperation().getLoginUser());
            wifiConfig.preSharedKey = String.format("\"%s\"", fragment.getMemoryOperation().getPasswordUser());
            wifiConfig.priority = 99999;
            netId = wifiManager.addNetwork(wifiConfig);
            wifiManager.saveConfiguration();
            wifiManager.enableNetwork(netId, true);
            wifiManager.startScan();
            wifiManager.disconnect();
            wifiManager.reconnect();
        }
    }
}
