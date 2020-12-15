package com.procialize.frontier.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.procialize.frontier.R;
import com.procialize.frontier.Utility.Util;

public class VideoViewActivity extends AppCompatActivity {

    String url;
    ProgressBar progressBar;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);


        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("url");
        }

        final VideoView videoView = findViewById(R.id.videoView1);

        Button fullscreenBtn = findViewById(R.id.fullscreenBtn);

        progressBar = findViewById(R.id.progressbar);

        //Creating MediaController
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        //specify the location of media file
        final Uri uri = Uri.parse(url);

        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        progressBar.setVisibility(View.GONE);

        Boolean flagUrl = url
                .contains("youtu");

        // Check for Internet Connection

        if (flagUrl) {

            String videoUrl = url;

//                    String videoId = videoUrl.substring(videoUrl
//                            .lastIndexOf("=") + 1);

//                    String url =videoUrl.substring(videoUrl
//                            .lastIndexOf("&index") + 0);

            String[] parts = videoUrl.split("=");
            String part1 = parts[0]; // 004
            String videoId = parts[0]; // 034556


//            String[] parts1 = videoId.split("&index");
//
//            String url = parts1[0];
//
//
//            String[] parts2 = videoId.split("&list");
//
//
//            String url2 = parts2[0];

            Log.e("video", url);

            try {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + url));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }

            } catch (ActivityNotFoundException e) {

                // youtube is not installed.Will be opened in other
                // available apps
                Intent videoIntent = new Intent(Intent.ACTION_VIEW);
                videoIntent.setDataAndType(
                        Uri.parse(url),
                        "video/*");
                startActivity(videoIntent);
            }

        } else {

            Intent videoIntent = new Intent(Intent.ACTION_VIEW);
            videoIntent.setDataAndType(
                    Uri.parse(url), "video/*");
            startActivity(videoIntent);
        }

//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                progressBar.setVisibility(View.GONE);
//                mp.start();
//            }
//        });



        /*
         * Apply our splash exit (fade out) and main entry (fade in)
         * animation transitions.
         */

//        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) videoView.getLayoutParams();
//        params.width =  (int) (250*metrics.density);
//        params.height = (int) (200*metrics.density);
//        params.leftMargin = 30;
//        videoView.setLayoutParams(params);

        fullscreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoView.getLayoutParams();
//                params.width =  metrics.widthPixels;
//                params.height = metrics.heightPixels;
//                params.leftMargin = 0;
//                videoView.setLayoutParams(params);

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                intent.setDataAndType(uri, "video/mp4");
                intent.setDataAndType(uri, "video/*");
                startActivity(intent);
            }
        });

        videoView.start();
    }

    @Override
    protected void onResume() {
        //   overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }
}
