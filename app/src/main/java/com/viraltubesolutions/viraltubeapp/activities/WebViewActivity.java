package com.viraltubesolutions.viraltubeapp.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.viraltubesolutions.viraltubeapp.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    String aboutUs,terms;

    @SuppressLint("SetJavaScriptEnabled")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            aboutUs = bundle.getString("aboutusurl");
            terms=bundle.getString("terms");
        }

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        if(aboutUs!=null) {
            webView.loadUrl(aboutUs);
            aboutUs=null;
        }
        else if(terms!=null) {
            webView.loadUrl(terms);
            terms=null;
        }
    }

}
