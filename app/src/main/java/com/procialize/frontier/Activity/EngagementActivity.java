package com.procialize.frontier.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.procialize.frontier.Engagement.Activity.SelfieContestActivity;
import com.procialize.frontier.Engagement.Activity.VideoContestActivity;
import com.procialize.frontier.R;
import com.procialize.frontier.Utility.Util;

public class EngagementActivity extends AppCompatActivity {

    CardView videocard_view, selfiecard_view;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engagement2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.btn_back), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
//                Intent intent = new Intent(SelfieContestActivity.this, EngagementActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        selfiecard_view = findViewById(R.id.selfiecard_view);
        videocard_view = findViewById(R.id.videocard_view);

        videocard_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selfie = new Intent(EngagementActivity.this, VideoContestActivity.class);
                selfie.putExtra("header", "selfieTitle");
                startActivity(selfie);
                finish();
            }
        });

        selfiecard_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selfie = new Intent(EngagementActivity.this, SelfieContestActivity.class);
                selfie.putExtra("header", "selfieTitle");
                startActivity(selfie);
                finish();
            }
        });
    }
}