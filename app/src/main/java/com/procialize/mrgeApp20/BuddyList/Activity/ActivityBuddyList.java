package com.procialize.mrgeApp20.BuddyList.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.BuddyList.Adapter.BuddyListAdapter;
import com.procialize.mrgeApp20.BuddyList.DataModel.Buddy;
import com.procialize.mrgeApp20.BuddyList.DataModel.FetchBuddyList;
import com.procialize.mrgeApp20.BuddyList.DataModel.FetchSendRequest;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.BaseResponse;
import com.procialize.mrgeApp20.InnerDrawerActivity.NotificationActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.KeyboardUtility;
import com.procialize.mrgeApp20.Utility.Util;
import com.procialize.mrgeApp20.util.GetUserActivityReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_BUDDYLIST_PATH;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_PROFILE_PIC_PATH;
import static com.procialize.mrgeApp20.Utility.Util.setNotification;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityBuddyList extends AppCompatActivity implements BuddyListAdapter.AttendeeAdapterListner {

    public static String chat_id = "0";
    ConnectionDetector cd;
    RecyclerView attendeerecycler;
    SwipeRefreshLayout attendeefeedrefresh;
    EditText searchEt;
    BuddyListAdapter attendeeAdapter;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    LinearLayout ll_empty_view;
    String token;
    SpotChatReciever spotChatReciever;
    IntentFilter spotChatFilter;
    private APIService mAPIService;
    private ProgressBar progressBar;
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private List<Buddy> buddyDBList;
    String is_tc_accepted = "0";
    AlertDialog deleteDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy_list);


        ll_empty_view = findViewById(R.id.ll_empty_view);
        initView();
    }

    public void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        Intent intent=getIntent();
