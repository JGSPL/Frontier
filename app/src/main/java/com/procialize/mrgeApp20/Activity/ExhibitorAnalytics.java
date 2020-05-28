package com.procialize.mrgeApp20.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.mrgeApp20.Adapter.DocumentAnalyticAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.GetterSetter.Brochure_Anlytics_Result;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorDashboard;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import java.io.File;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExhibitorAnalytics extends AppCompatActivity implements DocumentAnalyticAdapter.DocumentAnalyticAdapterListner {

    ImageView headerlogoIv;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive, apikey, exhibitor_id, exhibitor_status;
    APIService mAPIService;
    SessionManager sessionManager;
    LinearLayout linear, linear1, linear2, linear3, linearmain1, linearmain2, linearmain3;
    RecyclerView recycler_document;
    TextView txt_header, txt_meeting, txt_msgcnt, txt_visitcnt,pullrefresh;
    DocumentAnalyticAdapter adapter;
    SwipeRefreshLayout swiperefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitor_analytics);

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


        headerlogoIv = findViewById(R.id.headerlogoIv);
        linear = findViewById(R.id.linear);
        recycler_document = findViewById(R.id.recycler_document);
        txt_header = findViewById(R.id.txt_header);
        txt_meeting = findViewById(R.id.txt_meeting);
        txt_msgcnt = findViewById(R.id.txt_msgcnt);
        txt_visitcnt = findViewById(R.id.txt_visitcnt);
        linearmain1 = findViewById(R.id.linearmain1);
        linearmain2 = findViewById(R.id.linearmain2);
        linearmain3 = findViewById(R.id.linearmain3);
        swiperefresh = findViewById(R.id.swiperefresh);
        pullrefresh = findViewById(R.id.pullrefresh);
        Util.logomethod(this, headerlogoIv);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        txt_header.setTextColor(Color.parseColor(colorActive));
        txt_visitcnt.setTextColor(Color.parseColor(colorActive));
        txt_msgcnt.setTextColor(Color.parseColor(colorActive));
        txt_meeting.setTextColor(Color.parseColor(colorActive));
        pullrefresh.setTextColor(Color.parseColor(colorActive));

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(ExhibitorAnalytics.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        apikey = user.get(SessionManager.KEY_TOKEN);
        exhibitor_id = user.get(SessionManager.EXHIBITOR_ID);
        exhibitor_status = user.get(SessionManager.EXHIBITOR_STATUS);

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
        linear3 = (LinearLayout) findViewById(R.id.linear3);
        linear1 = (LinearLayout) findViewById(R.id.linear1);
        linear2 = (LinearLayout) findViewById(R.id.linear2);

        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(80);
        shape.setColor(Color.parseColor(colorActive));

        linear3.setBackground(shape);
        linear1.setBackground(shape);
        linear2.setBackground(shape);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler_document.setLayoutManager(mLayoutManager);

        ExhibitorDashboard(eventid, apikey, exhibitor_id);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ExhibitorDashboard(eventid, apikey, exhibitor_id);
            }
        });

        linearmain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(ExhibitorAnalytics.this, VisitUsersActivity.class);
                startActivity(main);

            }
        });
        linearmain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(ExhibitorAnalytics.this, MsgInboxUsersActivity.class);
                startActivity(main);

            }
        });
        linearmain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(ExhibitorAnalytics.this, MeetingUsersActivity.class);
                startActivity(main);

            }
        });

    }

    public void ExhibitorDashboard(String event_id, String token, String exhibitor_id) {
        mAPIService.ExhibitorDashboard(token, event_id, exhibitor_id).enqueue(new Callback<ExhibitorDashboard>() {
            @Override
            public void onResponse(Call<ExhibitorDashboard> call, Response<ExhibitorDashboard> response) {
                if (swiperefresh.isRefreshing()) {
                    swiperefresh.setRefreshing(false);
                }
                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        Intent main = new Intent(ExhibitorAnalytics.this, LoginActivity.class);
                        startActivity(main);
                        finish();

                    } else {

                        txt_visitcnt.setText(response.body().getTotal_exhibitor_visits());
                        txt_msgcnt.setText(response.body().getTotal_exhibitor_msg());
                        txt_meeting.setText(response.body().getTotal_exhibitor_meeting());

                        adapter = new DocumentAnalyticAdapter(ExhibitorAnalytics.this, response.body().getBrochure_anlytics_result(), ExhibitorAnalytics.this);
                        adapter.notifyDataSetChanged();
                        recycler_document.setAdapter(adapter);


                    }
                } else {
                    if (swiperefresh.isRefreshing()) {
                        swiperefresh.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ExhibitorDashboard> call, Throwable t) {
                Toast.makeText(ExhibitorAnalytics.this, "Low network or no network", Toast.LENGTH_SHORT).show();
                if (swiperefresh.isRefreshing()) {
                    swiperefresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onContactSelected(Brochure_Anlytics_Result travel) {

        Intent intent = new Intent(ExhibitorAnalytics.this, AnalyticUserListingActivity.class);
        intent.putExtra("brochoure_id", travel.getBrochure_id());
        intent.putExtra("brochoure_name", travel.getBrochure_title());
        startActivity(intent);

    }
}
