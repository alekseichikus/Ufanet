package com.example.ufanet.edit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.ufanet.R;
import com.example.ufanet.edit.presenter.EditPresenter;
import com.example.ufanet.edit.presenter.IEditPresenter;
import com.example.ufanet.utils.MemoryOperation;

import static com.example.ufanet.utils.Constants.APP_PREFERENCES_PASSWORD_DEVICES;
import static com.example.ufanet.utils.Constants.APP_PREFERENCES_SSID_DEVICES;

public class EditActivity extends AppCompatActivity implements IEditView {

    MemoryOperation memoryOperation;
    EditText loginUserET;
    EditText passwordUserET;
    CardView saveButtonCV;
    CardView closeButtonCV;

    Switch bluetoothSW;
    Switch wiegandSW;
    Switch dallasSW;
    Switch gerkonSW;
    Switch buttonSW;
    Switch lockSW;
    Switch lockInvertSW;
    EditText lockTimeET;
    Switch buzzerCaseSW;
    Switch buzzerGerkonSW;
    Switch buzzerKeySW;
    Switch buzzerLockSW;

    private BluetoothLeAdvertiser bluetoothAdvertiser;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;

    private AdvertiseData.Builder dataBuilder;
    private AdvertiseSettings.Builder settingsBuilder;

    private IEditPresenter presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initUI();
        setListeners();

        memoryOperation = new MemoryOperation(this);
        presenter = new EditPresenter(this);

        setData();
    }

    void setData(){
        loginUserET.setText(memoryOperation.getLoginUser());
        passwordUserET.setText(memoryOperation.getPasswordUser());
        bluetoothSW.setChecked(memoryOperation.getBluetoothSW());
        wiegandSW.setChecked(memoryOperation.getWiegandSW());
        dallasSW.setChecked(memoryOperation.getDallasSW());
        gerkonSW.setChecked(memoryOperation.getGerkonSW());
        buttonSW.setChecked(memoryOperation.getButtonSW());
        lockSW.setChecked(memoryOperation.getLockSW());
        lockInvertSW.setChecked(memoryOperation.getLockInvertSW());
        //lockTimeET.setText(memoryOperation.getLockTimeConfig());
        buzzerCaseSW.setChecked(memoryOperation.getBuzzerCaseSW());
        buzzerGerkonSW.setChecked(memoryOperation.getBuzzerGerkonSW());
        buzzerKeySW.setChecked(memoryOperation.getBuzzerKeySW());
        buzzerLockSW.setChecked(memoryOperation.getBuzzerLockSW());
    }

    void setListeners() {
        saveButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().requestEditConfig();
            }
        });

        closeButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void initUI() {
        loginUserET = findViewById(R.id.et_login);
        passwordUserET = findViewById(R.id.et_password);
        saveButtonCV = findViewById(R.id.cv_save_button);

        bluetoothSW = findViewById(R.id.sw_bluetooth);
        wiegandSW = findViewById(R.id.sw_wiegand);
        dallasSW = findViewById(R.id.sw_dallas);
        gerkonSW = findViewById(R.id.sw_gerkon);
        closeButtonCV = findViewById(R.id.cv_close_button);
        buttonSW = findViewById(R.id.sw_button);
        lockSW = findViewById(R.id.sw_lock);
        lockInvertSW = findViewById(R.id.sw_lock_invert);
        lockTimeET = findViewById(R.id.et_time_open_lock);
        buzzerCaseSW = findViewById(R.id.sw_buzzer_case);
        buzzerGerkonSW = findViewById(R.id.sw_buzzer_gerkon);
        buzzerKeySW = findViewById(R.id.sw_buzzer_key);
        buzzerLockSW = findViewById(R.id.sw_buzzer_lock);
    }


    @Override
    public String getLoginUser() {
        return loginUserET.getText().toString();
    }

    @Override
    public String getPasswordUser() {
        return passwordUserET.getText().toString();
    }

    @Override
    public Boolean isWiegand() {
        return wiegandSW.isChecked();
    }

    @Override
    public Boolean isDallas() {
        return dallasSW.isChecked();
    }

    @Override
    public Boolean isGerkon() {
        return gerkonSW.isChecked();
    }

    @Override
    public Boolean isButton() {
        return buttonSW.isChecked();
    }

    @Override
    public MemoryOperation getMemoryOperation() {
        return memoryOperation;
    }

    @Override
    public Boolean isBluetooth() {
        return bluetoothSW.isChecked();
    }

    @Override
    public Boolean isLock() {
        return lockSW.isChecked();
    }

    @Override
    public Boolean isLockInvert() {
        return lockInvertSW.isChecked();
    }

    @Override
    public Integer getLockTime() {
        return Integer.valueOf(lockTimeET.getText().toString());
    }

    @Override
    public Boolean isBuzzerCase() {
        return buzzerCaseSW.isChecked();
    }

    @Override
    public Boolean isBuzzerGerkon() {
        return buzzerGerkonSW.isChecked();
    }

    @Override
    public Boolean isBuzzerKey() {
        return buzzerKeySW.isChecked();
    }

    @Override
    public Boolean isBuzzerLock() {
        return buzzerLockSW.isChecked();
    }

    @Override
    public void onResponse(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeView() {
        finish();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(this, "Произошла какая-то бяка", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IEditPresenter getPresenter() {
        return presenter;
    }
}
