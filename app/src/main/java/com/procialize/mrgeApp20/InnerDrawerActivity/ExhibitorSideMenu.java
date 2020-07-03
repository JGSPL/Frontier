package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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

import com.procialize.mrgeApp20.Activity.ExhibitorListingActivity;
import com.procialize.mrgeApp20.Activity.LoginActivity;
import com.procialize.mrgeApp20.Exhibitor.Adapter.ExhibitorListingAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.BuildConfig;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorCatList;
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

public class ExhibitorSideMenu extends AppCompatActivity implements ExhibitorListingAdapter.ExhibitorAdapterListner {

    ImageView headerlogoIv;
    LinearLayout linearmain;
    String MY_PREFS_CATEGORY = "categorycnt";
    String catcnt;
    RecyclerView exhibitorrecycler;
    SwipeRefreshLayout eventrefresh;
    ExhibitorListingAdapter eventAdapter;
    String MY_PREFS_NAME = "ProcializeInfo";
    EditText searchEt;
    ImageView img_logout, imageview;
    SessionManager session;
    String platform, device, os_version, app_version, accesstoken, eventid,colorActive;
    private APIService mAPIService;
    private ProgressBar progressBar;
    LinearLayout linear;
    LinearLayout linear1;
    ConnectionDetector cd;
    String url;
    SharedPreferences prefs;
    HashMap<String, String> user;
    private List<ExhibitorCatList> ExhibitorCatList = new ArrayList<>();
    ExhibitorCatList firstExhi = new ExhibitorCatList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitor_side_menu);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        linearmain = findViewById(R.id.linearmain);
        Util.logomethod(this, headerlogoIv);

        SharedPreferences prefs8 = getSharedPreferences(MY_PREFS_CATEGORY, MODE_PRIVATE);
        catcnt = prefs8.getString("categorycnt", "");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        try {

            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linearmain.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linearmain.setBackgroundColor(Color.parseColor("#f1f1f1"));

        }

        exhibitorrecycler = findViewById(R.id.exhibitorrecycler);
        eventrefresh = findViewById(R.id.eventrefresh);
        linear1 = findViewById(R.id.linear1);


        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        session = new SessionManager(ExhibitorSideMenu.this);
        user = session.getUserDetails();

        // token
        accesstoken = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Exhibitor Side Menu",accesstoken);
        firbaseAnalytics(this,"Exhibitor Side Menu",accesstoken);
        cd = new ConnectionDetector(ExhibitorSideMenu.this);

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
//        searchEt.setFocusable(false);

        int columns = 2;
        exhibitorrecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), columns));





        mAPIService = ApiUtils.getAPIService();


        sendExhiList(eventid, accesstoken);

        eventrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ExhibitorCatList.size() > 0)
                    ExhibitorCatList.clear();

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
                        Intent main = new Intent(ExhibitorSideMenu.this, LoginActivity.class);
                        startActivity(main);

                    } else {
                        showResponse(response);
                    }
                } else {
                    dismissProgress();
                    Toast.makeText(ExhibitorSideMenu.this, response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<ExhibitorList> call, Throwable t) {
                Toast.makeText(ExhibitorSideMenu.this, "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponse(Response<ExhibitorList> response) {

        if (response.body().getStatus().equals("success")) {

            if (ExhibitorCatList.size() > 0)
                ExhibitorCatList.clear();

            firstExhi.setName("All Category");
            firstExhi.setExhibitor_category_id("0");
            firstExhi.setTotal_exhibitor_count(String.valueOf(response.body().getExhibitorDataList().size()));
            ExhibitorCatList.add(firstExhi);
            // ExhibitorCatList =response.body().getExhibitorCatList();
            for (int i = 0; i < response.body().getExhibitorCatList().size(); i++) {
                ExhibitorCatList tempExhi = new ExhibitorCatList();
                tempExhi.setName(response.body().getExhibitorCatList().get(i).getName());
                tempExhi.setExhibitor_category_id(response.body().getExhibitorCatList().get(i).getExhibitor_category_id());
                tempExhi.setTotal_exhibitor_count(response.body().getExhibitorCatList().get(i).getTotal_exhibitor_count());
                ExhibitorCatList.add(tempExhi);
            }


            eventAdapter = new ExhibitorListingAdapter(ExhibitorSideMenu.this, ExhibitorCatList, response.body().getExhibitorDataList(), this);
            eventAdapter.notifyDataSetChanged();
            exhibitorrecycler.setAdapter(eventAdapter);
            exhibitorrecycler.scheduleLayoutAnimation();


            if (eventrefresh.isRefreshing()) {
                eventrefresh.setRefreshing(false);
            }

        } else {
            Toast.makeText(ExhibitorSideMenu.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onContactSelected(ExhibitorCatList eventList) {
        if (ExhibitorCatList.size() > 0) {

            Intent quizOptionIntent = new Intent(ExhibitorSideMenu.this, ExhibitorListingActivity.class);
            quizOptionIntent.putExtra("ExhiName", eventList.getName());
            quizOptionIntent.putExtra("ExhiId", eventList.getExhibitor_category_id());
            startActivity(quizOptionIntent);
            finish();
        } else {
            Toast.makeText(ExhibitorSideMenu.this, "Exhibitor not available", Toast.LENGTH_SHORT).show();

        }
    }
}
