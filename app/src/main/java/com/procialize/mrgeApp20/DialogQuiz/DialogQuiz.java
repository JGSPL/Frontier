package com.procialize.mrgeApp20.DialogQuiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.procialize.mrgeApp20.Adapter.QuizAdapter;
import com.procialize.mrgeApp20.Adapter.QuizFolderAdapter;
import com.procialize.mrgeApp20.Adapter.QuizPagerAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.CustomTools.CustomViewPager;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.DialogQuiz.adapter.QuizPagerDialogAdapter;
import com.procialize.mrgeApp20.GetterSetter.Quiz;
import com.procialize.mrgeApp20.GetterSetter.QuizFolder;
import com.procialize.mrgeApp20.GetterSetter.QuizOptionList;
import com.procialize.mrgeApp20.InnerDrawerActivity.YourScoreActivity;
import com.procialize.mrgeApp20.MrgeInnerFragment.FolderQuizFragment;
import com.procialize.mrgeApp20.Parser.QuizFolderParser;
import com.procialize.mrgeApp20.Parser.QuizOptionParser;
import com.procialize.mrgeApp20.Parser.QuizParser;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.MyApplication;
import com.procialize.mrgeApp20.Utility.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import static android.content.Context.MODE_PRIVATE;

public class DialogQuiz implements View.OnClickListener {
    static BottomSheetDialog dialog, Detaildialog, ThankyouDialog, ResultDialog;
    private ProgressDialog pDialog;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    APIService mAPIService;
    SessionManager sessionManager;
    String apikey, getQuizUrl = "";
    Context context2;
    private QuizFolderParser quizFolderParser;
    private ArrayList<QuizOptionList> quizOptionList = new ArrayList<QuizOptionList>();
    public String Jsontr;
    private ArrayList<QuizFolder> quizFolders = new ArrayList<QuizFolder>();
    public static MyApplication appDelegate;
    private ArrayList<Quiz> quizList = new ArrayList<Quiz>();

    //QuizDetail

    // Session Manager Class
    private SessionManager session;

    // Access Token Variable
    private String accessToken, event_id;

    public static String quiz_question_id;
    private String quiz_options_id;

    private String quizQuestionUrl = "";
    private ConnectionDetector cd;
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    boolean[] timerProcessing = {false};
    boolean[] timerStarts = {false};
    private ApiConstant constant = new ApiConstant();


    private QuizAdapter adapter;

    private QuizParser quizParser;
    RelativeLayout relative;
    private QuizOptionParser quizOptionParser;
    public static String foldername = "null";
    public static String folderId = "null";

    public static Button submit;
    ImageView headerlogoIv;
    TextView questionTv, txt_time, txtHeaderQ;
    CustomViewPager pager;
    ViewPager pager2;

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

    public void welcomeQuizDialog(Context context) {

        // dialog = new BottomSheetDialog(context);
        dialog = new BottomSheetDialog(context, R.style.SheetDialog);

        dialog.setContentView(R.layout.bottom_quiz_welcome);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setDimAmount(0);

        context2 = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        getQuizUrl = ApiConstant.baseUrl + ApiConstant.Spotquizlist;

        appDelegate = (MyApplication) context.getApplicationContext();

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(context);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);

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
                new getQuizList().execute();
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    public void QuizDetailDialog(Context context, String folderid, String folderName) {

        // dialog = new BottomSheetDialog(context);
        Detaildialog = new BottomSheetDialog(context, R.style.SheetDialog);

        Detaildialog.setContentView(R.layout.botom_quiz_questiondetail);
        Detaildialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Detaildialog.getWindow().setDimAmount(0);

        Detaildialog.setCancelable(false);

        ImageView ic_close = Detaildialog.findViewById(R.id.imgClose);
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Detaildialog.dismiss();
            }
        });

        context2 = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        getQuizUrl = ApiConstant.baseUrl + ApiConstant.quizlist;

        appDelegate = (MyApplication) context.getApplicationContext();

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(context);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);
        quizQuestionUrl = constant.baseUrl + constant.Spotquizsubmit;
        procializeDB = new DBHelper(context);
        db = procializeDB.getWritableDatabase();

        db = procializeDB.getReadableDatabase();


        foldername = folderName;
        folderId = folderid;

        // Session Manager

        cd = new ConnectionDetector(context);

        // Initialize Get Quiz URL
        getQuizUrl = constant.baseUrl + constant.Spotquizlist;


        submit = (Button) Detaildialog.findViewById(R.id.submit);


        questionTv = (TextView) Detaildialog.findViewById(R.id.questionTv);

        questionTv.setText(foldername);
        questionTv.setBackgroundColor(Color.parseColor(colorActive));
        submit.setBackgroundColor(Color.parseColor(colorActive));
        submit.setOnClickListener(this);

