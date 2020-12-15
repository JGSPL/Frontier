package com.procialize.frontier.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.frontier.Activity.ExhibitorListingActivity;
import com.procialize.frontier.Activity.LoginActivity;
import com.procialize.frontier.Exhibitor.Adapter.ExhibitorListingAdapter;
import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.BuildConfig;
import com.procialize.frontier.DbHelper.ConnectionDetector;
import com.procialize.frontier.GetterSetter.ExhibitorCatList;
import com.procialize.frontier.GetterSetter.ExhibitorList;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.frontier.util.CommonFunction.crashlytics;
import static com.procialize.frontier.util.CommonFunction.firbaseAnalytics;

public class ExhiCatFragment extends Fragment implements ExhibitorListingAdapter.ExhibitorAdapterListner {
    RecyclerView exhibitorrecycler;
    SwipeRefreshLayout eventrefresh;
    ExhibitorListingAdapter eventAdapter;
    String MY_PREFS_NAME = "ProcializeInfo";
    TextView pullrefresh;
    EditText searchEt;
    ImageView img_logout, imageview;
    SessionManager session;
    String platform, device, os_version, app_version, accesstoken, eventid;

    private APIService mAPIService;
    private ProgressBar progressBar;
    private String  colorActive = "";
    LinearLayout linear;
    LinearLayout linear1;
    ConnectionDetector cd;
    SharedPreferences prefs;
    HashMap<String, String> user;
    private List<ExhibitorCatList> ExhibitorCatList = new ArrayList<>();
    ExhibitorCatList firstExhi = new ExhibitorCatList();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_exhibitor_listing, container, false);

        exhibitorrecycler = view.findViewById(R.id.exhibitorrecycler);
        eventrefresh = view.findViewById(R.id.eventrefresh);
        linear1 = view.findViewById(R.id.linear1);
        pullrefresh = view.findViewById(R.id.pullrefresh);

        LinearLayout linHeader = view.findViewById(R.id.linHeader);
        linHeader.setVisibility(View.GONE);

        prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        session = new SessionManager(getContext());
        user = session.getUserDetails();
        pullrefresh.setTextColor(Color.parseColor(colorActive));
        // token
        accesstoken = user.get(SessionManager.KEY_TOKEN);

        crashlytics("Exhibitor Category", accesstoken);
        firbaseAnalytics(getActivity(), "Exhibitor Category",accesstoken);
        cd = new ConnectionDetector(getContext());

        platform = "android";
        device = Build.MODEL;
        os_version = Build.VERSION.RELEASE;
        app_version = "Version" + BuildConfig.VERSION_NAME;
        eventrefresh = view.findViewById(R.id.eventrefresh);
        progressBar = view.findViewById(R.id.progressBar);
        searchEt = view.findViewById(R.id.searchEt);
        img_logout = view.findViewById(R.id.img_logout);
        linear = view.findViewById(R.id.linear);
        imageview = view.findViewById(R.id.imageview);

        int columns = 2;
        exhibitorrecycler.setLayoutManager(new GridLayoutManager(getContext(), columns));

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

        return view;
    }

    public void sendExhiList(String event_id, String token) {
        mAPIService.ExhibitorFetch(event_id, token).enqueue(new Callback<ExhibitorList>() {
            @Override
            public void onResponse(Call<ExhibitorList> call, Response<ExhibitorList> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        Intent main = new Intent(getContext(), LoginActivity.class);
                        startActivity(main);

                    } else {
                        showResponse(response);
                    }
                } else {
                    dismissProgress();
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<ExhibitorList> call, Throwable t) {
                Toast.makeText(getContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
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

            for (int i = 0; i < response.body().getExhibitorCatList().size(); i++) {
                ExhibitorCatList tempExhi = new ExhibitorCatList();
                tempExhi.setName(response.body().getExhibitorCatList().get(i).getName());
                tempExhi.setExhibitor_category_id(response.body().getExhibitorCatList().get(i).getExhibitor_category_id());
                tempExhi.setTotal_exhibitor_count(response.body().getExhibitorCatList().get(i).getTotal_exhibitor_count());
                ExhibitorCatList.add(tempExhi);
            }


            eventAdapter = new ExhibitorListingAdapter(getContext(), ExhibitorCatList, response.body().getExhibitorDataList(), ExhiCatFragment.this);
            eventAdapter.notifyDataSetChanged();
            exhibitorrecycler.setAdapter(eventAdapter);
            exhibitorrecycler.scheduleLayoutAnimation();


            if (eventrefresh.isRefreshing()) {
                eventrefresh.setRefreshing(false);
            }

        } else {
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
            Intent quizOptionIntent = new Intent(getContext(), ExhibitorListingActivity.class);
            quizOptionIntent.putExtra("ExhiName", eventList.getName());
            quizOptionIntent.putExtra("ExhiId", eventList.getExhibitor_category_id());
            startActivity(quizOptionIntent);
            getActivity().finish();
        } else {
            Toast.makeText(getContext(), "Exhibitor not available", Toast.LENGTH_SHORT).show();

        }
    }
}
