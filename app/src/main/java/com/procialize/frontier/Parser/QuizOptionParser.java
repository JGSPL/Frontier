package com.procialize.frontier.Parser;

import android.util.Log;

import com.procialize.frontier.GetterSetter.QuizOptionList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuizOptionParser {

	JSONObject jsonObj = null;
	JSONObject userJsonObject = null;

	// JSON Node names
	private static final String TAG_QUIZ_OPTION_LIST = "quiz_option_list";

	// JSONArray
	JSONArray quiz_option_list = null;
	ArrayList<QuizOptionList> quizOptionList;
	QuizOptionList quizOption;

	public ArrayList<QuizOptionList> Quiz_Option_Parser(String jsonStr) {

		quizOptionList = new ArrayList<QuizOptionList>();

		if (jsonStr != null) {
			try {

				jsonObj = new JSONObject(jsonStr);

				// Getting poll info JSON Array node
				quiz_option_list = jsonObj.getJSONArray(TAG_QUIZ_OPTION_LIST);
				JSONObject jsonAgenda = null;

				for (int i = 0; i < quiz_option_list.length(); i++) {
					jsonAgenda = quiz_option_list.getJSONObject(i);
					quizOption = new QuizOptionList();

					String quiz_id = jsonAgenda.getString("quiz_id");
					if (quiz_id != null && quiz_id.length() > 0) {
						quizOption.setQuizId(quiz_id);
					}
					String option_id = jsonAgenda.getString("option_id");
					if (option_id != null && option_id.length() > 0) {
						quizOption.setOptionId(option_id);
					}
					String option = jsonAgenda.getString("option");
					if (option != null && option.length() > 0) {
						quizOption.setOption(option);
					}

					quizOptionList.add(quizOption);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}
		return quizOptionList;
	}
}
