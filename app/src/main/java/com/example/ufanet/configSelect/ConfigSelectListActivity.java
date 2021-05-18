package com.example.ufanet.configSelect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ufanet.Adapter.ConfigSelectListAdapter;
import com.example.ufanet.R;
import com.example.ufanet.WifiConnectAppCompatActivity;
import com.example.ufanet.templates.TrimConfig;
import com.example.ufanet.utils.MemoryOperation;
import java.util.ArrayList;
import androidx.appcompat.widget.Toolbar;

public class ConfigSelectListActivity extends WifiConnectAppCompatActivity implements IConfigSelectListView {

    ConfigSelectListAdapter configSelectListAdapter;
    ArrayList<TrimConfig> items = new ArrayList<>();
    RecyclerView containerRV;
    RelativeLayout emptyListRL;

    Integer id_device;

    MemoryOperation memoryOperation;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_list);
        initUI();
        setListeners();

        containerRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        containerRV.setAdapter(configSelectListAdapter);

        Intent intent = getIntent();
        id_device = intent.getIntExtra("id_device", 0);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Конфиги");
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

    private Boolean isItemsConfigListArray(){
        if(memoryOperation.getConfigsArraySize()>0){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    void updateData(){
        items.clear();
        items.addAll(memoryOperation.getConfigList());
        configSelectListAdapter.notifyDataSetChanged();
        if(isItemsConfigListArray()){
            hideEmptyView();
        }
        else{
            showEmptyView();
        }
    }

    private void showEmptyView(){
        emptyListRL.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView(){
        emptyListRL.setVisibility(View.GONE);
    }

    void initUI(){
        emptyListRL = findViewById(R.id.l_empty_list);
        configSelectListAdapter = new ConfigSelectListAdapter( ConfigSelectListActivity.this, items);
        containerRV = findViewById(R.id.list);
        memoryOperation = new MemoryOperation(this);
    }

    void setListeners(){
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
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
