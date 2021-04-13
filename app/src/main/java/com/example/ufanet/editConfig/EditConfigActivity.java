package com.example.ufanet.editConfig;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.ufanet.R;
import com.example.ufanet.templates.TrimConfig;
import com.example.ufanet.utils.MemoryOperation;

public class EditConfigActivity extends AppCompatActivity implements IEditConfigView {

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

    private TrimConfig trimConfig;
    private Integer id_config = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_config);
        initUI();
        setListeners();

        Intent intent = getIntent();
        id_config = intent.getIntExtra("id_config", 0);

        trimConfig = new TrimConfig(memoryOperation.getConfigDataName(id_config), memoryOperation.getConfigDataWord(id_config));

        setData();
    }

    void setData(){
        setWiegand(intToBoolean(trimConfig.getWiegand()));
        setDallas(intToBoolean(trimConfig.getDallas()));
        setGerkon(intToBoolean(trimConfig.getGerkon()));
        setButton(intToBoolean(trimConfig.getButton()));
        setBluetooth(intToBoolean(trimConfig.getBluetooth()));
        setLockInvert(intToBoolean(trimConfig.getLock_invert()));
        setLockTime(trimConfig.getLock_time());
        setConfigName(trimConfig.getNameConfig());
        setBuzzerCase(intToBoolean(trimConfig.getBuzzer_case()));
        setBuzzerGerkon(intToBoolean(trimConfig.getBuzzer_gerkon()));
        setBuzzerKey(intToBoolean(trimConfig.getBuzzer_key()));
        setBuzzerLock(intToBoolean(trimConfig.getBuzzer_lock()));
    }

    void setListeners() {
        saveButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String binaryString = boolToString(isWiegand()) + boolToString(isDallas()) + boolToString(isBluetooth()) + boolToString(isGerkon()) + boolToString(isButton())
                        + boolToString(isLock()) + boolToString(isLockInvert()) + boolToString(isBuzzerCase()) + boolToString(isBuzzerGerkon()) + boolToString(isBuzzerLock())
                        + boolToString(isBuzzerKey()) + addingLeadingZeros(Integer.toBinaryString(getLockTime()));

                int number = Integer.parseInt(binaryString, 2);

                memoryOperation.setConfigDataWord(id_config, number);
                memoryOperation.setConfigDataName(id_config, getConfigName());

                finish();
            }
        });

        closeButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean intToBoolean(int input) {
        if((input==0)||(input==1)) {
            return input!=0;
        }
        return true;
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

        memoryOperation = new MemoryOperation(this);
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
    public void setWiegand(Boolean state) {
        wiegandSW.setChecked(state);
    }

    @Override
    public void setDallas(Boolean state) {
        dallasSW.setChecked(state);
    }

    @Override
    public void setGerkon(Boolean state) {
        gerkonSW.setChecked(state);
    }

    @Override
    public void setButton(Boolean state) {
        buttonSW.setChecked(state);
    }

    @Override
    public void setBluetooth(Boolean state) {
        bluetoothSW.setChecked(state);
    }

    @Override
    public void setLock(Boolean state) {
        lockSW.setChecked(state);
    }

    @Override
    public void setLockInvert(Boolean state) {
        lockInvertSW.setChecked(state);
    }

    @Override
    public void setLockTime(Integer time) {
        lockTimeET.setText(time.toString());
    }

    @Override
    public void setConfigName(String text) {
        nameConfigET.setText(text);
    }

    @Override
    public void setBuzzerCase(Boolean state) {
        buzzerCaseSW.setChecked(state);
    }

    @Override
    public void setBuzzerGerkon(Boolean state) {
        buzzerGerkonSW.setChecked(state);
    }

    @Override
    public void setBuzzerKey(Boolean state) {
        buzzerKeySW.setChecked(state);
    }

    @Override
    public void setBuzzerLock(Boolean state) {
        buzzerLockSW.setChecked(state);
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
