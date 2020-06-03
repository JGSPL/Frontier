package com.procialize.mrgeApp20.MergeMain;

import android.app.Dialog;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
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
import com.procialize.mrgeApp20.Activity.EULAActivity;
import com.procialize.mrgeApp20.Activity.EventChooserActivity;
import com.procialize.mrgeApp20.Activity.ExhibitorAnalytics;
import com.procialize.mrgeApp20.Activity.ExhibitorListingActivity;
import com.procialize.mrgeApp20.Activity.LoginActivity;
import com.procialize.mrgeApp20.Activity.PrivacyPolicy;
import com.procialize.mrgeApp20.Activity.ProfileActivity;
import com.procialize.mrgeApp20.Activity.WebViewActivity;
import com.procialize.mrgeApp20.Adapter.CustomMenuAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.Background.WallPostService;
import com.procialize.mrgeApp20.BuddyList.Activity.ActivityBuddyList;
import com.procialize.mrgeApp20.BuildConfig;
import com.procialize.mrgeApp20.CustomTools.CustomViewPager;
import com.procialize.mrgeApp20.CustomTools.PicassoTrustAll;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.Downloads.DocumentsActivity;
import com.procialize.mrgeApp20.Downloads.DownloadsFragment;
import com.procialize.mrgeApp20.EmptyViewActivity;
import com.procialize.mrgeApp20.Engagement.Fragment.EngagementFragment;
import com.procialize.mrgeApp20.Fragments.AgendaFolderFragment;
import com.procialize.mrgeApp20.Fragments.AgendaFragment;
import com.procialize.mrgeApp20.Fragments.AttendeeFragment;
import com.procialize.mrgeApp20.Speaker.Views.SpeakerFragment;
import com.procialize.mrgeApp20.Gallery.Image.Activity.GalleryFragment;
import com.procialize.mrgeApp20.Gallery.Video.Activity.VideoFragment;
import com.procialize.mrgeApp20.GetterSetter.AgendaList;
import com.procialize.mrgeApp20.GetterSetter.EventMenuSettingList;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.GetterSetter.FetchAgenda;
import com.procialize.mrgeApp20.GetterSetter.FetchFeed;
import com.procialize.mrgeApp20.GetterSetter.ProfileSave;
import com.procialize.mrgeApp20.GetterSetter.YouTubeApiList;
import com.procialize.mrgeApp20.InnerDrawerActivity.AgendaActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.AgendaVacationActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.AttendeeActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.EventInfoActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.ExhibitorSideMenu;
import com.procialize.mrgeApp20.InnerDrawerActivity.FeedBackActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.FolderQuizActivity;
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
import com.procialize.mrgeApp20.MrgeInnerFragment.BlankFragment;
import com.procialize.mrgeApp20.MrgeInnerFragment.EmergencyFragment;
import com.procialize.mrgeApp20.MrgeInnerFragment.EventInfoFragment;
import com.procialize.mrgeApp20.MrgeInnerFragment.FolderQuizFragment;
import com.procialize.mrgeApp20.MrgeInnerFragment.LivePollListFragment;
import com.procialize.mrgeApp20.MrgeInnerFragment.QnADirectFragment;
import com.procialize.mrgeApp20.MrgeInnerFragment.QnASessionFragment;
import com.procialize.mrgeApp20.MrgeInnerFragment.QnASpeakerFragment;
import com.procialize.mrgeApp20.NewsFeed.Views.Fragment.FragmentNewsFeed;
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
import java.util.Objects;

import javax.annotation.Nullable;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import io.fabric.sdk.android.Fabric;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.procialize.mrgeApp20.Utility.Util.setNotification;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;

public class MrgeHomeActivity extends AppCompatActivity implements CustomMenuAdapter.CustomMenuAdapterListner {

    public static final int RequestPermissionCode = 8;
    public static String logoImg = "", colorActive = "", eventback = "";
    public static int activetab;
    public static int flag = 0;
    public static ImageView headerlogoIv, notificationlogoIv, grid_image_view, list_image_view;
    public static TextView txtMainHeader;
    public static LinearLayout linear_livestream, linear_zoom, linear_layout,linear_changeView;
    public static boolean flagshown = false;
    public static TextView txt_zoom, txt_streaming;
    public static ImageView img_zoom, img_stream;
    RecyclerView menurecycler;
    SessionManager session;
    List<EventSettingList> eventSettingLists;
    List<EventMenuSettingList> eventMenuSettingLists;
    HashMap<String, String> eventlist;
    private String event_details  = "0", attendee = "0",attendee_designation = "0",attendee_company = "0",
            attendee_location = "0",attendee_mobile = "0",attendee_save_contact = "0",speaker = "0",
            speaker_rating = "0",speaker_designation = "0",speaker_company = "0", speaker_location = "0",
            speaker_mobile = "0", speaker_save_contact = "0", agenda, agenda_conference = "0",
            agenda_vacation = "0", event_info = "0",event_info_display_map = "0", event_info_description = "0",
            event_info_email = "0", event_info_contact = "0", attendee_message = "0",
            speaker_message = "0", emergency_contact = "0";

    private String interact  = "0",QnA  = "0" , QnA_speaker = "0",QnA_session = "0",QnA_like_question = "0",
            QnA_reply_question = "0",QnA_direct_question = "0", engagement_selfie_contest = "0",
            engagement_video_contest = "0",quiz = "0", live_poll = "0", engagement = "0";
    private String folder = "0",image_gallery = "0",document_download = "0",video_gallery = "0";

    String side_menu = "0", side_menu_my_travel = "0", side_menu_notification = "0", side_menu_display_qr = "0", side_menu_qr_scanner = "0",
            side_menu_quiz = "0", side_menu_live_poll = "0", side_menu_survey = "0",
            side_menu_feedback = "0", side_menu_gallery_video = "0", gallery_video_native = "0", gallery_video_youtube = "0",
            side_menu_image_gallery = "0", selfie_contest = "0", video_contest = "0",
            side_menu_event_info = "0", side_menu_document = "0", side_menu_engagement = "0",
            news_feed_video = "0", QA_speaker = "0", QA_direct = "0", QA_session = "0", side_menu_attendee = "0", side_menu_speaker = "0", side_menu_agenda = "0",
            side_menu_general_info = "0", edit_profile_company = "0", edit_profile_designation = "0",
            side_menu_contact = "0", side_menu_email = "0", side_menu_leaderboard = "0", side_menu_exhibitor = "0",
            side_menu_sponsor = "0";
    String news_feed = "0", edit_profile = "0", general_ifo = "0", main_tab_exhibitor = "0";
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String MY_EVENT = "EventId";
    String eventid, token;
    String email, password, exhibitorstatus, exhibitorid;
    CustomMenuAdapter customMenuAdapter;
    TextView logout, buddyList, editProfile, home, contactus, eventname, switchbt, chatbt, eula, privacy_policy, eventInfo, notification, exh_analytics, txt_version;
    String eventnamestr;
    LinearLayout linear;
    public static LinearLayout ll_notification_count;
    public static TextView tv_notification;
    String imgname, accesstoken;
    HashMap<String, String> user;
    String MY_PREFS_CATEGORY = "categorycnt";
    WallPostReciever mReceiver;
    public static NotificationCountReciever notificationCountReciever;
    IntentFilter mFilter;
    public static IntentFilter notificationCountFilter;
    String catcnt;
    LinearLayout linTab4, linTab3, linTab2;
    String zoom_meeting_id, zoom_password, zoom_status, zoom_time;//,youtube_stream_url,  stream_status, stream_time
    ImageView float_icon;
    String YouvideoId;
    YouTubePlayerTracker mTracker = null;
    YouTubePlayer youTubePlayer;
    Fragment fragment = null;
    Dialog myDialog;
    ViewPagerAdapterSub viewPagerAdapterSub3;
    ViewPagerAdapterSub viewPagerAdapterSub2;
    ViewPagerAdapterSub viewPagerAdapterSub4;
    LinearLayout linChange, linzoom, linStream;
    ImageView img_view;
    TextView txt_change;

