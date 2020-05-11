package com.procialize.mrgeApp20.NewsFeed.Views.Activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.BuildConfig;
import com.procialize.mrgeApp20.CustomTools.MyJzvdStd;
import com.procialize.mrgeApp20.CustomTools.PicassoTrustAll;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.GetterSetter.news_feed_media;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.NewsFeed.Views.Adapter.SwipeMultimediaDetailsAdapter;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.jzvd.JzvdStd;

import static com.procialize.mrgeApp20.Utility.Utility.setgradientDrawable;

public class ImageMultipleActivity extends AppCompatActivity {
    public ProgressDialog progressDialog;
    Button savebtn, sharebtn;
    String url;
    ImageView headerlogoIv;
    String MY_PREFS_NAME = "ProcializeInfo";
    String imgname, colorActive;
    // CardView card_view;
    ViewPager rvp_slide;
    LinearLayout ll_dots;
    int position = 0;
    List<com.procialize.mrgeApp20.GetterSetter.news_feed_media> news_feed_media;
    int shareOrSaveImagePosition = 0;
    private ProgressBar progressBar;
    private ConnectionDetector cd;
    private String strPath;
    boolean isShare;

    static public void shareImage(String url, final Context context) {
  /*      if (url.contains("mp4")) {
            ContentValues content = new ContentValues(4);
            content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
                    System.currentTimeMillis() / 1000);
            content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            content.put(MediaStore.Video.Media.DATA, url);
            ContentResolver resolver = context.getContentResolver();
            Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("video/*");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Title");
            sharingIntent.putExtra(Intent.EXTRA_TEXT,uri);
            context.startActivity(Intent.createChooser(sharingIntent,"share:"));

        } else {*/
        Picasso.with(context).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                // i.putExtra(Intent.EXTRA_SUBJECT, data);
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
        // }

    }

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_multiple);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        cd = new ConnectionDetector(getApplicationContext());

        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CommentMultiActivity.this, HomeActivity.class);
