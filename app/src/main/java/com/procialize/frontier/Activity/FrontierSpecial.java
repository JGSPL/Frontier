package com.procialize.frontier.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.procialize.frontier.Adapter.FrontierTVAdapter;
import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.GetterSetter.FrontierTV;
import com.procialize.frontier.GetterSetter.FrontierTVFetch;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.Util;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.frontier.util.CommonFunction.crashlytics;
import static com.procialize.frontier.util.CommonFunction.firbaseAnalytics;

public class FrontierSpecial extends AppCompatActivity implements FrontierTVAdapter.FrontierTVAdapterListner {

    SwipeRefreshLayout frontiarspecialrefreash;
    RecyclerView frontierrecycler;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    private APIService mAPIService;
    RelativeLayout linear;
    TextView msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontier_special);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
        Util.logomethod(this, headerlogoIv);

        TextView header = findViewById(R.id.title);
        frontierrecycler = findViewById(R.id.frontierrecycler);
        frontiarspecialrefreash = findViewById(R.id.frontiarspecialrefreash);
        progressBar = findViewById(R.id.progressBar);
        msg = findViewById(R.id.msg);

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("FrontierSpecial", token);
        firbaseAnalytics(this, "FrontierSpecial", token);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        frontierrecycler.setLayoutManager(mLayoutManager);

        fetchFeedback(token, eventid);

        frontiarspecialrefreash.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchFeedback(token, eventid);
            }
        });
    }

    public void fetchFeedback(String token, String eventid) {
        showProgress();
        mAPIService.FrontierTVFetch(token, eventid).enqueue(new Callback<FrontierTVFetch>() {
            @Override
            public void onResponse(Call<FrontierTVFetch> call, Response<FrontierTVFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (frontiarspecialrefreash.isRefreshing()) {
                        frontiarspecialrefreash.setRefreshing(false);
                    }
                    dismissProgress();
                    showResponse(response);
                } else {

                    if (frontiarspecialrefreash.isRefreshing()) {
                        frontiarspecialrefreash.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FrontierTVFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                if (frontiarspecialrefreash.isRefreshing()) {
                    frontiarspecialrefreash.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<FrontierTVFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if (!(response.body().getFrontier_tv().isEmpty())) {
                frontierrecycler.setVisibility(View.VISIBLE);
                msg.setVisibility(View.GONE);
                FrontierTVAdapter frontiertvadapter = new FrontierTVAdapter(this, response.body().getFrontier_tv(), response.body().getImage_path(), this);
                frontiertvadapter.notifyDataSetChanged();
                frontierrecycler.setAdapter(frontiertvadapter);
//                feedbackRv.scheduleLayoutAnimation();
            } else {
                frontierrecycler.setVisibility(View.GONE);
                msg.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

        }
    }

    public void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void dismissProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        //   overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }


    @Override
    public void onContactSelected(FrontierTV frontiertv, String path) {

        Intent pdfview = new Intent(this, FrontierTVDetail.class);
        pdfview.putExtra("url", frontiertv.getUrl());
        pdfview.putExtra("title", frontiertv.getTitle());
        pdfview.putExtra("desc", frontiertv.getDescription());
        pdfview.putExtra("thumbimg", path + frontiertv.getThumb_image());
        pdfview.putExtra("id",frontiertv.getId());
        pdfview.putExtra("registration_enable", frontiertv.getRegistration_enable());
        startActivity(pdfview);
        finish();

    }
}