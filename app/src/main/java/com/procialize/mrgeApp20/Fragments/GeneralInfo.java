package com.procialize.mrgeApp20.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.procialize.mrgeApp20.Activity.CurrencyConverter;
import com.procialize.mrgeApp20.Activity.InitGeneralInfoActivity;
import com.procialize.mrgeApp20.Activity.LoginActivity;
import com.procialize.mrgeApp20.Adapter.GeneralInfoListAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.GetterSetter.Analytic;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.GetterSetter.GeneralInfoList;
import com.procialize.mrgeApp20.GetterSetter.InfoList;
import com.procialize.mrgeApp20.InnerDrawerActivity.WeatherActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class GeneralInfo extends Fragment implements GeneralInfoListAdapter.GeneralInfoListener {

    List<EventSettingList> eventSettingLists;
    TextView weather_tv, abtcurency_tv, about_hotel, pullrefresh;
    String eventid;
    List<InfoList> generalinfoLists;
    LinearLayout linearlayout, general_info_cur, general_info_wea;
    SwipeRefreshLayout generalInforefresh;
    LinearLayout.LayoutParams params;
    TextView textView, header;
    ProgressBar progressBar;
    RecyclerView general_item_list;
    GeneralInfoListAdapter generalInfoListAdapter;
    List views = new ArrayList();
    View viewlayout;
    LinearLayout insertPoint;
    String MY_PREFS_NAME = "ProcializeInfo";
    String colorActive;
    SessionManager sessionManager;
    private APIService mAPIService;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.general_info, container, false);
        SessionManager sessionManager = new SessionManager(getActivity());
        eventSettingLists = SessionManager.loadEventList();
        SharedPreferences prefs1 = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs1.getString("colorActive", "");

        sessionManager = new SessionManager(getContext());

        weather_tv = view.findViewById(R.id.weather_tv);
        abtcurency_tv = view.findViewById(R.id.abtcurency_tv);
        header = view.findViewById(R.id.header);
        pullrefresh = view.findViewById(R.id.pullrefresh);

//        about_hotel = (TextView) view.findViewById(R.id.about_hotel);
        linearlayout = view.findViewById(R.id.linearlayout);

        weather_tv.setTextColor(Color.parseColor(colorActive));
        abtcurency_tv.setTextColor(Color.parseColor(colorActive));
        header.setTextColor(Color.parseColor(colorActive));
        pullrefresh.setTextColor(Color.parseColor(colorActive));


        general_info_wea = view.findViewById(R.id.general_info_wea);
        general_info_cur = view.findViewById(R.id.general_info_cur);

        ImageView ic_rightarrow = view.findViewById(R.id.ic_rightarrow);
        ImageView ic_rightarrow1 = view.findViewById(R.id.ic_rightarrow1);
//        int colorInt = Color.parseColor(colorActive);
//
//        ColorStateList csl = ColorStateList.valueOf(colorInt);
//        Drawable drawable = DrawableCompat.wrap(ic_rightarrow.getDrawable());
//        DrawableCompat.setTintList(drawable, csl);
//        ic_rightarrow.setImageDrawable(drawable);


//        Drawable drawable1 = DrawableCompat.wrap(ic_rightarrow1.getDrawable());
//        DrawableCompat.setTintList(drawable1, csl);
//        ic_rightarrow1.setImageDrawable(drawable1);


        ic_rightarrow.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);
        ic_rightarrow1.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);

        pullrefresh = view.findViewById(R.id.pullrefresh);
        generalInforefresh = view.findViewById(R.id.generalInforefresh);

        general_item_list = view.findViewById(R.id.general_item_list);
        progressBar = view.findViewById(R.id.progressBar);


        HashMap<String, String> user = sessionManager.getUserDetails();


        // token
        final String token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("General Info", token);
        firbaseAnalytics(getActivity(), "General Info", token);;
        if (generalInforefresh.isRefreshing()) {
            generalInforefresh.setRefreshing(false);
        }

        try {

            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            generalInforefresh.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            generalInforefresh.setBackgroundColor(Color.parseColor("#f1f1f1"));

        }

        mAPIService = ApiUtils.getAPIService();
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
//        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        // Create TextView programmatically.
//        textView = new TextView(getActivity());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        general_item_list.setLayoutManager(mLayoutManager);
        getInfoTab();

//        generalInfoAdapter.scheduleLayoutAnimation();
        for (int i = 0; i < eventSettingLists.size(); i++) {
            String eventName = eventSettingLists.get(i).getFieldName();
            String eventValue = eventSettingLists.get(i).getFieldValue();

            if (eventName.equalsIgnoreCase("gen_info_weather") && eventValue.equalsIgnoreCase("1")) {
                weather_tv.setVisibility(View.VISIBLE);
                general_info_wea.setVisibility(View.VISIBLE);

            }

            if (eventName.equalsIgnoreCase("gen_info_currency_converter") && eventValue.equalsIgnoreCase("1")) {
                abtcurency_tv.setVisibility(View.VISIBLE);
                general_info_cur.setVisibility(View.VISIBLE);

            }
        }

        SubmitAnalytics(token, eventid, "", "", "generalInfo");
        weather_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                startActivity(intent);
            }
        });

        abtcurency_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CurrencyConverter.class);
                startActivity(intent);
            }
        });


        generalInforefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getInfoTab();

            }
        });


        return view;
    }

    public void getInfoTab() {
        showProgress();

        mAPIService.FetchGeneralInfo(eventid).enqueue(new Callback<GeneralInfoList>() {
            @Override
            public void onResponse(Call<GeneralInfoList> call, Response<GeneralInfoList> response) {

                if (response.body().getStatus().equals("success")) {
                    dismissProgress();
                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        sessionManager.logoutUser();
                        Intent main = new Intent(getContext(), LoginActivity.class);
                        startActivity(main);
                        getActivity().finish();

                    } else {

                        Log.i("hit", "post submitted to API." + response.body().toString());
                        generalinfoLists = response.body().getInfoList();


                        generalInfoListAdapter = new GeneralInfoListAdapter(getActivity(), generalinfoLists, GeneralInfo.this);
                        generalInfoListAdapter.notifyDataSetChanged();
                        general_item_list.setAdapter(generalInfoListAdapter);
                    }


                    if (generalInforefresh.isRefreshing()) {
                        generalInforefresh.setRefreshing(false);
                    }


                } else {
                    dismissProgress();
                    //  Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralInfoList> call, Throwable t) {
                dismissProgress();
                Log.e("hit", "Unable to submit post to API.");
                //  Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                if (generalInforefresh.isRefreshing()) {
                    generalInforefresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }

    @Override
    public void onContactSelected(InfoList firstLevelFilter) {
        Intent intent = new Intent(getActivity(), InitGeneralInfoActivity.class);
        intent.putExtra("name", firstLevelFilter.getName());
        intent.putExtra("description", firstLevelFilter.getDescription());
        startActivity(intent);
    }

    public void SubmitAnalytics(String token, String eventid, String target_attendee_id, String target_attendee_type, String analytic_type) {

        mAPIService.Analytic(token, eventid, target_attendee_id, target_attendee_type, analytic_type).enqueue(new Callback<Analytic>() {
            @Override
            public void onResponse(Call<Analytic> call, Response<Analytic> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());


                } else {

                    //Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
                //  Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();

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
