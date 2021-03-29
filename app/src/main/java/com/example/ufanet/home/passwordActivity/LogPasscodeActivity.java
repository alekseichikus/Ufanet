package com.example.ufanet.home.passwordActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ufanet.MainActivity;
import com.example.ufanet.R;
import com.example.ufanet.passwordView.PasscodeView;
import com.example.ufanet.utils.MemoryOperation;

public class LogPasscodeActivity extends AppCompatActivity {

    private MemoryOperation memoryOperation;
    private PasscodeView passcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        memoryOperation = new MemoryOperation(this);

        initUI();
        setListeners();

        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        passcodeView
                .setPasscodeLength(4)
                .setLocalPasscode(memoryOperation.getPasswordUser());
    }

    private void initUI(){
        passcodeView = (PasscodeView) findViewById(R.id.passcodeView);
    }
    private void setListeners(){
        passcodeView.setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail(String wrongNumber) {
                Toast.makeText(getApplication(),R.string.try_again_passcode,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String number) {
                Intent intent = new Intent(LogPasscodeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                onBackPressed();
            }
        });
    }

}