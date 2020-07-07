package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.mrgeApp20.Adapter.QuizFolderAdapter;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
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
import com.procialize.mrgeApp20.Utility.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;

import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class FolderQuizActivity extends AppCompatActivity {

    public static MyApplication appDelegate;
    public String Jsontr;
    ApiConstant constant;
    String MY_PREFS_NAME = "ProcializeInfo";
    ImageView headerlogoIv;
    private ProgressDialog pDialog;
    // Session Manager Class
    private SessionManager session;
    // Access Token Variable
    private String accessToken, event_id;
    private String quiz_id, colorActive;
    private String quiz_options_id;
    private String quizQuestionUrl = "";
    private String getQuizUrl = "";
    private ConnectionDetector cd;
    private ListView quizNameList;
    private QuizFolderAdapter adapter;
    private QuizParser quizParser;
    private QuizFolderParser quizFolderParser;
    private ArrayList<Quiz> quizList = new ArrayList<Quiz>();
    private ArrayList<QuizFolder> quizFolders = new ArrayList<QuizFolder>();
    private QuizOptionParser quizOptionParser;
    private ArrayList<QuizOptionList> quizOptionList = new ArrayList<QuizOptionList>();
    SwipeRefreshLayout quizrefresher;
    TextView empty, pullrefresh;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_quiz);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        quizrefresher = findViewById(R.id.quizrefresher);
        Util.logomethod(this, headerlogoIv);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        constant = new ApiConstant();
        appDelegate = (MyApplication) getApplicationContext();

        // Session Manager
        session = new SessionManager(getApplicationContext());
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);

        crashlytics("Quiz Folder",accessToken);
        firbaseAnalytics(this, "Quiz Folder",accessToken);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        event_id = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        cd = new ConnectionDetector(getApplicationContext());

        // Initialize Get Quiz URL
        getQuizUrl = ApiConstant.baseUrl + ApiConstant.quizlist;

        TextView header = findViewById(R.id.header);
        empty = findViewById(R.id.empty);
        linear = findViewById(R.id.linear);
        pullrefresh = findViewById(R.id.pullrefresh);
        header.setTextColor(Color.parseColor(colorActive));
        pullrefresh.setTextColor(Color.parseColor(colorActive));

        RelativeLayout layoutTop = findViewById(R.id.layoutTop);
        layoutTop.setBackgroundColor(Color.parseColor(colorActive));

        quizNameList = findViewById(R.id.quiz_list);
        quizNameList.setScrollingCacheEnabled(false);
        quizNameList.setAnimationCacheEnabled(false);

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        if (cd.isConnectingToInternet()) {
            new getQuizList().execute();
        } else {

            Toast.makeText(FolderQuizActivity.this, "No internet connection",
                    Toast.LENGTH_SHORT).show();

        }


        quizrefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {
                    new getQuizList().execute();
                } else {

                    Toast.makeText(FolderQuizActivity.this, "No internet connection",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });


/*
        quizNameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                QuizFolder quiz = adapter.getQuestionIdFromList(position);


                if (quiz != null) {
                    if (quiz.getFolder_name() != null && !quiz.getFolder_name().equalsIgnoreCase("null")) {

                        if (Jsontr != null) {

                            QuizParser quizParser = new QuizParser();

                            quizList = new ArrayList<>();

                            Log.e("size", quizList.size() + "");
                            Log.e("size", quiz.getFolder_name());

                            quizList = quizParser.Quiz_Parser(Jsontr, quiz.getFolder_name());


                            if (*/
        /*quizList != null ||*//*
 quizList.size() > 0) {

                                if (quizList.get(0).getReplied().equals("1")) {

                                    Toast.makeText(FolderQuizActivity.this, "You already submitted the quiz.", Toast.LENGTH_SHORT).show();

                                } else {
                                    Intent quizOptionIntent = new Intent(FolderQuizActivity.this, QuizActivity.class);
                                    quizOptionIntent.putExtra("folder", quiz.getFolder_name());
                                    startActivity(quizOptionIntent);
                                    //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                }
                            } else {

                                Toast.makeText(FolderQuizActivity.this,
                                        "Question not available.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {

                        Toast.makeText(FolderQuizActivity.this,
                                "Question not available.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(FolderQuizActivity.this,
                            "Question not available.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
*/
        quizNameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                QuizFolder quiz = adapter.getQuestionIdFromList(position);


                if (quiz != null) {
                    if (quiz.getFolder_name() != null && !quiz.getFolder_name().equalsIgnoreCase("null")) {

                        if (Jsontr != null) {

                            QuizParser quizParser = new QuizParser();

                            quizList = new ArrayList<>();

                            Log.e("size", quizList.size() + "");
                            Log.e("size", quiz.getFolder_name());

                            quizList = quizParser.Quiz_Parser(Jsontr, quiz.getFolder_name());


                            if (/*quizList != null ||*/ quizList.size() > 0) {

                                if (quizList.get(0).getReplied().equals("1")) {

                                    Intent quizOptionIntent = new Intent(FolderQuizActivity.this, QuizNewActivity.class);
                                    quizOptionIntent.putExtra("folder", quiz.getFolder_name());
                                    startActivity(quizOptionIntent);
                                    finish();

                                } else {
                                    QuizActivity.count1 = 1;
                                    QuizActivity.submitflag = false;

                                    Intent quizOptionIntent = new Intent(FolderQuizActivity.this, QuizActivity.class);
                                    quizOptionIntent.putExtra("folder", quiz.getFolder_name());
                                    startActivity(quizOptionIntent);
                                    finish();
//                                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                }
                            } else {

                                Toast.makeText(FolderQuizActivity.this,
                                        "Question not available.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {

                        Toast.makeText(FolderQuizActivity.this,
                                "Question not available.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(FolderQuizActivity.this,
                            "Question not available.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cd.isConnectingToInternet()) {
            new getQuizList().execute();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
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
            pDialog = new ProgressDialog(FolderQuizActivity.this,
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

            Log.e("api_access_token", accessToken);
            Log.e("api_access_token", event_id);
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
                quizFolders = quizFolderParser.QuizFolder_Parser(jsonStr);

                appDelegate.setQuizOptionList(quizOptionList);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (quizrefresher.isRefreshing()) {
                quizrefresher.setRefreshing(false);
            }
            // Dismiss the progress dialog
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }

            empty.setTextColor(Color.parseColor(colorActive));
            if (quizFolders.size() != 0) {
                empty.setVisibility(View.GONE);
                adapter = new QuizFolderAdapter(FolderQuizActivity.this, quizFolders,"");
                quizNameList.setAdapter(adapter);
            } else {
                empty.setVisibility(View.VISIBLE);
            }

        }
    }

}
