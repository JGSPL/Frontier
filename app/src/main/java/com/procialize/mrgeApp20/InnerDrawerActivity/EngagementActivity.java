package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;

import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class EngagementActivity extends AppCompatActivity {

    CardView selfiecard_view, videocard_view;
    HashMap<String, String> user;
    String engagement_selfie_contest = "0", engagement_video_contest = "0";
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    private List<EventSettingList> eventSettingLists;
    LinearLayout linear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engagement);
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent selfie = new Intent(EngagementActivity.this, HomeActivity.class);
//                startActivity(selfie);
                finish();
            }
        });
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        selfiecard_view = findViewById(R.id.selfiecard_view);
        videocard_view = findViewById(R.id.videocard_view);
        linear = findViewById(R.id.linear);


        TextView header = findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));
        TextView selfieTv = findViewById(R.id.selfieTv);
        selfieTv.setBackgroundColor(Color.parseColor(colorActive));
        TextView videoTv = findViewById(R.id.videoTv);
        videoTv.setBackgroundColor(Color.parseColor(colorActive));

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }
        SessionManager sessionManager = new SessionManager(this);

        user = sessionManager.getUserDetails();


        crashlytics("Engagement",user.get(SessionManager.KEY_TOKEN));
        firbaseAnalytics(this, "Engagement",user.get(SessionManager.KEY_TOKEN));
        eventSettingLists = SessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }


        if (engagement_selfie_contest.equalsIgnoreCase("0")) {
            selfiecard_view.setVisibility(View.GONE);
        }

        if (engagement_video_contest.equalsIgnoreCase("0")) {
            videocard_view.setVisibility(View.GONE);
        }

        selfiecard_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent selfie = new Intent(EngagementActivity.this, SelfieContestActivity.class);
                startActivity(selfie);
//                finish();
            }
        });

        videocard_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent videocontest = new Intent(EngagementActivity.this, VideoContestActivity.class);
                startActivity(videocontest);
//                finish();
            }
        });
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("engagement_selfie_contest")) {
                engagement_selfie_contest = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("engagement_video_contest")) {
                engagement_video_contest = eventSettingLists.get(i).getFieldValue();
            }

        }
    }

    @Override
    protected void onResume() {
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }

}
