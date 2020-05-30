package com.procialize.mrgeApp20.Engagement.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.Engagement.Activity.SelfieContestActivity;
import com.procialize.mrgeApp20.Engagement.Activity.VideoContestActivity;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;

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
    private List<EventSettingList> eventSettingLists;
    LinearLayout linear;
   View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_engagement, container, false);
        initView(rootView);
        return rootView;
    }

   public void initView(View rootView) {

       try {
           setNotification(getActivity(),MrgeHomeActivity.tv_notification, MrgeHomeActivity.ll_notification_count);
       }catch (Exception e)
       {e.printStackTrace();}

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");



        selfiecard_view = rootView.findViewById(R.id.selfiecard_view);
        videocard_view = rootView.findViewById(R.id.videocard_view);
        linear = rootView.findViewById(R.id.linear);


        TextView header = rootView.findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));
        TextView selfieTv = rootView.findViewById(R.id.selfieTv);
        //selfieTv.setBackgroundColor(Color.parseColor(colorActive));
        TextView videoTv = rootView.findViewById(R.id.videoTv);
        //videoTv.setBackgroundColor(Color.parseColor(colorActive));


        SessionManager sessionManager = new SessionManager(getActivity());

        user = sessionManager.getUserDetails();


        crashlytics("Engagement",user.get(SessionManager.KEY_TOKEN));
        firbaseAnalytics(getActivity(), "Engagement",user.get(SessionManager.KEY_TOKEN));
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
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

           /* if (eventSettingLists.get(i).getFieldName().equals("engagement_selfie_contest")) {
                engagement_selfie_contest = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("engagement_video_contest")) {
                engagement_video_contest = eventSettingLists.get(i).getFieldValue();
            }*/
            if (eventSettingLists.get(i).getFieldName().equals("interact")) {

                if(eventSettingLists.get(i).getSub_menuList()!=null) {
                    if (eventSettingLists.get(i).getSub_menuList().size() > 0) {
                        for (int k = 0; k < eventSettingLists.get(i).getSub_menuList().size(); k++) {
                            if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("engagement_selfie_contest")) {
                                engagement_selfie_contest = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("engagement_video_contest")) {
                                engagement_video_contest = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }
                        }
                    }
                }
            }

        }
    }


    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }

}
