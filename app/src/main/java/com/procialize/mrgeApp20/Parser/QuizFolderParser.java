package com.procialize.mrgeApp20.Parser;

import android.util.Log;

import com.procialize.mrgeApp20.GetterSetter.QuizFolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Naushad on 2/14/2018.
 */

public class QuizFolderParser {

    // JSON Node names
    private static final String TAG_QUIZ_LIST = "quiz_folder_list";

    JSONObject jsonObj = null;
    JSONObject userJsonObject = null;
    // JSONArray
    JSONArray quiz_list = null;
    ArrayList<QuizFolder> quizList;
    QuizFolder quiz;

    public ArrayList<QuizFolder> QuizFolder_Parser(String jsonStr) {

        quizList = new ArrayList<QuizFolder>();

        if (jsonStr != null) {
            try {

                jsonObj = new JSONObject(jsonStr);

                // Getting poll info JSON Array node
                quiz_list = jsonObj.getJSONArray(TAG_QUIZ_LIST);
                JSONObject jsonAgenda = null;

                for (int i = 0; i < quiz_list.length(); i++) {
                    jsonAgenda = quiz_list.getJSONObject(i);
                    quiz = new QuizFolder();

                    String folder_list = jsonAgenda.getString("folder_name");
                    if (folder_list != null && folder_list.length() > 0) {
                        quiz.setFolder_name(folder_list);
                    }

                    String timer = jsonAgenda.getString("timer");
                    if (timer != null && timer.length() > 0) {
                        quiz.setTimer(timer);
                    }

                    String answered = jsonAgenda.getString("answered");
                    if (answered != null && answered.length() > 0) {
                        quiz.setAnswered(answered);
                    }

                    String total_quiz = jsonAgenda.getString("total_quiz");
                    if (total_quiz != null && total_quiz.length() > 0) {
                        quiz.setTotal_quiz(total_quiz);
                    }
                    String total_correct = jsonAgenda.getString("total_correct");
                    if (total_correct != null && total_correct.length() > 0) {
                        quiz.setTotal_correct(total_correct);
                    }

                    quizList.add(quiz);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return quizList;
    }

    public ArrayList<QuizFolder> QuizFolder_Parser2(String jsonStr) {

        quizList = new ArrayList<QuizFolder>();

        if (jsonStr != null) {
            try {

                jsonObj = new JSONObject(jsonStr);

                // Getting poll info JSON Array node
                quiz_list = jsonObj.getJSONArray(TAG_QUIZ_LIST);
                JSONObject jsonAgenda = null;

                for (int i = 0; i < quiz_list.length(); i++) {
                    jsonAgenda = quiz_list.getJSONObject(i);
                    quiz = new QuizFolder();

                    String folder_list = jsonAgenda.getString("folder_name");
                    if (folder_list != null && folder_list.length() > 0) {
                        quiz.setFolder_name(folder_list);
                    }

                    String timer = jsonAgenda.getString("timer");
                    if (timer != null && timer.length() > 0) {
                        quiz.setTimer(timer);
                    }

                    String id = jsonAgenda.getString("id");
                    if (id != null && id.length() > 0) {
                        quiz.setFolder_id(id);
                    }

                   /* String answered = jsonAgenda.getString("answered");
                    if (answered != null && answered.length() > 0) {
                        quiz.setAnswered(answered);
                    }

                    String total_quiz = jsonAgenda.getString("total_quiz");
                    if (total_quiz != null && total_quiz.length() > 0) {
                        quiz.setTotal_quiz(total_quiz);
                    }
                    String total_correct = jsonAgenda.getString("total_correct");
                    if (total_correct != null && total_correct.length() > 0) {
                        quiz.setTotal_correct(total_correct);
                    }*/

                    quizList.add(quiz);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return quizList;
    }

}

