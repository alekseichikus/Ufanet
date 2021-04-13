package com.example.ufanet.start;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.example.ufanet.passwordActivity.LogPasscodeActivity;
import com.example.ufanet.passwordActivity.RegPasscodeActivity;
import com.example.ufanet.utils.MemoryOperation;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class StartActivity extends AppCompatActivity {

    MemoryOperation memoryOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        memoryOperation = new MemoryOperation(this);

        if(memoryOperation.getTokenUser().isEmpty()){
            String alphabet = "1234567890ABCDEF";
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            int length = 16;

            for(int i = 0; i < length; i++) {
                int index = random.nextInt(alphabet.length());
                char randomChar = alphabet.charAt(index);
                sb.append(randomChar);
            }

            String randomString = sb.toString();
            System.out.println("Random String is: " + randomString);
            memoryOperation.setTokenUser(randomString);
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
}
