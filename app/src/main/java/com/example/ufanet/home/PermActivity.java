package com.example.ufanet.home;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ufanet.R;
import com.example.ufanet.passwordActivity.LogPasscodeActivity;
import com.example.ufanet.passwordActivity.RegPasscodeActivity;
import com.example.ufanet.utils.MemoryOperation;

import java.util.Random;

public class PermActivity extends AppCompatActivity {

    MemoryOperation memoryOperation;
    ConstraintLayout imageView;
    CardView circleCV;
    CardView rectangleCV;
    CardView closeCV;
    LinearLayout contentLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_perm);
        imageView = findViewById(R.id.image);
        circleCV = findViewById(R.id.circle);
        rectangleCV = findViewById(R.id.rectangle);
        contentLL = findViewById(R.id.ll_content);
        closeCV = findViewById(R.id.cv_close_button);

        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        window.setAttributes(winParams);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setListeners();
    }

    private void setListeners(){
        closeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        a.setDuration(800); // in ms
        imageView.startAnimation(a);
    }
}
