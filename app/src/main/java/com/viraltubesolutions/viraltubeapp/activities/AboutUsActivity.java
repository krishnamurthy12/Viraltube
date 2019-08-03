package com.viraltubesolutions.viraltubeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.customs.MyCustomTextView;

public class AboutUsActivity extends AppCompatActivity {
     Toolbar toolbar;
    MyCustomTextView termsNConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        toolbar= (Toolbar) findViewById(R.id.toolbar_aboutus);
        toolbar.setNavigationIcon(R.drawable.custom_back_icon);
        termsNConditions=(MyCustomTextView)findViewById(R.id.termsandconditions_text);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //ntent backintent=new Intent(AboutUsActivity.this,HomePageActivity.class);
                //startActivity(backintent);
            }
        });

        termsNConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //define on click
                Intent terms=new Intent(AboutUsActivity.this,TermsnConditions.class);
                startActivity(terms);
                overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_in_from_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
