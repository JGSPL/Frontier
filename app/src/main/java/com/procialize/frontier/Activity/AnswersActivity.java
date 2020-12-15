package com.procialize.frontier.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.procialize.frontier.DbHelper.ConnectionDetector;
import com.procialize.frontier.DbHelper.DBHelper;
import com.procialize.frontier.GetterSetter.Quiz;
import com.procialize.frontier.GetterSetter.QuizOptionList;
import com.procialize.frontier.InnerDrawerActivity.QuizNewActivity;
import com.procialize.frontier.R;

import java.util.ArrayList;
import java.util.List;

public class AnswersActivity extends AppCompatActivity {

    private List<QuizOptionList> quizOptionList = new ArrayList<>();
    ArrayList<QuizOptionList> quizSpecificOptionListnew = new ArrayList<QuizOptionList>();
    ArrayList<Quiz> quizList = new ArrayList<Quiz>();
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private DBHelper dbHelper;
    String[] dataArray;
    String[] dataIDArray;
    String[] checkArray;
    String[] ansArray;
    RadioGroup viewGroup;
    int flag = 0;
    String quiz_options_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        viewGroup = (RadioGroup)
                findViewById(R.id.radiogroup);

        dbHelper = new DBHelper(AnswersActivity.this);

        procializeDB = new DBHelper(AnswersActivity.this);
        db = procializeDB.getWritableDatabase();

        db = procializeDB.getReadableDatabase();


        Intent intent = getIntent();
        String folder_name = intent.getStringExtra("folderName");
        quizOptionList = QuizNewActivity.appDelegate.getQuizOptionList();
        quizList=procializeDB.getQuizList(folder_name);

        quizOptionList = QuizNewActivity.appDelegate.getQuizOptionList();
        if (quizSpecificOptionListnew.size() > 0) {
            quizSpecificOptionListnew.clear();
        }

        quizList = procializeDB.getQuizList(folder_name);
        for (int i = 0; i < quizOptionList.size()-1; i++) {

            if (quizOptionList.get(i).getQuizId().equalsIgnoreCase(quizList.get(i).getId())) {

                QuizOptionList quizTempOptionList = new QuizOptionList();

                quizTempOptionList.setOption(quizOptionList.get(i).getOption());
                quizTempOptionList.setOptionId(quizOptionList.get(i)
                        .getOptionId());
                quizTempOptionList.setQuizId(quizOptionList.get(i)
                        .getQuizId());

                quizSpecificOptionListnew.add(quizTempOptionList);

            }

        }


    }
}