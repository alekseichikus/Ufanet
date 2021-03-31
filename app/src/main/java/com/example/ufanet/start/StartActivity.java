package com.example.ufanet.start;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ufanet.passwordActivity.LogPasscodeActivity;
import com.example.ufanet.passwordActivity.RegPasscodeActivity;
import com.example.ufanet.utils.MemoryOperation;

import java.security.SecureRandom;
import java.util.Base64;

public class StartActivity extends AppCompatActivity {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    MemoryOperation memoryOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        memoryOperation = new MemoryOperation(this);

        if(memoryOperation.getTokenUser().isEmpty()){
            memoryOperation.setTokenUser(generateNewToken());
        }

        if(memoryOperation.getPasscodeUser().isEmpty()){
            Intent intent = new Intent(StartActivity.this, RegPasscodeActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(StartActivity.this, LogPasscodeActivity.class);
            startActivity(intent);
        }
    }

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
