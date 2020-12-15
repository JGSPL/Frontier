package com.procialize.frontier.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.GetterSetter.Quiz;
import com.procialize.frontier.GetterSetter.QuizOptionList;
import com.procialize.frontier.InnerDrawerActivity.QuizActivity;
import com.procialize.frontier.InnerDrawerActivity.YourScoreActivity;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.MyApplication;
import com.procialize.frontier.Utility.ServiceHandler;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

public class QuizNewAdapter extends RecyclerView.Adapter<QuizNewAdapter.ViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Quiz> quizList;
    private List<QuizOptionList> quizOptionList = new ArrayList<>();
    static ArrayList<QuizOptionList> quizSpecificOptionListnew = new ArrayList<QuizOptionList>();

    // ArrayList<String> dataArray=new ArrayList<String>();
    private ProgressDialog pDialog;
    String MY_PREFS_NAME = "ProcializeInfo";
    public static String[] dataArray;
    int[] righanswe;
    public static String[] dataIDArray;
    static String[] checkArray;
    String[] ansArray;
    ApiConstant constant = new ApiConstant();
    String quiz_options_id;
    MyApplication appDelegate;
    int remaining;
    public Timer timer;
    String[] flagArray;
    public static long millisInFuture = 10000; //10 seconds
    long countDownInterval = 1000; //1 second
    public int time = 10;
    int flag = 0;
    String correctAnswer;
    private SparseIntArray mSpCheckedState = new SparseIntArray();
    String selectedOption;
    public static int selectopt = 0;
    public TimerTask timerTask;
    String accessToken, event_id, colorActive;
    Typeface typeFace;
    private RadioGroup lastCheckedRadioGroup = null;
    int count = 0;
    private String quizQuestionUrl = "";
    private SessionManager session;
    //    public CountDownTimer W;
    private final int START_TIME = 10000;


    public QuizNewAdapter(Activity activity, List<Quiz> quizList) {
        this.activity = activity;
        this.quizList = quizList;
        dataArray = new String[quizList.size()];
        dataIDArray = new String[quizList.size()];
        checkArray = new String[quizList.size()];
        ansArray = new String[quizList.size()];

        session = new SessionManager(activity.getApplicationContext());
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS_NAME, activity.MODE_PRIVATE);
        event_id = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        colorActive = prefs.getString("colorActive", "");

        quizQuestionUrl = constant.baseUrl + constant.quizsubmit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_row_test, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.setIsRecyclable(false);
        holder.txt_time.setTextColor(Color.parseColor(colorActive));
        holder.textno1.setTextColor(Color.parseColor(colorActive));
        holder.quiz_title_txt.setTextColor(Color.parseColor(colorActive));
        if (quizList.get(position).getQuiz_type() == null) {

            if (holder.raiolayout.getVisibility() == View.GONE) {
                holder.raiolayout.setVisibility(View.VISIBLE);
            }
        try{
            holder.quiz_title_txt.setText(StringEscapeUtils.unescapeJava(quizList.get(position).getQuestion()));

        }catch (IllegalArgumentException e){
            e.printStackTrace();

        }
            quizOptionList = QuizActivity.appDelegate.getQuizOptionList();
            if (quizSpecificOptionListnew.size() > 0) {
                quizSpecificOptionListnew.clear();
            }


            for (int i = 0; i < quizOptionList.size(); i++) {

                if (quizOptionList.get(i).getQuizId().equalsIgnoreCase(quizList.get(position).getId())) {

                    QuizOptionList quizTempOptionList = new QuizOptionList();

                    quizTempOptionList.setOption(quizOptionList.get(i).getOption());
                    quizTempOptionList.setOptionId(quizOptionList.get(i)
                            .getOptionId());
                    quizTempOptionList.setQuizId(quizOptionList.get(i)
                            .getQuizId());

                    quizSpecificOptionListnew.add(quizTempOptionList);

                }

            }

            correctAnswer = quizList.get(position).getCorrect_answer();
            int number = quizSpecificOptionListnew.size() + 1;

            Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth() - 40;
            double ratio = ((float) (width)) / 300.0;
            int height = (int) (ratio * 50);


            for (int row = 0; row < 1; row++) {


                for (int i = 1; i < number; i++) {

                    AppCompatRadioButton rdbtn = new AppCompatRadioButton(activity);
                    rdbtn.setId((row * 2) + i);
                    rdbtn.setTypeface(typeFace);
                    rdbtn.setText(StringEscapeUtils.unescapeJava(quizSpecificOptionListnew.get(i - 1).getOption()));
                    rdbtn.setTextColor(Color.BLACK);
                    rdbtn.setTextSize(14);
//                    rdbtn.setBackgroundResource(R.drawable.livepollback);
                    if (Build.VERSION.SDK_INT >= 21) {

                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{

                                        new int[]{-android.R.attr.state_checked}, //disabled
                                        new int[]{android.R.attr.state_checked} //enabled
                                },
                                new int[]{

                                        Color.parseColor("#585e44")//disabled
                                        , Color.parseColor("#e31e24")//enabled

                                }
                        );


                        rdbtn.setButtonTintList(colorStateList);//set the color tint list
                        rdbtn.invalidate(); //could not be necessary
                    }
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                            width,
                            height
                    );

                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    p.setMargins(10, 15, 10, 10);

                    // rdbtn.setPadding(10,10,10,5);
                    rdbtn.setLayoutParams(p);
                    rdbtn.setTag(quizSpecificOptionListnew.get(i - 1).getOptionId());
                    rdbtn.setBackgroundResource(R.drawable.livepollback);

//                    rdbtn.setButtonDrawable(R.drawable.radio_buttontoggle_first);
                    rdbtn.setPaddingRelative(5, 5, 5, 5);
                    rdbtn.setPadding(15, 15, 15, 15);


                    if (checkArray[position] != null) {
                        if (rdbtn.getText().toString().equalsIgnoreCase(checkArray[position])) {
                            rdbtn.setChecked(true);


                        }
                    }


                    holder.viewGroup.addView(rdbtn);

                    flag = 1;
                }


