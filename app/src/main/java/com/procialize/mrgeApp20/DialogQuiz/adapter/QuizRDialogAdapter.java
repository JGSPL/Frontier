package com.procialize.mrgeApp20.DialogQuiz.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

import com.procialize.mrgeApp20.Adapter.AnswerAdapter;
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

public class QuizRDialogAdapter extends RecyclerView.Adapter<QuizRDialogAdapter.ViewHolder> {
    private Context context;
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


    public QuizRDialogAdapter(Context context, List<Quiz> quizList) {
        this.context = context;
        this.quizList = quizList;
        dataArray = new String[quizList.size()];
        dataIDArray = new String[quizList.size()];
        checkArray = new String[quizList.size()];
        ansArray = new String[quizList.size()];
        procializeDB = new DBHelper(context);
        db = procializeDB.getWritableDatabase();

        db = procializeDB.getReadableDatabase();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @SuppressLint("RestrictedApi")
    @Override
    public QuizRDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_row_list, parent, false);

        QuizRDialogAdapter.ViewHolder viewHolder = new QuizRDialogAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(QuizRDialogAdapter.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        if (quizList.get(position).getQuiz_type() == null) {

            holder.txt_question.setText(StringEscapeUtils.unescapeJava(quizList.get(position).getQuestion()));
            if (holder.raiolayout.getVisibility() == View.GONE) {
                holder.raiolayout.setVisibility(View.VISIBLE);
            }

            holder.txt_question.setTextColor(Color.parseColor(colorActive));
            quizOptionList = QuizNewActivity.appDelegate.getQuizOptionList();
            if (quizSpecificOptionListnew.size() > 0) {
                quizSpecificOptionListnew.clear();
            }


            for (int i = 0; i < quizOptionList.size(); i++) {

                if (quizOptionList.get(i).getQuizId().equalsIgnoreCase(quizList.get(position).getId())) {

                    QuizOptionList quizTempOptionList = new QuizOptionList();

                    quizTempOptionList.setOption(StringEscapeUtils.unescapeJava(quizOptionList.get(i).getOption()));
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

            adapter = new AnswerAdapter(context, quizSpecificOptionListnew, correctAnswer, selectedAnswer);
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


            raiolayout = (LinearLayout) convertView
                    .findViewById(R.id.raiolayout);
            ansList = (RecyclerView) convertView
                    .findViewById(R.id.ansList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            ansList.setLayoutManager(mLayoutManager);

            txt_question = convertView.findViewById(R.id.txt_question);


        }
    }



}