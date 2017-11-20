package com.viraltubesolutions.viraltubeapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viraltubesolutions.videoplayerlibrary.customview.JZVideoPlayerStandard;
import com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.shareResponse.ShareResponse;
import com.viraltubesolutions.viraltubeapp.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;


public class SharedActivity extends AppCompatActivity {
//RecyclerView mRecyclerView;
//LinearLayoutManager mLayoutManager;
JZVideoPlayerStandard jzVideoPlayerStandard;
SpotsDialog loadingDialog;
String userID;
    ArrayList<ShareResponse> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);
        initializeViews();
    }
    private void initializeViews()
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("Data", null);
        Type type = new TypeToken<ArrayList<ShareResponse>>() {}.getType();
        arrayList= gson.fromJson(json, type);

        jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer_as);

        // mRecyclerView= (RecyclerView) findViewById(R.id.recyclerview_sharedvideo);
        //mRecyclerView.setHasFixedSize(true);
        //mLayoutManager = new LinearLayoutManager(this);
        //mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences preferences =getSharedPreferences("LogIn", MODE_PRIVATE);
        userID=preferences.getString("userID",null);

        loadingDialog=new SpotsDialog(this,"Loading your video please wait...");
        loadingDialog.show();
        //SharedVideoAdapter adapter=new SharedVideoAdapter(SharedActivity.this,userID,loadingDialog);
        //mRecyclerView.setAdapter(adapter);
        setdata();

    }
    private void setdata()
    {
        jzVideoPlayerStandard.setUp("http://viraltube.co.in/viral/Viraltube_Video.mp4"
                , JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        // jzVideoPlayerStandard.thumbImageView.setImageResource(R.drawable.age);
        // Glide.with(context).load("http://viraltube.co.in/viral/kt_talent.jpeg").into(jzVideoPlayerStandard);
        Glide.with(this)
                .load("http://viraltube.co.in/viral/kt_talent.jpeg")
                .into(jzVideoPlayerStandard.thumbImageView);
        if(loadingDialog.isShowing())
        {
            loadingDialog.dismiss();
        }

    }
}