//        eventid=intent.getStringExtra("eventId");
//        eventnamestr=intent.getStringExtra("eventnamestr");
        cd = new ConnectionDetector(ActivityBuddyList.this);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onBackPressed();
                KeyboardUtility.hideSoftKeyboard(ActivityBuddyList.this);

                finish();
            }
        });

        try {
            dbHelper = new DBHelper(this);
            procializeDB = new DBHelper(this);
            db = procializeDB.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }



        buddyDBList = new ArrayList<>();

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        attendeerecycler = findViewById(R.id.attendeerecycler);
        searchEt = findViewById(R.id.searchEt);
        attendeefeedrefresh = findViewById(R.id.attendeefeedrefresh);
        progressBar = findViewById(R.id.progressBar);

        ImageView notificationlogoIv = findViewById(R.id.notificationlogoIv);
        notificationlogoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(main);
                //finish();
            }
        });

        try {
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/" + ApiConstant.folderName + "/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            //linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            //linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }
       /* TextView header = findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));*/
        //pullrefresh.setTextColor(Color.parseColor(colorActive));

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        attendeerecycler.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        //attendeerecycler.setLayoutAnimation(animation);


        mAPIService = ApiUtils.getBuddyAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);

        SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        is_tc_accepted = prefs1.getString("buddy_tc_accepted","");


        if(is_tc_accepted.equalsIgnoreCase("0"))
        { openDisclaimerDialog();}

        crashlytics("Attendee", token);
        firbaseAnalytics(this, "Attendee", token);
        if (cd.isConnectingToInternet()) {
            fetchFeed(token, eventid);
        } else {
            if (attendeefeedrefresh.isRefreshing()) {
                attendeefeedrefresh.setRefreshing(false);
            }

            db = procializeDB.getReadableDatabase();

            buddyDBList = dbHelper.getBuddyDetail();

            attendeeAdapter = new BuddyListAdapter(this, buddyDBList, this);
            attendeeAdapter.notifyDataSetChanged();
            attendeerecycler.setAdapter(attendeeAdapter);
            attendeerecycler.scheduleLayoutAnimation();
        }

        attendeefeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (cd.isConnectingToInternet()) {
                    fetchFeed(token, eventid);
                } else {
                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(false);
                    }

                    db = procializeDB.getReadableDatabase();

                    buddyDBList = dbHelper.getBuddyDetail();

                    attendeeAdapter = new BuddyListAdapter(ActivityBuddyList.this, buddyDBList, ActivityBuddyList.this);
                    attendeeAdapter.notifyDataSetChanged();
                    attendeerecycler.setAdapter(attendeeAdapter);
                    attendeerecycler.scheduleLayoutAnimation();

                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(true);
                    }
                }
            }
        });


        searchEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                //attendeeAdapter.getFilter().filter(s.toString());
                if (attendeeAdapter != null) {
                    try {
                        attendeeAdapter.getFilter().filter(s.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }


        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this, token,
                eventid,
                ApiConstant.pageVisited,
                "42",
                "");
        getUserActivityReport.userActivityReport();

        try {
            spotChatReciever = new SpotChatReciever();
            spotChatFilter = new IntentFilter(ApiConstant.BROADCAST_ACTION_FOR_SPOT_ChatBuddy);
            LocalBroadcastManager.getInstance(this).registerReceiver(spotChatReciever, spotChatFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchFeed(String token, String eventid) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.getBuddyList(eventid, token).enqueue(new Callback<FetchBuddyList>() {
            @Override
            public void onResponse(Call<FetchBuddyList> call, Response<FetchBuddyList> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressBar.setVisibility(View.GONE);
                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(false);
                    }
                    showResponse(response);
                } else {
                    progressBar.setVisibility(View.GONE);

                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(false);
                    }
                    Toast.makeText(ActivityBuddyList.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchBuddyList> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(ActivityBuddyList.this, "Low network or no network", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                if (attendeefeedrefresh.isRefreshing()) {
                    attendeefeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<FetchBuddyList> response) {

        // specify an adapter (see also next example)
        if (response.body().getBuddyList() != null) {
            //if (!(response.body().getBuddyList().isEmpty())) {

            ll_empty_view.setVisibility(View.GONE);
            attendeerecycler.setVisibility(View.VISIBLE);


            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(KEY_BUDDYLIST_PATH, response.body().getProfile_pic_url_path());
            edit.putString("buddy_tc_accepted",response.body().getBuddy_accept_terms());
            edit.commit();


            dbHelper.clearBuddyTable();
            dbHelper.insertBuddyInfo(response.body().getBuddyList(), db);

            attendeeAdapter = new BuddyListAdapter(ActivityBuddyList.this, response.body().getBuddyList(), this);
            attendeeAdapter.notifyDataSetChanged();
            attendeerecycler.setAdapter(attendeeAdapter);


            //}
        } else {

            ll_empty_view.setVisibility(View.VISIBLE);
            attendeerecycler.setVisibility(View.GONE);

        }
    }


    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }

    public void openDisclaimerDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.buddy_list_disclaimer_dialog, null);
        deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialog.setCancelable(false);
        CheckBox checkBox = deleteDialogView.findViewById(R.id.checkBox);


        deleteDialogView.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                if (checkBox.isChecked()) {

                    acceptTnc(token,eventid);
                } else {
                    Toast.makeText(ActivityBuddyList.this, "Please agree with terms and conditions to continue", Toast.LENGTH_SHORT).show();

                }
            }
        });
        deleteDialog.show();
    }

    @Override
    public void onContactSelected(Buddy attendee) {
        Intent attendeetail = new Intent(this, ActivityBuddyChat.class);
        attendeetail.putExtra("id", attendee.getFriend_id());
        attendeetail.putExtra("name", attendee.getFirstName() + " " + attendee.getLastName());
        attendeetail.putExtra("city", attendee.getCity());
        attendeetail.putExtra("country", attendee.getDesignation());
        attendeetail.putExtra("company", attendee.getDesignation());
        attendeetail.putExtra("designation", attendee.getDesignation());
        attendeetail.putExtra("description", attendee.getDesignation());
        attendeetail.putExtra("profile", attendee.getProfilePic());
        attendeetail.putExtra("mobile", attendee.getDesignation());
//                speakeretail.putExtra("totalrate",attendee.getTotalRating());
        startActivity(attendeetail);
    }

    @Override
    public void onAcceptSelected(Buddy attendee) {
        AccectRejectMethod(eventid, token, attendee.getFriend_id(), "1");
    }

    @Override
    public void onRejectSelected(Buddy attendee) {
        AccectRejectMethod(eventid, token, attendee.getFriend_id(), "2");

    }

    @Override
    public void onCancelSelected(Buddy attendee) {
        CancelMethod(eventid, token, attendee.getFriend_id());
    }

    public void CancelMethod(String eventid, String toke, String Buddy_id) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.cancelFriendRequest(eventid, toke, Buddy_id).enqueue(new Callback<FetchSendRequest>() {
            @Override
            public void onResponse(Call<FetchSendRequest> call, Response<FetchSendRequest> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressBar.setVisibility(View.GONE);
                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(false);
                    }
                    showResponseBuddy(response);
                } else {
                    progressBar.setVisibility(View.GONE);

                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(false);
                    }
                    Toast.makeText(ActivityBuddyList.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchSendRequest> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(ActivityBuddyList.this, "Low network or no network", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                if (attendeefeedrefresh.isRefreshing()) {
                    attendeefeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void AccectRejectMethod(String eventid, String toke, String Buddy_id, String response) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.respondToFriendRequest(eventid, toke, Buddy_id, response).enqueue(new Callback<FetchSendRequest>() {
            @Override
            public void onResponse(Call<FetchSendRequest> call, Response<FetchSendRequest> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressBar.setVisibility(View.GONE);
                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(false);
                    }
                    showResponseBuddy(response);
                } else {
                    progressBar.setVisibility(View.GONE);

                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(false);
                    }
                    Toast.makeText(ActivityBuddyList.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchSendRequest> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(ActivityBuddyList.this, "Low network or no network", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                if (attendeefeedrefresh.isRefreshing()) {
                    attendeefeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponseBuddy(Response<FetchSendRequest> response) {

        if (response.body().getStatus().equalsIgnoreCase("success")) {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            fetchFeed(token, eventid);

        } else {

            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cd.isConnectingToInternet()) {
            fetchFeed(token, eventid);
        }

    }


    private class SpotChatReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            attendeeAdapter.notifyDataSetChanged();
        }
    }

    public void acceptTnc(String token, String eventid) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.acceptBuddyTerms(token,eventid ).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    showResponseAcceptTC(response);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(ActivityBuddyList.this, "Low network or no network", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                if (attendeefeedrefresh.isRefreshing()) {
                    attendeefeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponseAcceptTC(Response<BaseResponse> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus() != null) {
            deleteDialog.dismiss();


            SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs1.edit();
            editor.putString("buddy_tc_accepted","1");
            editor.commit();
        } else {

        }
    }

}
