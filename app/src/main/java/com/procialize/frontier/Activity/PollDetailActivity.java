package com.procialize.frontier.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.frontier.Adapter.PollGraphAdapter;
import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.GetterSetter.LivePollFetch;
import com.procialize.frontier.GetterSetter.LivePollOptionList;
import com.procialize.frontier.GetterSetter.LivePollSubmitFetch;
import com.procialize.frontier.InnerDrawerActivity.NotificationActivity;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.Util;
import com.procialize.frontier.util.GetUserActivityReport;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.frontier.Utility.Util.setNotification;
import static com.procialize.frontier.Utility.Utility.setgradientDrawable;

public class PollDetailActivity extends AppCompatActivity implements View.OnClickListener {

    String questionId, question, replyFlag, quiz_options_id, show_result;
    List<LivePollOptionList> optionLists;
    List<LivePollOptionList> AlloptionLists;
    TextView questionTv, test;
    RadioGroup ll;
    Button subBtn, PollBtn;
    ProgressBar progressBar;
    String selected;
    int Count;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    ViewGroup viewGroup;
    List<RadioButton> radios;
    String token, colorActive;
    LinearLayout linView;
    RecyclerView pollGraph;
    ImageView headerlogoIv;
    private APIService mAPIService;
    RelativeLayout relative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_detail);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(PollDetailActivity.this, LivePollActivity.class);
                startActivity(intent);*/
                finish();
            }
        });
     //   toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        ImageView notificationlogoIv = findViewById(R.id.notificationlogoIv);
        notificationlogoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(main);
                finish();
            }
        });

        pollGraph = findViewById(R.id.pollGraph);
        relative = findViewById(R.id.relative);
        test = findViewById(R.id.test);

        TextView title = findViewById(R.id.title);
        title.setTextColor(Color.parseColor(colorActive));

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        pollGraph.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        pollGraph.setLayoutAnimation(animation);
        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            relative.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            relative.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        optionLists = new ArrayList<>();
        mAPIService = ApiUtils.getAPIService();

        questionTv = findViewById(R.id.questionTv);
        subBtn = findViewById(R.id.subBtn);
        subBtn.setOnClickListener(this);



        GradientDrawable shape = setgradientDrawable(5, colorActive);

        subBtn.setBackground(shape);

        PollBtn = findViewById(R.id.PollBtn);
        PollBtn.setBackground(shape);

