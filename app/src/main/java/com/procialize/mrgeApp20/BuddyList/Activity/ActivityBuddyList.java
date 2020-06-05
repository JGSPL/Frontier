package com.procialize.mrgeApp20.BuddyList.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.BuddyList.Adapter.BuddyListAdapter;
import com.procialize.mrgeApp20.BuddyList.DataModel.Buddy;
import com.procialize.mrgeApp20.BuddyList.DataModel.FetchBuddyList;
import com.procialize.mrgeApp20.BuddyList.DataModel.FetchSendRequest;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.InnerDrawerActivity.NotificationActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityBuddyList extends AppCompatActivity  implements BuddyListAdapter.AttendeeAdapterListner{

    ConnectionDetector cd;
    RecyclerView attendeerecycler;
    SwipeRefreshLayout attendeefeedrefresh;
    EditText searchEt;
    BuddyListAdapter attendeeAdapter;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    private APIService mAPIService;
    private ProgressBar progressBar;
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private List<AttendeeList> attendeeDBList;
    LinearLayout ll_empty_view;
     String token;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy_list);

        openDisclaimerDialog();
        ll_empty_view = findViewById(R.id.ll_empty_view);
        initView();
    }

    public void initView()
    {
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
                finish();
            }
        });

        try {
            dbHelper = new DBHelper(this);
            procializeDB = new DBHelper(this);
            db = procializeDB.getWritableDatabase();
        }catch (Exception e){
            e.printStackTrace();
        }


        attendeeDBList = new ArrayList<>();

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
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ ApiConstant.folderName+"/" + "background.jpg");
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
        crashlytics("Attendee",token);
        firbaseAnalytics(this,"Attendee",token);
        if (cd.isConnectingToInternet()) {
            fetchFeed(token, eventid);
        } else {
           /* db = procializeDB.getReadableDatabase();

            attendeeDBList = dbHelper.getAttendeeDetails();

            attendeeAdapter = new BuddyListAdapter(this, attendeeDBList, this);
            attendeeAdapter.notifyDataSetChanged();
            attendeerecycler.setAdapter(attendeeAdapter);
            attendeerecycler.scheduleLayoutAnimation();*/
        }

        attendeefeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (cd.isConnectingToInternet()) {
                    fetchFeed(token, eventid);
                } else {
                    /*db = procializeDB.getReadableDatabase();

                    attendeeDBList = dbHelper.getAttendeeDetails();

                    attendeeAdapter = new BuddyListAdapter(ActivityBuddyList.this, attendeeDBList, ActivityBuddyList.this);
                    attendeeAdapter.notifyDataSetChanged();
                    attendeerecycler.setAdapter(attendeeAdapter);
                    attendeerecycler.scheduleLayoutAnimation();

                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(true);
                    }*/
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
                attendeeAdapter.getFilter().filter(s.toString());
            }
        });
    }

    public void fetchFeed(String token, String eventid) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.getBuddyList(eventid,token).enqueue(new Callback<FetchBuddyList>() {
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
        if (!(response.body().getBuddyList().isEmpty())) {

            ll_empty_view.setVisibility(View.GONE);
            attendeerecycler.setVisibility(View.VISIBLE);

           /* dbHelper.clearAttendeesTable();
            dbHelper.insertAttendeesInfo(response.body().getAttendeeList(), db);
*/

            attendeeAdapter = new BuddyListAdapter(ActivityBuddyList.this, response.body().getBuddyList(), this);
            attendeeAdapter.notifyDataSetChanged();
            attendeerecycler.setAdapter(attendeeAdapter);
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

    public void openDisclaimerDialog()
    {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.buddy_list_disclaimer_dialog, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                deleteDialog.dismiss();
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
        AccectRejectMethod(eventid,token,attendee.getFriend_id(),"1");
    }

    @Override
    public void onRejectSelected(Buddy attendee) {
        AccectRejectMethod(eventid,token,attendee.getFriend_id(),"2");

    }

    @Override
    public void onCancelSelected(Buddy attendee) {
        CancelMethod(eventid,token,attendee.getFriend_id());
    }

    public void CancelMethod(String eventid, String toke, String Buddy_id) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.cancelFriendRequest(eventid,toke, Buddy_id).enqueue(new Callback<FetchSendRequest>() {
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
        mAPIService.respondToFriendRequest(eventid,toke, Buddy_id, response).enqueue(new Callback<FetchSendRequest>() {
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


}
