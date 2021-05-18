package com.example.ufanet.addConfig;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.example.ufanet.R;
import com.example.ufanet.WifiConnectAppCompatActivity;
import com.example.ufanet.utils.MemoryOperation;

public class AddConfigActivity extends WifiConnectAppCompatActivity implements IAddConfigView {

    MemoryOperation memoryOperation;

    EditText nameConfigET;

    CardView saveButtonCV;

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
    public static Integer MAX_LENGTH_LOCK_TIME = 5;
    public static Integer MAX_LENGTH_CONFIG_NAME = 128;
    public static Integer MIN_LENGTH_CONFIG_NAME = 1;
    public static Integer MIN_LOCK_TIME_VALUE = 1;
    public static Integer MAX_LOCK_TIME_VALUE = 31;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_config);
        initUI();
        setListeners();

        memoryOperation = new MemoryOperation(this);
        setData();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Добавление конфига");
        toolbar.setTitleTextColor(0xff313435);
        toolbar.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance);

        addBackButton();
    }

    private void addBackButton(){
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_r);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void setData(){
        nameConfigET.setText("Конфиг_" + (getMemoryOperation().getConfigsArraySize()+1));
        bluetoothSW.setChecked(getMemoryOperation().getBluetoothSW());
        wiegandSW.setChecked(getMemoryOperation().getWiegandSW());
        dallasSW.setChecked(getMemoryOperation().getDallasSW());
        gerkonSW.setChecked(getMemoryOperation().getGerkonSW());
        buttonSW.setChecked(getMemoryOperation().getButtonSW());
        lockSW.setChecked(getMemoryOperation().getLockSW());
        lockInvertSW.setChecked(getMemoryOperation().getLockInvertSW());
        lockTimeET.setText("4");
        buzzerCaseSW.setChecked(getMemoryOperation().getBuzzerCaseSW());
        buzzerGerkonSW.setChecked(getMemoryOperation().getBuzzerGerkonSW());
        buzzerKeySW.setChecked(getMemoryOperation().getBuzzerKeySW());
        buzzerLockSW.setChecked(getMemoryOperation().getBuzzerLockSW());
    }

    void setListeners() {
        saveButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getMemoryOperation().getConfigsArraySize() < MAX_COUNT_CONFIG){
                    if(isValidConfigName()){
                        if(isValidLockTime()){
                            String binaryString = getBinaryConfigString();
                            int number = getDecConfigInt(binaryString);
                            addNewConfig(number, getConfigName());
                            Toast.makeText(AddConfigActivity.this, "Конфиг добавлен", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
            }
        });
    }

    private Boolean isValidLockTime(){
        if(getLockTime() <= MAX_LOCK_TIME_VALUE){
            if(getLockTime() >= MIN_LOCK_TIME_VALUE){

            }
            else{
                onResponse("Время срабатывания должно быть от 1 до 31");
                return false;
            }
        }
        else{
            onResponse("Время срабатывания должно быть от 1 до 31");
            return false;
        }
        return true;
    }

    private Boolean isValidConfigName(){
        if(getConfigName().trim().length() >= MIN_LENGTH_CONFIG_NAME){
            if(getConfigName().length() < MAX_LENGTH_CONFIG_NAME){

            }
            else{
                onResponse("Название конфига не должно быть больше 128 символов");
                return false;
            }
        }
        else{
            onResponse("Название конфига не должно быть пустым");
            return false;
        }
        return true;
    }

    private String getBinaryConfigString(){
        return boolToString(isWiegand()) + boolToString(isDallas()) + boolToString(isBluetooth()) + boolToString(isGerkon()) + boolToString(isButton())
                + boolToString(isLock()) + boolToString(isLockInvert()) + boolToString(isBuzzerCase()) + boolToString(isBuzzerGerkon()) + boolToString(isBuzzerLock())
                + boolToString(isBuzzerKey()) + addingLeadingZeros(Integer.toBinaryString(getLockTime()));
    }

    private void addNewConfig(int number, String name){
        memoryOperation.setConfigDataWord(memoryOperation.getConfigsArraySize(), number);
        memoryOperation.setConfigDataName(memoryOperation.getConfigsArraySize(), name);
        memoryOperation.setConfigsArraySize(memoryOperation.getConfigsArraySize()+1);
    }

    private int getDecConfigInt(String binaryString){
        return Integer.parseInt(binaryString, 2);
    }

    String addingLeadingZeros(String text){
        while(text.length() != MAX_LENGTH_LOCK_TIME){
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
        Toast.makeText(this, "Произошла какая-то бяка(ошибка)", Toast.LENGTH_SHORT).show();
    }
}
