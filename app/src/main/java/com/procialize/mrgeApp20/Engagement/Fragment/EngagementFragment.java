package com.procialize.mrgeApp20.Engagement.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.Engagement.Activity.SelfieContestActivity;
import com.procialize.mrgeApp20.Engagement.Activity.VideoContestActivity;
import com.procialize.mrgeApp20.Engagement.Models.FetchEngagementData;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Utility.Util.setNotification;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class EngagementFragment extends Fragment {

    CardView selfiecard_view, videocard_view;
    HashMap<String, String> user;
    String engagement_selfie_contest = "0", engagement_video_contest = "0";
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    LinearLayout linear;
    View rootView;
    ProgressBar progressBar;
    APIService mAPIService;
    TextView selfieTv, tv_selfie_total_items, tv_selfie_desc, videoTv, video_total_items, tv_video_desc;
    ConnectionDetector cd;
    private List<EventSettingList> eventSettingLists;
    public static Activity activity;

    public EngagementFragment(Activity activity) {
        this.activity = activity;
    }

    public static EngagementFragment newInstance() {
        EngagementFragment fragment = new EngagementFragment(activity);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_engagement, container, false);
        initView(rootView);
        return rootView;
    }

    public void initView(View rootView) {

        mAPIService = ApiUtils.getAPIService();
        try {
            setNotification(getActivity(), MrgeHomeActivity.tv_notification, MrgeHomeActivity.ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }

        selfieTv = (TextView) rootView.findViewById(R.id.selfieTv);
        tv_selfie_total_items = (TextView) rootView.findViewById(R.id.tv_selfie_total_items);
        tv_selfie_desc = (TextView) rootView.findViewById(R.id.tv_selfie_desc);
        videoTv = (TextView) rootView.findViewById(R.id.videoTv);
        video_total_items = (TextView) rootView.findViewById(R.id.video_total_items);
        tv_video_desc = (TextView) rootView.findViewById(R.id.tv_video_desc);

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        selfiecard_view = rootView.findViewById(R.id.selfiecard_view);
        videocard_view = rootView.findViewById(R.id.videocard_view);
        linear = rootView.findViewById(R.id.linear);


      /*  TextView header = rootView.findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));*/
        TextView selfieTv = rootView.findViewById(R.id.selfieTv);
        //selfieTv.setBackgroundColor(Color.parseColor(colorActive));
        TextView videoTv = rootView.findViewById(R.id.videoTv);
        progressBar = rootView.findViewById(R.id.progressBar);
        //videoTv.setBackgroundColor(Color.parseColor(colorActive));


        SessionManager sessionManager = new SessionManager(getActivity());

        user = sessionManager.getUserDetails();
        String token = user.get(SessionManager.KEY_TOKEN);
        String event_id = prefs.getString("eventid", "1");


        crashlytics("Engagement", user.get(SessionManager.KEY_TOKEN));
        firbaseAnalytics(getActivity(), "Engagement", user.get(SessionManager.KEY_TOKEN));
        eventSettingLists = SessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }


        if (engagement_selfie_contest.equalsIgnoreCase("0")) {
            selfiecard_view.setVisibility(View.GONE);
        }

        if (engagement_video_contest.equalsIgnoreCase("0")) {
            videocard_view.setVisibility(View.GONE);
        }

        selfiecard_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent selfie = new Intent(getActivity(), SelfieContestActivity.class);
                startActivity(selfie);
//                finish();
            }
        });

        videocard_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent videocontest = new Intent(getActivity(), VideoContestActivity.class);
                startActivity(videocontest);
//                finish();
            }
        });

        cd = new ConnectionDetector(getActivity());
        if (cd.isConnectingToInternet()) {
            getEngagementData(event_id, token);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection..!", Toast.LENGTH_SHORT).show();
        }
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

           /* if (eventSettingLists.get(i).getFieldName().equals("engagement_selfie_contest")) {
                engagement_selfie_contest = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("engagement_video_contest")) {
                engagement_video_contest = eventSettingLists.get(i).getFieldValue();
            }*/
            if (eventSettingLists.get(i).getFieldName().equals("interact")) {

                if (eventSettingLists.get(i).getSub_menuList() != null) {
                    if (eventSettingLists.get(i).getSub_menuList().size() > 0) {
                        for (int k = 0; k < eventSettingLists.get(i).getSub_menuList().size(); k++) {
                            if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("engagement_selfie_contest")) {
                                engagement_selfie_contest = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("engagement_video_contest")) {
                                engagement_video_contest = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }
                        }
                    }
                }
            }

        }
    }

    public void getEngagementData(String eventid, String apikey) {
        showProgress();
        mAPIService.getEngagementData(eventid, apikey).enqueue(new Callback<FetchEngagementData>() {
            @Override
            public void onResponse(Call<FetchEngagementData> call, Response<FetchEngagementData> response) {
                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponse(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchEngagementData> call, Throwable t) {
                Toast.makeText(getActivity(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }


    public void showResponse(Response<FetchEngagementData> response) {
        if (response.body().getStatus().equals("success")) {
            String selfieTitle = response.body().getSelfie_engagement().getSelfie_title();
            String selfieDescription = response.body().getSelfie_engagement().getSelfie_description();
            String selfieTotalItem = response.body().getSelfie_engagement().getTotal_items();

            selfieTv.setText(selfieTitle);
            tv_selfie_total_items.setText(selfieTotalItem);
            tv_selfie_desc.setText(selfieDescription);

            String videoTitle = response.body().getVideo_engagement().getVideo_title();
            String videoDescription = response.body().getVideo_engagement().getVideo_description();
            String videoTotalItems = response.body().getVideo_engagement().getTotal_items();

            videoTv.setText(videoTitle);
            video_total_items.setText(videoTotalItems);
            tv_video_desc.setText(videoDescription);

        } else {
            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
    }

    public void onBackpressed() {

        Intent intent = new Intent(activity, MrgeHomeActivity.class);
        activity.startActivity(intent);
    }

}