package com.procialize.mrgeApp20.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.percolate.mentions.Mentionable;
import com.percolate.mentions.Mentions;
import com.percolate.mentions.QueryListener;
import com.percolate.mentions.SuggestionsListener;
import com.procialize.mrgeApp20.Adapter.UsersAdapter;
import com.procialize.mrgeApp20.Adapter.ViewPagerAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.CustomTools.ImagePath_MarshMallow;
import com.procialize.mrgeApp20.CustomTools.PicassoTrustAll;
import com.procialize.mrgeApp20.CustomTools.ProgressRequestBodyImage;
import com.procialize.mrgeApp20.CustomTools.ProgressRequestBodyVideo;
import com.procialize.mrgeApp20.CustomTools.RecyclerItemClickListener;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.GetterSetter.Comment;
import com.procialize.mrgeApp20.GetterSetter.Mention;
import com.procialize.mrgeApp20.GetterSetter.NewsFeedPostMultimedia;
import com.procialize.mrgeApp20.GetterSetter.SelectedImages;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.MediaLoader;
import com.procialize.mrgeApp20.Utility.MyApplication;
import com.procialize.mrgeApp20.Utility.Util;
import com.procialize.mrgeApp20.Utility.Utility;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.procialize.mrgeApp20.Utility.ScalingUtilities.getPath;
import static org.apache.http.HttpVersion.HTTP_1_1;

//import android.support.text.emoji.widget.EmojiAppCompatEditText;

