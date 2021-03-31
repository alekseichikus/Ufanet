package com.example.ufanet.home;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ufanet.Json.JsonPlaceHolderApi2;
import com.example.ufanet.R;
import com.example.ufanet.utils.MemoryOperation;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class homeFragments extends BottomSheetDialogFragment {

    View view;

    Handler handler;
    Runnable myRunnable;
    LinearLayout addButtonContainerLayout;
    CardView circleLinearLayout;
    CardView shareCardView;
    ImageView circleImageView;
    CardView openButton;
    CardView settingButton;

    Vibrator vibrator;
    MemoryOperation memoryOperation;

    private BluetoothLeAdvertiser mAdvertiser;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private int BEACON_BLUETOOTH_DELAY = 1000;

    private AdvertiseData.Builder dataBuilder;
    private AdvertiseSettings.Builder settingsBuilder;

    byte[] payload = {(byte)0x55,
            (byte)0x10, (byte)0x20, (byte)0x20, (byte)0x10, (byte)0x40, (byte)0x30, (byte)0x50, (byte)0x90, (byte)0x43, (byte)0x01};

    public homeFragments(AppCompatActivity appCompatActivity){
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container,
                false);

        memoryOperation = new MemoryOperation(getContext());
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        initUI();
        setListeners();
        initRunnable();


        return view;
    }

    private void initUI(){
        addButtonContainerLayout = view.findViewById(R.id.add_button_container);
        circleLinearLayout = view.findViewById(R.id.circle_container);
        circleImageView = view.findViewById(R.id.circle_imageview);
        shareCardView = view.findViewById(R.id.share_button);
        openButton = view.findViewById(R.id.add_button);
        settingButton = view.findViewById(R.id.cv_setting_button);

        dataBuilder = new AdvertiseData.Builder();
        dataBuilder.addManufacturerData(0xFFFF, payload);
        dataBuilder.setIncludeDeviceName(true);
        settingsBuilder = new AdvertiseSettings.Builder();

        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        settingsBuilder.setConnectable(false);

        mBluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mBluetoothAdapter.setName("UKEY");
        mAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
    }

    private void initRunnable(){
        handler =  new Handler();
        myRunnable = new Runnable() {
            public void run() {
                mAdvertiser.stopAdvertising(advertiseCallback);

                addButtonContainerLayout.setBackgroundResource(R.drawable.transition_gradient_2);
                AnimationDrawable animationDrawable = (AnimationDrawable) addButtonContainerLayout.getBackground();
                animationDrawable.setEnterFadeDuration(300);
                animationDrawable.setExitFadeDuration(300);
                animationDrawable.start();
                openButton.setEnabled(true);
                circleImageView.setImageResource(R.drawable.ic_lock_alt_r);
            }
        };
    }

    private void setListeners(){
        shareCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareText();
            }
        });

        openButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                mAdvertiser.startAdvertising(settingsBuilder.build(), dataBuilder.build(), advertiseCallback);
                handler.postDelayed(myRunnable, BEACON_BLUETOOTH_DELAY);
                addButtonContainerLayout.setBackgroundResource(R.drawable.transition_gradient_1);
                AnimationDrawable animationDrawable = (AnimationDrawable) addButtonContainerLayout.getBackground();
                animationDrawable.setEnterFadeDuration(300);
                animationDrawable.setExitFadeDuration(300);
                animationDrawable.start();

                circleImageView.setImageResource(R.drawable.ic_lock_open_alt);
                openButton.setEnabled(false);
                circleLinearLayout.requestLayout();
                vibrator.vibrate(200);
                Toast.makeText(getContext(), R.string.open_door_beacon, Toast.LENGTH_LONG).show();
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AuthBottomDialogFragment addPhotoBottomDialogFragment =
                        new AuthBottomDialogFragment();
                addPhotoBottomDialogFragment.show(getFragmentManager(),
                        "day_of_week_select_fragment");
            }
        });

        shareCardView.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                shareText();
            }
        });
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



    private void shareText() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, R.string.share_key + memoryOperation.getTokenUser());
        String shareKeyTitle = String.valueOf(R.string.share_key_title);
        startActivity(Intent.createChooser(share, shareKeyTitle));
    }
}