/*
                for (int i = 1; i < number; i++) {

                    LinearLayout ll = new LinearLayout(activity);

                    LinearLayout l3 = new LinearLayout(activity);
                    FrameLayout fl = new FrameLayout(activity);

                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.setPadding(10, 10, 10, 10);

                    LinearLayout ll2 = new LinearLayout(activity);
                    ll2.setOrientation(LinearLayout.HORIZONTAL);
                    ll2.setPadding(10, 10, 10, 10);

                    LinearLayout.LayoutParams rprms, rprmsRdBtn, rpms2;

                    AppCompatRadioButton rdbtn = new AppCompatRadioButton(activity);
                    rdbtn.setId((row * 2) + i);
                    rdbtn.setText(quizSpecificOptionListnew.get(i - 1).getOption());
                    rdbtn.setTextColor(Color.BLACK);
                    rdbtn.setButtonDrawable(R.drawable.radio_buttontoggle);

//                rdbtn.setTypeface(typeFace);
                   // rdbtn.setOnClickListener(activity);


                    //radios.add(rdbtn);

                    // rdbtn.setBackgroundResource(R.drawable.edit_background);

                    rprms = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    rprms.setMargins(5, 5, 5, 5);

                    Float weight = 0.0f;



                    rpms2 = new LinearLayout.LayoutParams(0,
                            ViewGroup.LayoutParams.MATCH_PARENT, weight);
                    rpms2.setMargins(5, 5, 5, 5);

                    ll.setBackgroundResource(R.drawable.bg_poll);
                    ll.setWeightSum(100);
                    ll.setLayoutParams(rprms);

                    l3.setLayoutParams(rprms);
                    l3.setWeightSum(100);

                    // ll2.setBackgroundColor(Color.parseColor(color[i]));
                    ll2.setLayoutParams(rpms2);

                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    params.gravity = Gravity.CENTER;

                    rprmsRdBtn = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    rprms.setMargins(5, 5, 5, 5);
                    rdbtn.setLayoutParams(rprmsRdBtn);

                    l3.addView(ll2, rpms2);

                    fl.addView(l3, rprms);
                    fl.addView(rdbtn, rprmsRdBtn);

                    // ll2.addView(rdbtn, rprmsRdBtn);
                    ll.addView(fl, params);

                    if (checkArray[position] != null) {
                        if (rdbtn.getText().toString().equalsIgnoreCase(checkArray[position])) {
                            rdbtn.setChecked(true);


                        }
                    }

                    holder.viewGroup.addView(ll, rprms);
                    holder.viewGroup.invalidate();
                    flag = 1;
                }
*/

            }

            for (int i = 0; i < quizSpecificOptionListnew.size(); i++) {

                if (quizSpecificOptionListnew
                        .get(0)
                        .getOption()
                        .equalsIgnoreCase(
                                quizSpecificOptionListnew.get(i).getOption())) {

                    quiz_options_id = quizSpecificOptionListnew.get(i)
                            .getOptionId();
                }

            }
            int genid = holder.viewGroup.getCheckedRadioButtonId();
            AppCompatRadioButton radioButton = holder.viewGroup.findViewById(genid);


            //  value[0] = radioButton.getText().toString();
            if (radioButton != null) {
                dataArray[position] = radioButton.getText().toString();
                dataIDArray[position] = radioButton.getText().toString();
            }
        } else if (quizList.get(position).getQuiz_type().equalsIgnoreCase("2")) {

            if (holder.raiolayout.getVisibility() == View.VISIBLE) {
                holder.raiolayout.setVisibility(View.GONE);
            }

            holder.quiz_title_txt.setText(quizList.get(position).getQuestion());
            holder.quiz_question_distruct.setText(quizList.get(position).getQuestion());


        } else if (quizList.get(position).getQuiz_type().equalsIgnoreCase("1")) {

            if (holder.raiolayout.getVisibility() == View.GONE) {
                holder.raiolayout.setVisibility(View.VISIBLE);
            }

            holder.quiz_title_txt.setText(quizList.get(position).getQuestion());
            quizOptionList = QuizActivity.appDelegate.getQuizOptionList();
            if (quizSpecificOptionListnew.size() > 0) {
                quizSpecificOptionListnew.clear();
            }


            for (int i = 0; i < quizOptionList.size(); i++) {

                if (quizOptionList.get(i).getQuizId().equalsIgnoreCase(quizList.get(position).getId())) {

                    QuizOptionList quizTempOptionList = new QuizOptionList();

                    quizTempOptionList.setOption(quizOptionList.get(i).getOption());
                    quizTempOptionList.setOptionId(quizOptionList.get(i)
                            .getOptionId());
                    quizTempOptionList.setQuizId(quizOptionList.get(i)
                            .getQuizId());

                    quizSpecificOptionListnew.add(quizTempOptionList);

                }

            }


            int number = quizSpecificOptionListnew.size() + 1;

            Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth() - 40;
            double ratio = ((float) (width)) / 300.0;
            int height = (int) (ratio * 50);


            for (int row = 0; row < 1; row++) {
               /* LinearLayout ll = new LinearLayout(activity);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setGravity(Gravity.CENTER);*/
                //  holder.viewGroup.removeAllViews();
                //	if(flag==0)

                for (int i = 1; i < number; i++) {

                    final AppCompatRadioButton rdbtn = new AppCompatRadioButton(activity);
                    rdbtn.setId((row * 2) + i);
                    rdbtn.setTypeface(typeFace);
                    rdbtn.setText(StringEscapeUtils.unescapeJava(quizSpecificOptionListnew.get(i - 1).getOption()));
                    rdbtn.setTextColor(Color.BLACK);
                    rdbtn.setTextSize(9);
                    rdbtn.setBackgroundResource(R.drawable.livepollback);

                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                            width,
                            height
                    );

                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    p.setMargins(10, 15, 10, 10);
                    // rdbtn.setPadding(10,10,10,5);
                    rdbtn.setLayoutParams(p);
                    rdbtn.setTag(quizSpecificOptionListnew.get(i - 1).getOptionId());
                    if (Build.VERSION.SDK_INT >= 21) {

                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{

                                        new int[]{-android.R.attr.state_checked}, //disabled
                                        new int[]{android.R.attr.state_checked} //enabled
                                },
                                new int[]{

                                        Color.parseColor("#585e44")//disabled
                                        , Color.parseColor("#e31e24")//enabled

                                }
                        );


                        rdbtn.setButtonTintList(colorStateList);//set the color tint list
                        rdbtn.invalidate(); //could not be necessary
                    }
//                    rdbtn.setButtonDrawable(R.drawable.radio_buttontoggle_first);
//                    rdbtn.setGravity(Gravity.CENTER);
                    rdbtn.setPadding(15, 15, 15, 15);
                    rdbtn.setPaddingRelative(5, 5, 5, 5);

                    // rdbtn.setCompoundDrawablePadding(5);

//                    ColorStateList colorStateList = new ColorStateList(
//                            new int[][]{
//                                    new int[]{-android.R.attr.state_checked}, // unchecked
//                                    new int[]{android.R.attr.state_checked}  // checked
//                            },
//                            new int[]{
//                                    Integer.parseInt(String.valueOf(activity.getResources().getDrawable(R.drawable.unchecked_radio))),
//                                    Integer.parseInt(String.valueOf(activity.getResources().getDrawable(R.drawable.checked_radio)))
//                            }
//                    );
//                    rdbtn.setSupportButtonTintList(colorStateList);
                    //  rdbtn.setOnCheckedChangeListener(activity.this);

//                    if (i == 1)
//                        rdbtn.setChecked(true);

                    if (checkArray[position] != null) {
                        if (rdbtn.getText().toString().equalsIgnoreCase(checkArray[position])) {
                            rdbtn.setChecked(true);

                        }
                    }


                    holder.viewGroup.addView(rdbtn);

                    flag = 1;
                }


            }

            for (int i = 0; i < quizSpecificOptionListnew.size(); i++) {

                if (quizSpecificOptionListnew
                        .get(i)
                        .getOption()
                        .equalsIgnoreCase(
                                quizSpecificOptionListnew.get(i).getOption())) {

                    quiz_options_id = quizSpecificOptionListnew.get(i)
                            .getOptionId();
                }

            }
            int genid = holder.viewGroup.getCheckedRadioButtonId();
            AppCompatRadioButton radioButton = holder.viewGroup.findViewById(genid);


            if (radioButton != null) {
                dataArray[position] = radioButton.getText().toString();
                dataIDArray[position] = radioButton.getText().toString();
            }


        }


        final TextWatcher txwatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start,
                                          int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                dataArray[position] = s.toString();
                ansArray[position] = s.toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                System.out.print("Hello");
            }
        };


