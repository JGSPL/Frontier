package com.procialize.frontier.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.procialize.frontier.R;
import com.procialize.frontier.Utility.Util;

public class JigsawActivity extends AppCompatActivity {

    CardView puzzleone, puzzletwo_view;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jigsaw);


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
        puzzleone = findViewById(R.id.puzzleone);
        puzzletwo_view = findViewById(R.id.puzzletwo_view);

        puzzleone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videocontest = new Intent(JigsawActivity.this, LeaderboardActivity.class);
                videocontest.putExtra("image_name", "puzzle_1");
                startActivity(videocontest);
            }
        });

        puzzletwo_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videocontest = new Intent(JigsawActivity.this, LeaderboardActivity.class);
                videocontest.putExtra("image_name", "puzzle_2");
                startActivity(videocontest);
            }
        });
    }
}