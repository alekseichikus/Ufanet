package com.example.ufanet.home;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ufanet.R;
import com.example.ufanet.utils.MemoryOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.ufanet.home.AuthActivity.isBluetoothEnabled;
import static com.example.ufanet.home.AuthActivity.isGeoDisabled;
import static com.example.ufanet.home.AuthActivity.isWifiEnabled;

public class PermActivity extends AppCompatActivity {

    MemoryOperation memoryOperation;
    ConstraintLayout imageView;
    CardView circleCV;
    CardView rectangleCV;
    CardView closeCV;
    CardView buttonCV;
    TextView titleTV;
    TextView postTitleTV;
    ImageView logoIV;
    LinearLayout contentLL;

    public final Integer REQUEST_PERMISSION_CODE = 1;

    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perm);
        imageView = findViewById(R.id.image);
        circleCV = findViewById(R.id.circle);
        rectangleCV = findViewById(R.id.rectangle);
        contentLL = findViewById(R.id.ll_content);
        closeCV = findViewById(R.id.cv_close_button);
        logoIV = findViewById(R.id.imageView);
        buttonCV = findViewById(R.id.cv_button);
        titleTV = findViewById(R.id.tv_title);
        postTitleTV = findViewById(R.id.tv_posttitle);

        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        window.setAttributes(winParams);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setListeners();

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String postTitle = intent.getStringExtra("post_title");
        id = intent.getIntExtra("id", 0);
        int idResourceImage = intent.getIntExtra("id_image", 0);

        titleTV.setText(title);
        postTitleTV.setText(postTitle);
        logoIV.setImageResource(idResourceImage);

        if(id==2){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //buttonCV.setCardBackgroundColor(0xff00ff00);
            }
            else {
                changeButtonText("Перейти в настройки");
                //buttonCV.setCardBackgroundColor(0xff000000);
            }
        }

        blockAnimate();
    }

    private void setListeners(){
        closeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id == 0){
                    if (isBluetoothEnabled()) {
                        loadBluetoothActivity();
                    }
                    else{
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivity(intent);
                    }
                }
                else if(id == 1){
                    if (isWifiEnabled(PermActivity.this)) {
                        loadWifiActivity();
                    }
                    else{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                            startActivityForResult(panelIntent, 0);
                        } else {
                            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                            if(wifiManager != null){
                                wifiManager.setWifiEnabled(true);
                                loadWifiActivity();
                            }
                        }
                    }
                } else if (id == 2) {
                    if(checkAndRequestPermissions()) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }
            }
        });
    }

    private  boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_PERMISSION_CODE);
            return false;
        }
        return true;
    }


    private void loadBluetoothActivity(){
        Intent intent = new Intent(PermActivity.this, PermActivity.class);
        intent.putExtra("title", "Wifi");
        intent.putExtra("id_image", R.drawable.ic_wifi);
        intent.putExtra("id", 1);
        intent.putExtra("post_title", "Bluetooth нам нужен для поиска устройства" +
                " и для активации функции администрирования");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void loadWifiActivity(){
        Intent intent = new Intent(PermActivity.this, PermActivity.class);
        intent.putExtra("title", "Геолокация");
        intent.putExtra("id_image", R.drawable.ic_map_marker_alt);
        intent.putExtra("id", 2);
        intent.putExtra("post_title", "Bluetooth нам нужен для поиска устройства" +
                " и для активации функции администрирования");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(id==0)
        {
            if (isBluetoothEnabled()) {
                loadBluetoothActivity();
            }
        }
        else if(id==1){
            if (isWifiEnabled(this)){
                loadWifiActivity();
            }
        }
        else if(id==2){
            if (ActivityCompat.checkSelfPermission(PermActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if(isGeoDisabled(PermActivity.this)){
                    //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
                else{
                    finish();
                    Intent intent = new Intent(PermActivity.this, AuthActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        }
    }

    private void rectangleAnimate(){
        final int bbnewLeftMargin = 300;
        Animation ab = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rectangleCV.getLayoutParams();
                params.leftMargin = (int)(bbnewLeftMargin * interpolatedTime - 600);
                rectangleCV.setLayoutParams(params);
            }


        };
        ab.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
                animation1.setDuration(400);
                animation1.setFillAfter(true);
                contentLL.startAnimation(animation1);
                contentLL.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ab.setDuration(600); // in ms
        rectangleCV.startAnimation(ab);
    }

    private void circleAnimate(){
        final int anewLeftMargin = 200;
        Animation aa = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) circleCV.getLayoutParams();
                params.rightMargin = (int)(anewLeftMargin * interpolatedTime + -140);
                circleCV.setLayoutParams(params);
            }


        };
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        aa.setDuration(300); // in ms
        circleCV.startAnimation(aa);
    }

    private void blockAnimate(){
        final int newLeftMargin = 300;
        final int newLeftMargin2 = 1700;
        Animation a = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
                params.leftMargin = (int)(newLeftMargin * interpolatedTime);
                params.width  = (int)(newLeftMargin2 * interpolatedTime) + 100;
                params.height = (int)(newLeftMargin2 * interpolatedTime) + 100;
                imageView.setLayoutParams(params);
            }


        };
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rectangleAnimate();
                circleAnimate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        a.setDuration(600); // in ms
        imageView.startAnimation(a);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION_CODE) {
            Map<String, Integer> perms = new HashMap<>();
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "sms & location services permission granted");
                } else {
                    Log.d(TAG, "Some permissions are not granted ask again ");
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        buttonCV.setCardBackgroundColor(0xff00ff00);
                    }
                    else {
                        changeButtonText("Перейти в настройки");
                        buttonCV.setCardBackgroundColor(0xff000000);
                        Intent intent = new Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                }
            }
        }
    }

    private void changeButtonText(String text){
        TextView textView = findViewById(R.id.tv_button);
        textView.setText(text);
    }
}
