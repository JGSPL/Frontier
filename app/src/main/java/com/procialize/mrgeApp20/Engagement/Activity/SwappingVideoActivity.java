package com.procialize.mrgeApp20.Engagement.Activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.procialize.mrgeApp20.BuildConfig;
import com.procialize.mrgeApp20.Engagement.Adapter.SwipePagerVideoAdapter;
import com.procialize.mrgeApp20.Engagement.Adapter.SwipeEngagmentVideoAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.CustomTools.PicassoTrustAll;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.GetterSetter.SelfieLike;
import com.procialize.mrgeApp20.GetterSetter.VideoContest;
import com.procialize.mrgeApp20.GetterSetter.VideoContestLikes;
import com.procialize.mrgeApp20.GetterSetter.VideoContestListFetch;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;
import com.procialize.mrgeApp20.util.GetUserActivityReport;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.mrgeApp20.Utility.Util.setNotification;

public class SwappingVideoActivity extends AppCompatActivity implements SwipeEngagmentVideoAdapter.SwipeImageSelfieAdapterListner {
    public int rvposition = 0;
    String name,position="0",positionSelect="0";
    List<VideoContest> firstLevelFilters;
    SwipeEngagmentVideoAdapter swipeImageAdapter;
    SwipePagerVideoAdapter swipepagerAdapter;
    ViewPager pager;
    ImageView right, left, backIv;
    ImageView headerlogoIv;
    String colorActive, eventid, user_id, token;
    String MY_PREFS_NAME = "ProcializeInfo";
    String img = "";
    RecyclerView recyclerView;
    RelativeLayout relative;
    TextView tv_name, tv_like;
    ImageView likeIv;
    ProgressBar progressBar;
    int likePos;
    private ConnectionDetector cd;
    private APIService mAPIService;
    public boolean isShare=false;
    String strPath = "";
    TextView tv_header;

