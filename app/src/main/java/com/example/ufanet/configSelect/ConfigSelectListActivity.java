package com.example.ufanet.configSelect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ufanet.Adapter.ConfigListAdapter;
import com.example.ufanet.Adapter.ConfigSelectListAdapter;
import com.example.ufanet.R;
import com.example.ufanet.configList.ControlConfigListDialogFragment;
import com.example.ufanet.configList.IConfigListView;
import com.example.ufanet.editConfig.EditConfigActivity;
import com.example.ufanet.keyss.ChangeKeyBottomDialogFragment;
import com.example.ufanet.settings.IKey;
import com.example.ufanet.settings.presenter.ISettingPresenter;
import com.example.ufanet.templates.TrimConfig;
import com.example.ufanet.utils.MemoryOperation;

import java.util.ArrayList;

public class ConfigSelectListActivity extends AppCompatActivity implements IConfigSelectListView {

    CardView closeButtonCV;
    ConfigSelectListAdapter adapter;
    ArrayList<TrimConfig> key_items = new ArrayList<>();
    RecyclerView listView;

    Integer id_device;

    MemoryOperation memoryOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_list);
        initUI();
        setListeners();

        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        id_device = intent.getIntExtra("id_device", 0);

        key_items.addAll(memoryOperation.getConfigList());
        adapter.notifyDataSetChanged();
    }

    void initUI(){
        closeButtonCV = findViewById(R.id.cv_close_button);
        adapter = new ConfigSelectListAdapter( ConfigSelectListActivity.this, key_items);
        listView = findViewById(R.id.list);
        memoryOperation = new MemoryOperation(this);
    }

    void setListeners(){
        closeButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onResponse(String string) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onResponseFailure(Throwable throwable) {

    }


    @Override
    public void closeView() {

    }

    @Override
    public void selectConfig(Integer id_config) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("id_config", id_config);
        resultIntent.putExtra("id_device", id_device);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
