package com.procialize.mrgeApp20.DialogLivePoll;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.procialize.mrgeApp20.Adapter.PollGraphAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.CustomTools.CustomViewPager;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.DialogQuiz.adapter.QuizPagerDialogAdapter;
import com.procialize.mrgeApp20.GetterSetter.LivePollFetch;
import com.procialize.mrgeApp20.GetterSetter.LivePollList;
import com.procialize.mrgeApp20.GetterSetter.LivePollOptionList;
import com.procialize.mrgeApp20.GetterSetter.LivePollSubmitFetch;
import com.procialize.mrgeApp20.GetterSetter.QuizFolder;
import com.procialize.mrgeApp20.GetterSetter.QuizOptionList;
import com.procialize.mrgeApp20.Parser.QuizFolderParser;
import com.procialize.mrgeApp20.Parser.QuizOptionParser;
import com.procialize.mrgeApp20.Parser.QuizParser;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.MyApplication;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class DialogLivePoll implements View.OnClickListener{
    public static MyApplication appDelegate;
    public static String quiz_question_id;
    public static ListView recyclerView;
    public static Button submit, btnNext;
    public static LinearLayoutManager llm;
    public static int count1 = 1;
    public static boolean submitflag = false;
    public static int countpage = 1;
    static BottomSheetDialog dialog, Detaildialog, dialogThankYou, dialogResult;
    private static LinearLayout layoutHolder;
    public String Jsontr;
    public int time = 10;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    APIService mAPIService;
    SessionManager sessionManager;
    int Count;
    //QuizDetail
    Context context2;
    List<LivePollOptionList> totalOptionLists;
    List<LivePollOptionList> optionLists = new ArrayList<>();
    List<LivePollList> pollLists;
    LivePollList pollListsNew;
    CountDownTimer timercountdown;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    boolean[] timerProcessing = {false};
    boolean[] timerStarts = {false};
    RelativeLayout relative;
    ImageView headerlogoIv;
    TextView questionTv, txt_time, txtHeaderQ, test;
    CustomViewPager pager;
    //    private QuizNewAdapter adapter;
    QuizPagerDialogAdapter pagerAdapter;
    LinearLayoutManager recyclerLayoutManager;
    boolean flag = true;
    boolean flag1 = true;
    boolean flag2 = true;
    Timer timer;
    ViewGroup viewGroup;
    String questionId, replyFlag;
    List<RadioButton> radios;
    private ProgressDialog pDialog;
    private QuizFolderParser quizFolderParser;
    private ArrayList<QuizOptionList> quizOptionList = new ArrayList<QuizOptionList>();
    private ArrayList<QuizFolder> quizFolders = new ArrayList<QuizFolder>();
    // Session Manager Class
    private SessionManager session;
    // Access Token Variable
    private String accessToken, event_id, show_result;
    private String quiz_options_id;
    private String quizQuestionUrl = "";
    private ConnectionDetector cd;
    private long startTime = 0L;
    private ApiConstant constant = new ApiConstant();
    private QuizParser quizParser;
    private QuizOptionParser quizOptionParser;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    RecyclerView pollGraph;


    public void welcomeLivePollDialog(Context context) {

        // dialog = new BottomSheetDialog(context);
        dialog = new BottomSheetDialog(context, R.style.SheetDialog);
        dialog.setContentView(R.layout.bottom_live_poll_welcome);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setDimAmount(0);

        context2 = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        appDelegate = (MyApplication) context.getApplicationContext();

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(context);

        HashMap<String, String> user = sessionManager.getUserDetails();
        // token
        accessToken = user.get(SessionManager.KEY_TOKEN);

        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        Button btnQuizStart = dialog.findViewById(R.id.btnQuizStart);
        CardView Quizcard = dialog.findViewById(R.id.Quizcard);
        Quizcard.setBackgroundColor(Color.parseColor("#ffffff"));
        Quizcard.setAlpha(0.8f);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnQuizStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPoll(accessToken, eventid);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void livePollDetailDialog(Context context) {

        // dialog = new BottomSheetDialog(context);
        /*Detaildialog =new BottomSheetDialog(context,R.style.SheetDialog);

        Detaildialog.setContentView(R.layout.botom_live_poll_detail);
        //Detaildialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Detaildialog.getWindow().setDimAmount(0);*/
        Detaildialog = new BottomSheetDialog(context, R.style.SheetDialog);
        Detaildialog.setContentView(R.layout.botom_live_poll_detail);
        Detaildialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Detaildialog.getWindow().setDimAmount(0);
        LinearLayout Quizcard = Detaildialog.findViewById(R.id.Quizcard);
        Quizcard.setBackgroundColor(Color.parseColor("#ffffff"));
        Quizcard.setAlpha(0.8f);

        context2 = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        appDelegate = (MyApplication) context.getApplicationContext();

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(context);

        HashMap<String, String> user = sessionManager.getUserDetails();
        // token
        accessToken = user.get(SessionManager.KEY_TOKEN);
        procializeDB = new DBHelper(context);
        db = procializeDB.getWritableDatabase();

        // Session Manager
        cd = new ConnectionDetector(context);
        submit = (Button) Detaildialog.findViewById(R.id.submit);
        btnNext = (Button) Detaildialog.findViewById(R.id.btnNext);
        txt_time = (TextView) Detaildialog.findViewById(R.id.txt_time);
        test = (TextView) Detaildialog.findViewById(R.id.test);

//        btnNext.setOnClickListener(this);

        pollGraph =Detaildialog. findViewById(R.id.pollGraph);
        relative = Detaildialog.findViewById(R.id.relative);
        test = Detaildialog.findViewById(R.id.test);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        pollGraph.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
        pollGraph.setLayoutAnimation(animation);

        //recyclerView = (ListView) Detaildialog.findViewById(R.id.quiz_list);
        questionTv = (TextView) Detaildialog.findViewById(R.id.questionTv);
        radios = new ArrayList<RadioButton>();
        questionTv.setText(pollListsNew.getQuestion());
        questionId = pollListsNew.getId();
        replyFlag = pollListsNew.getReplied();
        if (totalOptionLists.size() != 0) {
            for (int i = 0; i < totalOptionLists.size(); i++) {
                if (totalOptionLists.get(i).getLivePollId().equalsIgnoreCase(pollListsNew.getId())) {
                    Count = Count + Integer.parseInt(totalOptionLists.get(i).getTotalUser());
                    optionLists.add(totalOptionLists.get(i));
                }
            }
        }

        if (optionLists.size() != 0) {
            viewGroup = (RadioGroup) Detaildialog.findViewById(R.id.radiogroup);
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
        }

        // token
        accessToken = user.get(SessionManager.KEY_TOKEN);
        eventid = prefs.getString("eventid", "1");
        questionId = pollListsNew.getId();

        /*submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (replyFlag.equalsIgnoreCase("1")) {
                    Toast.makeText(context2, "You Already Submited This Poll", Toast.LENGTH_SHORT).show();
                } else {
                    if (quiz_options_id != null) {
                        Detaildialog.dismiss();
                        thankYouDialog(context2, pollListsNew.getQuestion(), questionId, optionLists);
                       // LivePollSubmitFetch(accessToken, eventid, questionId, quiz_options_id);
                    } else {
                        Toast.makeText(context2, "Please select something", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });*/


        pager = Detaildialog.findViewById(R.id.pager);
       /*recyclerView.setAnimationCacheEnabled(true);
       recyclerView.setDrawingCacheEnabled(true);
       recyclerView.hasFixedSize();*/

        Detaildialog.show();
    }
    public void addRadioButtons(int number) {

        viewGroup.removeAllViewsInLayout();


        String[] color = {"#112F7A", "#0E73BA", "#04696E", "#00A89C", "#000000", "#4D4D4D", "#949494", "#112F7A", "#0E73BA", "#04696E", "#00A89C", "#000000"};

        Float totalUser = 0.0f;



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
                PollGraphAdapter pollAdapter = new PollGraphAdapter(context2, optionLists, questionId);
                pollAdapter.notifyDataSetChanged();
                pollGraph.setAdapter(pollAdapter);
                pollGraph.scheduleLayoutAnimation();
                submit.setVisibility(View.GONE);
               // PollBtn.setVisibility(View.VISIBLE);
            } else {

            }

        } else {

            pollGraph.setVisibility(View.GONE);
            viewGroup.setVisibility(View.VISIBLE);

            for (int row = 0; row < 1; row++) {

                for (int i = 1; i < number; i++) {

                    LinearLayout ll = new LinearLayout(context2);

                    LinearLayout l3 = new LinearLayout(context2);
                    FrameLayout fl = new FrameLayout(context2);

                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.setPadding(5, 10, 5, 10);

                    LinearLayout ll2 = new LinearLayout(context2);
                    ll2.setOrientation(LinearLayout.HORIZONTAL);


                    LinearLayout.LayoutParams rprms, rprmsRdBtn, rpms2;

                    RadioButton rdbtn = new RadioButton(context2);
                    rdbtn.setId((row * 2) + i);
                    rdbtn.setText(StringEscapeUtils.unescapeJava(optionLists.get(i - 1).getOption()));
                    rdbtn.setTextColor(Color.BLACK);
//                rdbtn.setTypeface(typeFace);
                    rdbtn.setOnClickListener(this);



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


/*
    public void addRadioButtons(int number) {

        viewGroup.removeAllViewsInLayout();

        */
/*
         * String[] color = { "#2196F3", "#00BCD4", "#FF5722", "#8BC34A",
         * "#FF9800", "#1B5E20" };
         *//*


        String[] color = {"#112F7A", "#0E73BA", "#04696E", "#00A89C", "#000000", "#4D4D4D", "#949494", "#112F7A", "#0E73BA", "#04696E", "#00A89C", "#000000"};

        Float totalUser = 0.0f;


        for (int k = 0; k < optionLists.size(); k++) {

            if (optionLists.get(k).getLivePollId()
                    .equalsIgnoreCase(questionId)) {
                totalUser = (totalUser + Float.parseFloat(optionLists
                        .get(k).getTotalUser()));

            }

        }

        //pollGraph.setVisibility(View.GONE);
        viewGroup.setVisibility(View.VISIBLE);

        for (int row = 0; row < 1; row++) {

            for (int i = 1; i < number; i++) {

                LinearLayout ll = new LinearLayout(context2);

                LinearLayout l3 = new LinearLayout(context2);
                FrameLayout fl = new FrameLayout(context2);

                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setPadding(5, 10, 5, 10);

                LinearLayout ll2 = new LinearLayout(context2);
                ll2.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams rprms, rprmsRdBtn, rpms2;

                RadioButton rdbtn = new RadioButton(context2);
                rdbtn.setId((row * 2) + i);
                rdbtn.setText(StringEscapeUtils.unescapeJava(optionLists.get(i - 1).getOption()));
                rdbtn.setTextColor(Color.BLACK);
//                rdbtn.setTypeface(typeFace);
                // rdbtn.setOnClickListener(context2);

                radios.add(rdbtn);

                rprms = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                rprms.setMargins(5, 5, 5, 5);

                Float weight = 1.0f;

                if (replyFlag.equalsIgnoreCase("1")) {
                    ll2.setBackgroundColor(Color.parseColor(color[i]));

                    weight = ((Float.parseFloat(optionLists.get(i - 1)
                            .getTotalUser()) / totalUser) * 100);
                } else {
                    weight = 100.0f;
                }

                rpms2 = new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.MATCH_PARENT, weight);
                rpms2.setMargins(5, 5, 5, 5);

                ll.setWeightSum(weight);
                ll.setLayoutParams(rprms);

                l3.setLayoutParams(rprms);
                ll.setPadding(5, 10, 5, 10);
                l3.setWeightSum(weight);

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
*/

    public void fetchPoll(String token, String eventid) {
        //showProgress();
        mAPIService.SpotLivePollFetch(token, eventid).enqueue(new Callback<LivePollFetch>() {
            @Override
            public void onResponse(Call<LivePollFetch> call, Response<LivePollFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    showResponse(response);
                } else {

                    Toast.makeText(context2, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LivePollFetch> call, Throwable t) {
                Toast.makeText(context2, "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showResponse(Response<LivePollFetch> response) {

        // specify an adapter (see also next example)
        totalOptionLists = response.body().getLivePollOptionList();
        pollLists = response.body().getLivePollList();
        //empty.setTextColor(Color.parseColor(colorActive));
        if (response.body().getLivePollOptionList().size() != 0) {
            //empty.setVisibility(View.GONE);
            for (int i = 0; i < pollLists.size(); i++) {
                if (pollLists.get(i).getReplied().equalsIgnoreCase("0")) {
                    //if (pollLists.get(i).getShow_result().equalsIgnoreCase("0")) {
                    String id = pollLists.get(i).getId();
                    String question = pollLists.get(i).getQuestion();
                    String replied = pollLists.get(i).getReplied();
                    String show_result = pollLists.get(i).getShow_result();
                    pollListsNew = pollLists.get(i);
                    livePollDetailDialog(context2);
                    return;
                    //}
                }
            }
            /*LivePollDialogAdapter pollAdapter = new LivePollDialogAdapter(context2, response.body().getLivePollList(), response.body().getLivePollOptionList());
            pollAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(pollAdapter);*/
        } else {
            // empty.setVisibility(View.VISIBLE);
        }
    }

    public void LivePollSubmitFetch(String token, String eventid, String pollid, String polloptionid) {

        mAPIService.SpotLivePollSubmit(token, eventid, pollid, polloptionid).enqueue(new Callback<LivePollSubmitFetch>() {
            @Override
            public void onResponse(Call<LivePollSubmitFetch> call, Response<LivePollSubmitFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    showResponseSubmit(response);
                } else {
                    Toast.makeText(context2, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LivePollSubmitFetch> call, Throwable t) {
                Toast.makeText(context2, "Unable to process", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showResponseSubmit(Response<LivePollSubmitFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            optionLists.clear();

            //   fetchPoll(token, eventid);
            if (response.body().getLivePollList().size() != 0) {


                /*
                pollGraph.setVisibility(View.VISIBLE);
                AlloptionLists.clear();
                AlloptionLists = response.body().getLivePollOptionList();*/
                if (totalOptionLists.size() != 0) {
                    for (int i = 0; i < totalOptionLists.size(); i++) {

                        if (totalOptionLists.get(i).getLivePollId().equalsIgnoreCase(questionId)) {
                            Count = Count + Integer.parseInt(totalOptionLists.get(i).getTotalUser());
                            optionLists.add(totalOptionLists.get(i));
                        }
                    }

                    replyFlag = "1";
                    if (optionLists.size() != 0) {
                        viewGroup.setVisibility(View.GONE);
                        thankYouDialog(context2, pollListsNew.getQuestion(), questionId, optionLists);
                    }
                }
            }
        } else {
            Toast.makeText(context2, response.message(), Toast.LENGTH_SHORT).show();
        }
    }

    public void thankYouDialog(Context context, String question, String questionId, List<LivePollOptionList> optionLists) {
        // dialog = new BottomSheetDialog(context);
        dialogThankYou = new BottomSheetDialog(context, R.style.SheetDialog);
        dialogThankYou.setContentView(R.layout.bottom_live_poll_thank_you_dialog);
        dialogThankYou.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogThankYou.getWindow().setDimAmount(0);

        context2 = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        appDelegate = (MyApplication) context.getApplicationContext();

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(context);

        HashMap<String, String> user = sessionManager.getUserDetails();
        // token
        accessToken = user.get(SessionManager.KEY_TOKEN);

        ImageView imgClose = dialogThankYou.findViewById(R.id.imgClose);
        TextView tv_view_result = dialogThankYou.findViewById(R.id.tv_view_result);
        tv_view_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogThankYou.dismiss();
                resultDialog(context2, question,
                        questionId,
                        optionLists);
            }
        });
        //Button btnQuizStart = dialog.findViewById(R.id.btnQuizStart);
        LinearLayout Quizcard = dialogThankYou.findViewById(R.id.Quizcard);
        Quizcard.setBackgroundColor(Color.parseColor("#ffffff"));
        Quizcard.setAlpha(0.8f);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogThankYou.dismiss();
            }
        });
       /* btnQuizStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPoll(accessToken, eventid);
                dialog.dismiss();

            }
        });*/
        dialogThankYou.show();
    }

    public void resultDialog(Context context, String question, String questionId, List<LivePollOptionList> optionLists) {
        dialogResult = new BottomSheetDialog(context, R.style.SheetDialog);
        dialogResult.setContentView(R.layout.bottom_live_poll_result_dialog);
        dialogResult.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogResult.getWindow().setDimAmount(0);

        LinearLayout Quizcard = dialogResult.findViewById(R.id.Quizcard);
        Quizcard.setBackgroundColor(Color.parseColor("#ffffff"));
        Quizcard.setAlpha(0.8f);

        TextView tvQuestion = dialogResult.findViewById(R.id.tvQuestion);
        ImageView imgClose = dialogResult.findViewById(R.id.imgClose);
        RecyclerView pollGraph = dialogResult.findViewById(R.id.pollGraph);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogResult.dismiss();
            }
        });

        tvQuestion.setText(question);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        pollGraph.setLayoutManager(mLayoutManager);
        PollGraphAdapter pollAdapter = new PollGraphAdapter(context, optionLists, questionId);
        pollAdapter.notifyDataSetChanged();
        pollGraph.setAdapter(pollAdapter);
        pollGraph.scheduleLayoutAnimation();

        dialogResult.show();
    }

    @Override
    public void onClick(View v) {
        if (v == submit) {
            if (replyFlag.equalsIgnoreCase("1")) {
                Toast.makeText(context2, "You Already Submited This Poll", Toast.LENGTH_SHORT).show();

            } else {
                if (quiz_options_id != null) {
                    LivePollSubmitFetch(accessToken, eventid, questionId, quiz_options_id);

                } else {
                    Toast.makeText(context2, "Please select something", Toast.LENGTH_SHORT).show();

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
}
