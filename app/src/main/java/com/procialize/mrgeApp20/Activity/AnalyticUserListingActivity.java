package com.procialize.mrgeApp20.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.procialize.mrgeApp20.Adapter.UserAnalyticAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.GetterSetter.ExhibitoreBrochureViewUserListing;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyticUserListingActivity extends AppCompatActivity {

    ImageView headerlogoIv;
    RecyclerView recycler_user;
    LinearLayout linear;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive, apikey, exhibitor_id, exhibitor_status, brochoure_id, brochoure_name;
    APIService mAPIService;
    SessionManager sessionManager;
    EditText searchEt;
    UserAnalyticAdapter adapter;
    SwipeRefreshLayout attendeefeedrefresh;
    ConnectionDetector cd;
    TextView txt_header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytic_user_listing);

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

        Intent intent = getIntent();
        brochoure_id = intent.getStringExtra("brochoure_id");
        brochoure_name = intent.getStringExtra("brochoure_name");

        headerlogoIv = findViewById(R.id.headerlogoIv);
        recycler_user = findViewById(R.id.recycler_user);
        linear = findViewById(R.id.linear);
        attendeefeedrefresh = findViewById(R.id.attendeefeedrefresh);
        searchEt = findViewById(R.id.searchEt);
        txt_header = findViewById(R.id.txt_header);
        Util.logomethod(this, headerlogoIv);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        txt_header.setTextColor(Color.parseColor(colorActive));
        txt_header.setText("Exhibitor Analytics : " + StringEscapeUtils.unescapeJava(brochoure_name));
        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(AnalyticUserListingActivity.this);
        cd = new ConnectionDetector(AnalyticUserListingActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        apikey = user.get(SessionManager.KEY_TOKEN);
        exhibitor_id = user.get(SessionManager.EXHIBITOR_ID);
        exhibitor_status = user.get(SessionManager.EXHIBITOR_STATUS);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler_user.setLayoutManager(mLayoutManager);

        try {

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


        if (cd.isConnectingToInternet()) {
            ExhibitorViewUserListing(eventid, apikey, exhibitor_id, brochoure_id);
        } else {
            Toast.makeText(AnalyticUserListingActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        attendeefeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {
                    ExhibitorViewUserListing(eventid, apikey, exhibitor_id, brochoure_id);
                } else {
                    Toast.makeText(AnalyticUserListingActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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
                adapter.getFilter().filter(s.toString());
            }
        });
    }

    public void ExhibitorViewUserListing(String event_id, String token, String exhibitor_id, String brochoure_id) {
        mAPIService.ExhibitoreBrochureViewUserListing(token, event_id, exhibitor_id, brochoure_id).enqueue(new Callback<ExhibitoreBrochureViewUserListing>() {
            @Override
            public void onResponse(Call<ExhibitoreBrochureViewUserListing> call, Response<ExhibitoreBrochureViewUserListing> response) {

                if (response.isSuccessful()) {
                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(false);
                    }
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        Intent main = new Intent(AnalyticUserListingActivity.this, LoginActivity.class);
                        startActivity(main);
                        finish();

                    } else {

                        adapter = new UserAnalyticAdapter(AnalyticUserListingActivity.this, response.body().getExhibitor_brochure_view_user_list());
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
            public void onFailure(Call<ExhibitoreBrochureViewUserListing> call, Throwable t) {
                Toast.makeText(AnalyticUserListingActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();
                if (attendeefeedrefresh.isRefreshing()) {
                    attendeefeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