public class PostActivity extends AppCompatActivity implements OnClickListener, ProgressRequestBodyImage.UploadCallbacks, ProgressRequestBodyVideo.UploadCallbacks, QueryListener, SuggestionsListener {
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private static final String SERVER_PATH = "";
    private static final String TAG = "VIDEO_COMPRESSION";
    EditText postEt;
    TextView postbtn;
    APIService mAPIService;
    ProgressBar progressbar;
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
    String mCurrentPhotoPath;
    ImageView imgPlay;
    SessionManager session;
    File sourceFile;
    MyApplication appDelegate;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    TextView txtcount, txtcount1;
    Uri imageUri;
    TextView textData;
    List<AttendeeList> customers = null;
    RelativeLayout linear;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    List<SelectedImages> resultList = new ArrayList<SelectedImages>();
    ArrayList<Integer> videoPositionArray = new ArrayList<Integer>();
    LinearLayout sliderDotspanel;
    String folderUniqueId;
    String news_feed_id1 = "";
    String is_completed = "0";
    String postUrlmulti;
    private UsersAdapter usersAdapter;
    private int REQUEST_TAKE_GALLERY = 1, REQUEST_TAKE_PHOTO = 2, REQUEST_TAKE_GALLERY_VIDEO = 3;
    private Uri uri;
    private String pathToStoredVideo;
    private String selectedImagePath;
    private VideoView displayRecordedVideo;
    private String postMsg = "";
    private String picturePath = "";
    private String videothumbpath = "";
    private String actionFlag;
    private ConnectionDetector cd;
    private String senderImageURL = "";
    private String postUrl = "";
    private ProgressDialog pDialog;
    private List<AttendeeList> userList;
    private Mentions mentions;
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private int dotscount;
    private ImageView[] dots;
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();//Array For selected images and videos
    private FFmpeg ffmpeg;
    private LinearLayout llUploadMedia;

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
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 50, out);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;

    }

    public static HttpResponse transformResponse(Response response) {

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
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;


            }
        });

        return builder.build();

    }

    @TargetApi(23)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        actionFlag = getIntent().getExtras().getString("for");

        // InitializeGUI
        initializeGUI();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        linear = findViewById(R.id.linear);
        llUploadMedia = findViewById(R.id.llUploadMedia);

        postUrlmulti = ApiConstant.baseUrl + "PostNewsFeedMultiple";

        Util.logomethod(this, headerlogoIv);


        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                appDelegate.setPostImagePath("");
            }
        });

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

        loadFFMpegBinary();
        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new MediaLoader())
                .setLocale(Locale.getDefault())
                .build()
        );
        if (actionFlag.equalsIgnoreCase("image")) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Android M Permission check
                /*if (PostActivity.this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED && PostActivity.this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                    final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
                    final String[] permissionswrite = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
                    ActivityCompat.requestPermissions(PostActivity.this, permissionswrite, 0);
                    ActivityCompat.requestPermissions(PostActivity.this, permissions, 0);
                } else {

                    showAlert(R.array.selectType);

                }*/
                if (this.checkSelfPermission(
                        "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                    final String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                    ActivityCompat.requestPermissions(PostActivity.this, permissions, 0);
                } else if (this.checkSelfPermission(
                        "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                    final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                    ActivityCompat.requestPermissions(PostActivity.this, permissions, 0);
                } else {

                    //showAlert(R.array.selectType);

                }

            } else {
                showAlert(R.array.selectType);

            }
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Android M Permission check
                    if (this.checkSelfPermission(
                            "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                        final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
                        ActivityCompat.requestPermissions(PostActivity.this, permissions, 0);
                    } else {

                    showAlert(R.array.selectType);

                }

            } else {
                showAlert(R.array.selectType);

            }*/


        } else if (actionFlag.equalsIgnoreCase("video")) {
            selectVideo();
        }

        /*
         * else { post_status_post
         * .setHint("What's on your mind? (Not more than 500 characters)");
         * post_thumbnail.setVisibility(View.GONE); }
         */

        // Toast.makeText(getApplicationContext(), "testing", Toast.LENGTH_LONG)
        // .show();

        llUploadMedia.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAlbum();
            }
        });

    }

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        Log.e("Getpath", "Cool" + mCurrentPhotoPath);
//        return image;
//    }

    public void showAlert(int res) {

        new MaterialDialog.Builder(PostActivity.this)
                .title("Select Image")
                .items(res)
                .titleColor(getResources().getColor(android.R.color.black))
                .contentColor(getResources().getColor(android.R.color.black))
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        selectType(which);

                        return true; // allow selection


                    }
                })
                .positiveText("CHOOSE")
                .cancelable(false)
                .show();
    }

    private void selectType(int pos) {
        if (pos == 0) {

            openGallery(pos);

        } /*else if (pos == 1) {

            cameraTask(pos);

        } */ else {
            finish();
        }

    }

    public void cameraTask(int imgPos) {


        Log.i("android", "startStorage");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            // Android M Permission check
            if (this.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(PostActivity.this,
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

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, 100);
            }

        }
    }

    private void startCamera() {


        File direct = new File(Environment.getExternalStorageDirectory() + "/MyFolder/Images");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/MyFolder/Images/");
            wallpaperDirectory.mkdirs();
        }

        file = new File(new File("/sdcard/MyFolder/Images/"), System.currentTimeMillis() + ".jpg");
        imageUri = Uri.fromFile(file);


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                imageUri);


        startActivityForResult(takePictureIntent, 1);


    }

    private void openGallery(int pos) {

//        startStorage();
        // selectAlbum();

    }

    private void selectAlbum() {
        Album.album(this)
                .multipleChoice()
                .columnCount(2)
                .selectCount(10)
                .camera(true)
                .cameraVideoQuality(1)
                .cameraVideoLimitDuration(15000)
                //.cameraVideoLimitBytes(Integer.MAX_VALUE)
                /*.filterMimeType(new Filter<String>() { // MimeType of File.
                    @Override
                    public boolean filter(String attributes) {
                        // MimeType: image/jpeg, image/png, video/mp4, video/3gp...
                        return attributes.contains("gif");
                    }
                })*/
                .checkedList(mAlbumFiles)
                .widget(
                        Widget.newDarkBuilder(this)
                                .toolBarColor(getResources().getColor(R.color.colorPrimary))
                                .statusBarColor(getResources().getColor(R.color.colorgrey))
                                .title("Select Image/Video")
                                .mediaItemCheckSelector(getResources().getColor(R.color.colorwhite), getResources().getColor(R.color.colorPrimary))
                                //.bucketItemCheckSelector(getResources().getColor(R.color.white),getResources().getColor(R.color.active_menu))
                                //.title(toolbar.getTitle().toString())
                                .build()
                )
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        try {
                            mAlbumFiles = result;
                            //compressVideo();
                            //resultList.clear();

                            //Get Original paths from selected arraylist
                            List selectedFileList = new ArrayList();
                            for (int i = 0; i < resultList.size(); i++) {
                                selectedFileList.add(resultList.get(i).getmOriginalFilePath());
                            }

                            //Get Data from Album
                            List albumFileList = new ArrayList();
                            for (int k = 0; k < mAlbumFiles.size(); k++) {
                                albumFileList.add(mAlbumFiles.get(k).getPath());
                            }

                            //If user deselects image/video then remove that from resultList
                            if (selectedFileList.size() > 0) {
                                for (int l = 0; l < selectedFileList.size(); l++) {
                                    if (!albumFileList.contains(selectedFileList.get(l))) {
                                        selectedFileList.remove(l);
                                        resultList.remove(l);
                                    }
                                }
                            }

                            String strMediaType;

                            for (int j = 0; j < mAlbumFiles.size(); j++) {
                                //To check selected image/video is already present in previous arraylist
                                if (!selectedFileList.contains(mAlbumFiles.get(j).getPath())) {
                                    if (mAlbumFiles.get(j).getMediaType() == AlbumFile.TYPE_VIDEO) {
                                        strMediaType = "video";
                                    } else {
                                        strMediaType = "image";
                                    }

                                    resultList.add(new SelectedImages(mAlbumFiles.get(j).getPath(),
                                            mAlbumFiles.get(j).getPath(), mAlbumFiles.get(j).getThumbPath(), false, strMediaType));
                                    if (mAlbumFiles.get(j).getMediaType() == AlbumFile.TYPE_VIDEO) {
                                        videoPositionArray.add(j);
                                    }
                                }
                            }

                            try {
                            /*if (videoPositionArray.size() > 0) {
                                if (mAlbumFiles.get(videoPositionArray.get(0)).getMediaType() == AlbumFile.TYPE_VIDEO) {
                                    String strPath = mAlbumFiles.get(videoPositionArray.get(0)).getPath();
                                    executeCutVideoCommand(startMsForVideoCutting, endMsForVideoCutting, Uri.parse(strPath), videoPositionArray.get(0));
                                }
                            } else {*/
                                setPagerAdapter(resultList);
                                //}
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        Toast.makeText(PostActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    public void startStorage() {

        Log.i("android", "startStorage");


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_TAKE_GALLERY);


    }

    private void initializeGUI() {

        appDelegate = ( MyApplication ) getApplicationContext();
        // Connection Detector Reference
        cd = new ConnectionDetector(getApplicationContext());

        // Session Manager
        session = new SessionManager(getApplicationContext());
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventId = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        HashMap<String, String> user = session.getUserDetails();

        String profilepic = user.get(SessionManager.KEY_PIC);
        // Typeface Initialize
//        Typeface typeFace = Typeface.createFromAsset(getAssets(),
//                "fonts/FuturaStd-Medium.ttf");

        apikey = user.get(SessionManager.KEY_TOKEN);

        // TextView header1 = (TextView)findViewById(R.id.header1);
        // header1.setTypeface(typeFace);


        postEt = findViewById(R.id.posttextEt);
        txtcount = findViewById(R.id.txtcount);
        postbtn = findViewById(R.id.postbtn);
        Uploadiv = findViewById(R.id.Uploadiv);
        profileIV = findViewById(R.id.profileIV);
        displayRecordedVideo = findViewById(R.id.Upvideov);
        imgPlay = findViewById(R.id.imgPlay);
        progressbar = findViewById(R.id.progressbar);
        textData = ( TextView ) findViewById(R.id.textData);
        txtcount1 = ( TextView ) findViewById(R.id.txtcount1);
        sliderDotspanel = ( LinearLayout ) findViewById(R.id.SliderDots);
        txtcount1.setTextColor(Color.parseColor(colorActive));
        txtcount.setTextColor(Color.parseColor(colorActive));
        viewPager = findViewById(R.id.viewPager);
        //postbtn.setOnClickListener(this);


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
                txtcount.setText(String.valueOf(500 - s.length()) + "/");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                System.out.print("Hello");
            }
        };

        postEt.addTextChangedListener(txwatcher);


        // Initialize Constant Class Reference


        // User Image URL
        senderImageURL = ApiConstant.profilepic + profilepic;

        // Post Status & Image URL
        postUrl = ApiConstant.baseUrl + "PostNewsFeed";

        PicassoTrustAll.getInstance(this).load(senderImageURL)
                .placeholder(R.drawable.profilepic_placeholder)
                .into(profileIV);
        procializeDB = new DBHelper(PostActivity.this);
        db = procializeDB.getReadableDatabase();
        dbHelper = new DBHelper(PostActivity.this);
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
            public void onClick(View view) {
                folderUniqueId = insertMultimediaDataToDB();
                if (cd.isConnectingToInternet()) {
                    postMsg = postEt.getText().toString().trim();
                    final Comment comment = new Comment();
                    comment.setComment(postMsg);
                    comment.setMentions(mentions.getInsertedMentions());
                    textData.setText(postMsg);

                    postMsg = highlightMentions(textData, comment.getMentions());
                    /*if (data.isEmpty()) {
                        Toast.makeText(PostActivity.this, "Please Enter your Post", Toast.LENGTH_SHORT).show();
                    } else*/
                    if (resultList.size() != 0) {
                        postbtn.setEnabled(false);
                        postbtn.setClickable(false);
                        if (resultList.size() > 0) {
                            postbtn.setEnabled(false);
                            postbtn.setClickable(false);

                            picturePath = resultList.get(0).getmPath();
                            videothumbpath = resultList.get(0).getmThumbPath();
                            new SubmitPostTask().execute("", "");

                        }
                    } else {
                        if (postMsg.isEmpty()) {
                            Toast.makeText(PostActivity.this, "Please Enter your Post", Toast.LENGTH_SHORT).show();
                        } else {
                            is_completed = "1";

                            new SubmitPostTask().execute("", "");
                        }
                    }
                } else {
                    Toast.makeText(PostActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

       /* if (v == postbtn) {


            (( InputMethodManager ) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(postEt.getWindowToken(),
                            0);

            postMsg = postEt.getText().toString();

            final Comment comment = new Comment();
            comment.setComment(postMsg);
            comment.setMentions(mentions.getInsertedMentions());
            textData.setText(postMsg);

            postMsg = highlightMentions(textData, comment.getMentions());

            picturePath = appDelegate.getPostImagePath();
            // Check for Internet Connection
           /* if (picturePath.equalsIgnoreCase("")) {
                Toast.makeText(PostActivity.this,
                        "Select Image", Toast.LENGTH_SHORT)
                        .show();
            } else {*/
           /* folderUniqueId = insertMultimediaDataToDB();
            if (cd.isConnectingToInternet()) {

                if (actionFlag.equalsIgnoreCase("image")) {
                    if (appDelegate.getPostImagePath() != null
                            && appDelegate.getPostImagePath().length() > 0) {
                        System.out
                                .println("Post Image URL  inside SubmitPostTask :"
                                        + appDelegate.getPostImagePath());

                        appDelegate.setPostImagePath("");
                        if (resultList.size() > 0) {
                            postbtn.setEnabled(false);
                            postbtn.setClickable(false);
                            picturePath = resultList.get(0).getmPath();
                            videothumbpath = resultList.get(0).getmThumbPath();
                            new SubmitPostTask().execute("", "");
                        }
                    } else {
                        is_completed = "1";
                        new SubmitPostTask().execute("", "");
                    }
                } else {
                    Toast.makeText(PostActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }*/

    }

    public String escapeJavaString(String st) {
        int ss1 = 0, ss2;
        Boolean high = true;
        StringBuilder builder = new StringBuilder();
        try {
            for (int i = 0; i < st.length(); i++) {
                char c = st.charAt(i);
                if (!Character.isLetterOrDigit(c) && !Character.isSpaceChar(c) && !Character.isWhitespace(c)) {
                    String unicode = String.valueOf(c);
                    int code = ( int ) c;
                    if (!(code >= 0 && code <= 255)) {
                        unicode = Integer.toHexString(c);
                        if (high) {
                            high = false;
                            ss1 = Integer.parseInt(unicode, 16);
                        } else {
                            high = true;
                            ss2 = Integer.parseInt(unicode, 16);
                            char chars = Character.toChars(ss1)[0];
                            char chars2 = Character.toChars(ss2)[0];
                            int codepoint = Character.toCodePoint(chars, chars2);
                            unicode = "Ax" + codepoint;
                            builder.append(unicode);
                        }
                    }
                } else {
                    builder.append(c);
                }
            }
            //  Log.i(TAG, builder.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return builder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (data == null) {
//            Intent intent1=new Intent(PostActivity.this,HomeActivity.class);
//            startActivity(intent1);
//            finish();
//        } else {

        if (data != null) {
            if (data.getData() != null) {
                if (requestCode == REQUEST_TAKE_GALLERY && resultCode == RESULT_OK && data != null) {
//            if (data == null) {
//                Intent intent1 = new Intent(PostActivity.this, HomeActivity.class);
//                startActivity(intent1);
//                finish();
//            } else {
                    postEt.setHint("Say something about this photo");
                    Uploadiv.setVisibility(View.VISIBLE);

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);

//                    String compressedImagePath = compressImage(picturePath);
//                    appDelegate.setPostImagePath(compressedImagePath);

                    // PicassoTrustAll.getInstance(this).load(compressedImagePath).into(post_thumbnail);
                    if (picturePath.contains(".gif")) {
//                        postEt.setHint("Say something about this photo");
//                        gif_imageview.setVisibility(View.VISIBLE);
                        appDelegate.setPostImagePath(picturePath);
//                        gif_imageview.setImageURI(Uri.parse(picturePath));
//                        gif_imageview.setImageURI(Uri.parse(picturePath));
                        Glide.with(displayRecordedVideo).load(picturePath).into(Uploadiv);
                    } else {
//                        postEt.setHint("Say something about this photo");
//                        Uploadiv.setVisibility(View.VISIBLE);
                        String compressedImagePath = compressImage(picturePath);
                        appDelegate.setPostImagePath(compressedImagePath);
                        Glide.with(this).load(compressedImagePath).into(Uploadiv);
                    }

                    // PicassoTrustAll.getInstance(PostActivity.this).load(compressedImagePath)
                    // .into(post_thumbnail);

                    Toast.makeText(PostActivity.this, "Image selected",
                            Toast.LENGTH_SHORT).show();

                    cursor.close();
//            }
                } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                    setpic2();

                    // setPic();
                } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
                    uri = data.getData();
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    android.widget.LinearLayout.LayoutParams params = ( android.widget.LinearLayout.LayoutParams ) displayRecordedVideo.getLayoutParams();
                    params.width = ( int ) (300 * metrics.density);
                    params.height = ( int ) (250 * metrics.density);

                    displayRecordedVideo.setLayoutParams(params);
                    displayRecordedVideo.setVideoURI(uri);
//                    displayRecordedVideo.setMediaController(new MediaController(this));
//                    ViewGroup.LayoutParams params = displayRecordedVideo.getLayoutParams();
//
//                    params.height = 600;
                    displayRecordedVideo.setLayoutParams(params);
                    displayRecordedVideo.start();

                    if (Build.VERSION.SDK_INT > 22)
                        pathToStoredVideo = ImagePath_MarshMallow.getPath(PostActivity.this, uri);
                    else
                        //else we will get path directly
                        pathToStoredVideo = uri.getPath();
                    Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                    //Store the video to your server
                    file = new File(pathToStoredVideo);

                    Uploadiv.setVisibility(View.GONE);
                    displayRecordedVideo.setVisibility(View.VISIBLE);

                    uri = data.getData();


                    displayRecordedVideo.setVideoURI(uri);

                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    android.widget.LinearLayout.LayoutParams params1 = ( android.widget.LinearLayout.LayoutParams ) displayRecordedVideo.getLayoutParams();
                    params.width = ( int ) (300 * metrics.density);
                    params.height = ( int ) (250 * metrics.density);

                    displayRecordedVideo.setLayoutParams(params1);
                    displayRecordedVideo.setVideoURI(uri);
//                    displayRecordedVideo.setMediaController(new MediaController(this));
//                    ViewGroup.LayoutParams params = displayRecordedVideo.getLayoutParams();
//
//                    params.height = 600;
                    displayRecordedVideo.setLayoutParams(params1);
                    displayRecordedVideo.start();
                    try {
                        if (uri != null) {

                            MediaPlayer mp = MediaPlayer.create(this, uri);
                            int duration = mp.getDuration();
                            mp.release();

                            if ((duration / 1000) > 15) {
                                // Show Your Messages
                                Toast.makeText(PostActivity.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                //Store the video to your server


//                                    pathToStoredVideo = getRealPathFromURIPathVideo(data.getData(),PostViewActivity.this);
                                if (Build.VERSION.SDK_INT > 22) {
                                    pathToStoredVideo = ImagePath_MarshMallow.getPath(PostActivity.this, uri);
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

                } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                    Uri selectedMediaUri = data.getData();
                    if (selectedMediaUri != null) {
                        if (selectedMediaUri.toString().contains("video")) {

                            if (data != null && data.getStringExtra("video") != null)

                                Uploadiv.setVisibility(View.GONE);
                            displayRecordedVideo.setVisibility(View.VISIBLE);
                            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                                Uri selectedImageUri = data.getData();

                                // OI FILE Manager
                                selectedImagePath = selectedImageUri.getPath();

                                // MEDIA GALLERY
                                selectedImagePath = getPath(PostActivity.this, selectedImageUri);
                                uri = selectedImageUri;
                                if (selectedImagePath != null) {

                                    displayRecordedVideo.setVideoURI(selectedImageUri);
                                    DisplayMetrics metrics = new DisplayMetrics();
                                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                                    android.widget.LinearLayout.LayoutParams params = ( android.widget.LinearLayout.LayoutParams ) displayRecordedVideo.getLayoutParams();
                                    params.width = ( int ) (300 * metrics.density);
                                    params.height = ( int ) (250 * metrics.density);

                                    displayRecordedVideo.setLayoutParams(params);
                                    displayRecordedVideo.setVideoURI(uri);
//                    displayRecordedVideo.setMediaController(new MediaController(this));
//                    ViewGroup.LayoutParams params = displayRecordedVideo.getLayoutParams();
//
//                    params.height = 600;
                                    displayRecordedVideo.setLayoutParams(params);
                                    displayRecordedVideo.start();
                                    try {
                                        if (uri != null) {

                                            MediaPlayer mp = MediaPlayer.create(this, uri);
                                            int duration = mp.getDuration();
                                            mp.release();

                                            if ((duration / 1000) > 15) {
                                                // Show Your Messages
                                                Toast.makeText(PostActivity.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                //Store the video to your server


//                                    pathToStoredVideo = getRealPathFromURIPathVideo(data.getData(),PostViewActivity.this);
                                                if (Build.VERSION.SDK_INT > 22) {
                                                    pathToStoredVideo = ImagePath_MarshMallow.getPath(PostActivity.this, uri);
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
                            } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
                                uri = data.getData();

                                displayRecordedVideo.setVideoURI(uri);
                                DisplayMetrics metrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                                android.widget.LinearLayout.LayoutParams params = ( android.widget.LinearLayout.LayoutParams ) displayRecordedVideo.getLayoutParams();
                                params.width = ( int ) (300 * metrics.density);
                                params.height = ( int ) (250 * metrics.density);

                                displayRecordedVideo.setLayoutParams(params);
                                displayRecordedVideo.setVideoURI(uri);
//                    displayRecordedVideo.setMediaController(new MediaController(this));
//                    ViewGroup.LayoutParams params = displayRecordedVideo.getLayoutParams();
//
//                    params.height = 600;
                                displayRecordedVideo.setLayoutParams(params);
                                displayRecordedVideo.start();

                                try {
                                    if (uri != null) {

                                        MediaPlayer mp = MediaPlayer.create(this, uri);
                                        int duration = mp.getDuration();
                                        mp.release();

                                        if ((duration / 1000) > 15) {
                                            // Show Your Messages
                                            Toast.makeText(PostActivity.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            //Store the video to your server


//                                    pathToStoredVideo = getRealPathFromURIPathVideo(data.getData(),PostViewActivity.this);
                                            if (Build.VERSION.SDK_INT > 22) {
                                                pathToStoredVideo = ImagePath_MarshMallow.getPath(PostActivity.this, uri);
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
                            } else if (resultCode == Activity.RESULT_OK) {
                                Uploadiv.setVisibility(View.GONE);
                                displayRecordedVideo.setVisibility(View.VISIBLE);

                                uri = data.getData();


                                displayRecordedVideo.setVideoURI(uri);
                                DisplayMetrics metrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                                android.widget.LinearLayout.LayoutParams params = ( android.widget.LinearLayout.LayoutParams ) displayRecordedVideo.getLayoutParams();
                                params.width = ( int ) (300 * metrics.density);
                                params.height = ( int ) (250 * metrics.density);

                                displayRecordedVideo.setLayoutParams(params);
                                displayRecordedVideo.setVideoURI(uri);
//                    displayRecordedVideo.setMediaController(new MediaController(this));
//                    ViewGroup.LayoutParams params = displayRecordedVideo.getLayoutParams();
//
//                    params.height = 600;
                                displayRecordedVideo.setLayoutParams(params);
                                displayRecordedVideo.start();


                                try {
                                    if (uri != null) {

                                        MediaPlayer mp = MediaPlayer.create(this, uri);
                                        int duration = mp.getDuration();
                                        mp.release();

                                        if ((duration / 1000) > 15) {
                                            // Show Your Messages
                                            Toast.makeText(PostActivity.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //Store the video to your server


//                                    pathToStoredVideo = getRealPathFromURIPathVideo(data.getData(),PostViewActivity.this);


                                            if (Build.VERSION.SDK_INT > 22) {
                                                pathToStoredVideo = ImagePath_MarshMallow.getPath(PostActivity.this, uri);
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
                        }
                    }
                }

            } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                setpic2();
            } else {
                finish();
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setpic2();
        } else {
            finish();
        }
//        }

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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.procialize.eventsapp.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void setpic2() {

        Uploadiv.setVisibility(View.VISIBLE);
        //selfieSubmit.setVisibility(View.VISIBLE);
        //edtImagename.setVisibility(View.VISIBLE);


        String compressedImagePath = compressImage(mCurrentPhotoPath);
        appDelegate.setPostImagePath(compressedImagePath);

        Glide.with(this).load(compressedImagePath).into(Uploadiv);


        Toast.makeText(PostActivity.this, "Image selected",
                Toast.LENGTH_SHORT).show();

    }

    private void setPic() {
        // Get the dimensions of the View
        //int targetW = post_thumbnail.getWidth();
        //int targetH = post_thumbnail.getHeight();

        int targetW = 400;
        int targetH = 600;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        // mImageView.setImageBitmap(bitmap);

        //    String compressedImagePath = compressImage(mCurrentPhotoPath);


        Glide.with(this).load(mCurrentPhotoPath).into(Uploadiv);


        Toast.makeText(PostActivity.this, "Image selected" + mCurrentPhotoPath,
                Toast.LENGTH_SHORT).show();

    }

    /* @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                 && null != data) {

             post_status_post.setHint("Say something about this photo");
             post_status_post.setEnabled(true);
             post_thumbnail.setVisibility(View.VISIBLE);


             Uri selectedImage = data.getData();
             String[] filePathColumn = {MediaStore.Images.Media.DATA};

             Cursor cursor = getContentResolver().query(selectedImage,
                     filePathColumn, null, null, null);
             cursor.moveToFirst();

             int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
             String picturePath = cursor.getString(columnIndex);

             String compressedImagePath = compressImage(picturePath);
             appDelegate.setPostImagePath(compressedImagePath);

             PicassoTrustAll.getInstance(this).load(compressedImagePath).into(post_thumbnail);

             // PicassoTrustAll.getInstance(PostActivity.this).load(compressedImagePath)
             // .into(post_thumbnail);

             Toast.makeText(PostActivity.this, "Image selected",
                     Toast.LENGTH_SHORT).show();

             cursor.close();
         }
     }
 */
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

        float maxHeight = 916.0f;
        float maxWidth = 712.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = ( int ) (imgRatio * actualWidth);
                actualHeight = ( int ) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = ( int ) (imgRatio * actualHeight);
                actualWidth = ( int ) maxWidth;
            } else {
                actualHeight = ( int ) maxHeight;
                actualWidth = ( int ) maxWidth;
            }
        }

        // setting inSampleSize value allows to load a scaled down version of
        // the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);

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

        float ratioX = actualWidth / ( float ) options.outWidth;
        float ratioY = actualHeight / ( float ) options.outHeight;
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
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

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
            final int heightRatio = Math.round(( float ) height
                    / ( float ) reqHeight);
            final int widthRatio = Math.round(( float ) width / ( float ) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                showAlert(R.array.selectType);


            } else {
                Toast.makeText(this, "No permission to read external storage.",
                        Toast.LENGTH_SHORT).show();

            }
        } else if (requestCode == REQUEST_TAKE_PHOTO) {

            int permGranted = PackageManager.PERMISSION_GRANTED;

            Boolean permissionRequired = false;

            for (int perm : grantResults) {

                if (perm != permGranted) {
                    permissionRequired = true;
                }
            }

            //if permission is still required
            if (permissionRequired) {
                Toast.makeText(this, "No permission to capture storage.",
                        Toast.LENGTH_SHORT).show();

            } else {
                //captureImage();

                dispatchTakePictureIntent();
            }
/*if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();

            } else {
                Toast.makeText(this, "No permission to capture photo.",
                        Toast.LENGTH_SHORT).show();

            }*/
        }
    }


    @Override
    public void onDestroy() {

        dismissProgress();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        appDelegate.setPostImagePath("");

    }

    private void selectVideo() {
        final CharSequence[] items = {"Take Video", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Video!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(PostActivity.this);

                if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        videogalleryIntent();

                } else if (items[item].equals("Take Video")) {
                    userChoosenTask = "Take Video";
                    if (result) {
                        Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        videoCaptureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
                        videoCaptureIntent.putExtra(MediaStore.EXTRA_FULL_SCREEN, true);
                        videoCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//                        videoCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); //0 means low & 1 means high
                        if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                        }
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
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

    public void showProgress() {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.progrssdialogback);
        postbtn.setEnabled(false);
        postEt.setEnabled(false);
        progressbar.setVisibility(View.VISIBLE);
//        progressbar.setProgress(0);
//        progressbar.setMaxValue(100);
//        progressbar.setProgressColor(Color.parseColor(colorActive));
//        progressbar.setText(String.valueOf(0));
//        progressbar.setTextColor(Color.parseColor(colorActive));
//        progressbar.setSuffix("%");
//        progressbar.setPrefix("");

    }

    public void dismissProgress() {
        postbtn.setEnabled(true);
        postEt.setEnabled(true);
        if (progressbar.getVisibility() == View.VISIBLE) {
            progressbar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onProgressUpdate(int percentage) {
        Log.e("per", String.valueOf(percentage));
//        progressbar.showValue(99f, 100f, true);
//        progressbar.setProgress(percentage);
//        progressbar.setText(String.valueOf(percentage));
    }

    @Override
    public void onError() {
        dismissProgress();
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onQueryReceived(String s) {
        final List<AttendeeList> users = searchUsers(s);
        if (users != null && !users.isEmpty()) {
            usersAdapter.clear();
            usersAdapter.setCurrentQuery(s);
            ArrayList<String> arr = new ArrayList<String>(users.size());
            for (int j = 0; j < users.size(); j++) {
                arr.add(users.get(j).getAttendeeId());
            }

            for (int i = 0; i < mentions.getInsertedMentions().size(); i++) {
                String mentionName = mentions.getInsertedMentions().get(i).getMentionid();
                if (arr.contains(mentionName)) {
                    int index = arr.indexOf(mentionName);
                    users.remove(index);
                    arr.clear();
                    for (int j = 0; j < users.size(); j++) {
                        arr.add(users.get(j).getAttendeeId());
                    }
                }
            }
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
        InputMethodManager inputManager = ( InputMethodManager ) getSystemService(Activity.INPUT_METHOD_SERVICE);
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
                    mention.setMentionName(user.getFirstName() + " " + user.getLastName());
                    mention.setMentionid(user.getAttendeeId());
                    mentions.insertMention(mention);


                }
            }
        }));
    }

    private void setPagerAdapter(List<SelectedImages> resultList) {
        viewPagerAdapter = new ViewPagerAdapter(PostActivity.this, resultList);
        viewPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        sliderDotspanel.removeAllViews();
        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(PostActivity.this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //Tagging function By Aparna
    private String highlightMentions(TextView commentTextView, final List<Mentionable> mentions) {
        final SpannableStringBuilder spannable = new SpannableStringBuilder(commentTextView.getText());
        SQLiteDatabase db = procializeDB.getWritableDatabase();
        for (int i = 0; i < mentions.size(); i++) {
            String mentionNameFromDb = procializeDB.getMeentionNameFromAttendeeId(String.valueOf(mentions.get(i).getMentionid()), db);
            if (mentionNameFromDb.isEmpty()) {
                mentionNameFromDb = "<" + mentions.get(i).getMentionid() + "^" + mentions.get(i).getMentionName() + ">";
            }
            int offset = mentions.get(i).getMentionOffset();
            int length = mentions.get(i).getMentionLength();
            if (i != 0) {
                for (int j = 0; j < i; j++) {
                    offset = offset + mentions.get(j).getMentionid().length() + 3;
                }
            }
            spannable.setSpan(mentionNameFromDb, offset, offset + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.replace(offset, offset + length, mentionNameFromDb);
            //String mentionedData = data.replace(mentionName, mentionNameFromDb);
            commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
        }
        return commentTextView.getText().toString();
    }

    private void loadFFMpegBinary() {
        try {
            if (ffmpeg == null) {
                Log.d(TAG, "ffmpeg : era nulo");
                ffmpeg = FFmpeg.getInstance(this);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    //showUnsupportedExceptionDialog();
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "ffmpeg : correct Loaded");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            //showUnsupportedExceptionDialog();
        } catch (Exception e) {
            Log.d(TAG, "EXception no controlada : " + e);
        }
    }

    public String insertMultimediaDataToDB() {

        ArrayList<NewsFeedPostMultimedia> arrayListNewsFeedMultiMedia = new ArrayList<>();

        Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        for (int i = 0; i < resultList.size(); i++) {
            picturePath = resultList.get(i).getmPath();
            videothumbpath = resultList.get(i).getmThumbPath();
            NewsFeedPostMultimedia newsFeedPostMultimedia = new NewsFeedPostMultimedia();
            newsFeedPostMultimedia.setMedia_file(picturePath);
            newsFeedPostMultimedia.setMedia_file_thumb(videothumbpath);
            newsFeedPostMultimedia.setNews_feed_id(news_feed_id1);
            newsFeedPostMultimedia.setCompressedPath("");
            newsFeedPostMultimedia.setMedia_type(resultList.get(i).getmMediaType());
            newsFeedPostMultimedia.setFolderUniqueId(ts.toString());
            arrayListNewsFeedMultiMedia.add(newsFeedPostMultimedia);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.insertUploadMultimediaInfo(arrayListNewsFeedMultiMedia, news_feed_id1, db);

        return ts.toString();
    }


  /*  private String highlightMentions(TextView commentTextView, final List<Mentionable> mentions) {


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
                    spannable.setSpan(sample, mentions.get(i).getMentionOffset() + i + i * 6, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i + i * 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.replace(mentions.get(i).getMentionOffset() + i + i * 6, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i + i * 6, sample);


                    commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
                } catch (Exception e) {
                    sample = "<" + mentions.get(i).getMentionid() + "^" + mentions.get(i).getMentionName() + ">";
                    spannable.setSpan(sample, mentions.get(i).getMentionOffset() + i + i * 6, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i * 6 - i + i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.replace(mentions.get(i).getMentionOffset() + i + i * 6, mentions.get(i).getMentionOffset() + mentions.get(i).getMentionLength() + i * 6 - i + i + 1, sample);


                    commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
                }
            }
        }

        return commentTextView.getText().toString();
    }*/

    public class SubmitPostTasknew extends AsyncTask<String, String, JSONObject> {

        String json1 = "";
        InputStream is = null;
        JSONObject json = null;
        JSONObject status;


        String message = "";

        String error = "";
        String msg = "";

        String res = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            showProgress();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {


                if (picturePath != null && !(picturePath.equalsIgnoreCase(""))) {
                    sourceFile = new File(picturePath);
                }


                //Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());

                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

                String filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);


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
                if (picturePath != null && !(picturePath.equalsIgnoreCase(""))) {
                    builder.addFormDataPart("media_file", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile));
                }

                builder.addFormDataPart("api_access_token", apikey);
                builder.addFormDataPart("event_id", eventId);

                builder.addFormDataPart("type", actionFlag);
                builder.addFormDataPart("status", StringEscapeUtils.escapeJava(postMsg));

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

            } catch (NullPointerException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());

            }


            return json;

        }

        @Override
        protected void onPostExecute(JSONObject result) {

            super.onPostExecute(result);
            // Dismiss the progress dialog
            dismissProgress();

            if (error.equalsIgnoreCase("success")) {

                // notifyDataSetChanged();

                Toast.makeText(PostActivity.this, message, Toast.LENGTH_SHORT)
                        .show();

                Intent MainIntent = new Intent(PostActivity.this, HomeActivity.class);
                startActivity(MainIntent);
                finish();

            } else {
                Toast.makeText(PostActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
                Intent MainIntent = new Intent(PostActivity.this, HomeActivity.class);
                startActivity(MainIntent);
                finish();
            }


        }

    }

    public class SubmitStatusOnlyTask extends
            AsyncTask<String, String, JSONObject> {

        String json1 = "";
        InputStream is = null;
        JSONObject json = null;
        JSONObject status;


        String message = "";

        String error = "";
        String msg = "";

        String res = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            showProgress();

        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {

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

                builder.addFormDataPart("api_access_token", apikey);
                builder.addFormDataPart("event_id", eventId);
                builder.addFormDataPart("user_type", actionFlag);
                builder.addFormDataPart("status", StringEscapeUtils.unescapeJava(postMsg));


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

            }


            return json;

        }

        @Override
        protected void onPostExecute(JSONObject result) {

            super.onPostExecute(result);
            // Dismiss the progress dialog

            dismissProgress();

            // String s = "";
            // String message = "";
            // Toast.makeText(activity, "Status Posted Successfully",
            // Toast.LENGTH_SHORT).show();

            // try {
            // s = result.get("error").toString();
            // message = result.get("msg").toString();
            // } catch (JSONException e) {
            // e.printStackTrace();
            // }

            if (error.equalsIgnoreCase("success")) {

                // notifyDataSetChanged();

                Toast.makeText(PostActivity.this, message, Toast.LENGTH_SHORT)
                        .show();

                Intent MainIntent = new Intent(PostActivity.this, HomeActivity.class);
                startActivity(MainIntent);
                finish();
            } else {
                Intent MainIntent = new Intent(PostActivity.this, HomeActivity.class);
                startActivity(MainIntent);
                finish();
            }

            // try {
            // s = result.get("error").toString();
            // message = result.get("msg").toString();
            // if (s.equalsIgnoreCase("success")) {
            //
            // Toast.makeText(activity, message, Toast.LENGTH_SHORT)
            // .show();
            //
            // } else {
            // Toast.makeText(activity, message, Toast.LENGTH_SHORT)
            // .show();
            // }
            // } catch (JSONException e1) {
            // e1.printStackTrace();
            // }

            // if (s.equalsIgnoreCase("success"))

            // new FetchWallNotification().execute();
            // try {
            // status = result.getJSONObject("error");
            // s = status.getString("error");
            // if (s.equalsIgnoreCase("success")) {
            //
            // Toast.makeText(activity, " Status Posted Successfully",
            // Toast.LENGTH_SHORT).show();
            // } else {
            //
            //
            //
            // Toast.makeText(activity, "Update failed",
            // Toast.LENGTH_SHORT).show();
            // }
            //
            // } catch (JSONException e) {
            // e.printStackTrace();
            // }

        }
    }


    public class SubmitPostTask extends AsyncTask<String, String, JSONObject> {

        InputStream is = null;
        JSONObject json = null;
        JSONObject status;
        String message = "";
        String error = "";
        String msg = "";
        String news_feed_id1 = "";
        String res = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            //showProgress("");

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                OkHttpClient client = null;
                try {
                    URL url = new URL(postUrlmulti);
                    client = getUnsafeOkHttpClient().newBuilder().build();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                builder.addFormDataPart("api_access_token", apikey);
                builder.addFormDataPart("event_id", eventId);
                builder.addFormDataPart("type", "image");
                builder.addFormDataPart("status", StringEscapeUtils.escapeJava(postMsg));
                builder.addFormDataPart("is_completed", is_completed);

                RequestBody requestBody = builder.build();
                Request request = new Request.Builder()
                        .url(postUrlmulti)
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

            } catch (IOException e) {
                e.printStackTrace();
            }

            // try parse the string to a JSON object
            try {
                json = new JSONObject(res);
                error = json.getString("status");
                message = json.getString("msg");
                news_feed_id1 = json.getString("news_feed_id");
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            } catch (NullPointerException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return json;

        }

        @Override
        protected void onPostExecute(JSONObject result) {

            super.onPostExecute(result);

            if (error.equalsIgnoreCase("success")) {


                if (resultList.size() > 0) {
                    Toast.makeText(PostActivity.this, message + " Your multimedia will be uploaded shortly..!", Toast.LENGTH_SHORT)
                            .show();

                    postbtn.setEnabled(false);
                    postbtn.setClickable(false);
                            /*for (int i = 0; i < mAlbumFiles.size(); i++) {
                                picturePath = mAlbumFiles.get(i).getPath();
                                videothumbpath = mAlbumFiles.get(i).getThumbPath();*/

                   /* ArrayList<NewsFeedPostMultimedia> arrayListNewsFeedMultiMedia = new ArrayList<>();

                    Date date= new Date();
                    long time = date.getTime();
                    Timestamp ts = new Timestamp(time);
                    for (int i = 0; i < resultList.size(); i++) {
                        picturePath = resultList.get(i).getmPath();
                        videothumbpath = resultList.get(i).getmThumbPath();
                        NewsFeedPostMultimedia newsFeedPostMultimedia = new NewsFeedPostMultimedia();
                        newsFeedPostMultimedia.setMedia_file(picturePath);
                        newsFeedPostMultimedia.setMedia_file_thumb(videothumbpath);
                        newsFeedPostMultimedia.setNews_feed_id(news_feed_id1);
                        newsFeedPostMultimedia.setCompressedPath("");
                        newsFeedPostMultimedia.setMedia_type(resultList.get(i).getmMediaType());
                        newsFeedPostMultimedia.setFolderUniqueId(ts.toString());
                        arrayListNewsFeedMultiMedia.add(newsFeedPostMultimedia);
                    }

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    dbHelper.insertUploadMultimediaInfo(arrayListNewsFeedMultiMedia, news_feed_id1, db);*/
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    dbHelper.updateNewsFeedId(news_feed_id1, folderUniqueId, db);

                    Intent MainIntent = new Intent(PostActivity.this, HomeActivity.class);
                    startActivity(MainIntent);
                    finish();
                    /*if (arrayListNewsFeedMultiMedia.size() > 0) {
                        Intent intent = new Intent(PostNewActivity.this, BackgroundService.class);
                        intent.putExtra("arrayListNewsFeedMultiMedia", arrayListNewsFeedMultiMedia);
                        intent.putExtra("api_access_token", apikey);
                        intent.putExtra("event_id", eventId);
                        intent.putExtra("status", data);
                        intent.putExtra("isNew", true);
                        startService(intent);
                    }*/

                    /*}*/
                    //uploadToServer(resultList.get(0));
                } else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    dbHelper.updateNewsFeedId(news_feed_id1, folderUniqueId, db);

                        Intent MainIntent = new Intent(PostActivity.this, HomeActivity.class);
                        startActivity(MainIntent);
                        finish();
                }
            } else {
                Toast.makeText(PostActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


}
