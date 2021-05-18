package com.example.ufanet.editKey;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.ufanet.R;
import com.example.ufanet.WifiConnectAppCompatActivity;
import com.example.ufanet.home.addKey.IAddKeyView;
import com.example.ufanet.home.addKey.presenter.AddKeyPresenter;
import com.example.ufanet.home.addKey.presenter.IAddKeyPresenter;
import com.example.ufanet.utils.MemoryOperation;

public class EditKeyActivity extends WifiConnectAppCompatActivity implements IEditKeyView, IAddKeyView {

    MemoryOperation memoryOperation;
    EditText fioET;
    EditText keyET;
    CardView saveButtonCV;

    private Integer id_key = 0;
    private Toolbar toolbar;
    IAddKeyPresenter addKeyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_key);
        initUI();
        setListeners();

        addKeyPresenter = new AddKeyPresenter(this);

        Intent intent = getIntent();
        id_key = intent.getIntExtra("id_key", 0);

        setData();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xff313435);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Редактирование ключа");
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
        fioET.setText(memoryOperation.getKeyDataFIO(id_key));
        keyET.setText(memoryOperation.getKeyDataKey(id_key));
    }

    void setListeners() {
        saveButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memoryOperation.setKeyDataFio(id_key, fioET.getText().toString());
                memoryOperation.setKeyDataKey(id_key, keyET.getText().toString());
                saveButtonCV.setEnabled(false);
                addKeyPresenter.requestEditConfig();
            }
        });
    }

    void initUI() {
        saveButtonCV = findViewById(R.id.cv_save_button);
        fioET = findViewById(R.id.et_fio);
        keyET = findViewById(R.id.et_key);

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
        Toast.makeText(EditKeyActivity.this, "Ключ изменен", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public String getFio() {
        return "a";
    }

    @Override
    public String getKey() {
        return "a";
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(this, "Произошла какая-то бяка", Toast.LENGTH_SHORT).show();
    }
}
