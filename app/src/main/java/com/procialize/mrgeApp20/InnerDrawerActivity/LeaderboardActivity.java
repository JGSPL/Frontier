package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.mrgeApp20.Adapter.LeaderBoardAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.GetterSetter.LeaderBoardListFetch;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import java.io.File;
import java.util.HashMap;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class LeaderboardActivity extends AppCompatActivity {


    private APIService mAPIService;
    SwipeRefreshLayout leadRvrefresh;
    RecyclerView LeaderRv;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    ConnectionDetector cd;
    RelativeLayout relative;
    TextView txt_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

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
        LeaderRv = findViewById(R.id.LeaderRv);
        leadRvrefresh = findViewById(R.id.leadRvrefresh);
        progressBar = findViewById(R.id.progressBar);
        relative = findViewById(R.id.relative);
        txt_title = findViewById(R.id.txt_title);
        txt_title.setTextColor(Color.parseColor(colorActive));
        cd = new ConnectionDetector(LeaderboardActivity.this);

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            relative.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            relative.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Leaderboard",token);
        firbaseAnalytics(this, "Leaderboard", token);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        LeaderRv.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        //  LeaderRv.setLayoutAnimation(animation);


        if (cd.isConnectingToInternet()) {
            fetchLeaderboard(eventid, token);
        } else {
            Toast.makeText(LeaderboardActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        leadRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {
                    fetchLeaderboard(eventid, token);
                } else {
                    Toast.makeText(LeaderboardActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void fetchLeaderboard(String eventid, String token) {
        showProgress();
        mAPIService.LeaderBoardListFetch(eventid, token).enqueue(new Callback<LeaderBoardListFetch>() {
            @Override
            public void onResponse(Call<LeaderBoardListFetch> call, Response<LeaderBoardListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    dismissProgress();
                    if (leadRvrefresh.isRefreshing()) {
                        leadRvrefresh.setRefreshing(false);
                    }
                    showResponse(response);
                } else {

                    if (leadRvrefresh.isRefreshing()) {
                        leadRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LeaderBoardListFetch> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (leadRvrefresh.isRefreshing()) {
                    leadRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<LeaderBoardListFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if (!(response.body().getLeaderboardList().isEmpty())) {
                LeaderBoardAdapter docAdapter = new LeaderBoardAdapter(LeaderboardActivity.this, response.body().getLeaderboardList());
                docAdapter.notifyDataSetChanged();
                LeaderRv.setAdapter(docAdapter);
                LeaderRv.setVisibility(View.VISIBLE);
                leadRvrefresh.setVisibility(View.VISIBLE);

            } else {

                LeaderRv.setVisibility(View.GONE);
                leadRvrefresh.setVisibility(View.VISIBLE);
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
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }


}

