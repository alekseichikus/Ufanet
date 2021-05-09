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
import androidx.cardview.widget.CardView;

import com.example.ufanet.MainActivity;
import com.example.ufanet.R;
import com.example.ufanet.passwordActivity.LogPasscodeActivity;
import com.example.ufanet.utils.MemoryOperation;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class homeFragments extends BottomSheetDialogFragment {

    View view;

    Handler handlerBluetooth;
    Runnable runnableBluetooth;
    LinearLayout addButtonContainerLayout;
    CardView circleLinearLayout;
    CardView shareCardView;
    ImageView circleImageView;
    CardView openButton;
    CardView settingButton;

    Vibrator vibrator;
    MemoryOperation memoryOperation;

    private BluetoothLeAdvertiser advertiser;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private int BEACON_BLUETOOTH_DELAY = 1000;

    private AdvertiseData.Builder dataBuilder;
    private AdvertiseSettings.Builder settingsBuilder;

    byte[] tokenBeacon = {(byte) 0x55, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0};

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

        byte[] tokenByteArray = hexStringToByteArray(memoryOperation.getTokenUser());

        for (int i = 1; i <= 8; i++) {
            tokenBeacon[i] = tokenByteArray[i-1];
        }

        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        initUI();
        setListeners();
        initRunnable();

        return view;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }

    private void initUI(){
        addButtonContainerLayout = view.findViewById(R.id.add_button_container);
        circleLinearLayout = view.findViewById(R.id.circle_container);
        circleImageView = view.findViewById(R.id.circle_imageview);
        shareCardView = view.findViewById(R.id.share_button);
        openButton = view.findViewById(R.id.add_button);
        settingButton = view.findViewById(R.id.cv_setting_button);

        dataBuilder = new AdvertiseData.Builder();
        Log.d("button_key", new String(tokenBeacon));
        dataBuilder.addManufacturerData(0xFFFF, tokenBeacon);
        dataBuilder.setIncludeDeviceName(true);
        settingsBuilder = new AdvertiseSettings.Builder();

        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        settingsBuilder.setConnectable(false);

        bluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);

        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothAdapter.setName("UKEY");
        advertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
    }

    private void initRunnable(){
        handlerBluetooth =  new Handler();
        runnableBluetooth = new Runnable() {
            public void run() {
                advertiser.stopAdvertising(advertiseCallback);

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
                myKeyDialogFragment fragment =
                        new myKeyDialogFragment();
                fragment.show(getFragmentManager(),
                        "auth_fragment");
                //shareText();
            }
        });

        openButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(AuthBottomDialogFragment.isBluetoothEnabled()){
                    advertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
                    advertiser.startAdvertising(settingsBuilder.build(), dataBuilder.build(), advertiseCallback);
                    handlerBluetooth.postDelayed(runnableBluetooth, BEACON_BLUETOOTH_DELAY);
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
                else{
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(intent);
                }
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myKeyDialogFragment fragment =
                        new myKeyDialogFragment();
                fragment.show(getFragmentManager(),
                        "auth_fragment");
                //shareText();
            }
        });

        shareCardView.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PermActivity.class);
                intent.putExtra("title", "Bluetooth");
                intent.putExtra("id_image", R.drawable.ic_bluetooth_b);
                intent.putExtra("id", 0);
                intent.putExtra("post_title", "Bluetooth нам нужен для поиска устройства" +
                        " и для активации функции администрирования");
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
//                GeoLocalRequestToPermissionDialogFragment addPhotoBottomDialogFragment =
//                        new GeoLocalRequestToPermissionDialogFragment();
//                addPhotoBottomDialogFragment.show(getFragmentManager(),
//                        "auth_fragment");
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
}