//                startActivity(intent);
                finish();
                JzvdStd.releaseAllVideos();
            }
        });

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        LinearLayout linear = findViewById(R.id.linear);
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

        cd = new ConnectionDetector(getApplicationContext());

        //toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.activetab), PorterDuff.Mode.SRC_ATOP);

       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        });*/

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        if (intent != null) {
            news_feed_media = ( ArrayList<news_feed_media> ) getIntent().getSerializableExtra("media_list");
            position = getIntent().getIntExtra("position", 0);
            shareOrSaveImagePosition = position;
//            imgname = url.substring(58, 60);

        }
        final Random myRandom = new Random();
        imgname = String.valueOf(myRandom.nextInt(500));


        //int colorInt = Color.parseColor(colorActive);

        savebtn = findViewById(R.id.savebtn);
        sharebtn = findViewById(R.id.sharebtn);
        progressBar = findViewById(R.id.progressBar);
        //card_view = findViewById(R.id.card_view);
        rvp_slide = findViewById(R.id.rvp_slide);
        ll_dots = findViewById(R.id.ll_dots);
        progressBar = findViewById(R.id.progressBar);

        GradientDrawable shape = setgradientDrawable(5, colorActive);
        savebtn.setBackground(shape);
        sharebtn.setBackground(shape);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cd.isConnectingToInternet()) {
                    isShare = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (ImageMultipleActivity.this.checkSelfPermission(
                                "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                            final String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                            ActivityCompat.requestPermissions(ImageMultipleActivity.this, permissions, 0);
                        } else if (ImageMultipleActivity.this.checkSelfPermission(
                                "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                            final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                            ActivityCompat.requestPermissions(ImageMultipleActivity.this, permissions, 0);
                        } else {

                            // new myAsyncTask().execute();
                            // new ImageDownloadAndSave().execute();

                            // addImageToGallery(url,ImageMultipleActivity.this);

                            url = ApiConstant.newsfeedwall + news_feed_media.get(shareOrSaveImagePosition).getMediaFile();
                            if (url.contains("mp4")) {
                                new DownloadFile().execute(url);
                            } else {
                                url = ApiConstant.newsfeedwall + news_feed_media.get(shareOrSaveImagePosition).getMediaFile();
                                //String root = Environment.getExternalStorageDirectory().toString();
                                PicassoTrustAll.getInstance(ImageMultipleActivity.this)
                                        .load(url)
                                        .into(new Target() {
                                                  @Override
                                                  public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                      try {
                                                          String root = Environment.getExternalStorageDirectory().toString();
                                                          File myDir = new File(root + "/"+ApiConstant.folderName);

                                                          if (!myDir.exists()) {
                                                              myDir.mkdirs();
                                                          }
                                                          Date date = new Date();
                                                          //getTime() returns current time in milliseconds
                                                          long time = date.getTime();
                                                          Toast.makeText(ImageMultipleActivity.this,
                                                                  "Download completed- check folder "+ApiConstant.folderName,
                                                                  Toast.LENGTH_SHORT).show();
                                                          String name = imgname + time + ".jpg";
                                                          myDir = new File(myDir, name);
                                                          FileOutputStream out = new FileOutputStream(myDir);
                                                          bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                                          out.flush();
                                                          out.close();
                                                      } catch (Exception e) {
                                                      }
                                                  }

                                                  @Override
                                                  public void onBitmapFailed(Drawable errorDrawable) {
                                                  }

                                                  @Override
                                                  public void onPrepareLoad(Drawable placeHolderDrawable) {
                                                  }
                                              }
                                        );
                            }
                        }
                    }
                } else {
                    Toast.makeText(getBaseContext(), "No Internet Connection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        final ArrayList<String> imagesSelectednew = new ArrayList<>();
        final ArrayList<String> imagesSelectednew1 = new ArrayList<>();
        final ImageView[] ivArrayDotsPager;
        for (int i = 0; i < news_feed_media.size(); i++) {
            imagesSelectednew.add(ApiConstant.newsfeedwall + news_feed_media.get(i).getMediaFile());
            imagesSelectednew1.add(ApiConstant.newsfeedwall + news_feed_media.get(i).getThumb_image());
        }


        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShare = true;
               /* url = ApiConstant.newsfeedwall + news_feed_media.get(shareOrSaveImagePosition).getMediaFile();
                if (url.contains("mp4")) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                    // Add data to the intent, the receiving app will decide
                    // what to do with it.
                    share.putExtra(Intent.EXTRA_SUBJECT, "");
                    share.putExtra(Intent.EXTRA_TEXT, url);

                    startActivity(Intent.createChooser(share, "Share link!"));
                } else {
                    shareImage(url, ImageMultipleActivity.this);
                }*/

                //final List<news_feed_media> newsFeedMedia = myList;
                String type = news_feed_media.get(shareOrSaveImagePosition).getMedia_type();
                if (cd.isConnectingToInternet()) {
                    if (news_feed_media.size() > 0) {

                        if (type.equals("Video")) {
                            boolean isPresentFile = false;
                            File dir = new File(Environment.getExternalStorageDirectory().toString() + "/" + ApiConstant.folderName);
                            if (dir.isDirectory()) {
                                String[] children = dir.list();
                                for (int i = 0; i < children.length; i++) {
                                    String filename = children[i].toString();
                                    if (news_feed_media.get(shareOrSaveImagePosition).getMediaFile().equals(filename)) {
                                        isPresentFile = true;
                                    }
                                }
                            }

                            if (!isPresentFile) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ImageMultipleActivity.this);
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

                                                new DownloadFile().execute(ApiConstant.newsfeedwall + news_feed_media.get(shareOrSaveImagePosition).getMediaFile());
                                            }
                                        });
                                builder.show();

                            } else if (isPresentFile) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ImageMultipleActivity.this);
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
                                                new DownloadFile().execute(ApiConstant.newsfeedwall + news_feed_media.get(shareOrSaveImagePosition).getMediaFile());
                                            }
                                        });
                                builder.show();
                            }
                        } else {
                            shareImage( ApiConstant.newsfeedwall + news_feed_media.get(shareOrSaveImagePosition).getMediaFile(), ImageMultipleActivity.this);
                        }
                    } else {
                       // shareTextUrl(date + "\n" + heading, StringEscapeUtils.unescapeJava(heading));
                    }
                }
            }
        });

        SwipeMultimediaDetailsAdapter swipepagerAdapter = new SwipeMultimediaDetailsAdapter(ImageMultipleActivity.this, imagesSelectednew, imagesSelectednew1);
        rvp_slide.setAdapter(swipepagerAdapter);
        swipepagerAdapter.notifyDataSetChanged();
        rvp_slide.setCurrentItem(position);
        setupPagerIndidcatorDots(position, ll_dots, imagesSelectednew.size());
        if (imagesSelectednew.size() > 1) {
            ivArrayDotsPager = new ImageView[imagesSelectednew.size()];
            //setupPagerIndidcatorDots(0, ll_dots, imagesSelectednew.size());
            rvp_slide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    JzvdStd.releaseAllVideos();
                    MyJzvdStd.releaseAllVideos();
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.pause();
                }

                @Override
                public void onPageSelected(int position1) {
                    shareOrSaveImagePosition = position1;
                    JzvdStd.releaseAllVideos();
                    setupPagerIndidcatorDots(position1, ll_dots, imagesSelectednew.size());
                   /* NewsfeedAdapter.ViewHolder viewHolder = new NewsfeedAdapter.ViewHolder();
                    if (viewHolder.VideoView != null) {
                        viewHolder.VideoView.pause();
                    }*/
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.pause();
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    JzvdStd.releaseAllVideos();
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.pause();
                }
            });
        }

    }

    private void shareTextUrl(String data, String url) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, data);
        share.putExtra(Intent.EXTRA_TEXT, url);

        startActivity(Intent.createChooser(share, "Share link!"));
    }


    private void setupPagerIndidcatorDots(int currentPage, LinearLayout ll_dots, int size) {

        TextView[] dots = new TextView[size];
        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(ImageMultipleActivity.this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(Color.parseColor("#cc9933"));
            ll_dots.addView(dots[i]);
        }

        try {
            if (dots.length > 0) {
                if (dots.length != currentPage) {
                    dots[currentPage].setTextColor(Color.parseColor("#A2A2A2"));
                }
            }
        } catch (Exception e) {

        }

    }

    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
        Log.d("picassoImageTarget", " picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {
                }
            }
        };
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
            this.progressDialog = new ProgressDialog(ImageMultipleActivity.this);
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
                    Log.d("ImageMultipleActivity", "Progress: " + (int) ((total * 100) / lengthOfFile));

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

            if(isShare)
            {
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
                startActivity(Intent.createChooser(sharingIntent, "Share Video"));}
            else {
                // Display File path after downloading
                Toast.makeText(getApplicationContext(),
                        message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyJzvdStd.releaseAllVideos();
        JzvdStd.releaseAllVideos();
        finish();
    }

}
