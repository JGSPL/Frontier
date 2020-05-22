package com.procialize.mrgeApp20.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.BuildConfig;
import com.procialize.mrgeApp20.CustomTools.CircleDisplay;
import com.procialize.mrgeApp20.CustomTools.ImagePath_MarshMallow;
import com.procialize.mrgeApp20.CustomTools.ProgressRequestBodyImage;
import com.procialize.mrgeApp20.CustomTools.ProgressRequestBodyVideo;
import com.procialize.mrgeApp20.GetterSetter.PostTextFeed;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.MyApplication;
import com.procialize.mrgeApp20.Utility.ScalingUtilities;
import com.procialize.mrgeApp20.Utility.Utility;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.apache.http.HttpVersion.HTTP_1_1;

public class PostActivityVideo extends AppCompatActivity implements ProgressRequestBodyImage.UploadCallbacks, ProgressRequestBodyVideo.UploadCallbacks {

    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private static final int READ_REQUEST_CODE = 200;
    private static final String SERVER_PATH = "";
    EditText postEt;
    // Button postbtn;
    TextView postbtn;
    APIService mAPIService;
    CircleDisplay progressbar;
    RelativeLayout relative;
    SessionManager sessionManager;
    String apikey = "";
    RequestBody fbody = null;
    String to;
    ImageView Uploadiv;
    String typepost;
    String userChoosenTask;
    File file;
    Uri capturedImageUri;
    String eventId;
    ImageView profileIV;
    MyApplication appDelegate;
    String mCurrentPhotoPath;
    ImageView imgPlay;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, REQUEST_TAKE_PHOTO = 2, REQUEST_TAKE_GALLERY_VIDEO = 3;
    private Uri uri;
    private String pathToStoredVideo;
    String videoUrl;
    private String selectedImagePath;
    private VideoView displayRecordedVideo;
    private String picturePath = "";
    public static File imgeFile;
    String angle = "0";
    File mediaFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventId = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        appDelegate = (MyApplication) getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appDelegate.setPostImagePath("");
                onBackPressed();
            }
        });

        Intent intent = getIntent();

        headerlogoIv = findViewById(R.id.headerlogoIv);
        //Util.logomethod(this, headerlogoIv);

        postEt = findViewById(R.id.posttextEt);
        postbtn = findViewById(R.id.postbtn);
        Uploadiv = findViewById(R.id.Uploadiv);
        profileIV = findViewById(R.id.profileIV);
        displayRecordedVideo = findViewById(R.id.Upvideov);
        imgPlay = findViewById(R.id.imgPlay);
        progressbar = findViewById(R.id.progressbar);
        relative = findViewById(R.id.relative);
        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(getApplicationContext());
        final TextView txtcount1 = findViewById(R.id.txtcount1);
        // postbtn.setBackgroundColor(Color.parseColor(colorActive));

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(displayRecordedVideo);
        displayRecordedVideo.setMediaController(mediaController);

        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        final String profilepic = user.get(SessionManager.KEY_PIC);

        try {
//            ContextWrapper cw = new ContextWrapper(MrgeHomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            relative.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            relative.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }
        if (profilepic != null) {
//            Glide.with(this).load(ApiConstant.profilepic + profilepic).listener(new RequestListener<Drawable>() {
//                @Override
//                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                    return true;
//                }
//
//                @Override
//                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                    return false;
//                }
//            }).into(profileIV).onLoadStarted(getDrawable(R.drawable.profilepic_placeholder));

            Picasso.with(getBaseContext()).load(ApiConstant.profilepic + profilepic).placeholder(R.drawable.profilepic_placeholder).into(profileIV);

        } else {
            profileIV.setImageResource(R.drawable.profilepic_placeholder);

        }


        if (intent != null) {

            to = intent.getStringExtra("for");
        }


        if (to.equals("text")) {
            typepost = "status";
            Uploadiv.setVisibility(View.GONE);
            displayRecordedVideo.setVisibility(View.GONE);
        } else if (to.equals("image")) {
            typepost = "image";

            Uploadiv.setVisibility(View.VISIBLE);
            displayRecordedVideo.setVisibility(View.GONE);
            selectImage();
        } else if (to.equals("video")) {

            typepost = "video";
            Uploadiv.setVisibility(View.VISIBLE);
            displayRecordedVideo.setVisibility(View.GONE);
            imgPlay.setVisibility(View.VISIBLE);
            selectVideo();
        }

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);

        final TextWatcher txwatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start,
                                          int count, int after) {
                int tick = start + after;
                if (tick < 128) {
                    int remaining = 500 - tick;
                    // txtcount1.setText(String.valueOf(remaining));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                txtcount1.setText(String.valueOf(500 - s.length()));

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                System.out.print("Hello");
            }
        };

        postEt.addTextChangedListener(txwatcher);


        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayRecordedVideo.pause();
                displayRecordedVideo.setEnabled(false);

                String data = postEt.getText().toString();
