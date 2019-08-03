package com.viraltubesolutions.viraltubeapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomTextView;

public class TermsnConditions extends AppCompatActivity {

    Toolbar toolbar;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsn_conditions);
        //toolbar.setNavigationIcon(R.drawable.jz_backward_icon);
        toolbar= (Toolbar) findViewById(R.id.toolbar_termsnconditions);
        toolbar.setNavigationIcon(R.drawable.custom_back_icon);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent backintent=new Intent(AboutUsActivity.this,HomePageActivity.class);
                //startActivity(backintent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
