package com.procialize.frontier.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.procialize.frontier.Activity.AgendaDetailActivity;
import com.procialize.frontier.Adapter.AgendaAdapter;
import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.DbHelper.ConnectionDetector;
import com.procialize.frontier.DbHelper.DBHelper;
import com.procialize.frontier.GetterSetter.AgendaList;
import com.procialize.frontier.GetterSetter.Analytic;
import com.procialize.frontier.GetterSetter.FetchAgenda;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.Util;

import java.util.HashMap;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.frontier.util.CommonFunction.crashlytics;
import static com.procialize.frontier.util.CommonFunction.firbaseAnalytics;

public class AgendaActivity extends AppCompatActivity implements AgendaAdapter.AgendaAdapterListner {

    RecyclerView agendarecycler;
    SwipeRefreshLayout agendafeedrefresh;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    ImageView headerlogoIv;
    private APIService mAPIService;
    ConnectionDetector cd;
    private ProgressBar progressBar;
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");


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
        agendafeedrefresh = findViewById(R.id.agendafeedrefresh);
        agendarecycler = findViewById(R.id.agendarecycler);
        progressBar = findViewById(R.id.progressBar);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        agendarecycler.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        agendarecycler.setLayoutAnimation(animation);


        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);

        crashlytics("Agenda",token);
        firbaseAnalytics(this, "Agenda",token);

        cd = new ConnectionDetector(AgendaActivity.this);

        try {
            dbHelper = new DBHelper(AgendaActivity.this);
            procializeDB = new DBHelper(AgendaActivity.this);
            db = procializeDB.getWritableDatabase();

        } catch (Exception e) {
            e.printStackTrace();
        }

        fetchAgenda(token, eventid);

        SubmitAnalytics(token, eventid, "", "", "agenda");

        agendafeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchAgenda(token, eventid);
            }
        });
    }


    public void fetchAgenda(String token, String eventid) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.AgendaFetchPost(token, eventid).enqueue(new Callback<FetchAgenda>() {
            @Override
            public void onResponse(Call<FetchAgenda> call, Response<FetchAgenda> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressBar.setVisibility(View.GONE);

                    if (agendafeedrefresh.isRefreshing()) {
                        agendafeedrefresh.setRefreshing(false);
                    }
                    showResponse(response);
                } else {
                    progressBar.setVisibility(View.GONE);
                    if (agendafeedrefresh.isRefreshing()) {
                        agendafeedrefresh.setRefreshing(false);
                    }
                    Toast.makeText(AgendaActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchAgenda> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                progressBar.setVisibility(View.GONE);
                if (agendafeedrefresh.isRefreshing()) {
                    agendafeedrefresh.setRefreshing(false);
                }
                Toast.makeText(AgendaActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showResponse(Response<FetchAgenda> response) {

        try {
            AgendaAdapter agendaAdapter = new AgendaAdapter(AgendaActivity.this, response.body().getAgendaList(), this);
            agendaAdapter.notifyDataSetChanged();
            agendarecycler.setAdapter(agendaAdapter);
            agendarecycler.scheduleLayoutAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onContactSelected(AgendaList agenda) {
        Intent agendadetail = new Intent(this, AgendaDetailActivity.class);

        agendadetail.putExtra("id", agenda.getSessionId());
        agendadetail.putExtra("date", agenda.getSessionDate());
        agendadetail.putExtra("name", agenda.getSessionName());
        agendadetail.putExtra("description", agenda.getSessionDescription());
        agendadetail.putExtra("starttime", agenda.getSessionStartTime());
        agendadetail.putExtra("endtime", agenda.getSessionEndTime());

        startActivity(agendadetail);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }

    public void SubmitAnalytics(String token, String eventid, String target_attendee_id, String target_attendee_type, String analytic_type) {

        mAPIService.Analytic(token, eventid, target_attendee_id, target_attendee_type, analytic_type).enqueue(new Callback<Analytic>() {
            @Override
            public void onResponse(Call<Analytic> call, Response<Analytic> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());


                } else {

//                    Toast.makeText(AgendaActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
//                Toast.makeText(AgendaActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }


}
