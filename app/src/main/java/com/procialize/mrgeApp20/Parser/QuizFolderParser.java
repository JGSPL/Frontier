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

    JSONObject jsonObj = null;
    JSONObject userJsonObject = null;

    // JSON Node names
    private static final String TAG_QUIZ_LIST = "quiz_folder_list";

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
                    if (folder_list!=null && folder_list.length()>0)
                    {
                        quiz.setFolder_name(folder_list);
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
}