//                if (data.equals("")) {
//
//                } else {
                picturePath = appDelegate.getPostImagePath();


                if (typepost.equalsIgnoreCase("status")) {
                    if (data.isEmpty()) {
                        Toast.makeText(PostActivityVideo.this, "Please Enter your Post", Toast.LENGTH_SHORT).show();
                    } else {
                        showProgress();
                        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), typepost);
                        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), apikey);
                        RequestBody eventid = RequestBody.create(MediaType.parse("text/plain"), eventId);
                        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), StringEscapeUtils.escapeJava(data));
                        MultipartBody.Part body = null;

                        postFeed(type, token, eventid, status, body);

                        if (file != null) {

                            if (typepost.equals("image")) {
                                ProgressRequestBodyImage reqFile = new ProgressRequestBodyImage(file, PostActivityVideo.this);
                                body = MultipartBody.Part.createFormData("media_file", file.getName(), reqFile);
                                postFeed(type, token, eventid, status, body);
                            } else if (typepost.equals("video")) {
                                ProgressRequestBodyVideo reqFile = new ProgressRequestBodyVideo(file, PostActivityVideo.this);
                                body = MultipartBody.Part.createFormData("media_file_thumb", file.getName(), reqFile);
                                postFeed(type, token, eventid, status, body);
                            }
                        }/* else {

                            Toast.makeText(PostViewActivity.this, "Please Enter your Post", Toast.LENGTH_SHORT).show();

                        }*/


                    }
                } else {
                    if (typepost.equals("image")) {
                        if (appDelegate.getPostImagePath() != null
                                && appDelegate.getPostImagePath().length() > 0) {
                            System.out
                                    .println("Post Image URL  inside SubmitPostTask :"
                                            + appDelegate.getPostImagePath());

                            appDelegate.setPostImagePath("");

                            RequestBody type = RequestBody.create(MediaType.parse("text/plain"), typepost);
                            RequestBody token = RequestBody.create(MediaType.parse("text/plain"), apikey);
                            RequestBody eventid = RequestBody.create(MediaType.parse("text/plain"), eventId);
                            RequestBody status = RequestBody.create(MediaType.parse("text/plain"), StringEscapeUtils.escapeJava(data));
                            MultipartBody.Part body = null;


                            if (file != null) {
                                showProgress();

                                ProgressRequestBodyImage reqFile = new ProgressRequestBodyImage(file, PostActivityVideo.this);
                                body = MultipartBody.Part.createFormData("media_file", file.getName(), reqFile);
                                postFeed(type, token, eventid, status, body);

                            } else {
                                Toast.makeText(PostActivityVideo.this, "Please Select any Image", Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Toast.makeText(PostActivityVideo.this, "Please Select any Image", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }
                    if (typepost.equals("video")) {

                        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), typepost);
                        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), apikey);
                        RequestBody eventid = RequestBody.create(MediaType.parse("text/plain"), eventId);

                        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), StringEscapeUtils.escapeJava(data));
                        MultipartBody.Part body = null;
                        MultipartBody.Part body1 = null;
//
                        if (file != null) {
//
                            MediaMetadataRetriever m = new MediaMetadataRetriever();

                            try {


                                m.setDataSource(file.getAbsolutePath());

                                Bitmap bit = m.getFrameAtTime();
                                String filename = String.valueOf(System.currentTimeMillis()) + ".png";
                                File sd = Environment.getExternalStorageDirectory();
                                File dest = new File(sd, filename);


                                try {
                                    FileOutputStream out = new FileOutputStream(dest);
                                    bit.compress(Bitmap.CompressFormat.PNG, 90, out);
                                    out.flush();
                                    out.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                File file1 = new File(dest.getAbsolutePath());

                                showProgress();

                                ProgressRequestBodyVideo reqFile = new ProgressRequestBodyVideo(file, PostActivityVideo.this);
                                body = MultipartBody.Part.createFormData("media_file", file.getName(), reqFile);

                                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file1);
                                body1 = MultipartBody.Part.createFormData("media_file_thumb", file1.getName(), requestFile);


                                postFeedVideo(type, token, eventid, status, body, body1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            new SubmitPostTask().execute();
                        } else {
                            Toast.makeText(PostActivityVideo.this, "Please Select any Video", Toast.LENGTH_SHORT).show();

                        }


                    }

                }
