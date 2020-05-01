package com.procialize.mrgeApp20.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.crashlytics.android.Crashlytics;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.YouTubePlayerTracker;
import com.pipvideo.youtubepipvideoplayer.FlyingVideo;
import com.pipvideo.youtubepipvideoplayer.TaskCoffeeVideo;
import com.procialize.mrgeApp20.Adapter.CustomMenuAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.Background.WallPostService;
import com.procialize.mrgeApp20.BuildConfig;
import com.procialize.mrgeApp20.CustomTools.CustomViewPager;
import com.procialize.mrgeApp20.CustomTools.MyJZVideoPlayerStandard;
import com.procialize.mrgeApp20.CustomTools.PicassoTrustAll;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.EmptyViewActivity;
import com.procialize.mrgeApp20.Fragments.AgendaFolderFragment;
import com.procialize.mrgeApp20.Fragments.AgendaFragment;
import com.procialize.mrgeApp20.Fragments.AllExhibitorFragment;
import com.procialize.mrgeApp20.Fragments.AttendeeFragment;
import com.procialize.mrgeApp20.Fragments.ExhiCatFragment;
import com.procialize.mrgeApp20.Fragments.GeneralInfo;
import com.procialize.mrgeApp20.Fragments.SpeakerFragment;
import com.procialize.mrgeApp20.Fragments.WallFragment_POST;
import com.procialize.mrgeApp20.GetterSetter.AgendaList;
import com.procialize.mrgeApp20.GetterSetter.EventMenuSettingList;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.GetterSetter.FetchAgenda;
import com.procialize.mrgeApp20.GetterSetter.FetchFeed;
import com.procialize.mrgeApp20.GetterSetter.ProfileSave;
import com.procialize.mrgeApp20.InnerDrawerActivity.AgendaActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.AgendaVacationActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.AttendeeActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.DocumentsActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.EngagementActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.EventInfoActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.ExhibitorSideMenu;
import com.procialize.mrgeApp20.InnerDrawerActivity.FeedBackActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.FolderQuizActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.GalleryActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.GeneralInfoActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.LeaderboardActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.LivePollActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.MyTravelActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.NotificationActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.NotificationExhibitorActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.QAAttendeeActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.QADirectActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.QASpeakerActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.QRGeneratorActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.QRScanActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.SpeakerActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.SponsorActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.VideoActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.PlayerConfig;
import com.procialize.mrgeApp20.Utility.Res;
import com.procialize.mrgeApp20.Utility.Util;
import com.procialize.mrgeApp20.Zoom.ui.InitAuthSDKActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import cn.jzvd.JZVideoPlayerStandard;
import io.fabric.sdk.android.Fabric;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;


//preet

public class HomeActivity extends AppCompatActivity implements CustomMenuAdapter.CustomMenuAdapterListner {

    public static final int RequestPermissionCode = 8;
    public static String logoImg = "", colorActive = "", eventback = "";
    public static int activetab;
    public static int flag = 0;
    RecyclerView menurecycler;
    SessionManager session;
    List<EventSettingList> eventSettingLists;
    List<EventMenuSettingList> eventMenuSettingLists;
    HashMap<String, String> eventlist;
    ImageView headerlogoIv;
    String side_menu = "0", side_menu_my_travel = "0", side_menu_notification = "0", side_menu_display_qr = "0", side_menu_qr_scanner = "0",
            side_menu_quiz = "0", side_menu_live_poll = "0", side_menu_survey = "0",
            side_menu_feedback = "0", side_menu_gallery_video = "0", gallery_video_native = "0", gallery_video_youtube = "0",
            side_menu_image_gallery = "0", selfie_contest = "0", video_contest = "0",
            side_menu_event_info = "0", side_menu_document = "0", side_menu_engagement = "0",
            engagement_selfie_contest = "0", engagement_video_contest = "0",
            news_feed_video = "0", QA_speaker = "0", QA_direct = "0", QA_session = "0", side_menu_attendee = "0", side_menu_speaker = "0", side_menu_agenda = "0",
            side_menu_general_info = "0", edit_profile_company = "0", edit_profile_designation = "0", agenda_conference = "0", agenda_vacation = "0",
            side_menu_contact = "0", side_menu_email = "0", side_menu_leaderboard = "0", side_menu_exhibitor = "0",
            side_menu_sponsor = "0";
    String news_feed = "0", attendee = "0", speaker = "0", agenda = "0", edit_profile = "0", general_ifo = "0", main_tab_exhibitor = "0";
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String MY_EVENT = "EventId";
    String eventid, token;
    String email, password, exhibitorstatus, exhibitorid;
    CustomMenuAdapter customMenuAdapter;
    TextView logout, home, contactus, eventname, switchbt, chatbt, eula, privacy_policy, eventInfo, notification, exh_analytics, txt_version;
    String eventnamestr;
    LinearLayout linear;
    String imgname, accesstoken;
    HashMap<String, String> user;
    String MY_PREFS_CATEGORY = "categorycnt";
    WallPostReciever mReceiver;
    IntentFilter mFilter;
    String catcnt;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private APIService mAPIService;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private List<AgendaList> agendaList;
    private List<AgendaList> agendaDBList;
    private List<AgendaList> tempagendaList = new ArrayList<AgendaList>();
    private DBHelper procializeDB;
    private int[] tabIcons = {
            R.drawable.ic_newsfeed,
            R.drawable.ic_agenda,
            R.drawable.ic_attendee,
            R.drawable.ic_speaker,
            R.drawable.active_exhibitor,
            R.drawable.general_info

    };
    private Res res;
    private DBHelper dbHelper;

