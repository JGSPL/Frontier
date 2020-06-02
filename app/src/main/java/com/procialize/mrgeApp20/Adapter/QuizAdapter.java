package com.procialize.mrgeApp20.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.Quiz;
import com.procialize.mrgeApp20.GetterSetter.QuizOptionList;
import com.procialize.mrgeApp20.InnerDrawerActivity.QuizNewActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Utility.MyApplication;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naushad on 10/31/2017.
 */

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Quiz> quizList;
    private List<Quiz> question;
    private List<QuizOptionList> quizOptionList = new ArrayList<>();
    ArrayList<QuizOptionList> quizSpecificOptionListnew = new ArrayList<QuizOptionList>();
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    // ArrayList<String> dataArray=new ArrayList<String>();
    String MY_PREFS_NAME = "ProcializeInfo";

    String[] dataArray;
    int[] righanswe;
    String[] dataIDArray;
    String[] checkArray;
    String[] ansArray;
    ApiConstant constant = new ApiConstant();
    String quiz_options_id;
    MyApplication appDelegate;

    String[] flagArray;

    int flag = 0;
    String correctAnswer;
    String selectedAnswer, colorActive;
    private SparseIntArray mSpCheckedState = new SparseIntArray();
    String selectedOption;
    int selectopt = 0;

    Typeface typeFace;
    private RadioGroup lastCheckedRadioGroup = null;
    int count = 0;
    AnswerAdapter adapter;


    public QuizAdapter(Activity activity, List<Quiz> quizList) {
        this.activity = activity;
        this.quizList = quizList;
        dataArray = new String[quizList.size()];
        dataIDArray = new String[quizList.size()];
        checkArray = new String[quizList.size()];
        ansArray = new String[quizList.size()];
        procializeDB = new DBHelper(activity);
        db = procializeDB.getWritableDatabase();

        db = procializeDB.getReadableDatabase();
        SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS_NAME, activity.MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @SuppressLint("RestrictedApi")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_row_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        if (quizList.get(position).getQuiz_type() == null) {
            try{
            holder.txt_question.setText(StringEscapeUtils.unescapeJava(quizList.get(position).getQuestion()));
            }catch (IllegalArgumentException e){
                e.printStackTrace();

            }

            if (holder.raiolayout.getVisibility() == View.GONE) {
                holder.raiolayout.setVisibility(View.VISIBLE);
            }

           // holder.txt_question.setTextColor(Color.parseColor(colorActive));
            quizOptionList = QuizNewActivity.appDelegate.getQuizOptionList();
            if (quizSpecificOptionListnew.size() > 0) {
                quizSpecificOptionListnew.clear();
            }


            for (int i = 0; i < quizOptionList.size(); i++) {

                if (quizOptionList.get(i).getQuizId().equalsIgnoreCase(quizList.get(position).getId())) {

                    QuizOptionList quizTempOptionList = new QuizOptionList();
                    try{
                    quizTempOptionList.setOption(StringEscapeUtils.unescapeJava(quizOptionList.get(i).getOption()));
                    }catch (IllegalArgumentException e){
                        e.printStackTrace();

                    }
                    quizTempOptionList.setOptionId(quizOptionList.get(i)
                            .getOptionId());
                    quizTempOptionList.setQuizId(quizOptionList.get(i)
                            .getQuizId());

                    quizSpecificOptionListnew.add(quizTempOptionList);

                }

            }

            correctAnswer = quizList.get(position).getCorrect_answer();
            selectedAnswer = quizList.get(position).getSelected_option();

            if (correctAnswer.equalsIgnoreCase(selectedAnswer)) {
                count = count + 1;
            }

            int number = quizSpecificOptionListnew.size() + 1;

            adapter = new AnswerAdapter(activity, quizSpecificOptionListnew, correctAnswer, selectedAnswer);
            holder.ansList.setAdapter(adapter);

        }

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

        //        TextView quiz_title_txt, quiz_question_distruct, textno1, textno;
        LinearLayout raiolayout;
        RecyclerView ansList;
        TextView txt_question;
        //        EditText ans_edit;
//        RadioGroup viewGroup;

        public ViewHolder(View convertView) {
            super(convertView);
//            quiz_title_txt = (TextView) convertView
//                    .findViewById(R.id.quiz_question);
            //quiz_title_txt.setTag(position);

//            quiz_question_distruct = (TextView) convertView
//                    .findViewById(R.id.quiz_question_distruct);
//
//            ans_edit = (EditText) convertView
//                    .findViewById(R.id.ans_edit);
//
//            discript_layout = (LinearLayout) convertView
//                    .findViewById(R.id.discript_layout);

            raiolayout = (LinearLayout) convertView
                    .findViewById(R.id.raiolayout);
            ansList = (RecyclerView) convertView
                    .findViewById(R.id.ansList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
            ansList.setLayoutManager(mLayoutManager);

            txt_question = convertView.findViewById(R.id.txt_question);

//            viewGroup = (RadioGroup) convertView
//                    .findViewById(R.id.radiogroup);

//            textno1 = (TextView) convertView
//                    .findViewById(R.id.textno1);

//            textno = (TextView) convertView
//                    .findViewById(R.id.textno);


//            Typeface typeFace = Typeface.createFromAsset(activity.getAssets(),
//                    "Roboto-Light.ttf");
//
//            Typeface typeFace1 = Typeface.createFromAsset(activity.getAssets(),
//                    "Roboto-Light.ttf");
//            quiz_title_txt.setTypeface(typeFace1);
//            quiz_question_distruct.setTypeface(typeFace);
//            ans_edit.setTypeface(typeFace);
//            textno.setTypeface(typeFace);
//            textno1.setTypeface(typeFace);

//            viewGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup radioGroup, int i) {
//
//
//
//                    dataArray[position]=quizList.get(position).getQuestion();
//
////                    since only one package is allowed to be selected
////                    this logic clears previous selection
////                    it checks state of last radiogroup and
////                     clears it if it meets conditions
//                    if (lastCheckedRadioGroup != null
//                            && lastCheckedRadioGroup.getCheckedRadioButtonId()
//                            != radioGroup.getCheckedRadioButtonId()
//                            && lastCheckedRadioGroup.getCheckedRadioButtonId() != -1) {
//                        lastCheckedRadioGroup.clearCheck();
//
////                        Toast.makeText(QuizNewAdapter.this.context,
////                                "Radio button clicked " + radioGroup.getCheckedRadioButtonId(),
////                                Toast.LENGTH_SHORT).show();
//
//                    }
//                    lastCheckedRadioGroup = radioGroup;
//
//                }
//            });
        }
    }



}