    List<YouTubeApiList> youTubeApiLists = new ArrayList<>();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private CustomViewPager viewPager, Subviewpager, Subviewpager2, Subviewpager3;
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
            R.drawable.ic_eventdetail,
            R.drawable.ic_folder,
            R.drawable.ic_interact,
            R.drawable.ic_interact,
            R.drawable.ic_interact
    };
    private int[] sub1tabIcons = {
            R.drawable.ic_eventinfo,
            R.drawable.ic_attendeee,
            R.drawable.ic_speakers,
            R.drawable.ic_schedule,
            R.drawable.ic_emergency,
    };
    private int[] sub2tabIcons = {
            R.drawable.ic_image,
            R.drawable.ic_video,
            R.drawable.ic_download,
            R.drawable.engagement,
    };
    private int[] sub3tabIcons = {
            R.drawable.ic_quiz,
            R.drawable.ic_livepoll,
            R.drawable.ic_qna,
            R.drawable.ic_engagement,
    };
    private Res res;
    private DBHelper dbHelper;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private TabLayout sub2tabLayout, sub3tabLayout, sub4tabLayout;
    int youTubeLinkPosition=-1;

    public static LinearLayout ll_notification_count_drawer;
    public static TextView tv_notification_drawer;

    @Override
    public Resources getResources() {
        if (res == null) {
            res = new Res(super.getResources());
        }
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_home);

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

        try {
            notificationCountReciever = new NotificationCountReciever();
            notificationCountFilter = new IntentFilter(ApiConstant.BROADCAST_ACTION_FOR_NOTIFICATION_COUNT);
            LocalBroadcastManager.getInstance(this).registerReceiver(notificationCountReciever, notificationCountFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeView() {

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user1 = sessionManager.getUserDetails();
        linear = findViewById(R.id.linear);
        ll_notification_count = findViewById(R.id.ll_notification_count);

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
        linear_changeView = findViewById(R.id.linear_changeView);
        float_icon = findViewById(R.id.float_icon);
        txt_streaming = findViewById(R.id.txt_streaming);
        txt_zoom = findViewById(R.id.txt_zoom);
        img_stream = findViewById(R.id.img_stream);
        img_zoom = findViewById(R.id.img_zoom);
        linChange = findViewById(R.id.linChange);
        linzoom = findViewById(R.id.linzoom);

        linStream = findViewById(R.id.linStream);
        img_view = findViewById(R.id.img_view);
        txt_change = findViewById(R.id.txt_change);

        tv_notification = (TextView) findViewById(R.id.tv_notification);
        SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String notificationCount = prefs1.getString("notificationCount","");
        tv_notification.setText(notificationCount);

        if (cd.isConnectingToInternet()) {
            fetchFeed(token, eventid);
        } else {
        }

        linear_livestream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(youTubeLinkPosition <= youTubeApiLists.size()) {
                    initializeYoutubePlayer(youTubeLinkPosition);
                }*/
                FlyingVideo.get(MrgeHomeActivity.this).close();
                linear_layout.setVisibility(View.VISIBLE);
                linear_livestream.setVisibility(View.GONE);
            }
        });
        linear_changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(youTubeLinkPosition < youTubeApiLists.size()-1) {
                    youTubeLinkPosition = youTubeLinkPosition+1;
                    initializeYoutubePlayer(youTubeLinkPosition);
                }
                else {
                    youTubeLinkPosition = 0;
                    initializeYoutubePlayer(youTubeLinkPosition);
                }
            }
        });

        linear_zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MrgeHomeActivity.this, InitAuthSDKActivity.class);
                intent.putExtra("meeting_id", zoom_meeting_id);
                intent.putExtra("meeting_password", zoom_password);
                startActivity(intent);
            }
        });

        FlyingVideo.get(MrgeHomeActivity.this).close();  // token

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

        PicassoTrustAll.getInstance(MrgeHomeActivity.this)
                .load(ApiConstant.eventpic + eventback)
                .into(new com.squareup.picasso.Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                              try {
                                  String root = Environment.getExternalStorageDirectory().toString();
                                  File myDir = new File(root + "/" + ApiConstant.folderName);

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
        txtMainHeader = findViewById(R.id.txtMainHeader);
        notificationlogoIv = findViewById(R.id.notificationlogoIv);
        grid_image_view = findViewById(R.id.grid_image_view);
        list_image_view = findViewById(R.id.list_image_view);

        Util.logomethodwithText(MrgeHomeActivity.this, false, "", txtMainHeader, headerlogoIv);

        // Util.logomethod(this, headerlogoIv);

        notificationlogoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(main);

            }
        });

        viewPager = findViewById(R.id.viewpager);
        Subviewpager = findViewById(R.id.Subviewpager);
        Subviewpager2 = findViewById(R.id.Subviewpager2);
        Subviewpager3 = findViewById(R.id.Subviewpager3);

        // activity_spring_indicator_indicator_default = findViewById(R.id.activity_spring_indicator_indicator_default);

        viewPager.setPagingEnabled(false);
        setupViewPager(viewPager);

        Subviewpager.setPagingEnabled(false);
        Subviewpager3.setPagingEnabled(false);

        Sub2setupViewPager(Subviewpager);
        Sub3setupViewPager(Subviewpager2);
        Sub4setupViewPager(Subviewpager3);

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
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));

        txtMainHeader.setTextColor(Color.parseColor(colorActive));
        linTab2 = findViewById(R.id.linTab2);
        linTab3 = findViewById(R.id.linTab3);
        linTab4 = findViewById(R.id.linTab4);
        linTab2.setVisibility(View.GONE);
        linTab3.setVisibility(View.GONE);
        linTab4.setVisibility(View.GONE);

        sub2tabLayout = findViewById(R.id.tabsSecond);
        sub2tabLayout.setupWithViewPager(Subviewpager);
        sub2tabLayout.setVisibility(View.GONE);

        sub3tabLayout = findViewById(R.id.tabsThird);
        sub3tabLayout.setupWithViewPager(Subviewpager2);
        sub3tabLayout.setVisibility(View.GONE);

        sub4tabLayout = findViewById(R.id.tabsForth);
        sub4tabLayout.setupWithViewPager(Subviewpager3);
        sub4tabLayout.setVisibility(View.GONE);

        Sub2setupTabIcons();
        Sub3setupTabIcons();
        Sub4setupTabIcons();

        sub2tabLayout.setTabTextColors(Color.parseColor("#4D4D4D"), Color.parseColor(colorActive));
        sub3tabLayout.setTabTextColors(Color.parseColor("#4D4D4D"), Color.parseColor(colorActive));
        sub4tabLayout.setTabTextColors(Color.parseColor("#4D4D4D"), Color.parseColor(colorActive));

        try {

            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/" + ApiConstant.folderName + "/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            // viewPager.setBackgroundDrawable(bd);
            //  Subviewpager.setBackgroundDrawable(bd);
            //Subviewpager2.setBackgroundDrawable(bd);
            //Subviewpager3.setBackgroundDrawable(bd);
            drawerLayout.setBackgroundDrawable(bd);


            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            viewPager.setBackgroundColor(Color.parseColor("#f1f1f1"));
            Subviewpager.setBackgroundColor(Color.parseColor("#f1f1f1"));
            Subviewpager2.setBackgroundColor(Color.parseColor("#f1f1f1"));
            Subviewpager3.setBackgroundColor(Color.parseColor("#f1f1f1"));

        }

       /* if (viewPager.getBackground() == null) {
            viewPager.setBackgroundResource(Integer.parseInt(ApiConstant.eventpic + eventback));
            Subviewpager.setBackgroundResource(Integer.parseInt(ApiConstant.eventpic + eventback));
            Subviewpager2.setBackgroundResource(Integer.parseInt(ApiConstant.eventpic + eventback));
            Subviewpager3.setBackgroundResource(Integer.parseInt(ApiConstant.eventpic + eventback));

        }*/

        MainTabMecahnism();
        SubTab2Mechanism();
        SubTab3Mechanism();
        SubTab4Mechanism();


        //Initializing NavigationView
        navigationView = findViewById(R.id.navigation_view);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = metrics.widthPixels;
        navigationView.setLayoutParams(params);
        menurecycler = navigationView.findViewById(R.id.menurecycler);
        logout = navigationView.findViewById(R.id.logout);
        home = navigationView.findViewById(R.id.home);
        editProfile = navigationView.findViewById(R.id.editProfile);
        buddyList = navigationView.findViewById(R.id.buddyList);
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
            txt_version.setText("Stage Version : " + BuildConfig.VERSION_NAME + "(3)");
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
                Intent intent = new Intent(MrgeHomeActivity.this, ExhibitorAnalytics.class);
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
                    File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/" + ApiConstant.folderName + "/" + "background.jpg");
                    mypath.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dbHelper.deleteData(MrgeHomeActivity.this);
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

                Intent main = new Intent(getApplicationContext(), MrgeHomeActivity.class);
                startActivity(main);
                finish();
            }
        });


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(main);
                // finish();
            }
        });

        buddyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), ActivityBuddyList.class);
                startActivity(main);
                //finish();
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

               /* Intent main = new Intent(getApplicationContext(), EventInfoActivity.class);
                startActivity(main);*/
                fragment = new SpeakerFragment();

                /* Fragment fragment2 = new MainFragment();
                 *//*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment2, fragment2.getClass().getSimpleName()).addToBackStack(null).commit();*//*
                MainFragment.flag = flag;*/
                viewPager.setVisibility(View.GONE);
                Subviewpager.setVisibility(View.GONE);
                FrameLayout f = findViewById(R.id.content_frame);
                f.setVisibility(View.VISIBLE);

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();
                }

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
                    File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/" + ApiConstant.folderName + "/" + "background.jpg");
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

                dbHelper.deleteData(MrgeHomeActivity.this);
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

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.menuicon);

        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
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

    private void Sub2setupTabIcons() {
        if (sub2tabLayout.getTabAt(0) != null) {
            if (sub2tabLayout.getTabAt(0).getText().equals("EVENT INFO")) {
                sub2tabLayout.getTabAt(0).setIcon(sub1tabIcons[0]);
            } else if (sub2tabLayout.getTabAt(0).getText().equals("ATTENDEES")) {
                sub2tabLayout.getTabAt(0).setIcon(sub1tabIcons[1]);
            } else if (sub2tabLayout.getTabAt(0).getText().equals("SPEAKERS")) {
                sub2tabLayout.getTabAt(0).setIcon(sub1tabIcons[2]);
            } else if (sub2tabLayout.getTabAt(0).getText().equals("SCHEDULE")) {
                sub2tabLayout.getTabAt(0).setIcon(sub1tabIcons[3]);
            }else if (sub2tabLayout.getTabAt(0).getText().equals("EMERGENCY")) {
                sub2tabLayout.getTabAt(0).setIcon(sub1tabIcons[4]);
            }
        }


        if (sub2tabLayout.getTabAt(1) != null) {
            if (sub2tabLayout.getTabAt(1).getText().equals("EVENT INFO")) {
                sub2tabLayout.getTabAt(1).setIcon(sub1tabIcons[0]);
            } else if (sub2tabLayout.getTabAt(1).getText().equals("ATTENDEES")) {
                sub2tabLayout.getTabAt(1).setIcon(sub1tabIcons[1]);
            } else if (sub2tabLayout.getTabAt(1).getText().equals("SPEAKERS")) {
                sub2tabLayout.getTabAt(1).setIcon(sub1tabIcons[2]);
            } else if (sub2tabLayout.getTabAt(1).getText().equals("SCHEDULE")) {
                sub2tabLayout.getTabAt(1).setIcon(sub1tabIcons[3]);
            }else if (sub2tabLayout.getTabAt(1).getText().equals("EMERGENCY")) {
                sub2tabLayout.getTabAt(1).setIcon(sub1tabIcons[4]);
            }
        }


        if (sub2tabLayout.getTabAt(2) != null) {
            if (sub2tabLayout.getTabAt(2).getText().equals("EVENT INFO")) {
                sub2tabLayout.getTabAt(2).setIcon(sub1tabIcons[0]);
            } else if (sub2tabLayout.getTabAt(2).getText().equals("ATTENDEES")) {
                sub2tabLayout.getTabAt(2).setIcon(sub1tabIcons[1]);
            } else if (sub2tabLayout.getTabAt(2).getText().equals("SPEAKERS")) {
                sub2tabLayout.getTabAt(2).setIcon(sub1tabIcons[2]);
            } else if (sub2tabLayout.getTabAt(2).getText().equals("SCHEDULE")) {
                sub2tabLayout.getTabAt(2).setIcon(sub1tabIcons[3]);
            }else if (sub2tabLayout.getTabAt(2).getText().equals("EMERGENCY")) {
                sub2tabLayout.getTabAt(2).setIcon(sub1tabIcons[4]);
            }
        }


        if (sub2tabLayout.getTabAt(3) != null) {
            if (sub2tabLayout.getTabAt(3).getText().equals("EVENT INFO")) {
                sub2tabLayout.getTabAt(3).setIcon(sub1tabIcons[0]);
            } else if (sub2tabLayout.getTabAt(3).getText().equals("ATTENDEES")) {
                sub2tabLayout.getTabAt(3).setIcon(sub1tabIcons[1]);
            } else if (sub2tabLayout.getTabAt(3).getText().equals("SPEAKERS")) {
                sub2tabLayout.getTabAt(3).setIcon(sub1tabIcons[2]);
            } else if (sub2tabLayout.getTabAt(3).getText().equals("SCHEDULE")) {
                sub2tabLayout.getTabAt(3).setIcon(sub1tabIcons[3]);
            }else if (sub2tabLayout.getTabAt(3).getText().equals("EMERGENCY")) {
                sub2tabLayout.getTabAt(3).setIcon(sub1tabIcons[4]);
            }
        }

        if (sub2tabLayout.getTabAt(4) != null) {
            if (sub2tabLayout.getTabAt(4).getText().equals("EVENT INFO")) {
                sub2tabLayout.getTabAt(4).setIcon(sub1tabIcons[0]);
            } else if (sub2tabLayout.getTabAt(4).getText().equals("ATTENDEES")) {
                sub2tabLayout.getTabAt(4).setIcon(sub1tabIcons[1]);
            } else if (sub2tabLayout.getTabAt(4).getText().equals("SPEAKERS")) {
                sub2tabLayout.getTabAt(4).setIcon(sub1tabIcons[2]);
            } else if (sub2tabLayout.getTabAt(4).getText().equals("SCHEDULE")) {
                sub2tabLayout.getTabAt(4).setIcon(sub1tabIcons[3]);
            } else if (sub2tabLayout.getTabAt(4).getText().equals("EMERGENCY")) {
                sub2tabLayout.getTabAt(4).setIcon(sub1tabIcons[4]);
            }
        }


    }

    private void Sub2setupViewPager(ViewPager viewPager) {

        viewPagerAdapterSub2 = new ViewPagerAdapterSub(getSupportFragmentManager());
        if(event_info.equalsIgnoreCase("1")){
            viewPagerAdapterSub2.addFragment(new EventInfoFragment(), "EVENT INFO");
        }
        if(attendee.equalsIgnoreCase("1")){
            viewPagerAdapterSub2.addFragment(new AttendeeFragment(), "ATTENDEES");
        }
        if(speaker.equalsIgnoreCase("1")){
            viewPagerAdapterSub2.addFragment(new SpeakerFragment(), "SPEAKERS");
        }
        if(agenda.equalsIgnoreCase("1")){
            if(agenda_conference.equalsIgnoreCase("1")){
                viewPagerAdapterSub2.addFragment(new AgendaFragment(), "SCHEDULE");

            }else {
                viewPagerAdapterSub2.addFragment(new AgendaFolderFragment(), "SCHEDULE");
            }
        }
        if(emergency_contact.equalsIgnoreCase("1")){
            viewPagerAdapterSub2.addFragment(new EmergencyFragment(), "EMERGENCY");
        }


        Subviewpager.setAdapter(viewPagerAdapterSub2);
    }

    private void Sub3setupTabIcons() {
        if (sub3tabLayout.getTabAt(0) != null) {
            if (sub3tabLayout.getTabAt(0).getText().equals("IMAGE")) {
                sub3tabLayout.getTabAt(0).setIcon(sub2tabIcons[0]);
            } else if (sub3tabLayout.getTabAt(0).getText().equals("VIDEO")) {
                sub3tabLayout.getTabAt(0).setIcon(sub2tabIcons[1]);
            } else if (sub3tabLayout.getTabAt(0).getText().equals("DOWNLOADS")) {
                sub3tabLayout.getTabAt(0).setIcon(sub2tabIcons[2]);
            } else if (Objects.requireNonNull(sub3tabLayout.getTabAt(0)).getText().equals("Engagement")) {
                sub3tabLayout.getTabAt(0).setIcon(sub2tabIcons[3]);
            }
        }

        if (sub3tabLayout.getTabAt(1) != null) {
            if (sub3tabLayout.getTabAt(1).getText().equals("IMAGE")) {
                sub3tabLayout.getTabAt(1).setIcon(sub2tabIcons[0]);
            } else if (sub3tabLayout.getTabAt(1).getText().equals("VIDEO")) {
                sub3tabLayout.getTabAt(1).setIcon(sub2tabIcons[1]);
            } else if (sub3tabLayout.getTabAt(1).getText().equals("DOWNLOADS")) {
                sub3tabLayout.getTabAt(1).setIcon(sub2tabIcons[2]);
            } else if (sub3tabLayout.getTabAt(1).getText().equals("Engagement")) {
                sub3tabLayout.getTabAt(1).setIcon(sub2tabIcons[3]);
            }
        }

        if (sub3tabLayout.getTabAt(2) != null) {
            if (sub3tabLayout.getTabAt(2).getText().equals("IMAGE")) {
                sub3tabLayout.getTabAt(2).setIcon(sub2tabIcons[0]);
            } else if (sub3tabLayout.getTabAt(2).getText().equals("VIDEO")) {
                sub3tabLayout.getTabAt(2).setIcon(sub2tabIcons[1]);
            } else if (sub3tabLayout.getTabAt(2).getText().equals("DOWNLOADS")) {
                sub3tabLayout.getTabAt(2).setIcon(sub2tabIcons[2]);
            } else if (sub3tabLayout.getTabAt(2).getText().equals("Engagement")) {
                sub3tabLayout.getTabAt(2).setIcon(sub2tabIcons[3]);
            }
        }

        if (sub3tabLayout.getTabAt(3) != null) {
            if (sub3tabLayout.getTabAt(3).getText().equals("IMAGE")) {
                sub3tabLayout.getTabAt(3).setIcon(sub2tabIcons[0]);
            } else if (sub3tabLayout.getTabAt(3).getText().equals("VIDEO")) {
                sub3tabLayout.getTabAt(3).setIcon(sub2tabIcons[1]);
            } else if (sub3tabLayout.getTabAt(3).getText().equals("DOWNLOADS")) {
                sub3tabLayout.getTabAt(3).setIcon(sub2tabIcons[2]);
            } else if (sub3tabLayout.getTabAt(3).getText().equals("Engagement")) {
                sub3tabLayout.getTabAt(3).setIcon(sub2tabIcons[3]);
            }
        }
    }

    private void Sub3setupViewPager(ViewPager viewPager) {

        viewPagerAdapterSub3 = new ViewPagerAdapterSub(getSupportFragmentManager());
        if(image_gallery.equalsIgnoreCase("1")){
            viewPagerAdapterSub3.addFragment(new GalleryFragment(), "IMAGE");

        }
        if(video_gallery.equalsIgnoreCase("1")){
            viewPagerAdapterSub3.addFragment(new VideoFragment(), "VIDEO");

        }
        if(document_download.equalsIgnoreCase("1")){
            viewPagerAdapterSub3.addFragment(new DownloadsFragment(), "DOWNLOADS");

        }

        Subviewpager2.setAdapter(viewPagerAdapterSub3);
    }

    private void Sub4setupTabIcons() {
        if (sub4tabLayout.getTabAt(0) != null) {
            if (sub4tabLayout.getTabAt(0).getText().equals("QUIZ")) {
                sub4tabLayout.getTabAt(0).setIcon(sub3tabIcons[0]);
            } else if (sub4tabLayout.getTabAt(0).getText().equals("LIVE POLL")) {
                sub4tabLayout.getTabAt(0).setIcon(sub3tabIcons[1]);
            } else if (sub4tabLayout.getTabAt(0).getText().equals("Q&A")) {
                sub4tabLayout.getTabAt(0).setIcon(sub3tabIcons[2]);
            } else if (sub4tabLayout.getTabAt(0).getText().equals("ENGAGEMENT")) {
                sub4tabLayout.getTabAt(0).setIcon(sub3tabIcons[3]);
            }
        }


        if (sub4tabLayout.getTabAt(1) != null) {
            if (sub4tabLayout.getTabAt(1).getText().equals("QUIZ")) {
                sub4tabLayout.getTabAt(1).setIcon(sub3tabIcons[0]);
            } else if (sub4tabLayout.getTabAt(1).getText().equals("LIVE POLL")) {
                sub4tabLayout.getTabAt(1).setIcon(sub3tabIcons[1]);
            } else if (sub4tabLayout.getTabAt(1).getText().equals("Q&A")) {
                sub4tabLayout.getTabAt(1).setIcon(sub3tabIcons[2]);
            } else if (sub4tabLayout.getTabAt(1).getText().equals("ENGAGEMENT")) {
                sub4tabLayout.getTabAt(1).setIcon(sub3tabIcons[3]);
            }
        }


        if (sub4tabLayout.getTabAt(2) != null) {
            if (sub4tabLayout.getTabAt(2).getText().equals("QUIZ")) {
                sub4tabLayout.getTabAt(2).setIcon(sub3tabIcons[0]);
            } else if (sub4tabLayout.getTabAt(2).getText().equals("LIVE POLL")) {
                sub4tabLayout.getTabAt(2).setIcon(sub3tabIcons[1]);
            } else if (sub4tabLayout.getTabAt(2).getText().equals("Q&A")) {
                sub4tabLayout.getTabAt(2).setIcon(sub3tabIcons[2]);
            } else if (sub4tabLayout.getTabAt(2).getText().equals("ENGAGEMENT")) {
                sub4tabLayout.getTabAt(2).setIcon(sub3tabIcons[3]);
            }
        }

        if (sub4tabLayout.getTabAt(3) != null) {
            if (sub4tabLayout.getTabAt(3).getText().equals("QUIZ")) {
                sub4tabLayout.getTabAt(3).setIcon(sub3tabIcons[0]);
            } else if (sub4tabLayout.getTabAt(3).getText().equals("LIVE POLL")) {
                sub4tabLayout.getTabAt(3).setIcon(sub3tabIcons[1]);
            } else if (sub4tabLayout.getTabAt(3).getText().equals("Q&A")) {
                sub4tabLayout.getTabAt(3).setIcon(sub3tabIcons[2]);
            } else if (sub4tabLayout.getTabAt(3).getText().equals("ENGAGEMENT")) {
                sub4tabLayout.getTabAt(3).setIcon(sub3tabIcons[3]);
            }
        }

    }

    private void Sub4setupViewPager(ViewPager viewPager) {
       /*private String interact  = "0",QnA  = "0" , QnA_speaker = "0",QnA_session = "0",QnA_like_question = "0",
                            QnA_reply_question = "0",QnA_direct_question = "0", engagement_selfie_contest = "0",
                            engagement_video_contest = "0",quiz = "0", live_poll = "0", engagement = "0";*/
        viewPagerAdapterSub4 = new ViewPagerAdapterSub(getSupportFragmentManager());
        if(quiz.equalsIgnoreCase("1")){
            viewPagerAdapterSub4.addFragment(new FolderQuizFragment(), "QUIZ");

        }
        if(live_poll.equalsIgnoreCase("1")){
            viewPagerAdapterSub4.addFragment(new LivePollListFragment(), "LIVE POLL");

        }
        if(QnA.equalsIgnoreCase("1")){
            if(QnA_session.equalsIgnoreCase("1")){
                viewPagerAdapterSub4.addFragment(new QnASessionFragment(), "Q&A");

            }else if(QnA_speaker.equalsIgnoreCase("1")){
                viewPagerAdapterSub4.addFragment(new QnASpeakerFragment(), "Q&A");

            }else{
                viewPagerAdapterSub4.addFragment(new QnADirectFragment(), "Q&A");

            }

        }

        if(engagement.equalsIgnoreCase("1")){
            viewPagerAdapterSub4.addFragment(new EngagementFragment(), "ENGAGEMENT");

        }

        Subviewpager3.setAdapter(viewPagerAdapterSub4);
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
        final ImageView iv_close = outer.findViewById(R.id.iv_close);
        final ProgressBar progressView = outer.findViewById(R.id.progressView);
        ll_notification_count_drawer = outer.findViewById(R.id.ll_notification_count_drawer);
        tv_notification_drawer = outer.findViewById(R.id.tv_notification_drawer);
        SharedPreferences prefs1 = getSharedPreferences("ProcializeInfo", MODE_PRIVATE);
        String notificationCount = prefs1.getString("notificationCount","");
        tv_notification_drawer.setText(notificationCount);

        if(notificationCount.equalsIgnoreCase("0"))
        {
            tv_notification_drawer.setVisibility(View.GONE);
            ll_notification_count_drawer.setVisibility(View.GONE);
        }
        else
        {
            tv_notification_drawer.setVisibility(View.VISIBLE);
            ll_notification_count_drawer.setVisibility(View.VISIBLE);
        }
        eventname = outer.findViewById(R.id.eventname);
        eventname.setTextColor(Color.parseColor(colorActive));
        // headerRel.setBackgroundColor(Color.parseColor(colorActive));
        //outer.setBackgroundColor(Color.parseColor(colorActive));


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
        String company = user.get(SessionManager.KEY_CITY);

        //profilepic
        String profilepic = user.get(SessionManager.KEY_PIC);

        if (edit_profile_company.equalsIgnoreCase("0")) {
//            compantyTv.setVisibility(View.GONE);
            // compantyTv.setText("");
        } else {
            compantyTv.setVisibility(View.VISIBLE);
            //compantyTv.setText(company);
        }

        if (edit_profile_designation.equalsIgnoreCase("0")) {
//            designationTv.setVisibility(View.GONE);
            designationTv.setText("");
        } else {
            designationTv.setVisibility(View.VISIBLE);
            designationTv.setText(designation + " - " + company);
        }


        nameTv.setText(name + " " + lname);
//        lastNameTv.setText(lname);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });


        if (profilepic != null) {
            Glide.with(this).load(ApiConstant.profilepic + profilepic).circleCrop()
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

    }

    private void setupTabIcons() {


        if (tabLayout.getTabAt(0) != null) {
            if (tabLayout.getTabAt(0).getText().equals("News Feed")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(0).getText().equals("Event Details")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(0).getText().equals("Folder")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(0).getText().equals("Interact")) {
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
            } else if (tabLayout.getTabAt(1).getText().equals("Event Details")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(1).getText().equals("Folder")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(1).getText().equals("Interact")) {
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
            } else if (tabLayout.getTabAt(2).getText().equals("Event Details")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(2).getText().equals("Folder")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(2).getText().equals("Interact")) {
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
            } else if (tabLayout.getTabAt(3).getText().equals("Event Details")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(3).getText().equals("Folder")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(3).getText().equals("Interact")) {
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
            } else if (tabLayout.getTabAt(4).getText().equals("Event Details")) {
                tabLayout.getTabAt(4).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(4).getText().equals("Folder")) {
                tabLayout.getTabAt(4).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(4).getText().equals("Interact")) {
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
            } else if (tabLayout.getTabAt(5).getText().equals("Event Details")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(5).getText().equals("Folder")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(5).getText().equals("Interact")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(5).getText().equals("Exhibitors")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[4]);
            } else if (tabLayout.getTabAt(5).getText().equals("General Info")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[5]);
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (news_feed.equalsIgnoreCase("1")) {
            adapter.addFragment(new FragmentNewsFeed(), "News Feed");
        }
        if (event_details.equalsIgnoreCase("1")) {
            //if (agenda_conference.equalsIgnoreCase("1")) {
                adapter.addFragment(new BlankFragment(), "Event Details");
            /*} else if (agenda_vacation.equalsIgnoreCase("1")) {
                adapter.addFragment(new BlankFragment(), "Event Details");
            }*/

        }
        if (folder.equalsIgnoreCase("1")) {
            adapter.addFragment(new BlankFragment(), "Folder");
        }


        if (interact.equalsIgnoreCase("1")) {
            adapter.addFragment(new BlankFragment(), "Interact");
        }

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
        setNotification(this,tv_notification,ll_notification_count);


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
                }else if (eventSettingLists.get(i).getFieldName().equals("event_details")) {

                    event_details = eventSettingLists.get(i).getFieldValue();
                    if(eventSettingLists.get(i).getSub_menuList()!=null) {
                        if (eventSettingLists.get(i).getSub_menuList().size() > 0) {
                            for (int k = 0; k < eventSettingLists.get(i).getSub_menuList().size(); k++) {
                                if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee")) {

                                    attendee = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_designation")) {
                                    attendee_designation = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_company")) {
                                    attendee_company = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_location")) {
                                    attendee_location = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_mobile")) {
                                    attendee_mobile = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_save_contact")) {
                                    attendee_save_contact = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("agenda")) {
                                    agenda = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("agenda_conference")) {
                                    agenda_conference = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("agenda_vacation")) {
                                    agenda_vacation = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("event_info")) {
                                    event_info = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("event_info_display_map")) {
                                    event_info_display_map = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("event_info_description")) {
                                    event_info_description = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("event_info_email")) {
                                    event_info_email = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("event_info_contact")) {
                                    event_info_contact = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_message")) {
                                    attendee_message = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_message")) {
                                    speaker_message = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("emergency_contact")) {
                                    emergency_contact = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker")) {
                                    speaker = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_rating")) {
                                    speaker_rating = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_designation")) {
                                    speaker_designation = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_company")) {
                                    speaker_company = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_location")) {
                                    speaker_location = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_mobile")) {
                                    speaker_mobile = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_save_contact")) {
                                    speaker_save_contact = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }

                            }
                        }
                    }
                }else if (eventSettingLists.get(i).getFieldName().equals("interact")) {

                    interact = eventSettingLists.get(i).getFieldValue();
                    if(eventSettingLists.get(i).getSub_menuList()!=null) {
                        if (eventSettingLists.get(i).getSub_menuList().size() > 0) {
                            for (int k = 0; k < eventSettingLists.get(i).getSub_menuList().size(); k++) {
                                if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("Q&A")) {
                                    QnA = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("Q&A_speaker")) {
                                    QnA_speaker = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("Q&A_session")) {
                                    QnA_session = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("Q&A_like_question")) {
                                    QnA_like_question = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("Q&A_reply_question")) {
                                    QnA_reply_question = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("Q&A_direct_question")) {
                                    QnA_direct_question = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("engagement_selfie_contest")) {
                                    engagement_selfie_contest = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("engagement_video_contest")) {
                                    engagement_video_contest = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("quiz")) {
                                    quiz = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("live_poll")) {
                                    live_poll = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("engagement")) {
                                    engagement = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }
                        }
                        }
                    }
                }else if (eventSettingLists.get(i).getFieldName().equals("folder")) {


                    /*
                    private String folder = "0",image_gallery = "0",document_download = "0",video_gallery = "0";*/
                    folder = eventSettingLists.get(i).getFieldValue();
                    if(eventSettingLists.get(i).getSub_menuList()!=null) {
                        if (eventSettingLists.get(i).getSub_menuList().size() > 0) {
                            for (int k = 0; k < eventSettingLists.get(i).getSub_menuList().size(); k++) {
                                if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("image_gallery")) {

                                    image_gallery = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("document_download")) {

                                    document_download = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("video_gallery")) {

                                    video_gallery = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                                }
                            }
                        }
                    }
                }

            }
        }

        afterSettingView();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        boolean check = JzvdStd.backPress();


        if (check == true) {

            JzvdStd.goOnPlayOnPause();

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(MrgeHomeActivity.this);
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

                            ActivityCompat.finishAffinity(MrgeHomeActivity.this);

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
            Toast.makeText(MrgeHomeActivity.this, "Comming Soon...", Toast.LENGTH_SHORT).show();

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_gallery_video")) {
         /*   Intent video = new Intent(this, VideoFragment.class);
            startActivity(video);*/

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_image_gallery")) {
            /*Intent gallery = new Intent(this, GalleryFragment.class);
            startActivity(gallery);*/

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

                Intent intent = new Intent(MrgeHomeActivity.this, EmptyViewActivity.class);
                startActivity(intent);

            }


        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_engagement")) {
            Intent engagement = new Intent(this, EngagementFragment.class);
            startActivity(engagement);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_attendee")) {
            Intent attendee = new Intent(this, AttendeeActivity.class);
            startActivity(attendee);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_speaker")) {
            Intent speaker = new Intent(this, SpeakerActivity.class);
            startActivity(speaker);

        }  else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_agenda")) {

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
            Toast.makeText(MrgeHomeActivity.this, "Coming Soon...", Toast.LENGTH_SHORT).show();

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
        ActivityCompat.requestPermissions(MrgeHomeActivity.this, new String[]
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

                        Toast.makeText(MrgeHomeActivity.this, "We need your permission so you can enjoy full features of app", Toast.LENGTH_LONG).show();
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

                        Intent main = new Intent(MrgeHomeActivity.this, LoginActivity.class);
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
                        Toast.makeText(MrgeHomeActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
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


                    SessionManager sessionManager = new SessionManager(MrgeHomeActivity.this);
                    if (sessionManager != null) {
                        sessionManager.createProfileSession(name, company, designation, pic, lastname, city, description, country, email, mobno, attendee_status, exhibitor_id, exhibitor_status);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }
        } else {
            Toast.makeText(MrgeHomeActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

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
                    // for (int i = 0; i < response.body().getLive_steaming_info().size(); i++) {
                    zoom_meeting_id = response.body().getLive_steaming_info().getZoom_meeting_id();
                    zoom_password = response.body().getLive_steaming_info().getZoom_password();
                    zoom_status = response.body().getLive_steaming_info().getZoom_status();

                    zoom_time = response.body().getLive_steaming_info().getZoom_datetime();

                    if (response.body().getYoutube_info().size() > 0) {
                        youTubeApiLists = response.body().getYoutube_info();

                        if(youTubeApiLists.get(0).getStream_status().equalsIgnoreCase("1"))
                        {
                            linStream.setBackgroundColor(Color.parseColor(colorActive));
                            txt_streaming.setBackgroundColor(Color.parseColor(colorActive));
                            img_stream.setBackgroundColor(Color.parseColor(colorActive));

                            txt_streaming.setText("Live Streaming! Tap to view ");
                            Animation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(500); //You can manage the blinking time with this parameter
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            img_stream.startAnimation(anim);
                        }
                    }

                      /*  if (stream_status.equalsIgnoreCase("1")) {
//                            countDownlivestream();
                           // linear_livestream.setBackgroundColor(Color.parseColor("#Ff0000"));
                            linStream.setBackgroundColor(Color.parseColor(colorActive));
                            txt_streaming.setBackgroundColor(Color.parseColor(colorActive));
                            img_stream.setBackgroundColor(Color.parseColor(colorActive));

                            txt_streaming.setText("Live Streaming! Tap to view ");
                            Animation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(500); //You can manage the blinking time with this parameter
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            img_stream.startAnimation(anim);
                        } else {

                            linStream.setBackgroundColor(Color.parseColor("#686868"));
                            txt_streaming.setBackgroundColor(Color.parseColor("#686868"));
                            img_stream.setBackgroundColor(Color.parseColor("#686868"));
                            //linear_livestream.setBackgroundColor(Color.parseColor("#686868"));
                            txt_streaming.setText("Nothing Streaming currently");
                        }*/
                    //}
                    if (zoom_status.equalsIgnoreCase("1")) {
//                            countDownzoom();

                        linChange.setBackgroundColor(Color.parseColor(colorActive));
                        img_view.setBackgroundColor(Color.parseColor(colorActive));
                        txt_change.setBackgroundColor(Color.parseColor(colorActive));

                        linzoom.setBackgroundColor(Color.parseColor(colorActive));
                        txt_zoom.setBackgroundColor(Color.parseColor(colorActive));
                        img_zoom.setBackgroundColor(Color.parseColor(colorActive));

                        txt_zoom.setText("Participates");
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(500); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        img_zoom.startAnimation(anim);
                    } else {
                        linChange.setBackgroundColor(Color.parseColor("#686868"));
                        img_view.setBackgroundColor(Color.parseColor("#686868"));
                        txt_change.setBackgroundColor(Color.parseColor("#686868"));

                        linzoom.setBackgroundColor(Color.parseColor("#686868"));
                        txt_zoom.setBackgroundColor(Color.parseColor("#686868"));
                        img_zoom.setBackgroundColor(Color.parseColor("#686868"));
                        // linear_livestream.setBackgroundColor(Color.parseColor("#686868"));
                        txt_zoom.setText("Participates");
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

    private void initializeYoutubePlayer(int pos) {

        flagshown = false;
        FlyingVideo.get(MrgeHomeActivity.this).close();
        linear_layout.setVisibility(View.VISIBLE);
        linear_livestream.setVisibility(View.GONE);

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.onDestroy();

        youTubePlayerFragment.initialize(PlayerConfig.API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {

                    String youtube_stream_url = youTubeApiLists.get(pos).getYoutube_stream_url();
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
                FlyingVideo.get(MrgeHomeActivity.this)
                        .setFloatMode(TaskCoffeeVideo.FLOAT_MOVE.FREE)
                        .setVideoStartSecond((mTracker == null) ? 0 : mTracker.getCurrentSecond())
                        .coffeeVideoSetup(YouvideoId)
                        .setFlyGravity(TaskCoffeeVideo.FLY_GRAVITY.BOTTOM)
                        .show(linear_layout);
            }
        });
    }

    void MainTabMecahnism() {
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

                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        InputMethodManager imm = (InputMethodManager) MrgeHomeActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(tabLayout.getWindowToken(), 0);
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        if (tab.getText().equals("Event Details")) {
                            Subviewpager.setPagingEnabled(false);
                            linTab2.setVisibility(View.VISIBLE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub2.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else if (tab.getText().equals("Folder")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.VISIBLE);
                            linTab4.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.VISIBLE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager2.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub3.getPageTitle(pos);
                            if (pageTitle.equalsIgnoreCase("DOWNLOADS")) {
                                grid_image_view.setVisibility(View.VISIBLE);
                                list_image_view.setVisibility(View.VISIBLE);
                            } else {
                                grid_image_view.setVisibility(View.GONE);
                                list_image_view.setVisibility(View.GONE);
                            }
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------

                        } else if (tab.getText().equals("General Info") || tab.getText().equals("Interact") ||
                                tab.getText().equals("Exhibitors")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.VISIBLE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.VISIBLE);
                            sub4tabLayout.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.GONE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager3.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub4.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);

                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);

                            linear_livestream.setVisibility(View.VISIBLE);

                        }


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
                        if (tab.getText().equals("Event Details")) {
                            Subviewpager.setPagingEnabled(false);
                            linTab2.setVisibility(View.VISIBLE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);

                            sub2tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub2.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else if (tab.getText().equals("Folder")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.VISIBLE);
                            linTab4.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.VISIBLE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager2.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub3.getPageTitle(pos);
                            if (pageTitle.equalsIgnoreCase("DOWNLOADS")) {
                                grid_image_view.setVisibility(View.VISIBLE);
                                list_image_view.setVisibility(View.VISIBLE);
                            } else {
                                grid_image_view.setVisibility(View.GONE);
                                list_image_view.setVisibility(View.GONE);
                            }
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------

                        } else if (tab.getText().equals("General Info") || tab.getText().equals("Interact") ||
                                tab.getText().equals("Exhibitors")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.VISIBLE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.VISIBLE);
                            sub4tabLayout.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.GONE);
//-------------------------------------------------------------------------------
                            int pos = Subviewpager3.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub4.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------

                        } else {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.VISIBLE);


                        }


                    }
                });
            } else if (i == 4) {
                grid_image_view.setVisibility(View.GONE);
                list_image_view.setVisibility(View.GONE);

                tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);


                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        InputMethodManager imm = (InputMethodManager) MrgeHomeActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(tabLayout.getWindowToken(), 0);
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        if (tab.getText().equals("Event Details")) {
                            Subviewpager.setPagingEnabled(false);
                            linTab2.setVisibility(View.VISIBLE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);

                            sub2tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "", txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub2.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------

                        } else if (tab.getText().equals("Folder")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.VISIBLE);
                            linTab4.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.VISIBLE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);

                            //--------------------------------------------------------------------------------------
                            int pos = Subviewpager2.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub3.getPageTitle(pos);

                            if (pageTitle.equalsIgnoreCase("DOWNLOADS")) {
                                grid_image_view.setVisibility(View.VISIBLE);
                                list_image_view.setVisibility(View.VISIBLE);
                            } else {
                                grid_image_view.setVisibility(View.GONE);
                                list_image_view.setVisibility(View.GONE);
                            }

                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-----------------------------------------------------------------------------------------
                        } else if (tab.getText().equals("General Info") || tab.getText().equals("Interact") ||
                                tab.getText().equals("Exhibitors")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.VISIBLE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.VISIBLE);
                            sub4tabLayout.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.GONE);
                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager3.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub4.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------

                        } else if (tab.getText().equals("News Feed")) {
                          /* headerlogoIv.setVisibility(View.GONE);
                           txtMainHeader.setVisibility(View.VISIBLE);*/
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.VISIBLE);


                            Util.logomethodwithText(MrgeHomeActivity.this, false, "", txtMainHeader, headerlogoIv);
                        } else {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.VISIBLE);

                        }
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
                        if (tab.getText().equals("Event Details")) {
                            Subviewpager.setPagingEnabled(false);
                            linTab2.setVisibility(View.VISIBLE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);

                            sub2tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub2.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else if (tab.getText().equals("Folder")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.VISIBLE);
                            linTab4.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.VISIBLE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);
                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager2.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub3.getPageTitle(pos);
                            if (pageTitle.equalsIgnoreCase("DOWNLOADS")) {
                                grid_image_view.setVisibility(View.VISIBLE);
                                list_image_view.setVisibility(View.VISIBLE);
                            } else {
                                grid_image_view.setVisibility(View.GONE);
                                list_image_view.setVisibility(View.GONE);
                            }
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else if (tab.getText().equals("General Info") || tab.getText().equals("Interact") ||
                                tab.getText().equals("Exhibitors")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.VISIBLE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.VISIBLE);
                            sub4tabLayout.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.GONE);
                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager3.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub4.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------

                        } else if (tab.getText().equals("News Feed")) {
                          /* headerlogoIv.setVisibility(View.GONE);
                           txtMainHeader.setVisibility(View.VISIBLE);*/
                            Util.logomethodwithText(MrgeHomeActivity.this, false, "", txtMainHeader, headerlogoIv);
                        } else {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.VISIBLE);

                        }

                    }
                });
            } else if (i == 3) {
                grid_image_view.setVisibility(View.GONE);
                list_image_view.setVisibility(View.GONE);

                tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);

                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);

                        if (tab.getText().equals("Event Details")) {
                            Subviewpager.setPagingEnabled(false);
                            linTab2.setVisibility(View.VISIBLE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);

                            sub2tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub2.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else if (tab.getText().equals("Folder")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.VISIBLE);
                            linTab4.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.VISIBLE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager2.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub3.getPageTitle(pos);
                            if (pageTitle.equalsIgnoreCase("DOWNLOADS")) {
                                grid_image_view.setVisibility(View.VISIBLE);
                                list_image_view.setVisibility(View.VISIBLE);
                            } else {
                                grid_image_view.setVisibility(View.GONE);
                                list_image_view.setVisibility(View.GONE);
                            }
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else if (tab.getText().equals("General Info") || tab.getText().equals("Interact") ||
                                tab.getText().equals("Exhibitors")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.VISIBLE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.VISIBLE);
                            sub4tabLayout.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.GONE);
                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager3.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub4.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------

                        } else {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.VISIBLE);

                        }

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                        String string1 = "#4D4D4D";
                        int color1 = Color.parseColor(string1);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
