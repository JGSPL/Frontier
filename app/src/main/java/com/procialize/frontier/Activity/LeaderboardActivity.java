package com.procialize.frontier.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.frontier.Adapter.JigsawLeaderboardAdapter;
import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.DbHelper.ConnectionDetector;
import com.procialize.frontier.GetterSetter.Jigsaw_puzzle_leader;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.Util;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.frontier.util.CommonFunction.crashlytics;
import static com.procialize.frontier.util.CommonFunction.firbaseAnalytics;

public class LeaderboardActivity extends AppCompatActivity  {

    ImageView headerlogoIv;
    String MY_PREFS_NAME = "ProcializeInfo";
    APIService mAPIService;
    SessionManager sessionManager;
    String eventid,puzzlename;
    ConnectionDetector cd;
    RecyclerView jigsawlLeaderboard;
    ProgressBar progressBar;
    TextView msg;
    Button btn_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.btn_back), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent=getIntent();
        puzzlename=intent.getStringExtra("image_name");

        headerlogoIv = findViewById(R.id.headerlogoIv);
        jigsawlLeaderboard = findViewById(R.id.jigsawlLeaderboard);
        progressBar = findViewById(R.id.progress);
        msg = findViewById(R.id.msg);
        btn_play = findViewById(R.id.btn_play);
        Util.logomethod(this, headerlogoIv);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");

        cd = new ConnectionDetector(this);
        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Leaderboard", token);
        firbaseAnalytics(this, "Leaderboard", token);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        jigsawlLeaderboard.setLayoutManager(mLayoutManager);

        fetchFeedback(token,eventid,puzzlename);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(puzzlename.equalsIgnoreCase("puzzle_1")){
                    Intent videocontest = new Intent(LeaderboardActivity.this, PuzzlenewActivity.class);
                    videocontest.putExtra("image_name", "puzzle_1");
                    startActivity(videocontest);
                }else if(puzzlename.equalsIgnoreCase("puzzle_2")){
                    Intent videocontest = new Intent(LeaderboardActivity.this, PuzzletwoActivity.class);
                    videocontest.putExtra("image_name", "puzzle_2");
                    startActivity(videocontest);
                }
            }
        });

    }

    public void fetchFeedback(String token, String eventid,String puzzlename) {
        showProgress();
        mAPIService.Jigsawpuzzleleader(token, eventid,puzzlename).enqueue(new Callback<Jigsaw_puzzle_leader>() {
            @Override
            public void onResponse(Call<Jigsaw_puzzle_leader> call, Response<Jigsaw_puzzle_leader> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    dismissProgress();
                    showResponse(response);
                } else {


                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Jigsaw_puzzle_leader> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();


            }
        });
    }

    public void showResponse(Response<Jigsaw_puzzle_leader> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if (!(response.body().getJgsawPuzzle().isEmpty())) {
                jigsawlLeaderboard.setVisibility(View.VISIBLE);
                msg.setVisibility(View.GONE);
                JigsawLeaderboardAdapter frontiertvadapter = new JigsawLeaderboardAdapter(this, response.body().getJgsawPuzzle(), response.body().getProfile_pic_url_path());
                frontiertvadapter.notifyDataSetChanged();
                jigsawlLeaderboard.setAdapter(frontiertvadapter);
//                feedbackRv.scheduleLayoutAnimation();
            } else {
                jigsawlLeaderboard.setVisibility(View.GONE);
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


}