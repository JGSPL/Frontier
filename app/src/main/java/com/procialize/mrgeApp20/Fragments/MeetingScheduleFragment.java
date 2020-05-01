package com.procialize.mrgeApp20.Fragments;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procialize.mrgeApp20.Adapter.MeetingScheduleAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.Exhibitor_Meeting_Request_List;
import com.procialize.mrgeApp20.GetterSetter.NotificationListExhibitorFetch;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MeetingScheduleFragment extends Fragment implements MeetingScheduleAdapter.MeetingScheduleAdapterListner {

    RecyclerView recycler_meeting;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, logoImg, colorActive;
    private APIService mAPIService;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    SessionManager session;
    String accesstoken, exhibitor_status, exhibitor_id;
    HashMap<String, String> user;
    String token, attendee_status;
    ConnectionDetector cd;
    private List<Exhibitor_Meeting_Request_List> newsfeedsDBList = new ArrayList<>();
    MeetingScheduleAdapter messageAdapter;
    SwipeRefreshLayout notificationRvrefresh;
    TextView msg,pullrefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meeting_schedule, container, false);

        recycler_meeting = view.findViewById(R.id.recycler_meeting);
        notificationRvrefresh = view.findViewById(R.id.notificationRvrefresh);
        pullrefresh = view.findViewById(R.id.pullrefresh);
        msg = view.findViewById(R.id.msg);
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        logoImg = prefs.getString("logoImg", "");
        colorActive = prefs.getString("colorActive", "");


        procializeDB = new DBHelper(getActivity());
        db = procializeDB.getWritableDatabase();
        dbHelper = new DBHelper(getActivity());
        mAPIService = ApiUtils.getAPIService();
        cd = new ConnectionDetector(getActivity());

        prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        session = new SessionManager(getActivity());
        user = session.getUserDetails();
        // token
        accesstoken = user.get(SessionManager.KEY_TOKEN);
        exhibitor_id = user.get(SessionManager.EXHIBITOR_ID);
        exhibitor_status = user.get(SessionManager.EXHIBITOR_STATUS);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recycler_meeting.setLayoutManager(mLayoutManager);
        pullrefresh.setTextColor(Color.parseColor(colorActive));
        SessionManager sessionManager = new SessionManager(getActivity());

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);
        attendee_status = user.get(SessionManager.ATTENDEE_STATUS);

        if (cd.isConnectingToInternet()) {
            NotificationListExhibitorFetch(eventid, accesstoken, exhibitor_id);
        } else {
            newsfeedsDBList.clear();
            newsfeedsDBList = dbHelper.getMeetingDetails();

            if (newsfeedsDBList.size() == 0) {
                recycler_meeting.setVisibility(View.GONE);
                msg.setVisibility(View.VISIBLE);
            } else {
                recycler_meeting.setVisibility(View.VISIBLE);
                msg.setVisibility(View.GONE);
                messageAdapter = new MeetingScheduleAdapter(getActivity(), newsfeedsDBList, new MeetingScheduleFragment());
                messageAdapter.notifyDataSetChanged();
                recycler_meeting.setAdapter(messageAdapter);
            }

        }

        notificationRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {
                    NotificationListExhibitorFetch(eventid, accesstoken, exhibitor_id);
                } else {
                    newsfeedsDBList.clear();
                    newsfeedsDBList = dbHelper.getMeetingDetails();

                    if (newsfeedsDBList.size() == 0) {
                        recycler_meeting.setVisibility(View.GONE);
                        msg.setVisibility(View.VISIBLE);
                    } else {
                        recycler_meeting.setVisibility(View.VISIBLE);
                        msg.setVisibility(View.GONE);
                        messageAdapter = new MeetingScheduleAdapter(getActivity(), newsfeedsDBList, new MeetingScheduleFragment());
                        messageAdapter.notifyDataSetChanged();
                        recycler_meeting.setAdapter(messageAdapter);
                    }

                }
            }
        });

        return view;
    }


    public void NotificationListExhibitorFetch(String eventid, String token, String exhibitor_id) {

        mAPIService.NotificationListExhibitorFetch(token, eventid, exhibitor_id).enqueue(new Callback<NotificationListExhibitorFetch>() {
            @Override
            public void onResponse(Call<NotificationListExhibitorFetch> call, Response<NotificationListExhibitorFetch> response) {

                if (response.isSuccessful()) {
                    if (notificationRvrefresh.isRefreshing()) {
                        notificationRvrefresh.setRefreshing(false);
                    }
                    dbHelper.clearExNotificationTable();
                    dbHelper.clearEXMeetingTable();
                    dbHelper.clearNotificationTable();
                    dbHelper.insertExNotificationList(response.body().getExhibitor_notification_list(), db);
                    dbHelper.insertNotificationList(response.body().getNotification_list(), db);
                    dbHelper.insertMeetingList(response.body().getExhibitor_meeting_request_list(), db);

                    newsfeedsDBList.clear();
                    newsfeedsDBList = dbHelper.getMeetingDetails();

                    if (newsfeedsDBList.size() == 0) {
                        recycler_meeting.setVisibility(View.GONE);
                        msg.setVisibility(View.VISIBLE);
                    } else {
                        recycler_meeting.setVisibility(View.VISIBLE);
                        msg.setVisibility(View.GONE);
                        messageAdapter = new MeetingScheduleAdapter(getActivity(), newsfeedsDBList, new MeetingScheduleFragment());
                        messageAdapter.notifyDataSetChanged();
                        recycler_meeting.setAdapter(messageAdapter);
                    }
                } else {
                    if (notificationRvrefresh.isRefreshing()) {
                        notificationRvrefresh.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationListExhibitorFetch> call, Throwable t) {
                if (notificationRvrefresh.isRefreshing()) {
                    notificationRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onContactSelected(Exhibitor_Meeting_Request_List travel) {

    }
}
