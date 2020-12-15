package com.procialize.frontier.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.frontier.Adapter.GameAdapter;
import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.DbHelper.ConnectionDetector;
import com.procialize.frontier.GetterSetter.GamesList;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.Util;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameActivity extends AppCompatActivity {

    ImageView headerlogoIv;
    RecyclerView game_recycler;
    String eventid, apikey;
    SessionManager sessionManager;
    private APIService mAPIService;
    ProgressBar progressBar;
    ConnectionDetector cd;
    String MY_PREFS_NAME = "ProcializeInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.btn_back), PorterDuff.Mode.SRC_ATOP);

        cd = new ConnectionDetector(this);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        progressBar = findViewById(R.id.progress);
        Util.logomethod(this, headerlogoIv);

        game_recycler = findViewById(R.id.game_recycler);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        sessionManager = new SessionManager(GameActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);
        mAPIService = ApiUtils.getAPIService();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(GameActivity.this);
        game_recycler.setLayoutManager(mLayoutManager);

        if (cd.isConnectingToInternet()) {
            fetchGame(apikey, eventid);
        } else {
            Toast.makeText(GameActivity.this, "No Interner Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchGame(String token, String eventid) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.GameFetch(token, eventid).enqueue(new Callback<GamesList>() {
            @Override
            public void onResponse(Call<GamesList> call, Response<GamesList> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressBar.setVisibility(View.GONE);

                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        sessionManager.logoutUser();
                        Intent main = new Intent(GameActivity.this, LoginActivity.class);
                        startActivity(main);
                        finish();
                    } else {
                        showResponse(response);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<GamesList> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                //  Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void showResponse(Response<GamesList> response) {

        GameAdapter adapter = new GameAdapter(GameActivity.this, response.body().getGame_list());
        adapter.notifyDataSetChanged();
        game_recycler.setAdapter(adapter);
    }

}