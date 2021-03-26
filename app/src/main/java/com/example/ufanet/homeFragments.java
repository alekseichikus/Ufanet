package com.example.ufanet;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class homeFragments extends BottomSheetDialogFragment {

    Context mContext;
    SharedPreferences mSettings;
    AppCompatActivity appCompatActivity;
    Handler handler;
    Runnable myRunnable;
    LinearLayout addButtonContainerLayout;
    CardView circleLinearLayout;
    ImageView circleImageView;


    private BluetoothLeAdvertiser mAdvertiser;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;


    public homeFragments(AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container,
                false);
        TextView vvv = view.findViewById(R.id.name_item);
        addButtonContainerLayout = view.findViewById(R.id.add_button_container);
        circleLinearLayout = view.findViewById(R.id.circle_container);
        circleImageView = view.findViewById(R.id.circle_imageview);

        CardView add_button = view.findViewById(R.id.add_button);

        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();

        byte[] payload = {(byte)0x55, // this makes it a iBeacon
                (byte)0x10, (byte)0x20, (byte)0x20, (byte)0x10, (byte)0x40, (byte)0x30, (byte)0x50, (byte)0x90}; // Minor

        dataBuilder.addManufacturerData(0xFFFF, payload);// 0x004c is for Apple inc.

        dataBuilder.setIncludeDeviceName(true);
        AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();

        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        settingsBuilder.setConnectable(false);

        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        mBluetoothAdapter.setName("UKEY");
        Log.i("sd", "localdevicename : "+mBluetoothAdapter.getName()+" localdeviceAddress : "+mBluetoothAdapter.getAddress());
        mAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();


        handler =  new Handler();
        myRunnable = new Runnable() {
            public void run() {
                mAdvertiser.stopAdvertising(advertiseCallback);
                addButtonContainerLayout.setBackgroundResource(R.drawable.transition_gradient_2);
                AnimationDrawable animationDrawable = (AnimationDrawable) addButtonContainerLayout.getBackground();
                animationDrawable.setEnterFadeDuration(300);
                animationDrawable.setExitFadeDuration(300);
                animationDrawable.start();
                add_button.setEnabled(true);
                circleImageView.setImageResource(R.drawable.ic_lock_alt_r);
            }
        };



        add_button.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                mAdvertiser.startAdvertising(settingsBuilder.build(), dataBuilder.build(), advertiseCallback);
                handler.postDelayed(myRunnable, 4000);
                addButtonContainerLayout.setBackgroundResource(R.drawable.transition_gradient_1);
                AnimationDrawable animationDrawable = (AnimationDrawable) addButtonContainerLayout.getBackground();
                animationDrawable.setEnterFadeDuration(300);
                animationDrawable.setExitFadeDuration(300);
                animationDrawable.start();

                circleImageView.setImageResource(R.drawable.ic_lock_open_alt);

                add_button.setEnabled(false);
                //circleLinearLayout.getLayoutParams().height = 200;
                //circleLinearLayout.getLayoutParams().width = 200;
                circleLinearLayout.requestLayout();
            }
        });
        return view;
    }

    private AdvertiseCallback advertiseCallback = new AdvertiseCallback() {

        @Override
        public void onStartFailure(int errorCode) {
            Log.e("TAG", "Advertisement start failed with code: "+errorCode);
        }

        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            Log.i("TAG", "Advertisement start succeeded.");
        }
    };
}