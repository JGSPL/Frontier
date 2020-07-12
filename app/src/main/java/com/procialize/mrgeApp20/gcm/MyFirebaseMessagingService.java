package com.procialize.mrgeApp20.gcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.procialize.mrgeApp20.Activity.SplashActivity;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.AttendeeChat.AttendeeChatActivity;
import com.procialize.mrgeApp20.BuddyList.Activity.ActivityBuddyChat;
import com.procialize.mrgeApp20.BuddyList.Activity.ActivityBuddyList;
import com.procialize.mrgeApp20.BuddyList.DataModel.Buddy;
import com.procialize.mrgeApp20.BuddyList.DataModel.chat_list_db;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.InnerDrawerActivity.NotificationActivity;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
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
    String strEventId = "";
    PendingIntent contentIntent;
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

        try {
            strEventId = remoteMessage.getData().get("event_id");
        }catch (Exception e)
        {}
        //  Log.d("Event Id==>", remoteMessage.getData().get("event_id"));
        if (strEventId.equalsIgnoreCase(event_id)) {

            notificationCount++;
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupChannels();
            }
            notificationId = new Random().nextInt(60000);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            try {
                String msgTye = remoteMessage.getData().get("type");
                Log.d("notification_type", msgTye);

                if (msgTye.equalsIgnoreCase("spot_poll")) {


                    // if(!MyApplication.isAppDestoryed) {
                    /*Intent notifyIntent = new Intent(this, MrgeHomeActivity.class);
                    notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    notifyIntent.putExtra("spot_poll","spot_poll");
                    startActivity(notifyIntent);*/
                    MrgeHomeActivity.spot_poll = "spot_poll";


                    Intent broadcastIntent = new Intent(ApiConstant.BROADCAST_ACTION_FOR_SPOT_LIVE_POLL);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    //}
                }

                if (msgTye.equalsIgnoreCase("spot_quiz")) {


                    MrgeHomeActivity.spot_quiz = "spot_quiz";


                    Intent broadcastIntent = new Intent(ApiConstant.BROADCAST_ACTION_FOR_SPOT_Quiz);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    //}
                }
                if (msgTye.contains("eventchat_")) {

                    String[] message = remoteMessage.getData().get("message").split("-");
                    AttendeeChatActivity.SpotEventChat = "Eventchat";
                    AttendeeChatActivity.chat_id = msgTye;
                    AttendeeChatActivity.attendee_chat_message = message[1];
                    if (msgTye.contains("eventchat_")) {
                        msgTye = msgTye.replace("eventchat_", "");
                    }



                    chat_list_db UsersList = new chat_list_db();
                    UsersList.setIs_read("0");
                    UsersList.setId("");
                    UsersList.setSender_id(msgTye);
                    UsersList.setReceiver_id("");
                    UsersList.setMessage(message[1]);
                    UsersList.setTimestamp("");
                    UsersList.setStatus("");
                    DBHelper procializeDB = new DBHelper(this);
                    SQLiteDatabase db = procializeDB.getWritableDatabase();
                    procializeDB.insertAttendeeChat(UsersList, db);

                    Intent broadcastIntent = new Intent(ApiConstant.BROADCAST_ACTION_FOR_SPOT_ChatBuddy);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    //}
                }
                if (msgTye.equalsIgnoreCase("Post")) {
                    Intent broadcastIntent = new Intent(ApiConstant.BROADCAST_ACTION_FOR_POST_NEWS_FEED);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                }

                if (msgTye.contains("chat_")) {

                    String[] message = remoteMessage.getData().get("message").split("-");
                    ActivityBuddyChat.SpotChat = "chat";
                    ActivityBuddyChat.chat_id = msgTye;
                    ActivityBuddyChat.chat_message = message[1];

                    if (msgTye.contains("chat_")) {
                        msgTye = msgTye.replace("chat_", "");
                    }


                    chat_list_db UsersList = new chat_list_db();
                    UsersList.setIs_read("0");
                    UsersList.setId("");
                    UsersList.setSender_id(msgTye);
                    UsersList.setReceiver_id("");
                    UsersList.setMessage(message[1]);
                    UsersList.setTimestamp("");
                    UsersList.setStatus("");
                    DBHelper procializeDB = new DBHelper(this);
                    SQLiteDatabase db = procializeDB.getWritableDatabase();
                    procializeDB.insertBuddyChatCount(UsersList, db);
                    Intent broadcastIntent = new Intent(ApiConstant.BROADCAST_ACTION_FOR_SPOT_ChatBuddy);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    //}
                }

                if(msgTye.equalsIgnoreCase("androidPost"))
                {
                    com.procialize.mrgeApp20.Background.BackgroundService.successfullUploadedMediaCount =com.procialize.mrgeApp20.Background.BackgroundService.successfullUploadedMediaCount++;
                    String[] message = remoteMessage.getData().get("message").split("#");
                    DBHelper dbHelper = new DBHelper(this);
                    dbHelper.getReadableDatabase();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    String news_feed_id =message[0];
                    String media_file = message[1];
                    String media_file_thumb = message[2];
                    String folderUniqueId = message[3];
                    dbHelper.updateNewsFeedId(news_feed_id, folderUniqueId, db);
                    dbHelper.updateMultimediaInfo(media_file, news_feed_id, db, media_file_thumb, folderUniqueId);
                    Log.e("n_media_file_id===>",news_feed_id);
                    Log.e("n_media_file===>",media_file);
                    Log.e("n_media_file_thumb===>",media_file_thumb);
                    Log.e("n_media_file_UniqueId=>",folderUniqueId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String imageUri = remoteMessage.getData().get("image");
            long when = System.currentTimeMillis();
            bitmap = getBitmapfromUrl(imageUri);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID);
//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //transperent icon
                if (Build.BRAND.equalsIgnoreCase("samsung")) {
                    notificationBuilder.setSmallIcon(R.drawable.app_icon);
                } else {
                    notificationBuilder.setSmallIcon(R.drawable.trans_logo);
                    notificationBuilder.setColor(getResources().getColor(R.color.colorwhite));
                }
            } else {
                notificationBuilder.setSmallIcon(R.drawable.app_icon);
//            notificationBuilder.setColor(getResources().getColor(R.color.activetab));
            }
            //-----------For notification count----------------------
            SharedPreferences prefs2 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String strNotificationCount = prefs2.getString("notificationCount", "");

            if (strNotificationCount.isEmpty()) {
                strNotificationCount = "0";
            }

            notificationCount = Integer.parseInt(strNotificationCount);

            notificationCount = notificationCount + 1;

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
            if(!remoteMessage.getData().get("type").equalsIgnoreCase("androidPost")) {
                if (session.isLoggedIn()) {
                    bitmap = getBitmapfromUrl(imageUri);
                    sendNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("type"), bitmap);
                }
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
    private void sendNotification(String messageBody, String messageType, Bitmap image) {

       /* Intent notificationIntent = new Intent(getApplicationContext(),
                NotificationActivity.class);*/

       if(messageType.contains("eventchat_"))
       {
            String attendeeId = messageType.replace("eventchat_", "");


           DBHelper procializeDB = new DBHelper(this);
           SQLiteDatabase db = procializeDB.getWritableDatabase();
           List<AttendeeList> attendeeInfoList = procializeDB.getAttendeeDetailsFromAttendeeId(attendeeId);

           if(attendeeInfoList.size() > 0) {
               Intent notificationIntent = new Intent(getApplicationContext(),
                       AttendeeChatActivity.class);
               notificationIntent.putExtra("id",  attendeeInfoList.get(0).getAttendeeId());
               notificationIntent.putExtra("name",  attendeeInfoList.get(0).getFirstName()+" "+attendeeInfoList.get(0).getLastName());
               notificationIntent.putExtra("city",  attendeeInfoList.get(0).getCity());
               notificationIntent.putExtra("country",  attendeeInfoList.get(0).getCountry());
               notificationIntent.putExtra("company", attendeeInfoList.get(0).getCompanyName());
               notificationIntent.putExtra("designation",  attendeeInfoList.get(0).getDesignation());
               notificationIntent.putExtra("description",   attendeeInfoList.get(0).getDescription());
               notificationIntent.putExtra("profile",  attendeeInfoList.get(0).getProfilePic());
               notificationIntent.putExtra("mobile",  attendeeInfoList.get(0).getMobile());
               notificationIntent.putExtra("buddy_status",  attendeeInfoList.get(0).getBuddy_status());
               notificationIntent.putExtra("fromPage",  "noti");
               /*notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                       | Intent.FLAG_ACTIVITY_CLEAR_TOP);
               contentIntent = PendingIntent.getActivity(
                       getApplicationContext(), new Random().nextInt(),
                       notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/
               TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stackResultActivity
               stackBuilder.addParentStack(MrgeHomeActivity.class);
// Adds the Intent to the top of the stack
               stackBuilder.addNextIntent(notificationIntent);
// Gets a PendingIntent containing the entire back stack
               contentIntent =
                       stackBuilder.getPendingIntent(new Random().nextInt(), PendingIntent.FLAG_UPDATE_CURRENT);
           }
           else
           {
               Intent notificationIntent = new Intent(getApplicationContext(),
                       SplashActivity.class);
               notificationIntent.putExtra("fromNotification", "fromNotification");
               notificationIntent.putExtra("type", "");
               notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                       | Intent.FLAG_ACTIVITY_CLEAR_TOP);
               contentIntent = PendingIntent.getActivity(
                       getApplicationContext(), new Random().nextInt(),
                       notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
           }
       }
       else if(messageType.contains("chat_"))
       {
           String attendeeId = messageType.replace("chat_", "");


           DBHelper procializeDB = new DBHelper(this);
           SQLiteDatabase db = procializeDB.getWritableDatabase();
           List<Buddy> attendeeInfoList = procializeDB.getBuddyDetailbyId(attendeeId);

           if(attendeeInfoList.size() > 0) {
               Intent notificationIntent = new Intent(getApplicationContext(),
                       ActivityBuddyChat.class);
               notificationIntent.putExtra("id", attendeeInfoList.get(0).getFriend_id());
               notificationIntent.putExtra("name", attendeeInfoList.get(0).getFirstName()+" "+attendeeInfoList.get(0).getLastName());
               notificationIntent.putExtra("city",  attendeeInfoList.get(0).getCity());
               notificationIntent.putExtra("country",  "");
               notificationIntent.putExtra("company",  "");
               notificationIntent.putExtra("designation",  attendeeInfoList.get(0).getDesignation());
               notificationIntent.putExtra("description",  "");
               notificationIntent.putExtra("profile",  attendeeInfoList.get(0).getProfilePic());
               notificationIntent.putExtra("mobile",  "");
               notificationIntent.putExtra("from",  "noti");
               /*notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                       | Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
               TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stackResultActivity
               stackBuilder.addParentStack(MrgeHomeActivity.class);
// Adds the Intent to the top of the stack
               stackBuilder.addNextIntent(notificationIntent);
// Gets a PendingIntent containing the entire back stack
               contentIntent =
                       stackBuilder.getPendingIntent(new Random().nextInt(), PendingIntent.FLAG_UPDATE_CURRENT);

               /*contentIntent = PendingIntent.getActivity(
                       getApplicationContext(), new Random().nextInt(),
                       notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/
           }
           else
           {
               Intent notificationIntent = new Intent(getApplicationContext(),
                       SplashActivity.class);
               notificationIntent.putExtra("fromNotification", "fromNotification");
               notificationIntent.putExtra("type", "");
               notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                       | Intent.FLAG_ACTIVITY_CLEAR_TOP);
               contentIntent = PendingIntent.getActivity(
                       getApplicationContext(), new Random().nextInt(),
                       notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
           }
       }
       else if(messageType.contains("spot_quiz"))
       {
           Intent notificationIntent = new Intent(getApplicationContext(),
                   MrgeHomeActivity.class);
           notificationIntent.putExtra("fromNotification", "fromNotification");
           notificationIntent.putExtra("type", messageType);
           notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                   | Intent.FLAG_ACTIVITY_CLEAR_TOP);
           contentIntent = PendingIntent.getActivity(
                   getApplicationContext(), new Random().nextInt(),
                   notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
       }else if(messageType.equalsIgnoreCase("request")||messageType.equalsIgnoreCase("accept"))
       {
           Intent notificationIntent = new Intent(getApplicationContext(),
                   ActivityBuddyList.class);
           notificationIntent.putExtra("fromNotification", "fromNotification");
           notificationIntent.putExtra("type", messageType);
           notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                   | Intent.FLAG_ACTIVITY_CLEAR_TOP);
           contentIntent = PendingIntent.getActivity(
                   getApplicationContext(), new Random().nextInt(),
                   notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
       } else if(messageType.contains("spot_poll"))
       {
           Intent notificationIntent = new Intent(getApplicationContext(),
                   MrgeHomeActivity.class);
           notificationIntent.putExtra("fromNotification", "fromNotification");
           notificationIntent.putExtra("type", messageType);
           notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                   | Intent.FLAG_ACTIVITY_CLEAR_TOP);
           contentIntent = PendingIntent.getActivity(
                   getApplicationContext(), new Random().nextInt(),
                   notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
       }
       else
        {
           /* if (messageType.equalsIgnoreCase("spot_poll")) {
                MrgeHomeActivity.spot_poll = "spot_poll";
            }

            if (messageType.equalsIgnoreCase("spot_quiz")) {

                MrgeHomeActivity.spot_quiz = "spot_quiz";
            }*/
            Intent notificationIntent = new Intent(getApplicationContext(),
                    NotificationActivity.class);
            notificationIntent.putExtra("fromNotification", "fromNotification");
            notificationIntent.putExtra("type", messageType);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            contentIntent = PendingIntent.getActivity(
                    getApplicationContext(), new Random().nextInt(),
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }


        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long when = System.currentTimeMillis();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //transperent icon
            if (Build.BRAND.equalsIgnoreCase("samsung")) {
                mBuilder.setSmallIcon(R.drawable.app_icon);
            } else {
                mBuilder.setSmallIcon(R.drawable.trans_logo);
                mBuilder.setColor(getResources().getColor(R.color.colorwhite));
            }
        } else {
            mBuilder.setSmallIcon(R.drawable.app_icon);
//            mBuilder.setColor(getResources().getColor(R.color.activetab));
        }

//        mBuilder.setSmallIcon(R.drawable.app_icon);
//        mBuilder.setSmallIcon(getNotificationIcon())

        if(image!=null) {
            mBuilder.setContentTitle("MRGE")
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
        }
        else
        {
            mBuilder.setContentTitle("MRGE")
                    .setLargeIcon(image)
                    .setColorized(true)
                    .setSound(alarmSound)
                    .setWhen(when)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(getEmojiFromString(messageBody)))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentText(getEmojiFromString(messageBody));
        }
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