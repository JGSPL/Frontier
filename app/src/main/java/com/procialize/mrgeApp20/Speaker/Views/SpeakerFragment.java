package com.procialize.mrgeApp20.Speaker.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
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

import com.procialize.mrgeApp20.Activity.LoginActivity;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.Speaker.Views.Adapter.SpeakerAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.Analytic;
import com.procialize.mrgeApp20.Speaker.Models.FetchSpeaker;
import com.procialize.mrgeApp20.Speaker.Models.PdfList;
import com.procialize.mrgeApp20.Speaker.Models.SpeakerList;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.util.GetUserActivityReport;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_SPEAKER_PIC_PATH;
import static com.procialize.mrgeApp20.Utility.Util.setNotification;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class SpeakerFragment extends Fragment implements SpeakerAdapter.SpeakerAdapterListner {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SwipeRefreshLayout speakerfeedrefresh;
    RecyclerView speakerrecycler;
    EditText searchEt;
    SpeakerAdapter speakerAdapter;
    SessionManager sessionManager;
    String eventid, colorActive;
    String MY_PREFS_NAME = "ProcializeInfo";
    String token;
    TextView pullrefresh;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private APIService mAPIService;
    private ProgressBar progressBar;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private List<SpeakerList> speakerList;
    String pdf_file_path,profile_pic_path;
    private List<PdfList> pdfList;
    private List<SpeakerList> speakersDBList;
    private DBHelper dbHelper;
    LinearLayout linear;
    Boolean isVisible = false;
    String picPath="";


    public SpeakerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpeakerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpeakerFragment newInstance(String param1, String param2) {
        SpeakerFragment fragment = new SpeakerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_speaker, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        try {
            setNotification(getActivity(), MrgeHomeActivity.tv_notification,MrgeHomeActivity.ll_notification_count);
        }catch (Exception e)
        {e.printStackTrace();}

        speakerrecycler = view.findViewById(R.id.speakerrecycler);

        speakerfeedrefresh = view.findViewById(R.id.speakerfeedrefresh);

        searchEt = view.findViewById(R.id.searchEt);

        progressBar = view.findViewById(R.id.progressBar);
        linear = view.findViewById(R.id.linear);
        pullrefresh = view.findViewById(R.id.pullrefresh);

        cd = new ConnectionDetector(getActivity());
        dbHelper = new DBHelper(getActivity());
        sessionManager = new SessionManager(getContext());

        try {
            procializeDB = new DBHelper(getActivity());
            db = procializeDB.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }


        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        pullrefresh.setTextColor(Color.parseColor(colorActive));

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);

        crashlytics("Speaker", token);
        firbaseAnalytics(getActivity(), "Speaker", token);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        speakerrecycler.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        //speakerrecycler.setLayoutAnimation(animation);

        if (cd.isConnectingToInternet()) {
            fetchSpeaker(token, eventid);
        } else {
            db = procializeDB.getReadableDatabase();

            speakersDBList = dbHelper.getSpeakerDetails();
            SharedPreferences prefs1 = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            picPath =  prefs1.getString(KEY_SPEAKER_PIC_PATH,"");
            speakerAdapter = new SpeakerAdapter(getActivity(), speakersDBList,picPath, this);
            speakerAdapter.notifyDataSetChanged();
            speakerrecycler.setAdapter(speakerAdapter);
            speakerrecycler.scheduleLayoutAnimation();
        }

        speakerfeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // fetchSpeaker(token,evntid);
                if (cd.isConnectingToInternet()) {
                    fetchSpeaker(token, eventid);
                } else {
                    db = procializeDB.getReadableDatabase();

                    speakersDBList = dbHelper.getSpeakerDetails();
                    SharedPreferences prefs1 = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    picPath =  prefs1.getString(KEY_SPEAKER_PIC_PATH,"");
                    speakerAdapter = new SpeakerAdapter(getActivity(), speakersDBList, picPath,SpeakerFragment.this);
                    speakerAdapter.notifyDataSetChanged();
                    speakerrecycler.setAdapter(speakerAdapter);
                    speakerrecycler.scheduleLayoutAnimation();
                }
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

                if (speakerAdapter != null) {
                    try {
                        speakerAdapter.getFilter().filter(s.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        /*GetUserActivityReport getUserActivityReport = new GetUserActivityReport(getActivity(), token,
                eventid,
                ApiConstant.pageVisited,
                "12",
                "");
        getUserActivityReport.userActivityReport();*/
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser ) {
           /* if (cd.isConnectingToInternet()) {
                fetchSpeaker(token, eventid);
            } else {
                db = procializeDB.getReadableDatabase();

                speakersDBList = dbHelper.getSpeakerDetails();

                speakerAdapter = new SpeakerAdapter(getActivity(), speakersDBList, this);
                speakerAdapter.notifyDataSetChanged();
                speakerrecycler.setAdapter(speakerAdapter);
                speakerrecycler.scheduleLayoutAnimation();
            }*/

        }
    }



    public void fetchSpeaker(String token, String eventid) {

        showProgress();
        mAPIService.SpeakerFetchPost(token, eventid).enqueue(new Callback<FetchSpeaker>() {
            @Override
            public void onResponse(Call<FetchSpeaker> call, Response<FetchSpeaker> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    hideProgress();
                    if (speakerfeedrefresh.isRefreshing()) {
                        speakerfeedrefresh.setRefreshing(false);
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

                    hideProgress();
                    if (speakerfeedrefresh.isRefreshing()) {
                        speakerfeedrefresh.setRefreshing(false);
                    }
                    //Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchSpeaker> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                //  Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();

                hideProgress();
                if (speakerfeedrefresh.isRefreshing()) {
                    speakerfeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<FetchSpeaker> response) {

        speakerList = response.body().getSpeakerList();
        pdf_file_path = response.body().getSpeaker_pdf_url_path();
        profile_pic_path = response.body().getProfile_pic_url_path();
        procializeDB.clearSpeakersTable();
        procializeDB.insertSpeakersInfo(speakerList, db);
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(KEY_SPEAKER_PIC_PATH,profile_pic_path);
        edit.commit();
        // specify an adapter (see also next example)
        speakerAdapter = new SpeakerAdapter(getActivity(), response.body().getSpeakerList(),profile_pic_path, this);
        speakerAdapter.notifyDataSetChanged();
        speakerrecycler.setAdapter(speakerAdapter);
        speakerrecycler.scheduleLayoutAnimation();

        SubmitAnalytics(token, eventid, "", "", "speaker");
    }

    private void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    private void hideProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onContactSelected(SpeakerList speaker) {
        Intent speakeretail = new Intent(getContext(), SpeakerDetailsActivity.class);
        speakeretail.putExtra("id", speaker.getAttendeeId());
        speakeretail.putExtra("name", speaker.getFirstName() + " " + speaker.getLastName());
        speakeretail.putExtra("city", speaker.getCity());
        speakeretail.putExtra("country", speaker.getCountry());
        speakeretail.putExtra("company", speaker.getCompany());
        speakeretail.putExtra("designation", speaker.getDesignation());
        speakeretail.putExtra("description", speaker.getDescription());
        speakeretail.putExtra("totalrate", speaker.getTotalRating());
        speakeretail.putExtra("profile", speaker.getProfilePic());
        speakeretail.putExtra("mobile", speaker.getMobileNumber());
        speakeretail.putExtra("pdf_list",  (Serializable)speaker.getPdf_list());
        speakeretail.putExtra("pdf_file_path", pdf_file_path);
        startActivity(speakeretail);
    }


    @Override
    public void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
    }

    public void SubmitAnalytics(String token, String eventid, String target_attendee_id, String target_attendee_type, String analytic_type) {

        mAPIService.Analytic(token, eventid, target_attendee_id, target_attendee_type, analytic_type).enqueue(new Callback<Analytic>() {
            @Override
            public void onResponse(Call<Analytic> call, Response<Analytic> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());


                } else {

                    //  Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
                // Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onResume() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
