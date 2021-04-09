package com.example.ufanet.configList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ufanet.Adapter.ConfigListAdapter;
import com.example.ufanet.R;
import com.example.ufanet.editConfig.EditConfigActivity;
import com.example.ufanet.keyss.ChangeKeyBottomDialogFragment;
import com.example.ufanet.settings.IKey;
import com.example.ufanet.settings.presenter.ISettingPresenter;
import com.example.ufanet.templates.TrimConfig;
import com.example.ufanet.utils.MemoryOperation;

import java.util.ArrayList;

public class ConfigListActivity extends AppCompatActivity implements IConfigListView {

    CardView closeButtonCV;
    ConfigListAdapter adapter;
    ArrayList<TrimConfig> key_items = new ArrayList<>();
    RecyclerView listView;

    MemoryOperation memoryOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_list);
        initUI();
        setListeners();

        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(adapter);

        key_items.addAll(memoryOperation.getConfigList());
        adapter.notifyDataSetChanged();
    }

    void initUI(){
        closeButtonCV = findViewById(R.id.cv_close_button);
        adapter = new ConfigListAdapter( ConfigListActivity.this, key_items);
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
    public void showFragmentSettings(IKey key) {
        ChangeKeyBottomDialogFragment addPhotoBottomDialogFragment =
                new ChangeKeyBottomDialogFragment(key);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), "change_key_fragment");
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onResponseFailure(Throwable throwable) {

    }

    @Override
    public ISettingPresenter getPresenter() {
        return null;
    }

    @Override
    public void closeView() {

    }

    @Override
    public void startEditConfig(Integer id_config) {
        Intent intent = new Intent(this, EditConfigActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("id_config", id_config);
        startActivity(intent);
    }

    @Override
    public void startControlConfigDialogFragment(Integer id_config) {
        ControlConfigListDialogFragment dialogFragment = new ControlConfigListDialogFragment(id_config);
        dialogFragment.show(getSupportFragmentManager(), "Sample Fragment");
    }
}
