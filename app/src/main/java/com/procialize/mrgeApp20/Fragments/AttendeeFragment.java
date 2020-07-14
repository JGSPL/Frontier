package com.procialize.mrgeApp20.Fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.procialize.mrgeApp20.Activity.AttendeeDetailActivity;
import com.procialize.mrgeApp20.Activity.LoginActivity;
import com.procialize.mrgeApp20.Adapter.AttendeeAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.AttendeeChat.AttendeeChatActivity;
import com.procialize.mrgeApp20.BuddyList.DataModel.FetchChatList;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.GetterSetter.FetchAttendee;
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
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_ATTENDEE_PIC_PATH;
import static com.procialize.mrgeApp20.Utility.Util.setNotification;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction eventsapp.
 * Use the {@link AttendeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendeeFragment extends Fragment implements AttendeeAdapter.AttendeeAdapterListner {
    private Handler mHandler;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView attendeerecycler;
    SwipeRefreshLayout attendeefeedrefresh;
    EditText searchEt;
    AttendeeAdapter attendeeAdapter;
    List<EventSettingList> eventSettingLists;
    SessionManager sessionManager;
    String eventid,colorActive;
    TextView pullrefresh;
    String MY_PREFS_NAME = "ProcializeInfo";
    String picPath="";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private APIService mAPIService;
    private ProgressBar progressBar;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private List<AttendeeList> attendeeList;
    private List<AttendeeList> attendeesDBList;
    private DBHelper dbHelper;
    LinearLayout linear;
     String token;
    AttendeeList attendeeTmp;
    SpotChatReciever spotChatReciever;
    IntentFilter spotChatFilter;
    Boolean isVisible = false;
    public static Activity activity;

    public AttendeeFragment(Activity activity) {
        this.activity=activity;
    }


    // TODO: Rename and change types and number of parameters
    public static AttendeeFragment newInstance() {
        AttendeeFragment fragment = new AttendeeFragment(activity);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendee, container, false);

        mHandler = new Handler();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        SharedPreferences prefs1 = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        picPath =  prefs1.getString(KEY_ATTENDEE_PIC_PATH,"");

        attendeerecycler = view.findViewById(R.id.attendeerecycler);
        searchEt = view.findViewById(R.id.searchEt);
        attendeefeedrefresh = view.findViewById(R.id.attendeefeedrefresh);
        progressBar = view.findViewById(R.id.progressBar);
        linear = view.findViewById(R.id.linear);
        pullrefresh = view.findViewById(R.id.pullrefresh);
        cd = new ConnectionDetector(getActivity());
        dbHelper = new DBHelper(getActivity());
        sessionManager = new SessionManager(getContext());

        procializeDB = new DBHelper(getActivity());
        db = procializeDB.getWritableDatabase();

        try {
            setNotification(getActivity(), MrgeHomeActivity.tv_notification,MrgeHomeActivity.ll_notification_count);
        }catch (Exception e)
        {e.printStackTrace();}

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        attendeerecycler.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        // attendeerecycler.setLayoutAnimation(animation);

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        pullrefresh.setTextColor(Color.parseColor(colorActive));

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
         token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Attendee",token);
        firbaseAnalytics(getActivity(),"Attendee",token);
        if (cd.isConnectingToInternet()) {
            fetchFeed(token, eventid);
        } else {
            db = procializeDB.getReadableDatabase();

            attendeesDBList = dbHelper.getAttendeeDetails();

            attendeeAdapter = new AttendeeAdapter(getActivity(), attendeesDBList,picPath, this);
            attendeeAdapter.notifyDataSetChanged();
            attendeerecycler.setAdapter(attendeeAdapter);
            attendeerecycler.scheduleLayoutAnimation();
        }

        attendeefeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {
                    fetchFeed(token, eventid);
                } else {
                    attendeesDBList = dbHelper.getAttendeeDetails();

                    attendeeAdapter = new AttendeeAdapter(getActivity(), attendeesDBList, picPath,AttendeeFragment.this);
                    attendeeAdapter.notifyDataSetChanged();
                    attendeerecycler.setAdapter(attendeeAdapter);
                    attendeerecycler.scheduleLayoutAnimation();
                }
            }
        });


        try {
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

                    if (attendeeAdapter != null) {
                        try {
                            attendeeAdapter.getFilter().filter(s.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

      /*  GetUserActivityReport getUserActivityReport = new GetUserActivityReport(getActivity(), token,
                eventid,
                ApiConstant.pageVisited,
                "9",
                "");
        getUserActivityReport.userActivityReport();*/
        try {
            spotChatReciever = new SpotChatReciever();
            spotChatFilter = new IntentFilter(ApiConstant.BROADCAST_ACTION_FOR_SPOT_ChatBuddy);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(spotChatReciever, spotChatFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser ) {
           /* if (cd.isConnectingToInternet()) {
                fetchFeed(token, eventid);
            } else {
                db = procializeDB.getReadableDatabase();

                attendeesDBList = dbHelper.getAttendeeDetails();

                attendeeAdapter = new AttendeeAdapter(getActivity(), attendeesDBList, this);
                attendeeAdapter.notifyDataSetChanged();
                attendeerecycler.setAdapter(attendeeAdapter);
                attendeerecycler.scheduleLayoutAnimation();
            }*/

        }
    }



    public void fetchFeed(String token, String eventid) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.AttendeeFetchPost(token, eventid).enqueue(new Callback<FetchAttendee>() {
            @Override
            public void onResponse(Call<FetchAttendee> call, Response<FetchAttendee> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressBar.setVisibility(View.GONE);
                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(false);
                    }
                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        sessionManager.logoutUser();
                        Intent main = new Intent(getContext(), LoginActivity.class);
                        startActivity(main);
                        getActivity().finish();
                    } else {
                        showResponse(response);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);

                    if (attendeefeedrefresh.isRefreshing()) {
                        attendeefeedrefresh.setRefreshing(false);
                    }
                    //Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchAttendee> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                //  Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                if (attendeefeedrefresh.isRefreshing()) {
                    attendeefeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<FetchAttendee> response) {

        // specify an adapter (see also next example)
        attendeeList = response.body().getAttendeeList();
        String picPath = response.body().getProfile_pic_url_path();
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(KEY_ATTENDEE_PIC_PATH,picPath);
        edit.commit();



        attendeeAdapter = new AttendeeAdapter(getActivity(), attendeeList,picPath, this);
        //attendeeAdapter.notifyDataSetChanged();
        attendeerecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        attendeerecycler.setAdapter(attendeeAdapter);
        attendeerecycler.smoothScrollToPosition(0);
        //attendeerecycler.scheduleLayoutAnimation();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Update the value background thread to UI thread
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        procializeDB.clearAttendeesTable();
                        procializeDB.insertAttendeesInfo(attendeeList, db);
                    }
                });
            }
        }).start();

        //attendeesDBList = dbHelper.getAttendeeDetails();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onContactSelected(AttendeeList attendee) {

      //  attendeeTmp = attendee;
        if (attendee.getChat_data_count().equalsIgnoreCase("0")) {
            Intent attendeetail = new Intent(getContext(), AttendeeDetailActivity.class);
            attendeetail.putExtra("id", attendee.getAttendeeId());
            attendeetail.putExtra("name", attendee.getFirstName() + " " + attendee.getLastName());
            attendeetail.putExtra("city", attendee.getCity());
            attendeetail.putExtra("country", attendee.getCountry());
            attendeetail.putExtra("company", attendee.getCompanyName());
            attendeetail.putExtra("designation", attendee.getDesignation());
            attendeetail.putExtra("description", attendee.getDescription());
            attendeetail.putExtra("profile", attendee.getProfilePic());
            attendeetail.putExtra("mobile", attendee.getMobile());
            attendeetail.putExtra("buddy_status", attendee.getBuddy_status());
            attendeetail.putExtra("page_status", "ListPage");


            startActivity(attendeetail);

        } else{
            Intent attendeetail = new Intent(getContext(), AttendeeChatActivity.class);
            attendeetail.putExtra("id", attendee.getAttendeeId());
            attendeetail.putExtra("name", attendee.getFirstName() + " " + attendee.getLastName());
            attendeetail.putExtra("city", attendee.getCity());
            attendeetail.putExtra("country", attendee.getCountry());
            attendeetail.putExtra("company", attendee.getCompanyName());
            attendeetail.putExtra("designation", attendee.getDesignation());
            attendeetail.putExtra("description", attendee.getDescription());
            attendeetail.putExtra("profile", attendee.getProfilePic());
            attendeetail.putExtra("mobile", attendee.getMobile());
            attendeetail.putExtra("buddy_status", attendee.getBuddy_status());
            attendeetail.putExtra("fromPage", "Attendee");

//                speakeretail.putExtra("totalrate",attendee.getTotalRating());
            startActivity(attendeetail);
        }
       // UserChatHistory(eventid,token,attendeeTmp.getAttendeeId(),"1");;

    }

    private class SpotChatReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            attendeeAdapter.notifyDataSetChanged();
            attendeerecycler.smoothScrollToPosition(0);
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }

  /*  @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (cd.isConnectingToInternet()) {
            fetchFeed(token, eventid);
        } else {
            db = procializeDB.getReadableDatabase();

            attendeesDBList = dbHelper.getAttendeeDetails();

            SharedPreferences prefs1 = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            picPath =  prefs1.getString(KEY_ATTENDEE_PIC_PATH,"");

            attendeeAdapter = new AttendeeAdapter(getActivity(), attendeesDBList,picPath, this);
            attendeeAdapter.notifyDataSetChanged();
            attendeerecycler.setAdapter(attendeeAdapter);
            attendeerecycler.scheduleLayoutAnimation();
        }
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onBackpressed() {

        Intent intent = new Intent(activity, MrgeHomeActivity.class);
        activity.startActivity(intent);
    }


}
