package com.procialize.frontier.NewsFeed.Views.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.percolate.mentions.Mentionable;
import com.percolate.mentions.Mentions;
import com.percolate.mentions.QueryListener;
import com.percolate.mentions.SuggestionsListener;
import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.CustomTools.RecyclerItemClickListener;
import com.procialize.frontier.DbHelper.ConnectionDetector;
import com.procialize.frontier.DbHelper.DBHelper;
import com.procialize.frontier.GetterSetter.AttendeeList;
import com.procialize.frontier.GetterSetter.Comment;
import com.procialize.frontier.GetterSetter.EventSettingList;
import com.procialize.frontier.GetterSetter.Mention;
import com.procialize.frontier.GetterSetter.NewsFeedPostMultimedia;
import com.procialize.frontier.GetterSetter.SelectedImages;
import com.procialize.frontier.MergeMain.MrgeHomeActivity;
import com.procialize.frontier.NewsFeed.Views.Adapter.UsersAdapter;
import com.procialize.frontier.NewsFeed.Views.Adapter.ViewPagerMultimediaAdapter;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.MediaLoader;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import cn.jzvd.JzvdStd;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.procialize.frontier.NewsFeed.Views.Fragment.FragmentNewsFeed.isFinishedService;
import static com.procialize.frontier.Session.ImagePathConstants.KEY_PROFILE_PIC_PATH;
import static com.procialize.frontier.Session.SessionManager.MY_PREFS_NAME;
import static com.procialize.frontier.UnsafeOkHttpClient.getUnsafeOkHttpClient;
import static org.apache.http.HttpVersion.HTTP_1_1;

public class PostNewActivity extends AppCompatActivity implements View.OnClickListener, SuggestionsListener, QueryListener {

    EditText posttextEt;
    TextView txtcount;
    LinearLayout llUploadMedia,ll_count;
    List<SelectedImages> resultList = new ArrayList<SelectedImages>();
    ArrayList<Integer> videoPositionArray = new ArrayList<Integer>();
    ViewPagerMultimediaAdapter viewPagerAdapter;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    TextView postbtn, tv_name;
    String postMsg = "";
    TextView textData;
    String is_completed = "0";
    String folderUniqueId;
    String news_feed_id1 = "";
    ConnectionDetector cd;
    SQLiteDatabase db;
    String postUrlmulti;
    SessionManager session;
    String apikey, eventId;
    LinearLayout linear;
    ImageView headerlogoIv;
    ImageView profileIV;
    ProgressBar progressView;
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();//Array For selected images and videos
    private int dotscount;
    private ImageView[] dots;
    private Mentions mentions;
    private List<AttendeeList> userList;
    private String picturePath = "";
    private String videothumbpath = "";
    private UsersAdapter usersAdapter;
    private DBHelper procializeDB;
    private ArrayList<AttendeeList> customers;
    private List<EventSettingList> eventSettingLists;
    String news_feed_post = "1", news_feed_images = "1", news_feed_video = "1", news_feed_gif = "0", designatio = "1";
    String news_feed_like = "0", news_feed_comment = "0", news_feed_share = "0";
    boolean isTextPost;

    String colorActive;

    //==============================
      File from;
      File too;
      File thumbpath;
      File thumb;
      File tothumb;
      File tovideo;
    String videofile, imagefile;
      //=============================
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new);

        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new MediaLoader())
                .setLocale(Locale.getDefault())
                .build()
        );

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
        init();
    }

    private void init() {

        linear = findViewById(R.id.linear);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        profileIV = findViewById(R.id.profileIV);
        headerlogoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        try {
//            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/" + ApiConstant.folderName + "/" + "background.jpg");
//            Resources res = getResources();
//            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
//            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
//            linear.setBackgroundDrawable(bd);
//
//            Log.e("PATH", String.valueOf(mypath));
//        } catch (Exception e) {
//            e.printStackTrace();
//            linear.setBackgroundResource(R.drawable.background_login);
//        }


        cd = new ConnectionDetector(this);
        llUploadMedia = (LinearLayout) findViewById(R.id.llUploadMedia);

        ll_count = (LinearLayout) findViewById(R.id.ll_count);
        llUploadMedia.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                JzvdStd.goOnPlayOnPause();
            }

            @Override
            public void onPageSelected(int position1) {
                JzvdStd.goOnPlayOnPause();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                JzvdStd.goOnPlayOnPause();
            }
        });

        postbtn = findViewById(R.id.postbtn);
        postbtn.setOnClickListener(this);

        posttextEt = findViewById(R.id.posttextEt);
        txtcount = findViewById(R.id.txtcount);
        textData = findViewById(R.id.textData);
        tv_name = findViewById(R.id.tv_name);
        TextView tv_header = findViewById(R.id.tv_header);
        progressView = findViewById(R.id.progressView);
        // get user data from session

        tv_header.setTextColor(Color.parseColor(colorActive));


        ImageView imguploadimg = findViewById(R.id.imguploadimg);
        TextView txtUploadImg = findViewById(R.id.txtUploadImg);
        txtUploadImg.setTextColor(Color.parseColor(colorActive));
        imguploadimg.setColorFilter(Color.parseColor(colorActive));
