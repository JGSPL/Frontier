package com.procialize.mrgeApp20.DialogQuiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.DialogQuiz.adapter.QuizRDialogAdapter;
import com.procialize.mrgeApp20.GetterSetter.Quiz;
import com.procialize.mrgeApp20.GetterSetter.QuizOptionList;
import com.procialize.mrgeApp20.Parser.QuizOptionParser;
import com.procialize.mrgeApp20.Parser.QuizParser;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.MyApplication;
import com.procialize.mrgeApp20.Utility.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DialogQuizREsult {
    static BottomSheetDialog  ResultDialog;
    private ProgressDialog pDialog;
    // Session Manager Class
    private SessionManager session;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    APIService mAPIService;
    SessionManager sessionManager;
    String apikey, getQuizUrl = "";
    // Access Token Variable
    private String accessToken, event_id;

    private String quiz_question_id;
    private String quiz_options_id;

    private String quizQuestionUrl = "";
    private ConnectionDetector cd;
    private ApiConstant constant = new ApiConstant();

    private RecyclerView quizNameList;
    private QuizRDialogAdapter adapter;

    private QuizParser quizParser;
    private ArrayList<Quiz> quizList = new ArrayList<Quiz>();

    private QuizOptionParser quizOptionParser;
    private ArrayList<QuizOptionList> quizOptionList = new ArrayList<QuizOptionList>();

    public static MyApplication appDelegate;
    String foldername = "null";
    Button submit;
    ImageView headerlogoIv;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    int count = 1;
    private DBHelper dbHelper;
    LinearLayoutManager llm;
    LinearLayoutManager recyclerLayoutManager;
    ViewPager pager;
    TextView questionTv, txt_count;
    Button btnNext;
    RelativeLayout relative;
    Context context2;

    public void resultQuizDialog(Context context, String folder) {

        // dialog = new BottomSheetDialog(context);
        ResultDialog = new BottomSheetDialog(context, R.style.SheetDialog);

        ResultDialog.setContentView(R.layout.bottom_quiz_result);
        ResultDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ResultDialog.getWindow().setDimAmount(0);

        context2 = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        getQuizUrl = ApiConstant.baseUrl + ApiConstant.quizlist;
        cd = new ConnectionDetector(context);
        procializeDB = new DBHelper(context);
        db = procializeDB.getWritableDatabase();

        db = procializeDB.getReadableDatabase();
        foldername = folder;


        appDelegate = (MyApplication) context.getApplicationContext();

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(context);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);

        getQuizUrl = constant.baseUrl + constant.quizlist;


        submit = (Button) ResultDialog.findViewById(R.id.submit);
        pager = (ViewPager)ResultDialog.findViewById(R.id.pager);
        questionTv = (TextView) ResultDialog.findViewById(R.id.questionTv);
        btnNext = (Button) ResultDialog.findViewById(R.id.btnNext);
        txt_count = (TextView) ResultDialog.findViewById(R.id.txt_count);
//        txt_count.setVisibility(View.GONE);
        quizNameList = (RecyclerView) ResultDialog.findViewById(R.id.quiz_list);
        btnNext = (Button) ResultDialog.findViewById(R.id.btnNext);
        relative = (RelativeLayout) ResultDialog.findViewById(R.id.relative);
        recyclerLayoutManager = new LinearLayoutManager(context);
        quizNameList.setLayoutManager(recyclerLayoutManager);
        questionTv.setBackgroundColor(Color.parseColor(colorActive));
        submit.setBackgroundColor(Color.parseColor(colorActive));
        btnNext.setBackgroundColor(Color.parseColor(colorActive));
        txt_count.setTextColor(Color.parseColor(colorActive));
//		quizNameList.setItemViewCacheSize(0);
        quizNameList.setAnimationCacheEnabled(true);
        quizNameList.setDrawingCacheEnabled(true);
        quizNameList.hasFixedSize();
        questionTv.setText(foldername);
        quizNameList.setLayoutFrozen(true);
//        quizNameList.setNestedScrollingEnabled(false);
        quizNameList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        llm = (LinearLayoutManager) quizNameList.getLayoutManager();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int option = adapter.getSelectedOption();
                String correctOption = quizList.get(llm.findLastVisibleItemPosition()).getCorrect_answer();
                int i = adapter.getItemCount();
//                adapter.getItemViewType(llm.findLastVisibleItemPosition());
                if (i != count) {
//                    if (option != llm.findLastVisibleItemPosition()) {
                    quizNameList.getLayoutManager().scrollToPosition(llm.findLastVisibleItemPosition() + 1);
                    txt_count.setText(count + 1 + "/" + i);
                    count = count + 1;
                    if (quizList.size() == llm.findLastVisibleItemPosition() + 2) {

                        btnNext.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                    } else {
                        btnNext.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.GONE);
                    }
//                    } else {
//                        Toast.makeText(QuizNewActivity.this, "Please Select Option", Toast.LENGTH_SHORT).show();
//                    }
                }


            }
        });

        if (cd.isConnectingToInternet()) {
            new getQuizList().execute();
        } else {

            Toast.makeText(context, "No internet connection",
                    Toast.LENGTH_SHORT).show();


        }


        ResultDialog.show();
    }


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
//
            adapter = new QuizRDialogAdapter(context2, quizList);
            quizNameList.setAdapter(adapter);
            int itemcount = adapter.getItemCount();
            txt_count.setText(1 + "/" + itemcount);
            if (quizList.size() > 1) {
                btnNext.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
            } else {
                btnNext.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
            }

        }
    }


}
