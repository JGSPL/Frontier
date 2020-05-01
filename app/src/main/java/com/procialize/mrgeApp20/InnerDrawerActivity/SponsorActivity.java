package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.GetterSetter.SponsorsFetch;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import java.io.File;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class SponsorActivity extends AppCompatActivity {
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    ProgressBar progressBar;
    WebView mywebview;
    private APIService mAPIService;
    SwipeRefreshLayout webrefresher;
    ConnectionDetector cd;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor);

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


        cd = new ConnectionDetector(this);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Sponsor",token);
        firbaseAnalytics(this,"Sponsor",token);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        mywebview = findViewById(R.id.webView);
        webrefresher = findViewById(R.id.webrefresher);
        linear = findViewById(R.id.linear);
        mywebview.setBackgroundColor(Color.TRANSPARENT);

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


        WebSettings settings = mywebview.getSettings();
        settings.setJavaScriptEnabled(true);
//        settings.setUseWideViewPort(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        if (cd.isConnectingToInternet()) {
            fetchContact(eventid, token);
        } else {
            Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
        }


        webrefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (cd.isConnectingToInternet()) {
                    fetchContact(eventid, token);
                } else {
                    if (webrefresher.isRefreshing()) {
                        webrefresher.setRefreshing(false);
                    }
                    Toast.makeText(SponsorActivity.this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void fetchContact(String eventid, String token) {
        showProgress();
        mAPIService.SponsorsFetch(eventid, token).enqueue(new Callback<SponsorsFetch>() {
            @Override
            public void onResponse(Call<SponsorsFetch> call, Response<SponsorsFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    dismissProgress();
                    if (webrefresher.isRefreshing()) {
                        webrefresher.setRefreshing(false);
                    }
                    showResponse(response);
                } else {

                    if (webrefresher.isRefreshing()) {
                        webrefresher.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SponsorsFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                if (webrefresher.isRefreshing()) {
                    webrefresher.setRefreshing(false);
                }
                dismissProgress();

            }
        });
    }

    public void showResponse(Response<SponsorsFetch> response) {

        // specify an adapter (see also next example)

        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if (!(response.body().getSponsors_url().isEmpty())) {
                try {
                    String sponsor = response.body().getSponsors_url();
                    mywebview.clearCache(true);
                    mywebview.loadUrl(sponsor);
                    mywebview.setWebViewClient(new CustomWebViewClient());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        } else {
            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    private class CustomWebViewClient extends WebViewClient {

        public void onPageFinished(WebView view, String url) {

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
                view.clearCache(true);
                view.reload();
                return true;
            } else if (url.startsWith("mailto:")) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                startActivity(intent);
                view.clearCache(true);
                view.reload();
                return true;
            }

            view.clearCache(true);
            view.loadUrl(url);
            return true;
        }
    }

    public void showProgress() {
       /* if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }*/
    }

    public void dismissProgress() {
       /* if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }*/
    }

    @Override
    protected void onResume() {
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