//        questionTv.setTextColor(Color.parseColor(colorActive));
        progressBar = findViewById(R.id.progressBar);


        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);

        radios = new ArrayList<RadioButton>();
        try {
            questionId = getIntent().getExtras().getString("id");
            question = getIntent().getExtras().getString("question");
            replyFlag = getIntent().getExtras().getString("replied");
            show_result = getIntent().getExtras().getString("show_result");
            AlloptionLists = (List<LivePollOptionList>) getIntent().getSerializableExtra("optionlist");

        } catch (Exception e) {
            e.printStackTrace();
        }
        PollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (replyFlag != null) {
            if (replyFlag.equalsIgnoreCase("1")) {
                subBtn.setVisibility(View.GONE);
            } else {
                subBtn.setVisibility(View.VISIBLE);
            }
        }

        questionTv.setText(StringEscapeUtils.unescapeJava(question));

        if (AlloptionLists.size() != 0) {
            for (int i = 0; i < AlloptionLists.size(); i++) {

                if (AlloptionLists.get(i).getLivePollId().equalsIgnoreCase(questionId)) {
                    Count = Count + Integer.parseInt(AlloptionLists.get(i).getTotalUser());
                    optionLists.add(AlloptionLists.get(i));
                }
            }
        }


        if (optionLists.size() != 0) {

            viewGroup = (RadioGroup) findViewById(R.id.radiogroup);
            // viewGroup.setOnCheckedChangeListener(this);

            addRadioButtons(optionLists.size() + 1);

            if (viewGroup.isSelected()) {
                for (int i = 0; i < optionLists.size(); i++) {
                    test.setText(StringEscapeUtils.unescapeJava(optionLists.get(i).getOption()));
                    if (optionLists.get(i).getOption().equalsIgnoreCase(test.getText().toString())) {

                        quiz_options_id = optionLists.get(i)
                                .getOptionId();

                    }

                }
            }
        } else {
            Toast.makeText(PollDetailActivity.this, "Select Option", Toast.LENGTH_SHORT).show();
        }


        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------
        //----------------------------------------------------------------------------------------------
        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this, token,
                eventid,
                ApiConstant.pageVisited,
                "29",
                "");
        getUserActivityReport.userActivityReport();
        //---------------------------------------------------------------
    }


    public void LivePollSubmitFetch(String token, String eventid, String pollid, String polloptionid) {
        showProgress();
        mAPIService.LivePollSubmitFetch(token, eventid, pollid, polloptionid).enqueue(new Callback<LivePollSubmitFetch>() {
            @Override
            public void onResponse(Call<LivePollSubmitFetch> call, Response<LivePollSubmitFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    dismissProgress();
                    showResponse(response);
                } else {

                    dismissProgress();
                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LivePollSubmitFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();

                dismissProgress();
            }
        });
    }

    public void showResponse(Response<LivePollSubmitFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            optionLists.clear();

            //   fetchPoll(token, eventid);
            if (response.body().getLivePollList().size() != 0) {
                pollGraph.setVisibility(View.VISIBLE);
                AlloptionLists.clear();
                AlloptionLists = response.body().getLivePollOptionList();
                if (AlloptionLists.size() != 0) {
                    for (int i = 0; i < AlloptionLists.size(); i++) {

                        if (AlloptionLists.get(i).getLivePollId().equalsIgnoreCase(questionId)) {
                            Count = Count + Integer.parseInt(AlloptionLists.get(i).getTotalUser());
                            optionLists.add(AlloptionLists.get(i));
                        }
                    }

                    replyFlag = "1";
                    subBtn.setVisibility(View.GONE);

                    if (show_result.equalsIgnoreCase("0")) {
                        if (optionLists.size() != 0) {


                            viewGroup.setVisibility(View.GONE);
                            PollGraphAdapter pollAdapter = new PollGraphAdapter(this, optionLists, questionId);
                            pollAdapter.notifyDataSetChanged();
                            pollGraph.setAdapter(pollAdapter);
                            pollGraph.scheduleLayoutAnimation();
                            subBtn.setVisibility(View.GONE);
                            PollBtn.setVisibility(View.VISIBLE);

                        }
                       /* viewGroup = (RadioGroup) findViewById(R.id.radiogroup);
                        // viewGroup.setOnCheckedChangeListener(this);

                        addRadioButtons(optionLists.size() + 1);


                    for (int i = 0; i < optionLists.size(); i++) {

                        if (optionLists
                                .get(0)
                                .getOption()
                                .equalsIgnoreCase(
                                        optionLists.get(i).getOption())) {

                            quiz_options_id = optionLists.get(i)
                                    .getOptionId();

                        }

                    }*/

                    } else {
                        /*Intent intent = new Intent(PollDetailActivity.this, LivePollActivity.class);
                        startActivity(intent);*/
                        finish();

                    }

                }

            }
            Toast.makeText(PollDetailActivity.this,
                    response.body().getMsg(), Toast.LENGTH_SHORT)
                    .show();


        } else {
            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();

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


    public void addRadioButtons(int number) {

        viewGroup.removeAllViewsInLayout();

        /*
         * String[] color = { "#2196F3", "#00BCD4", "#FF5722", "#8BC34A",
         * "#FF9800", "#1B5E20" };
         */

        String[] color = {"#112F7A", "#0E73BA", "#04696E", "#00A89C", "#000000", "#4D4D4D", "#949494", "#112F7A", "#0E73BA", "#04696E", "#00A89C", "#000000"};

        Float totalUser = 0.0f;

//        ArrayList<LivePollOptionList> tempOptionListnew;
//
//        if (replyFlag.equalsIgnoreCase("result")) {
//
//            if (optionLists.size() > 0) {
//                tempOptionListnew = quizOptionListFinal;
//            } else {
//                tempOptionListnew = quizSpecificOptionListnew;
//            }
//
//        } else {
//
//            tempOptionListnew = quizSpecificOptionListnew;
//
//        }

        for (int k = 0; k < optionLists.size(); k++) {

            if (optionLists.get(k).getLivePollId()
                    .equalsIgnoreCase(questionId)) {
                totalUser = (totalUser + Float.parseFloat(optionLists
                        .get(k).getTotalUser()));

            }

        }

        // quiz_layout.setBackgroundColor(Color.parseColor("#1B5E20"));
        if (replyFlag.equalsIgnoreCase("1")) {
            if (show_result.equalsIgnoreCase("0")) {
                viewGroup.setVisibility(View.GONE);
                PollGraphAdapter pollAdapter = new PollGraphAdapter(this, optionLists, questionId);
                pollAdapter.notifyDataSetChanged();
                pollGraph.setAdapter(pollAdapter);
                pollGraph.scheduleLayoutAnimation();
                subBtn.setVisibility(View.GONE);
                PollBtn.setVisibility(View.VISIBLE);
            } else {
               /* Intent intent = new Intent(PollDetailActivity.this, LivePollActivity.class);
                startActivity(intent);*/
                finish();
            }

        } else {

            pollGraph.setVisibility(View.GONE);
            viewGroup.setVisibility(View.VISIBLE);

            for (int row = 0; row < 1; row++) {

                for (int i = 1; i < number; i++) {

                    LinearLayout ll = new LinearLayout(this);

                    LinearLayout l3 = new LinearLayout(this);
                    FrameLayout fl = new FrameLayout(this);

                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.setPadding(5, 10, 5, 10);

                    LinearLayout ll2 = new LinearLayout(this);
                    ll2.setOrientation(LinearLayout.HORIZONTAL);


                    LinearLayout.LayoutParams rprms, rprmsRdBtn, rpms2;

                    RadioButton rdbtn = new RadioButton(this);
                    rdbtn.setId((row * 2) + i);
                    rdbtn.setText(StringEscapeUtils.unescapeJava(optionLists.get(i - 1).getOption()));
                    rdbtn.setTextColor(Color.parseColor("#898989"));
//                rdbtn.setTypeface(typeFace);
                    rdbtn.setOnClickListener(this);


//                    if (replyFlag.equalsIgnoreCase("1")) {
//
//                        rdbtn.setClickable(false);
//                        rdbtn.setChecked(false);
//
//                    } else {
//
//                        if (i == 1)
//                            rdbtn.setChecked(true);
//                    }

                    radios.add(rdbtn);

                    // rdbtn.setBackgroundResource(R.drawable.edit_background);

                    rprms = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    rprms.setMargins(5, 5, 5, 5);

                    Float weight = 1.0f;

                    if (replyFlag.equalsIgnoreCase("1")) {

                        // if (quizSpecificOptionListnew.get(i -
                        // 1).getLive_poll_id()
                        // .equalsIgnoreCase(questionId)) {
                        ll2.setBackgroundColor(Color.parseColor(color[i]));

                        weight = ((Float.parseFloat(optionLists.get(i - 1)
                                .getTotalUser()) / totalUser) * 100);

                        // }

                    } else {

                        weight = 100.0f;
                    }

                    rpms2 = new LinearLayout.LayoutParams(0,
                            ViewGroup.LayoutParams.MATCH_PARENT, weight);
                    rpms2.setMargins(5, 5, 5, 5);


//                    ll.setBackgroundResource(R.drawable.agenda_bg);
                    ll.setWeightSum(weight);
                    ll.setLayoutParams(rprms);

                    l3.setLayoutParams(rprms);
                    ll.setPadding(5, 10, 5, 10);
                    l3.setWeightSum(weight);

                    // ll2.setBackgroundColor(Color.parseColor(color[i]));
                    ll2.setLayoutParams(rpms2);


                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    params.gravity = Gravity.CENTER;
                    rprmsRdBtn = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    rprms.setMargins(5, 5, 5, 5);

                    rdbtn.setLayoutParams(rprmsRdBtn);
                    if (Build.VERSION.SDK_INT >= 21) {

                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{

                                        new int[]{-android.R.attr.state_checked}, //disabled
                                        new int[]{android.R.attr.state_checked} //enabled
                                },
                                new int[]{

                                        Color.parseColor("#4d4d4d")//disabled
                                        , Color.parseColor(colorActive)//enabled

                                }
                        );


                        rdbtn.setButtonTintList(colorStateList);//set the color tint list
                        rdbtn.invalidate(); //could not be necessary
                    }
//                    rdbtn.setButtonDrawable(R.drawable.radio_buttontoggle_first);
                    rdbtn.setBackgroundResource(R.drawable.livepollback);
                    l3.addView(ll2, rpms2);


                    fl.addView(l3, rprms);
                    fl.addView(rdbtn, rprmsRdBtn);

                    // ll2.addView(rdbtn, rprmsRdBtn);
                    ll.addView(fl, params);

                    viewGroup.addView(ll, rprms);
                    viewGroup.invalidate();
                }

            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v == subBtn) {
            if (replyFlag.equalsIgnoreCase("1")) {
                Toast.makeText(getApplicationContext(), "You Already Submited This Poll", Toast.LENGTH_SHORT).show();

            } else {
                if (quiz_options_id != null) {
                    LivePollSubmitFetch(token, eventid, questionId, quiz_options_id);

                } else {
                    Toast.makeText(getApplicationContext(), "Please select something", Toast.LENGTH_SHORT).show();

                }
            }

        } else {

            String option = ((RadioButton) v).getText().toString();

            for (RadioButton radio : radios) {
                if (!radio.getText().equals(option)) {

                    radio.setChecked(false);

                }

            }

            for (int i = 0; i < optionLists.size(); i++) {
                test.setText(StringEscapeUtils.unescapeJava(optionLists.get(i).getOption()));
                if (option.equalsIgnoreCase(test.getText().toString())) {

                    quiz_options_id = optionLists.get(i)
                            .getOptionId();
                }

            }

        }
    }

    public void fetchPoll(String token, String eventid) {
        showProgress();
        mAPIService.LivePollFetch(token, eventid).enqueue(new Callback<LivePollFetch>() {
            @Override
            public void onResponse(Call<LivePollFetch> call, Response<LivePollFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    dismissProgress();
                    showResponseLiveList(response);
                } else {

                    dismissProgress();
                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LivePollFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();

                dismissProgress();

            }
        });
    }

    public void showResponseLiveList(Response<LivePollFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getLivePollList().size() != 0) {
//            PollAdapter pollAdapter = new PollAdapter(this, response.body().getLivePollList(), response.body().getLivePollOptionList(), this);
//            pollAdapter.notifyDataSetChanged();
//            pollGraph.setAdapter(pollAdapter);
//            pollGraph.scheduleLayoutAnimation();
            AlloptionLists.clear();

            AlloptionLists = response.body().getLivePollOptionList();
            if (AlloptionLists.size() != 0) {
                for (int i = 0; i < AlloptionLists.size(); i++) {

                    if (AlloptionLists.get(i).getLivePollId().equalsIgnoreCase(questionId)) {
                        Count = Count + Integer.parseInt(AlloptionLists.get(i).getTotalUser());
                        optionLists.add(AlloptionLists.get(i));
                    }
                }

                replyFlag = "1";
                subBtn.setVisibility(View.GONE);
                if (optionLists.size() != 0) {

                    viewGroup = (RadioGroup) findViewById(R.id.radiogroup);
                    // viewGroup.setOnCheckedChangeListener(this);

                    addRadioButtons(optionLists.size() + 1);

/*
                    for (int i = 0; i < optionLists.size(); i++) {

                        if (optionLists
                                .get(0)
                                .getOption()
                                .equalsIgnoreCase(
                                        optionLists.get(i).getOption())) {

                            quiz_options_id = optionLists.get(i)
                                    .getOptionId();

                        }

                    }
*/


                }
               /* viewGroup.setVisibility(View.GONE);
                PollGraphAdapter pollAdapter = new PollGraphAdapter(this,optionLists,questionId );
                pollAdapter.notifyDataSetChanged();
                pollGraph.setAdapter(pollAdapter);
                pollGraph.scheduleLayoutAnimation();*/
                startActivity(getIntent());


            }

        } else {
            Toast.makeText(getApplicationContext(), "No Poll Available", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent(PollDetailActivity.this, LivePollActivity.class);
        startActivity(intent);*/
        finish();
    }
}
