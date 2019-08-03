package com.viraltubesolutions.viraltubeapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.viraltubesolutions.viraltubeapp.R;

import cn.jzvd.JZVideoPlayerStandard;

public class DynamicLinkOpenActivity extends AppCompatActivity {

    JZVideoPlayerStandard jzVideoPlayerStandard;
    static String deeplink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_link_open);
        Bundle extras = getIntent().getExtras();
        jzVideoPlayerStandard= (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
        if (extras != null) {
            deeplink = extras.getString("deeplink");
            setdata();
            /*Log.d("receivedlink",deeplink);
            Toast.makeText(this, "link is "+deeplink, Toast.LENGTH_SHORT).show();*/

        }

    }
    public void setdata()
    {
        jzVideoPlayerStandard.setUp(deeplink
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
        Glide.with(this)
                .load("http://viraltube.co.in/viral/kt_talent.jpeg")
                .into(jzVideoPlayerStandard.thumbImageView);
    }

}
