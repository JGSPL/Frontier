package com.procialize.mrgeApp20.Gallery.Video.Activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.BuildConfig;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.Gallery.Video.Adapter.SwipeVideoAdapter;
import com.procialize.mrgeApp20.Gallery.Video.Adapter.SwipeVideoPagerAdapter;
import com.procialize.mrgeApp20.GetterSetter.FirstLevelFilter;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Utility.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.jzvd.JzvdStd;

import static com.procialize.mrgeApp20.Utility.Util.setNotification;

public class VideoViewGalleryActivity extends AppCompatActivity implements SwipeVideoAdapter.SwipeVideoAdapterListner, SwipeVideoPagerAdapter.onItemClick {

    public int rvposition = 0;
    String name;
    List<FirstLevelFilter> firstLevelFilters;
    SwipeVideoAdapter swipeImageAdapter;
    SwipeVideoPagerAdapter swipepagerAdapter;
    RecyclerView recyclerView;
    ViewPager pager;
    ImageView right, left, backIv;
    ImageView headerlogoIv;
    String colorActive;
    String MY_PREFS_NAME = "ProcializeInfo";
    String img = "";
    RelativeLayout linear;
    TextView tv_title;
    String strPath = "";
    boolean isShare;
    /*TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    //No need to implement.
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    //No need to implement.
                }
            }
    };*/
    private ConnectionDetector cd;

    static public void shareImage(String url, final Context context) {
        Picasso.with(context).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_SUBJECT, "");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));

                context.startActivity(Intent.createChooser(i, "Share Image"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    static public Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
//            bmpUri = Uri.fromFile(file);
            bmpUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".android.fileprovider", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public void onContactSelected(FirstLevelFilter filtergallerylists) {

        indexset(filtergallerylists.getFileName());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void indexset(String name) {
        for (int j = 0; j < firstLevelFilters.size(); j++) {
            if (firstLevelFilters.get(j).getFileName().equalsIgnoreCase(name)) {
                pager.setCurrentItem(j);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view_gallery);
        cd = new ConnectionDetector(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /*try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println(e);
        }
*/
        try {
            name = getIntent().getExtras().getString("videoUrl");
            firstLevelFilters = (List<FirstLevelFilter>) getIntent().getExtras().getSerializable("gallerylist");
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.listrecycler);
        pager = findViewById(R.id.pager);
        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
        backIv = findViewById(R.id.backIv);
        linear = findViewById(R.id.linear);
        tv_title = findViewById(R.id.tv_title);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*try {

            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        swipeImageAdapter = new SwipeVideoAdapter(this, firstLevelFilters, this);
        recyclerView.setAdapter(swipeImageAdapter);
        swipeImageAdapter.notifyDataSetChanged();


        swipepagerAdapter = new SwipeVideoPagerAdapter(this, firstLevelFilters, this);
        pager.setAdapter(swipepagerAdapter);
        swipepagerAdapter.notifyDataSetChanged();


        LinearLayout linShare = findViewById(R.id.linShare);
        LinearLayout linSave = findViewById(R.id.linSave);
        TextView savebtn = findViewById(R.id.savebtn);
        TextView sharebtn = findViewById(R.id.sharebtn);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(VideoViewGalleryActivity.this);
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

                                            new DownloadFile().execute(ApiConstant.folderimage + UrlfileName);
                                        }
                                    });
                            builder.show();

                        } else if (isPresentFile) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(VideoViewGalleryActivity.this);
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
                                            new DownloadFile().execute(ApiConstant.folderimage + UrlfileName);
                                        }
                                    });
                            builder.show();
                        }
                    }
                } else {
                    Toast.makeText(VideoViewGalleryActivity.this, "No Internet Connection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        linSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShare = false;
                String UrlfileName = firstLevelFilters.get(rvposition).getFileName();
                if (UrlfileName.contains("youtu")) {
                } else {
                    new DownloadFile().execute(ApiConstant.folderimage + UrlfileName);
                }
            }
        });

        indexset(name);
        int colorInt = Color.parseColor(colorActive);
        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(right.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        right.setImageDrawable(drawable);

        Drawable drawable1 = DrawableCompat.wrap(left.getDrawable());
        DrawableCompat.setTintList(drawable1, csl);
        left.setImageDrawable(drawable1);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    int position = pager.getCurrentItem() + 1;

                    if (position <= firstLevelFilters.size()) {
                        pager.setCurrentItem(position);
                        recyclerView.scrollToPosition(position);
//                        if(pager.getCurrentItem()==position) {
//                            recyclerView.setBackgroundColor(Color.parseColor(colorActive));
//                        }
                        recyclerView.getItemDecorationAt(position);
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
                        recyclerView.scrollToPosition(position);

//                        if(pager.getCurrentItem()==position) {
//                            recyclerView.setBackgroundColor(Color.parseColor(colorActive));
//                        }
                        recyclerView.getItemDecorationAt(position);
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
                String title = firstLevelFilters.get(position).getTitle();
                tv_title.setText(title);
//                recyclerView.getItemDecorationAt(position);
                rvposition = position;

                String UrlfileName = firstLevelFilters.get(rvposition).getFileName();
                if (UrlfileName.contains("youtu")) {
                    linShare.setVisibility(View.VISIBLE);
                    linSave.setVisibility(View.GONE);
                } else {
                    linShare.setVisibility(View.VISIBLE);
                    linSave.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this,tv_notification,ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------

    }

    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }

    @Override
    public void onClickListener(int position) {
        Boolean flagUrl = firstLevelFilters.get(position).getFileName()
                .contains("youtu");
        if (flagUrl) {
            String videoUrl = firstLevelFilters.get(position).getFileName();
            String[] parts = videoUrl.split("=");
            String part1 = parts[0]; // 004
            String videoId = parts[0]; // 034556
            String[] parts1 = videoId.split("&index");
            String url = parts1[0];
            String[] parts2 = videoId.split("&list");
            String url2 = parts2[0];
            Log.e("video", firstLevelFilters.get(position).getFileName());
            try {
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(videoUrl));
                startActivity(webIntent);
            } catch (ActivityNotFoundException e) {
                Intent videoIntent = new Intent(Intent.ACTION_VIEW);
                videoIntent.setDataAndType(
                        Uri.parse(firstLevelFilters.get(position).getFileName()),
                        "video/*");
                startActivity(videoIntent);

            }
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
            this.progressDialog = new ProgressDialog(VideoViewGalleryActivity.this);
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
}
