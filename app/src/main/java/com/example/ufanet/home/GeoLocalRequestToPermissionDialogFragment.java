package com.example.ufanet.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

public class GeoLocalRequestToPermissionDialogFragment extends BottomSheetDialogFragment {

    View view;
    CardView requestButtonCV;
    CardView openSettingsButtonCV;
    TextView infoTV;
    public final Integer REQUEST_PERMISSION_CODE = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_geolocal_request_to_connect, container,
                false);

        initUI();
        setListeners();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(AuthBottomDialogFragment.isGeoDisabled(getContext())){
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
            else{
                BluetoothRequestToPermissionDialogFragment fragment =
                        new BluetoothRequestToPermissionDialogFragment();
                fragment.show(getFragmentManager(),
                        "bluetooth_request_to_permission_dialog_fragment");
                dismiss();
            }
        }
    }

    void initUI(){
        requestButtonCV = view.findViewById(R.id.cv_request_button);
        openSettingsButtonCV = view.findViewById(R.id.cv_open_settings_button);
        infoTV = view.findViewById(R.id.tv_info);
    }

    void setListeners(){
        requestButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAndRequestPermissions()) {
                }
            }
        });

        openSettingsButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsApp();
            }
        });
    }

    private  boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_PERMISSION_CODE);
            return false;
        }
        return true;
    }

    void setSettingsLayout(){
        openSettingsButtonCV.setVisibility(View.VISIBLE);
        requestButtonCV.setVisibility(View.GONE);
        infoTV.setText("Вы запретили доступ к местоположению. Необходим доступ к местоположению. Перейдите в настройки и дайте нам это право");
    }

    void setDefaultLayout(){
        openSettingsButtonCV.setVisibility(View.GONE);
        requestButtonCV.setVisibility(View.VISIBLE);
        infoTV.setText("Предоставить доступ к геолокации");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION_CODE) {
            Map<String, Integer> perms = new HashMap<>();
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "sms & location services permission granted");
                } else {
                    Log.d(TAG, "Some permissions are not granted ask again ");
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                    }
                    else {
                        setSettingsLayout();
                    }
                }
            }
        }
    }

    private void openSettingsApp() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }
}