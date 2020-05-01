//package com.procialize.eventsapp.Activity;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.BitmapFactory;
//import android.graphics.PorterDuff;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import androidx.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.FileProvider;
//import androidx.viewpager.widget.ViewPager;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.appcompat.widget.Toolbar;
//import android.text.Editable;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.DataSource;
//import com.bumptech.glide.load.engine.GlideException;
//import com.bumptech.glide.request.RequestListener;
//import com.bumptech.glide.request.target.Target;
//import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
//import com.percolate.mentions.Mentionable;
//import com.percolate.mentions.Mentions;
//import com.percolate.mentions.QueryListener;
//import com.percolate.mentions.SuggestionsListener;
//
//import com.procialize.eventsapp.Adapter.UsersAdapter;
//import com.procialize.eventsapp.Adapter.ViewPagerAdapter;
//import com.procialize.eventsapp.ApiConstant.APIService;
//import com.procialize.eventsapp.ApiConstant.ApiConstant;
//import com.procialize.eventsapp.ApiConstant.ApiUtils;
//import com.procialize.eventsapp.CustomTools.CircleDisplay;
//import com.procialize.eventsapp.CustomTools.MyJZVideoPlayerStandard;
//import com.procialize.eventsapp.CustomTools.ProgressRequestBodyImage;
//import com.procialize.eventsapp.CustomTools.ProgressRequestBodyVideo;
//import com.procialize.eventsapp.CustomTools.RecyclerItemClickListener;
//import com.procialize.eventsapp.DbHelper.ConnectionDetector;
//import com.procialize.eventsapp.DbHelper.DBHelper;
//import com.procialize.eventsapp.GetterSetter.AttendeeList;
//import com.procialize.eventsapp.GetterSetter.Mention;
//import com.procialize.eventsapp.GetterSetter.PostTextFeed;
//import com.procialize.eventsapp.GetterSetter.SelectedImages;
//import com.procialize.eventsapp.R;
//import com.procialize.eventsapp.Session.SessionManager;
//import com.procialize.eventsapp.Utility.MediaLoader;
//import com.procialize.eventsapp.Utility.MyApplication;
//import com.yanzhenjie.album.Action;
//import com.yanzhenjie.album.Album;
//import com.yanzhenjie.album.AlbumConfig;
//import com.yanzhenjie.album.AlbumFile;
//import com.yanzhenjie.album.api.widget.Widget;
//
//import org.apache.commons.lang3.StringEscapeUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.entity.InputStreamEntity;
//import org.apache.http.message.BasicHttpResponse;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.security.cert.X509Certificate;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//
//import cn.jzvd.JZVideoPlayer;
//
//import okhttp3.Headers;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.ResponseBody;
//import retrofit2.Response;
//
//import static android.Manifest.permission.CAMERA;
//import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
//import static android.Manifest.permission.VIBRATE;
//import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
//import static org.apache.http.HttpVersion.HTTP_1_1;
//
//public class PostNewActivity extends AppCompatActivity implements ProgressRequestBodyImage.UploadCallbacks,
//        ProgressRequestBodyVideo.UploadCallbacks, QueryListener, SuggestionsListener, View.OnClickListener {
//    public static final int RequestPermissionCode = 6;
//    //Video Compression
//    private static final String TAG = "VIDEO_COMPRESSION";
//    HashMap<String, String> user;
//    String profilepic, fname, lname, company, city;
//    TextView txt_company, txt_name, txt_city;
//    String data;
//    TextView textData;
//    List<AttendeeList> customers = null;
//    EditText postEt;
//    TextView postbtn;
//    APIService mAPIService;
//    CircleDisplay progressbar;
//    ProgressBar progress;
//    SessionManager sessionManager;
//    String apikey = "";
//    String to;
//    ImageView Uploadiv;
//    File file;
//    String eventId;
//    ImageView profileIV;
//    MyApplication appDelegate;
//    String mCurrentPhotoPath;
//    ImageView imgPlay;
//    String MY_PREFS_NAME = "ProcializeInfo";
//    String eventid, colorActive;
//    ImageView headerlogoIv;
//    File sourceFile;
//    TextView txtUploadImg;
//    ImageView imguploadimg;
//   MyJZVideoPlayerStandard VideoView;
//    ConnectionDetector cd;
//    LinearLayout mainLLpost;
//    ViewPager viewPager;
//    RelativeLayout rel1;
//    int successfullUploadedMediaCount = 0;
//    LinearLayout LinSelectorButton;
//    String news_feed_id1 = "";
//    List<String> path;
//    LinearLayout sliderDotspanel;
//    Toolbar toolbar;
//    ProgressDialog progressDialog;
//    List<SelectedImages> resultList = new ArrayList<SelectedImages>();
//    int startMsForVideoCutting = 0;//Start time for cutting video
//    int endMsForVideoCutting = 120000;//End TIme for cutting video
//    ArrayList<Integer> videoPositionArray = new ArrayList<Integer>();
//    ViewPagerAdapter viewPagerAdapter;
//    private List<AttendeeList> userList;
//    private Mentions mentions;
//    private DBHelper dbHelper;
//    private DBHelper procializeDB;
//    private UsersAdapter usersAdapter;
//    private String postUrlmulti = "";
//    private int REQUEST_TAKE_PHOTO = 2;
//    private String picturePath = "";
//    private String videothumbpath = "";
//    private int dotscount;
//    private ImageView[] dots;
//    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();//Array For selected images and videos
//    private FFmpeg ffmpeg;
//    String is_completed="0";
//    private static OkHttpClient getUnsafeOkHttpClient() {
//
//        // Create a trust manager that does not validate certificate chains
//        final TrustManager[] trustAllCerts = new TrustManager[]{
//                new X509TrustManager() {
//                    @Override
//                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
//                    }
//
//                    @Override
//                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
//                    }
//
//                    @Override
//                    public X509Certificate[] getAcceptedIssuers() {
//                        return new X509Certificate[0];
//                    }
//                }
//        };
//
//        // Install the all-trusting trust manager
//        SSLContext sslContext = null;
//        try {
//            sslContext = SSLContext.getInstance("SSL");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        try {
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//        // Create an ssl socket factory with our all-trusting manager
//        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//        OkHttpClient client = new OkHttpClient();
//
//        OkHttpClient.Builder builder = client.newBuilder();
//        builder.sslSocketFactory(sslSocketFactory);
//        builder.hostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        });
//
//        return builder.build();
//
//    }
//
//    public static HttpResponse transformResponse(okhttp3.Response response) {
//
//        BasicHttpResponse httpResponse = null;
//        try {
//            int code = 0;
//            if (response != null)
//                code = response.code();
//
//            try {
//                String message = response.message();
//                httpResponse = new BasicHttpResponse(HTTP_1_1, code, message);
//
//                ResponseBody body = response.body();
//                InputStreamEntity entity = new InputStreamEntity(body.byteStream(), body.contentLength());
//                httpResponse.setEntity(entity);
//
//                Headers headers = response.headers();
//                for (int i = 0, size = headers.size(); i < size; i++) {
//                    String name = headers.name(i);
//                    String value = headers.value(i);
//                    httpResponse.addHeader(name, value);
//                    if ("Content-Type".equalsIgnoreCase(name)) {
//                        entity.setContentType(value);
//                    } else if ("Content-Encoding".equalsIgnoreCase(name)) {
//                        entity.setContentEncoding(value);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return httpResponse;
//    }
//
//    @SuppressLint("ResourceAsColor")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post);
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle(null);
//        progressDialog.setCancelable(false);
//        loadFFMpegBinary();
//        Album.initialize(AlbumConfig.newBuilder(this)
//                .setAlbumLoader(new MediaLoader())
//                .setLocale(Locale.getDefault())
//                .build()
//        );
//
//        headerlogoIv = findViewById(R.id.headerlogoIv);
//        profileIV = findViewById(R.id.profileIV);
////        txt_company = findViewById(R.id.txt_company);
//        txt_name = findViewById(R.id.txt_name);
//        txt_city = findViewById(R.id.txt_city);
////        LinSelectorButton = findViewById(R.id.LinSelectorButton);
//        VideoView = findViewById(R.id.videoplayer);
//        viewPager = findViewById(R.id.viewPager);
//        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
//        progress = findViewById(R.id.progress);
////        rel1 = findViewById(R.id.rel1);
//
//        LinSelectorButton.setOnClickListener(this);
//
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        appDelegate = (MyApplication) getApplicationContext();
//
//        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//        //eventId = prefs.getString("eventid", "1");
//        colorActive = prefs.getString("colorActive", "");
//        colorActive = "#fe8b79";
//
//        SessionManager sessionManager = new SessionManager(PostNewActivity.this);
//        user = sessionManager.getUserDetails();
//        eventId = sessionManager.get();
//
//        profilepic = user.get(SessionManager.KEY_PIC);
//        fname = user.get(SessionManager.KEY_NAME);
//        lname = user.get(SessionManager.KEY_LNAME);
//        company = user.get(SessionManager.KEY_COMPANY);
//        city = user.get(SessionManager.KEY_CITY);
//        apikey = user.get(SessionManager.KEY_TOKEN);
//
//        //Util.logomethod(this, headerlogoIv);
//        txt_name.setText(fname + " " + lname);
//        String AttType = sessionManager.getAttndeeType();
//        if (AttType.equalsIgnoreCase("BD")) {
//            txt_company.setText("Bride");
//
//        } else if (AttType.equalsIgnoreCase("GM")) {
//            txt_company.setText("Groom");
//
//        } else {
//            txt_company.setText(user.get(SessionManager.KEY_RELATION_WITH) + "'s " + user.get(SessionManager.KEY_RELATION));
//        }
//        txt_city.setText(user.get(SessionManager.KEY_CITY));
//
//
//        mAPIService = ApiUtils.getAPIService();
//        cd = new ConnectionDetector(this);
//        postUrlmulti = ApiConstant.baseUrl + "PostNewsFeedMultiple";
//        Glide.with(this).load(ApiConstant.profilepic + profilepic).listener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                profileIV.setImageResource(R.drawable.profilepic_placeholder);
//                return true;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                return false;
//            }
//        }).into(profileIV);
//
//        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_ATOP);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                appDelegate.setPostImagePath("");
//                onBackPressed();
//            }
//        });
//
//        postEt = findViewById(R.id.posttextEt);
//        InputMethodManager imm =
//                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(postEt.getWindowToken(), 0);
//        mainLLpost = (LinearLayout) findViewById(R.id.mainLLpost);
//        txtUploadImg = findViewById(R.id.txtUploadImg);
//        imguploadimg = findViewById(R.id.imguploadimg);
//        postbtn = findViewById(R.id.postBtn);
//        progressbar = findViewById(R.id.progressbar);
//        textData = findViewById(R.id.textData);
//        Uploadiv = findViewById(R.id.Uploadiv);
//        imgPlay = findViewById(R.id.imgPlay);
//
//        if (CheckingPermissionIsEnabledOrNot()) {
//        } else {
//            //Calling method to enable permission.
//            RequestMultiplePermission();
//
//        }
//
//        final TextView txtcount1 = findViewById(R.id.txtcount1);
//        final TextWatcher txwatcher = new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start,
//                                          int count, int after) {
//                int tick = start + after;
//                if (tick < 250) {
//                    int remaining = 250 - tick;
//                    // txtcount1.setText(String.valueOf(remaining));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//                txtcount1.setText(String.valueOf(s.length()));
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                // TODO Auto-generated method stub
//                System.out.print("Hello");
//            }
//        };
//
//        postEt.addTextChangedListener(txwatcher);
//        procializeDB = new DBHelper(PostNewActivity.this);
//        dbHelper = new DBHelper(PostNewActivity.this);
//        customers = new ArrayList<AttendeeList>();
//        userList = procializeDB.getAllAttendeeDetails();
//        procializeDB.getReadableDatabase();
//        customers = dbHelper.getAllAttendeeDetails();
//
//        mentions = new Mentions.Builder(this, postEt)
//                .suggestionsListener(this)
//                .queryListener(this)
//                .build();
//
//        setupMentionsList();
//
//        postbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (cd.isConnectingToInternet()) {
//                    data = postEt.getText().toString().trim();
//                    final Comment comment = new Comment();
//                    comment.setComment(data);
//                    comment.setMentions(mentions.getInsertedMentions());
//                    textData.setText(data);
//
//                    data = highlightMentions(textData, comment.getMentions());
//                    if (data.isEmpty()) {
//                        Toast.makeText(PostNewActivity.this, "Please Enter your Post", Toast.LENGTH_SHORT).show();
//                    } else if (resultList.size() != 0) {
//                        postbtn.setEnabled(false);
//                        postbtn.setClickable(false);
//                        if (resultList.size() > 0) {
//                            postbtn.setEnabled(false);
//                            postbtn.setClickable(false);
//                            /*for (int i = 0; i < mAlbumFiles.size(); i++) {
//                                picturePath = mAlbumFiles.get(i).getPath();
//                                videothumbpath = mAlbumFiles.get(i).getThumbPath();*/
//                            picturePath = resultList.get(0).getmPath();
//                            videothumbpath = resultList.get(0).getmThumbPath();
//                            new SubmitPostTask().execute("", "");
//
//                            // uploadToServer(picturePath);
//                            // new SubmitPostMultiTask().execute(picturePath, videothumbpath);
//
//                           /* Intent intent = new Intent(PostNewActivity.this, BackgroundService.class);
//                            intent.putExtra("media_file", picturePath);
//                            intent.putExtra("media_file_thumb", videothumbpath);
//                            intent.putExtra("api_access_token", apikey);
//                            intent.putExtra("event_id", eventId);
//                            intent.putExtra("news_feed_id", news_feed_id1);
//                            intent.putExtra("status", data);
//                            // builder.addFormDataPart("isDebug", "1");
//                            startService(intent);*/
//
//                            /*}*/
//                            //uploadToServer(resultList.get(0));
//                        }
//                    } else {
//                        is_completed = "1";
//                        new SubmitPostTask().execute("", "");
//                    }
//                } else {
//                    Toast.makeText(PostNewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }
//
//    public void cameraTask() {
//        Log.i("android", "startStorage");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // Android M Permission check
//            if (this.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
//                ActivityCompat.requestPermissions(PostNewActivity.this,
//                        permissions, 1);
//            } else {
//                dispatchTakePictureIntent();
//            }
//        } else {
//            dispatchTakePictureIntent();
//        }
//    }
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//            }
//
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.procialize.weddingapp.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    public int calculateInSampleSize(BitmapFactory.Options options,
//                                     int reqWidth, int reqHeight) {
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//            final int heightRatio = Math.round((float) height
//                    / (float) reqHeight);
//            final int widthRatio = Math.round((float) width / (float) reqWidth);
//            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
//        }
//
//        final float totalPixels = width * height;
//        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
//        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
//            inSampleSize++;
//        }
//
//        return inSampleSize;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        /*if (requestCode == REQUEST_IMAGE) {
//            if (resultCode == RESULT_OK) {
//                if (resultList != null && path != null) {
//                    resultList.clear();
//                    path.clear();
//                }
//                path = data.getStringArrayListExtra(EXTRA_RESULT);
//                resultList = data.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
//                setPagerAdapter(resultList);
//            }
//        }*/
//    }
//
//    public void showResponse(Response<PostTextFeed> response) {
//        if (response.body().getStatus().equals("success")) {
//            Intent i = new Intent(this, HomeActivity.class);
//            startActivity(i);
//            finish();
//        } else {
//            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void showProgress(String fileName) {
//
//        try {
//        /*postbtn.setEnabled(false);
//        postEt.setEnabled(false);
//        progressbar.setVisibility(View.VISIBLE);
//        progressbar.setProgress(10);
//        progressbar.setMaxValue(100);
//        progressbar.setProgressColor(Color.parseColor(colorActive));
//        progressbar.setText(String.valueOf(0));
//        progressbar.setTextColor(Color.parseColor(colorActive));
//        progressbar.setSuffix("%");
//        progressbar.setPrefix("");*/
//            progressDialog = new ProgressDialog(this);
//            progressDialog.setMessage("Wait while uploading " + fileName);
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void dismissProgress() {
//        try {
//            postbtn.setEnabled(true);
//            postEt.setEnabled(true);
//       /* if (progressbar.getVisibility() == View.VISIBLE) {
//            progressbar.setVisibility(View.GONE);
//        }*/
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            if (progressDialog != null && progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onQueryReceived(String s) {
//        final List<AttendeeList> users = searchUsers(s);
//        if (users != null && !users.isEmpty()) {
//            usersAdapter.clear();
//            usersAdapter.setCurrentQuery(s);
//            //String[] username = new String[users.size()];
//            ArrayList<String> arr = new ArrayList<String>(users.size());
//            for (int j = 0; j < users.size(); j++) {
//                arr.add(users.get(j).getAttendeeId());
//            }
//
//            for (int i = 0; i < mentions.getInsertedMentions().size(); i++) {
//                String mentionName = mentions.getInsertedMentions().get(i).getMentionid();
//                //List<String> name = Arrays.asList(arr);
//                if (arr.contains(mentionName)) {
//                    int index = arr.indexOf(mentionName);
//                    users.remove(index);
//                }
//            }
//            usersAdapter.addAll(users);
//        }
//        if (users != null && !users.isEmpty()) {
//            showMentionsList(true);
//        } else {
//            showMentionsList(false);
//        }
//    }
//
//    @Override
//    public void displaySuggestions(boolean b) {
//        if (b) {
//            com.percolate.caffeine.ViewUtils.showView(this, R.id.mentions_list_layout);
//        } else {
//            com.percolate.caffeine.ViewUtils.hideView(this, R.id.mentions_list_layout);
//        }
//    }
//
//    private void showMentionsList(boolean display) {
//        com.percolate.caffeine.ViewUtils.showView(this, R.id.mentions_list_layout);
//        if (display) {
//            com.percolate.caffeine.ViewUtils.showView(this, R.id.mentions_list);
//            com.percolate.caffeine.ViewUtils.hideView(this, R.id.mentions_empty_view);
//        } else {
//            com.percolate.caffeine.ViewUtils.hideView(this, R.id.mentions_list);
//            com.percolate.caffeine.ViewUtils.showView(this, R.id.mentions_empty_view);
//        }
//
//    }
//
//    private void setupMentionsList() {
//        final RecyclerView mentionsList = findViewById(R.id.mentions_list);
//        mentionsList.setLayoutManager(new LinearLayoutManager(this));
//        usersAdapter = new UsersAdapter(this);
//        mentionsList.setAdapter(usersAdapter);
//        InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputManager.hideSoftInputFromWindow(postEt.getWindowToken(), 0);
//        // set on item click listener
//        mentionsList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(final View view, final int position) {
//                final AttendeeList user = usersAdapter.getItem(position);
//
//                /*
//                 * We are creating a mentions object which implements the
//                 * <code>Mentionable</code> interface this allows the library to set the offset
//                 * and length of the mention.
//                 */
//                if (user != null) {
//                    final Mention mention = new Mention();
//                    //  mention.setMentionName(user.getFirstName() + " " + user.getLastName() + " ");
//                    mention.setMentionName(user.getFirstName() + " ");
//
//                    mention.setMentionid(user.getAttendeeId());
//                    mentions.insertMention(mention);
//
//                }
//            }
//        }));
//    }
//
//    public List<AttendeeList> searchUsers(String query) {
//        final List<AttendeeList> searchResults = new ArrayList<>();
//        if (StringUtils.isNotBlank(query)) {
//            query = query.toLowerCase(Locale.US);
//            if (userList != null && !userList.isEmpty()) {
//                for (AttendeeList user : userList) {
//                    final String firstName = user.getFirstName().toLowerCase();
//                    //  final String lastName = user.getLastName().toLowerCase();
//                    if (firstName.startsWith(query) /*|| lastName.startsWith(query)*/) {
//                        searchResults.add(user);
//                    }
//                }
//            }
//
//        }
//        return searchResults;
//    }
//
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
//                        if (commentTextView.length() >= end - 1) {
//
//
////                        spannable.append(sample, start+1, end+1);
//
//                            sample = "<" + mention.getMentionid() + "^" + commentTextView.getText().toString().substring(start, end - 1) + ">";
//
////                        commentTextView = commentTextView.substring(start, end) + ">" + commentTextView.substring(end, commentTextView.length());
//                            spannable.setSpan(sample, start, end - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            spannable.replace(start, end - 1, sample);
//                            commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
//                            flag = 2;
//                        }
//                    } else if (mentions.indexOf(mention) < 3) {
//
//                        /*<23553-Sneha Deshmukh><23371-Anis Ansari><23362-Atish k>Bharat Rawal */
//                        start = mention.getMentionOffset() + 6 * mentions.indexOf(mention);
//                        end = start + mention.getMentionLength() + 1;
//                        if (commentTextView.length() >= end) {
//                            sample = "<" + mention.getMentionid() + "^" + commentTextView.getText().toString().substring(start + 1, end - 1) + ">";
//
////                        commentTextView = commentTextView.substring(start, end) + ">" + commentTextView.substring(end, commentTextView.length());
//                            spannable.setSpan(sample, start + mentions.indexOf(mention), end - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            spannable.replace(start + mentions.indexOf(mention), end - 1
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
//
//    @Override
//    public void onProgressUpdate(int percentage) {
//        Log.e("per", String.valueOf(percentage));
////        progressbar.showValue(99f, 100f, true);
//        progressbar.setProgress(percentage);
//        progressbar.setText(String.valueOf(percentage));
//    }
//
//    @Override
//    public void onError() {
//
//    }
//
//    @Override
//    public void onFinish() {
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.LinSelectorButton:
//
//                selectAlbum();
//              /*  if (resultList != null) {
//                    for (int i = 0; i < resultList.size(); i++) {
//                        arrayList.add(i, resultList.get(i));
//                    }
//                }*/
//                /*Intent intent = new Intent(PostNewActivity.this, MultiImageSelectorActivity.class);
//// whether show camera
//                intent.putExtra(EXTRA_SHOW_CAMERA, true);
//// max select image amount
//                intent.putExtra(EXTRA_SELECT_COUNT, 10);
//// select mode (MultiImageSelectorActivityNew.MODE_SINGLE OR MultiImageSelectorActivityNew.MODE_MULTI)
//                intent.putExtra(EXTRA_SELECT_MODE, MODE_MULTI);
//// default select images (support array list)
//                intent.putStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST, arrayList);
//                startActivityForResult(intent, REQUEST_IMAGE);*/
//                break;
//            default:
//                break;
//        }
//    }
//
//    public boolean CheckingPermissionIsEnabledOrNot() {
//
//        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
//        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
//        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
//        int SixthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), VIBRATE);
//
//        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
//                ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
//                FifthPermissionResult == PackageManager.PERMISSION_GRANTED &&
//                SixthPermissionResult == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void RequestMultiplePermission() {
//
//        // Creating String Array with Permissions.
//        ActivityCompat.requestPermissions(PostNewActivity.this, new String[]
//                {
//                        CAMERA,
//                        WRITE_EXTERNAL_STORAGE,
//                        READ_EXTERNAL_STORAGE,
//                        VIBRATE
//                }, RequestPermissionCode);
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//
//            case RequestPermissionCode:
//
//                if (grantResults.length > 0) {
//
//                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean readstoragepermjission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    boolean writestoragepermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
//                    boolean vibratepermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
//
//                    if (CameraPermission && readstoragepermjission && writestoragepermission && vibratepermission) {
////                        Toast.makeText(PostNewActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(PostNewActivity.this, "We need your permission so you can enjoy full features of app", Toast.LENGTH_LONG).show();
//                        RequestMultiplePermission();
//
//                    }
//                }
//
//                break;
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        JZVideoPlayer.releaseAllVideos();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        JZVideoPlayer.releaseAllVideos();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        JZVideoPlayer.releaseAllVideos();
//    }
//
//    private void selectAlbum() {
//        Album.album(this)
//                .multipleChoice()
//                .columnCount(2)
//                .selectCount(10)
//                .camera(true)
//                .cameraVideoQuality(1)
//                .cameraVideoLimitDuration(15000)
//                //.cameraVideoLimitBytes(Integer.MAX_VALUE)
//                /*.filterMimeType(new Filter<String>() { // MimeType of File.
//                    @Override
//                    public boolean filter(String attributes) {
//                        // MimeType: image/jpeg, image/png, video/mp4, video/3gp...
//                        return attributes.contains("gif");
//                    }
//                })*/
//                .checkedList(mAlbumFiles)
//                .widget(
//                        Widget.newDarkBuilder(this)
//                                .toolBarColor(getResources().getColor(R.color.active_menu))
//                                .statusBarColor(getResources().getColor(R.color.grey))
//                                .title("Select Image/Video")
//                                .mediaItemCheckSelector(getResources().getColor(R.color.white), getResources().getColor(R.color.active_menu))
//                                //.bucketItemCheckSelector(getResources().getColor(R.color.white),getResources().getColor(R.color.active_menu))
//                                //.title(toolbar.getTitle().toString())
//                                .build()
//                )
//                .onResult(new Action<ArrayList<AlbumFile>>() {
//                    @Override
//                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
//                        try {
//                            mAlbumFiles = result;
//                            //compressVideo();
//                            //resultList.clear();
//
//                            //Get Original paths from selected arraylist
//                            List selectedFileList = new ArrayList();
//                            for (int i = 0; i < resultList.size(); i++) {
//                                selectedFileList.add(resultList.get(i).getmOriginalFilePath());
//                            }
//
//                            //Get Data from Album
//                            List albumFileList = new ArrayList();
//                            for (int k = 0; k < mAlbumFiles.size(); k++) {
//                                albumFileList.add(mAlbumFiles.get(k).getPath());
//                            }
//
//                            //If user deselects image/video then remove that from resultList
//                            if (selectedFileList.size() > 0) {
//                                for (int l = 0; l < selectedFileList.size(); l++) {
//                                    if (!albumFileList.contains(selectedFileList.get(l))) {
//                                        selectedFileList.remove(l);
//                                        resultList.remove(l);
//                                    }
//                                }
//                            }
//
//                            String strMediaType;
//
//                            for (int j = 0; j < mAlbumFiles.size(); j++) {
//                                //To check selected image/video is already present in previous arraylist
//                                if (!selectedFileList.contains(mAlbumFiles.get(j).getPath())) {
//                                    if (mAlbumFiles.get(j).getMediaType() == AlbumFile.TYPE_VIDEO) {
//                                        strMediaType = "video";
//                                    } else {
//                                        strMediaType = "image";
//                                    }
//
//                                    resultList.add(new SelectedImages(mAlbumFiles.get(j).getPath(),
//                                            mAlbumFiles.get(j).getPath(), mAlbumFiles.get(j).getThumbPath(), false, strMediaType));
//                                    if (mAlbumFiles.get(j).getMediaType() == AlbumFile.TYPE_VIDEO) {
//                                        videoPositionArray.add(j);
//                                    }
//                                }
//                            }
//
//                            try {
//                                /*if (videoPositionArray.size() > 0) {
//                                    if (mAlbumFiles.get(videoPositionArray.get(0)).getMediaType() == AlbumFile.TYPE_VIDEO) {
//                                        String strPath = mAlbumFiles.get(videoPositionArray.get(0)).getPath();
//                                        executeCutVideoCommand(startMsForVideoCutting, endMsForVideoCutting, Uri.parse(strPath), videoPositionArray.get(0));
//                                    }
//                                } else {*/
//                                setPagerAdapter(resultList);
//                                //}
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                })
//                .onCancel(new Action<String>() {
//                    @Override
//                    public void onAction(@NonNull String result) {
//                        Toast.makeText(PostNewActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
//                    }
//                })
//                .start();
//    }
//
//    /**
//     * Command for cutting video
//     */
//    private String executeCutVideoCommand(int startMs, int endMs, Uri selectedVideoUri, int position) {
//      /*  File moviesDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_MOVIES
//        );
//*/
//        String root = Environment.getExternalStorageDirectory().toString();
//        File moviesDir = new File(root + "/TheWeddingApp/Video");
//
//        if (!moviesDir.exists()) {
//            moviesDir.mkdirs();
//        }
//        String path = selectedVideoUri.getPath();
//        int cut = path.lastIndexOf('/');
//        if (cut != -1) {
//            path = path.substring(cut + 1);
//        }
//        /*String filePrefix = "cut_video";
//        String fileExtn = ".mp4";
//        //String yourRealPath = getPath(PostNewActivity.this, selectedVideoUri);
//        File dest = new File(moviesDir, filePrefix + fileExtn);
//        int fileNo = 0;
//        while (dest.exists()) {
//            fileNo++;
//            dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
//
//        }*/
//        String fileExtn = ".mp4";
//        String filePrefix = path.replace(".mp4", "");
//        File dest = new File(moviesDir, filePrefix + fileExtn);
//
//
//        int fileNo = 0;
//        while (dest.exists()) {
//            fileNo++;
//            dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
//        }
//        //fileNo++;
//        // File dest = new File(moviesDir, filePrefix + fileExtn);
//        //Log.d(TAG, "startTrim: src: " + yourRealPath);
//        Log.d(TAG, "startTrim: dest: " + dest.getAbsolutePath());
//        Log.d(TAG, "startTrim: startMs: " + startMs);
//        Log.d(TAG, "startTrim: endMs: " + endMs);
//        String filePath = dest.getAbsolutePath();
//       /* String[] complexCommand = {"-ss", "" + startMs / 1000,
//                "-y", "-i", selectedVideoUri.toString(),
//                "-preset", "ultrafast",
//                "-t", "" + (endMs - startMs) / 1000,
//                "-vcodec", "mpeg4",
//                "-b:v", "2097152",
//                "-b:a", "48000",
//                "-ac", "2",
//                "-ar", "22050",
//                "-preset", "ultrafast",
//                "-crf", "23",
//                "-c:a", "copy",
//                "-fs", "17000000",
//                filePath,
//                "-b", "512k"};*/
//
//        String[] complexCommand = {
//                "-y", "-i", selectedVideoUri.toString(),
//                /*"-ss", "" + startMs / 1000,
//                 "-t", "" + (endMs - startMs) / 1000,*/
//                "-ss", "" + startMs / 1000,
//                "-t", "" + (endMs - startMs) / 1000,
//                "-vcodec", "mpeg4",
//                "-b:v", "2097152",
//                "-b:a", "48000",
//                "-ac", "2",
//                "-ar", "22050",
//                "-preset", "ultrafast",
//                "-crf", "51",
//                // "-fs", "17000000",
//                "-c:a", "copy",
//                filePath,
//                "-b", "512k"};
//
//        execFFmpegBinary(complexCommand, filePath);
//
//        return filePath;
//    }
//
//    /**
//     * Executing ffmpeg binary
//     */
//    private String execFFmpegBinary(final String[] command, String outputPath) {
//        try {
//            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
//                @Override
//                public void onFailure(String s) {
//                    Log.d(TAG, "FAILED with output : " + s);
//                }
//
//                @Override
//                public void onSuccess(String s) {
//                    Log.d(TAG, "SUCCESS with output : " + s);
//                    //progressDialog.setMessage("progress : " + s);
//                    if (videoPositionArray.size() != 0) {
//                        int pos = videoPositionArray.get(0);
//                        String thumbPath = mAlbumFiles.get(pos).getThumbPath();
//                        String originalPath = mAlbumFiles.get(pos).getPath();
//                        resultList.set(pos, new SelectedImages(originalPath, outputPath, thumbPath, true, "video"));
//                        videoPositionArray.remove(0);
//
//                        if (videoPositionArray.size() != 0) {
//                            executeCutVideoCommand(startMsForVideoCutting, endMsForVideoCutting, Uri.parse(mAlbumFiles.get(videoPositionArray.get(0)).getPath()), videoPositionArray.get(0));
//                        } else {
//                            setPagerAdapter(resultList);
//                        }
//                    }
//                }
//
//                @Override
//                public void onProgress(String s) {
//                    Log.d(TAG, "Started command : ffmpeg " + command);
//
//                    progressDialog.setMessage("Wait while loading...");
//                    //progressDialog.setMessage("progress : " + s);
//                    Log.d(TAG, "progress : " + s);
//                }
//
//                @Override
//                public void onStart() {
//                    Log.d(TAG, "Started command : ffmpeg " + command);
//                    // progressDialog.setMessage("Processing...");
//                    progressDialog.show();
//                }
//
//                @Override
//                public void onFinish() {
//                    Log.d(TAG, "Finished command : ffmpeg " + command);
//                    if (videoPositionArray.size() == 0) {
//                        progressDialog.dismiss();
//                    }
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            e.printStackTrace();
//        }
//        return outputPath;
//    }
//
//    /**
//     * Load FFmpeg binary
//     */
//    private void loadFFMpegBinary() {
//        try {
//            if (ffmpeg == null) {
//                Log.d(TAG, "ffmpeg : era nulo");
//                ffmpeg = FFmpeg.getInstance(this);
//            }
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
//                @Override
//                public void onFailure() {
//                    //showUnsupportedExceptionDialog();
//                }
//
//                @Override
//                public void onSuccess() {
//                    Log.d(TAG, "ffmpeg : correct Loaded");
//                }
//            });
//        } catch (FFmpegNotSupportedException e) {
//            //showUnsupportedExceptionDialog();
//        } catch (Exception e) {
//            Log.d(TAG, "EXception no controlada : " + e);
//        }
//    }
//
//    /**
//     * View Pager adapter for selected images and videos
//     */
//    private void setPagerAdapter(List<SelectedImages> resultList) {
//        viewPagerAdapter = new ViewPagerAdapter(PostNewActivity.this, resultList);
//        viewPagerAdapter.notifyDataSetChanged();
//        viewPager.setAdapter(viewPagerAdapter);
//        dotscount = viewPagerAdapter.getCount();
//        dots = new ImageView[dotscount];
//
//        sliderDotspanel.removeAllViews();
//        for (int i = 0; i < dotscount; i++) {
//            dots[i] = new ImageView(PostNewActivity.this);
//            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(8, 0, 8, 0);
//            sliderDotspanel.addView(dots[i], params);
//        }
//        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//                for (int i = 0; i < dotscount; i++) {
//                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
//                }
//                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }
//
//    public class SubmitPostTask extends AsyncTask<String, String, JSONObject> {
//
//        InputStream is = null;
//        JSONObject json = null;
//        JSONObject status;
//        String message = "";
//        String error = "";
//        String msg = "";
//        String news_feed_id1 = "";
//        String res = null;
//
//        @Override
//        protected void onPreExecute() {
//
//            super.onPreExecute();
//            // Showing progress dialog
//            showProgress("");
//
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... params) {
//            try {
//                OkHttpClient client = null;
//                try {
//                    URL url = new URL(postUrlmulti);
//                    client = getUnsafeOkHttpClient().newBuilder().build();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                MultipartBody.Builder builder = new MultipartBody.Builder();
//                builder.setType(MultipartBody.FORM);
//                builder.addFormDataPart("api_access_token", apikey);
//                builder.addFormDataPart("event_id", eventId);
//                builder.addFormDataPart("type", "image");
//                builder.addFormDataPart("status", StringEscapeUtils.escapeJava(data));
//                builder.addFormDataPart("is_completed",is_completed);
//
//                RequestBody requestBody = builder.build();
//                Request request = new Request.Builder()
//                        .url(postUrlmulti)
//                        .post(requestBody)
//                        .build();
//
//                HttpEntity httpEntity = null;
//                okhttp3.Response response = null;
//
//
//                try {
//                    response = client.newCall(request).execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //res = response.body().string();
//                httpEntity = transformResponse(response).getEntity();
//                res = EntityUtils.toString(httpEntity);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            // try parse the string to a JSON object
//            try {
//                json = new JSONObject(res);
//                error = json.getString("status");
//                message = json.getString("msg");
//                news_feed_id1 = json.getString("news_feed_id");
//            } catch (JSONException e) {
//                Log.e("JSON Parser", "Error parsing data " + e.toString());
//            } catch (NullPointerException e) {
//                Log.e("JSON Parser", "Error parsing data " + e.toString());
//            }
//            return json;
//
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject result) {
//
//            super.onPostExecute(result);
//
//            if (error.equalsIgnoreCase("success")) {
//
//
//                if (resultList.size() > 0) {
//                    Toast.makeText(PostNewActivity.this, message + " Your multimedia will be uploaded shortly..!", Toast.LENGTH_SHORT)
//                            .show();
//
//                    postbtn.setEnabled(false);
//                    postbtn.setClickable(false);
//                            /*for (int i = 0; i < mAlbumFiles.size(); i++) {
//                                picturePath = mAlbumFiles.get(i).getPath();
//                                videothumbpath = mAlbumFiles.get(i).getThumbPath();*/
//
//                    ArrayList<NewsFeedPostMultimedia> arrayListNewsFeedMultiMedia = new ArrayList<>();
//                    for (int i = 0; i < resultList.size(); i++) {
//                        picturePath = resultList.get(i).getmPath();
//                        videothumbpath = resultList.get(i).getmThumbPath();
//                        NewsFeedPostMultimedia newsFeedPostMultimedia = new NewsFeedPostMultimedia();
//                        newsFeedPostMultimedia.setMedia_file(picturePath);
//                        newsFeedPostMultimedia.setMedia_file_thumb(videothumbpath);
//                        newsFeedPostMultimedia.setNews_feed_id(news_feed_id1);
//                        newsFeedPostMultimedia.setCompressedPath("");
//                        newsFeedPostMultimedia.setMedia_type(resultList.get(i).getmMediaType());
//                        arrayListNewsFeedMultiMedia.add(newsFeedPostMultimedia);
//                    }
//
//                    SQLiteDatabase db = dbHelper.getWritableDatabase();
//                    dbHelper.insertUploadMultimediaInfo(arrayListNewsFeedMultiMedia, news_feed_id1, db);
//
//                    Intent MainIntent = new Intent(PostNewActivity.this, HomeActivity.class);
//                    startActivity(MainIntent);
//                    finish();
//                    /*if (arrayListNewsFeedMultiMedia.size() > 0) {
//                        Intent intent = new Intent(PostNewActivity.this, BackgroundService.class);
//                        intent.putExtra("arrayListNewsFeedMultiMedia", arrayListNewsFeedMultiMedia);
//                        intent.putExtra("api_access_token", apikey);
//                        intent.putExtra("event_id", eventId);
//                        intent.putExtra("status", data);
//                        intent.putExtra("isNew", true);
//                        startService(intent);
//                    }*/
//
//                    /*}*/
//                    //uploadToServer(resultList.get(0));
//                } else {
//                    Intent MainIntent = new Intent(PostNewActivity.this, HomeActivity.class);
//                    startActivity(MainIntent);
//                    finish();
//                }
//            } else {
//                Toast.makeText(PostNewActivity.this, message, Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }
//    }
//}