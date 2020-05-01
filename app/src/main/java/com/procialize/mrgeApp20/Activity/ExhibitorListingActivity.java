package com.procialize.mrgeApp20.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.procialize.mrgeApp20.Adapter.ExhibitorAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.BuildConfig;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorDataList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorList;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class ExhibitorListingActivity extends AppCompatActivity implements ExhibitorAdapter.ExhibitorAdapterListner {

    RecyclerView exhibitorrecycler;
    SwipeRefreshLayout eventrefresh;
    ExhibitorAdapter eventAdapter;
    String MY_PREFS_NAME = "ProcializeInfo";
    EditText searchEt;
    ImageView img_logout, imageview;
    SessionManager session;
    String platform, device, os_version, app_version, accesstoken, eventid, colorActive;
    ImageView headerlogoIv;
    private APIService mAPIService;
    private ProgressBar progressBar;
    LinearLayout linear;
    ConnectionDetector cd;
    String url, CatName, CatId, eventnamestr;
    HashMap<String, String> user;
    private List<ExhibitorDataList> ExhibitorDataList = new ArrayList<>();
    private List<String> DataList = new ArrayList<>();
    SharedPreferences prefs;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    LinearLayout linearmain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitor);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                HomeActivity.flag = 1;
            }
        });
        exhibitorrecycler = findViewById(R.id.exhibitorrecycler);
        eventrefresh = findViewById(R.id.eventrefresh);
        linearmain = findViewById(R.id.linearmain);

        try {
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linearmain.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linearmain.setBackgroundColor(Color.parseColor("#f1f1f1"));

        }

        CatName = getIntent().getExtras().getString("ExhiName");
        CatId = getIntent().getExtras().getString("ExhiId");


        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        eventnamestr = prefs.getString("eventnamestr", "");
        colorActive = prefs.getString("colorActive", "");
        session = new SessionManager(this);
        user = session.getUserDetails();

        // token
        accesstoken = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Exhibitor List",accesstoken);
        firbaseAnalytics(this, "Exhibitor List", accesstoken);


        cd = new ConnectionDetector(ExhibitorListingActivity.this);

        platform = "android";
        device = Build.MODEL;
        os_version = Build.VERSION.RELEASE;
        app_version = "Version" + BuildConfig.VERSION_NAME;
        eventrefresh = findViewById(R.id.eventrefresh);
        progressBar = findViewById(R.id.progressBar);
        searchEt = findViewById(R.id.searchEt);
        img_logout = findViewById(R.id.img_logout);
        linear = findViewById(R.id.linear);
        imageview = findViewById(R.id.imageview);
        dbHelper = new DBHelper(ExhibitorListingActivity.this);

        procializeDB = new DBHelper(ExhibitorListingActivity.this);
        db = procializeDB.getWritableDatabase();

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        exhibitorrecycler.setLayoutManager(mLayoutManager);


        mAPIService = ApiUtils.getAPIService();


        sendExhiList(eventid, accesstoken);

        eventrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ExhibitorDataList.size() > 0)
                    ExhibitorDataList.clear();

                sendExhiList(eventid, accesstoken);
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
                    eventAdapter.getFilter().filter(s.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }


    public void sendExhiList(String event_id, String token) {
        mAPIService.ExhibitorFetch(event_id, token).enqueue(new Callback<ExhibitorList>() {
            @Override
            public void onResponse(Call<ExhibitorList> call, Response<ExhibitorList> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        Intent main = new Intent(ExhibitorListingActivity.this, LoginActivity.class);
                        startActivity(main);
                        finish();

                    } else {
                        showResponse(response);
                    }
                } else {
                    dismissProgress();
                    Toast.makeText(ExhibitorListingActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<ExhibitorList> call, Throwable t) {
                Toast.makeText(ExhibitorListingActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponse(Response<ExhibitorList> response) {

        if (response.body().getStatus().equals("success")) {
            dbHelper.clearCatagoryTable();
            dbHelper.clearEXCatagoryTable();
            dbHelper.insertExebitorTable(response.body().getExhibitorCatList(), db);
            dbHelper.insertExebitorCatTable(response.body().getExhiCatDetailList(), db);

//            for (int i = 0; i < response.body().getExhibitorAttendeeList().size(); i++) {
//                DataList.add(i, response.body().getExhibitorAttendeeList().get(i).getAttendee_id());
//            }
//            dbHelper.insertMtingList(DataList, db);


            if (CatId.equalsIgnoreCase("0")) {
                eventAdapter = new ExhibitorAdapter(ExhibitorListingActivity.this, response.body().getExhibitorDataList(), this, CatName);
                eventAdapter.notifyDataSetChanged();
                exhibitorrecycler.setAdapter(eventAdapter);
                exhibitorrecycler.scheduleLayoutAnimation();

            } else {
                for (int i = 0; i < response.body().getExhibitorDataList().size(); i++) {
                    if (response.body().getExhibitorDataList().get(i).getExhibitor_category_id().equalsIgnoreCase(CatId)) {
                        ExhibitorDataList tempExhi = new ExhibitorDataList();
                        tempExhi.setName(response.body().getExhibitorDataList().get(i).getName());
                        tempExhi.setExhibitor_category_id(response.body().getExhibitorDataList().get(i).getExhibitor_category_id());
                        tempExhi.setAddress(response.body().getExhibitorDataList().get(i).getAddress());

                        tempExhi.setAllowed_message(response.body().getExhibitorDataList().get(i).getAllowed_message());
                        tempExhi.setAllowed_setup_meeting(response.body().getExhibitorDataList().get(i).getAllowed_setup_meeting());
                        tempExhi.setCreated(response.body().getExhibitorDataList().get(i).getCreated());
                        tempExhi.setEvent_id(response.body().getExhibitorDataList().get(i).getEvent_id());
                        tempExhi.setExhibitor_id(response.body().getExhibitorDataList().get(i).getExhibitor_id());
                        tempExhi.setLogo(response.body().getExhibitorDataList().get(i).getLogo());
                        tempExhi.setModified(response.body().getExhibitorDataList().get(i).getModified());
                        tempExhi.setStall_number(response.body().getExhibitorDataList().get(i).getStall_number());
                        tempExhi.setTile_image(response.body().getExhibitorDataList().get(i).getTile_image());

                        ExhibitorDataList.add(tempExhi);
                    }
                }

                eventAdapter = new ExhibitorAdapter(ExhibitorListingActivity.this, ExhibitorDataList, this, CatName);
                eventAdapter.notifyDataSetChanged();
                exhibitorrecycler.setAdapter(eventAdapter);
                exhibitorrecycler.scheduleLayoutAnimation();
            }


            if (eventrefresh.isRefreshing()) {
                eventrefresh.setRefreshing(false);
            }

        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            if (eventrefresh.isRefreshing()) {
                eventrefresh.setRefreshing(false);
            }
        }
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void dismissProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }


    @Override
    public void onContactSelected(ExhibitorDataList eventList) {
        Intent attendeetail = new Intent(this, ExhibitorDetailActivity.class);
        attendeetail.putExtra("address", eventList.getAddress());
        attendeetail.putExtra("allowedMessage", eventList.getAllowed_message());
        attendeetail.putExtra("allowedsetupMeeting", eventList.getAllowed_setup_meeting());
        attendeetail.putExtra("ExhiCatId", eventList.getExhibitor_category_id());
        attendeetail.putExtra("Exhi_id", eventList.getExhibitor_id());
        attendeetail.putExtra("ExhiLogo", eventList.getLogo());
        attendeetail.putExtra("ExhiName", eventList.getName());
        attendeetail.putExtra("StallNum", eventList.getStall_number());
        attendeetail.putExtra("TileImage", eventList.getTile_image());
        attendeetail.putExtra("CatName", CatName);
        startActivity(attendeetail);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HomeActivity.flag = 1;
    }
}


