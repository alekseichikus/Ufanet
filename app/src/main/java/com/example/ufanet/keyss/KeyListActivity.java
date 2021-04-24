package com.example.ufanet.keyss;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ufanet.Adapter.KeyListAdapter;
import com.example.ufanet.R;
import com.example.ufanet.WifiConnectAppCompatActivity;
import com.example.ufanet.configList.ControlConfigListDialogFragment;
import com.example.ufanet.editConfig.EditConfigActivity;
import com.example.ufanet.editKey.EditKeyActivity;
import com.example.ufanet.settings.IKey;
import com.example.ufanet.settings.presenter.ISettingPresenter;
import com.example.ufanet.utils.MemoryOperation;

import java.util.ArrayList;

public class KeyListActivity extends WifiConnectAppCompatActivity implements IKeyListView{

    CardView closeButtonCV;
    KeyListAdapter adapter;
    ArrayList<IKey> key_items = new ArrayList<>();
    RecyclerView listView;
    MemoryOperation memoryOperation;
    RelativeLayout emptyListRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_list);
        initUI();
        setListeners();

        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    void updateData(){
        key_items.clear();
        key_items.addAll(memoryOperation.getKeyList());
        adapter.notifyDataSetChanged();
        if(isItemsKeyListArray()){
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

    private Boolean isItemsKeyListArray(){
        if(memoryOperation.getKeyArraySize()>0){
            return true;
        }
        else{
            return false;
        }
    }

    void initUI(){
        closeButtonCV = findViewById(R.id.cv_close_button);
        emptyListRL = findViewById(R.id.l_empty_list);
        adapter = new KeyListAdapter( KeyListActivity.this, key_items);
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
    public void startEditKey(Integer position) {
        Intent intent = new Intent(this, EditKeyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("id_key", position);
        startActivity(intent);
    }

    @Override
    public void startControlKeyDialogFragment(Integer position) {
        ControKeyListDialogFragment dialogFragment = new ControKeyListDialogFragment(position);
        dialogFragment.show(getSupportFragmentManager(), "Sample Fragment");
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
}
