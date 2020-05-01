package com.procialize.mrgeApp20.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Utility.Util;

import java.io.File;

public class InitGeneralInfoActivity extends AppCompatActivity {
    String name, description;
    TextView txt_title, text_maininfo;
    ImageView headerlogoIv;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    WebView mywebview;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        colorActive = prefs.getString("colorActive", "");

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        description = intent.getStringExtra("description");

//        if (name.equalsIgnoreCase("about location")) {
//            setContentView(R.layout.about_city);
//
//            TextView attendeetitle = findViewById(R.id.attendeetitle);
//            attendeetitle.setTextColor(Color.parseColor(colorActive));
//
//            RelativeLayout layoutTop = findViewById(R.id.layoutTop);
//            layoutTop.setBackgroundColor(Color.parseColor(colorActive));
//
//            TextView tvname = findViewById(R.id.tvname);
//            tvname.setTextColor(Color.parseColor("#FFFFFF"));
//
//
//        } else if (name.equalsIgnoreCase("about hotel")) {
//            setContentView(R.layout.about_hotel);
//
//            TextView attendeetitle = findViewById(R.id.attendeetitle);
//            attendeetitle.setTextColor(Color.parseColor(colorActive));
//
//            RelativeLayout layoutTop = findViewById(R.id.layoutTop);
//            layoutTop.setBackgroundColor(Color.parseColor(colorActive));
//
//            TextView tvname = findViewById(R.id.tvname);
//            tvname.setTextColor(Color.parseColor("#FFFFFF"));
//
//
//        } else {
        setContentView(R.layout.activity_init_general_info);
        mywebview = findViewById(R.id.webView);


        WebSettings settings = mywebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        mywebview.clearCache(true);
        mywebview.loadData(description, "text/html", "UTF-8");

        TextView txt_title = findViewById(R.id.txt_title);
        txt_title.setTextColor(Color.parseColor(colorActive));
//        }

//        txt_title = (TextView) findViewById(R.id.txt_title);
//        text_maininfo = (TextView) findViewById(R.id.text_maininfo);
        //
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        linear = findViewById(R.id.linear);
        Util.logomethod(this, headerlogoIv);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

        txt_title.setText(name);
//        txt_title.setAllCaps(true);
//        text_maininfo.setText(description);


    }
}