// int tabIconColor = ContextCompat.getColor(MrgeHomeActivity.this,color1);//tabunselected color
                        tab.getIcon().setColorFilter(color1, PorterDuff.Mode.SRC_IN);

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        if (tab.getText().equals("Event Details")) {
                            Subviewpager.setPagingEnabled(false);
                            linTab2.setVisibility(View.VISIBLE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);

                            sub2tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub2.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else if (tab.getText().equals("Folder")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.VISIBLE);
                            linTab4.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.VISIBLE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager2.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub3.getPageTitle(pos);
                            if (pageTitle.equalsIgnoreCase("DOWNLOADS")) {
                                grid_image_view.setVisibility(View.VISIBLE);
                                list_image_view.setVisibility(View.VISIBLE);
                            } else {
                                grid_image_view.setVisibility(View.GONE);
                                list_image_view.setVisibility(View.GONE);
                            }
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else if (tab.getText().equals("General Info") || tab.getText().equals("Interact") ||
                                tab.getText().equals("Exhibitors")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.VISIBLE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.VISIBLE);
                            sub4tabLayout.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.GONE);
                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager3.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub4.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------

                        } else {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.VISIBLE);

                        }
                    }
                });
            } else if (i == 2) {
//               tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//               tabLayout.getTabAt(1).setIcon(tabIcons[1]);

                grid_image_view.setVisibility(View.GONE);
                list_image_view.setVisibility(View.GONE);

                tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);


                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        if (tab.getText().equals("Event Details")) {
                            Subviewpager.setPagingEnabled(false);
                            linTab2.setVisibility(View.VISIBLE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);

                            sub2tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub2.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else if (tab.getText().equals("Folder")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.VISIBLE);
                            linTab4.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.VISIBLE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager2.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub3.getPageTitle(pos);
                            if (pageTitle.equalsIgnoreCase("DOWNLOADS")) {
                                grid_image_view.setVisibility(View.VISIBLE);
                                list_image_view.setVisibility(View.VISIBLE);
                            } else {
                                grid_image_view.setVisibility(View.GONE);
                                list_image_view.setVisibility(View.GONE);
                            }
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------

                        } else if (tab.getText().equals("General Info") || tab.getText().equals("Interact") ||
                                tab.getText().equals("Exhibitors")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.VISIBLE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.VISIBLE);
                            sub4tabLayout.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.GONE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager3.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub4.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------

                        } else {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.VISIBLE);

                        }
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
                        if (tab.getText().equals("Event Details")) {
                            Subviewpager.setPagingEnabled(false);
                            linTab2.setVisibility(View.VISIBLE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);

                            sub2tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub2.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else if (tab.getText().equals("Folder")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.VISIBLE);
                            linTab4.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.VISIBLE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            sub4tabLayout.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);

