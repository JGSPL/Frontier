package com.procialize.mrgeApp20.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.percolate.mentions.Mentionable;
import com.percolate.mentions.Mentions;
import com.percolate.mentions.QueryListener;
import com.percolate.mentions.SuggestionsListener;
import com.procialize.mrgeApp20.Adapter.UsersAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.CustomTools.CircleDisplay;
import com.procialize.mrgeApp20.CustomTools.ImagePath_MarshMallow;
import com.procialize.mrgeApp20.CustomTools.ProgressRequestBodyImage;
import com.procialize.mrgeApp20.CustomTools.ProgressRequestBodyVideo;
import com.procialize.mrgeApp20.CustomTools.RecyclerItemClickListener;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.GetterSetter.Comment;
import com.procialize.mrgeApp20.GetterSetter.Mention;
import com.procialize.mrgeApp20.GetterSetter.PostTextFeed;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.MyApplication;
import com.procialize.mrgeApp20.Utility.Util;
import com.procialize.mrgeApp20.Utility.Utility;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostViewActivity extends AppCompatActivity implements ProgressRequestBodyImage.UploadCallbacks, ProgressRequestBodyVideo.UploadCallbacks, QueryListener, SuggestionsListener {

    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private static final int READ_REQUEST_CODE = 200;
    private static final String SERVER_PATH = "";
    EditText postEt;
    // Button postbtn;
    TextView postbtn, txtcount1, txtcount;
    APIService mAPIService;
    CircleDisplay progressbar;
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
    private List<AttendeeList> userList;
    private Mentions mentions;
    TextView textData;
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    List<AttendeeList> customers = null;
    private SQLiteDatabase db;
    private UsersAdapter usersAdapter;
    RelativeLayout relative;

    String data;

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
        relative = findViewById(R.id.relative);
        Util.logomethod(this, headerlogoIv);
        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
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
        postEt = findViewById(R.id.posttextEt);
        txtcount1 = findViewById(R.id.txtcount1);
        txtcount = findViewById(R.id.txtcount);
        postbtn = findViewById(R.id.postbtn);
        Uploadiv = findViewById(R.id.Uploadiv);
        profileIV = findViewById(R.id.profileIV);
        displayRecordedVideo = findViewById(R.id.Upvideov);
        imgPlay = findViewById(R.id.imgPlay);
        progressbar = findViewById(R.id.progressbar);
        textData = findViewById(R.id.textData);
        txtcount1.setTextColor(Color.parseColor(colorActive));
        txtcount.setTextColor(Color.parseColor(colorActive));

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

        procializeDB = new DBHelper(PostViewActivity.this);
        db = procializeDB.getReadableDatabase();
        dbHelper = new DBHelper(PostViewActivity.this);
        customers = new ArrayList<AttendeeList>();
        userList = procializeDB.getAttendeeDetails();
        procializeDB.getReadableDatabase();
        // attendeesList = (ListView)
        // getActivity().findViewById(R.id.speakers_list);
        customers = dbHelper.getAttendeeDetails();
        mentions = new Mentions.Builder(this, postEt)
                .suggestionsListener(this)
                .queryListener(this)
                .build();

        setupMentionsList();

        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayRecordedVideo.pause();
                displayRecordedVideo.setEnabled(false);

                data = postEt.getText().toString();
//            postMsg = StringEscapeUtils.escapeJava(postEt.getText().toString().trim());
                // post_status_post.setText("");
                // post_status_post
                // .setHint("What's on your mind (Not more than 500 characters)");

                final Comment comment = new Comment();
                comment.setComment(data);
                comment.setMentions(mentions.getInsertedMentions());
                textData.setText(data);

                data = highlightMentions(textData, comment.getMentions());
//                if (data.equals("")) {
//
//                } else {
                picturePath = appDelegate.getPostImagePath();


                if (typepost.equalsIgnoreCase("status")) {
                    if (data.isEmpty()) {
                        Toast.makeText(PostViewActivity.this, "Please Enter your Post", Toast.LENGTH_SHORT).show();
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
                                ProgressRequestBodyImage reqFile = new ProgressRequestBodyImage(file, PostViewActivity.this);
                                body = MultipartBody.Part.createFormData("media_file", file.getName(), reqFile);
                                postFeed(type, token, eventid, status, body);
                            } else if (typepost.equals("video")) {
                                ProgressRequestBodyVideo reqFile = new ProgressRequestBodyVideo(file, PostViewActivity.this);
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

                                ProgressRequestBodyImage reqFile = new ProgressRequestBodyImage(file, PostViewActivity.this);
                                body = MultipartBody.Part.createFormData("media_file", file.getName(), reqFile);
                                postFeed(type, token, eventid, status, body);

                            } else {
                                Toast.makeText(PostViewActivity.this, "Please Select any Image", Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Toast.makeText(PostViewActivity.this, "Please Select any Image", Toast.LENGTH_SHORT).show();
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

                        if (file != null) {

                            MediaMetadataRetriever m = new MediaMetadataRetriever();

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

                            ProgressRequestBodyVideo reqFile = new ProgressRequestBodyVideo(file, PostViewActivity.this);
                            body = MultipartBody.Part.createFormData("media_file", file.getName(), reqFile);

                            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file1);
                            body1 = MultipartBody.Part.createFormData("media_file_thumb", file1.getName(), requestFile);


                            postFeedVideo(type, token, eventid, status, body, body1);
                        } else {
                            Toast.makeText(PostViewActivity.this, "Please Select any Video", Toast.LENGTH_SHORT).show();

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

                if (response.body().getMsg().equalsIgnoreCase("success")) {
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
//                Log.e("hit", "Unable to submit post to API.");
//                Log.e("hit", t.getMessage());
//                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                dismissProgress();
                finish();
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

            Intent i = new Intent(this, HomeActivity.class);
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
                boolean result = Utility.checkPermission(PostViewActivity.this);

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
                            if (PostViewActivity.this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED && PostViewActivity.this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                                final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
                                final String[] permissionswrite = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
                                ActivityCompat.requestPermissions(PostViewActivity.this, permissionswrite, 0);
                                ActivityCompat.requestPermissions(PostViewActivity.this, permissions, 0);
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
                        imagegalleryIntent();
                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
//                    Intent intent = new Intent(PostViewActivity.this, HomeActivity.class);
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
                ActivityCompat.requestPermissions(PostViewActivity.this,
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
                        "com.procialize.eventsapp.android.fileprovider",
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
                boolean result = Utility.checkPermission(PostViewActivity.this);

//                recorderTask(1);

                if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        videogalleryIntent();

                } else if (items[item].equals("Take Video")) {
                    userChoosenTask = "Take Video";
                    if (result) {
                        Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        videoCaptureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
//                        videoCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); //0 means low & 1 means high
                        if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                        }
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    Intent intent = new Intent(PostViewActivity.this, HomeActivity.class);
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
                ActivityCompat.requestPermissions(PostViewActivity.this,
                        permissions, 2);
            } else {


                Toast.makeText(PostViewActivity.this, "Record a video not more than 15 seconds",
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
            Toast.makeText(PostViewActivity.this, "Record a video not more than 15 seconds",
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
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
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


            Toast.makeText(PostViewActivity.this, "Image selected",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PostViewActivity.this, "Please select any image",
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

            onSelectFromGalleryResult(data);

        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE && data.getData() != null) {
            uri = data.getData();

            displayRecordedVideo.setVideoURI(uri);
            displayRecordedVideo.start();


            if (Build.VERSION.SDK_INT > 22)
                pathToStoredVideo = ImagePath_MarshMallow.getPath(PostViewActivity.this, uri);
            else
                //else we will get path directly
                pathToStoredVideo = uri.getPath();
            Log.d("video", "Recorded Video Path " + pathToStoredVideo);
            //Store the video to your server
            file = new File(pathToStoredVideo);

            Bitmap b = ThumbnailUtils.createVideoThumbnail(pathToStoredVideo, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

            Uploadiv.setVisibility(View.VISIBLE);
            Uploadiv.setImageBitmap(b);
            imgPlay.setVisibility(View.VISIBLE);
            displayRecordedVideo.setVisibility(View.GONE);

            uri = data.getData();
//            displayRecordedVideo.setVideoURI(uri);
//            displayRecordedVideo.start();
            try {
                if (uri != null) {


                    MediaPlayer mp = MediaPlayer.create(this, uri);
                    int duration = mp.getDuration();
                    mp.release();

                    if ((duration / 1000) > 15) {
                        // Show Your Messages
                        Toast.makeText(PostViewActivity.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PostViewActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        //Store the video to your server


//                                    pathToStoredVideo = getRealPathFromURIPathVideo(data.getData(),PostViewActivity.this);


                        if (Build.VERSION.SDK_INT > 22) {
                            pathToStoredVideo = ImagePath_MarshMallow.getPath(PostViewActivity.this, uri);
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

            }

        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_GALLERY_VIDEO && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            // OI FILE Manager
            selectedImagePath = selectedImageUri.getPath();

            // MEDIA GALLERY
            selectedImagePath = getPath(PostViewActivity.this, selectedImageUri);
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
                            Toast.makeText(PostViewActivity.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PostViewActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //Store the video to your server


//                                    pathToStoredVideo = getRealPathFromURIPathVideo(data.getData(),PostViewActivity.this);
                            if (Build.VERSION.SDK_INT > 22) {
                                pathToStoredVideo = ImagePath_MarshMallow.getPath(PostViewActivity.this, uri);
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
                            selectedImagePath = getPath(PostViewActivity.this, selectedImageUri);
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
                                            Toast.makeText(PostViewActivity.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(PostViewActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            //Store the video to your server


//                                    pathToStoredVideo = getRealPathFromURIPathVideo(data.getData(),PostViewActivity.this);
                                            if (Build.VERSION.SDK_INT > 22) {
                                                pathToStoredVideo = ImagePath_MarshMallow.getPath(PostViewActivity.this, uri);
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

                                }

                                Bitmap b = ThumbnailUtils.createVideoThumbnail(selectedImagePath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                                Uploadiv.setImageBitmap(b);
                            }
                        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
                            uri = data.getData();

//                        displayRecordedVideo.setVideoURI(uri);
//                        displayRecordedVideo.start();


                            if (Build.VERSION.SDK_INT > 22)
                                pathToStoredVideo = ImagePath_MarshMallow.getPath(PostViewActivity.this, uri);
                            else
                                //else we will get path directly
                                pathToStoredVideo = uri.getPath();
                            Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                            //Store the video to your server
                            file = new File(pathToStoredVideo);
                            Bitmap b = ThumbnailUtils.createVideoThumbnail(pathToStoredVideo, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                            Uploadiv.setImageBitmap(b);
                        } else if (resultCode == Activity.RESULT_OK) {
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
                                        Toast.makeText(PostViewActivity.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //Store the video to your server


//                                    pathToStoredVideo = getRealPathFromURIPathVideo(data.getData(),PostViewActivity.this);
                                        if (Build.VERSION.SDK_INT > 22) {
                                            pathToStoredVideo = ImagePath_MarshMallow.getPath(PostViewActivity.this, uri);
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
            file = new File(mCurrentPhotoPath);
            Uploadiv.setImageURI(Uri.parse(mCurrentPhotoPath));

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
        progressbar.setTextColor(Color.parseColor(colorActive));
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
        Intent i = new Intent(this, HomeActivity.class);
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

                Toast.makeText(PostViewActivity.this, "Record a video not more than 15 seconds",
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

    @Override
    public void onQueryReceived(String s) {
        final List<AttendeeList> users = searchUsers(s);
        if (users != null && !users.isEmpty()) {
            usersAdapter.clear();
            usersAdapter.setCurrentQuery(s);
            usersAdapter.addAll(users);
            showMentionsList(true);
        } else {
            showMentionsList(false);
        }
    }

    @Override
    public void displaySuggestions(boolean b) {
        if (b) {
            com.percolate.caffeine.ViewUtils.showView(this, R.id.mentions_list_layout);
        } else {
            com.percolate.caffeine.ViewUtils.hideView(this, R.id.mentions_list_layout);
        }
    }

    private void showMentionsList(boolean display) {
        com.percolate.caffeine.ViewUtils.showView(this, R.id.mentions_list_layout);
        if (display) {
            com.percolate.caffeine.ViewUtils.showView(this, R.id.mentions_list);
            com.percolate.caffeine.ViewUtils.hideView(this, R.id.mentions_empty_view);
        } else {
            com.percolate.caffeine.ViewUtils.hideView(this, R.id.mentions_list);
            com.percolate.caffeine.ViewUtils.showView(this, R.id.mentions_empty_view);
        }

    }

    private void setupMentionsList() {
        final RecyclerView mentionsList = findViewById(R.id.mentions_list);
        mentionsList.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UsersAdapter(this);
        mentionsList.setAdapter(usersAdapter);
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(postEt.getWindowToken(), 0);
        // set on item click listener
        mentionsList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                final AttendeeList user = usersAdapter.getItem(position);

                /*
                 * We are creating a mentions object which implements the
                 * <code>Mentionable</code> interface this allows the library to set the offset
                 * and length of the mention.
                 */
                if (user != null) {
                    final Mention mention = new Mention();
                    mention.setMentionName(user.getFirstName() + " " + user.getLastName() + " ");
                    mention.setMentionid(user.getAttendeeId());
                    mentions.insertMention(mention);


                }
            }
        }));
    }

    public List<AttendeeList> searchUsers(String query) {
        final List<AttendeeList> searchResults = new ArrayList<>();
        if (StringUtils.isNotBlank(query)) {
            query = query.toLowerCase(Locale.US);
            if (userList != null && !userList.isEmpty()) {
                for (AttendeeList user : userList) {
                    final String firstName = user.getFirstName().toLowerCase();
                    final String lastName = user.getLastName().toLowerCase();
                    if (firstName.startsWith(query) || lastName.startsWith(query)) {
                        searchResults.add(user);
                    }
                }
            }

        }
        return searchResults;
    }


    private String highlightMentions(TextView commentTextView, final List<Mentionable> mentions) {


        String sample = null;
        final SpannableStringBuilder spannable = new SpannableStringBuilder(commentTextView.getText());
        for (int i = 0; i < mentions.size(); i++) {
            if (i == 0) {
                sample = "<" + mentions.get(i).getMentionid() + "^" + mentions.get(i).getMentionName() + ">";
                spannable.setSpan(sample, mentions.get(i).getMentionOffset(), mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.replace(mentions.get(i).getMentionOffset(), mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength(), sample);

                commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
            } else {
                try {
                    sample = "<" + mentions.get(i).getMentionid() + "^" + mentions.get(i).getMentionName() + ">";
                    spannable.setSpan(sample, mentions.get(i).getMentionOffset() + i + i * 6, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i + i * 6 + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.replace(mentions.get(i).getMentionOffset() + i + i * 6, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i + i * 6 + 1, sample);


                    commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
                } catch (Exception e) {
                    sample = "<" + mentions.get(i).getMentionid() + "^" + mentions.get(i).getMentionName() + ">";
                    spannable.setSpan(sample, mentions.get(i).getMentionOffset() + i + i * 6-1, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i + i * 6-3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.replace(mentions.get(i).getMentionOffset() + i + i * 6-1, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i + i * 6-3, sample);


                    commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
                }

            } /*else if (i > 3) {
                try {
                    sample = "<" + mentions.get(i).getMentionid() + "^" + mentions.get(i).getMentionName() + ">";
                    spannable.setSpan(sample, mentions.get(i).getMentionOffset() + i + i * 6, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i + i * 6 + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.replace(mentions.get(i).getMentionOffset() + i + i * 6, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i + i * 6 + 2, sample);

                    commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
                } catch (Exception e) {
                    sample = "<" + mentions.get(i).getMentionid() + "^" + mentions.get(i).getMentionName() + ">";
                    spannable.setSpan(sample, mentions.get(i).getMentionOffset() + i + i * 6, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i + i * 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.replace(mentions.get(i).getMentionOffset() + i + i * 6, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i + i * 6, sample);


                    commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
                }*/
//            }
        }

        return commentTextView.getText().toString();

    }

//    private String highlightMentions(TextView commentTextView, final List<Mentionable> mentions) {
//
//
//        String sample = null;
//        final SpannableStringBuilder spannable = new SpannableStringBuilder(commentTextView.getText());
//        for (int i = 0; i < mentions.size(); i++) {
//            if (i == 0) {
//                sample = "<" + mentions.get(i).getMentionid() + "^" + mentions.get(i).getMentionName() + ">";
//                spannable.setSpan(sample, mentions.get(i).getMentionOffset(), mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                spannable.replace(mentions.get(i).getMentionOffset(), mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength(), sample);
//                commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
//
//            } else {
//                try {
//                    sample = "<" + mentions.get(i).getMentionid() + "^" + mentions.get(i).getMentionName() + ">";
//                    spannable.setSpan(sample, mentions.get(i).getMentionOffset() + i + i * 6 + i - 1, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i + i * 6 + i - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    spannable.replace(mentions.get(i).getMentionOffset() + i + i * 6 + i - 1, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i + i * 6 + i - 1, sample);
//                    commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
//
//                } catch (Exception e) {
//                    sample = "<" + mentions.get(i).getMentionid() + "^" + mentions.get(i).getMentionName() + ">";
//                    spannable.setSpan(sample, mentions.get(i).getMentionOffset() + i + i * 6 - i + 1, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i * 6 - i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    spannable.replace(mentions.get(i).getMentionOffset() + i + i * 6 - i + 1, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i * 6 - i, sample);
//                    commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
//
//                }
////                sample = "<" + mentions.get(i).getMentionid() + "^" + mentions.get(i).getMentionName() + ">";
////                spannable.setSpan(sample, mentions.get(i).getMentionOffset() + i + i * 6, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i + i * 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////                spannable.replace(mentions.get(i).getMentionOffset() + i + i * 6, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i + i * 6, sample);
////
////
////                commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
//            }
//        }
//
//
//        return commentTextView.getText().toString();
//    }

//    private String highlightMentions(TextView commentTextView, final List<Mentionable> mentions) {
//        String sample = null;
//        int flag = 1;
//        int flag2 = 1;
//
//        if (commentTextView != null && mentions != null && !mentions.isEmpty()) {
//            final SpannableStringBuilder spannable = new SpannableStringBuilder(commentTextView.getText());
//            int start;
//            int end;
//
//            for (Mentionable mention : mentions) {
//                if (mention != null) {
//                    if (flag == 1) {
//                        start = mention.getMentionOffset();
//                        end = start + mention.getMentionLength();
//
//                        if (commentTextView.length() >= end) {
//
//
////                        spannable.append(sample, start+1, end+1);
//
//                            sample = "<" + mention.getMentionid() + "^" + commentTextView.getText().toString().substring(start, end) + ">";
//
////                        commentTextView = commentTextView.substring(start, end) + ">" + commentTextView.substring(end, commentTextView.length());
//                            spannable.setSpan(sample, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            spannable.replace(start, end, sample);
//                            commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
//                            flag = 2;
//                        }
//                    } else if (mentions.indexOf(mention) < 3) {
//
//                        /*<23553-Sneha Deshmukh><23371-Anis Ansari><23362-Atish k>Bharat Rawal */
//                        start = mention.getMentionOffset() + 6 * mentions.indexOf(mention);
//                        end = start + mention.getMentionLength() + 1;
//                        if (commentTextView.length() >= end) {
//                            sample = "<" + mention.getMentionid() + "^" + commentTextView.getText().toString().substring(start + 1, end) + ">";
//
////                        commentTextView = commentTextView.substring(start, end) + ">" + commentTextView.substring(end, commentTextView.length());
//                            spannable.setSpan(sample, start + mentions.indexOf(mention), end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            spannable.replace(start + mentions.indexOf(mention), end
//                                    , sample);
//                            commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
//                        }
//                    } else if (mentions.indexOf(mention) >= 3) {
//                        if (flag2 == 1) {
//                            start = mention.getMentionOffset() + 6 * mentions.indexOf(mention);
//                            end = start + mention.getMentionLength() + 2;
//                            if (commentTextView.length() >= end) {
//                                /*<23553-Sneha Deshmukh><23553-Sneha Deshmukh><23553-Sneha Deshmukh>Sneha Deshmukh Sneha Deshmukh */
//                                sample = "<" + mention.getMentionid() + "^" + commentTextView.getText().toString().substring(start + mentions.indexOf(mention) - 1, end + 1) + ">";
//
////                        commentTextView = commentTextView.substring(start, end) + ">" + commentTextView.substring(end, commentTextView.length());
//                                spannable.setSpan(sample, start + mentions.indexOf(mention) - 1, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                spannable.replace(start + mentions.indexOf(mention) - 1, end + 1
//                                        , sample);
//                                commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
//                                flag2 = 2;
//                            }
//                        } else {
//                            start = mention.getMentionOffset() + 6 * mentions.indexOf(mention);
//                            end = start + mention.getMentionLength() + 3;
//                            if (commentTextView.length() >= end) {
//                                /*<23553-Sneha Deshmukh><23553-Sneha Deshmukh><23553-Sneha Deshmukh>Sneha Deshmukh Sneha Deshmukh */
//                                sample = "<" + mention.getMentionid() + "^" + commentTextView.getText().toString().substring(start + mentions.indexOf(mention) - 1, end + 1) + ">";
//
////                        commentTextView = commentTextView.substring(start, end) + ">" + commentTextView.substring(end, commentTextView.length());
//                                spannable.setSpan(sample, start + mentions.indexOf(mention), end + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                spannable.replace(start + mentions.indexOf(mention), end + 2
//                                        , sample);
//                                commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
//                            }
//                        }
//                        Log.v("Final String", commentTextView.toString());
//
//
//                    }
//                }
//            }
////            commentTextView.setText(spannable);
//        }
//
//        return commentTextView.getText().toString();
//    }

}
