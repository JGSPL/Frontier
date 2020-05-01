/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.procialize.mrgeApp20.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.procialize.mrgeApp20.Activity.SplashActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import java.util.Random;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    public static final String TAG = "GCM Demo";
    NotificationCompat.Builder builder;
    String msg;
    SessionManager session;
    private NotificationManager mNotificationManager;

    public GcmIntentService() {
        super("GcmIntentService");
    }

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
    protected void onHandleIntent(Intent intent) {
        System.out.println("Inside OnHandle Intent");

        // sendNotification("Received: ");

        Bundle extras = intent.getExtras();
        extras.toString();
        msg = intent.getStringExtra("message");
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        // GCM Message TYpe
        // String messageType = gcm.getMessageType(intent)0;

        String messageType = "gcm";

        if (!extras.isEmpty()) { // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that
             * GCM will be extended in the future with new message types, just
             * ignore any message types you're not interested in, or that you
             * don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i = 0; i < 5; i++) {
                    Log.i(TAG,
                            "Working... " + (i + 1) + "/5 @ "
                                    + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }

                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());

                // Session Manager
                session = new SessionManager(getApplicationContext());
                if (session.isLoggedIn()) {

                    // Post notification of received message.
                    String tempString = msg.substring(0, 4);

                    Toast.makeText(getBaseContext(), tempString,
                            Toast.LENGTH_SHORT).show();

                    System.out.println(tempString);

                    if (tempString.equalsIgnoreCase("poll")) {
                        sendPollNotification(msg);
                    } else {

                        sendOrgNotification(msg);

                    }

                }
                Log.i(TAG, "Received: " + extras.toString());
            }
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    private void sendOrgNotification(String msg) {

        System.out.print("Inside Gcm Service Service Service");

        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        System.out.print("Inside Gcm Service");

        String tempString = msg.substring(0, 3);

        Toast.makeText(getBaseContext(), tempString, Toast.LENGTH_SHORT).show();

        System.out.println(tempString);

        if (tempString.equalsIgnoreCase("poll")) {

        }

        // Opens Notification Activty
       /* Intent notificationIntent = new Intent(getApplicationContext(),
                NotificationActivity.class);
        notificationIntent.putExtra("fromNotification", "fromNotification");
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(), new Random().nextInt(),
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                .setSmallIcon(getNotificationIcon())
                .setContentTitle("Continuum")
                // .setColor(Color.parseColor("#ffff00"))
                .setColorized(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getEmojiFromString(msg)))
                .setContentText(getEmojiFromString(msg)).setSound(alarmSound);

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);

        // contentIntent.writePendingIntentOrNullToParcel(sender, out)
        if (msg != null) {

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

            Intent countIntent = new Intent("myBroadcastIntent");
            countIntent.putExtra("countBroadCast", "countBroadCast");
            LocalBroadcastManager.getInstance(this).sendBroadcast(countIntent);
        }*/

    }

    private int getNotificationIcon() {
        boolean selectIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return selectIcon ? R.drawable.app_icon : R.drawable.app_icon;
    }

    // Put the message into a notification and post it.
    private void sendPollNotification(String msg) {

        System.out.print("Inside Gcm Service Service Service");

        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

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
                    .setContentTitle("Continuum")
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(finalMsg))
                    .setContentText(finalMsg).setSound(alarmSound);


            mBuilder.setContentIntent(contentIntent);
            mBuilder.setAutoCancel(true);
        } else {
            mBuilder = new NotificationCompat.Builder(
                    this)

                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("Continuum")
                    .setColor(getResources().getColor(R.color.colorPrimary))

                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(finalMsg))
                    .setContentText(finalMsg).setSound(alarmSound);


            mBuilder.setContentIntent(contentIntent);
            mBuilder.setAutoCancel(true);
        }


        // contentIntent.writePendingIntentOrNullToParcel(sender, out)
        if (msg != null) {

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

            Intent countIntent = new Intent("myBroadcastIntent");
            countIntent.putExtra("countBroadCast", "countBroadCast");
            LocalBroadcastManager.getInstance(this).sendBroadcast(countIntent);
        }

    }
}