    private YouTubePlayerSupportFragment youTubePlayerFragment;
    public static LinearLayout linear_livestream, linear_zoom, linear_layout;
    String zoom_meeting_id, zoom_password, zoom_status, youtube_stream_url, stream_status, zoom_time, stream_time;
    ImageView float_icon;
    String YouvideoId;
    public static boolean flagshown = false;
    YouTubePlayerTracker mTracker = null;
    YouTubePlayer youTubePlayer;
    public static TextView txt_zoom, txt_streaming;
    public static ImageView img_zoom, img_stream;

    /*@Override
    public Resources getResources() {
        if (res == null) {
            res = new Res(super.getResources());
        }
        return res;
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        if (CheckingPermissionIsEnabledOrNot()) {
//            Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        }

        // If, If permission is not enabled then else condition will execute.
        else {
            //Calling method to enable permission.
            RequestMultiplePermission();
        }
        mAPIService = ApiUtils.getAPIService();
        drawerLayout = findViewById(R.id.drawer);

        session = new SessionManager(this);
        user = session.getUserDetails();
        cd = new ConnectionDetector(this);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "");
        eventnamestr = prefs.getString("eventnamestr", "");
        logoImg = prefs.getString("logoImg", "");
        colorActive = prefs.getString("colorActive", "");
        eventback = prefs.getString("eventback", "");
        // token
        accesstoken = user.get(SessionManager.KEY_TOKEN);
        Fabric.with(this, new Crashlytics());
        crashlytics("Home Page", accesstoken);
        fetchProfileDetail(accesstoken, eventid);
        initializeView();

        try {
            mReceiver = new WallPostReciever();
            mFilter = new IntentFilter(ApiConstant.BROADCAST_ACTION_BUZZ_FEED);
            // Registering BroadcastReceiver with this activity for the intent filter
            LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, mFilter);
            Intent intent = new Intent(this, WallPostService.class);
            this.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initializeView() {

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user1 = sessionManager.getUserDetails();
        linear = findViewById(R.id.linear);
        // token
        token = user1.get(SessionManager.KEY_TOKEN);

        firbaseAnalytics();

        exhibitorid = user.get(SessionManager.EXHIBITOR_ID);
        exhibitorstatus = user.get(SessionManager.EXHIBITOR_STATUS);
        activetab = getResources().getColor(R.color.activetab);
        activetab = Color.parseColor(colorActive);

        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager()
                .findFragmentById(R.id.youtube_player_fragment);
        linear_layout = findViewById(R.id.linear_layout);
        float_icon = findViewById(R.id.float_icon);
        linear_zoom = findViewById(R.id.linear_zoom);
        linear_livestream = findViewById(R.id.linear_livestream);
        float_icon = findViewById(R.id.float_icon);
        txt_streaming = findViewById(R.id.txt_streaming);
        txt_zoom = findViewById(R.id.txt_zoom);
        img_stream = findViewById(R.id.img_stream);
        img_zoom = findViewById(R.id.img_zoom);

        if (cd.isConnectingToInternet()) {
            fetchFeed(token, eventid);
        } else {

        }

        linear_livestream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeYoutubePlayer();
            }
        });

        linear_zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, InitAuthSDKActivity.class);
                intent.putExtra("meeting_id", zoom_meeting_id);
                intent.putExtra("meeting_password", zoom_password);
                startActivity(intent);
            }
        });

        FlyingVideo.get(HomeActivity.this).close();  // token

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_LOGIN, MODE_PRIVATE).edit();
        editor.putString("loginfirst", "1");
        editor.apply();


        SharedPreferences prefs8 = getSharedPreferences(MY_PREFS_CATEGORY, MODE_PRIVATE);
        catcnt = prefs8.getString("categorycnt", "");

//        Intent intent = getIntent();
//        eventid = intent.getStringExtra("eventId");
//        eventnamestr = intent.getStringExtra("eventnamestr");


//        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//        editor.putString("eventid", eventid);
//        editor.putString("eventnamestr", eventnamestr);
//        editor.putString("loginfirst", "1");
//        editor.apply();

        imgname = "background";//url.substring(58, 60);

        PicassoTrustAll.getInstance(HomeActivity.this)
                .load(ApiConstant.eventpic + eventback)
                .into(new com.squareup.picasso.Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                              try {
                                  String root = Environment.getExternalStorageDirectory().toString();
                                  File myDir = new File(root + "/Procialize");

                                  if (!myDir.exists()) {
                                      myDir.mkdirs();
                                  }

                                  String name = imgname + ".jpg";
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


        mAPIService = ApiUtils.getAPIService();
        session = new SessionManager(getApplicationContext());
        eventSettingLists = new ArrayList<>();
        eventMenuSettingLists = new ArrayList<>();

        dbHelper = new DBHelper(this);

        procializeDB = new DBHelper(this);
        db = procializeDB.getWritableDatabase();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        email = user.get(SessionManager.KEY_EMAIL);
        password = user.get(SessionManager.KEY_PASSWORD);


        if (session != null) {
            eventSettingLists = SessionManager.loadEventList();
            eventMenuSettingLists = SessionManager.loadMenuEventList();
            Setting(eventSettingLists);
        }

    }

    private void afterSettingView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headerlogoIv = findViewById(R.id.headerlogoIv);

        Util.logomethod(this, headerlogoIv);

        viewPager = findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);
        setupViewPager(viewPager);

        if (flag == 0) {
            viewPager.setCurrentItem(0);
        } else if (flag == 1) {
            viewPager.setCurrentItem(3);
            flag = 0;
        } else {
            viewPager.setCurrentItem(0);
        }


        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.setTabTextColors(Color.parseColor("#4D4D4D"), Color.parseColor(colorActive));

        try {

            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            viewPager.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            viewPager.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        if (viewPager.getBackground() == null) {
            viewPager.setBackgroundResource(Integer.parseInt(ApiConstant.eventpic + eventback));
        }

        try {

            int i = tabLayout.getTabCount();
            if (i == 5) {


                tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(4).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);


                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        JZVideoPlayerStandard.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        InputMethodManager imm = (InputMethodManager) HomeActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(tabLayout.getWindowToken(), 0);
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                        String string1 = "#4D4D4D";
                        int color1 = Color.parseColor(string1);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        tab.getIcon().setColorFilter(color1, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            } else if (i == 4) {


                tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);


                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        JZVideoPlayerStandard.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        InputMethodManager imm = (InputMethodManager) HomeActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(tabLayout.getWindowToken(), 0);
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                        String string1 = "#4D4D4D";
                        int color1 = Color.parseColor(string1);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        tab.getIcon().setColorFilter(color1, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            } else if (i == 3) {

                tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);

                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        JZVideoPlayerStandard.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);


                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                        String string1 = "#4D4D4D";
                        int color1 = Color.parseColor(string1);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
// int tabIconColor = ContextCompat.getColor(HomeActivity.this,color1);//tabunselected color
                        tab.getIcon().setColorFilter(color1, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            } else if (i == 2) {
//               tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//               tabLayout.getTabAt(1).setIcon(tabIcons[1]);


                tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);


                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        JZVideoPlayerStandard.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

// int tabIconColor = ContextCompat.getColor(HomeActivity.this, color); //tabselected color
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);

//                        if (tab.getText().equals("Agenda")) {
//                            if (cd.isConnectingToInternet()) {
//
//                                fetchAgenda(token, eventid);
//                            }
//                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                        String string1 = "#4D4D4D";
                        int color1 = Color.parseColor(string1);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

// int tabIconColor = ContextCompat.getColor(HomeActivity.this,color1);//tabunselected color
                        tab.getIcon().setColorFilter(color1, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            } else if (i == 1) {
//               tabLayout.getTabAt(0).setIcon(tabIcons[0]);


                tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);

                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        JZVideoPlayerStandard.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

// int tabIconColor = ContextCompat.getColor(HomeActivity.this, color); //tabselected color
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);

//                        if (tab.getText().equals("Agenda")) {
//                            if (cd.isConnectingToInternet()) {
//
//                                fetchAgenda(token, eventid);
//                            }
//                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//                       String string1 = "#7898a9";
//                       int color1 = Color.parseColor(string1);
//
//// int tabIconColor = ContextCompat.getColor(HomeActivity.this,color1);//tabunselected color
//                       tab.getIcon().setColorFilter(color1, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Initializing NavigationView
        navigationView = findViewById(R.id.navigation_view);
        menurecycler = navigationView.findViewById(R.id.menurecycler);
        logout = navigationView.findViewById(R.id.logout);
        home = navigationView.findViewById(R.id.home);
        contactus = navigationView.findViewById(R.id.contactus);
        switchbt = navigationView.findViewById(R.id.switchbt);
        chatbt = navigationView.findViewById(R.id.chatbt);
        privacy_policy = navigationView.findViewById(R.id.privacy_policy);
        eventInfo = navigationView.findViewById(R.id.eventInfo);
        notification = navigationView.findViewById(R.id.notification);
        exh_analytics = navigationView.findViewById(R.id.exh_analytics);
        txt_version = navigationView.findViewById(R.id.txt_version);

        eula = navigationView.findViewById(R.id.eula);

        txt_version = findViewById(R.id.txt_version);

        eula = navigationView.findViewById(R.id.eula);

        if (ApiConstant.baseUrl.contains("stage")) {
            txt_version.setText("Stage Version : " + BuildConfig.VERSION_NAME);
        } else {
            txt_version.setText("Version : " + BuildConfig.VERSION_NAME);
        }


        try {
            if (exhibitorstatus.equalsIgnoreCase("1")) {
                exh_analytics.setVisibility(View.VISIBLE);
            } else {
                exh_analytics.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            exh_analytics.setVisibility(View.GONE);
        }

        exh_analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExhibitorAnalytics.class);
                startActivity(intent);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.logoutUser();
                SharedPreferences.Editor pref = getSharedPreferences("PROFILE_PICTURE", MODE_PRIVATE).edit();
                pref.clear();

                try {
                    File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
                    mypath.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dbHelper.deleteData(HomeActivity.this);
//                SharedPreferences settings = getSharedPreferences(MY_PREFS_LOGIN, 0);
//                if (settings.contains("loginfirst")) {
//                    SharedPreferences.Editor editor = settings.edit();
//                    editor.remove("loginfirst");
//                    editor.apply();
//                }
                Intent main = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(main);
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(main);
                finish();
            }
        });


        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), WebViewActivity.class);
                startActivity(main);

            }
        });
        eventInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), EventInfoActivity.class);
                startActivity(main);

            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (exhibitorstatus.equalsIgnoreCase("1")) {
                        Intent main = new Intent(getApplicationContext(), NotificationExhibitorActivity.class);
                        startActivity(main);
                    } else {
                        Intent main = new Intent(getApplicationContext(), NotificationActivity.class);
                        startActivity(main);
                    }

                } catch (Exception e) {
                    Intent main = new Intent(getApplicationContext(), NotificationActivity.class);
                    startActivity(main);
                }

            }
        });


        eula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), EULAActivity.class);
                startActivity(main);

            }
        });

        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), PrivacyPolicy.class);
                startActivity(main);

            }
        });

//        chatbt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intercom.client().displayMessenger();
//            }
//        });

        switchbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                if (prefs.contains("eventnamestr")) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("eventnamestr");
                    editor.commit();
                }
                if (prefs.contains("eventid")) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("eventid");
                    editor.commit();
                }

                // SharedPreferences.Editor pref = getSharedPreferences("PROFILE_PICTURE", MODE_PRIVATE).edit();
                // pref.clear();

                try {
                    File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
                    mypath.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_CATEGORY, MODE_PRIVATE);
                if (prefs1.contains("categorycnt")) {
                    SharedPreferences.Editor editor = prefs1.edit();
                    editor.remove("categorycnt");
                    editor.commit();
                }

                session.logoutUser();

                dbHelper.deleteData(HomeActivity.this);
                Intent main = new Intent(getApplicationContext(), EventChooserActivity.class);
                main.putExtra("email", email);
                main.putExtra("password", password);
                main.putExtra("accesstiken", accesstoken);
                startActivity(main);
                finish();
            }
        });


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        menurecycler.setLayoutManager(mLayoutManager);


        List<EventMenuSettingList> anotherList = new ArrayList<EventMenuSettingList>();
        anotherList.addAll(eventMenuSettingLists);


        if (eventMenuSettingLists != null) {
            customMenuAdapter = new CustomMenuAdapter(this, eventMenuSettingLists, this, side_menu_agenda);
            menurecycler.setAdapter(customMenuAdapter);
            customMenuAdapter.notifyDataSetChanged();
        }


        profiledetails();


        // Initializing Drawer Layout and ActionBarToggle


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
//                profiledetails();
                super.onDrawerOpened(drawerView);
            }
        };
//        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
//        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.menuicon);
//
//        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
//                if (drawer.isDrawerOpen(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START);
//                } else {
//                    drawer.openDrawer(GravityCompat.START);
//                }
//            }
//        });
        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        if (side_menu.equalsIgnoreCase("0")) {
            navigationView.setVisibility(View.GONE);
        } else {
            navigationView.setVisibility(View.VISIBLE);
        }

    }

    public void profiledetails() {

        View header = navigationView.getHeaderView(0);
        RelativeLayout outer = findViewById(R.id.my);
        RelativeLayout headerRel = outer.findViewById(R.id.relbelo);

        TextView nameTv = outer.findViewById(R.id.nameTv);
        TextView lastNameTv = outer.findViewById(R.id.lastNameTv);
        TextView designationTv = outer.findViewById(R.id.designationTv);
        TextView compantyTv = outer.findViewById(R.id.compantyTv);
        final ImageView profileIV = outer.findViewById(R.id.profileIV);
        final ProgressBar progressView = outer.findViewById(R.id.progressView);

        eventname = outer.findViewById(R.id.eventname);
        eventname.setTextColor(Color.parseColor(colorActive));
        headerRel.setBackgroundColor(Color.parseColor(colorActive));
        outer.setBackgroundColor(Color.parseColor(colorActive));

        eventname.setText(eventnamestr);


        ImageView editIv = outer.findViewById(R.id.editIv);


//        if (edit_profile.equalsIgnoreCase("1")) {
//            editIv.setVisibility(View.VISIBLE);
//
//        } else {
//
//            editIv.setVisibility(View.GONE);
//
//        }


        editIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                SharedPreferences mypref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//                String event_id = mypref.getString("eventid", "");
//                String eventnamestr = mypref.getString("eventnamestr", "");

                Intent Profile = new Intent(getApplicationContext(), ProfileActivity.class);
//                Profile.putExtra("back",eventlist.)
//                Profile.putExtra("eventId", event_id);
//                Profile.putExtra("eventnamestr", eventnamestr);
                startActivity(Profile);
                finish();
            }
        });


        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);
        String lname = user.get(SessionManager.KEY_LNAME);

        // designation
        String designation = user.get(SessionManager.KEY_DESIGNATION);

        // company
        String company = user.get(SessionManager.KEY_COMPANY);

        //profilepic
        String profilepic = user.get(SessionManager.KEY_PIC);

        if (edit_profile_company.equalsIgnoreCase("0")) {
//            compantyTv.setVisibility(View.GONE);
            compantyTv.setText("");
        } else {
            compantyTv.setVisibility(View.VISIBLE);
            compantyTv.setText(company);
        }

        if (edit_profile_designation.equalsIgnoreCase("0")) {
//            designationTv.setVisibility(View.GONE);
            designationTv.setText("");
        } else {
            designationTv.setVisibility(View.VISIBLE);
            designationTv.setText(designation);
        }


        nameTv.setText(name + " " + lname);
//        lastNameTv.setText(lname);


        if (profilepic != null) {
            Glide.with(this).load(ApiConstant.profilepic + profilepic).listener(new RequestListener<Drawable>() {
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

    }

    private void setupTabIcons() {


        if (tabLayout.getTabAt(0) != null) {
            if (tabLayout.getTabAt(0).getText().equals("News Feed")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(0).getText().equals("Agenda")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(0).getText().equals("Attendee")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(0).getText().equals("Speaker")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(0).getText().equals("Exhibitors")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[4]);
            } else if (tabLayout.getTabAt(0).getText().equals("General Info")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[5]);
            }
        }


        if (tabLayout.getTabAt(1) != null) {
            if (tabLayout.getTabAt(1).getText().equals("News Feed")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(1).getText().equals("Agenda")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(1).getText().equals("Attendee")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(1).getText().equals("Speaker")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(1).getText().equals("Exhibitors")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[4]);
            } else if (tabLayout.getTabAt(1).getText().equals("General Info")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[5]);
            }
        }


        if (tabLayout.getTabAt(2) != null) {
            if (tabLayout.getTabAt(2).getText().equals("News Feed")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(2).getText().equals("Agenda")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(2).getText().equals("Attendee")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(2).getText().equals("Speaker")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(2).getText().equals("Exhibitors")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[4]);
            } else if (tabLayout.getTabAt(2).getText().equals("General Info")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[5]);
            }
        }


        if (tabLayout.getTabAt(3) != null) {
            if (tabLayout.getTabAt(3).getText().equals("News Feed")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(3).getText().equals("Agenda")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(3).getText().equals("Attendee")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(3).getText().equals("Speaker")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(3).getText().equals("Exhibitors")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[4]);
            } else if (tabLayout.getTabAt(3).getText().equals("General Info")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[5]);
            }
        }
        if (tabLayout.getTabAt(4) != null) {
            if (tabLayout.getTabAt(4).getText().equals("News Feed")) {
                tabLayout.getTabAt(4).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(4).getText().equals("Agenda")) {
                tabLayout.getTabAt(4).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(4).getText().equals("Attendee")) {
                tabLayout.getTabAt(4).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(4).getText().equals("Speaker")) {
                tabLayout.getTabAt(4).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(4).getText().equals("Exhibitors")) {
                tabLayout.getTabAt(4).setIcon(tabIcons[4]);
            } else if (tabLayout.getTabAt(4).getText().equals("General Info")) {
                tabLayout.getTabAt(4).setIcon(tabIcons[5]);
            }
        }
        if (tabLayout.getTabAt(5) != null) {
            if (tabLayout.getTabAt(5).getText().equals("News Feed")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(5).getText().equals("Agenda")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(5).getText().equals("Attendee")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(5).getText().equals("Speaker")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(5).getText().equals("Exhibitors")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[4]);
            } else if (tabLayout.getTabAt(5).getText().equals("General Info")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[5]);
            }
        }

//
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            //noinspection ConstantConditions
//            TextView tv=(TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
//            ImageView imageView= (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
//            if (i==0)
//            {
//                tv.setText("News Feed");
//                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_newsfeed) );
//            }else if (i==1)
//            {
//                tv.setText("Agenda");
//                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_newsfeed) );
//            }else if (i==2)
//            {
//                tv.setText("Attendees");
//
//            }else if (i==3)
//            {
//                tv.setText("Speakers");
//            }
//            tabLayout.getTabAt(i).setCustomView(tv);
//
//        }
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (news_feed.equalsIgnoreCase("1")) {
            adapter.addFragment(new WallFragment_POST(), "News Feed");
        }
        if (agenda.equalsIgnoreCase("1")) {
            if (agenda_conference.equalsIgnoreCase("1")) {
                adapter.addFragment(new AgendaFragment(), "Agenda");
            } else if (agenda_vacation.equalsIgnoreCase("1")) {
                adapter.addFragment(new AgendaFolderFragment(), "Agenda");
            }

        }
        if (attendee.equalsIgnoreCase("1")) {
            adapter.addFragment(new AttendeeFragment(), "Attendee");
        }

        if (main_tab_exhibitor.equalsIgnoreCase("1")) {
            try {
                if (Integer.parseInt(catcnt) <= 3) {
                    adapter.addFragment(new AllExhibitorFragment(), "Exhibitors");
                } else {
                    adapter.addFragment(new ExhiCatFragment(), "Exhibitors");
                }
            } catch (Exception e) {
                adapter.addFragment(new AllExhibitorFragment(), "Exhibitors");
            }

        }

        if (speaker.equalsIgnoreCase("1")) {
            adapter.addFragment(new SpeakerFragment(), "Speaker");
        }


        if (general_ifo.equalsIgnoreCase("1")) {
            adapter.addFragment(new GeneralInfo(), "General Info");
        }
//        if (news_feed.equalsIgnoreCase("1") && agenda.equalsIgnoreCase("0") &&
//                attendee.equalsIgnoreCase("0") && speaker.equalsIgnoreCase("0") &&
//                general_ifo.equalsIgnoreCase("0")) {
//            adapter.addFragment(new WallFragment_POST(), "News Feed");
//            tabLayout = findViewById(R.id.tabs);
//            AppBarLayout appTab = findViewById(R.id.appTab);
////            appTab.setElevation(0);
//            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    10.0f
//            );
//
//            viewPager.setLayoutParams(param);
//            appTab.setVisibility(View.VISIBLE);
//            tabLayout.setVisibility(View.VISIBLE);


//        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onResume() {
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//        profiledetails();
        super.onResume();
    }

    private void Setting(List<EventSettingList> eventSettingLists) {

        if (eventSettingLists.size() != 0) {
            for (int i = 0; i < eventSettingLists.size(); i++) {
                if (eventSettingLists.get(i).getFieldName().equals("side_menu")) {
                    side_menu = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_my_travel")) {
                    side_menu_my_travel = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_notification")) {
                    side_menu_notification = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_display_qr")) {
                    side_menu_display_qr = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_qr_scanner")) {
                    side_menu_qr_scanner = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_quiz")) {
                    side_menu_quiz = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_live_poll")) {
                    side_menu_live_poll = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_survey")) {
                    side_menu_survey = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_feedback")) {
                    side_menu_feedback = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_gallery_video")) {
                    side_menu_gallery_video = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("gallery_video_native")) {
                    gallery_video_native = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("gallery_video_youtube")) {
                    gallery_video_youtube = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_image_gallery")) {
                    side_menu_image_gallery = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("selfie_contest")) {
                    selfie_contest = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("video_contest")) {
                    video_contest = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_document")) {
                    side_menu_document = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("news_feed_video")) {
                    news_feed_video = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("news_feed")) {
                    news_feed = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("main_tab_attendee")) {
                    attendee = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("main_tab_speaker")) {
                    speaker = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("main_tab_exhibitor")) {
                    main_tab_exhibitor = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("main_tab_agenda")) {
                    agenda = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("edit_profile")) {
                    edit_profile = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("main_tab_gen_info")) {
                    general_ifo = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("Q&A_session")) {
                    QA_session = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("Q&A_speaker")) {
                    QA_speaker = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("side_menu_engagement")) {
                    side_menu_engagement = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("engagement_selfie_contest")) {
                    engagement_selfie_contest = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("engagement_video_contest")) {
                    engagement_video_contest = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_event_info")) {
                    side_menu_event_info = eventSettingLists.get(i).getFieldValue();
                }/* else if (eventSettingLists.get(i).getFieldName().equals("side_menu_agenda")) {
                    side_menu_agenda = eventSettingLists.get(i).getFieldValue();
                } */ else if (eventSettingLists.get(i).getFieldName().equals("side_menu_attendee")) {
                    side_menu_attendee = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_speaker")) {
                    side_menu_speaker = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("edit_profile_designation")) {
                    edit_profile_designation = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("edit_profile_company")) {
                    edit_profile_company = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("Q&A_direct_question")) {
                    QA_direct = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("agenda_conference")) {
                    agenda_conference = eventSettingLists.get(i).getFieldValue();
                    if (agenda_conference.equalsIgnoreCase("1")) {
                        side_menu_agenda = "1";
                    }
                    ////

                } else if (eventSettingLists.get(i).getFieldName().equals("agenda_vacation")) {
                    agenda_vacation = eventSettingLists.get(i).getFieldValue();
                    if (agenda_vacation.equalsIgnoreCase("1")) {
                        side_menu_agenda = "1";
                    }


                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_contact_us")) {
                    side_menu_contact = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_email_template")) {
                    side_menu_email = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_leaderboard")) {
                    side_menu_leaderboard = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_exhibitor")) {
                    side_menu_exhibitor = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_sponsor")) {

                    side_menu_sponsor = eventSettingLists.get(i).getFieldValue();
                }

            }
        }

        afterSettingView();
    }

    @Override
    public void onBackPressed() {

        boolean check = MyJZVideoPlayerStandard.backPress();

        if (check == true) {
            MyJZVideoPlayerStandard.goOnPlayOnPause();
            MyJZVideoPlayerStandard.quitFullscreenOrTinyWindow();
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Exit");
            builder.setMessage("Are you sure you want to exit?");
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

                            ActivityCompat.finishAffinity(HomeActivity.this);

                        }
                    });
            builder.show();
        }
    }

    @Override
    public void onContactSelected(EventMenuSettingList menuSettingList) {

        if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_my_travel")) {
            Intent selfie = new Intent(this, MyTravelActivity.class);
            startActivity(selfie);


        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_notification")) {
            Intent notify = new Intent(this, NotificationActivity.class);
            startActivity(notify);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_display_qr")) {
            Intent qrgenerate = new Intent(this, QRGeneratorActivity.class);
            startActivity(qrgenerate);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_qr_scanner")) {
            Intent scanqr = new Intent(this, QRScanActivity.class);
            startActivity(scanqr);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_quiz")) {
            Intent quiz = new Intent(this, FolderQuizActivity.class);
            startActivity(quiz);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_live_poll")) {
            Intent livepol = new Intent(this, LivePollActivity.class);
            startActivity(livepol);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_survey")) {
            Intent feedback = new Intent(this, FeedBackActivity.class);
            startActivity(feedback);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_feedback")) {
//            Intent feedback = new Intent(this, FeedBackActivity.class);
////            startActivity(feedback);
            Toast.makeText(HomeActivity.this, "Comming Soon...", Toast.LENGTH_SHORT).show();

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_gallery_video")) {
            Intent video = new Intent(this, VideoActivity.class);
            startActivity(video);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_image_gallery")) {
            Intent gallery = new Intent(this, GalleryActivity.class);
            startActivity(gallery);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_document")) {

            Intent doc = new Intent(this, DocumentsActivity.class);
            startActivity(doc);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_event_info")) {
            Intent event = new Intent(this, EventInfoActivity.class);
            startActivity(event);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_q&a")) {
            if (QA_session.equalsIgnoreCase("1")) {
                Intent event = new Intent(this, QAAttendeeActivity.class);
                startActivity(event);
            } else if (QA_speaker.equalsIgnoreCase("1")) {
                Intent event = new Intent(this, QASpeakerActivity.class);
                startActivity(event);
            } else if (QA_direct.equalsIgnoreCase("1")) {
                Intent event = new Intent(this, QADirectActivity.class);
                startActivity(event);
            } else {

                Intent intent = new Intent(HomeActivity.this, EmptyViewActivity.class);
                startActivity(intent);
//                setContentView(R.layout.activity_empty_view);
//                ImageView imageView = findViewById(R.id.back);
//                TextView text_empty = findViewById(R.id.text_empty);
//
//                final ImageView headerlogoIv1 = findViewById(R.id.headerlogoIv);
//                Glide.with(this).load("http://www.procialize.info/uploads/app_logo/" + logoImg).listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        headerlogoIv1.setImageResource(R.drawable.splashlogo);
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        return false;
//                    }
//                }).into(headerlogoIv1);
//                text_empty.setText("No Data Found");
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
//                        startActivity(intent);
//                    }
//                });
            }


        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_engagement")) {
            Intent engagement = new Intent(this, EngagementActivity.class);
            startActivity(engagement);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_attendee")) {
            Intent attendee = new Intent(this, AttendeeActivity.class);
            startActivity(attendee);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_speaker")) {
            Intent speaker = new Intent(this, SpeakerActivity.class);
            startActivity(speaker);

        } /*else if (menuSettingList.getFieldName().equalsIgnoreCase("agenda_conference") ||menuSettingList.getFieldName().equalsIgnoreCase("agenda_vacation")) {

            if (agenda_conference.equalsIgnoreCase("1")) {
                Intent agenda = new Intent(this, AgendaActivity.class);
                startActivity(agenda);
            } else if (agenda_vacation.equalsIgnoreCase("1")) {
                Intent agenda = new Intent(this, AgendaVacationActivity.class);
                startActivity(agenda);
            }


        }*/ else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_agenda")) {

            if (agenda_conference.equalsIgnoreCase("1")) {
                Intent agenda = new Intent(this, AgendaActivity.class);
                startActivity(agenda);
            } else if (agenda_vacation.equalsIgnoreCase("1")) {
                Intent agenda = new Intent(this, AgendaVacationActivity.class);
                startActivity(agenda);
            }


        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_gen_info")) {
            Intent generalinfo = new Intent(this, GeneralInfoActivity.class);
            startActivity(generalinfo);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_contact_us")) {
            Intent generalinfo = new Intent(this, WebViewActivity.class);
            startActivity(generalinfo);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_email_template")) {
            Toast.makeText(HomeActivity.this, "Coming Soon...", Toast.LENGTH_SHORT).show();

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_leaderboard")) {
            Intent generalinfo = new Intent(this, LeaderboardActivity.class);
            startActivity(generalinfo);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_exhibitor")) {
            if (Integer.parseInt(catcnt) <= 0) {
                Intent generalinfo = new Intent(this, ExhibitorListingActivity.class);
                generalinfo.putExtra("ExhiId", "0");
                generalinfo.putExtra("ExhiName", "All Category");
                startActivity(generalinfo);
            } else {
                Intent generalinfo = new Intent(this, ExhibitorSideMenu.class);
                startActivity(generalinfo);
            }
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_sponsor")) {
            Intent generalinfo = new Intent(this, SponsorActivity.class);
            startActivity(generalinfo);

        }


    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                        FifthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]
                {

                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean readstoragepermjission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writestoragepermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (readstoragepermjission && writestoragepermission) {

//                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(HomeActivity.this, "We need your permission so you can enjoy full features of app", Toast.LENGTH_LONG).show();
                        RequestMultiplePermission();

                    }
                }

                break;
        }
    }

    public void fetchAgenda(String token, String eventid) {

        mAPIService.AgendaFetchPost(token, eventid).enqueue(new Callback<FetchAgenda>() {
            @Override
            public void onResponse(Call<FetchAgenda> call, Response<FetchAgenda> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    showResponse(response);
                } else {

                    // Toast.makeText(getContext(),"Unable to process",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchAgenda> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");

                // Toast.makeText(getContext(),"Unable to process",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showResponse(Response<FetchAgenda> response) {
        try {
            String date = "";
            for (int i = 0; i < response.body().getAgendaList().size(); i++) {
                if (response.body().getAgendaList().get(i).getSessionDate().equalsIgnoreCase(date)) {
                    date = response.body().getAgendaList().get(i).getSessionDate();
                    tempagendaList.add(response.body().getAgendaList().get(i));
                } else {
                    date = response.body().getAgendaList().get(i).getSessionDate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        agendaList = response.body().getAgendaList();
        procializeDB.clearAgendaTable();
        procializeDB.insertAgendaInfo(agendaList, db);


    }

    public void fetchProfileDetail(String token, String eventid) {

        RequestBody token1 = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody eventid1 = RequestBody.create(MediaType.parse("text/plain"), eventid);
//        showProgress();
        mAPIService.fetchProfileDetail(token1, eventid1).enqueue(new Callback<ProfileSave>() {
            @Override
            public void onResponse(Call<ProfileSave> call, Response<ProfileSave> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        session.logoutUser();

                        Intent main = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(main);
                        finish();
                    } else {

                        showPfResponse(response);
                    }
//                    dismissProgress();
//                    showResponse(response);
                } else {


//                    dismissProgress();
                    try {
                        Toast.makeText(HomeActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ProfileSave> call, Throwable t) {
                //  Toast.makeText(getActivity(), "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();

            }
        });
    }

    public void showPfResponse(Response<ProfileSave> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if (!(response.body().getUserData().equals(null))) {

                try {
                    String name = response.body().getUserData().getFirstName();
                    String company = response.body().getUserData().getCompanyName();
                    String designation = response.body().getUserData().getDesignation();
                    String pic = response.body().getUserData().getProfilePic();
                    String lastname = response.body().getUserData().getLastName();
                    String city = response.body().getUserData().getCity();
                    String mobno = response.body().getUserData().getMobile();
                    String email = response.body().getUserData().getEmail();
                    String country = response.body().getUserData().getCountry();
                    String description = response.body().getUserData().getDescription();
                    String attendee_status = response.body().getUserData().getAttendee_status();
                    String exhibitor_id = response.body().getUserData().getExhibitor_id();
                    String exhibitor_status = response.body().getUserData().getExhibitor_status();


                    SessionManager sessionManager = new SessionManager(HomeActivity.this);
                    if (sessionManager != null) {
                        sessionManager.createProfileSession(name, company, designation, pic, lastname, city, description, country, email, mobno, attendee_status, exhibitor_id, exhibitor_status);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }
        } else {
            Toast.makeText(HomeActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

        }
    }


    private class WallPostReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // progressbarForSubmit.setVisibility(View.GONE);
            Log.d("service end", "service end");
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void firbaseAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString("Screen", "Home Page");// foods[1]);
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        //Sets whether analytics collection is enabled for this app on this device.
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
        firebaseAnalytics.setMinimumSessionDuration(10000);
        //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
        firebaseAnalytics.setSessionTimeoutDuration(1800000);
        //Sets the user ID property.
        firebaseAnalytics.setUserId(token);
        firebaseAnalytics.setCurrentScreen(this, "Home Page", null /* class override */);
    }

    public void fetchFeed(String token, String eventid) {

        mAPIService.FeedFetchPost(token, eventid).enqueue(new Callback<FetchFeed>() {
            @Override
            public void onResponse(Call<FetchFeed> call, Response<FetchFeed> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    for (int i = 0; i < response.body().getLive_steaming_info().size(); i++) {

                        zoom_meeting_id = response.body().getLive_steaming_info().get(i).getZoom_meeting_id();
                        zoom_password = response.body().getLive_steaming_info().get(i).getZoom_password();
                        zoom_status = response.body().getLive_steaming_info().get(i).getZoom_status();
                        youtube_stream_url = response.body().getLive_steaming_info().get(i).getYoutube_stream_url();
                        stream_status = response.body().getLive_steaming_info().get(i).getStream_status();
                        stream_time = response.body().getLive_steaming_info().get(i).getStream_datetime();
                        zoom_time = response.body().getLive_steaming_info().get(i).getZoom_datetime();

                        if (stream_status.equalsIgnoreCase("1")) {
//                            countDownlivestream();
                            linear_livestream.setBackgroundColor(Color.parseColor("#Ff0000"));
                            txt_streaming.setText("Live Streaming! Tap to view ");
                            Animation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(500); //You can manage the blinking time with this parameter
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            img_stream.startAnimation(anim);
                        } else {
                            linear_livestream.setBackgroundColor(Color.parseColor("#686868"));
                            txt_streaming.setText("Nothing Streaming currently");
                        }

                        if (zoom_status.equalsIgnoreCase("1")) {
//                            countDownzoom();
                            linear_zoom.setBackgroundColor(Color.parseColor("#2d8cff"));
                            txt_zoom.setText("Join Meeting via Zoom ");
                            Animation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(500); //You can manage the blinking time with this parameter
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            img_zoom.startAnimation(anim);
                        } else {
                            linear_livestream.setBackgroundColor(Color.parseColor("#686868"));
                            txt_zoom.setText("No Meeting active currently");
                        }

                    }


                } else {

                }
            }

            @Override
            public void onFailure(Call<FetchFeed> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
//                Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void initializeYoutubePlayer() {

        flagshown = false;
        FlyingVideo.get(HomeActivity.this).close();
        linear_layout.setVisibility(View.VISIBLE);
        linear_livestream.setVisibility(View.GONE);

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize(PlayerConfig.API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {

                    YouvideoId = youtube_stream_url.substring(youtube_stream_url.lastIndexOf("=") + 1);

                    String[] parts = youtube_stream_url.split("=");
                    String part1 = parts[0]; // 004
                    String videoId = parts[0]; // 034556


                    String[] parts1 = videoId.split("&index");

                    String url = parts1[0];

                    String[] parts2 = videoId.split("&list");


                    String url2 = parts2[0];


                    Log.e("videoid", YouvideoId);
                    youTubePlayer = player;

                    //set the player style default
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    //cue the 1st video by default
                    youTubePlayer.cueVideo(YouvideoId);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                youTubePlayer.play();
                youTubePlayer.getFullscreenControlFlags();
                //print or show error if initialization failed
                Log.e("", "Youtube Player View initialization failed");
            }
        });

        float_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagshown = true;
                linear_layout.setVisibility(View.GONE);
                linear_livestream.setVisibility(View.VISIBLE);
                mTracker = new YouTubePlayerTracker();
                FlyingVideo.get(HomeActivity.this)
                        .setFloatMode(TaskCoffeeVideo.FLOAT_MOVE.FREE)
                        .setVideoStartSecond((mTracker == null) ? 0 : mTracker.getCurrentSecond())
                        .coffeeVideoSetup(YouvideoId)
                        .setFlyGravity(TaskCoffeeVideo.FLY_GRAVITY.BOTTOM)
                        .show(linear_layout);
            }
        });
    }

}
