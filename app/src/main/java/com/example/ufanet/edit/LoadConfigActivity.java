package com.example.ufanet.edit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ufanet.Adapter.ConfigSelectAdapter;
import com.example.ufanet.R;
import com.example.ufanet.WifiConnectAppCompatActivity;
import com.example.ufanet.configSelect.ConfigSelectListActivity;
import com.example.ufanet.edit.presenter.LoadConfigPresenter;
import com.example.ufanet.edit.presenter.ILoadConfigPresenter;
import com.example.ufanet.templates.ConfigSelect;
import com.example.ufanet.utils.MemoryOperation;
import java.util.ArrayList;

public class LoadConfigActivity extends WifiConnectAppCompatActivity implements ILoadConfigView {

    MemoryOperation memoryOperation;
    EditText loginUserET;
    EditText passwordUserET;
    CardView saveButtonCV;
    CardView addSlaveButtonCV;

    RecyclerView listView;
    ArrayList<ConfigSelect> items = new ArrayList<>();

    ConfigSelectAdapter configSelectAdapter;

    private ILoadConfigPresenter presenter;
    private Integer id_device = 0;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initUI();
        setListeners();

        memoryOperation = new MemoryOperation(this);
        presenter = new LoadConfigPresenter(this);

        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(configSelectAdapter);

        items.add(new ConfigSelect("Главное устройство", "Не выбрано", (char) 565656));
        configSelectAdapter.notifyDataSetChanged();

        setData();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(0xff313435);
        getSupportActionBar().setTitle("Настройки конфига");
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

    void initUI() {
        loginUserET = findViewById(R.id.et_login);
        passwordUserET = findViewById(R.id.et_password);
        saveButtonCV = findViewById(R.id.cv_save_button);
        addSlaveButtonCV = findViewById(R.id.cv_add_slave_button);
        configSelectAdapter = new ConfigSelectAdapter( LoadConfigActivity.this, items);
        listView = findViewById(R.id.list);
    }

    void setData(){
        loginUserET.setText(memoryOperation.getLoginUser());
        passwordUserET.setText(memoryOperation.getPasswordUser());
    }

    void setListeners() {
        saveButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().requestEditConfig();
            }
        });


        addSlaveButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(items.size() < 4){
                    items.add(new ConfigSelect("Доп. устройство", "Не выбрано", (char) 565656));
                    configSelectAdapter.notifyDataSetChanged();
                    if(items.size() == 4){
                        addSlaveButtonCV.setVisibility(View.GONE);
                    }
                }
            }
        });
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
    public void onResponse(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeView() {
        Intent data = new Intent();
        getMemoryOperation().setLoginUser(getLoginUser());
        getMemoryOperation().setPasswordUser(getPasswordUser());
        setResult(2, data);
        finish();
    }

    @Override
    public void startSelectConfigActivity(Integer position) {
        Intent intent = new Intent(LoadConfigActivity.this, ConfigSelectListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("id_device", position);
        id_device = position;
        startActivityForResult(intent, 100);
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(this, "Произошла какая-то бяка", Toast.LENGTH_SHORT).show();
    }

    @Override
    public ILoadConfigPresenter getPresenter() {
        return presenter;
    }

    @Override
    public Integer[] getConfigsSelect() {
        Integer[] items = new Integer[this.items.size()];
        for (int i = 0; i < this.items.size(); i++) {
            items[i] = (int) this.items.get(i).getConfigWord();
        }
        return items;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Integer id_config = data.getIntExtra("id_config", 0);
            items.get(id_device).setNameConfig(memoryOperation.getConfigDataName(id_config));
            items.get(id_device).setWordConfig(memoryOperation.getConfigDataWord(id_config));
            configSelectAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
