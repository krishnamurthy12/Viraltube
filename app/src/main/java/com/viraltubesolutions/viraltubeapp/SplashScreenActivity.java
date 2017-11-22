package com.viraltubesolutions.viraltubeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.viraltubesolutions.viraltubeapp.activities.HomePageActivity;
import com.viraltubesolutions.viraltubeapp.activities.LoginActivity;

public class SplashScreenActivity extends AppCompatActivity {

    LinearLayout splash_image;
    int SPLASH_DISPLAY_LENGTH = 3000;
    boolean doubleBackToExitPressedOnce = false;
    boolean isLoggedin;
    String UserType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();
    }

    private void init() {
        splash_image = (LinearLayout) findViewById(R.id.splash_screen_image);
        callAnimation();

        SharedPreferences preferences = getSharedPreferences("LogIn", MODE_PRIVATE);
        isLoggedin= preferences.getBoolean("isLoggedin",false);
        UserType=preferences.getString("UserType",null);

    }

    private void callAnimation() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scaling);
        splash_image.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isLoggedin)
                {
                    Intent homeIntent=new Intent(SplashScreenActivity.this, HomePageActivity.class);
                    startActivity(homeIntent);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_in);

                }
                else {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
                }
            }


        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(splash_image, "press back again to exit", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
