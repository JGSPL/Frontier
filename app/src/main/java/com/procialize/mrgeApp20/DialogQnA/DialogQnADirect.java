package com.procialize.mrgeApp20.DialogQnA;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.procialize.mrgeApp20.Adapter.QADirectAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.MyApplication;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Utility.Utility.setgradientDrawable;

public class DialogQnADirect {

    static BottomSheetDialog dialog, Detaildialog, ThankyouDialog, ResultDialog;
    private ProgressDialog pDialog;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    APIService mAPIService;
    SessionManager sessionManager;
    String apikey, getQuizUrl = "";
    Activity activityP;
    public static MyApplication appDelegate;

    public Button postbtn;
    public QADirectAdapter qaAttendeeAdapter;
    Dialog myDialog;
    String token;
    SwipeRefreshLayout qaRvrefresh;
    ProgressBar progressBar;
    RecyclerView qaRv;
    ImageView headerlogoIv;
    RelativeLayout linUpper;
    TextView txtEmpty, nmtxt,pullrefresh;
    LinearLayout linear;


    public void welcomeQnADialog(Activity context) {

        // dialog = new BottomSheetDialog(context);
        dialog = new BottomSheetDialog(context, R.style.SheetDialog);

        dialog.setContentView(R.layout.bottom_qna_direct);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setDimAmount(0);

        activityP = context;
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

        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        Button btnQuizStart = dialog.findViewById(R.id.btnQuizStart);
        CardView Quizcard = dialog.findViewById(R.id.Quizcard);
        Quizcard.setBackgroundColor(Color.parseColor("#ffffff"));
        Quizcard.setAlpha(0.8f);


        progressBar = dialog.findViewById(R.id.progressBar);
        qaRvrefresh = dialog.findViewById(R.id.qaRvrefresh);
        postbtn = dialog.findViewById(R.id.postbtn);
        qaRv = dialog.findViewById(R.id.qaRv);
        linUpper = dialog.findViewById(R.id.linUpper);
        txtEmpty = dialog.findViewById(R.id.txtEmpty);
        nmtxt = dialog.findViewById(R.id.nmtxt);
        linear = dialog.findViewById(R.id.linear);
        pullrefresh = dialog.findViewById(R.id.pullrefresh);

        GradientDrawable shape = setgradientDrawable(5, colorActive);
        postbtn.setBackground(shape);


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnQuizStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new getQuizList().execute();
                dialog.dismiss();

            }
        });

        dialog.show();
    }

}
