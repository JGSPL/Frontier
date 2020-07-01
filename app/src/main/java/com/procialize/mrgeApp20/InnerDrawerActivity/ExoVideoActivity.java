package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Utility.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jzvd.JzvdStd;

import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_FOLDER_IMAGE_PATH;


/**
 * Created by HP-PC on 11-08-2016.
 */
public class ExoVideoActivity extends AppCompatActivity {

    JzvdStd emVideoView;

    String videoUrl = "";
    String title = "";
    String tripId = "", page;
    Button btn_share;
    RelativeLayout llTop;
    ImageView headerlogoIv;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    TextView txtTitle;
    ProgressDialog pDialog;
    TextView txtView;
    private DownloadManager downloadManager;
    File file = null;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoview);
        videoUrl = getIntent().getExtras().getString("videoUrl");
        title = getIntent().getExtras().getString("title");
        page = getIntent().getExtras().getString("page");

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
                onBackPressed();
            }
        });

        context = ExoVideoActivity.this;

        TextView txtIcon = findViewById(R.id.txtIcon);
        btn_share = findViewById(R.id.btn_share);
        txtView = findViewById(R.id.txtView);
//        llTop = (RelativeLayout) findViewById(R.id.rlData);
//        headerlogoIv = findViewById(R.id.headerlogoIv);
//        headerlogoIv.setText(title);
//        Util.logomethod(this,headerlogoIv);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        txtIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtTitle = findViewById(R.id.txtTitle);
        if (!(title == null) || title != null || !(title.equalsIgnoreCase("null"))) {
            txtTitle.setText(title);
            txtTitle.setTextColor(Color.parseColor(colorActive));

        } else {
            txtTitle.setText(" ");

        }

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTextUrl(title, ApiConstant.selfievideo + videoUrl);
            }
        });


        if (page.equalsIgnoreCase("videoMain")) {
            SharedPreferences prefs1 = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String picPath = prefs1.getString(KEY_FOLDER_IMAGE_PATH, "");

            setupVideoView(/*ApiConstant.folderimage*/picPath + videoUrl);

        } else if (page.equalsIgnoreCase("travel")) {
            setupVideoView(ApiConstant.imgURL + "uploads/travel_gallery/" + videoUrl);

        } else {
            setupVideoView(ApiConstant.selfievideo + videoUrl);

        }


        try {
            file = createVideoFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                shareTextUrl(title, ApiConstant.selfievideo + videoUrl);
                SharedPreferences prefs1 = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String picPath = prefs1.getString(KEY_FOLDER_IMAGE_PATH, "");
                downloadFile(/*ApiConstant.folderimage*/picPath + videoUrl, file);
                shareVideo(file.getAbsolutePath());
            }
        });

    }

    private File createVideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "MP4_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File video = File.createTempFile(
                imageFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        Uri videoUri = Uri.fromFile(video);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(videoUri);
        this.sendBroadcast(mediaScanIntent);
        return video;
    }


    private void downloadFile(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();

            shareVideo(outputFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }

    private void shareVideo(String path1) {

        File path = new File(path1);

//        ContentValues content = new ContentValues(4);
//        content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
//                System.currentTimeMillis() / 1000);
//        content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
//        content.put(MediaStore.Video.Media.DATA, path);
//
//        ContentResolver resolver = context.getApplicationContext().getContentResolver();
//        Uri uri = resolver.insert(MediaStore.Video.Media.INTERNAL_CONTENT_URI, content);
//
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        sharingIntent.setType("video/*");
//        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey this is the video subject");
//        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Hey this is the video text");
//        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        context.startActivity(Intent.createChooser(sharingIntent, "Share Video"));

//     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)


        Uri uri = Uri.parse(path.getPath());
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Text");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("video/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));

    }

    private void setupVideoView(final String videoUrl1) {

        MediaController mediaController = new MediaController(this);
        Uri uri = Uri.parse(videoUrl1);
        emVideoView = findViewById(R.id.video_view);
  /*      emVideoView.setUp(videoUrl1
                , JzvdStd.SCREEN_WINDOW_NORMAL, "");*/

        emVideoView.setUp(videoUrl1,""
                , JzvdStd.SCREEN_NORMAL);

        emVideoView.startVideo();

        Glide.with(emVideoView).load(videoUrl1).into(emVideoView.thumbImageView);
//        emVideoView.setVisibility(View.VISIBLE);
//        emVideoView.setVideoURI(uri);
//        emVideoView.setMediaController(mediaController);
//
//        emVideoView.requestFocus();
//        ViewGroup.LayoutParams params = emVideoView.getLayoutParams();
//        params.height = 600;
//        emVideoView.setLayoutParams(params);
//
////        Display display = getWindowManager().getDefaultDisplay();
////        Point size = new Point();
////        display.getSize(size);
////        emVideoView.getHolder().setFixedSize(size.x, size.y);
//        emVideoView.start();
        //  Glide.with(emVideoView.getContext()).load(ApiConstant.folderimage+videoUrl).into(emVideoView.thumbImageView);

    }

