package com.example.ufanet.configList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.Toolbar;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ufanet.Adapter.ConfigListAdapter;
import com.example.ufanet.R;
import com.example.ufanet.WifiConnectAppCompatActivity;
import com.example.ufanet.editConfig.EditConfigActivity;
import com.example.ufanet.templates.TrimConfig;
import com.example.ufanet.utils.MemoryOperation;
import java.util.ArrayList;

public class ConfigListActivity extends WifiConnectAppCompatActivity implements IConfigListView {

    ConfigListAdapter configListAdapter;
    ArrayList<TrimConfig> items = new ArrayList<>();
    RecyclerView containerRV;
    RelativeLayout emptyListRL;

    private Toolbar toolbar;
    MemoryOperation memoryOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_list);

        memoryOperation = new MemoryOperation(this);

        initUI();
        setListeners();

        if(isItemsConfigListArray()){
            hideEmptyView();
        }
        else{
            showEmptyView();
        }

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xff313435);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Конфиги");
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

    @Override
    protected void onResume() {
        super.onResume();
        items.clear();
        items.addAll(memoryOperation.getConfigList());
        configListAdapter.notifyDataSetChanged();
    }

    void initUI(){
        emptyListRL = findViewById(R.id.l_empty_list);
        configListAdapter = new ConfigListAdapter( ConfigListActivity.this, items);
        containerRV = findViewById(R.id.list);
        containerRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        containerRV.setAdapter(configListAdapter);
    }

    void setListeners(){
    }

    private Boolean isItemsConfigListArray(){
        if(memoryOperation.getConfigsArraySize()>0){
            return true;
        }
        else{
            return false;
        }
    }

    private void showEmptyView(){
        emptyListRL.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView(){
        emptyListRL.setVisibility(View.GONE);
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
    public void startEditConfig(Integer id_config) {
        Intent intent = new Intent(this, EditConfigActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("id_config", id_config);
        startActivityForResult(intent, 0);
    }

    @Override
    public void startControlConfigDialogFragment(Integer id_config) {
        ControlConfigListDialogFragment dialogFragment = new ControlConfigListDialogFragment(id_config);
        dialogFragment.show(getSupportFragmentManager(), "Sample Fragment");
    }
}
