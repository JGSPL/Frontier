package com.procialize.frontier.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.GetterSetter.TimeWeather;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeWeatherActivity extends AppCompatActivity {

    TextView txtCity, temp, min, max, txtdate;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    ProgressBar progressBar;
    ImageView headerlogoIv;
    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_weather);

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

        txtCity = findViewById(R.id.txtCity);
        temp = findViewById(R.id.temp);
        max = findViewById(R.id.max);
        min = findViewById(R.id.min);
        txtdate = findViewById(R.id.txtdate);

        SessionManager sessionManager = new SessionManager(TimeWeatherActivity.this);
        mAPIService = ApiUtils.getAPIService();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");


        getInfoTab();

    }

    public void getInfoTab() {
        showProgress();

        mAPIService.FetchTimeWeather(eventid).enqueue(new Callback<TimeWeather>() {
            @Override
            public void onResponse(Call<TimeWeather> call, Response<TimeWeather> response) {

                if (response.body().getStatus().equals("success")) {
                    dismissProgress();
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    String date_time = response.body().getDate_time();
                    String mintemp = response.body().getMin();
                    String maxtemp = response.body().getMax();
                    String cityname = response.body().getCityname();
                    String currenttemp = response.body().getCurrent_temp();

                    txtCity.setText(cityname);
                    temp.setText(currenttemp + "°");
                    min.setText(mintemp);
                    max.setText(maxtemp);
                    txtdate.setText(date_time);


                } else {
                    dismissProgress();
                    Toast.makeText(TimeWeatherActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TimeWeather> call, Throwable t) {
                dismissProgress();
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(TimeWeatherActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
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
