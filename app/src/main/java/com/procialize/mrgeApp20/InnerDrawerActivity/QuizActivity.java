package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.procialize.mrgeApp20.Adapter.QuizPagerAdapter;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.CustomTools.CustomViewPager;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.Quiz;
import com.procialize.mrgeApp20.GetterSetter.QuizOptionList;
import com.procialize.mrgeApp20.Parser.QuizOptionParser;
import com.procialize.mrgeApp20.Parser.QuizParser;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.MyApplication;
import com.procialize.mrgeApp20.Utility.ServiceHandler;
import com.procialize.mrgeApp20.Utility.Util;
import com.procialize.mrgeApp20.util.GetUserActivityReport;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import static com.procialize.mrgeApp20.Utility.Util.setNotification;

public class QuizActivity extends AppCompatActivity implements OnClickListener {

    private ProgressDialog pDialog;
    // Session Manager Class
    private SessionManager session;

    // Access Token Variable
    private String accessToken, event_id, colorActive;

    public static String quiz_question_id;
    private String quiz_options_id;

    private String quizQuestionUrl = "";
    private String getQuizUrl = "";
    CountDownTimer timercountdown;
    private ConnectionDetector cd;
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    boolean[] timerProcessing = {false};
    boolean[] timerStarts = {false};
    private ApiConstant constant = new ApiConstant();

    public static RecyclerView quizNameList;
//    private QuizNewAdapter adapter;

    private QuizParser quizParser;
    private ArrayList<Quiz> quizList = new ArrayList<Quiz>();
    RelativeLayout relative;
    private QuizOptionParser quizOptionParser;
    private ArrayList<QuizOptionList> quizOptionList = new ArrayList<QuizOptionList>();
    QuizPagerAdapter pagerAdapter;
    public static MyApplication appDelegate;
    public static String foldername = "null",strTimer="0";
    public static Button submit, btnNext;
    ImageView headerlogoIv;
    TextView questionTv, txt_time,txtSkip;
    public static TextView txt_count;
    CustomViewPager pager;
    //    QuizPagerAdapter pagerAdapter;
    LinearLayoutManager recyclerLayoutManager;
    String MY_PREFS_NAME = "ProcializeInfo";
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
    public int time = 10,timerForQuiz;
    public static int countpage = 1;
    ProgressBar progressBarCircle;
    TextView  textViewTime;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 1;
                pagerAdapter.selectopt = 0;
                try {
                    timercountdown.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }

               /* Intent intent = new Intent(QuizActivity.this, FolderQuizActivity.class);
                startActivity(intent);
                finish();*/
               onBackPressed();

            }
        });

        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);


        quizQuestionUrl = constant.baseUrl + constant.quizsubmit ;
        procializeDB = new DBHelper(QuizActivity.this);
        db = procializeDB.getWritableDatabase();

        db = procializeDB.getReadableDatabase();
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        appDelegate = (MyApplication) getApplicationContext();

        foldername = getIntent().getExtras().getString("folder");
        strTimer = getIntent().getExtras().getString("timer");
        timerForQuiz = Integer.parseInt(getIntent().getExtras().getString("timer"));
        time = timerForQuiz;
        time = Integer.parseInt(strTimer);
        // Session Manager
        session = new SessionManager(getApplicationContext());
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        event_id = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        cd = new ConnectionDetector(getApplicationContext());

        // Initialize Get Quiz URL
        getQuizUrl = constant.baseUrl + constant.quizlist;


        submit = (Button) findViewById(R.id.submit);
        btnNext = (Button) findViewById(R.id.btnNext);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txtSkip = (TextView) findViewById(R.id.txtSkip);
        txtSkip.setOnClickListener(this);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);

        submit.setOnClickListener(this);
//        btnNext.setOnClickListener(this);

        quizNameList = (RecyclerView) findViewById(R.id.quiz_list);
        questionTv = (TextView) findViewById(R.id.questionTv);
        txt_count = (TextView) findViewById(R.id.txt_count);
        relative = (RelativeLayout) findViewById(R.id.relative);
        questionTv.setText(StringEscapeUtils.unescapeJava(foldername));
        quizNameList.setLayoutFrozen(true);
        //questionTv.setBackgroundColor(Color.parseColor(colorActive));
        btnNext.setBackgroundColor(Color.parseColor(colorActive));
        submit.setBackgroundColor(Color.parseColor(colorActive));
        //txt_count.setTextColor(Color.parseColor(colorActive));

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            relative.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            relative.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }
//        quizNameList.setNestedScrollingEnabled(false);
        quizNameList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        llm = (LinearLayoutManager) quizNameList.getLayoutManager();


        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                int option = pagerAdapter.getSelectedOption();

