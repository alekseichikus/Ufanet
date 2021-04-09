package com.example.ufanet.addConfig;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ufanet.R;
import com.example.ufanet.edit.IEditView;
import com.example.ufanet.edit.presenter.EditPresenter;
import com.example.ufanet.edit.presenter.IEditPresenter;
import com.example.ufanet.utils.MemoryOperation;

public class AddConfigActivity extends AppCompatActivity implements IAddConfigView {

    MemoryOperation memoryOperation;
    EditText nameConfigET;
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

    public static Integer MAX_COUNT_CONFIG = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_config);
        initUI();
        setListeners();

        memoryOperation = new MemoryOperation(this);

        setData();
    }

    void setData(){
        nameConfigET.setText("Конфиг_" + (memoryOperation.getConfigsArraySize()+1));
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
                if(memoryOperation.getConfigsArraySize() < MAX_COUNT_CONFIG){

                    String binaryString = boolToString(isWiegand()) + boolToString(isDallas()) + boolToString(isBluetooth()) + boolToString(isGerkon()) + boolToString(isButton())
                            + boolToString(isLock()) + boolToString(isLockInvert()) + boolToString(isBuzzerCase()) + boolToString(isBuzzerGerkon()) + boolToString(isBuzzerLock())
                            + boolToString(isBuzzerKey()) + addingLeadingZeros(Integer.toBinaryString(getLockTime()));
                    int number = Integer.parseInt(binaryString, 2);


                    memoryOperation.setConfigDataWord(memoryOperation.getConfigsArraySize(), number);
                    memoryOperation.setConfigDataName(memoryOperation.getConfigsArraySize(), getConfigName());
                    memoryOperation.setConfigsArraySize(memoryOperation.getConfigsArraySize()+1);

                    finish();
                }
            }
        });

        closeButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    String addingLeadingZeros(String text){
        while(text.length() != 5){
            text = "0" + text;
        }
        return text;
    }

    String boolToString(Boolean b) {
        return String.valueOf(b.compareTo(false));
    }

    void initUI() {
        saveButtonCV = findViewById(R.id.cv_save_button);

        bluetoothSW = findViewById(R.id.sw_bluetooth);
        wiegandSW = findViewById(R.id.sw_wiegand);
        nameConfigET = findViewById(R.id.et_name_config);
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
    public String getConfigName() {
        return nameConfigET.getText().toString();
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
}
