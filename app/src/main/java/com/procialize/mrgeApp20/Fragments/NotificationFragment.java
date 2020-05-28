package com.procialize.mrgeApp20.Fragments;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.procialize.mrgeApp20.Adapter.ExhibitorNotificationAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.GetterSetter.NewsFeedList;
import com.procialize.mrgeApp20.GetterSetter.NotificationList;
import com.procialize.mrgeApp20.GetterSetter.NotificationListExhibitorFetch;
import com.procialize.mrgeApp20.GetterSetter.NotificationSend;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationFragment extends Fragment implements ExhibitorNotificationAdapter.ExhibitorNotificationAdapterListner {

    RecyclerView notificationRv;
    //    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, logoImg, colorActive;
    ImageView headerlogoIv;
    List<EventSettingList> eventSettingLists;
    public static String news_feed_share,
            news_feed_comment,
            news_feed_like;
    private APIService mAPIService;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private List<NewsFeedList> newsfeedsDBList;
    private List<AttendeeList> attendeeDBList;
    private List<NotificationList> notificationDBList = new ArrayList<>();
    RelativeLayout linear;
    Dialog myDialog;
    int currentApiVersion = android.os.Build.VERSION.SDK_INT;
    String formatdate;
    final long[] time = new long[1];
    String token, attendee_status;
    SharedPreferences prefs;
    HashMap<String, String> user;
    SessionManager session;
    String accesstoken, exhibitor_status, exhibitor_id;
    SwipeRefreshLayout notificationRvrefresh;
    ConnectionDetector cd;
    TextView msg, pullrefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        logoImg = prefs.getString("logoImg", "");
        colorActive = prefs.getString("colorActive", "");


        procializeDB = new DBHelper(getActivity());
        db = procializeDB.getReadableDatabase();
        dbHelper = new DBHelper(getActivity());

        TextView notyHeader = view.findViewById(R.id.notyHeader);
        notyHeader.setTextColor(Color.parseColor(colorActive));
        notificationRv = view.findViewById(R.id.notificationRv);
        notificationRvrefresh = view.findViewById(R.id.notificationRvrefresh);
        linear = view.findViewById(R.id.linear);
        msg = view.findViewById(R.id.msg);
        pullrefresh = view.findViewById(R.id.pullrefresh);
//        add_icon = view.findViewById(R.id.add_icon);
        int colorInt = Color.parseColor(colorActive);
        msg.setTextColor(Color.parseColor(colorActive));
        pullrefresh.setTextColor(Color.parseColor(colorActive));




        try {
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        mAPIService = ApiUtils.getAPIService();

        prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        session = new SessionManager(getActivity());
        user = session.getUserDetails();
        // token
        accesstoken = user.get(SessionManager.KEY_TOKEN);
        exhibitor_id = user.get(SessionManager.EXHIBITOR_ID);
        exhibitor_status = user.get(SessionManager.EXHIBITOR_STATUS);

        SessionManager sessionManager = new SessionManager(getActivity());

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);
        attendee_status = user.get(SessionManager.ATTENDEE_STATUS);

        eventSettingLists = new ArrayList<>();
        eventSettingLists = SessionManager.loadEventList();
        applysetting(eventSettingLists);


        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        notificationRv.setLayoutManager(mLayoutManager);
        cd = new ConnectionDetector(getActivity());
        if (cd.isConnectingToInternet()) {
            NotificationListExhibitorFetch(eventid, accesstoken, exhibitor_id);
        } else {
            notificationDBList.clear();
            notificationDBList = dbHelper.getNotificationDetails();

            if (notificationDBList.size() == 0) {
                notificationRv.setVisibility(View.GONE);
                msg.setVisibility(View.VISIBLE);
            } else {
                notificationRv.setVisibility(View.VISIBLE);
                msg.setVisibility(View.GONE);
                ExhibitorNotificationAdapter notificationAdapter = new ExhibitorNotificationAdapter(getActivity(), notificationDBList, new NotificationFragment());
                notificationAdapter.notifyDataSetChanged();
                notificationRv.setAdapter(notificationAdapter);
            }
        }


        notificationRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {
                    NotificationListExhibitorFetch(eventid, accesstoken, exhibitor_id);
                } else {
                    notificationDBList.clear();
                    notificationDBList = dbHelper.getNotificationDetails();

                    if (notificationDBList.size() == 0) {
                        notificationRv.setVisibility(View.GONE);
                        msg.setVisibility(View.VISIBLE);
                    } else {
                        notificationRv.setVisibility(View.VISIBLE);
                        msg.setVisibility(View.GONE);
                        ExhibitorNotificationAdapter notificationAdapter = new ExhibitorNotificationAdapter(getActivity(), notificationDBList, new NotificationFragment());
                        notificationAdapter.notifyDataSetChanged();
                        notificationRv.setAdapter(notificationAdapter);
                    }
                }
            }
        });

        return view;
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("news_feed_like")) {
                news_feed_like = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("news_feed_comment")) {
                news_feed_comment = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("news_feed_share")) {
                news_feed_share = eventSettingLists.get(i).getFieldValue();
            }

        }
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

                    notificationDBList.clear();
                    notificationDBList = dbHelper.getNotificationDetails();
                    if (notificationDBList.size() == 0) {
                        notificationRv.setVisibility(View.GONE);
                        msg.setVisibility(View.VISIBLE);
                    } else {
                        notificationRv.setVisibility(View.VISIBLE);
                        msg.setVisibility(View.GONE);
                        ExhibitorNotificationAdapter notificationAdapter = new ExhibitorNotificationAdapter(getActivity(), notificationDBList, new NotificationFragment());
                        notificationAdapter.notifyDataSetChanged();
                        notificationRv.setAdapter(notificationAdapter);
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
    public void onContactSelected(NotificationList notification, Context context) {

    }

    @Override
    public void onReplyClick(NotificationList notificationList, Context context) {

    }

    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }

    private void showratedialouge() {

        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.add_notification);
        myDialog.setCancelable(false);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


        LinearLayout diatitle = myDialog.findViewById(R.id.diatitle);
        ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);
        ImageView id_date = myDialog.findViewById(R.id.id_date);
        Button canclebtn = myDialog.findViewById(R.id.canclebtn);
        Button send_notification = myDialog.findViewById(R.id.send_notification);
        final EditText etmsg = myDialog.findViewById(R.id.etmsg);
        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String dateString = sdf.format(date);
        nametv.setText(dateString);

        nametv.setTextColor(Color.parseColor(colorActive));
        diatitle.setBackgroundColor(Color.parseColor(colorActive));
        send_notification.setBackgroundColor(Color.parseColor(colorActive));
        canclebtn.setBackgroundColor(Color.parseColor(colorActive));


        id_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View dialogView = View.inflate(getActivity(), R.layout.activity_date_picker, null);
                final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getActivity()).create();

                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {

                        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute());
                        datePicker.setMinDate(new Date().getTime());
                        int selectyear = datePicker.getYear();
                        int selectmonth = datePicker.getMonth();
                        int selectday = datePicker.getDayOfMonth();
                        int selecttime = 0;
                        int selecthour = 0;
                        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                            selecttime = timePicker.getMinute();
                            selecthour = timePicker.getHour();
                        } else {
                            selecttime = timePicker.getCurrentMinute();
                            selecthour = timePicker.getCurrentHour();

                        }
                        int seconds = calendar.get(Calendar.SECOND);

                        Date mDate = new GregorianCalendar(selectyear, selectmonth, selectday, selecthour, selecttime).getTime();
                        if (mDate.getTime() <= calendar.getTimeInMillis()) {
//                            int hour = hourOfDay % 12;
                        } else {
                            Toast.makeText(dialogView.getContext(), "Invalid Date", Toast.LENGTH_LONG).show();
                        }


                        formatdate = (String) android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss", mDate);
                        String date = (String) android.text.format.DateFormat.format("dd MMMM HH:mm", mDate);
                        nametv.setText(date);
                        time[0] = calendar.getTimeInMillis();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });


        send_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nametv.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Select Date", Toast.LENGTH_SHORT).show();
                } else if (etmsg.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter your message", Toast.LENGTH_SHORT).show();
                } else {
                    sendNotification(token, eventid, StringEscapeUtils.escapeJava(etmsg.getText().toString()), formatdate);
                }
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        etmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count = 250 - s.length();
                counttv.setText(count + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        myDialog.show();

    }

    public void sendNotification(String token, String eventid, String message, String display_time) {
//        showProgress();
        mAPIService.SendNotification(token, eventid, message, display_time).enqueue(new Callback<NotificationSend>() {
            @Override
            public void onResponse(Call<NotificationSend> call, Response<NotificationSend> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


//                    dismissProgress();
                    showResponseSendNotification(response);
                } else {


//                    dismissProgress();
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationSend> call, Throwable t) {
                Toast.makeText(getActivity(), "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();

            }
        });
    }

    public void showResponseSendNotification(Response<NotificationSend> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            myDialog.dismiss();
            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
        } else {
            myDialog.dismiss();
            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

        }
    }
}
