package com.example.ufanet.editKey;

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
import com.example.ufanet.editConfig.IEditConfigView;
import com.example.ufanet.templates.TrimConfig;
import com.example.ufanet.utils.MemoryOperation;

public class EditKeyActivity extends AppCompatActivity implements IEditKeyView {

    MemoryOperation memoryOperation;
    EditText nameConfigET;
    CardView saveButtonCV;
    CardView closeButtonCV;

    private BluetoothLeAdvertiser bluetoothAdvertiser;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;

    private AdvertiseData.Builder dataBuilder;
    private AdvertiseSettings.Builder settingsBuilder;

    private TrimConfig trimConfig;
    private Integer id_config = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_key);
        initUI();
        setListeners();

        Intent intent = getIntent();
        id_config = intent.getIntExtra("id_config", 0);

        trimConfig = new TrimConfig(memoryOperation.getConfigDataName(id_config), memoryOperation.getConfigDataWord(id_config));

        setData();
    }

    void setData(){
        Log.d("lkgkdgnlkg", trimConfig.getWiegand().toString());

    }

    void setListeners() {
        saveButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        nameConfigET = findViewById(R.id.et_name_config);
        closeButtonCV = findViewById(R.id.cv_close_button);

        memoryOperation = new MemoryOperation(this);
    }

    @Override
    public void setFio(String text) {

    }

    @Override
    public void setKey(String text) {

    }

    @Override
    public MemoryOperation getMemoryOperation() {
        return memoryOperation;
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
