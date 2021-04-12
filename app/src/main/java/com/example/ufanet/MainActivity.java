package com.example.ufanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.fragment.app.Fragment;

import com.example.ufanet.home.homeFragments;
import com.example.ufanet.profile.profileFragments;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class MainActivity extends AppCompatActivity{

    homeFragments homeFragments = new homeFragments(this);
    profileFragments profileFragments = new profileFragments(this);
    //private ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(homeFragments);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
