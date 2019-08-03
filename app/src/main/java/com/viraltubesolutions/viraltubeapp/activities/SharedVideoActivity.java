package com.viraltubesolutions.viraltubeapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.adapters.SharedVideoAdapter;

import dmax.dialog.SpotsDialog;


public class SharedVideoActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    SpotsDialog loadingDialog;
    String myURL,userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);
        initializeViews();
    }
    private void initializeViews()
    {
        SharedPreferences responsePreference=getSharedPreferences("RESPONSEDATA",MODE_PRIVATE);
        myURL=responsePreference.getString("RESPONSEURL",null).trim();

        SharedPreferences loginPreference=getSharedPreferences("LogIn",MODE_PRIVATE);
        userID=loginPreference.getString("userID",null);

        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerview_sharedvideo);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        loadingDialog=new SpotsDialog(this,"Loading your video please wait...");
        loadingDialog.show();

        splitURL(myURL);
    }
    private void splitURL(String data)
    {
        String b=data.substring(1,data.length()-1);
        String[] z=b.split(",");
        Log.d("notify",b);
        for(int i=0;i<z.length;i++)
        {
            int len=z[i].indexOf(":");
            z[i]=z[i].substring(len+1);
            z[i] = z[i].replaceAll("\"", "");
            Log.d("finalString",z[i]+" ");
            setdata(z);
        }
    }
    private void setdata(String[] z)
    {
        SharedVideoAdapter adapter=new SharedVideoAdapter(SharedVideoActivity.this,userID,z,loadingDialog);
        mRecyclerView.setAdapter(adapter);

    }
}
