package com.viraltubesolutions.viraltubeapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.activities.SharedVideoActivity;

/*
    Created by Shashi on 11/16/2017.
*/

    public class MyFirebaseMessagingService  extends FirebaseMessagingService {
        private static final String TAG = "MyFirebaseMsgService";
        public static final int NOTIFICATION_ID = 1;
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            //Displaying data in log
            //It is optional
            //   Log.d("MyFirebaseMsgService", "From: " + remoteMessage.getFrom());
//        Log.d("MyFirebaseMsgService", "Notification Message Body: " + remoteMessage.getNotification().getBody());
            //Calling method to generate notification
            // sendNotification(remoteMessage.getNotification().getBody()+"");
//        String title = remoteMessage.getNotification().getTitle();
            //  String message = remoteMessage.getNotification().getBody();
       /* if(message!=null){
            sendNotification(message);
        }*/
      /*  if(remoteMessage.getData().get("message")!=null){
            sendNotification(remoteMessage.getData().get("message"),remoteMessage.getData().get("title"));
        }

*/
            /*SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Gson gson = new Gson();
            String json = sharedPrefs.getString("Data", null);
            Type type = new TypeToken<ArrayList<ShareResponse>>() {}.getType();
            ArrayList<ShareResponse> arrayList = gson.fromJson(json, type);
            sendNotification(remoteMessage.getData().get("message"),"VT");
            remoteMessage.getData().get("url").toString();

            */
            SharedPreferences responsePreference=getSharedPreferences("RESPONSEDATA",MODE_PRIVATE);
            SharedPreferences.Editor editor=responsePreference.edit();
            editor.putString("RESPONSEURL", remoteMessage.getData().get("url"));
            editor.apply();

            Log.d("message","Message"+ remoteMessage.getData().get("message"));
            sendNotification(remoteMessage.getData().get("message"),"VT");
        }
        private void sendNotification(String messageBody,String title) {
            if(title==null){
                title="ViralTube";
            }
            Intent intent = new Intent(this, SharedVideoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
      /*  NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.onmoney_transparent);
            builder.setColor(this.getResources().getColor(R.color.app_bg));
        } else {
            builder.setSmallIcon(R.mipmap.app_icon);
        }
        Notification myNotification=   builder.setContentTitle(messageBody)
                .setContentText(messageBody)
                .setTicker("OnMoney")
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .build();
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, myNotification);*/


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.icon_web);
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_web));
                builder.setColor(this.getResources().getColor(R.color.colorPrimary));
            } else {
                builder.setSmallIcon(R.mipmap.icon_web);

            }
           /* Notification myNotification=   builder.setContentTitle(messageBody)
                    .setContentText("Shared video")
                    .setTicker(title)
                    .setContentTitle(title)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
                    .build();*/
            Notification myNotification=   builder.setContentTitle(messageBody)
                    .setContentText(messageBody)
                    .setTicker(title)
                    .setContentTitle(title)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
                    .build();
            NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION_ID, myNotification);
        /*NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Ebud")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());*/
        }
    }