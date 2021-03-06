package com.procialize.frontier.MrgeInnerFragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.procialize.frontier.Adapter.QuizFolderAdapter;
import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.DbHelper.ConnectionDetector;
import com.procialize.frontier.GetterSetter.Quiz;
import com.procialize.frontier.GetterSetter.QuizFolder;
import com.procialize.frontier.GetterSetter.QuizLogo;
import com.procialize.frontier.GetterSetter.QuizOptionList;
import com.procialize.frontier.InnerDrawerActivity.QuizActivity;
import com.procialize.frontier.InnerDrawerActivity.QuizNewActivity;
import com.procialize.frontier.MergeMain.MrgeHomeActivity;
import com.procialize.frontier.Parser.QuizFolderParser;
import com.procialize.frontier.Parser.QuizLogoParser;
import com.procialize.frontier.Parser.QuizOptionParser;
import com.procialize.frontier.Parser.QuizParser;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.MyApplication;
import com.procialize.frontier.Utility.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.frontier.Utility.Util.setNotification;
import static com.procialize.frontier.util.CommonFunction.crashlytics;
import static com.procialize.frontier.util.CommonFunction.firbaseAnalytics;

public class FolderQuizFragment extends Fragment {
    public static MyApplication appDelegate;
    public static Activity activity;
    public String Jsontr;
    ApiConstant constant;
    String MY_PREFS_NAME = "ProcializeInfo";
    ImageView headerlogoIv;
    QuizLogo quizlogo;
    SwipeRefreshLayout quizrefresher;
    TextView empty, pullrefresh;
    LinearLayout linear;
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
    private QuizLogoParser quizLogoParser;
    private ArrayList<Quiz> quizList = new ArrayList<Quiz>();
    private ArrayList<QuizFolder> quizFolders = new ArrayList<QuizFolder>();
    private QuizOptionParser quizOptionParser;
    private ArrayList<QuizOptionList> quizOptionList = new ArrayList<QuizOptionList>();

    public FolderQuizFragment() {
    }

    public FolderQuizFragment(Activity activity) {
        this.activity = activity;
    }

    public static FolderQuizFragment newInstance() {
        FolderQuizFragment fragment = new FolderQuizFragment(activity);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_folder_quiz, container, false);

        try {
            setNotification(getActivity(), MrgeHomeActivity.tv_notification, MrgeHomeActivity.ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }

        constant = new ApiConstant();
        appDelegate = (MyApplication) getActivity().getApplicationContext();

        // Session Manager
        session = new SessionManager(getContext());
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);

        crashlytics("Quiz Folder", accessToken);
        firbaseAnalytics(getContext(), "Quiz Folder", accessToken);

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        event_id = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        cd = new ConnectionDetector(getContext());

        // Initialize Get Quiz URL
        getQuizUrl = ApiConstant.baseUrl + ApiConstant.quizlist;

        TextView header = view.findViewById(R.id.header);
        empty = view.findViewById(R.id.empty);
        linear = view.findViewById(R.id.linear);
        pullrefresh = view.findViewById(R.id.pullrefresh);
        header.setTextColor(Color.parseColor(colorActive));
        pullrefresh.setTextColor(Color.parseColor(colorActive));
        quizrefresher = view.findViewById(R.id.quizrefresher);

        RelativeLayout layoutTop = view.findViewById(R.id.layoutTop);
        //  layoutTop.setBackgroundColor(Color.parseColor(colorActive));

        quizNameList = view.findViewById(R.id.quiz_list);
        quizNameList.setScrollingCacheEnabled(false);
        quizNameList.setAnimationCacheEnabled(false);

        if (cd.isConnectingToInternet()) {
            new getQuizList().execute();
        } else {

            Toast.makeText(getContext(), "No internet connection",
                    Toast.LENGTH_SHORT).show();

        }

       /* DialogLivePoll dQuiz = new DialogLivePoll();
        dQuiz.welcomeLivePollDialog(getContext());*/

        //DialogQuiz dQuiz = new DialogQuiz();
        //dQuiz.welcomeQuizDialog(getContext());

        quizrefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {
                    new getQuizList().execute();
                } else {

                    Toast.makeText(getContext(), "No internet connection",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });


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

                                    Intent quizOptionIntent = new Intent(getContext(), QuizNewActivity.class);
                                    quizOptionIntent.putExtra("folder", quiz.getFolder_name());
                                    quizOptionIntent.putExtra("timer", quiz.getTimer());
                                    startActivity(quizOptionIntent);

                                } else {
                                    QuizActivity.count1 = 1;
                                    QuizActivity.submitflag = false;

                                    Intent quizOptionIntent = new Intent(getContext(), QuizActivity.class);
                                    quizOptionIntent.putExtra("folder", quiz.getFolder_name());
                                    quizOptionIntent.putExtra("timer", quiz.getTimer());
                                    startActivity(quizOptionIntent);

                                }
                            } else {
                                Toast.makeText(getContext(),
                                        "Question not available.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getContext(),
                                "Question not available.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(getContext(),
                            "Question not available.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        //--------------------------------------------------------------------------------------
       /* GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this, token,
                eventid,
                ApiConstant.pageVisited,
                "25",
                "");
        getUserActivityReport.userActivityReport();*/
        //--------------------------------------------------------------------------------------
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (cd.isConnectingToInternet()) {
            new getQuizList().execute();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
    }

//    public void onBackpressed() {
//
//        Intent intent = new Intent(activity, MrgeHomeActivity.class);
//        activity.startActivity(intent);
//    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class getQuizList extends AsyncTask<Void, Void, Void> {

        String status = "";
        String message = "";
        String logoUrl = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Dismiss the progress dialog
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }

            // Showing progress dialog
            pDialog = new ProgressDialog(getContext(),
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
                    logoUrl = jsonResult.getString("logo_url_path");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (status.equalsIgnoreCase("success")) {


                //Get Folder Parser
                quizFolderParser = new QuizFolderParser();
                quizFolders = quizFolderParser.QuizFolder_Parser(jsonStr);
                quizLogoParser = new QuizLogoParser();
                quizlogo = quizLogoParser.QuizLogo_Parser(jsonStr);


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
            String Logoname = "";
            try {
                if (quizlogo.getApp_quiz_logo() != null) {
                    Logoname = quizlogo.getApp_quiz_logo();
                }
            } catch (NullPointerException e) {

            }
            String finalLogourl = logoUrl + Logoname;

            empty.setTextColor(Color.parseColor(colorActive));
            if (quizFolders.size() != 0) {
                empty.setVisibility(View.GONE);
                adapter = new QuizFolderAdapter(getActivity(), quizFolders, finalLogourl);
                quizNameList.setAdapter(adapter);
            } else {
                empty.setVisibility(View.VISIBLE);
            }

        }
    }


}
