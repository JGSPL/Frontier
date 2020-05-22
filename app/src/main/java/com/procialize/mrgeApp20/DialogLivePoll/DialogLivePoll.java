package com.procialize.mrgeApp20.DialogLivePoll;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.procialize.mrgeApp20.Activity.PollDetailActivity;
import com.procialize.mrgeApp20.Adapter.PollGraphAdapter;
import com.procialize.mrgeApp20.Adapter.PollNewAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.CustomTools.CustomViewPager;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.DialogLivePoll.adapter.LivePollDialogAdapter;
import com.procialize.mrgeApp20.DialogQuiz.adapter.QuizPagerDialogAdapter;
import com.procialize.mrgeApp20.GetterSetter.LivePollFetch;
import com.procialize.mrgeApp20.GetterSetter.LivePollList;
import com.procialize.mrgeApp20.GetterSetter.LivePollOptionList;
import com.procialize.mrgeApp20.GetterSetter.LivePollSubmitFetch;
import com.procialize.mrgeApp20.GetterSetter.Quiz;
import com.procialize.mrgeApp20.GetterSetter.QuizFolder;
import com.procialize.mrgeApp20.GetterSetter.QuizOptionList;
import com.procialize.mrgeApp20.Parser.QuizFolderParser;
import com.procialize.mrgeApp20.Parser.QuizOptionParser;
import com.procialize.mrgeApp20.Parser.QuizParser;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.MyApplication;
import com.procialize.mrgeApp20.Utility.ServiceHandler;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class DialogLivePoll {
    static BottomSheetDialog dialog, Detaildialog;
    private ProgressDialog pDialog;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    APIService mAPIService;
    SessionManager sessionManager;
    Context context2;
    private QuizFolderParser quizFolderParser;
    private ArrayList<QuizOptionList> quizOptionList = new ArrayList<QuizOptionList>();
    public String Jsontr;
    private ArrayList<QuizFolder> quizFolders = new ArrayList<QuizFolder>();
    public static MyApplication appDelegate;
    List<LivePollOptionList> totalOptionLists;
    List<LivePollOptionList> optionLists = new ArrayList<>();
    List<LivePollList> pollLists;
    LivePollList pollListsNew;

    //QuizDetail

    // Session Manager Class
    private SessionManager session;

    // Access Token Variable
    private String accessToken, event_id;

    public static String quiz_question_id;
    private String quiz_options_id;

    private String quizQuestionUrl = "";
    CountDownTimer timercountdown;
    private ConnectionDetector cd;
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    boolean[] timerProcessing = {false};
    boolean[] timerStarts = {false};
    private ApiConstant constant = new ApiConstant();

    public static ListView recyclerView;
//    private QuizNewAdapter adapter;

    private QuizParser quizParser;
    RelativeLayout relative;
    private QuizOptionParser quizOptionParser;
    public static Button submit, btnNext;
    ImageView headerlogoIv;
    TextView questionTv, txt_time,txtHeaderQ;
    CustomViewPager pager;
    QuizPagerDialogAdapter pagerAdapter;
    LinearLayoutManager recyclerLayoutManager;
    private static LinearLayout layoutHolder;
    public static LinearLayoutManager llm;
    public static int count1 = 1;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    int count;
    boolean flag = true;
    boolean flag1 = true;
    boolean flag2 = true;
    public static boolean submitflag = false;
    Timer timer;
    public int time = 10;
    public static int countpage = 1;
    ViewGroup viewGroup;
    String questionId,replyFlag;
    List<RadioButton> radios;
    public void welcomeLivePollDialog (Context context){

       // dialog = new BottomSheetDialog(context);
        dialog =new BottomSheetDialog(context,R.style.SheetDialog);
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
        CardView Quizcard  = dialog.findViewById(R.id.Quizcard);
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
                fetchPoll(accessToken,eventid);
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    public void livePollDetailDialog (Context context ){

        // dialog = new BottomSheetDialog(context);
        /*Detaildialog =new BottomSheetDialog(context,R.style.SheetDialog);

        Detaildialog.setContentView(R.layout.botom_live_poll_detail);
        //Detaildialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Detaildialog.getWindow().setDimAmount(0);*/

        Detaildialog =new BottomSheetDialog(context,R.style.SheetDialog);
        Detaildialog.setContentView(R.layout.botom_live_poll_detail);
        Detaildialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Detaildialog.getWindow().setDimAmount(0);

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

        db = procializeDB.getReadableDatabase();


        // Session Manager

        cd = new ConnectionDetector(context);



        submit = (Button) Detaildialog.findViewById(R.id.submit);
        btnNext = (Button) Detaildialog.findViewById(R.id.btnNext);
        txt_time = (TextView) Detaildialog.findViewById(R.id.txt_time);

//        btnNext.setOnClickListener(this);

        recyclerView = (ListView) Detaildialog.findViewById(R.id.quiz_list);
        questionTv = (TextView) Detaildialog.findViewById(R.id.questionTv);
        radios = new ArrayList<RadioButton>();
        questionTv.setText(pollListsNew.getQuestion());
        questionId = pollListsNew.getId();
        replyFlag = pollListsNew.getReplied();
        if (totalOptionLists.size() != 0) {
            for (int i = 0; i < totalOptionLists.size(); i++) {

                if (totalOptionLists.get(i).getLivePollId().equalsIgnoreCase(pollListsNew.getId())) {
                    //Count = Count + Integer.parseInt(pollListsNew.getTotalUser());
                    optionLists.add(totalOptionLists.get(i));
                }
            }
        }

        if (optionLists.size() != 0) {
            viewGroup = (RadioGroup) Detaildialog.findViewById(R.id.radiogroup);
            addRadioButtons(optionLists.size() + 1);
        }

        //recyclerView.setLayoutFrozen(true);
      /*  questionTv.setBackgroundColor(Color.parseColor(colorActive));
        btnNext.setBackgroundColor(Color.parseColor(colorActive));
        submit.setBackgroundColor(Color.parseColor(colorActive));*/

//        quizNameList.setNestedScrollingEnabled(false);
        //recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
       // llm = (LinearLayoutManager) recyclerView.getLayoutManager();


submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (replyFlag.equalsIgnoreCase("1")) {
            Toast.makeText(context2, "You Already Submited This Poll", Toast.LENGTH_SHORT).show();

        } else {
            if (quiz_options_id != null) {
                LivePollSubmitFetch(accessToken, eventid, questionId, quiz_options_id);
            } else {
                Toast.makeText(context2, "Please select something", Toast.LENGTH_SHORT).show();
            }
        }
    }
});


        pager = Detaildialog.findViewById(R.id.pager);
       /*recyclerView.setAnimationCacheEnabled(true);
       recyclerView.setDrawingCacheEnabled(true);
       recyclerView.hasFixedSize();*/

        Detaildialog.show();
    }

    public void addRadioButtons(int number) {

        viewGroup.removeAllViewsInLayout();

        /*
         * String[] color = { "#2196F3", "#00BCD4", "#FF5722", "#8BC34A",
         * "#FF9800", "#1B5E20" };
         */

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

    public void fetchPoll(String token, String eventid) {
        //showProgress();
        mAPIService.LivePollFetch(token, eventid).enqueue(new Callback<LivePollFetch>() {
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
            for(int i=0;i<pollLists.size();i++) {
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

        mAPIService.LivePollSubmitFetch(token, eventid, pollid, polloptionid).enqueue(new Callback<LivePollSubmitFetch>() {
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
            if (response.body().getLivePollList().size() != 0) {/*
                pollGraph.setVisibility(View.VISIBLE);
                AlloptionLists.clear();
                AlloptionLists = response.body().getLivePollOptionList();*/
               /* if (AlloptionLists.size() != 0) {
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
                        }
                    } else {

                    }
                }*/
            }
        } else {
            Toast.makeText(context2, response.message(), Toast.LENGTH_SHORT).show();
        }
    }

    public void thankYouDialog (Context context){

        // dialog = new BottomSheetDialog(context);
        dialog =new BottomSheetDialog(context,R.style.SheetDialog);
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
        CardView Quizcard  = dialog.findViewById(R.id.Quizcard);
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
                fetchPoll(accessToken,eventid);
                dialog.dismiss();

            }
        });

        dialog.show();
    }
}