//        holder.ans_edit.addTextChangedListener(txwatcher);
        holder.viewGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int genid = radioGroup.getCheckedRadioButtonId();
                int nonchecked = quizList.indexOf(correctAnswer);
                AppCompatRadioButton radioButton = radioGroup.findViewById(genid);
//                AppCompatRadioButton radioButton2 = radioGroup.findViewById(nonchecked);
                //  value[0] = radioButton.getText().toString();
                dataArray[position] = radioButton.getText().toString();
                checkArray[position] = radioButton.getText().toString();
//                 = radioButton.getText().toString();
//                if (quizOptionList.size() == 1) {
//                    selectedOption = quizOptionList.get(i).getOptionId();
//                } else {
                selectedOption = quizSpecificOptionListnew.get(i - 1).getOptionId();
                selectopt = selectopt + 1;
//                }
//                correctAnswer=quizList.get(position).getCorrect_answer();
                if (selectedOption.equalsIgnoreCase(correctAnswer)) {
                    count = count + 1;
                }
            }
        });

        timer = new Timer();
        timerTask = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        holder.txt_time.setText(String.format(Locale.getDefault(), "%d", time));
                        if (time > 0)
                            time -= 1;
                        else {
                            if (QuizActivity.submitflag == false) {

                                String selected_opt = quizList.get(position).getSelected_option();
                                int opt = 1;
                                if (selected_opt == "null") {

                                    int items = getItemCount();
//                adapter.getItemViewType(llm.findLastVisibleItemPosition());
                                    if (QuizActivity.count1 == QuizActivity.llm.findLastVisibleItemPosition() + 1) {
//                        if (option != QuizActivity.llm.findLastVisibleItemPosition()) {

                                        QuizActivity.quizNameList.getLayoutManager().scrollToPosition(QuizActivity.llm.findLastVisibleItemPosition() + 1);

                                        if (items < QuizActivity.count1) {
                                            QuizActivity.count1 = 1;
                                        }
                                        QuizActivity.txt_count.setText(QuizActivity.count1 + 1 + "/" + items);
                                        QuizActivity.count1 = QuizActivity.count1 + 1;


                                        if (quizSpecificOptionListnew.size() > 1) {
                                            selectedOption = quizSpecificOptionListnew.get(1).getOptionId();
                                        } else {
                                            selectedOption = quizSpecificOptionListnew.get(0).getOptionId();
                                        }


                                        if (quizSpecificOptionListnew.size() > 1) {
                                            if (selectedOption.equalsIgnoreCase(correctAnswer)) {
                                                selectopt = selectopt + 1;
                                                opt = 0;
                                            } else {
                                                selectopt = selectopt + 1;
                                                opt = 1;
                                            }
                                        } else {
                                            selectopt = 0;
                                            opt = 0;
                                        }

                                        dataArray[QuizActivity.llm.findLastVisibleItemPosition()] = quizSpecificOptionListnew.get(opt).getOption();
                                        checkArray[QuizActivity.llm.findLastVisibleItemPosition()] = quizSpecificOptionListnew.get(opt).getOption();


                                        if (quizList.size() == QuizActivity.llm.findLastVisibleItemPosition() + 2) {

                                            QuizActivity.btnNext.setVisibility(View.GONE);
                                            QuizActivity.submit.setVisibility(View.VISIBLE);
                                            if (timer != null) {
                                                timer.cancel();
                                                timer = null;
                                                dataIDArray = null;

                                            }
                                        } else if (quizList.size() >= QuizActivity.llm.findLastVisibleItemPosition() + 2) {
                                            QuizActivity.btnNext.setVisibility(View.VISIBLE);
                                            QuizActivity.submit.setVisibility(View.GONE);
                                            if (timer != null) {
                                                timer.cancel();
                                                timer = null;
                                                dataIDArray = null;

                                            }
                                        } else {

                                            if (timer != null) {
                                                timer.cancel();
                                                timer = null;
                                                dataIDArray = null;

                                            }
                                            if (QuizActivity.submit.getVisibility() == View.VISIBLE) {
                                                if (dataArray.length == getItemCount()) {
                                                    Boolean valid = true;
                                                    final int[] check = {0};
                                                    int sum = 0;
                                                    final String[] question_id = {""};
                                                    final String[] question_ans = {""};
                                                    final String[] value = {""};
                                                    final RadioGroup[] radioGroup = new RadioGroup[1];
                                                    final EditText[] ans_edit = new EditText[1];
                                                    final RadioButton[] radioButton = new RadioButton[1];
                                                    Log.e("size", getItemCount() + "");


                                                    String[] data = getselectedData();
                                                    String[] question = getselectedquestion();

                                                    if (data != null) {
                                                        for (int i = 0; i < data.length; i++) {
                                                            if (i != 0) {
                                                                question_id[0] = question_id[0] + "$#";
                                                                question_ans[0] = question_ans[0] + "$#";
                                                            }

                                                            String id = quizList.get(i).getId();
                                                            question_id[0] = question_id[0] + id;

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
                                                                            valid = false;
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
                                                                        valid = false;
                                                                    }
                                                                }
                                                            } else {
                                                                valid = false;
                                                            }

                                                        }


                                                        Log.e("valid", question_ans.toString());
                                                        Log.e("valid", question_id.toString());
                                                        Log.e("valid", valid.toString());


                                                        if (valid == true) {
                                                            QuizActivity.quiz_question_id = question_id[0];
                                                            quiz_options_id = question_ans[0];
                                                            int answers = getCorrectOption();
                                                            if (QuizActivity.count1 == 1) {

                                                            } else {
                                                                new postQuizQuestion().execute();
                                                            }

//                    Intent intent = new Intent(QuizActivity.this, YourScoreActivity.class);
//                    intent.putExtra("folderName", foldername);
//                    intent.putExtra("Answers", answers);
//                    intent.putExtra("TotalQue", adapter.getselectedData().length);
//                    startActivity(intent);
                                                        } else {
//                                            Toast.makeText(activity, "Please answer all questions", Toast.LENGTH_SHORT).show();
                                                        }


                                                    }
                                                }
                                            } else {
//                                    Toast.makeText(activity, "Please Select Option", Toast.LENGTH_SHORT).show();

                                                if (timer != null) {
                                                    timer.cancel();
                                                    timer = null;
                                                    dataIDArray = null;

                                                }
                                            }

                                        }
                                    } else {

                                        if (QuizActivity.submit.getVisibility() == View.VISIBLE) {
                                            if (dataArray.length == getItemCount()) {
                                                Boolean valid = true;
                                                final int[] check = {0};
                                                int sum = 0;
                                                final String[] question_id = {""};
                                                final String[] question_ans = {""};
                                                final String[] value = {""};
                                                final RadioGroup[] radioGroup = new RadioGroup[1];
                                                final EditText[] ans_edit = new EditText[1];
                                                final RadioButton[] radioButton = new RadioButton[1];
                                                Log.e("size", getItemCount() + "");


                                                String[] data = getselectedData();
                                                String[] question = getselectedquestion();

                                                if (data != null) {
                                                    for (int i = 0; i < data.length; i++) {
                                                        if (i != 0) {
                                                            question_id[0] = question_id[0] + "$#";
                                                            question_ans[0] = question_ans[0] + "$#";
                                                        }

                                                        String id = quizList.get(i).getId();
                                                        question_id[0] = question_id[0] + id;

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
                                                                        valid = false;
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
                                                                    valid = false;
                                                                }
                                                            }
                                                        } else {
                                                            valid = false;
                                                        }

                                                    }


                                                    Log.e("valid", question_ans.toString());
                                                    Log.e("valid", question_id.toString());
                                                    Log.e("valid", valid.toString());


                                                    if (valid == true) {
                                                        QuizActivity.quiz_question_id = question_id[0];
                                                        quiz_options_id = question_ans[0];
                                                        int answers = getCorrectOption();
                                                        if (QuizActivity.count1 == 1) {

                                                        } else {
                                                            new postQuizQuestion().execute();
                                                        }
//                    Intent intent = new Intent(QuizActivity.this, YourScoreActivity.class);
//                    intent.putExtra("folderName", foldername);
//                    intent.putExtra("Answers", answers);
//                    intent.putExtra("TotalQue", adapter.getselectedData().length);
//                    startActivity(intent);
                                                    } else {

//                                            Toast.makeText(activity, "Please answer all questions", Toast.LENGTH_SHORT).show();
                                                    }


                                                }
                                            }
                                        } else {
//                            Toast.makeText(activity, "Please Select Option", Toast.LENGTH_SHORT).show();

                                            if (timer != null) {
                                                timer.cancel();
                                                timer = null;
                                                dataIDArray = null;

                                            }
                                        }
                                    }

                                }

                            }
                        }

                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);




    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public String[] getselectedData() {
        return dataArray;

    }

    public String[] getselectedquestion() {
        return dataIDArray;

    }

    public int getSelectedOption() {
        return selectopt;

    }

    public int getCorrectOption() {
        return count;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView quiz_title_txt, quiz_question_distruct, textno1, txt_time;
        LinearLayout raiolayout;
        //        EditText ans_edit;
        RadioGroup viewGroup;

        public ViewHolder(View convertView) {
            super(convertView);
            quiz_title_txt = (TextView) convertView
                    .findViewById(R.id.quiz_question);

            raiolayout = (LinearLayout) convertView
                    .findViewById(R.id.raiolayout);

            viewGroup = (RadioGroup) convertView
                    .findViewById(R.id.radiogroup);

            textno1 = (TextView) convertView
                    .findViewById(R.id.textno1);

            txt_time = (TextView) convertView
                    .findViewById(R.id.txt_time);

//            textno = (TextView) convertView
//                    .findViewById(R.id.textno);


            Typeface typeFace = Typeface.createFromAsset(activity.getAssets(),
                    "DINPro-Light_13935.ttf");

            Typeface typeFace1 = Typeface.createFromAsset(activity.getAssets(),
                    "DINPro-Light_13935.ttf");
            quiz_title_txt.setTypeface(typeFace1);

            textno1.setTypeface(typeFace);


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
                pDialog = new ProgressDialog(activity,
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
                    accessToken));
            nameValuePair.add(new BasicNameValuePair("event_id",
                    event_id));
            nameValuePair.add(new BasicNameValuePair("quiz_id", QuizActivity.quiz_question_id));
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

            // Dismiss the progress dialog
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }

            if (error.equalsIgnoreCase("success")) {
                int answers = getCorrectOption();
                Intent intent = new Intent(activity, YourScoreActivity.class);
                intent.putExtra("folderName", QuizActivity.foldername);
                intent.putExtra("Answers", total_correct_answer);
                intent.putExtra("TotalQue", total_questions);
                activity.startActivity(intent);
                activity.finish();
                Toast.makeText(activity, message,
                        Toast.LENGTH_SHORT).show();
                QuizActivity.count1 = 1;
                selectopt = 0;

                if (timer != null) {
                    timer.cancel();
                    timer = null;
                    dataIDArray = null;
                }
            } else {

                Toast.makeText(activity, message,
                        Toast.LENGTH_SHORT).show();
            }

        }
    }


}