//-------------------------------------------------------------------------------
                            int pos = Subviewpager2.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub3.getPageTitle(pos);
                            if (pageTitle.equalsIgnoreCase("DOWNLOADS")) {
                                grid_image_view.setVisibility(View.VISIBLE);
                                list_image_view.setVisibility(View.VISIBLE);
                            } else {
                                grid_image_view.setVisibility(View.GONE);
                                list_image_view.setVisibility(View.GONE);
                            }
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else if (tab.getText().equals("General Info") || tab.getText().equals("Interact") ||
                                tab.getText().equals("Exhibitors")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.VISIBLE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.VISIBLE);
                            sub4tabLayout.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.GONE);
                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager3.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub4.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------

                        } else {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.VISIBLE);

                        }
                    }
                });
            } else if (i == 1) {
                grid_image_view.setVisibility(View.GONE);
                list_image_view.setVisibility(View.GONE);

                tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);

                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        if (tab.getText().equals("Event Details")) {
                            Subviewpager.setPagingEnabled(false);
                            linTab2.setVisibility(View.VISIBLE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);

                            sub2tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub2.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else if (tab.getText().equals("Folder")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.VISIBLE);
                            linTab4.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.VISIBLE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            linear_livestream.setVisibility(View.GONE);
                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager2.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub3.getPageTitle(pos);
                            if (pageTitle.equalsIgnoreCase("DOWNLOADS")) {
                                grid_image_view.setVisibility(View.VISIBLE);
                                list_image_view.setVisibility(View.VISIBLE);
                            } else {
                                grid_image_view.setVisibility(View.GONE);
                                list_image_view.setVisibility(View.GONE);
                            }
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------

                        } else if (tab.getText().equals("General Info") || tab.getText().equals("Interact") ||
                                tab.getText().equals("Exhibitors")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.VISIBLE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.VISIBLE);
                            sub4tabLayout.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.GONE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager3.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub4.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            linear_livestream.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        if (tab.getText().equals("Event Details")) {
                            Subviewpager.setPagingEnabled(false);
                            linTab2.setVisibility(View.VISIBLE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);

                            sub2tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub2.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else if (tab.getText().equals("Folder")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.VISIBLE);
                            linTab4.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.VISIBLE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.VISIBLE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager2.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub3.getPageTitle(pos);
                            if (pageTitle.equalsIgnoreCase("DOWNLOADS")) {
                                grid_image_view.setVisibility(View.VISIBLE);
                                list_image_view.setVisibility(View.VISIBLE);
                            } else {
                                grid_image_view.setVisibility(View.GONE);
                                list_image_view.setVisibility(View.GONE);
                            }
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------
                        } else if (tab.getText().equals("General Info") || tab.getText().equals("Interact") ||
                                tab.getText().equals("Exhibitors")) {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.VISIBLE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            Subviewpager.setPagingEnabled(false);
                            viewPager.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.VISIBLE);
                            sub4tabLayout.setVisibility(View.VISIBLE);

                            //-------------------------------------------------------------------------------
                            int pos = Subviewpager3.getCurrentItem();
                            String pageTitle = (String) viewPagerAdapterSub4.getPageTitle(pos);
                            String pageTitle1 = pageTitle.substring(0, 1).toUpperCase() + pageTitle.substring(1).toLowerCase();
                            Log.e("PageTitle", pageTitle1);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, pageTitle1, txtMainHeader, headerlogoIv);
                            //-------------------------------------------------------------------------------

                        } else {
                            linTab2.setVisibility(View.GONE);
                            linTab3.setVisibility(View.GONE);
                            linTab4.setVisibility(View.GONE);
                            sub2tabLayout.setVisibility(View.GONE);
                            sub3tabLayout.setVisibility(View.GONE);
                            Subviewpager.setVisibility(View.GONE);
                            Subviewpager2.setVisibility(View.GONE);
                            Subviewpager3.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);


                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void SubTab2Mechanism() {
        try {

            int i = sub2tabLayout.getTabCount();
            if (i == 5) {


                sub2tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                sub2tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                sub2tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                sub2tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                sub2tabLayout.getTabAt(4).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);


                sub2tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        InputMethodManager imm = (InputMethodManager) MrgeHomeActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(tabLayout.getWindowToken(), 0);
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        sub2tabLayout.setVisibility(View.GONE);
                        linTab2.setVisibility(View.GONE);
                        if (tab.getText().equals("EVENT INFO")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Event Info", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ATTENDEES")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Attendees", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SPEAKERS")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Speakers", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SCHEDULE")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Schedule", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("EMERGENCY")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Emergency Contact", txtMainHeader, headerlogoIv);
                        }
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
                        sub2tabLayout.setVisibility(View.GONE);
                        linTab2.setVisibility(View.GONE);
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        if (tab.getText().equals("EVENT INFO")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Event Info", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ATTENDEES")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Attendees", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SPEAKERS")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Speakers", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SCHEDULE")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Schedule", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("EMERGENCY")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Emergency Contact", txtMainHeader, headerlogoIv);
                        }
                    }
                });
            }
            if (i == 4) {


                sub2tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                sub2tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                sub2tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                sub2tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);


                sub2tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        InputMethodManager imm = (InputMethodManager) MrgeHomeActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(tabLayout.getWindowToken(), 0);
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        sub2tabLayout.setVisibility(View.GONE);
                        linTab2.setVisibility(View.GONE);
                        if (tab.getText().equals("EVENT INFO")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Event Info", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ATTENDEES")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Attendees", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SPEAKERS")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Speakers", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SCHEDULE")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Schedule", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("EMERGENCY")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Emergency Contact", txtMainHeader, headerlogoIv);
                        }
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

                        sub2tabLayout.setVisibility(View.GONE);
                        linTab2.setVisibility(View.GONE);
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        if (tab.getText().equals("EVENT INFO")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Event Info", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ATTENDEES")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Attendees", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SPEAKERS")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Speakers", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SCHEDULE")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Schedule", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("EMERGENCY")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Emergency Contact", txtMainHeader, headerlogoIv);
                        }
                    }
                });
            } else if (i == 3) {

                sub2tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                sub2tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                sub2tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);

                sub2tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        linTab2.setVisibility(View.GONE);

                        sub2tabLayout.setVisibility(View.GONE);
                        if (tab.getText().equals("EVENT INFO")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Event Info", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ATTENDEES")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Attendees", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SPEAKERS")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Speakers", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SCHEDULE")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Schedule", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("EMERGENCY")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Emergency Contact", txtMainHeader, headerlogoIv);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                        String string1 = "#4D4D4D";
                        int color1 = Color.parseColor(string1);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
