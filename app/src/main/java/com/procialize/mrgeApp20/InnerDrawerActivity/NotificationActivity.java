package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.procialize.mrgeApp20.Activity.AttendeeDetailActivity;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.NewsFeed.Views.Activity.CommentActivity;
import com.procialize.mrgeApp20.NewsFeed.Views.Activity.LikeDetailActivity;

import com.procialize.mrgeApp20.Adapter.NotificationAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.GetterSetter.NewsFeedList;
import com.procialize.mrgeApp20.GetterSetter.NotificationList;
import com.procialize.mrgeApp20.GetterSetter.NotificationListFetch;
import com.procialize.mrgeApp20.GetterSetter.NotificationSend;
import com.procialize.mrgeApp20.GetterSetter.news_feed_media;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;
import com.procialize.mrgeApp20.util.GetUserActivityReport;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.NotificationAdapterListner {


    SwipeRefreshLayout notificationRvrefresh;
    RecyclerView notificationRv;
    //    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, logoImg, colorActive;
    ImageView headerlogoIv;
    List<EventSettingList> eventSettingLists;
    String news_feed_share,
            news_feed_comment="1",
            news_feed_like="1";
    private APIService mAPIService;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private List<NewsFeedList> newsfeedsDBList;
    private List<AttendeeList> attendeeDBList;
    Dialog myDialog;
    int currentApiVersion = android.os.Build.VERSION.SDK_INT;
    String formatdate;
    final long[] time = new long[1];
    String token, attendee_status;
    ImageView add_icon;
    RelativeLayout linear;
    TextView msg, pullrefresh;
    private List<NotificationList> notificationDBList = new ArrayList<>();
    ConnectionDetector cd;
    List<news_feed_media> news_feed_media = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs1.edit();
        editor.putString("notificationCount", "0");
        editor.commit();


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        logoImg = prefs.getString("logoImg", "");
        colorActive = prefs.getString("colorActive", "");


        procializeDB = new DBHelper(NotificationActivity.this);
        db = procializeDB.getWritableDatabase();
        dbHelper = new DBHelper(NotificationActivity.this);
        //   overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        cd = new ConnectionDetector(NotificationActivity.this);
        TextView notyHeader = findViewById(R.id.notyHeader);
        notyHeader.setTextColor(Color.parseColor(colorActive));
        notificationRv = findViewById(R.id.notificationRv);
        linear = findViewById(R.id.linear);
        msg = findViewById(R.id.msg);
        pullrefresh = findViewById(R.id.pullrefresh);
        add_icon = findViewById(R.id.add_icon);
        int colorInt = Color.parseColor(colorActive);
        msg.setTextColor(Color.parseColor(colorActive));
        pullrefresh.setTextColor(Color.parseColor(colorActive));
        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(add_icon.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        add_icon.setImageDrawable(drawable);
//        progressBar = findViewById(R.id.progressBar);
        notificationRvrefresh = findViewById(R.id.notificationRvrefresh);

        try {
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

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);
        attendee_status = user.get(SessionManager.ATTENDEE_STATUS);

        crashlytics("Notification", token);
        firbaseAnalytics(this,"Notification", token);
        eventSettingLists = new ArrayList<>();
        eventSettingLists = SessionManager.loadEventList();
        applysetting(eventSettingLists);


        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        notificationRv.setLayoutManager(mLayoutManager);


        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        // notificationRv.setLayoutAnimation(animation);


        if (cd.isConnectingToInternet()) {
            fetchNotification(token, eventid);
        } else {
            notificationDBList.clear();
            notificationDBList = dbHelper.getNotificationDetails();

            if (notificationDBList.size() == 0) {
                notificationRv.setVisibility(View.GONE);
                msg.setVisibility(View.VISIBLE);
            } else {
                notificationRv.setVisibility(View.VISIBLE);
                msg.setVisibility(View.GONE);
                NotificationAdapter notificationAdapter = new NotificationAdapter(this, notificationDBList, this);
                notificationAdapter.notifyDataSetChanged();
                notificationRv.setAdapter(notificationAdapter);
                notificationRv.scheduleLayoutAnimation();
            }
        }

        notificationRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNotification(token, eventid);
            }
        });

        if (attendee_status.equalsIgnoreCase("1")) {
            add_icon.setVisibility(View.VISIBLE);
        } else {
            add_icon.setVisibility(View.GONE);
        }

        add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge();
            }
        });

        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this, token,
                eventid,
                ApiConstant.pageVisited,
                "40",
                "");
        getUserActivityReport.userActivityReport();
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {
            if (eventSettingLists.get(i).getFieldName().equals("news_feed")) {
                if (eventSettingLists.get(i).getSub_menuList() != null) {
                    if (eventSettingLists.get(i).getSub_menuList().size() > 0) {
                        for (int k = 0; k < eventSettingLists.get(i).getSub_menuList().size(); k++) {
                             if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_comment")) {
                                news_feed_comment = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_like")) {
                                news_feed_like = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_share")) {
                                news_feed_share = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }
                        }
                    }
                }
            }
        }
    }


    public void fetchNotification(String token, String eventid) {
//        showProgress();
        mAPIService.NotificationListFetch(token, eventid).enqueue(new Callback<NotificationListFetch>() {
            @Override
            public void onResponse(Call<NotificationListFetch> call, Response<NotificationListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (notificationRvrefresh.isRefreshing()) {
                        notificationRvrefresh.setRefreshing(false);
                    }
//                    dismissProgress();
                    showResponse(response);
                } else {

                    if (notificationRvrefresh.isRefreshing()) {
                        notificationRvrefresh.setRefreshing(false);
                    }
//                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationListFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
                if (notificationRvrefresh.isRefreshing()) {
                    notificationRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<NotificationListFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if ((!response.body().getNotificationList().isEmpty())) {
                dbHelper.clearNotificationTable();
                dbHelper.insertNotificationList(response.body().getNotificationList(), db);
                notificationRv.setVisibility(View.VISIBLE);
                msg.setVisibility(View.GONE);
                NotificationAdapter notificationAdapter = new NotificationAdapter(this, response.body().getNotificationList(), this);
                notificationAdapter.notifyDataSetChanged();
                notificationRv.setAdapter(notificationAdapter);
            } else {
                notificationRv.setVisibility(View.GONE);
                msg.setVisibility(View.VISIBLE);

            }
        } else {
            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }


    @Override
    public void onContactSelected(NotificationList notification, Context context) {
        if (notification.getNotificationType() != null) {
            db = procializeDB.getReadableDatabase();

            newsfeedsDBList = dbHelper.getNewsFeedLikeandComment(notification.getNotificationPostId());
           // if (notification.getNotificationType().equalsIgnoreCase("Cmnt") && news_feed_comment != null) {
            if (notification.getNotificationType().equalsIgnoreCase("Cmnt") && news_feed_comment != null) {

                if (!news_feed_comment.equalsIgnoreCase("0")) {
                    Intent comment = new Intent(this, CommentActivity.class);
                    comment.putExtra("feedid", notification.getNotificationPostId());
                    comment.putExtra("type", notification.getNotificationType());

                    comment.putExtra("noti_type", "Notification");
                    try {
                        float width = Float.parseFloat(newsfeedsDBList.get(0).getWidth());
                        float height = Float.parseFloat(newsfeedsDBList.get(0).getHeight());

                        float p1 = height / width;
                        comment.putExtra("heading", newsfeedsDBList.get(0).getPostStatus());
                        comment.putExtra("company", newsfeedsDBList.get(0).getCompanyName());
                        comment.putExtra("fname", newsfeedsDBList.get(0).getFirstName());
                        comment.putExtra("lname", newsfeedsDBList.get(0).getLastName());
                        comment.putExtra("profilepic", newsfeedsDBList.get(0).getProfilePic());
                        comment.putExtra("Likes", newsfeedsDBList.get(0).getTotalLikes());
                        comment.putExtra("Comments", newsfeedsDBList.get(0).getTotalComments());
                        comment.putExtra("designation", newsfeedsDBList.get(0).getDesignation());
                        comment.putExtra("Likeflag", newsfeedsDBList.get(0).getLikeFlag());
                        comment.putExtra("date", newsfeedsDBList.get(0).getPostDate());

                        comment.putExtra("AspectRatio", p1);
                        news_feed_media = newsfeedsDBList.get(0).getNews_feed_media();
                        if (news_feed_media.size() > 1) {
                            comment.putExtra("media_list", (Serializable) news_feed_media);
                        } else if (news_feed_media.size() > 0) {
                            comment.putExtra("type", news_feed_media.get(0).getMedia_type());
                            if (news_feed_media.get(0).getMedia_type().equalsIgnoreCase("Image")) {
                                comment.putExtra("url", ApiConstant.newsfeedwall + news_feed_media.get(0).getMediaFile());
                            } else if (news_feed_media.get(0).getMedia_type().equalsIgnoreCase("Video")) {
                                comment.putExtra("videourl", ApiConstant.newsfeedwall + news_feed_media.get(0).getMediaFile());
                                comment.putExtra("thumbImg", ApiConstant.newsfeedwall + news_feed_media.get(0).getThumb_image());
                            }
                        } else {
                            comment.putExtra("type", "status");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    startActivity(comment);

                }

            }
            else if (notification.getNotificationType().equalsIgnoreCase("Msg")) {

                attendeeDBList = dbHelper.getAttendeeDetailsId(notification.getAttendeeId());
                if (attendeeDBList.size() > 0) {

                    Intent attendeetail = new Intent(this, AttendeeDetailActivity.class);
                    attendeetail.putExtra("id", notification.getAttendeeId());
                    attendeetail.putExtra("name", notification.getAttendeeFirstName() + " " + notification.getAttendeeLastName());
                    attendeetail.putExtra("city", attendeeDBList.get(0).getCity());
                    attendeetail.putExtra("country", attendeeDBList.get(0).getCountry());
                    attendeetail.putExtra("company", notification.getCompanyName());
                    attendeetail.putExtra("designation", notification.getDesignation());
                    attendeetail.putExtra("description", attendeeDBList.get(0).getDescription());
                    attendeetail.putExtra("profile", notification.getProfilePic());
                    attendeetail.putExtra("mobile", attendeeDBList.get(0).getMobile());
                    attendeetail.putExtra("buddy_status", attendeeDBList.get(0).getBuddy_status());

                    startActivity(attendeetail);
                } else {
                    Intent attendeetail = new Intent(this, AttendeeDetailActivity.class);
                    attendeetail.putExtra("id", notification.getAttendeeId());
                    attendeetail.putExtra("name", notification.getAttendeeFirstName() + " " + notification.getAttendeeLastName());
                    attendeetail.putExtra("city", "");
                    attendeetail.putExtra("country", "");
                    attendeetail.putExtra("company", notification.getCompanyName());
                    attendeetail.putExtra("designation", notification.getDesignation());
                    attendeetail.putExtra("description", "");
                    attendeetail.putExtra("profile", notification.getProfilePic());
                    attendeetail.putExtra("mobile", " ");
                    attendeetail.putExtra("buddy_status", " ");

                    startActivity(attendeetail);
                }


            }
            else if (notification.getNotificationType().equalsIgnoreCase("Like") && news_feed_like != null) {
                newsfeedsDBList = dbHelper.getNewsFeedLikeandComment(notification.getNotificationPostId());
                if (!news_feed_like.equalsIgnoreCase("0")) {
                    //Intent likedetail = new Intent(this, LikeDetailActivity.class);
                    Intent likedetail = new Intent(this, CommentActivity.class);
                    likedetail.putExtra("feedid", notification.getNotificationPostId());
                    likedetail.putExtra("type", notification.getNotificationType());

                    likedetail.putExtra("noti_type", "Notification");
                    try {
                        float width = Float.parseFloat(newsfeedsDBList.get(0).getWidth());
                        float height = Float.parseFloat(newsfeedsDBList.get(0).getHeight());

                        float p1 = height / width;
                        likedetail.putExtra("heading", newsfeedsDBList.get(0).getPostStatus());
                        likedetail.putExtra("company", newsfeedsDBList.get(0).getCompanyName());
                        likedetail.putExtra("fname", newsfeedsDBList.get(0).getFirstName());
                        likedetail.putExtra("lname", newsfeedsDBList.get(0).getLastName());
                        likedetail.putExtra("profilepic", newsfeedsDBList.get(0).getProfilePic());
                        likedetail.putExtra("Likes", newsfeedsDBList.get(0).getTotalLikes());
                        likedetail.putExtra("Comments", newsfeedsDBList.get(0).getTotalComments());
                        likedetail.putExtra("designation", newsfeedsDBList.get(0).getDesignation());
                        likedetail.putExtra("Likeflag", newsfeedsDBList.get(0).getLikeFlag());
                        likedetail.putExtra("date", newsfeedsDBList.get(0).getPostDate());

                        likedetail.putExtra("AspectRatio", p1);
                        news_feed_media = newsfeedsDBList.get(0).getNews_feed_media();
                        if (news_feed_media.size() > 1) {
                            likedetail.putExtra("media_list", (Serializable) news_feed_media);
                        } else if (news_feed_media.size() > 0) {
                            likedetail.putExtra("type", news_feed_media.get(0).getMedia_type());
                            if (news_feed_media.get(0).getMedia_type().equalsIgnoreCase("Image")) {
                                likedetail.putExtra("url", ApiConstant.newsfeedwall + news_feed_media.get(0).getMediaFile());
                            } else if (news_feed_media.get(0).getMedia_type().equalsIgnoreCase("Video")) {
                                likedetail.putExtra("videourl", ApiConstant.newsfeedwall + news_feed_media.get(0).getMediaFile());
                                likedetail.putExtra("thumbImg", ApiConstant.newsfeedwall + news_feed_media.get(0).getThumb_image());
                            }
                        } else {
                            likedetail.putExtra("type", "status");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    startActivity(likedetail);
                }
            }
            else if (notification.getNotificationType().equalsIgnoreCase("T")) {

//                Intent intent = new Intent(NotificationActivity.this, HomeActivity.class);
//                startActivity(intent);

                Intent comment = new Intent(this, CommentActivity.class);
                comment.putExtra("feedid", notification.getNotificationPostId());
                comment.putExtra("type", notification.getNotificationType());

                comment.putExtra("noti_type", "Notification");
                try {
                    float width = Float.parseFloat(newsfeedsDBList.get(0).getWidth());
                    float height = Float.parseFloat(newsfeedsDBList.get(0).getHeight());

                    float p1 = height / width;
                    comment.putExtra("heading", newsfeedsDBList.get(0).getPostStatus());
                    comment.putExtra("company", newsfeedsDBList.get(0).getCompanyName());
                    comment.putExtra("fname", newsfeedsDBList.get(0).getFirstName());
                    comment.putExtra("lname", newsfeedsDBList.get(0).getLastName());
                    comment.putExtra("profilepic", newsfeedsDBList.get(0).getProfilePic());
                    comment.putExtra("Likes", newsfeedsDBList.get(0).getTotalLikes());
                    comment.putExtra("Comments", newsfeedsDBList.get(0).getTotalComments());
                    comment.putExtra("designation", newsfeedsDBList.get(0).getDesignation());
                    comment.putExtra("Likeflag", newsfeedsDBList.get(0).getLikeFlag());
                    comment.putExtra("date", newsfeedsDBList.get(0).getPostDate());

                    comment.putExtra("AspectRatio", p1);
                    news_feed_media = newsfeedsDBList.get(0).getNews_feed_media();
                    if (news_feed_media.size() > 1) {
                        comment.putExtra("media_list", (Serializable) news_feed_media);
                    } else if (news_feed_media.size() > 0) {
                        comment.putExtra("type", news_feed_media.get(0).getMedia_type());
                        if (news_feed_media.get(0).getMedia_type().equalsIgnoreCase("Image")) {
                            comment.putExtra("url", ApiConstant.newsfeedwall + news_feed_media.get(0).getMediaFile());
                        } else if (news_feed_media.get(0).getMedia_type().equalsIgnoreCase("Video")) {
                            comment.putExtra("videourl", ApiConstant.newsfeedwall + news_feed_media.get(0).getMediaFile());
                            comment.putExtra("thumbImg", ApiConstant.newsfeedwall + news_feed_media.get(0).getThumb_image());
                        }
                    } else {
                        comment.putExtra("type", "status");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                startActivity(comment);


            }
            else if (notification.getNotificationType().equalsIgnoreCase("Quiz") && news_feed_like != null) {
               /* Intent intent = new Intent(NotificationActivity.this, FolderQuizActivity.class);
                startActivity(intent);*/

            } else if (notification.getNotificationType().equalsIgnoreCase("Post") && news_feed_like != null) {
                Intent intent = new Intent(NotificationActivity.this, MrgeHomeActivity.class);
                intent.putExtra("notification_post_id", notification.getNotificationPostId());
                startActivity(intent);

            } else if (notification.getNotificationType().equalsIgnoreCase("Live_poll") && news_feed_like != null) {
                Intent intent = new Intent(NotificationActivity.this, LivePollActivity.class);
                startActivity(intent);

            }
        }


    }


    @Override
    public void onReplyClick(NotificationList notificationList, Context context) {


        attendeeDBList = dbHelper.getAttendeeDetailsId(notificationList.getAttendeeId());
        if (attendeeDBList.size() > 0) {

            Intent attendeetail = new Intent(this, AttendeeDetailActivity.class);
            attendeetail.putExtra("id", notificationList.getAttendeeId());
            attendeetail.putExtra("name", notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
            attendeetail.putExtra("city", attendeeDBList.get(0).getCity());
            attendeetail.putExtra("country", attendeeDBList.get(0).getCountry());
            attendeetail.putExtra("company", notificationList.getCompanyName());
            attendeetail.putExtra("designation", notificationList.getDesignation());
            attendeetail.putExtra("description", attendeeDBList.get(0).getDescription());
            attendeetail.putExtra("profile", notificationList.getProfilePic());
            attendeetail.putExtra("mobile", attendeeDBList.get(0).getMobile());
            attendeetail.putExtra("buddy_status", attendeeDBList.get(0).getBuddy_status());

            startActivity(attendeetail);
        } else {
            Intent attendeetail = new Intent(this, AttendeeDetailActivity.class);
            attendeetail.putExtra("id", notificationList.getAttendeeId());
            attendeetail.putExtra("name", notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
            attendeetail.putExtra("city", "");
            attendeetail.putExtra("country", "");
            attendeetail.putExtra("company", notificationList.getCompanyName());
            attendeetail.putExtra("designation", notificationList.getDesignation());
            attendeetail.putExtra("description", "");
            attendeetail.putExtra("profile", notificationList.getProfilePic());
            attendeetail.putExtra("mobile", " ");
            attendeetail.putExtra("buddy_status", " ");

            startActivity(attendeetail);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }

    private void showratedialouge() {

        myDialog = new Dialog(NotificationActivity.this);
        myDialog.setContentView(R.layout.add_notification);
        myDialog.setCancelable(false);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


        LinearLayout diatitle = myDialog.findViewById(R.id.diatitle);
        ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);
        ImageView id_date = myDialog.findViewById(R.id.id_date);
        Button canclebtn = myDialog.findViewById(R.id.canclebtn);
        Button send_notification = myDialog.findViewById(R.id.send_notification);
        final EditText etmsg = myDialog.findViewById(R.id.etmsg);
        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String dateString = sdf.format(date);
        nametv.setText(dateString);

        nametv.setTextColor(Color.parseColor(colorActive));
        diatitle.setBackgroundColor(Color.parseColor(colorActive));
        send_notification.setBackgroundColor(Color.parseColor(colorActive));
        canclebtn.setBackgroundColor(Color.parseColor(colorActive));

        id_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View dialogView = View.inflate(NotificationActivity.this, R.layout.activity_date_picker, null);
                final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(NotificationActivity.this).create();

                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {

                        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute());
                        datePicker.setMinDate(new Date().getTime());
                        int selectyear = datePicker.getYear();
                        int selectmonth = datePicker.getMonth();
                        int selectday = datePicker.getDayOfMonth();
                        int selecttime = 0;
                        int selecthour = 0;
                        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                            selecttime = timePicker.getMinute();
                            selecthour = timePicker.getHour();
                        } else {
                            selecttime = timePicker.getCurrentMinute();
                            selecthour = timePicker.getCurrentHour();

                        }
                        int seconds = calendar.get(Calendar.SECOND);

                        Date mDate = new GregorianCalendar(selectyear, selectmonth, selectday, selecthour, selecttime).getTime();
                        if (mDate.getTime() <= calendar.getTimeInMillis()) {
//                            int hour = hourOfDay % 12;
                        } else {
                            Toast.makeText(dialogView.getContext(), "Invalid Date", Toast.LENGTH_LONG).show();
                        }


                        formatdate = (String) android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss", mDate);
                        String date = (String) android.text.format.DateFormat.format("dd MMMM HH:mm", mDate);
                        nametv.setText(date);
                        time[0] = calendar.getTimeInMillis();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });


        send_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nametv.getText().toString().isEmpty()) {
                    Toast.makeText(NotificationActivity.this, "Select Date", Toast.LENGTH_SHORT).show();
                } else if (etmsg.getText().toString().isEmpty()) {
                    Toast.makeText(NotificationActivity.this, "Enter your message", Toast.LENGTH_SHORT).show();
                } else {
                    sendNotification(token, eventid, StringEscapeUtils.escapeJava(etmsg.getText().toString()), formatdate);
                }
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        etmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count = 250 - s.length();
                counttv.setText(count + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        myDialog.show();

    }

    public void sendNotification(String token, String eventid, String message, String display_time) {
//        showProgress();
        mAPIService.SendNotification(token, eventid, message, display_time).enqueue(new Callback<NotificationSend>() {
            @Override
            public void onResponse(Call<NotificationSend> call, Response<NotificationSend> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (notificationRvrefresh.isRefreshing()) {
                        notificationRvrefresh.setRefreshing(false);
                    }
//                    dismissProgress();
                    showResponseSendNotification(response);
                } else {

                    if (notificationRvrefresh.isRefreshing()) {
                        notificationRvrefresh.setRefreshing(false);
                    }
//                    dismissProgress();
                    Toast.makeText(NotificationActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationSend> call, Throwable t) {
                Toast.makeText(NotificationActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
                if (notificationRvrefresh.isRefreshing()) {
                    notificationRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponseSendNotification(Response<NotificationSend> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            myDialog.dismiss();
            Toast.makeText(NotificationActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        } else {
            myDialog.dismiss();
            Toast.makeText(NotificationActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

        }
    }

}
