package com.example.ufanet.passwordActivity;

import android.content.Intent;
import android.os.Bundle;
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


    }

    private void initUI(){
        passcodeView = (PasscodeView) findViewById(R.id.passcodeView);
        passcodeView
                .setPasscodeLength(4)
                .setLocalPasscode(memoryOperation.getPasscodeUser());
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
                finish();
            }
        });
    }

}