package com.procialize.frontier.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.CustomTools.PicassoTrustAll;
import com.procialize.frontier.GetterSetter.DeleteExhibitorBrochure;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.Util;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentDetailActivity extends AppCompatActivity {

    Button btn_download;
    String eventid, colorActive;
    String MY_PREFS_NAME = "ProcializeInfo";
    WebView webview;
    ImageView image_view;
    JzvdStd video_view;
    String filetype, file, br_id;
    File download_file, pdffile;
    ImageView headerlogoIv;
    ProgressDialog pDialog;
    String img = "";
    LinearLayout linear;
    APIService mAPIService;
    SharedPreferences prefs;
    HashMap<String, String> user;
    SessionManager session;
    String accesstoken, exhibitor_id, exhibitor_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                JzvdStd.releaseAllVideos();
            }
        });

        btn_download = findViewById(R.id.btn_download);
        webview = findViewById(R.id.webview);
        video_view = findViewById(R.id.video_view);
        image_view = findViewById(R.id.image_view);
        linear = findViewById(R.id.linear);
        mAPIService = ApiUtils.getAPIService();
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        session = new SessionManager(this);
        user = session.getUserDetails();
        // token
        accesstoken = user.get(SessionManager.KEY_TOKEN);
        exhibitor_status = user.get(SessionManager.EXHIBITOR_STATUS);

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));

        }


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        btn_download.setBackgroundColor(Color.parseColor(colorActive));

        Intent intent = getIntent();
        filetype = intent.getStringExtra("type");
        file = intent.getStringExtra("file");
        br_id = intent.getStringExtra("br_id");
        exhibitor_id = intent.getStringExtra("ex_id");
        ExhibitorAnalyticsSubmit(eventid, accesstoken, "exhibitor_brochure_view", br_id, exhibitor_id);
        if (filetype.equalsIgnoreCase("pdf")) {
            webview.setVisibility(View.VISIBLE);
            video_view.setVisibility(View.GONE);
            image_view.setVisibility(View.GONE);
            webview.setBackgroundColor(Color.TRANSPARENT);
            WebSettings settings = webview.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(false);
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            settings.setAppCacheEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


            webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webview.setScrollbarFadingEnabled(true);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }


            pDialog = new ProgressDialog(DocumentDetailActivity.this);

            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    try {
                        pDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    try {
                        pDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + ApiConstant.doc + "uploads/exhibitor_brochure/" + file);
        } else if (filetype.equalsIgnoreCase("image")) {
            webview.setVisibility(View.GONE);
            video_view.setVisibility(View.GONE);
            image_view.setVisibility(View.VISIBLE);

            Glide.with(this).load(ApiConstant.doc + "uploads/exhibitor_brochure/" + file).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                    image_view.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(image_view);
        } else if (filetype.equalsIgnoreCase("video")) {
            webview.setVisibility(View.GONE);
            video_view.setVisibility(View.VISIBLE);
            image_view.setVisibility(View.GONE);

           /* video_view.setUp(ApiConstant.doc + "uploads/exhibitor_brochure/" + file
                    , JzvdStd.SCREEN_WINDOW_NORMAL, "");
*/
            video_view.setUp("uploads/exhibitor_brochure/" + file,""
                    , JzvdStd.SCREEN_NORMAL);

            video_view.startVideo();

            Glide.with(video_view).load(ApiConstant.doc + "uploads/exhibitor_brochure/" + file).into(video_view.thumbImageView);
        }

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JzvdStd.releaseAllVideos();
                ExhibitorAnalyticsSubmit(eventid, accesstoken, "exhibitor_brochure_download", br_id, exhibitor_id);

                if (filetype.equalsIgnoreCase("pdf")) {
                    try {
                        pdffile = createPDFFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    new DownloadFile().execute(ApiConstant.doc + "uploads/exhibitor_brochure/" + file);
                } else if (filetype.equalsIgnoreCase("image")) {
                    try {
                        try {
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            String imageFileName = "JPEG_" + timeStamp + "_";
                            img = imageFileName;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    PicassoTrustAll.getInstance(DocumentDetailActivity.this)
                            .load(ApiConstant.doc + "uploads/exhibitor_brochure/" + file)
                            .into(new com.squareup.picasso.Target() {
                                      @Override
                                      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                          try {
                                              String root = Environment.getExternalStorageDirectory().toString();
                                              File myDir = new File(root + "/"+ApiConstant.folderName);

                                              if (!myDir.exists()) {
                                                  myDir.mkdirs();
                                              }
                                              Toast.makeText(DocumentDetailActivity.this,
                                                      "Download completed- check folder Procialize/Image",
                                                      Toast.LENGTH_SHORT).show();
                                              String name = img + ".jpg";
                                              myDir = new File(myDir, name);
                                              FileOutputStream out = new FileOutputStream(myDir);
                                              bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                              out.flush();
                                              out.close();
                                          } catch (Exception e) {
                                              // some action
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

                } else if (filetype.equalsIgnoreCase("video")) {
                    try {
                        download_file = createVideoFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    new DownloadFile().execute(ApiConstant.doc + "uploads/exhibitor_brochure/" + file);
//                    downloadFile(ApiConstant.imgURL + "uploads/exhibitor_brochure/" + file, download_file);
                }
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

    private File createPDFFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PDF_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        File video = File.createTempFile(
                imageFileName,  /* prefix */
                ".pdf",         /* suffix */
                storageDir      /* directory */
        );

        Uri videoUri = Uri.fromFile(video);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(videoUri);
        this.sendBroadcast(mediaScanIntent);
        return video;
    }


    private static void downloadFile(String url, File outputFile) {
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
        } catch (FileNotFoundException e) {
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }


    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(DocumentDetailActivity.this);
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
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "Procialize/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d("error", "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

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

            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();

//            sharePdf(folder + fileName, PdfViewerActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        JzvdStd.releaseAllVideos();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
    }

    public void ExhibitorAnalyticsSubmit(String event_id, String token, String analytic_type, String target_id, String exhibitor_id) {
        mAPIService.ExhibitorAnalyticsSubmit(event_id, token, analytic_type, target_id, exhibitor_id).enqueue(new Callback<DeleteExhibitorBrochure>() {
            @Override
            public void onResponse(Call<DeleteExhibitorBrochure> call, Response<DeleteExhibitorBrochure> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        Intent main = new Intent(DocumentDetailActivity.this, LoginActivity.class);
                        startActivity(main);

                    } else {
//                        Toast.makeText(DocumentDetailActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    Toast.makeText(DocumentDetailActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<DeleteExhibitorBrochure> call, Throwable t) {
//                Toast.makeText(DocumentDetailActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