// int tabIconColor = ContextCompat.getColor(MrgeHomeActivity.this,color1);//tabunselected color
                        tab.getIcon().setColorFilter(color1, PorterDuff.Mode.SRC_IN);

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        sub2tabLayout.setVisibility(View.GONE);
                        linTab2.setVisibility(View.GONE);
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        if (tab.getText().equals("EVENT INFO")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Event Info", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ATTENDEES")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Attendees", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SPEAKERS")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Speakers", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SCHEDULE")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Schedule", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("EMERGENCY")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Emergency Contact", txtMainHeader, headerlogoIv);
                        }
                    }
                });
            } else if (i == 2) {

                sub2tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                sub2tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);


                sub2tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


// int tabIconColor = ContextCompat.getColor(MrgeHomeActivity.this, color); //tabselected color
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        sub2tabLayout.setVisibility(View.GONE);
                        linTab2.setVisibility(View.GONE);
                        if (tab.getText().equals("EVENT INFO")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Event Info", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ATTENDEES")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Attendees", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SPEAKERS")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Speakers", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SCHEDULE")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Schedule", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("EMERGENCY")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Emergency Contact", txtMainHeader, headerlogoIv);
                        }
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
                        sub2tabLayout.setVisibility(View.GONE);
                        linTab2.setVisibility(View.GONE);
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        if (tab.getText().equals("EVENT INFO")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Event Info", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ATTENDEES")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Attendees", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SPEAKERS")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Speakers", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SCHEDULE")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Schedule", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("EMERGENCY")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Emergency Contact Contact", txtMainHeader, headerlogoIv);
                        }
                    }
                });
            } else if (i == 1) {

                sub2tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);

                sub2tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        // sub2tabLayout.setVisibility(View.GONE);
                        if (tab.getText().equals("EVENT INFO")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Event Info", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ATTENDEES")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Attendees", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SPEAKERS")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Speakers", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SCHEDULE")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Schedule", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("EMERGENCY")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Emergency Contact Contact Contact", txtMainHeader, headerlogoIv);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        sub2tabLayout.setVisibility(View.GONE);
                        linTab2.setVisibility(View.GONE);
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        if (tab.getText().equals("EVENT INFO")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Event Info", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ATTENDEES")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Attendees", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SPEAKERS")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Speakers", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("SCHEDULE")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Schedule", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("EMERGENCY")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Emergency Contact", txtMainHeader, headerlogoIv);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void SubTab3Mechanism() {
        try {

            int i = sub3tabLayout.getTabCount();
            if (i == 4) {


                sub3tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                sub3tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                sub3tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                sub3tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);


                sub3tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        Util.logomethodwithText(MrgeHomeActivity.this, true, "Engagement", MrgeHomeActivity.txtMainHeader, MrgeHomeActivity.headerlogoIv);

                        String string = colorActive;
                        int color = Color.parseColor(string);
                        InputMethodManager imm = (InputMethodManager) MrgeHomeActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(tabLayout.getWindowToken(), 0);
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        sub3tabLayout.setVisibility(View.GONE);
                        linTab3.setVisibility(View.GONE);
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
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        sub3tabLayout.setVisibility(View.GONE);
                        linTab3.setVisibility(View.GONE);
                        if (tab.getText().equals("IMAGE")) {
                            grid_image_view.setVisibility(View.GONE);
                            list_image_view.setVisibility(View.GONE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Image", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("VIDEO")) {
                            grid_image_view.setVisibility(View.GONE);
                            list_image_view.setVisibility(View.GONE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Video", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("DOWNLOADS")) {
                            grid_image_view.setVisibility(View.VISIBLE);
                            list_image_view.setVisibility(View.VISIBLE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Downloads", txtMainHeader, headerlogoIv);
                        }
                    }
                });
            } else if (i == 3) {


                sub3tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                sub3tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                sub3tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);


                sub3tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        Util.logomethodwithText(MrgeHomeActivity.this, true, "IMAGE", MrgeHomeActivity.txtMainHeader, MrgeHomeActivity.headerlogoIv);
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        linTab3.setVisibility(View.GONE);

                        sub3tabLayout.setVisibility(View.GONE);

                        if (tab.getText().equals("IMAGE")) {
                            grid_image_view.setVisibility(View.GONE);
                            list_image_view.setVisibility(View.GONE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Image", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("VIDEO")) {
                            grid_image_view.setVisibility(View.GONE);
                            list_image_view.setVisibility(View.GONE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Video", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("DOWNLOADS")) {
                            grid_image_view.setVisibility(View.VISIBLE);
                            list_image_view.setVisibility(View.VISIBLE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Downloads", txtMainHeader, headerlogoIv);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                        String string1 = "#4D4D4D";
                        int color1 = Color.parseColor(string1);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
// int tabIconColor = ContextCompat.getColor(MrgeHomeActivity.this,color1);//tabunselected color
                        tab.getIcon().setColorFilter(color1, PorterDuff.Mode.SRC_IN);

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        sub3tabLayout.setVisibility(View.GONE);
                        linTab3.setVisibility(View.GONE);
                        if (tab.getText().equals("IMAGE")) {
                            grid_image_view.setVisibility(View.GONE);
                            list_image_view.setVisibility(View.GONE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Image", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("VIDEO")) {
                            grid_image_view.setVisibility(View.GONE);
                            list_image_view.setVisibility(View.GONE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Video", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("DOWNLOADS")) {
                            grid_image_view.setVisibility(View.VISIBLE);
                            list_image_view.setVisibility(View.VISIBLE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Downloads", txtMainHeader, headerlogoIv);
                        }
                    }
                });
            } else if (i == 2) {
                sub3tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                sub3tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);

                sub3tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        Util.logomethodwithText(MrgeHomeActivity.this, true, "VIDEO", MrgeHomeActivity.txtMainHeader, MrgeHomeActivity.headerlogoIv);
// int tabIconColor = ContextCompat.getColor(MrgeHomeActivity.this, color); //tabselected color
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        sub3tabLayout.setVisibility(View.GONE);
                        linTab3.setVisibility(View.GONE);
                        if (tab.getText().equals("IMAGE")) {
                            grid_image_view.setVisibility(View.GONE);
                            list_image_view.setVisibility(View.GONE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Image", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("VIDEO")) {
                            grid_image_view.setVisibility(View.GONE);
                            list_image_view.setVisibility(View.GONE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Video", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("DOWNLOADS")) {
                            grid_image_view.setVisibility(View.VISIBLE);
                            list_image_view.setVisibility(View.VISIBLE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Downloads", txtMainHeader, headerlogoIv);
                        }
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
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        sub3tabLayout.setVisibility(View.GONE);
                        linTab3.setVisibility(View.GONE);
                        if (tab.getText().equals("IMAGE")) {
                            grid_image_view.setVisibility(View.GONE);
                            list_image_view.setVisibility(View.GONE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Image", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("VIDEO")) {
                            grid_image_view.setVisibility(View.GONE);
                            list_image_view.setVisibility(View.GONE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Video", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("DOWNLOADS")) {
                            grid_image_view.setVisibility(View.VISIBLE);
                            list_image_view.setVisibility(View.VISIBLE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Downloads", txtMainHeader, headerlogoIv);
                        }
                    }
                });
            } else if (i == 1) {
                Util.logomethodwithText(this, true, "DOWNLOADS", MrgeHomeActivity.txtMainHeader, MrgeHomeActivity.headerlogoIv);

                sub3tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);

                sub3tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        // sub3tabLayout.setVisibility(View.GONE);
                        if (tab.getText().equals("IMAGE")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Image", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("VIDEO")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Video", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("DOWNLOADS")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Downloads", txtMainHeader, headerlogoIv);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        sub3tabLayout.setVisibility(View.GONE);
                        linTab3.setVisibility(View.GONE);
                        if (tab.getText().equals("IMAGE")) {
                            grid_image_view.setVisibility(View.GONE);
                            list_image_view.setVisibility(View.GONE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Image", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("VIDEO")) {
                            grid_image_view.setVisibility(View.GONE);
                            list_image_view.setVisibility(View.GONE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Video", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("DOWNLOADS")) {
                            grid_image_view.setVisibility(View.VISIBLE);
                            list_image_view.setVisibility(View.VISIBLE);
                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Downloads", txtMainHeader, headerlogoIv);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void SubTab4Mechanism() {
        try {

            int i = sub4tabLayout.getTabCount();
            if (i == 4) {


                sub4tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                sub4tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                sub4tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                sub4tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);


                sub4tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        String string = colorActive;
                        int color = Color.parseColor(string);
                        InputMethodManager imm = (InputMethodManager) MrgeHomeActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(tabLayout.getWindowToken(), 0);
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        sub4tabLayout.setVisibility(View.GONE);
                        linTab4.setVisibility(View.GONE);

                        if (tab.getText().equals("QUIZ")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Quiz", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("LIVE POLL")) {
                            // showQuizDialouge();

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Live Poll", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("Q&A")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Q&A", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ENGAGEMENT")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Engagement", txtMainHeader, headerlogoIv);
                        }
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
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        sub4tabLayout.setVisibility(View.GONE);
                        linTab4.setVisibility(View.GONE);
                        if (tab.getText().equals("QUIZ")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Quiz", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("LIVE POLL")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Live Poll", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("Q&A")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Q&A", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ENGAGEMENT")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Engagement", txtMainHeader, headerlogoIv);
                        }
                    }
                });
            } else if (i == 3) {

                sub4tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                sub4tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                sub4tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);

                sub4tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        linTab4.setVisibility(View.GONE);

                        sub4tabLayout.setVisibility(View.GONE);
                        if (tab.getText().equals("QUIZ")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Quiz", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("LIVE POLL")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Live Poll", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("Q&A")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Q&A", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ENGAGEMENT")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Engagement", txtMainHeader, headerlogoIv);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                        String string1 = "#4D4D4D";
                        int color1 = Color.parseColor(string1);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
// int tabIconColor = ContextCompat.getColor(MrgeHomeActivity.this,color1);//tabunselected color
                        tab.getIcon().setColorFilter(color1, PorterDuff.Mode.SRC_IN);

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        sub4tabLayout.setVisibility(View.GONE);
                        linTab4.setVisibility(View.GONE);
                        if (tab.getText().equals("QUIZ")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Quiz", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("LIVE POLL")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Live Poll", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("Q&A")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Q&A", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ENGAGEMENT")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Engagement", txtMainHeader, headerlogoIv);
                        }
                    }
                });
            } else if (i == 2) {

                sub4tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                sub4tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);


                sub4tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


