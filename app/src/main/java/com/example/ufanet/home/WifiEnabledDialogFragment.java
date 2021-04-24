package com.example.ufanet.home;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ufanet.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class WifiEnabledDialogFragment extends BottomSheetDialogFragment {

    View view;
    CardView requestButtonCV;
    TextView infoTV;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wifi_request_to_connect, container,
                false);

        initUI();
        setListeners();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AuthBottomDialogFragment.isWifiEnabled(getContext())) {
            AuthBottomDialogFragment fragment =
                    new AuthBottomDialogFragment();
            fragment.show(getFragmentManager(),
                    "auth_bottom_dialog_fragment");
            dismiss();
        }
    }

    void initUI(){
        requestButtonCV = view.findViewById(R.id.cv_request_button);
        infoTV = view.findViewById(R.id.tv_info);
    }

    void setListeners(){
        requestButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AuthBottomDialogFragment.isWifiEnabled(getContext())) {
                    AuthBottomDialogFragment fragment =
                            new AuthBottomDialogFragment();
                    fragment.show(getFragmentManager(),
                            "auth_bottom_dialog_fragment");
                    dismiss();
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                        startActivityForResult(panelIntent, 0);
                    } else {
                        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        if(wifiManager != null){
                            wifiManager.setWifiEnabled(true);
                            AuthBottomDialogFragment fragment =
                                    new AuthBottomDialogFragment();
                            fragment.show(getFragmentManager(),
                                    "auth_bottom_dialog_fragment");
                            dismiss();
                        }
                    }
                }
            }
        });
    }
}