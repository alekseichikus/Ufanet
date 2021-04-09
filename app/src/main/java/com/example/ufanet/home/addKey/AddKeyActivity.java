package com.example.ufanet.home.addKey;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ufanet.R;
import com.example.ufanet.home.addKey.presenter.AddKeyPresenter;
import com.example.ufanet.home.addKey.presenter.IAddKeyPresenter;
import com.example.ufanet.utils.MemoryOperation;

public class AddKeyActivity extends AppCompatActivity implements IAddKeyView {

    MemoryOperation memoryOperation;
    EditText fioET;
    EditText keyET;
    CardView addButtonCV;
    CardView closeButtonCV;
    IAddKeyPresenter addKeyPresenter;

    public static Integer MAX_COUNT_CONFIG = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_key);
        initUI();
        setListeners();

        memoryOperation = new MemoryOperation(this);

        setData();
    }

    void setData(){

    }

    void setListeners() {
        addButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memoryOperation.setKeyDataFio(memoryOperation.getKeyArraySize(), fioET.getText().toString());
                memoryOperation.setKeyDataKey(memoryOperation.getKeyArraySize(), keyET.getText().toString());
                memoryOperation.setKeyDataType(memoryOperation.getKeyArraySize(), 1);
                memoryOperation.setKeyArraySize(memoryOperation.getKeyArraySize()+1);

                addKeyPresenter.requestEditConfig();
            }
        });

        closeButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void initUI() {
        addButtonCV = findViewById(R.id.cv_add_button);
        keyET = findViewById(R.id.et_key);
        fioET = findViewById(R.id.et_fio);
        closeButtonCV = findViewById(R.id.cv_close_button);
        addKeyPresenter = new AddKeyPresenter(this);
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
    public String getFio() {
        return fioET.getText().toString();
    }

    @Override
    public String getKey() {
        return keyET.getText().toString();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(this, "Произошла какая-то бяка", Toast.LENGTH_SHORT).show();
    }
}