//   /* @Override
//    public void onPrepared() {
//        //Starts the video playback as soon as it is ready
//        emVideoView.start();
//
//        llTop.setVisibility(View.GONE);
//        emVideoView.setVisibility(View.VISIBLE);
//
//
//    }*/
//
//
//
//
//    /*private void hide() {
//
//        SlideOutUpAnimator slideOutUpAnimator = new SlideOutUpAnimator();
//        slideOutUpAnimator.prepare(rlTop);
//        slideOutUpAnimator.setDuration(1500);
//
//        slideOutUpAnimator.animate();
//
//    }
//
//    private void show() {
//        rlTop.setVisibility(View.VISIBLE);
//        SlideInDownAnimator slideInDownAnimator = new SlideInDownAnimator();
//        slideInDownAnimator.prepare(rlTop);
//        slideInDownAnimator.setDuration(1500);
//
//        slideInDownAnimator.animate();
//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hide();
//            }
//        }, Constants.SPLASH_TIME);
//
//
//    }*/

    private void shareTextUrl(String data, String url) {
//        Intent share = new Intent(Intent.ACTION_SEND);
//        share.setType("text/plain");
//        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//        // Add data to the intent, the receiving app will decide
//        // what to do with it.
//        share.putExtra(Intent.EXTRA_SUBJECT, data);
//        share.putExtra(Intent.EXTRA_TEXT, url);
//
//        startActivity(Intent.createChooser(share, "Share link!"));


//        File f = new File(url);
//        Uri uriPath = Uri.parse(f.getPath());
//
//        Intent share = new Intent(Intent.ACTION_SEND);
//        share.setType("video/*");
//        share.putExtra(Intent.EXTRA_STREAM, uriPath);
//        share.putExtra(Intent.EXTRA_TEXT, data);
//        startActivity(Intent.createChooser(share, "Share Video"));

//        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri uri = Uri.parse(ApiConstant.folderimage + videoUrl);
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalFilesDir(ExoVideoActivity.this, Environment.DIRECTORY_DOWNLOADS, videoUrl);
//        Long reference = downloadManager.enqueue(request);
//        String path = url; //should be local path of downloaded video
////1552648367_thumb.mp4
//        ContentValues content = new ContentValues(4);
//        content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
//                System.currentTimeMillis() / 1000);
//        content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
//        content.put(MediaStore.Video.Media.DATA, path);

//        ContentResolver resolver = getApplicationContext().getContentResolver();
//        Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);
//
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        sharingIntent.setType("video/*");
//        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "");
//        sharingIntent.putExtra(Intent.EXTRA_TEXT, data);
//        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        startActivity(Intent.createChooser(sharingIntent, "Share Video"));
    }

    @Override
    protected void onPause() {
        JzvdStd.releaseAllVideos();
        super.onPause();
    }
}

