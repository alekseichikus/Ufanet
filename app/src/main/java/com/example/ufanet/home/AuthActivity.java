package com.example.ufanet.home;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class AuthActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_auth);
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
        winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        window.setAttributes(winParams);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setListeners();

        logoIV.setImageResource(R.drawable.ic_user_alt);
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

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        blockAnimate();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        aa.setDuration(400); // in ms
        circleCV.startAnimation(aa);
    }

    private void blockAnimate(){
        final int newLeftMargin = 300;
        final int newLeftMargin2 = 1300;
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
        a.setDuration(800); // in ms
        imageView.startAnimation(a);
    }
}
