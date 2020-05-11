package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.procialize.mrgeApp20.Activity.SpeakerDetailsActivity;
import com.procialize.mrgeApp20.Adapter.SpeakerAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.Analytic;
import com.procialize.mrgeApp20.GetterSetter.FetchSpeaker;
import com.procialize.mrgeApp20.GetterSetter.SpeakerList;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class SpeakerActivity extends AppCompatActivity implements SpeakerAdapter.SpeakerAdapterListner {


    SwipeRefreshLayout speakerfeedrefresh;
    RecyclerView speakerrecycler;
    EditText searchEt;
    SpeakerAdapter speakerAdapter;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    ImageView headerlogoIv;
    private APIService mAPIService;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ProgressBar progressBar;
    private ConnectionDetector cd;
    private DBHelper dbHelper;
    private List<SpeakerList> speakersDBList;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);

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

        speakerrecycler = findViewById(R.id.speakerrecycler);

        speakerfeedrefresh = findViewById(R.id.speakerfeedrefresh);

        searchEt = findViewById(R.id.searchEt);

        progressBar = findViewById(R.id.progressBar);
        linear = findViewById(R.id.linear);

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
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

//        speakerrecycler.setHasFixedSize(true);

        cd = new ConnectionDetector(SpeakerActivity.this);
        dbHelper = new DBHelper(SpeakerActivity.this);

        procializeDB = new DBHelper(SpeakerActivity.this);
        db = procializeDB.getWritableDatabase();

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Speaker",token);
        firbaseAnalytics(this,"Speaker",token);
        SubmitAnalytics(token, eventid, "", "", "speaker");
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        speakerrecycler.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        // speakerrecycler.setLayoutAnimation(animation);


        if (cd.isConnectingToInternet()) {
            fetchSpeaker(token, eventid);
        } else {
            db = procializeDB.getReadableDatabase();

            speakersDBList = dbHelper.getSpeakerDetails();

            speakerAdapter = new SpeakerAdapter(SpeakerActivity.this, speakersDBList, this);
            speakerAdapter.notifyDataSetChanged();
            speakerrecycler.setAdapter(speakerAdapter);
            speakerrecycler.scheduleLayoutAnimation();
        }

        speakerfeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {
                    fetchSpeaker(token, eventid);
                } else {
                    db = procializeDB.getReadableDatabase();

                    speakersDBList = dbHelper.getSpeakerDetails();

                    speakerAdapter = new SpeakerAdapter(SpeakerActivity.this, speakersDBList, SpeakerActivity.this);
                    speakerAdapter.notifyDataSetChanged();
                    speakerrecycler.setAdapter(speakerAdapter);
                    speakerrecycler.scheduleLayoutAnimation();


                    if (speakerfeedrefresh.isRefreshing()) {
                        speakerfeedrefresh.setRefreshing(true);
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
                try {
                    speakerAdapter.getFilter().filter(s.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void fetchSpeaker(String token, String eventid) {

        showProgress();
        mAPIService.SpeakerFetchPost(token, eventid).enqueue(new Callback<FetchSpeaker>() {
            @Override
            public void onResponse(Call<FetchSpeaker> call, Response<FetchSpeaker> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    hideProgress();
                    if (speakerfeedrefresh.isRefreshing()) {
                        speakerfeedrefresh.setRefreshing(false);
                    }
                    showResponse(response);
                } else {

                    hideProgress();
                    if (speakerfeedrefresh.isRefreshing()) {
                        speakerfeedrefresh.setRefreshing(false);
                    }
                    Toast.makeText(SpeakerActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchSpeaker> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(SpeakerActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

                hideProgress();
                if (speakerfeedrefresh.isRefreshing()) {
                    speakerfeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<FetchSpeaker> response) {

        // specify an adapter (see also next example)
        if (!(response.body().getSpeakerList().isEmpty())) {

            dbHelper.clearSpeakersTable();
            dbHelper.insertSpeakersInfo(response.body().getSpeakerList(), db);



            speakerAdapter = new SpeakerAdapter(SpeakerActivity.this, response.body().getSpeakerList(), this);
            speakerAdapter.notifyDataSetChanged();
            speakerrecycler.setAdapter(speakerAdapter);
            speakerrecycler.scheduleLayoutAnimation();
        } else {
            setContentView(R.layout.activity_empty_view);
            ImageView imageView = findViewById(R.id.back);
            TextView text_empty = findViewById(R.id.text_empty);
            text_empty.setText("Speakers not available");
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    private void hideProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onContactSelected(SpeakerList speaker) {
        Intent speakeretail = new Intent(this, SpeakerDetailsActivity.class);
        speakeretail.putExtra("id", speaker.getAttendeeId());
        speakeretail.putExtra("name", speaker.getFirstName() + " " + speaker.getLastName());
        speakeretail.putExtra("city", speaker.getCity());
        speakeretail.putExtra("country", speaker.getCountry());
        speakeretail.putExtra("company", speaker.getCompany());
        speakeretail.putExtra("designation", speaker.getDesignation());
        speakeretail.putExtra("description", speaker.getDescription());
        speakeretail.putExtra("totalrate", speaker.getTotalRating());
        speakeretail.putExtra("profile", speaker.getProfilePic());
        speakeretail.putExtra("mobile", speaker.getMobileNumber());
        startActivity(speakeretail);
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

//                    Toast.makeText(SpeakerActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
//                Toast.makeText(SpeakerActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
