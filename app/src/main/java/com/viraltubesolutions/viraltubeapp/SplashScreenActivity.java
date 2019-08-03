package com.viraltubesolutions.viraltubeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.viraltubesolutions.viraltubeapp.activities.DynamicLinkOpenActivity;
import com.viraltubesolutions.viraltubeapp.activities.HomePageActivity;
import com.viraltubesolutions.viraltubeapp.activities.LoginActivity;
import com.viraltubesolutions.viraltubeapp.custommediaplayer.JZMediaIjkplayer;

import cn.jzvd.JZVideoPlayer;

public class SplashScreenActivity extends AppCompatActivity {

    LinearLayout splash_image;
    int SPLASH_DISPLAY_LENGTH = 2000;
    boolean doubleBackToExitPressedOnce = false;
    boolean isLoggedin;
    String UserType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();
        JZVideoPlayer.setMediaInterface(new JZMediaIjkplayer());

    }

    private void init() {
        splash_image = (LinearLayout) findViewById(R.id.splash_screen_image);
        //callAnimation();
        SharedPreferences preferences = getSharedPreferences("LogIn", MODE_PRIVATE);
        isLoggedin= preferences.getBoolean("isLoggedin",false);
        UserType=preferences.getString("UserType",null);
        getLink();


    }

    private void callAnimation() {
        //Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scaling);
        //splash_image.startAnimation(animation);

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
    private void getLink() {
        //FirebaseAnalytics.getInstance(getApplicationContext());
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Intent i=new Intent(SplashScreenActivity.this, DynamicLinkOpenActivity.class);
                            i.putExtra("deeplink",deepLink.toString());
                            startActivity(i);
                        }
                        else {
                            callAnimation();
                        }

                        Log.d("Here",deepLink+" ");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //callAnimation();
                        Log.w("Fail", "getDynamicLink:onFailure", e);
                        Toast.makeText(SplashScreenActivity.this, "failed to retrive link", Toast.LENGTH_SHORT).show();
                        Snackbar.make(splash_image,"failed to retrive link",Snackbar.LENGTH_SHORT).show();
                    }
                });
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
