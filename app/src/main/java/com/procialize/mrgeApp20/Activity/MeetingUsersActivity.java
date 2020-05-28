package com.procialize.mrgeApp20.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.mrgeApp20.Adapter.MeetingUserAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorMeetingUserListing;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import java.io.File;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingUsersActivity extends AppCompatActivity {
    ImageView headerlogoIv;
    RecyclerView recycler_user;
    LinearLayout linear;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive, apikey, exhibitor_id, exhibitor_status;
    APIService mAPIService;
    SessionManager sessionManager;
    MeetingUserAdapter adapter;
    SwipeRefreshLayout attendeefeedrefresh;
    EditText searchEt;
    TextView txt_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_users);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        headerlogoIv = findViewById(R.id.headerlogoIv);
        recycler_user = findViewById(R.id.recycler_user);
        attendeefeedrefresh = findViewById(R.id.attendeefeedrefresh);
        searchEt = findViewById(R.id.searchEt);
        linear = findViewById(R.id.linear);
        txt_header = findViewById(R.id.txt_header);
        Util.logomethod(this, headerlogoIv);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        txt_header.setTextColor(Color.parseColor(colorActive));
        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(MeetingUsersActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        apikey = user.get(SessionManager.KEY_TOKEN);
        exhibitor_id = user.get(SessionManager.EXHIBITOR_ID);
        exhibitor_status = user.get(SessionManager.EXHIBITOR_STATUS);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler_user.setLayoutManager(mLayoutManager);

        try {

            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        ExhibitorViewUserListing(eventid, apikey, exhibitor_id);

        attendeefeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ExhibitorViewUserListing(eventid, apikey, exhibitor_id);
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
                adapter.getFilter().filter(s.toString());
            }
        });
    }

    public void ExhibitorViewUserListing(String event_id, String token, String exhibitor_id) {
        mAPIService.ExhibitorMeetingUserListing(token, event_id, exhibitor_id).enqueue(new Callback<ExhibitorMeetingUserListing>() {
            @Override
            public void onResponse(Call<ExhibitorMeetingUserListing> call, Response<ExhibitorMeetingUserListing> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(false);
                    }
                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        Intent main = new Intent(MeetingUsersActivity.this, LoginActivity.class);
                        startActivity(main);
                        finish();

                    } else {
                        adapter = new MeetingUserAdapter(MeetingUsersActivity.this, response.body().getExhibitor_meeting_user_list());
                        adapter.notifyDataSetChanged();
                        recycler_user.setAdapter(adapter);


                    }
                } else {
                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ExhibitorMeetingUserListing> call, Throwable t) {
                Toast.makeText(MeetingUsersActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();
                if (attendeefeedrefresh.isRefreshing()) {
                    attendeefeedrefresh.setRefreshing(false);
                }
            }
        });
    }



}

