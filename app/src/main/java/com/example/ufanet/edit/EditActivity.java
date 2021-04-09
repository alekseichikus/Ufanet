package com.example.ufanet.edit;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ufanet.Adapter.ConfigSelectAdapter;
import com.example.ufanet.R;
import com.example.ufanet.configSelect.ConfigSelectListActivity;
import com.example.ufanet.edit.presenter.EditPresenter;
import com.example.ufanet.edit.presenter.IEditPresenter;
import com.example.ufanet.templates.ConfigSelect;
import com.example.ufanet.utils.MemoryOperation;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity implements IEditView {

    MemoryOperation memoryOperation;
    EditText loginUserET;
    EditText passwordUserET;
    CardView saveButtonCV;
    CardView closeButtonCV;
    CardView addSlaveButtonCV;

    RecyclerView listView;
    ArrayList<ConfigSelect> key_items = new ArrayList<>();

    ConfigSelectAdapter configSelectAdapter;

    //EditText lockTimeET;

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

        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(configSelectAdapter);

        key_items.add(new ConfigSelect("Главное устройство", "Не выбрано", (char) 565656));
        configSelectAdapter.notifyDataSetChanged();

        setData();
    }

    void setData(){
        loginUserET.setText(memoryOperation.getLoginUser());
        passwordUserET.setText(memoryOperation.getPasswordUser());
        //lockTimeET.setText(memoryOperation.getLockTimeConfig());
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

        addSlaveButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key_items.add(new ConfigSelect("Дополнительное устройство", "Не выбрано", (char) 565656));
                configSelectAdapter.notifyDataSetChanged();
            }
        });
    }

    void initUI() {
        loginUserET = findViewById(R.id.et_login);
        passwordUserET = findViewById(R.id.et_password);
        saveButtonCV = findViewById(R.id.cv_save_button);
        addSlaveButtonCV = findViewById(R.id.cv_add_slave_button);

        closeButtonCV = findViewById(R.id.cv_close_button);

        //lockTimeET = findViewById(R.id.et_time_open_lock);

        configSelectAdapter = new ConfigSelectAdapter( EditActivity.this, key_items);
        listView = findViewById(R.id.list);

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
    public MemoryOperation getMemoryOperation() {
        return memoryOperation;
    }

    @Override
    public Integer getLockTime() {
        //Integer.valueOf(lockTimeET.getText().toString())
        return 11;
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
    public void startSelectConfigActivity(Integer position) {
        Intent intent = new Intent(EditActivity.this, ConfigSelectListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("id_device", position);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(this, "Произошла какая-то бяка", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IEditPresenter getPresenter() {
        return presenter;
    }

    @Override
    public Integer[] getConfigsSelect() {
        Integer[] items = new Integer[key_items.size()];
        for (int i = 0; i < key_items.size(); i++) {
            items[i] = (int)key_items.get(i).getConfigWord();
        }
        return items;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("erggreg", "asdasd");
        if (resultCode == Activity.RESULT_OK) {
            Log.d("erggreg", "sdsadasd");
            // TODO Extract the data returned from the child Activity.
            Integer id_device = data.getIntExtra("id_device", 0);
            Integer id_config = data.getIntExtra("id_config", 0);
            key_items.get(id_device).setNameConfig(memoryOperation.getConfigDataName(id_config));
            configSelectAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