//        llUploadMedia.setBackgroundColor(Color.parseColor(colorActive));
//        postbtn.setTextColor(Color.parseColor(colorActive));


        final TextWatcher txwatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start,
                                          int count, int after) {
                int tick = start + after;
                if (tick < 250) {
                    int remaining = 250 - tick;
                    // txtcount1.setText(String.valueOf(remaining));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                // txtcount.setText(String.valueOf(500 - s.length()) + "/");
                txtcount.setText(String.valueOf(s.length()));

                if (s.length() > 0) {
                    postbtn.setTextColor(getResources().getColor(R.color.colorwhite));
                    postbtn.setBackgroundColor(Color.parseColor(colorActive));
                } else {
                    postbtn.setTextColor(Color.parseColor(colorActive));
                    postbtn.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                System.out.print("Hello");
            }
        };

        posttextEt.addTextChangedListener(txwatcher);

        mentions = new Mentions.Builder(this, posttextEt)
                .suggestionsListener(PostNewActivity.this)
                .queryListener(this)
                .build();

        procializeDB = new DBHelper(PostNewActivity.this);
        db = procializeDB.getReadableDatabase();
        customers = new ArrayList<AttendeeList>();
        userList = procializeDB.getAttendeeDetails();
        procializeDB.getReadableDatabase();

        postUrlmulti = ApiConstant.baseUrl + "PostNewsFeedMultiple";

        session = new SessionManager(getApplicationContext());

        eventSettingLists = session.loadEventList();
        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }
        HashMap<String, String> user = session.getUserDetails();
        String profilepic = user.get(SessionManager.KEY_PIC);
        if (profilepic != null) {
            SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String profilePicPath  = prefs1.getString(KEY_PROFILE_PIC_PATH,"");

            Glide.with(this).load(profilePicPath/*ApiConstant.profilepic*/ + profilepic).circleCrop()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressView.setVisibility(View.GONE);
                            profileIV.setImageResource(R.drawable.profilepic_placeholder);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressView.setVisibility(View.GONE);
                            profileIV.setImageResource(R.drawable.profilepic_placeholder);
                            return false;
                        }
                    }).into(profileIV);
        } else {
            profileIV.setImageResource(R.drawable.profilepic_placeholder);
            progressView.setVisibility(View.GONE);
        }

        apikey = user.get(SessionManager.KEY_TOKEN);

        tv_name.setText(user.get(SessionManager.KEY_NAME) + " " + user.get(SessionManager.KEY_LNAME));
        SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventId = prefs1.getString("eventid", "1");

        setupMentionsList();

        if (news_feed_post.equalsIgnoreCase("1")) {
            posttextEt.setVisibility(View.VISIBLE);
            ll_count.setVisibility(View.VISIBLE);
        } else {
            posttextEt.setVisibility(View.GONE);
            ll_count.setVisibility(View.GONE);
        }

        if (news_feed_images.equalsIgnoreCase("1") && news_feed_video.equalsIgnoreCase("1") && news_feed_gif.equalsIgnoreCase("1")) {
            llUploadMedia.setVisibility(View.VISIBLE);
        } else {
            llUploadMedia.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llUploadMedia:
                selectAlbum();
                break;
            case R.id.postbtn:

                /*folderUniqueId = insertMultimediaDataToDB();
                if (cd.isConnectingToInternet()) {
                    postMsg = posttextEt.getText().toString().trim();
                    final Comment comment = new Comment();
                    comment.setComment(postMsg);
                    comment.setMentions(mentions.getInsertedMentions());
                    textData.setText(postMsg);

                    postMsg = highlightMentions(textData, comment.getMentions());
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
                            Toast.makeText(PostNewActivity.this, "Please Enter your Post", Toast.LENGTH_SHORT).show();
                        } else {
                            is_completed = "1";
                            postbtn.setEnabled(false);
                            postbtn.setClickable(false);
                            new SubmitPostTask().execute("", "");
                        }
                    }
                } else {
                    Toast.makeText(PostNewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }*/
                if (cd.isConnectingToInternet()) {
                    postMsg = posttextEt.getText().toString().trim();
                    final Comment comment = new Comment();
                    comment.setComment(postMsg);
                    comment.setMentions(mentions.getInsertedMentions());
                    textData.setText(postMsg);

                    postMsg = highlightMentions(textData, comment.getMentions());
                    if(postMsg.length()>0)
                    {isTextPost=true;}
                    else
                    {isTextPost=false;}
                    folderUniqueId = insertMultimediaDataToDB(isTextPost);

                    if (resultList.size() != 0) {
                        postbtn.setEnabled(false);
                        postbtn.setClickable(false);
                        if (resultList.size() > 0) {
                            postbtn.setEnabled(false);
                            postbtn.setClickable(false);

                            picturePath = resultList.get(0).getmPath();
                            videothumbpath = resultList.get(0).getmThumbPath();
                            if( isFinishedService) {
                                new SubmitPostTask().execute("", "");
                            }else
                            {
                                Intent MainIntent = new Intent(PostNewActivity.this, MrgeHomeActivity.class);
                                startActivity(MainIntent);
                                finish();
                            }

                        }
                    } else {
                        //folderUniqueId = insertMultimediaDataToDB();
                        if (postMsg.isEmpty()) {
                            Toast.makeText(PostNewActivity.this, "Please Enter your Post", Toast.LENGTH_SHORT).show();
                        } else {
                            is_completed = "1";

                            if( isFinishedService) {
                                new SubmitPostTask().execute("", "");
                            }else
                            {
                                Intent MainIntent = new Intent(PostNewActivity.this, MrgeHomeActivity.class);
                                startActivity(MainIntent);
                                finish();
                            }
                        }
                    }
                } else {
                    Toast.makeText(PostNewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

  /*  private void selectAlbum() {

        Album.album(this)
                .multipleChoice()
                .columnCount(2)
                .selectCount(10)
                .camera(true)
                .cameraVideoQuality(1)
                .cameraVideoLimitDuration(30)
                //.cameraVideoLimitBytes(Integer.MAX_VALUE)
                *//*.filterMimeType(new Filter<String>() { // MimeType of File.
                    @Override
                    public boolean filter(String attributes) {
                        // MimeType: image/jpeg, image/png, video/mp4, video/3gp...
                        return attributes.contains("gif");
                    }
                })*//*
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
                                from = null;
                                too = null;
                                thumbpath = null;
                                thumb = null;
                                tothumb = null;
                                tovideo = null;
                                //To check selected image/video is already present in previous arraylist
                                if (!selectedFileList.contains(mAlbumFiles.get(j).getPath())) {
                                    if (mAlbumFiles.get(j).getMediaType() == AlbumFile.TYPE_VIDEO) {
                                        strMediaType = "video";
                                        Random r = new Random();
                                        int i1 = r.nextInt(80 - 65) + 65;
                                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                        videofile = "video_" + i1 + timeStamp + "_";
//                                        thumbfile = "thumb" + timeStamp + "_";
                                        from = new File(mAlbumFiles.get(j).getPath());
//                                        thumbpath = new File(mAlbumFiles.get(j).getThumbPath());

                                        String root = Environment.getExternalStorageDirectory().toString();
                                        File moviesDir = new File(root + "/MrgeApp/Video");

                                        if (from != null) {
                                            copyFile(mAlbumFiles.get(j).getPath(), from.getName(), moviesDir.toString());
                                        }

//                                        if (thumbpath != null) {
//                                            copyFile(mAlbumFiles.get(j).getThumbPath(), thumbpath.getName(), moviesDir.toString());
//                                        }

                                        try {
                                            thumb = new File(moviesDir + "/" + from.getName());
                                            if (moviesDir.exists()) {
                                                tovideo = new File(moviesDir, videofile + ".mp4");
                                                if (thumb.exists())
                                                    thumb.renameTo(tovideo);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            thumb = new File(mAlbumFiles.get(j).getPath());
                                        }


                                    } else {
                                        strMediaType = "image";
                                        Random r = new Random();
                                        int i1 = r.nextInt(80 - 65) + 65;
                                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                        imagefile = "image_" + i1 + timeStamp + "_";
//                                        String thumbfile = "thumb" + timeStamp + "_";
                                        from = new File(mAlbumFiles.get(j).getPath());
//                                        thumbpath = new File(mAlbumFiles.get(j).getThumbPath());

                                        String root = Environment.getExternalStorageDirectory().toString();
                                        File moviesDir = new File(root + "/MrgeApp/Video");



                                        if (from != null) {
                                            String originalVideoPath = mAlbumFiles.get(j).getPath();
                                            String targetVideoPath = from.getName();
                                            copyFile(originalVideoPath, targetVideoPath, moviesDir.toString());
                                        }


                                        try {
                                            thumb = new File(moviesDir + "/" + from.getName());
                                            if (moviesDir.exists()) {
                                                if (from.getName().contains("gif")) {
                                                    tovideo = new File(moviesDir, imagefile + ".gif");
                                                } else {
                                                    tovideo = new File(moviesDir, imagefile + ".png");
                                                }
                                                if (thumb.exists())
                                                    thumb.renameTo(tovideo);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            thumb = new File(mAlbumFiles.get(j).getPath());
                                        }

                                    }

                                    resultList.add(new SelectedImages(tovideo.getPath(),
                                            tovideo.getPath(), mAlbumFiles.get(j).getThumbPath(), false, strMediaType));
                                    if (mAlbumFiles.get(j).getMediaType() == AlbumFile.TYPE_VIDEO) {

                                        videoPositionArray.add(j);
                                    }
                                }
                            }

                            try {
                            *//*if (videoPositionArray.size() > 0) {
                                if (mAlbumFiles.get(videoPositionArray.get(0)).getMediaType() == AlbumFile.TYPE_VIDEO) {
                                    String strPath = mAlbumFiles.get(videoPositionArray.get(0)).getPath();
                                    executeCutVideoCommand(startMsForVideoCutting, endMsForVideoCutting, Uri.parse(strPath), videoPositionArray.get(0));
                                }
                            } else {*//*
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
                        Toast.makeText(PostNewActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }*/

    private void copyFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

//create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath + "/" + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

// write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        } catch (FileNotFoundException fnfe1) {
            Log.e("tag11111", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }
    private void selectAlbum() {
        Album.album(this)
                .multipleChoice()
                .columnCount(2)
                .selectCount(10)
                .camera(true)
                .cameraVideoQuality(1)
                .cameraVideoLimitDuration(15)

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
                            if (result.size() > 0) {
                                postbtn.setTextColor(getResources().getColor(R.color.colorwhite));
                                postbtn.setBackgroundColor(Color.parseColor(colorActive));
                            } else {
                                postbtn.setTextColor(Color.parseColor(colorActive));
                                postbtn.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                            }
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
                                setPagerAdapter(resultList);
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
                        Toast.makeText(PostNewActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    private void setPagerAdapter(List<SelectedImages> resultList) {
        viewPagerAdapter = new ViewPagerMultimediaAdapter(PostNewActivity.this, resultList);
        viewPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        sliderDotspanel.removeAllViews();
        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(PostNewActivity.this);
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

    private String highlightMentions(TextView commentTextView, final List<Mentionable> mentions) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commentTextView.getText().toString();
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
                    String lastName = "";
                    try {
                        if (!user.getLastName().isEmpty()) {
                            lastName = user.getLastName().toLowerCase();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (firstName.startsWith(query) || lastName.startsWith(query)) {
                        searchResults.add(user);
                    }
                }
            }

        }
        return searchResults;
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


    public String insertMultimediaDataToDB(boolean isTextPost) {
        ArrayList<NewsFeedPostMultimedia> arrayListNewsFeedMultiMedia = new ArrayList<>();
        Date date = new Date();
        long time = date.getTime();
       //Timestamp ts = new Timestamp(time);
        if(isTextPost)
        {
            NewsFeedPostMultimedia newsFeedPostMultimedia = new NewsFeedPostMultimedia();
            newsFeedPostMultimedia.setMedia_file("");
            newsFeedPostMultimedia.setMedia_file_thumb("");
            newsFeedPostMultimedia.setNews_feed_id("");
            newsFeedPostMultimedia.setCompressedPath("");
            newsFeedPostMultimedia.setMedia_type("text");
            newsFeedPostMultimedia.setFolderUniqueId(time+"");
            newsFeedPostMultimedia.setPostText(postMsg);
            arrayListNewsFeedMultiMedia.add(newsFeedPostMultimedia);
           /* NewsFeedPostMultimedia newsFeedPostMultimedia = new NewsFeedPostMultimedia();
            newsFeedPostMultimedia.setMedia_type("text");
            newsFeedPostMultimedia.setFolderUniqueId(ts.toString());
            newsFeedPostMultimedia.setPostText(data);
            arrayListNewsFeedMultiMedia.add(newsFeedPostMultimedia);*/
        }
        if(resultList.size()>0) {
            for (int i = 0; i < resultList.size(); i++) {
                picturePath = resultList.get(i).getmPath();
                videothumbpath = resultList.get(i).getmThumbPath();
                NewsFeedPostMultimedia newsFeedPostMultimedia = new NewsFeedPostMultimedia();
                newsFeedPostMultimedia.setMedia_file(picturePath);
                newsFeedPostMultimedia.setMedia_file_thumb(videothumbpath);
                newsFeedPostMultimedia.setNews_feed_id(news_feed_id1);
                newsFeedPostMultimedia.setCompressedPath("");
                newsFeedPostMultimedia.setMedia_type(resultList.get(i).getmMediaType());
                newsFeedPostMultimedia.setFolderUniqueId(time+"");
                //if(resultList.get(i).getmMediaType().equalsIgnoreCase("text")) {
                newsFeedPostMultimedia.setPostText(postMsg);
                /*}else
                {
                    newsFeedPostMultimedia.setPostText("");
                }*/
                arrayListNewsFeedMultiMedia.add(newsFeedPostMultimedia);
            }
        }


        SQLiteDatabase db = procializeDB.getWritableDatabase();
        procializeDB.insertUploadMultimediaInfo(arrayListNewsFeedMultiMedia, news_feed_id1, db);

        return time+"";
    }


 /*   public String insertMultimediaDataToDB() {

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

        SQLiteDatabase db = procializeDB.getWritableDatabase();
        procializeDB.insertUploadMultimediaInfo(arrayListNewsFeedMultiMedia, news_feed_id1, db);

        return ts.toString();
    }*/

    private void setupMentionsList() {
        final RecyclerView mentionsList = findViewById(R.id.mentions_list);
        mentionsList.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UsersAdapter(this);
        mentionsList.setAdapter(usersAdapter);
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(posttextEt.getWindowToken(), 0);
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
                    Toast.makeText(PostNewActivity.this, message + " Your multimedia will be uploaded shortly..!", Toast.LENGTH_SHORT)
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
                    SQLiteDatabase db = procializeDB.getWritableDatabase();
                    procializeDB.updateNewsFeedId(news_feed_id1, folderUniqueId, db);

                    Intent MainIntent = new Intent(PostNewActivity.this, MrgeHomeActivity.class);
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
                    SQLiteDatabase db = procializeDB.getWritableDatabase();
                    procializeDB.updateNewsFeedId(news_feed_id1, folderUniqueId, db);

                    Intent MainIntent = new Intent(PostNewActivity.this, MrgeHomeActivity.class);
                    startActivity(MainIntent);
                    finish();
                }

                if (is_completed.equalsIgnoreCase("1")) {
                    Toast.makeText(PostNewActivity.this, "Post uploaded successfully..!!", Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                Toast.makeText(PostNewActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {
            if (eventSettingLists.get(i).getFieldName().equals("news_feed")) {
                if (eventSettingLists.get(i).getSub_menuList() != null) {
                    if (eventSettingLists.get(i).getSub_menuList().size() > 0) {
                        for (int k = 0; k < eventSettingLists.get(i).getSub_menuList().size(); k++) {
                            if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_post")) {
                                news_feed_post = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_images")) {
                                news_feed_images = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_video")) {
                                news_feed_video = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_comment")) {
                                news_feed_comment = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_like")) {
                                news_feed_like = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_share")) {
                                news_feed_share = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_gif")) {
                                news_feed_gif = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }
                        }
                    }
                }
            }
        }
    }
}