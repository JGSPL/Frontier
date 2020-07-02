package com.procialize.mrgeApp20.MrgeInnerFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.procialize.mrgeApp20.Adapter.QASpeakerAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DialogQnA.DialogQnADirect;
import com.procialize.mrgeApp20.GetterSetter.QASpeakerFetch;
import com.procialize.mrgeApp20.GetterSetter.QuestionSpeakerList;
import com.procialize.mrgeApp20.GetterSetter.SpeakerQuestionList;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.util.GetUserActivityReport;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_NEWSFEED_COMMENT_PROFILE_PATH;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_NEWSFEED_COMMENT_URL_PATH;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_SPEAKER_PROFILE_PATH;
import static com.procialize.mrgeApp20.Utility.Util.setNotification;
import static com.procialize.mrgeApp20.Utility.Utility.setgradientDrawable;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class QnASpeakerFragment extends Fragment implements QASpeakerAdapter.QASpeakerAdapterListner {

    View rootView;
    public static String Selectedspeaker, SelectedspeakerId;
    public RecyclerView qaRv;
    public Button postbtn;
    public QASpeakerAdapter qaSpeakerAdapter;
    SwipeRefreshLayout qaRvrefresh;
    ProgressBar progressBar;
    ArrayList<String> list;
    Spinner spinner;
    List<QuestionSpeakerList> agendaLisQAS;
    Boolean change = true;
    Dialog myDialog;
    String token;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    RelativeLayout linUpper;
    TextView txtEmpty;
    private APIService mAPIService;
    LinearLayout linear;
    ConnectionDetector cd;

    public QnASpeakerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_qaspeaker, container, false);

        try {
            setNotification(getActivity(),MrgeHomeActivity.tv_notification,MrgeHomeActivity.ll_notification_count);
        }catch (Exception e)
        {e.printStackTrace();}

        cd = new ConnectionDetector(getContext());
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        qaRv = rootView.findViewById(R.id.qaRv);
        postbtn = rootView.findViewById(R.id.postbtn);

        GradientDrawable shape = setgradientDrawable(5, colorActive);
        postbtn.setBackground(shape);

       // DialogQnADirect dialogQna = new DialogQnADirect();
       // dialogQna.welcomeQnADialog(getContext());

        qaRvrefresh = rootView.findViewById(R.id.qaRvrefresh);
        spinner = rootView.findViewById(R.id.spinner);
        progressBar = rootView.findViewById(R.id.progressBar);
        linUpper = rootView.findViewById(R.id.linUpper);
        txtEmpty = rootView.findViewById(R.id.txtEmpty);
        linear = rootView.findViewById(R.id.linear);

        TextView header = rootView.findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));

        list = new ArrayList<>();
        agendaLisQAS = new ArrayList<>();

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("QA Speaker",token);
        firbaseAnalytics(getContext(), "QA Speaker",token);
        QAFetch(token, eventid);

        // use a linear layout manager
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        qaRv.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        // qaRv.setLayoutAnimation(animation);


        qaRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                QAFetch(token, eventid);
            }
        });


        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Selectedspeaker = parent.getItemAtPosition(position).toString();
                SelectedspeakerId = agendaLisQAS.get(position).getSpeakerId();
                QAFetch(token, eventid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//----------------------------------------------------------------------------------
       /* GetUserActivityReport getUserActivityReport = new GetUserActivityReport(getActivity(),token,
                eventid,
                ApiConstant.pageVisited,
                "31",
                "");
        getUserActivityReport.userActivityReport();*/
        //--------------------------------------------------------------------------------------

        return rootView;
    }
    public void QAFetch(String token, String eventid) {
        showProgress();
        mAPIService.QASpeakerFetch(token, eventid).enqueue(new Callback<QASpeakerFetch>() {
            @Override
            public void onResponse(Call<QASpeakerFetch> call, Response<QASpeakerFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (qaRvrefresh.isRefreshing()) {
                        qaRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    showResponse(response);
                } else {
                    if (qaRvrefresh.isRefreshing()) {
                        qaRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QASpeakerFetch> call, Throwable t) {
                Toast.makeText(getContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (qaRvrefresh.isRefreshing()) {
                    qaRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<QASpeakerFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {


            if (response.body().getQuestionSpeakerList().size() != 0 && change == true) {

                change = false;

                agendaLisQAS = response.body().getQuestionSpeakerList();


                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_SPEAKER_PROFILE_PATH, response.body().getProfile_pic_url_path());
                editor.commit();

                for (int i = 0; i < response.body().getQuestionSpeakerList().size(); i++) {
                    list.add(response.body().getQuestionSpeakerList().get(i).getFirstName() + " " + response.body().getQuestionSpeakerList().get(i).getLastName());
                }

                // Creating adapter for spinner

                ArrayAdapter dataAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item, list);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);
            }


            if (SelectedspeakerId != null) {
                ArrayList<SpeakerQuestionList> speakerQuestionLists = new ArrayList<>();
                for (int j = 0; j < response.body().getSpeakerQuestionList().size(); j++) {
                    if (SelectedspeakerId.equalsIgnoreCase(response.body().getSpeakerQuestionList().get(j).getSpeakerId())) {
                        speakerQuestionLists.add(response.body().getSpeakerQuestionList().get(j));
                    }
                }
                if (!(speakerQuestionLists.isEmpty())) {
                    txtEmpty.setVisibility(View.GONE);
                    // linUpper.setBackground(getResources().getDrawable(R.drawable.close_icon));

                    qaSpeakerAdapter = new QASpeakerAdapter(getContext(), speakerQuestionLists, response.body().getQuestionSpeakerList(), QnASpeakerFragment.this, Selectedspeaker);
                    qaSpeakerAdapter.notifyDataSetChanged();
                    qaRv.setAdapter(qaSpeakerAdapter);
                    qaRv.scheduleLayoutAnimation();
                } else {
                    txtEmpty.setVisibility(View.VISIBLE);
                    // linUpper.setBackground(getResources().getDrawable(R.drawable.noqna));

                    qaSpeakerAdapter = new QASpeakerAdapter(getContext(), speakerQuestionLists, response.body().getQuestionSpeakerList(), QnASpeakerFragment.this, Selectedspeaker);
                    qaSpeakerAdapter.notifyDataSetChanged();
                    qaRv.setAdapter(qaSpeakerAdapter);
                    qaRv.scheduleLayoutAnimation();


                }


                /*if (!(response.body().getQuestionSpeakerList().isEmpty())) {
                    qaSpeakerAdapter = new QASpeakerAdapter(getContext(), speakerQuestionLists, response.body().getQuestionSpeakerList(), getContext(), Selectedspeaker);
                    qaSpeakerAdapter.notifyDataSetChanged();
                    qaRv.setAdapter(qaSpeakerAdapter);
                    qaRv.scheduleLayoutAnimation();
                } else {
                    setContentView(R.layout.activity_empty_view);
                    ImageView imageView = findViewById(R.id.back);
                    TextView text_empty = findViewById(R.id.text_empty);
                    text_empty.setText("Q&A not available");
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }*/
            }


        } else {
            Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();

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


    private void showratedialouge() {

        myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.dialouge_msg_layout);
        myDialog.setCancelable(false);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();

        LinearLayout diatitle = myDialog.findViewById(R.id.diatitle);

        diatitle.setBackgroundColor(Color.parseColor(colorActive));

        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);

        ratebtn.setBackgroundColor(Color.parseColor(colorActive));
        cancelbtn.setBackgroundColor(Color.parseColor(colorActive));


        final EditText etmsg = myDialog.findViewById(R.id.etmsg);

        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);
        final TextView title = myDialog.findViewById(R.id.title);
        final ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);

        nametv.setTextColor(Color.parseColor(colorActive));

        title.setText("Ask Question");

        nametv.setText("To " + Selectedspeaker);

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

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etmsg.getText().toString().length() > 0) {

                    String msg = StringEscapeUtils.escapeJava(etmsg.getText().toString());
                    PostQuetion(token, eventid, msg, SelectedspeakerId);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("");
                    builder.setMessage("Please post a question");

                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                    //
                    // Toast.makeText(getContext(), "Enter Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void PostQuetion(final String token, final String eventid, String msg, String selectedspeakerId) {
        mAPIService.QASpeakerPost(token, eventid, msg, selectedspeakerId).enqueue(new Callback<QASpeakerFetch>() {
            @Override
            public void onResponse(Call<QASpeakerFetch> call, Response<QASpeakerFetch> response) {

                if (response.isSuccessful()) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    if (myDialog != null) {
                        if (myDialog.isShowing()) {
                            myDialog.dismiss();
                        }
                    }
                    QAFetch(token, eventid);

                } else {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<QASpeakerFetch> call, Throwable t) {
                Toast.makeText(getContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onContactSelected(SpeakerQuestionList question) {

    }

    @Override
    public void onLikeListener(View v, SpeakerQuestionList question, int position, TextView countTv, ImageView likeIv) {

        if (question.getLikeFlag().equals("1")) {


            likeIv.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_like));
            try {

                int count = Integer.parseInt(question.getTotalLikes());

                if (count > 0) {
                    count = count - 1;
                    countTv.setText(count + " Likes");

                } else {
                    countTv.setText("0" + " Likes");
                }

//            QAFetch(token,eventid);
                QALike(token, eventid, question.getId(), question.getSpeakerId());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            likeIv.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_afterlike));
            likeIv.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);

            QALike(token, eventid, question.getId(), question.getSpeakerId());


            try {

                int count = Integer.parseInt(question.getTotalLikes());


                count = count + 1;
                countTv.setText(count + " Likes");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void QALike(String token, String eventid, String questionid, String sessionid) {
        mAPIService.QASpeakerLike(token, eventid, questionid, sessionid).enqueue(new Callback<QASpeakerFetch>() {
            @Override
            public void onResponse(Call<QASpeakerFetch> call, Response<QASpeakerFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    showLikeResponse(response);
                } else {

                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<QASpeakerFetch> call, Throwable t) {
                Toast.makeText(getContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void showLikeResponse(Response<QASpeakerFetch> response) {

        if (response.body().getStatus().equalsIgnoreCase("success")) {
//            Toast.makeText(getContext(),response.message(),Toast.LENGTH_SHORT).show();
            ArrayList<SpeakerQuestionList> speakerQuestionLists = new ArrayList<>();
            for (int j = 0; j < response.body().getSpeakerQuestionList().size(); j++) {
                if (SelectedspeakerId.equalsIgnoreCase(response.body().getSpeakerQuestionList().get(j).getSpeakerId())) {
                    speakerQuestionLists.add(response.body().getSpeakerQuestionList().get(j));
                }
            }
            if (!(speakerQuestionLists.isEmpty())) {
                txtEmpty.setVisibility(View.GONE);
                //  linUpper.setBackground(getResources().getDrawable(R.drawable.close_icon));

                qaSpeakerAdapter = new QASpeakerAdapter(getContext(), speakerQuestionLists, response.body().getQuestionSpeakerList(), QnASpeakerFragment.this, Selectedspeaker);
                qaSpeakerAdapter.notifyDataSetChanged();
                qaRv.setAdapter(qaSpeakerAdapter);
                qaRv.scheduleLayoutAnimation();
            } else {
                txtEmpty.setVisibility(View.VISIBLE);
                // linUpper.setBackground(getResources().getDrawable(R.drawable.noqna));

                qaSpeakerAdapter = new QASpeakerAdapter(getContext(), speakerQuestionLists, response.body().getQuestionSpeakerList(), QnASpeakerFragment.this, Selectedspeaker);
                qaSpeakerAdapter.notifyDataSetChanged();
                qaRv.setAdapter(qaSpeakerAdapter);
                qaRv.scheduleLayoutAnimation();

            }


           /* qaSpeakerAdapter = new QASpeakerAdapter(getContext(), speakerQuestionLists, response.body().getQuestionSpeakerList(), getContext(), Selectedspeaker);
            qaSpeakerAdapter.notifyDataSetChanged();
            qaRv.setAdapter(qaSpeakerAdapter);*/
        } else {
            Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }
   
}