    static public Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
//            bmpUri = Uri.fromFile(file);
            bmpUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".android.fileprovider", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swapping_selfiegallery);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent i = getIntent();

        cd = new ConnectionDetector(this);
        //name = getIntent().getExtras().getString("url");
        //positionSelect = getIntent().getStringExtra("position");
        firstLevelFilters = (List<VideoContest>) getIntent().getExtras().getSerializable("gallerylist");
        positionSelect = getIntent().getExtras().getString("positionSelect");
        mAPIService = ApiUtils.getAPIService();
        recyclerView = findViewById(R.id.listrecycler);
        pager = findViewById(R.id.pager);
        tv_header = findViewById(R.id.tv_header);
        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
        likeIv = findViewById(R.id.likeIv);
        backIv = findViewById(R.id.backIv);
        relative = findViewById(R.id.relative);
        tv_name = findViewById(R.id.tv_name);
        tv_like = findViewById(R.id.tv_like);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        progressBar = findViewById(R.id.progressBar);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
        eventid = prefs.getString("eventid", "1");

        tv_header.setText("Video Engagement");
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        user_id = user.get(SessionManager.KEY_ID);
        token = user.get(SessionManager.KEY_TOKEN);

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            // relative.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            //  relative.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }
        Util.logomethod(this, headerlogoIv);

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);


        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);

        swipeImageAdapter = new SwipeEngagmentVideoAdapter(this, firstLevelFilters, this);
        swipeImageAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(swipeImageAdapter);
        recyclerView.scheduleLayoutAnimation();


        swipepagerAdapter = new SwipePagerVideoAdapter(this, firstLevelFilters);
        swipepagerAdapter.notifyDataSetChanged();
        pager.setAdapter(swipepagerAdapter);
        pager.scheduleLayoutAnimation();


        LinearLayout linsave = findViewById(R.id.linSave);
        LinearLayout linShare = findViewById(R.id.linShare);
        LinearLayout linLike = findViewById(R.id.lin_like);
        TextView savebtn = findViewById(R.id.savebtn);
        TextView sharebtn = findViewById(R.id.sharebtn);

        //  linsave.setBackgroundColor(Color.parseColor(colorActive));
        //  linMsg.setBackgroundColor(Color.parseColor(colorActive));
        // savebtn.setBackgroundColor(Color.parseColor(colorActive));
        // sharebtn.setBackgroundColor(Color.parseColor(colorActive));

        linLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likePos = rvposition;
                if (firstLevelFilters.get(rvposition).getLikeFlag().equals("1")) {
                    likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                    likeIv.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                    VideoContestLike(token, eventid, firstLevelFilters.get(rvposition).getId());
                    try {
                        int count = Integer.parseInt(firstLevelFilters.get(rvposition).getTotalLikes());
                        if (count > 0) {
                            count = count - 1;
                            tv_like.setText(count + " Likes");
                        } else {
                            tv_like.setText("0 Likes");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_active_like));
                    likeIv.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                    VideoContestLike(token, eventid, firstLevelFilters.get(rvposition).getId());

                    try {
                        int count = Integer.parseInt(firstLevelFilters.get(rvposition).getTotalLikes());

                        count = count + 1;
                        tv_like.setText(count + " Likes");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

/*
        linsave.setBackgroundColor(Color.parseColor(colorActive));
        linMsg.setBackgroundColor(Color.parseColor(colorActive));
        savebtn.setBackgroundColor(Color.parseColor(colorActive));
        sharebtn.setBackgroundColor(Color.parseColor(colorActive));*/




        linShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    isShare = true;
                    String UrlfileName = firstLevelFilters.get(rvposition).getFileName();
                    if (UrlfileName.contains("youtu")) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                        // Add data to the intent, the receiving app will decide
                        // what to do with it.
                        share.putExtra(Intent.EXTRA_SUBJECT, "");
                        share.putExtra(Intent.EXTRA_TEXT, UrlfileName);

                        startActivity(Intent.createChooser(share, "Share link!"));
                    } else {
                        boolean isPresentFile = false;
                        File dir = new File(Environment.getExternalStorageDirectory().toString() + "/" + ApiConstant.folderName);
                        if (dir.isDirectory()) {
                            String[] children = dir.list();
                            for (int i = 0; i < children.length; i++) {
                                String filename = children[i].toString();
                                if (UrlfileName.equals(filename)) {
                                    isPresentFile = true;
                                }
                            }
                        }

                        if (!isPresentFile) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SwappingVideoActivity.this);
                            builder.setTitle("Download and Share");
                            builder.setMessage("Video will be share only after download,\nDo you want to continue for download and share?");
                            builder.setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            new DownloadFile().execute(ApiConstant.selfievideo + UrlfileName);
                                        }
                                    });
                            builder.show();

                        } else if (isPresentFile) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SwappingVideoActivity.this);
                            builder.setTitle("Download and Share");
                            builder.setMessage("Video will be share only after download,\nDo you want to continue for download and share?");
                            builder.setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            new DownloadFile().execute(ApiConstant.selfievideo + UrlfileName);
                                        }
                                    });
                            builder.show();
                        }
                    }
                } else {
                    Toast.makeText(SwappingVideoActivity.this, "No Internet Connection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        linsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShare = false;
                String UrlfileName = firstLevelFilters.get(rvposition).getFileName();
                if (UrlfileName.contains("youtu")) {
                } else {
                    //---------------------------------------------------------------------------------------------------------
                    GetUserActivityReport getUserActivityReport = new GetUserActivityReport(SwappingVideoActivity.this, token,
                            eventid,
                            ApiConstant.fileDownloaded,
                            "22",
                            firstLevelFilters.get(rvposition).getId());
                    getUserActivityReport.userActivityReport();
                    //---------------------------------------------------------------------------------------------
                    new DownloadFile().execute(ApiConstant.selfievideo + UrlfileName);
                }
            }
        });



        indexset(positionSelect);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    int position = pager.getCurrentItem() + 1;

                    if (position <= firstLevelFilters.size()) {
                        pager.setCurrentItem(position);
                        try{
                        tv_name.setText(StringEscapeUtils.unescapeJava(firstLevelFilters.get(position).getTitle()));
                        }catch (IllegalArgumentException e){
                            e.printStackTrace();

                        }
                        tv_like.setText(firstLevelFilters.get(position).getTotalLikes() + " Likes");
                        if (firstLevelFilters.get(position).getLikeFlag().equals("1")) {


                            likeIv.setImageResource(R.drawable.ic_active_like);
                            likeIv.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                        } else {
                            likeIv.setImageResource(R.drawable.ic_like);
                            likeIv.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    int position = pager.getCurrentItem() - 1;

                    if (position >= 0) {
                        pager.setCurrentItem(position);
                        try{
                        tv_name.setText(StringEscapeUtils.unescapeJava(firstLevelFilters.get(position).getTitle()));
                        }catch (IllegalArgumentException e){
                            e.printStackTrace();

                        }
                        tv_like.setText(firstLevelFilters.get(position).getTotalLikes() + " Likes");
                        if (firstLevelFilters.get(position).getLikeFlag().equals("1")) {
                            likeIv.setImageResource(R.drawable.ic_active_like);
                            likeIv.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                        } else {
                            likeIv.setImageResource(R.drawable.ic_like);
                            likeIv.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                recyclerView.scrollToPosition(position);
                rvposition = position;
                try{
                tv_name.setText(StringEscapeUtils.unescapeJava(firstLevelFilters.get(position).getTitle()));
                }catch (IllegalArgumentException e){
                    e.printStackTrace();

                }
                tv_like.setText(firstLevelFilters.get(position).getTotalLikes() + " Likes");
                if (firstLevelFilters.get(position).getLikeFlag().equals("1")) {
                    likeIv.setImageResource(R.drawable.ic_active_like);
                    likeIv.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                } else {
                    likeIv.setImageResource(R.drawable.ic_like);
                    likeIv.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void onPageSelected(int position) {
                //---------------------------------------------------------------------------------------------------------
                GetUserActivityReport getUserActivityReport = new GetUserActivityReport(SwappingVideoActivity.this, token,
                        eventid,
                        ApiConstant.fileViewed,
                        "22",
                        firstLevelFilters.get(rvposition).getId());
                getUserActivityReport.userActivityReport();
                //-----------------------------------------------------------------------------------------------

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void indexset(String position) {
       // for (int j = 0; j < firstLevelFilters.size(); j++) {
           // if (firstLevelFilters.get(j).getId().equalsIgnoreCase(name)) {
                pager.setCurrentItem(Integer.parseInt(position));
                recyclerView.scrollToPosition(Integer.parseInt(position));
           // }
       // }
    }

    @Override
    public void onContactSelected(VideoContest filtergallerylists) {
        indexset(filtergallerylists.getId());
    }

    @Override
    public void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
    }

    public void showResponse(Response<VideoContestListFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            firstLevelFilters.clear();
            firstLevelFilters = response.body().getVideoContest();
            swipeImageAdapter = new SwipeEngagmentVideoAdapter(this, firstLevelFilters, this);
            swipeImageAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(swipeImageAdapter);
            recyclerView.scheduleLayoutAnimation();

            swipepagerAdapter = new SwipePagerVideoAdapter(this, firstLevelFilters);
            swipepagerAdapter.notifyDataSetChanged();
            pager.setAdapter(swipepagerAdapter);
            pager.scheduleLayoutAnimation();

            pager.setCurrentItem(likePos);
            recyclerView.scrollToPosition(likePos);
        } else {
            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
        }
    }

    public void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void dismissProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }


    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(SwappingVideoActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());
                //Append timestamp to file name
                //fileName = timestamp + "_" + fileName;
                //External directory path to save file
                //folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";
                String folder = Environment.getExternalStorageDirectory().toString() + "/" + ApiConstant.folderName + "/";


                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                strPath = folder + fileName;
                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d("VideoViewGallery", "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Download completed- check folder " + ApiConstant.folderName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            if (isShare) {
                ContentValues content = new ContentValues(4);
                content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
                        System.currentTimeMillis() / 1000);
                content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                content.put(MediaStore.Video.Media.DATA, strPath);

                ContentResolver resolver = getContentResolver();
                Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("video/*");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Video Share");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(sharingIntent, "Share Video"));
            } else {
                // Display File path after downloading
                Toast.makeText(getApplicationContext(),
                        message, Toast.LENGTH_LONG).show();
            }
        }
    }


    public void VideoContestLike(String token, String eventid, String id) {
        mAPIService.VideoContestLikes(token, eventid, id).enqueue(new Callback<VideoContestLikes>() {
            @Override
            public void onResponse(Call<VideoContestLikes> call, Response<VideoContestLikes> response) {

                if (response.body().getStatus().equalsIgnoreCase("Success")) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    showLikeResponse(response);
                } else {

//                    Toast.makeText(VideoContestActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<VideoContestLikes> call, Throwable t) {
                Toast.makeText(SwappingVideoActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showLikeResponse(Response<VideoContestLikes> response) {
        if (response.body().getStatus().equalsIgnoreCase("Success")) {
            if (firstLevelFilters.get(rvposition).getLikeFlag().equals("1")) {
                firstLevelFilters.get(rvposition).setLikeFlag("0");
                try {

                    int count = Integer.parseInt(firstLevelFilters.get(rvposition).getTotalLikes());

                    if (count > 0) {
                        count = count - 1;
                        firstLevelFilters.get(rvposition).setTotalLikes(count + "");
                    } else {
                        firstLevelFilters.get(rvposition).setTotalLikes("0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                firstLevelFilters.get(rvposition).setLikeFlag("1");
                try {

                    int count = Integer.parseInt(firstLevelFilters.get(rvposition).getTotalLikes());
                    if (count > 0) {
                        count = count + 1;
                        firstLevelFilters.get(rvposition).setTotalLikes(count + "");
                    } else {
                        firstLevelFilters.get(rvposition).setTotalLikes("0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            swipepagerAdapter.notifyDataSetChanged();
            swipeImageAdapter.notifyDataSetChanged();

        } else {
        }
    }
}