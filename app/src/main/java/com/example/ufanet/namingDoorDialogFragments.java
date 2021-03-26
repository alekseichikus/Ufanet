package com.example.ufanet;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;


public class namingDoorDialogFragments extends BottomSheetDialogFragment {

    Context mContext;
    String nameItem = "";
    SharedPreferences mSettings;
    AppCompatActivity appCompatActivity;
    BluetoothDevice device;

    public namingDoorDialogFragments(AppCompatActivity appCompatActivity, String nameItem, BluetoothDevice device){
        this.nameItem = nameItem;
        this.appCompatActivity = appCompatActivity;
        this.device = device;

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
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

        View view = inflater.inflate(R.layout.naming_door, container,
                false);
        TextView vvv = view.findViewById(R.id.name_item);
        vvv.setText(nameItem);


        CardView add_button = view.findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mSettings.edit();
                EditText ee = view.findViewById(R.id.name_device);
                editor.putString("name_device", String.valueOf(ee.getText()));
                if(device.getAddress() != null){
                    editor.putString("uuid_device", device.getAddress());
                }
                if(device.getName() != null){
                    editor.putString("real_name_device", device.getName());
                }

                editor.commit();
                appCompatActivity.finish();
                dismiss();
            }
        });
        return view;

    }
}