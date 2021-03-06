package com.procialize.frontier.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.InnerDrawerActivity.NotificationActivity;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.Util;
import com.procialize.frontier.util.GetUserActivityReport;

import java.io.File;
import java.util.HashMap;

import static com.procialize.frontier.Utility.Util.setNotification;
import static com.procialize.frontier.util.CommonFunction.crashlytics;
import static com.procialize.frontier.util.CommonFunction.firbaseAnalytics;

public class EULAActivity extends AppCompatActivity {
    ImageView headerlogoIv;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    SessionManager sessionManager;
            String api_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eula);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.btn_back), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();


        api_token = user.get(SessionManager.KEY_TOKEN);


        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        TextView headertxt = findViewById(R.id.headertxt);
        headertxt.setTextColor(Color.parseColor(colorActive));
//        LinearLayout linear = findViewById(R.id.linear);

//        try {
//
//            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/"+ApiConstant.folderName+"/"+ "background.jpg");
//            Resources res = getResources();
//            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
//            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
//            linear.setBackgroundDrawable(bd);
//
//            Log.e("PATH", String.valueOf(mypath));
//        } catch (Exception e) {
//            e.printStackTrace();
//            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
//
//        }

        ImageView notificationlogoIv = findViewById(R.id.notificationlogoIv);
        notificationlogoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(main);
                finish();
            }
        });

        WebView mywebview = findViewById(R.id.webView);

        mywebview.loadUrl(ApiConstant.imgURL +"eula.html");

        crashlytics("EULA", "");
        firbaseAnalytics(this, "EULA", "");

        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }


        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this, api_token,
                eventid,
                ApiConstant.pageVisited,
                "45",
                "");
        getUserActivityReport.userActivityReport();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