//                }
            }
        });

        displayRecordedVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                imgPlay.setImageResource(R.drawable.ic_media_play);
                imgPlay.setVisibility(View.VISIBLE);
            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                displayRecordedVideo.setVisibility(View.VISIBLE);
                Uploadiv.setVisibility(View.GONE);

                if (!displayRecordedVideo.isPlaying()) {

                    try {
                        // Start the MediaController

                        imgPlay.setImageResource(R.drawable.ic_media_pause);

                        //  mediacontrolle.setAnchorView(videoview);
                        // Get the URL from String VideoURL

                        displayRecordedVideo.start();
                        imgPlay.setVisibility(View.GONE);


                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }

                } else {
                    displayRecordedVideo.pause();

                    imgPlay.setImageResource(R.drawable.ic_media_play);
                    imgPlay.setVisibility(View.VISIBLE);
                }

            }
        });

    }


    public void postFeed(RequestBody type, RequestBody api_access_token, RequestBody eventid, RequestBody status, MultipartBody.Part fbody) {
        mAPIService.PostNewsFeed(type, api_access_token, eventid, status, fbody).enqueue(new Callback<PostTextFeed>() {
            @Override
            public void onResponse(Call<PostTextFeed> call, Response<PostTextFeed> response) {
                Log.i("hit", "post submitted to API." + response.body().toString());
                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponse(response);
                } else {
                    dismissProgress();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PostTextFeed> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Log.e("hit", t.getMessage());
                finish();
                dismissProgress();
            }
        });
    }

    public void postFeedVideo(RequestBody type, RequestBody api_access_token, RequestBody eventid, RequestBody status, MultipartBody.Part fbody, MultipartBody.Part thumb) {
        mAPIService.PostNewsFeed(type, api_access_token, eventid, status, fbody, thumb).enqueue(new Callback<PostTextFeed>() {
            @Override
            public void onResponse(Call<PostTextFeed> call, Response<PostTextFeed> response) {
                Log.i("hit", "post submitted to API." + response.body().toString());
                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponse(response);
                } else {
                    dismissProgress();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PostTextFeed> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Log.e("hit", t.getMessage());
                finish();
                dismissProgress();
            }
        });
    }


    public void showResponse(Response<PostTextFeed> response) {

        if (response.body().getStatus().equals("success")) {

            Intent i = new Intent(this, MrgeHomeActivity.class);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(PostActivityVideo.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result) {

//                        Calendar cal = Calendar.getInstance();
//                        File file = new File(Environment.getExternalStorageDirectory(), (cal.getTimeInMillis() + ".jpg"));
//                        if (!file.exists()) {
//                            try {
//                                file.createNewFile();
//                            } catch (IOException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        } else {
//                            file.delete();
//                            try {
//                                file.createNewFile();
//                            } catch (IOException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        }
//                        capturedImageUri = Uri.fromFile(file);
//
//
//                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//                        StrictMode.setVmPolicy(builder.build());
//
//                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
//                        startActivityForResult(cameraIntent, REQUEST_CAMERA);

//                        cameraIntent();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            // Android M Permission check
                            if (PostActivityVideo.this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED && PostActivityVideo.this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                                final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
                                final String[] permissionswrite = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
                                ActivityCompat.requestPermissions(PostActivityVideo.this, permissionswrite, 0);
                                ActivityCompat.requestPermissions(PostActivityVideo.this, permissions, 0);
                            } else {

                                cameraTask();

                            }

                        } else {
                            cameraTask();

                        }
                    }

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result) {
                        videogalleryIntent();
                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
//                    Intent intent = new Intent(PostActivityVideo.this, MrgeHomeActivity.class);
//                    startActivity(intent);
                    finish();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void cameraTask() {


        Log.i("android", "startStorage");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            // Android M Permission check
            if (this.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(PostActivityVideo.this,
                        permissions, 1);
            } else {

                //startCamera();
                //  captureImage();
                dispatchTakePictureIntent();

            }

        } else {
            //startCamera();
            // captureImage();
            dispatchTakePictureIntent();

        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        file = null;
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            file = null;
            try {
                file = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID+".android.fileprovider",
                        file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        } else {
            file = null;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void selectVideo() {
        final CharSequence[] items = {"Take Video", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Video!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(PostActivityVideo.this);

//                recorderTask(1);

                if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        videogalleryIntent();

                } else if (items[item].equals("Take Video")) {
                    userChoosenTask = "Take Video";
                    if (result) {
//                        Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
////                        videoCaptureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
//////                        videoCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); //0 means low & 1 means high
////                        if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
////                            startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
////                        }
                        recorderTask(1);
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    Intent intent = new Intent(PostActivityVideo.this, MrgeHomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void recorderTask(int imgPos) {


        Log.i("android", "startStorage");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            // Android M Permission check
            if (this.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(PostActivityVideo.this,
                        permissions, 2);
            } else {


                Toast.makeText(PostActivityVideo.this, "Record a video not more than 15 seconds",
                        Toast.LENGTH_SHORT).show();


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mediaFile =
                                    new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                            + "/" + System.currentTimeMillis() + ".mp4");


                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
                            intent.putExtra("EXTRA_VIDEO_QUALITY", 0);
                            uri = Uri.fromFile(mediaFile);


                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);


            }

        } else {
            Toast.makeText(PostActivityVideo.this, "Record a video not more than 15 seconds",
                    Toast.LENGTH_SHORT).show();


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        mediaFile =
                                new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                        + "/" + System.currentTimeMillis() + ".mp4");


                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
                        intent.putExtra("EXTRA_VIDEO_QUALITY", 0);
                        uri = Uri.fromFile(mediaFile);


                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 2000);


        }


    }

    private void imagegalleryIntent() {
        Intent intent_upload = new Intent();
        intent_upload.setType("video/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent_upload, "Select File"), SELECT_FILE);
    }

    private void videogalleryIntent() {
//        Intent intent = new Intent();
//        intent.setType("*/*");
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/*"});
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_TAKE_GALLERY_VIDEO);
    }

    private void setpic2() {

        if (mCurrentPhotoPath != null) {

            Uploadiv.setVisibility(View.VISIBLE);
            //selfieSubmit.setVisibility(View.VISIBLE);
            //edtImagename.setVisibility(View.VISIBLE);


            // String compressedImagePath = compressImage(mCurrentPhotoPath);
            String compressedImagePath = mCurrentPhotoPath;
            appDelegate.setPostImagePath(compressedImagePath);


            Glide.with(this).load(compressedImagePath).into(Uploadiv);


            Toast.makeText(PostActivityVideo.this, "Image selected",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PostActivityVideo.this, "Please select any image",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        // by setting this field as true, the actual bitmap pixels are not
        // loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        // max Height and width values of the compressed image is taken as
        // 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        // setting inSampleSize value allows to load a scaled down version of
        // the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low
        // on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        // check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();

        try {
            out = new FileOutputStream(filename);

            // write the compressed bitmap at the destination specified by
            // filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            setpic2();

        } else if (resultCode == RESULT_OK && requestCode == SELECT_FILE && data.getData() != null) {

//            onSelectFromGalleryResult(data);

            uri = data.getData();

            ArrayList<String> supportedMedia = new ArrayList<String>();

            supportedMedia.add(".mp4");
            supportedMedia.add(".mov");
            supportedMedia.add(".3gp");


            videoUrl = ScalingUtilities.getPath(PostActivityVideo.this, data.getData());
            file = new File(videoUrl);

            String fileExtnesion = videoUrl.substring(videoUrl.lastIndexOf("."));


            if (supportedMedia.contains(fileExtnesion)) {


                long file_size = Integer.parseInt(String.valueOf(file.length()));
                long fileMb = bytesToMeg(file_size);


                //if (fileMb >= 16)
                // Toast.makeText(VideoPost.this, "Upload a video not more than 15 MB in size",
                //        Toast.LENGTH_SHORT).show();

                //  else {
                try {
                    MediaPlayer mplayer = new MediaPlayer();
                    mplayer.reset();
                    mplayer.setDataSource(videoUrl);
                    mplayer.prepare();

                    long totalFileDuration = mplayer.getDuration();
                    Log.i("android", "data is " + totalFileDuration);


                    int sec = (int) ((totalFileDuration / (1000)));

                    Log.i("android", "data is " + sec);


                    if (sec > 15) {
                        Toast.makeText(PostActivityVideo.this, "Select an video not more than 15 seconds",
                                Toast.LENGTH_SHORT).show();

                    } else {


                        //llPost.setVisibility(View.VISIBLE);
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(videoUrl);

                        Uri video = Uri.parse(videoUrl);


                        // videoview.setMediaController(mediacontrolle);
                        displayRecordedVideo.setVideoURI(video);


                        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                        imgeFile = createDirectoryAndSaveFile(bitmap);

                        Bitmap thumbnail = mediaMetadataRetriever.getFrameAtTime();

                        Uploadiv.setImageBitmap(thumbnail);
                        imgPlay.setVisibility(View.VISIBLE);
                        Uploadiv.setVisibility(View.VISIBLE);


                        MediaMetadataRetriever m = new MediaMetadataRetriever();

                        m.setDataSource(videoUrl);
                        //  Bitmap thumbnail = m.getFrameAtTime();
//
                        if (Build.VERSION.SDK_INT >= 17) {
                            angle = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);

                            //  Log.e("Rotation", s);
                        }


                        Toast.makeText(PostActivityVideo.this, "Video selected",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {


                    Log.i("android", "exception is " + e.getLocalizedMessage() + " " + e.getStackTrace());

                }
            }

        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE && data.getData() != null) {
            uri = data.getData();

            ArrayList<String> supportedMedia = new ArrayList<String>();

            supportedMedia.add(".mp4");
            supportedMedia.add(".mov");
            supportedMedia.add(".3gp");


            videoUrl = ScalingUtilities.getPath(PostActivityVideo.this, data.getData());
            file = new File(videoUrl);

            String fileExtnesion = videoUrl.substring(videoUrl.lastIndexOf("."));


            if (supportedMedia.contains(fileExtnesion)) {


                long file_size = Integer.parseInt(String.valueOf(file.length()));
                long fileMb = bytesToMeg(file_size);


                //if (fileMb >= 16)
                // Toast.makeText(VideoPost.this, "Upload a video not more than 15 MB in size",
                //        Toast.LENGTH_SHORT).show();

                //  else {
                try {
                    MediaPlayer mplayer = new MediaPlayer();
                    mplayer.reset();
                    mplayer.setDataSource(videoUrl);
                    mplayer.prepare();

                    long totalFileDuration = mplayer.getDuration();
                    Log.i("android", "data is " + totalFileDuration);


                    int sec = (int) ((totalFileDuration / (1000)));

                    Log.i("android", "data is " + sec);


                    if (sec > 15) {
                        Toast.makeText(PostActivityVideo.this, "Select an video not more than 15 seconds",
                                Toast.LENGTH_SHORT).show();

                    } else {


                        //llPost.setVisibility(View.VISIBLE);
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(videoUrl);

                        Uri video = Uri.parse(videoUrl);


                        // videoview.setMediaController(mediacontrolle);
                        displayRecordedVideo.setVideoURI(video);


                        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                        imgeFile = createDirectoryAndSaveFile(bitmap);

                        Bitmap thumbnail = mediaMetadataRetriever.getFrameAtTime();

                        Uploadiv.setImageBitmap(thumbnail);
                        imgPlay.setVisibility(View.VISIBLE);
                        Uploadiv.setVisibility(View.VISIBLE);


                        MediaMetadataRetriever m = new MediaMetadataRetriever();

                        m.setDataSource(videoUrl);
                        //  Bitmap thumbnail = m.getFrameAtTime();
//
                        if (Build.VERSION.SDK_INT >= 17) {
                            angle = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);

                            //  Log.e("Rotation", s);
                        }


                        Toast.makeText(PostActivityVideo.this, "Video selected",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {


                    Log.i("android", "exception is " + e.getLocalizedMessage() + " " + e.getStackTrace());

                }


            } else {


                Toast.makeText(PostActivityVideo.this, "Only .mp4,.mov,.3gp File formats allowed ", Toast.LENGTH_SHORT).show();

            }

        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_GALLERY_VIDEO && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            // OI FILE Manager
            selectedImagePath = selectedImageUri.getPath();

            // MEDIA GALLERY
            selectedImagePath = getPath(PostActivityVideo.this, selectedImageUri);
            if (selectedImagePath != null) {

                displayRecordedVideo.setVideoURI(selectedImageUri);
                displayRecordedVideo.start();
                uri = selectedImageUri;


                try {
                    if (uri != null) {

                        MediaPlayer mp = MediaPlayer.create(this, uri);
                        int duration = mp.getDuration();
                        mp.release();

                        if ((duration / 1000) > 15) {
                            // Show Your Messages
                            Toast.makeText(PostActivityVideo.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PostActivityVideo.this, MrgeHomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //Store the video to your server


//                                    pathToStoredVideo = getRealPathFromURIPathVideo(data.getData(),PostActivityVideo.this);
                            if (Build.VERSION.SDK_INT > 22) {
                                pathToStoredVideo = ImagePath_MarshMallow.getPath(PostActivityVideo.this, uri);
                                Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                            } else {
                                //else we will get path directly
                                pathToStoredVideo = uri.getPath();

                                Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                            }
                            file = new File(pathToStoredVideo);

                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            finish();
        }

        if (data != null) {
            if (data.getData() != null) {

//            Uri selectedMediaUri = (Uri) data.getExtras().get("data");
                Uri selectedMediaUri = data.getData();
                if (selectedMediaUri != null) {
                    if (selectedMediaUri.toString().contains("image")) {

                        Uploadiv.setVisibility(View.VISIBLE);
                        displayRecordedVideo.setVisibility(View.GONE);

                        if (resultCode == RESULT_OK) {
                            if (requestCode == SELECT_FILE)
                                onSelectFromGalleryResult(data);

                        }
                    } else if (selectedMediaUri.toString().contains("video")) {

                        if (data != null && data.getStringExtra("video") != null)

                            Uploadiv.setVisibility(View.VISIBLE);
                        Uploadiv.setVisibility(View.VISIBLE);
                        displayRecordedVideo.setVisibility(View.GONE);
                        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                            Uri selectedImageUri = data.getData();


                            // OI FILE Manager
                            selectedImagePath = selectedImageUri.getPath();

                            // MEDIA GALLERY
                            selectedImagePath = getPath(PostActivityVideo.this, selectedImageUri);
                            if (selectedImagePath != null) {

//                            displayRecordedVideo.setVideoURI(selectedImageUri);
//                            displayRecordedVideo.start();
                                uri = selectedImageUri;
                                try {
                                    if (uri != null) {

                                        MediaPlayer mp = MediaPlayer.create(this, uri);
                                        int duration = mp.getDuration();
                                        mp.release();

                                        if ((duration / 1000) > 15) {
                                            // Show Your Messages
                                            Toast.makeText(PostActivityVideo.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(PostActivityVideo.this, MrgeHomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            //Store the video to your server


//                                    pathToStoredVideo = getRealPathFromURIPathVideo(data.getData(),PostActivityVideo.this);
                                            if (Build.VERSION.SDK_INT > 22) {
                                                pathToStoredVideo = ImagePath_MarshMallow.getPath(PostActivityVideo.this, uri);
                                                Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                                            } else {
                                                //else we will get path directly
                                                pathToStoredVideo = uri.getPath();

                                                Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                                            }
                                            file = new File(pathToStoredVideo);

                                        }
                                    } else {

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Bitmap b = ThumbnailUtils.createVideoThumbnail(selectedImagePath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                                Uploadiv.setImageBitmap(b);
                            }
                        } /*else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
                            uri = data.getData();

//                        displayRecordedVideo.setVideoURI(uri);
//                        displayRecordedVideo.start();


                            if (Build.VERSION.SDK_INT > 22)
                                pathToStoredVideo = ImagePath_MarshMallow.getPath(PostActivityVideo.this, uri);
                            else
                                //else we will get path directly
                                pathToStoredVideo = uri.getPath();
                            Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                            //Store the video to your server
                            file = new File(pathToStoredVideo);
                            Bitmap b = ThumbnailUtils.createVideoThumbnail(pathToStoredVideo, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                            Uploadiv.setImageBitmap(b);
                        } */ else if (resultCode == Activity.RESULT_OK) {
                            Uploadiv.setVisibility(View.VISIBLE);
                            imgPlay.setVisibility(View.VISIBLE);
                            displayRecordedVideo.setVisibility(View.GONE);

                            uri = data.getData();
//                        displayRecordedVideo.setVideoURI(uri);
//                        displayRecordedVideo.start();
                            try {
                                if (uri != null) {

                                    MediaPlayer mp = MediaPlayer.create(this, uri);
                                    int duration = mp.getDuration();
                                    mp.release();

                                    if ((duration / 1000) > 15) {
                                        // Show Your Messages
                                        Toast.makeText(PostActivityVideo.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //Store the video to your server


//                                    pathToStoredVideo = getRealPathFromURIPathVideo(data.getData(),PostActivityVideo.this);
                                        if (Build.VERSION.SDK_INT > 22) {
                                            pathToStoredVideo = ImagePath_MarshMallow.getPath(PostActivityVideo.this, uri);
                                            Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                                        } else {
                                            //else we will get path directly
                                            pathToStoredVideo = uri.getPath();
                                            Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                                        }
                                        file = new File(pathToStoredVideo);

                                        Bitmap b = ThumbnailUtils.createVideoThumbnail(pathToStoredVideo, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                                        Uploadiv.setImageBitmap(b);

                                    }
                                } else {

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            } else {
                finish();
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA) {

            if (Uploadiv.getVisibility() == View.GONE) {
                Uploadiv.setVisibility(View.VISIBLE);
                displayRecordedVideo.setVisibility(View.GONE);
            }
            file = new File(capturedImageUri.getPath());
            Uploadiv.setImageURI(capturedImageUri);

        } else {

            finish();
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Uploadiv.setImageBitmap(bm);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), bm);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                file = new File(getRealPathFromURIGallery(tempUri));
                appDelegate.setPostImagePath(getRealPathFromURIGallery(tempUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Uploadiv.setImageBitmap(bm);
    }

    public String getRealPathFromURIGallery(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor1 = getContentResolver().query(contentUri,
                filePathColumn, null, null, null);
        cursor1.moveToFirst();

        int columnIndex = cursor1.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor1.getString(columnIndex);

        //String compressedImagePath = compressImage(picturePath);
        appDelegate.setPostImagePath(picturePath);

        Cursor cursor = getContentResolver().query(contentUri, null, null,
                null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public String getPath(Context context, Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(idx);
    }


    public void showProgress() {
        postbtn.setEnabled(false);
        postEt.setEnabled(false);
        progressbar.setVisibility(View.VISIBLE);
        progressbar.setProgress(0);
        progressbar.setMaxValue(100);
        progressbar.setProgressColor(Color.parseColor(colorActive));
        progressbar.setText(String.valueOf(0));
        progressbar.setTextColor("#ffffff");
        progressbar.setSuffix("%");
        progressbar.setPrefix("");
    }

    public void dismissProgress() {
        postbtn.setEnabled(true);
        postEt.setEnabled(true);
        if (progressbar.getVisibility() == View.VISIBLE) {
            progressbar.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    @Override
    public void onProgressUpdate(int percentage) {
        Log.e("per", String.valueOf(percentage));
//        progressbar.showValue(99f, 100f, true);
        progressbar.setProgress(percentage);
        progressbar.setText(String.valueOf(percentage));
    }

    @Override
    public void onError() {
        dismissProgress();
    }

    @Override
    public void onFinish() {

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MrgeHomeActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                cameraTask();


            } else {
                Toast.makeText(this, "No permission to read external storage.",
                        Toast.LENGTH_SHORT).show();

            }
        } else if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(PostActivityVideo.this, "Record a video not more than 15 seconds",
                        Toast.LENGTH_SHORT).show();


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mediaFile =
                                    new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                            + "/" + System.currentTimeMillis() + ".mp4");


                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
                            intent.putExtra("EXTRA_VIDEO_QUALITY", 0);
                            uri = Uri.fromFile(mediaFile);


                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);


            } else {
                Toast.makeText(this, "Permission not granted",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }

    private static final long MEGABYTE = 1024L * 1024L;

    public static long bytesToMeg(long bytes) {
        return bytes / MEGABYTE;
    }

    public static File createDirectoryAndSaveFile(Bitmap imageToSave) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/MyFolder/Images");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/MyFolder/Images/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/MyFolder/Images/"), System.currentTimeMillis() + ".jpg");
        if (file.exists()) {
            file.delete();
        }


        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;

    }

    public class SubmitPostTask extends AsyncTask<String, String, JSONObject> {

        String json1 = "";
        InputStream is = null;
        JSONObject json = null;
        JSONObject status;
        ProgressDialog pDialog;

        String message = "";

        String error = "";
        String msg = "";

        String res = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PostActivityVideo.this);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
           /* try {


                if (picturePath != null && !(picturePath.equalsIgnoreCase(""))) {
                    sourceFile = new File(picturePath);
                }


                //Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());

                final MediaType MEDIA_TYPE_PNG = MediaType.parse("video*//*");

               // String filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);


                OkHttpClient client = null;
                try {

                    URL url = new URL(postUrl);
                    // SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(url);


                    client = getUnsafeOkHttpClient().newBuilder().build();

                } catch (Exception e) {
                    e.printStackTrace();

                }

                MultipartBody.Builder builder = new MultipartBody.Builder();

                builder.setType(MultipartBody.FORM);
                *//*if (picturePath != null && !(picturePath.equalsIgnoreCase(""))) {
                    builder.addFormDataPart("wall_image", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile));
                }
*//*
                String filename = videoUrl.substring(videoUrl.lastIndexOf("/") + 1);

                //if (videoUrl != null && !(videoUrl.equalsIgnoreCase(""))) {
                    builder.addFormDataPart("wall_image", filename, RequestBody.create(MEDIA_TYPE_PNG, videoFile));
                //}

                builder.addFormDataPart("api_access_token", accessToken);
                builder.addFormDataPart("event_id", eventId);
                //builder.addFormDataPart("eventId", "1");

                builder.addFormDataPart("user_type", type_of_user);
                builder.addFormDataPart("status", postMsg);

                RequestBody requestBody = builder.build();


                Request request = new Request.Builder()
                        .url(postUrl)
                        .post(requestBody)
                        .build();

                HttpEntity httpEntity = null;
                Response response = null;


                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //res = response.body().string();
                httpEntity = transformResponse(response).getEntity();
                res = EntityUtils.toString(httpEntity);

            } catch (IOException e) {
                e.printStackTrace();
            }

            // try parse the string to a JSON object
            try {
                json = new JSONObject(res);
                error = json.getString("status");
                message = json.getString("msg");
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());

            }*/
            try {


                //Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());


                final MediaType MEDIA_TYPE_PNG = MediaType.parse("video/*");


                OkHttpClient client = null;
                try {

                    URL url = new URL(params[0]);
                    // SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(url);


                    client = getUnsafeOkHttpClient().newBuilder().build();

                } catch (Exception e) {
                    e.printStackTrace();

                }

                MultipartBody.Builder builder = new MultipartBody.Builder();

                builder.setType(MultipartBody.FORM);


                builder.addFormDataPart("api_access_token", apikey);
                builder.addFormDataPart("event_id", eventId);

//                builder.addFormDataPart("user_type", type_of_user);
                builder.addFormDataPart("status", StringEscapeUtils.escapeJava(postEt.getText().toString()));
                builder.addFormDataPart("angle", angle);


                String filename = videoUrl.substring(videoUrl.lastIndexOf("/") + 1);

                if (videoUrl != null && !(videoUrl.equalsIgnoreCase(""))) {
                    builder.addFormDataPart("wall_image", filename, RequestBody.create(MEDIA_TYPE_PNG, file));
                }


                String filename1 = imgeFile.getAbsolutePath().substring(imgeFile.getAbsolutePath().lastIndexOf("/") + 1);
                builder.addFormDataPart("thumbnail", filename1, RequestBody.create(MEDIA_TYPE_PNG, imgeFile));


                RequestBody requestBody = builder.build();


                Request request = new Request.Builder()
                        .url(ApiConstant.baseUrl + "PostNewsFeed")
                        .post(requestBody)
                        .build();

                HttpEntity httpEntity = null;
                okhttp3.Response response = null;


                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //res = response.body().string();
                httpEntity = transformResponse(response).getEntity();
                res = EntityUtils.toString(httpEntity);

            } catch (Exception e) {
                e.printStackTrace();
            }

//            {"status":"success","msg":"Posted Sucessfully"}
            // try parse the string to a JSON object
            try {
                json = new JSONObject(res);
                error = json.getString("status");
                message = json.getString("msg");
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());

            } catch (Exception e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());

            }


            return json;

        }

        @Override
        protected void onPostExecute(JSONObject result) {

            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }

            if (error.equalsIgnoreCase("success")) {

                // notifyDataSetChanged();


            }


        }

    }

    private static HttpResponse transformResponse(okhttp3.Response response) {

        BasicHttpResponse httpResponse = null;
        try {
            int code = 0;
            if (response != null)
                code = response.code();


            try {
                String message = response.message();
                httpResponse = new BasicHttpResponse(HTTP_1_1, code, message);

                ResponseBody body = response.body();
                InputStreamEntity entity = new InputStreamEntity(body.byteStream(), body.contentLength());
                httpResponse.setEntity(entity);

                Headers headers = response.headers();
                for (int i = 0, size = headers.size(); i < size; i++) {
                    String name = headers.name(i);
                    String value = headers.value(i);
                    httpResponse.addHeader(name, value);
                    if ("Content-Type".equalsIgnoreCase(name)) {
                        entity.setContentType(value);
                    } else if ("Content-Encoding".equalsIgnoreCase(name)) {
                        entity.setContentEncoding(value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return httpResponse;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {

        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient client = new OkHttpClient();

        OkHttpClient.Builder builder = client.newBuilder();
        builder.sslSocketFactory(sslSocketFactory);
        builder.connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;


            }
        });

        return builder.build();

    }

}