/*
        btnNext.setOnClickListener(new View.OnClickListener() {
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

                    //                    txt_time.setText(String.format(Locale.getDefault(), "%d", time));
//                    if (time > 0)
//                        time -= 1;
                } else if (quizList.size() >= pager.getCurrentItem() + 1) {
                    btnNext.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                    time = 0;
                    timercountdown.cancel();
                    timercountdown.start();
                    txt_time.setText("" + ":" + checkdigit(time));

                }
            }
        });
*/


        pager = Detaildialog.findViewById(R.id.pager);


        if (cd.isConnectingToInternet()) {
            new getQuizListDetail().execute();
        } else {

            Toast.makeText(context, "No internet connection",
                    Toast.LENGTH_SHORT).show();
        }


        Detaildialog.show();
    }


    private class getQuizList extends AsyncTask<Void, Void, Void> {

        String status = "";
        String message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Dismiss the progress dialog


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            nameValuePair.add(new BasicNameValuePair("api_access_token",
                    apikey));


            nameValuePair.add(new BasicNameValuePair("event_id",
                    eventid));

            Log.e("api_access_token", apikey);
            Log.e("api_access_token", eventid);
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(getQuizUrl,
                    ServiceHandler.POST, nameValuePair);

            Log.d("quizresponse: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    Jsontr = jsonStr;

                    JSONObject jsonResult = new JSONObject(jsonStr);
                    status = jsonResult.getString("status");
                    message = jsonResult.getString("msg");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (status.equalsIgnoreCase("success")) {


                //Get Folder Parser
                quizFolderParser = new QuizFolderParser();
                quizFolders = quizFolderParser.QuizFolder_Parser2(jsonStr);

                appDelegate.setQuizOptionList(quizOptionList);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (quizFolders.size() > 0) {
                String quiz = quizFolders.get(0).getFolder_id();
                String Foldername = quizFolders.get(0).getFolder_name();

                if (quiz != null) {
                    if (quiz != null && !quiz.equalsIgnoreCase("null")) {

                        if (Jsontr != null) {

                            QuizParser quizParser = new QuizParser();

                            quizList = new ArrayList<>();

                            Log.e("size", quizList.size() + "");
                            Log.e("size", quiz);

                            quizList = quizParser.Quiz_Parser2(Jsontr, quiz);


                            if (/*quizList != null ||*/ quizList.size() > 0) {

                                if (quizList.get(0).getReplied().equals("1")) {
                                    DialogQuizREsult qq = new DialogQuizREsult();
                                    qq.resultQuizDialog(context2, foldername, quiz);



                                } else {
                                    QuizDetailDialog(context2, quiz, foldername);

                                    count1 = 1;
                                    submitflag = false;


                                }
                            } else {

                                Toast.makeText(context2,
                                        "Question not available.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {

                        Toast.makeText(context2,
                                "Question not available.",
                                Toast.LENGTH_SHORT).show();
                    }
                }


            }
        }
    }

    public String checkdigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    private class getQuizListDetail extends AsyncTask<Void, Void, Void> {

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
            pDialog = new ProgressDialog(context2,
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
                    apikey));

            nameValuePair.add(new BasicNameValuePair("event_id",
                    eventid));

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
                quizList = quizParser.Quiz_Parser2(jsonStr, folderId);

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


            pagerAdapter = new QuizPagerDialogAdapter(context2, quizList);
            pager.setAdapter(pagerAdapter);
            pager.setPagingEnabled(false);
            submit.setVisibility(View.VISIBLE);

            /*if (quizList.size() > 1) {
                submit.setVisibility(View.GONE);

            } else {
                submit.setVisibility(View.VISIBLE);
            }*/


        }
    }

    public void ThankYouQuizDialog(Context context) {

        // dialog = new BottomSheetDialog(context);
        ThankyouDialog = new BottomSheetDialog(context, R.style.SheetDialog);

        ThankyouDialog.setContentView(R.layout.bottom_quiz_thankyou);
        ThankyouDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ThankyouDialog.getWindow().setDimAmount(0);
        ThankyouDialog.dismiss();

        context2 = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        getQuizUrl = ApiConstant.baseUrl + ApiConstant.quizlist;

        appDelegate = (MyApplication) context.getApplicationContext();

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(context);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);

        ImageView imgClose = ThankyouDialog.findViewById(R.id.imgClose);
        TextView txtViewResult = ThankyouDialog.findViewById(R.id.txtViewResult);
        CardView Quizcard = ThankyouDialog.findViewById(R.id.Quizcard);
        Quizcard.setBackgroundColor(Color.parseColor("#ffffff"));
        Quizcard.setAlpha(0.8f);
        txtViewResult.setTextColor(Color.parseColor(colorActive));


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThankyouDialog.dismiss();
            }
        });
        txtViewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new getQuizList().execute();
                DialogQuizREsult dQR = new DialogQuizREsult();
                dQR.resultQuizDialog(context, foldername,folderId);
                ThankyouDialog.dismiss();

            }
        });

        ThankyouDialog.show();
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


                if (valid == true) {
                    quiz_question_id = question_id[0];
                    quiz_options_id = question_ans[0];
                    int answers = pagerAdapter.getCorrectOption();
                    Toast.makeText(appDelegate, quiz_options_id, Toast.LENGTH_SHORT).show();
                    Log.d("Selected Options==>",quiz_options_id);
                    new postQuizQuestion().execute();
//                    Intent intent = new Intent(QuizActivity.this, YourScoreActivity.class);
//                    intent.putExtra("folderName", foldername);
//                    intent.putExtra("Answers", answers);
//                    intent.putExtra("TotalQue", adapter.getselectedData().length);
//                    startActivity(intent);
                } else {
                    Toast.makeText(context2, "Please answer all questions", Toast.LENGTH_SHORT).show();
                }
            }
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
                pDialog = new ProgressDialog(context2,
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
                    apikey));
            nameValuePair.add(new BasicNameValuePair("event_id",
                    eventid));
            nameValuePair.add(new BasicNameValuePair("quiz_id", quiz_question_id));
            nameValuePair.add(new BasicNameValuePair("quiz_options_id",
                    quiz_options_id));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(quizQuestionUrl,
                    ServiceHandler.POST, nameValuePair);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

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
                /*Intent intent = new Intent(context2, YourScoreActivity.class);
                intent.putExtra("folderName", foldername);
                intent.putExtra("Answers", String.valueOf(total_correct_answer));
                intent.putExtra("TotalQue", String.valueOf(total_questions));
                intent.putExtra("Page", "Question");
                context2.startActivity(intent);*/
                count1 = 1;
                pagerAdapter.selectopt = 0;
                submitflag = true;
               // DialogQuizREsult QuizRe = new DialogQuizREsult();
               // QuizRe.resultQuizDialog(context2,foldername,folderId);
                Detaildialog.dismiss();
                ThankYouQuizDialog(context2);

            } else {

                Toast.makeText(context2, message,
                        Toast.LENGTH_SHORT).show();
            }

        }
    }


}
