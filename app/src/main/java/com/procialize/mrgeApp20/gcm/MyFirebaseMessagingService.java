package com.procialize.mrgeApp20.gcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.procialize.mrgeApp20.Activity.SplashActivity;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;


/**
 * Created by Rajesh on 22-02-2018.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    //private NotificationManager notificationManager1;
    SessionManager session;
    int notificationId;
    String ADMIN_CHANNEL_ID = "001";
    NotificationManager notificationManager;
    HashMap<String, String> user;
    String exhibitor_status, exhibitor_id;
    String MY_PREFS_NAME = "ProcializeInfo";
    String event_id;
    Bitmap bitmap;
    int notificationCount = 0;

    public static String getEmojiFromString(String emojiString) {

        if (!emojiString.contains("\\u")) {

            return emojiString;
        }
        String EmojiEncodedString = "";

        int position = emojiString.indexOf("\\u");

        while (position != -1) {

            if (position != 0) {
                EmojiEncodedString += emojiString.substring(0, position);
            }

            String token = emojiString.substring(position + 2, position + 6);
            emojiString = emojiString.substring(position + 6);
            EmojiEncodedString += (char) Integer.parseInt(token, 16);
            position = emojiString.indexOf("\\u");
        }
        EmojiEncodedString += emojiString;

        return EmojiEncodedString;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

//        Bundle[{google.delivered_priority=normal, google.sent_time=1583143282324, google.ttl=2419200, google.original_priority=normal, contentText=context, from=321746269511, image=, title=For Developer Use, event_id=43, google.message_id=0:1583143283357256%82707a1df9fd7ecd, contentTitle=title, message=Admin has sent you a notification - "TEST", google.c.sender.id=321746269511, tickerText=ticker}]

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        event_id = prefs.getString("eventid", "1");

        if (remoteMessage.getData().get("event_id").equalsIgnoreCase(event_id)) {

            notificationCount++;
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupChannels();
            }
            notificationId = new Random().nextInt(60000);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            String imageUri = remoteMessage.getData().get("image");
            long when = System.currentTimeMillis();
            bitmap = getBitmapfromUrl(imageUri);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID);
//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //transperent icon
                if(Build.BRAND.equalsIgnoreCase("samsung")) {
                    notificationBuilder.setSmallIcon(R.drawable.app_icon);
                }else {
                    notificationBuilder.setSmallIcon(R.drawable.trans_logo);
                    notificationBuilder.setColor(getResources().getColor(R.color.colorwhite));
                }
            } else {
                notificationBuilder.setSmallIcon(R.drawable.app_icon);
//            notificationBuilder.setColor(getResources().getColor(R.color.activetab));
            }
            //-----------For notification count----------------------
            SharedPreferences prefs2 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String strNotificationCount = prefs2.getString("notificationCount","");

            if(strNotificationCount.isEmpty())
            {
                strNotificationCount = "0";
            }

            notificationCount = Integer.parseInt(strNotificationCount);

            notificationCount = notificationCount+1;

            SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs1.edit();
            editor.putString("notificationCount", String.valueOf(notificationCount));
            editor.commit();

            Intent broadcastIntent = new Intent(ApiConstant.BROADCAST_ACTION_FOR_NOTIFICATION_COUNT);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
            //----------------------------------------------------------------------


            notificationBuilder.setContentTitle("Mrge App")
                    .setColorized(true)
                    .setWhen(when)
                    .setAutoCancel(true)
                    .setShowWhen(true)
                    .setSound(alarmSound)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(getEmojiFromString(remoteMessage.getData().get("message"))))
                    .setContentText(getEmojiFromString(remoteMessage.getData().get("message")))
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap).bigLargeIcon(null));


            // Session Manager
            session = new SessionManager(getApplicationContext());
            if (session.isLoggedIn()) {
                bitmap = getBitmapfromUrl(imageUri);
                sendNotification(remoteMessage.getData().get("message"), bitmap);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    // Put the message into a notification and post it.
    private void sendNotification(String messageBody, Bitmap image) {

       /* Intent notificationIntent = new Intent(getApplicationContext(),
                NotificationActivity.class);*/
        Intent notificationIntent = new Intent(getApplicationContext(),
                SplashActivity.class);
        notificationIntent.putExtra("fromNotification", "fromNotification");
        notificationIntent.putExtra("type", "");
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(), new Random().nextInt(),
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long when = System.currentTimeMillis();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //transperent icon
            if(Build.BRAND.equalsIgnoreCase("samsung")) {
                mBuilder.setSmallIcon(R.drawable.app_icon);
            }else {
                mBuilder.setSmallIcon(R.drawable.trans_logo);
                mBuilder.setColor(getResources().getColor(R.color.colorwhite));
            }
        } else {
            mBuilder.setSmallIcon(R.drawable.app_icon);
//            mBuilder.setColor(getResources().getColor(R.color.activetab));
        }

//        mBuilder.setSmallIcon(R.drawable.app_icon);
//        mBuilder.setSmallIcon(getNotificationIcon())
        mBuilder.setContentTitle("The Event App")
                .setLargeIcon(image)
                .setColorized(true)
                .setSound(alarmSound)
                .setWhen(when)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getEmojiFromString(messageBody)))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image).bigLargeIcon(null))
                .setContentText(getEmojiFromString(messageBody));
        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);

        if (messageBody != null) {

            notificationManager.notify(notificationId, mBuilder.build());

            Intent countIntent = new Intent("myBroadcastIntent");
            countIntent.putExtra("countBroadCast", "countBroadCast");
            LocalBroadcastManager.getInstance(this).sendBroadcast(countIntent);
        }
    }
    private int getNotificationIcon() {
        boolean selectIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return selectIcon ? R.drawable.app_icon : R.drawable.app_icon;
    }

    // Put the message into a notification and post it.
    private void sendPollNotification(String msg) {

        System.out.print("Inside Gcm Service Service Service");

        //notificationManager = (NotificationManager) this
        //    .getSystemService(Context.NOTIFICATION_SERVICE);

        System.out.print("Inside Gcm Service");

        String url = msg.substring(msg.lastIndexOf("#") + 1);

        String finalTempMsg = msg.substring(msg.lastIndexOf("^") + 1);

        String[] parts = finalTempMsg.split("\\#");

        String finalMsg = "Please poll for " + parts[0];

        // Opens Notification Activty
       /* Intent notificationIntent = new Intent(getApplicationContext(),
                LivePollActivity.class);*/
        Intent notificationIntent = new Intent(getApplicationContext(),
                SplashActivity.class);
        notificationIntent.putExtra("pollUrl", url);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(), new Random().nextInt(),
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mBuilder = new NotificationCompat.Builder(
                    this)

                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("The Event App")
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(finalMsg))
                    .setContentText(finalMsg).setSound(alarmSound);


            mBuilder.setContentIntent(contentIntent);
            mBuilder.setAutoCancel(true);
        } else {
            mBuilder = new NotificationCompat.Builder(
                    this)

                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("The Event App")
                    .setColor(getResources().getColor(R.color.activetab))

                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(finalMsg))
                    .setContentText(finalMsg).setSound(alarmSound);


            mBuilder.setContentIntent(contentIntent);
            mBuilder.setAutoCancel(true);
        }


        // contentIntent.writePendingIntentOrNullToParcel(sender, out)
        if (msg != null) {

            notificationManager.notify(notificationId, mBuilder.build());

            Intent countIntent = new Intent("myBroadcastIntent");
            countIntent.putExtra("countBroadCast", "countBroadCast");
            LocalBroadcastManager.getInstance(this).sendBroadcast(countIntent);
        }

    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

}