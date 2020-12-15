package com.procialize.frontier.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.CustomTools.MyJzvdStd;
import com.procialize.frontier.GetterSetter.FrontierTV;
import com.procialize.frontier.GetterSetter.FrontierTVFetch;
import com.procialize.frontier.GetterSetter.frontierTVRegistration;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.Util;

import java.util.HashMap;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.frontier.util.CommonFunction.crashlytics;
import static com.procialize.frontier.util.CommonFunction.firbaseAnalytics;

public class FrontierTVDetail extends AppCompatActivity {

    TextView description_txt, registration, header;
    ImageView headerlogoIv;
    Button btn_registration;
    public MyJzvdStd videoview;
    String desc, title, url, registration_enable,id, thumbimg, colorActive, eventid;
    ProgressBar img_progress;
    APIService mAPIService;
    ProgressBar progrssbar;
    String MY_PREFS_NAME = "ProcializeInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontier_t_v_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                JzvdStd.releaseAllVideos();
                Intent intent=new Intent(FrontierTVDetail.this,FrontierSpecial.class);
                startActivity(intent);
                finish();
            }
        });

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("FrontierSpecialDetail", token);
        firbaseAnalytics(this, "FrontierSpecialDetail", token);

        mAPIService = ApiUtils.getAPIService();
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        description_txt = findViewById(R.id.description_txt);
        registration = findViewById(R.id.registration);
        header = findViewById(R.id.header);
        videoview = findViewById(R.id.videoview);
        img_progress = findViewById(R.id.img_progress);
        progrssbar = findViewById(R.id.progrssbar);
        btn_registration = findViewById(R.id.btn_registration);

        Intent intent = getIntent();
        desc = intent.getStringExtra("desc");
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");
        thumbimg = intent.getStringExtra("thumbimg");
        id = intent.getStringExtra("id");
        registration_enable = intent.getStringExtra("registration_enable");

        if(registration_enable.equalsIgnoreCase("0")){
            btn_registration.setVisibility(View.GONE);
            registration.setVisibility(View.VISIBLE);
        }else if(registration_enable.equalsIgnoreCase("1")){
            btn_registration.setVisibility(View.VISIBLE);
            registration.setVisibility(View.GONE);
        }

        videoview.setUp(url, ""
                , JzvdStd.SCREEN_NORMAL);
        MyJzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);

        ViewTarget<ImageView, Drawable> into = Glide.with(videoview)
                .load(thumbimg)
                .placeholder(R.drawable.gallery_placeholder)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        img_progress.setVisibility(View.GONE);
                        return false;

                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        img_progress.setVisibility(View.GONE);
                        return false;
                    }
                }).into(videoview.thumbImageView);

        header.setText(title);
        description_txt.setText(desc);

        btn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrontierTVRegistration(token, eventid, id);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        JzvdStd.releaseAllVideos();
        Intent intent=new Intent(FrontierTVDetail.this,FrontierSpecial.class);
        startActivity(intent);
        finish();

    }

    public void FrontierTVRegistration(String token, String eventid, String id) {
        showProgress();
        mAPIService.FrontierTVRegistration(token, eventid, id).enqueue(new Callback<frontierTVRegistration>() {
            @Override
            public void onResponse(Call<frontierTVRegistration> call, Response<frontierTVRegistration> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    btn_registration.setVisibility(View.GONE);
                    registration.setVisibility(View.VISIBLE);

                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<frontierTVRegistration> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();


            }
        });
    }

    public void showProgress() {
        if (progrssbar.getVisibility() == View.GONE) {
            progrssbar.setVisibility(View.VISIBLE);
        }
    }

    public void dismissProgress() {
        if (progrssbar.getVisibility() == View.VISIBLE) {
            progrssbar.setVisibility(View.GONE);
        }
    }


}