//                String correctOption = quizList.get(llm.findLastVisibleItemPosition()).getCorrect_answer();
                int i = pagerAdapter.getItemCount();

                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                countpage = pager.getCurrentItem();
                if (quizList.size() == pager.getCurrentItem() + 1) {
                    btnNext.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    submitflag = true;
                    //                    txt_time.setText(String.format(Locale.getDefault(), "%d", time));
//                    if (time > 0)
//                        time -= 1;
                } else if (quizList.size() >= pager.getCurrentItem() + 1) {
                    btnNext.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                    //time = 0;
                    time = timerForQuiz;
                    timercountdown.cancel();
                    timercountdown.start();
                    txt_time.setText("" + ":" + checkdigit(time));

//                    txt_time.setText(String.format(Locale.getDefault(), "%d", time));
//                    if (time > 0)
//                        time -= 1;

                }
            }
        });
        pager = findViewById(R.id.pager);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                time = timerForQuiz;
                txt_count.setText("Questions " + (position+1) + "/" + quizList.size());
               /* timercountdown.cancel();
                timercountdown.start();*/
                //startCountDownTimer(time * 1000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        quizNameList.setAnimationCacheEnabled(true);
        quizNameList.setDrawingCacheEnabled(true);
        quizNameList.hasFixedSize();


        if (cd.isConnectingToInternet()) {
            new getQuizList().execute();
        } else {

            Toast.makeText(QuizActivity.this, "No internet connection",
                    Toast.LENGTH_SHORT).show();
        }

        progressBarCircle.setMax(time);
        progressBarCircle.setProgress(time);

        timercountdown = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (time == 0) {
                    time = timerForQuiz;
                    //time = 10;
                }

                txt_time.setText("" + ":" + checkdigit(time));
                textViewTime.setText(String.valueOf(time));
                progressBarCircle.setProgress(time);

                time--;
            }

           /* public void onFinish() {
                time = 0;
                timercountdown.cancel();
                timercountdown.start();
                txt_time.setText("" + ":" + checkdigit(time));
                if (pager.getCurrentItem() < quizList.size() - 1) {
                    countpage = pager.getCurrentItem();
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                    int opt = 1;
                    if (pagerAdapter.quizSpecificOptionListnew.size() > 1) {
                        pagerAdapter.selectedOption = pagerAdapter.quizSpecificOptionListnew.get(1).getOptionId();
                    } else {
                        pagerAdapter.selectedOption = pagerAdapter.quizSpecificOptionListnew.get(0).getOptionId();
                    }


                    if (pagerAdapter.quizSpecificOptionListnew.size() > 1) {
                        if (pagerAdapter.selectedOption.equalsIgnoreCase(pagerAdapter.correctAnswer)) {
                            pagerAdapter.selectopt = pagerAdapter.selectopt + 1;
                            opt = 0;
                        } else {
                            pagerAdapter.selectopt = pagerAdapter.selectopt + 1;
                            opt = 1;
                        }
                    } else {
                        pagerAdapter.selectopt = 0;
                        opt = 0;
                    }


                    pagerAdapter.dataArray[pager.getCurrentItem()] = pagerAdapter.quizSpecificOptionListnew.get(opt).getOption();
                    pagerAdapter.checkArray[pager.getCurrentItem()] = pagerAdapter.quizSpecificOptionListnew.get(opt).getOption();

                    if (quizList.size() == pager.getCurrentItem() + 1) {
                        btnNext.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                    } else if (quizList.size() >= pager.getCurrentItem() + 1) {
                        btnNext.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.GONE);
                    }


                } else {
                    if (submitflag != true) {
//                        customHandler.removeCallbacks(updateTimerThread);
                        btnNext.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.GONE);

                        int opt = 1;
                        if (pagerAdapter.quizSpecificOptionListnew.size() > 1) {
                            pagerAdapter.selectedOption = pagerAdapter.quizSpecificOptionListnew.get(1).getOptionId();
                        } else {
                            pagerAdapter.selectedOption = pagerAdapter.quizSpecificOptionListnew.get(0).getOptionId();
                        }


                        if (pagerAdapter.quizSpecificOptionListnew.size() > 1) {
                            if (pagerAdapter.selectedOption.equalsIgnoreCase(pagerAdapter.correctAnswer)) {
                                pagerAdapter.selectopt = pagerAdapter.selectopt + 1;
                                opt = 0;
                            } else {
                                pagerAdapter.selectopt = pagerAdapter.selectopt + 1;
                                opt = 1;
                            }
                        } else {
                            pagerAdapter.selectopt = 0;
                            opt = 0;
                        }


                        pagerAdapter.dataArray[pager.getCurrentItem()] = pagerAdapter.quizSpecificOptionListnew.get(opt).getOption();
                        pagerAdapter.checkArray[pager.getCurrentItem()] = pagerAdapter.quizSpecificOptionListnew.get(opt).getOption();

                        submitflag = true;
                        Boolean valid = true;
                        final int[] check = {0};
                        int sum = 0;
                        final String[] question_id = {""};
                        final String[] question_ans = {""};
                        final String[] value = {""};
                        final RadioGroup[] radioGroup = new RadioGroup[1];
                        final EditText[] ans_edit = new EditText[1];
                        final RadioButton[] radioButton = new RadioButton[1];


                        String[] data = pagerAdapter.getselectedData();
                        String[] question = pagerAdapter.getselectedquestion();

                        if (data != null) {
                            for (int i = 0; i < data.length; i++) {
                                if (i != 0) {
                                    question_id[0] = question_id[0] + "$#";
                                    question_ans[0] = question_ans[0] + "$#";
                                }

                                String id = quizList.get(i).getId();
                                question_id[0] = question_id[0] + id;

                                flag = true;
                                flag1 = true;
                                flag2 = true;
                                if (data[i] != null) {
                                    if (quizList.get(i).getQuiz_type() != null) {
                                        if (quizList.get(i).getQuiz_type().equalsIgnoreCase("2")) {
                                            if (!data[i].equalsIgnoreCase("")) {
                                                question_ans[0] = question_ans[0] + data[i];
                                            } else {
                                                valid = false;
                                            }
                                        } else {

                                            if (!data[i].equalsIgnoreCase("")) {

                                                String idno = quizList.get(i).getId();

                                                for (int j = 0; j < quizOptionList.size(); j++) {
                                                    if (quizOptionList.get(j).getOption().equals(data[i]) && quizOptionList.get(j).getQuizId().equals(idno)) {
                                                        question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                                    }
                                                }
                                            } else {
                                                String idno = quizList.get(i).getId();


                                                for (int j = 0; j < quizOptionList.size(); j++) {
                                                    if (quizOptionList.get(j).getQuizId().equals(idno)) {
                                                        if (flag1 == true) {
                                                            question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                                            flag1 = false;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {

                                        if (!data[i].equalsIgnoreCase("")) {

                                            String idno = quizList.get(i).getId();

                                            for (int j = 0; j < quizOptionList.size(); j++) {
                                                if (quizOptionList.get(j).getOption().equals(data[i]) && quizOptionList.get(j).getQuizId().equals(idno)) {
                                                    question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                                }
                                            }
                                        } else {
                                            String idno = quizList.get(i).getId();


                                            for (int j = 0; j < quizOptionList.size(); j++) {
                                                if (quizOptionList.get(j).getQuizId().equals(idno)) {
                                                    if (flag2 == true) {
                                                        question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                                        flag2 = false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    String idno = quizList.get(i).getId();


                                    for (int j = 0; j < quizOptionList.size(); j++) {
                                        if (quizOptionList.get(j).getQuizId().equals(idno)) {
                                            if (flag == true) {
                                                question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                                flag = false;
                                            }
                                        }
                                    }
                                }

                            }


                            Log.e("valid", question_ans.toString());
                            Log.e("valid", question_id.toString());
                            Log.e("valid", valid.toString());


                            if (valid == true) {
                                quiz_question_id = question_id[0];
                                quiz_options_id = question_ans[0];
                                int answers = pagerAdapter.getCorrectOption();
                                Toast.makeText(QuizActivity.this, quiz_options_id, Toast.LENGTH_SHORT).show();
                                //new postQuizQuestion().execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Please answer all questions", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }*/
           public void onFinish() {
               time = 0;
               timercountdown.cancel();
               timercountdown.start();
               txt_time.setText("" + ":" + checkdigit(time));
               submitQuiz();
           }
        }.start();

        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------

        //----------------------------------------------------------------------------------
        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this,accessToken,
                event_id,
                ApiConstant.pageVisited,
                "26",
                "");
        getUserActivityReport.userActivityReport();
        //--------------------------------------------------------------------------------------
    }

    public String checkdigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class getQuizList extends AsyncTask<Void, Void, Void> {

        String status = "";
        String message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Dismiss the progress dialog
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }

            // Showing progress dialog
            pDialog = new ProgressDialog(QuizActivity.this,
                    R.style.Base_Theme_AppCompat_Dialog_Alert);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            nameValuePair.add(new BasicNameValuePair("api_access_token",
                    accessToken));

            nameValuePair.add(new BasicNameValuePair("event_id",
                    event_id));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(getQuizUrl,
                    ServiceHandler.POST, nameValuePair);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonResult = new JSONObject(jsonStr);
                    status = jsonResult.getString("status");
                    message = jsonResult.getString("msg");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (status.equalsIgnoreCase("success")) {

                // Get Quiz Parser
                quizParser = new QuizParser();
                quizList = quizParser.Quiz_Parser(jsonStr, foldername);

                // Get Quiz Option List
                quizOptionParser = new QuizOptionParser();
                quizOptionList = quizOptionParser.Quiz_Option_Parser(jsonStr);

                procializeDB.clearQuizTable();
                procializeDB.insertQuizTable(quizList, db);

                appDelegate.setQuizOptionList(quizOptionList);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }

            txt_count.setText("Questions 1/" + quizList.size());
            pagerAdapter = new QuizPagerAdapter(QuizActivity.this, quizList);
            pager.setAdapter(pagerAdapter);
            pager.setPagingEnabled(false);
            if (quizList.size() > 1) {
                btnNext.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);

            } else {
                btnNext.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == submit) {
            submitflag = true;
            Boolean valid = true;
            final int[] check = {0};
            int sum = 0;
            final String[] question_id = {""};
            final String[] question_ans = {""};
            final String[] value = {""};
            final RadioGroup[] radioGroup = new RadioGroup[1];
            final EditText[] ans_edit = new EditText[1];
            final RadioButton[] radioButton = new RadioButton[1];
//            Log.e("size", adapter.getItemCount() + "");


            String[] data = pagerAdapter.getselectedData();
            String[] question = pagerAdapter.getselectedquestion();

            if (data != null) {
                for (int i = 0; i < data.length; i++) {
                    if (i != 0) {
                        question_id[0] = question_id[0] + "$#";
                        question_ans[0] = question_ans[0] + "$#";
                    }

                    String id = quizList.get(i).getId();
                    question_id[0] = question_id[0] + id;

                    flag = true;
                    flag1 = true;
                    flag2 = true;
                    if (data[i] != null) {
                        if (quizList.get(i).getQuiz_type() != null) {
                            if (quizList.get(i).getQuiz_type().equalsIgnoreCase("2")) {
                                if (!data[i].equalsIgnoreCase("")) {
                                    question_ans[0] = question_ans[0] + data[i];
                                } else {
                                    valid = false;
                                }
                            } else {

                                if (!data[i].equalsIgnoreCase("")) {

                                    String idno = quizList.get(i).getId();

                                    for (int j = 0; j < quizOptionList.size(); j++) {
                                        if (quizOptionList.get(j).getOption().equals(data[i]) && quizOptionList.get(j).getQuizId().equals(idno)) {
                                            question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                        }
                                    }
                                } else {
                                    String idno = quizList.get(i).getId();

                                    for (int j = 0; j < quizOptionList.size(); j++) {
                                        if (quizOptionList.get(j).getQuizId().equals(idno)) {
                                            if (flag1 == true) {
                                                question_ans[0] = question_ans[0] + "0";
                                                //question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                                flag1 = false;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {

                            if (!data[i].equalsIgnoreCase("")) {

                                String idno = quizList.get(i).getId();

                                for (int j = 0; j < quizOptionList.size(); j++) {
                                    if (quizOptionList.get(j).getOption().equals(data[i]) && quizOptionList.get(j).getQuizId().equals(idno)) {
                                        question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                    }
                                }
                            } else {
                                String idno = quizList.get(i).getId();

                                for (int j = 0; j < quizOptionList.size(); j++) {
                                    if (quizOptionList.get(j).getQuizId().equals(idno)) {
                                        if (flag2 == true) {
                                            question_ans[0] = question_ans[0] + "0";
                                            // question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                            flag2 = false;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        String idno = quizList.get(i).getId();


                        for (int j = 0; j < quizOptionList.size(); j++) {
                            if (quizOptionList.get(j).getQuizId().equals(idno)) {
                                if (flag == true) {
                                    // question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                    question_ans[0] = question_ans[0] + "0";
                                    flag = false;
                                }
                            }
                        }
                    }

                }

                Log.e("valid_ans", question_ans.toString());
                Log.e("valid_id", question_id.toString());
                Log.e("valid_string", valid.toString());


                //if (valid == true) {
                if (submitflag == true) {
                    quiz_question_id = question_id[0];
                    quiz_options_id = question_ans[0];
                    int answers = pagerAdapter.getCorrectOption();
                    Toast.makeText(appDelegate, quiz_options_id, Toast.LENGTH_SHORT).show();
                    Log.d("Selected Options==>",quiz_options_id);
                    new postQuizQuestion().execute();


                } /*else {
                    Toast.makeText(getApplicationContext(), "Please answer all questions", Toast.LENGTH_SHORT).show();
                }*/
            }
        }
        else if(v.getId() == R.id.txtSkip) {
            Boolean valid=false;
            countpage = pager.getCurrentItem();

            //submitflag = true;
            //Boolean valid = true;
            final int[] check = {0};
            int sum = 0;
            final String[] question_id = {""};
            final String[] question_ans = {""};
            final String[] value = {""};
            final RadioGroup[] radioGroup = new RadioGroup[1];
            final EditText[] ans_edit = new EditText[1];
            final RadioButton[] radioButton = new RadioButton[1];
//            Log.e("size", adapter.getItemCount() + "");

            String[] data = pagerAdapter.getselectedData();
            String[] question = pagerAdapter.getselectedquestion();

            if (data != null) {
                for (int i = 0; i < data.length; i++) {
                    if (i != 0) {
                        question_id[0] = question_id[0] + "$#";
                        question_ans[0] = question_ans[0] + "$#";
                    }

                    String id = quizList.get(i).getId();
                    question_id[0] = question_id[0] + id;

                    flag = true;
                    flag1 = true;
                    flag2 = true;
                    if (data[i] != null) {
                        if (quizList.get(i).getQuiz_type() != null) {
                            if (quizList.get(i).getQuiz_type().equalsIgnoreCase("2")) {
                                if (!data[i].equalsIgnoreCase("")) {
                                    question_ans[0] = question_ans[0] + data[i];
                                } else {
                                    valid = false;
                                }
                            } else {

                                if (!data[i].equalsIgnoreCase("")) {

                                    String idno = quizList.get(i).getId();

                                    for (int j = 0; j < quizOptionList.size(); j++) {
                                        if (quizOptionList.get(j).getOption().equals(data[i]) && quizOptionList.get(j).getQuizId().equals(idno)) {
                                            question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                        }
                                    }
                                } else {
                                    String idno = quizList.get(i).getId();

                                    for (int j = 0; j < quizOptionList.size(); j++) {
                                        if (quizOptionList.get(j).getQuizId().equals(idno)) {
                                            if (flag1 == true) {
                                                question_ans[0] = question_ans[0] + "0";
                                                //question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                                flag1 = false;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {

                            if (!data[i].equalsIgnoreCase("")) {

                                String idno = quizList.get(i).getId();

                                for (int j = 0; j < quizOptionList.size(); j++) {
                                    if (quizOptionList.get(j).getOption().equals(data[i]) && quizOptionList.get(j).getQuizId().equals(idno)) {
                                        question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                    }
                                }
                            } else {
                                String idno = quizList.get(i).getId();

                                for (int j = 0; j < quizOptionList.size(); j++) {
                                    if (quizOptionList.get(j).getQuizId().equals(idno)) {
                                        if (flag2 == true) {
                                            question_ans[0] = question_ans[0] + "0";
                                            // question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                            flag2 = false;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        String idno = quizList.get(i).getId();


                        for (int j = 0; j < quizOptionList.size(); j++) {
                            if (quizOptionList.get(j).getQuizId().equals(idno)) {
                                if (flag == true) {
                                    // question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                    question_ans[0] = question_ans[0] + "0";
                                    flag = false;
                                }
                            }
                        }
                    }
                }

                Log.e("valid_ans", question_ans.toString());
                Log.e("valid_id", question_id.toString());
                Log.e("valid_string", valid.toString());

               // if (valid == true && submitflag == true) {
                if (submitflag == true) {
                    quiz_question_id = question_id[0];
                    quiz_options_id = question_ans[0];
                    int answers = pagerAdapter.getCorrectOption();

                    Log.d("Selected Options==>",quiz_options_id);

                    Toast.makeText(appDelegate, quiz_options_id, Toast.LENGTH_SHORT).show();
                    new postQuizQuestion().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "last question", Toast.LENGTH_SHORT).show();
                }
            }

            pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            if (quizList.size() == pager.getCurrentItem() + 1) {
                btnNext.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                submitflag = true;
                valid = true;
            } else if (quizList.size() >= pager.getCurrentItem() + 1) {
                btnNext.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                //time = 0;
                time = timerForQuiz;
                timercountdown.cancel();
                timercountdown.start();
                txt_time.setText("" + ":" + checkdigit(time));
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (cd.isConnectingToInternet()) {
            new getQuizList().execute();
        }
    }


    private class postQuizQuestion extends AsyncTask<Void, Void, Void> {

        String error = "";
        String message = "";
        String total_correct_answer = "";
        String total_questions = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            try {
                pDialog = new ProgressDialog(QuizActivity.this,
                        R.style.Base_Theme_AppCompat_Dialog_Alert);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            nameValuePair.add(new BasicNameValuePair("api_access_token",
                    accessToken));D
            nameValuePair.add(new BasicNameValuePair("event_id",
                    event_id));
            nameValuePair.add(new BasicNameValuePair("quiz_id", quiz_question_id));
            nameValuePair.add(new BasicNameValuePair("quiz_options_id",
                    quiz_options_id));
//1529$#1533$#1538$#1541$#1545$#1549$#1553$#1555$#1561$#1565
//1529$#1533$#1538$#1541$#1545$#1549$#1553$#1555$#1561$#1565
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(quizQuestionUrl,
                    ServiceHandler.POST, nameValuePair);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
//
//{"total_correct_answer":1,"total_questions":3,"status":"success","msg":"Answer posted successfully"}
                    JSONObject jsonResult = new JSONObject(jsonStr);
                    error = jsonResult.getString("status");
                    message = jsonResult.getString("msg");
                    total_correct_answer = jsonResult.getString("total_correct_answer");
                    total_questions = jsonResult.getString("total_questions");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            timercountdown.cancel();
            pagerAdapter.checkArray=null;
            pagerAdapter.correctAnswer="";
            pagerAdapter.selectedOption="";
            pagerAdapter.dataArray=null;
            pagerAdapter.dataIDArray=null;
            pagerAdapter.ansArray=null;
            // Dismiss the progress dialog
            try {
                if (pDialog != null) {
                    pDialog.dismiss();
                    pDialog = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (error.equalsIgnoreCase("success")) {
                int answers = pagerAdapter.getCorrectOption();
                Intent intent = new Intent(QuizActivity.this, YourScoreActivity.class);
                intent.putExtra("folderName", foldername);
                intent.putExtra("Answers", String.valueOf(total_correct_answer));
                intent.putExtra("TotalQue", String.valueOf(total_questions));
                intent.putExtra("Page", "Question");
                startActivity(intent);
                count1 = 1;
                pagerAdapter.selectopt = 0;
                submitflag = true;

                finish();

            } else {

                Toast.makeText(QuizActivity.this, message,
                        Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        count1 = 1;
        pagerAdapter.selectopt = 0;
        submitflag = false;
        try {
            timercountdown.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*Intent intent = new Intent(QuizActivity.this, FolderQuizActivity.class);
        startActivity(intent);
        finish();*/
getSupportFragmentManager().popBackStack();

    }


    public void submitQuiz()
    {
        Boolean valid=false;
        countpage = pager.getCurrentItem();

        //submitflag = true;
        //Boolean valid = true;
        final int[] check = {0};
        int sum = 0;
        final String[] question_id = {""};
        final String[] question_ans = {""};
        final String[] value = {""};
        final RadioGroup[] radioGroup = new RadioGroup[1];
        final EditText[] ans_edit = new EditText[1];
        final RadioButton[] radioButton = new RadioButton[1];
//            Log.e("size", adapter.getItemCount() + "");

        String[] data = pagerAdapter.getselectedData();
        String[] question = pagerAdapter.getselectedquestion();

        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if (i != 0) {
                    question_id[0] = question_id[0] + "$#";
                    question_ans[0] = question_ans[0] + "$#";
                }

                String id = quizList.get(i).getId();
                question_id[0] = question_id[0] + id;

                flag = true;
                flag1 = true;
                flag2 = true;
                if (data[i] != null) {
                    if (quizList.get(i).getQuiz_type() != null) {
                        if (quizList.get(i).getQuiz_type().equalsIgnoreCase("2")) {
                            if (!data[i].equalsIgnoreCase("")) {
                                question_ans[0] = question_ans[0] + data[i];
                            } else {
                                valid = false;
                            }
                        } else {

                            if (!data[i].equalsIgnoreCase("")) {

                                String idno = quizList.get(i).getId();

                                for (int j = 0; j < quizOptionList.size(); j++) {
                                    if (quizOptionList.get(j).getOption().equals(data[i]) && quizOptionList.get(j).getQuizId().equals(idno)) {
                                        question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                    }
                                }
                            } else {
                                String idno = quizList.get(i).getId();

                                for (int j = 0; j < quizOptionList.size(); j++) {
                                    if (quizOptionList.get(j).getQuizId().equals(idno)) {
                                        if (flag1 == true) {
                                            question_ans[0] = question_ans[0] + "0";
                                            //question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                            flag1 = false;
                                        }
                                    }
                                }
                            }
                        }
                    } else {

                        if (!data[i].equalsIgnoreCase("")) {

                            String idno = quizList.get(i).getId();

                            for (int j = 0; j < quizOptionList.size(); j++) {
                                if (quizOptionList.get(j).getOption().equals(data[i]) && quizOptionList.get(j).getQuizId().equals(idno)) {
                                    question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                }
                            }
                        } else {
                            String idno = quizList.get(i).getId();

                            for (int j = 0; j < quizOptionList.size(); j++) {
                                if (quizOptionList.get(j).getQuizId().equals(idno)) {
                                    if (flag2 == true) {
                                        question_ans[0] = question_ans[0] + "0";
                                        // question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                        flag2 = false;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    String idno = quizList.get(i).getId();


                    for (int j = 0; j < quizOptionList.size(); j++) {
                        if (quizOptionList.get(j).getQuizId().equals(idno)) {
                            if (flag == true) {
                                // question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                question_ans[0] = question_ans[0] + "0";
                                flag = false;
                            }
                        }
                    }
                }
            }

            Log.e("valid_ans", question_ans.toString());
            Log.e("valid_id", question_id.toString());
            Log.e("valid_string", valid.toString());

            //if (valid == true && submitflag == true) {
            if (submitflag == true) {
                quiz_question_id = question_id[0];
                quiz_options_id = question_ans[0];
                int answers = pagerAdapter.getCorrectOption();

                Log.d("Selected Options==>",quiz_options_id);

                Toast.makeText(appDelegate, quiz_options_id, Toast.LENGTH_SHORT).show();
                new postQuizQuestion().execute();
            } /*else {
                Toast.makeText(getApplicationContext(), "Please answer all questions", Toast.LENGTH_SHORT).show();
            }*/
        }

        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
        if (quizList.size() == pager.getCurrentItem() + 1) {
            btnNext.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
            submitflag = true;
            valid = true;
        } else if (quizList.size() >= pager.getCurrentItem() + 1) {
            btnNext.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
            //time = 0;
            time = timerForQuiz;
            timercountdown.cancel();
            timercountdown.start();
            txt_time.setText("" + ":" + checkdigit(time));
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        customHandler.postDelayed(updateTimerThread, 10000);
//
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        timeSwapBuff += timeInMilliseconds;
//        customHandler.removeCallbacks(updateTimerThread);
//
//    }
}
