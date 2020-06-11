package com.procialize.mrgeApp20.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.procialize.mrgeApp20.Activity.AttendeeDetailActivity;
import com.procialize.mrgeApp20.Activity.ExhibitorDetailActivity;
import com.procialize.mrgeApp20.Activity.LoginActivity;
import com.procialize.mrgeApp20.Adapter.ExhibitorAttendeeAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorAttendeeList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorBrochureList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorCatList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorDataList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorList;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.util.GetUserActivityReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ContactFragment extends Fragment implements ExhibitorAttendeeAdapter.MyTravelAdapterListner {
    RecyclerView attendeerecycler;
    SwipeRefreshLayout attendeefeedrefresh;
    ExhibitorAttendeeAdapter attendeeAdapter;
    List<ExhibitorAttendeeList> ExhibitorAttendeeList;
    List<AttendeeList> AttendeeList;
    List<ExhibitorAttendeeList> ExhibitorAttendeeTemp = new ArrayList<>();
    TextView empty;
    String ExhiID;
    String eventid, colorActive, accesstoken;
    String MY_PREFS_NAME = "ProcializeInfo";
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private List<AttendeeList> attendeeDBList;
    APIService mAPIService;
    private List<ExhibitorCatList> ExhibitorCatList = new ArrayList<>();
    private List<ExhibitorDataList> ExhibitorDataList = new ArrayList<>();
    public List<ExhibitorBrochureList> ExhibitorBrochureList = new ArrayList<>();
    ConnectionDetector cd;
    HashMap<String, String> user;
    SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);


        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        attendeerecycler = view.findViewById(R.id.attendeerecycler);
        attendeefeedrefresh = view.findViewById(R.id.attendeefeedrefresh);
        empty = view.findViewById(R.id.empty);
        dbHelper = new DBHelper(getActivity());

        procializeDB = new DBHelper(getActivity());
        db = procializeDB.getWritableDatabase();
        mAPIService = ApiUtils.getAPIService();
        session = new SessionManager(getActivity());
        user = session.getUserDetails();
        cd = new ConnectionDetector(getActivity());
        // token
        accesstoken = user.get(SessionManager.KEY_TOKEN);

        ExhibitorDetailActivity exhiD = new ExhibitorDetailActivity();

        ExhiID = ExhibitorDetailActivity.Exhi_id;

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        attendeerecycler.setLayoutManager(mLayoutManager);

        empty.setTextColor(Color.parseColor(colorActive));
        if (cd.isConnectingToInternet()) {
            sendExhiList(eventid, accesstoken);
        } else {
            empty.setVisibility(View.GONE);
            if (attendeefeedrefresh.isRefreshing() == true) {
                attendeefeedrefresh.setRefreshing(false);
            }
            attendeeAdapter = new ExhibitorAttendeeAdapter(getActivity(), ExhibitorAttendeeTemp, ContactFragment.this);
            attendeeAdapter.notifyDataSetChanged();
            attendeerecycler.setAdapter(attendeeAdapter);
            attendeerecycler.scheduleLayoutAnimation();
        }


        attendeefeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {
                    sendExhiList(eventid, accesstoken);
                } else {
                    empty.setVisibility(View.GONE);
                    if (attendeefeedrefresh.isRefreshing() == true) {
                        attendeefeedrefresh.setRefreshing(false);
                    }
                    attendeeAdapter = new ExhibitorAttendeeAdapter(getActivity(), ExhibitorAttendeeTemp, ContactFragment.this);
                    attendeeAdapter.notifyDataSetChanged();
                    attendeerecycler.setAdapter(attendeeAdapter);
                    attendeerecycler.scheduleLayoutAnimation();
                }

            }
        });

        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(getActivity(), accesstoken,
                eventid,
                ApiConstant.pageVisited,
                "16",
                "");
        getUserActivityReport.userActivityReport();
        return view;
    }

    public void sendExhiList(String event_id, String token) {
        mAPIService.ExhibitorFetch(event_id, token).enqueue(new Callback<ExhibitorList>() {
            @Override
            public void onResponse(Call<ExhibitorList> call, Response<ExhibitorList> response) {
                if (attendeefeedrefresh.isRefreshing() == true) {
                    attendeefeedrefresh.setRefreshing(false);
                }
                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        Intent main = new Intent(getActivity(), LoginActivity.class);
                        startActivity(main);

                    } else {
                        showResponse(response);
                    }
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<ExhibitorList> call, Throwable t) {
                if (attendeefeedrefresh.isRefreshing() == true) {
                    attendeefeedrefresh.setRefreshing(false);
                }
                Toast.makeText(getActivity(), "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showResponse(Response<ExhibitorList> response) {

        if (response.body().getStatus().equals("success")) {

            ExhibitorDataList = response.body().getExhibitorDataList();
            ExhibitorCatList = response.body().getExhibitorCatList();
            ExhibitorAttendeeList = response.body().getExhibitorAttendeeList();
            ExhibitorBrochureList = response.body().getExhibitorBrochureList();

            dbHelper.clearEXattendeeTable();
            dbHelper.clearEXbrocherTable();
            dbHelper.insertExAttendeeList(ExhibitorAttendeeList, db);
            dbHelper.insertExBrocherList(ExhibitorBrochureList, db);


            ExhibitorAttendeeTemp.clear();
            ExhibitorAttendeeList = dbHelper.getExAttendeeList();
            for (int i = 0; i < ExhibitorAttendeeList.size(); i++) {
                if (ExhibitorAttendeeList.get(i).getExhibitor_id().equalsIgnoreCase(ExhiID)) {
                    ExhibitorAttendeeList tempExhi = new ExhibitorAttendeeList();
                    tempExhi.setAttendee_id(ExhibitorAttendeeList.get(i).getAttendee_id());
                    tempExhi.setCity(ExhibitorAttendeeList.get(i).getCity());
                    tempExhi.setExhibitor_id(ExhibitorAttendeeList.get(i).getExhibitor_id());

                    tempExhi.setFirst_name(ExhibitorAttendeeList.get(i).getFirst_name());
                    tempExhi.setLast_name(ExhibitorAttendeeList.get(i).getLast_name());
                    tempExhi.setProfile_pic(ExhibitorAttendeeList.get(i).getProfile_pic());

                    ExhibitorAttendeeTemp.add(tempExhi);
                }
            }
            if (ExhibitorAttendeeTemp.size() > 0) {
                empty.setVisibility(View.GONE);
                attendeeAdapter = new ExhibitorAttendeeAdapter(getActivity(), ExhibitorAttendeeTemp, ContactFragment.this);
                attendeeAdapter.notifyDataSetChanged();
                attendeerecycler.setAdapter(attendeeAdapter);
                attendeerecycler.scheduleLayoutAnimation();
            } else {
                empty.setVisibility(View.VISIBLE);
            }

        } else {
            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onContactSelected(ExhibitorAttendeeList travel) {
        dbHelper.getWritableDatabase();
        AttendeeList = dbHelper.getAttendeeDetailsId(travel.getAttendee_id());

        Intent attendeetail = new Intent(getActivity(), AttendeeDetailActivity.class);
        attendeetail.putExtra("id", AttendeeList.get(0).getAttendeeId());
        attendeetail.putExtra("name", AttendeeList.get(0).getFirstName() + " " + AttendeeList.get(0).getLastName());
        attendeetail.putExtra("city", AttendeeList.get(0).getCity());
        attendeetail.putExtra("country", AttendeeList.get(0).getCountry());
        attendeetail.putExtra("company", AttendeeList.get(0).getCompanyName());
        attendeetail.putExtra("designation", AttendeeList.get(0).getDesignation());
        attendeetail.putExtra("description", AttendeeList.get(0).getDescription());
        attendeetail.putExtra("profile", AttendeeList.get(0).getProfilePic());
        attendeetail.putExtra("mobile", AttendeeList.get(0).getMobile());
        startActivity(attendeetail);

    }
}
