package com.viraltubesolutions.viraltubeapp.utils;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.viraltubesolutions.viraltubeapp.BuildConfig;
import com.viraltubesolutions.viraltubeapp.R;

import java.util.List;


public class CrashActivity extends AppCompatActivity {


    TextView vT_aca_crashView;

    Button vB_aca_sendEmail;
    String errlog,versionName;
    int versionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;

        vT_aca_crashView= (TextView) findViewById(R.id.vT_aca_crashView);
        vB_aca_sendEmail= (Button) findViewById(R.id.vB_aca_sendEmail);
        vB_aca_sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new SendEmailTask().execute();
                sendMail();
            }
        });

        Intent in = getIntent();
        Bundle bnd = in.getExtras();
        errlog = bnd.getString("errlog");

        vT_aca_crashView.setText("The cause for the crash is\n\n"+errlog);
    }


    public void sendMail()
    {
       /* Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.setData(Uri.parse("rudreshjr.jlr@gmail.com"));
        sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"rudreshjr.jlr@gmail.com" });
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Crash in OnMoney");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "The cause for the crash is\n\n"+errlog);
        startActivity(sendIntent);
        finish();*/


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/html");
        final PackageManager pm = this.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        String className = null;
        for (final ResolveInfo info : matches) {
            if (info.activityInfo.packageName.equals("com.google.android.gm")) {
                className = info.activityInfo.name;

                if(className != null && !className.isEmpty()){
                    break;
                }
            }
        }
        emailIntent.setClassName("com.google.android.gm", className);
        emailIntent.setData(Uri.parse("rudreshjr62@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"rudreshjr62@gmail.com" });
        //  emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Crash in ViralTube");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Crash in ViralTube..."+versionName());
        emailIntent.putExtra(Intent.EXTRA_TEXT, "The cause for the crash in ViralTube \n versionName"+versionName+"--versionCode "+versionCode+"\n\n"+errlog);
        startActivity(emailIntent);
        finish();
        //System.exit(1);
    }


    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
    public String versionName() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert pInfo != null;
        String version = pInfo.versionName;
        int verCode = pInfo.versionCode;


        return getDeviceName() + " v:"
                + String.valueOf(Build.VERSION.SDK_INT+" & apk level-"+version);
    }

}
