package com.example.ufanet.edit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ufanet.Json.JsonPlaceHolderApi2;
import com.example.ufanet.R;
import com.example.ufanet.templates.Config;
import com.example.ufanet.utils.MemoryOperation;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EditActivity extends AppCompatActivity {
    private static final SecureRandom secureRandom = new SecureRandom();

    MemoryOperation memoryOperation;
    EditText loginUserET;
    EditText passwordUserET;
    CardView saveButtonCV;

    Handler handler;
    Runnable myRunnable;

    private BluetoothLeAdvertiser mAdvertiser;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private int BEACON_BLUETOOTH_DELAY = 1000;

    private AdvertiseData.Builder dataBuilder;
    private AdvertiseSettings.Builder settingsBuilder;

    byte[] payload = {(byte)0x55,
            (byte)0x10, (byte)0x20, (byte)0x20, (byte)0x10, (byte)0x40, (byte)0x30, (byte)0x50, (byte)0x90, (byte)0x43, (byte)0x02};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initUI();
        initRunnable();
        setListeners();
        memoryOperation = new MemoryOperation(this);
        loginUserET.setText(memoryOperation.getLoginUser());
        passwordUserET.setText(memoryOperation.getPasswordUser());


        Toast.makeText(EditActivity.this, "Включил маяк", Toast.LENGTH_LONG).show();
        mAdvertiser.startAdvertising(settingsBuilder.build(), dataBuilder.build(), advertiseCallback);
        handler.postDelayed(myRunnable, BEACON_BLUETOOTH_DELAY);
    }

    void setListeners(){
        saveButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHttpResponse();
            }
        });
    }

    void initUI(){
        loginUserET = findViewById(R.id.et_login);
        passwordUserET = findViewById(R.id.et_password);
        saveButtonCV = findViewById(R.id.cv_save_button);

        dataBuilder = new AdvertiseData.Builder();
        dataBuilder.addManufacturerData(0xFFFF, payload);
        dataBuilder.setIncludeDeviceName(true);
        settingsBuilder = new AdvertiseSettings.Builder();

        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        settingsBuilder.setConnectable(false);

        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mBluetoothAdapter.setName("UKEY");
        mAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();


    }

    private void initRunnable(){
        handler =  new Handler();
        myRunnable = new Runnable() {
            public void run() {
                mAdvertiser.stopAdvertising(advertiseCallback);

                Toast.makeText(EditActivity.this, "Отключил маяк", Toast.LENGTH_LONG).show();
            }
        };
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

    public Object getHttpResponse() {
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl("http://192.168.4.1")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi2 jsonPlaceHolderApi1 = retrofit1.create(JsonPlaceHolderApi2.class);
        Config config = new Config("SCUD", "1234567890", 1,0,
                0,0,1, 1, 0, 3000,
                1, 1, 1,1,"15236547893");
        Call<Config> call1 = jsonPlaceHolderApi1.getMyJSON(config);
        Log.d("request", call1.request().headers().toString());
        Log.d("request body", call1.request().body().toString());
        //call1.request().newBuilder().addHeader("Content-Length", String.valueOf(100));
        //call1.request().newBuilder().addHeader("Content-Type", "application/json");
        call1.enqueue(new Callback<Config>() {
            @Override
            public void onResponse(Call<Config> call, Response<Config> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                Log.d("sukk", String.valueOf(response.code()));
                loginUserET.setText(response.code());
            }
            @Override
            public void onFailure(Call<Config> call, Throwable t) {
                Log.d("sukk", "bfd");
                //Log.d("sukk", t.);
                Log.d("sukkq", call1.request().toString());
                Log.d("dsfsdf", t.toString());
            }
        });
        Log.d("sukk", "aaa");
        return null;
    }
}
