package com.procialize.frontier.Activity;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.GetterSetter.QuizOptionList;
import com.procialize.frontier.GetterSetter.QuizSubmitFetch;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.frontier.Utility.Utility.setgradientDrawable;

public class QuizDetailActivity extends AppCompatActivity {

    String id, question, replied;
    List<QuizOptionList> optionLists;
    TextView questionTv;
    RadioGroup ll;
    Button subBtn;
    ProgressBar progressBar;
    String selected;
    int Count;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
     //   toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        headerlogoIv = findViewById(R.id.headerlogoIv);
        progressBar = findViewById(R.id.progressBar);
        Util.logomethod(this, headerlogoIv);


        optionLists = new ArrayList<>();
        mAPIService = ApiUtils.getAPIService();

        questionTv = findViewById(R.id.questionTv);
        subBtn = findViewById(R.id.subBtn);

        GradientDrawable shape = setgradientDrawable(5, colorActive);

        subBtn.setBackground(shape);

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);


        try {
            id = getIntent().getExtras().getString("id");
            question = getIntent().getExtras().getString("question");
            replied = getIntent().getExtras().getString("replied");
            optionLists = (List<QuizOptionList>) getIntent().getSerializableExtra("optionlist");

        } catch (Exception e) {
            e.printStackTrace();
        }


        questionTv.setText(question);
        questionTv.setTextColor(Color.parseColor(colorActive));

        if (optionLists.size() != 0) {

            for (int row = 0; row < 1; row++) {
                ll = findViewById(R.id.radiogroup);
                ll.setOrientation(LinearLayout.VERTICAL);

                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.MATCH_PARENT
                );
                params.setMargins(15, 15, 15, 0);
                ColorStateList colorStateList;

                colorStateList = new ColorStateList(

                        new int[][]{

                                new int[]{-android.R.attr.state_checked}, //disabled
                                new int[]{android.R.attr.state_checked} //enabled
                        },
                        new int[]{

                                Color.BLACK //disabled
                                , Color.GREEN //enabled

                        }
                );

//                ll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(RadioGroup group, int checkedId) {
//                        group.setBackground(getDrawable(R.drawable.button_on));
//                    }
//                });

                for (int i = 0; i < optionLists.size(); i++) {
                    if (optionLists.get(i).getQuizId().equalsIgnoreCase(id)) {
                        AppCompatRadioButton rdbtn = new AppCompatRadioButton(this);
                        rdbtn.setId((row * 2) + i);
                        rdbtn.setLayoutParams(params);
                        rdbtn.setBackgroundResource(R.drawable.agendabg);

                        rdbtn.setButtonDrawable(R.drawable.radio);
//                        rdbtn.setButtonTintList(colorStateList);
                        rdbtn.setTextColor(Color.parseColor("#FFFFFF"));
                        rdbtn.setText(optionLists.get(i).getOption());
//                        if (rdbtn.isSelected()) {
//                            rdbtn.setButtonDrawable(R.drawable.button_on);
//                        } else {
//                            rdbtn.setButtonDrawable(R.drawable.button_off);
//                        }
                        ll.addView(rdbtn);
                    }
                }
//                ((ViewGroup) findViewById(R.id.radiogroup)).addView(ll);

            }
        }

        ll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                for (int i = 0; i < rg.getChildCount(); i++) {
                    RadioButton btn = (RadioButton) rg.getChildAt(i);
                    if (btn.getId() == checkedId) {
                        selected = btn.getText().toString();
                        // do something with text
                        return;
                    }
                }
            }
        });

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selected != null) {
                    String optionid = "";

                    for (int j = 0; j < optionLists.size(); j++) {
                        if (optionLists.get(j).getOption().equals(selected)) {
                            optionid = optionLists.get(j).getOptionId();
                        }
                    }

                    QuizSubmitFetch(token, eventid, id, optionid);

                } else {
                    Toast.makeText(getApplicationContext(), "Please select something", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void QuizSubmitFetch(String token, String eventid, String quizid, String quizoptionid) {
        showProgress();
        mAPIService.QuizSubmitFetch(token, eventid, quizid, quizoptionid).enqueue(new Callback<QuizSubmitFetch>() {
            @Override
            public void onResponse(Call<QuizSubmitFetch> call, Response<QuizSubmitFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    dismissProgress();
                    showResponse(response);
                } else {

                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuizSubmitFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
            }
        });
    }

    public void showResponse(Response<QuizSubmitFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {


            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

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


}
