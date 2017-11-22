package com.viraltubesolutions.viraltubeapp.app_controller;


import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.viraltubesolutions.viraltubeapp.utils.CrashActivity;

import java.io.PrintWriter;
import java.io.StringWriter;


/* Created by Shiva on 6/7/2016.*/


public class AppController extends MultiDexApplication {

    public static final String TAG = AppController.class.getSimpleName();
    // public static final String ROOT_URL = "http://www.viraltube.co.in/moneytree";
// public static final String ROOT_URL = "http://viraltube.co.in/moneytree-test";
    //public static final String ROOT_URL = "http://m2jservices.com/zb/moneytree-test";

    public static final String ROOT_URL = "http://viraltube.co.in/viral";
    protected String userAgent;
    private static AppController mInstance;

    private static AppController enableMultiDex;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        //registerActivityLifecycleCallbacks(this);

        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                // handleUncaughtException (thread, e);
                e.printStackTrace();
                Log.e("uncaught", "one" + e.getMessage());
               System.exit(1);


                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));




                sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));




                Intent in = getPackageManager().getLaunchIntentForPackage("com.viraltubesolutions.viraltubeapp");
                in.setClass(getApplicationContext(), CrashActivity.class);
                in.putExtra("errlog", sw.toString());
                startActivity(in);
                System.exit(1);

            }
        });

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public AppController(){
        enableMultiDex=this;
    }

    public static AppController getEnableMultiDexApp() {
        return enableMultiDex;
    }




}
