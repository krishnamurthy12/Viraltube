package com.viraltubesolutions.viraltubeapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.viraltubesolutions.viraltubeapp.R;

import dmax.dialog.SpotsDialog;


public class SharedActivity extends AppCompatActivity {
RecyclerView mRecyclerView;
LinearLayoutManager mLayoutManager;
SpotsDialog loadingDialog;
String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);
        initializeViews();
    }
    private void initializeViews()
    {
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerview_sharedvideo);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences preferences =getSharedPreferences("LogIn", MODE_PRIVATE);
        userID=preferences.getString("userID",null);

        loadingDialog=new SpotsDialog(this,"Loading your video please wait...");
        loadingDialog.show();

    }
}