// int tabIconColor = ContextCompat.getColor(MrgeHomeActivity.this, color); //tabselected color
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        sub4tabLayout.setVisibility(View.GONE);
                        linTab4.setVisibility(View.GONE);
                        if (tab.getText().equals("QUIZ")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Quiz", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("LIVE POLL")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Live Poll", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("Q&A")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Q&A", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ENGAGEMENT")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Engagement", txtMainHeader, headerlogoIv);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                        String string1 = "#4D4D4D";
                        int color1 = Color.parseColor(string1);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        tab.getIcon().setColorFilter(color1, PorterDuff.Mode.SRC_IN);
                        if (tab.getText().equals("QUIZ")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Quiz", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("LIVE POLL")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Live Poll", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("Q&A")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Q&A", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ENGAGEMENT")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Engagement", txtMainHeader, headerlogoIv);
                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        sub4tabLayout.setVisibility(View.GONE);
                        linTab4.setVisibility(View.GONE);
                    }
                });
            } else if (i == 1) {

                sub4tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);

                sub4tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        // sub3tabLayout.setVisibility(View.GONE);
                        if (tab.getText().equals("QUIZ")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Quiz", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("LIVE POLL")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Live Poll", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("Q&A")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Q&A", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ENGAGEMENT")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Engagement", txtMainHeader, headerlogoIv);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        grid_image_view.setVisibility(View.GONE);
                        list_image_view.setVisibility(View.GONE);
                        JzvdStd.releaseAllVideos();
                        sub4tabLayout.setVisibility(View.GONE);
                        linTab4.setVisibility(View.GONE);
                        if (tab.getText().equals("QUIZ")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Quiz", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("LIVE POLL")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Live Poll", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("Q&A")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Q&A", txtMainHeader, headerlogoIv);
                        } else if (tab.getText().equals("ENGAGEMENT")) {

                            Util.logomethodwithText(MrgeHomeActivity.this, true, "Engagement", txtMainHeader, headerlogoIv);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navmenudownloads, menu);
        //menu.clear();
        menu.removeGroup(R.id.downloads);
        return true;
    }

    private void showQuizDialouge() {

        myDialog = new Dialog(MrgeHomeActivity.this);
        myDialog.setContentView(R.layout.dialog_rate_layout);
        myDialog.setCancelable(false);
        myDialog.show();


    }

    class ViewPagerAdapterSub extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapterSub(FragmentManager manager) {
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

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private class WallPostReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // progressbarForSubmit.setVisibility(View.GONE);
            Log.d("service end", "service end");
        }
    }

    public static class NotificationCountReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // progressbarForSubmit.setVisibility(View.GONE);
            //Log.d("service end", "service end");
           /* SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String notificationCount = prefs1.getString("notificationCount", "");
            tv_notification.setVisibility(View.VISIBLE);
            ll_notification_count.setVisibility(View.VISIBLE);
            tv_notification.setText(notificationCount);*/
            setNotification(context,tv_notification,ll_notification_count);




            try {
                notificationCountReciever = new MrgeHomeActivity.NotificationCountReciever();
                notificationCountFilter = new IntentFilter(ApiConstant.BROADCAST_ACTION_FOR_NOTIFICATION_COUNT);
                LocalBroadcastManager.getInstance(context).registerReceiver(notificationCountReciever, notificationCountFilter);
            } catch (Exception e) {
                e.printStackTrace();
            }


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

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}