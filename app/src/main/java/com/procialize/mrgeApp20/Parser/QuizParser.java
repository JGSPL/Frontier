package com.procialize.mrgeApp20.Parser;

import android.util.Log;

import com.procialize.mrgeApp20.GetterSetter.Quiz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuizParser {

	JSONObject jsonObj = null;
	JSONObject userJsonObject = null;

	// JSON Node names
	private static final String TAG_QUIZ_LIST = "quiz_list";

	// JSONArray
	JSONArray quiz_list = null;
	ArrayList<Quiz> quizList;
	Quiz quiz;

	public ArrayList<Quiz> Quiz_Parser(String jsonStr, String foldername) {

		quizList = new ArrayList<Quiz>();

		if (jsonStr != null) {
			try {

				jsonObj = new JSONObject(jsonStr);

				// Getting poll info JSON Array node
				quiz_list = jsonObj.getJSONArray(TAG_QUIZ_LIST);
				JSONObject jsonAgenda = null;

				for (int i = 0; i < quiz_list.length(); i++) {
					jsonAgenda = quiz_list.getJSONObject(i);
					quiz = new Quiz();

					String folder = jsonAgenda.getString("folder_name");
					if (folder.trim().equalsIgnoreCase(foldername.trim())) {
						String id = jsonAgenda.getString("id");
						if (id != null && id.length() > 0) {
							quiz.setId(id);
						}
						String question = jsonAgenda.getString("question");
						if (question != null && question.length() > 0) {
							quiz.setQuestion(question);
						}
						String correct_answer = jsonAgenda
								.getString("correct_answer_id");
						if (correct_answer != null && correct_answer.length() > 0) {
							quiz.setCorrect_answer(correct_answer);
						}
						String replied = jsonAgenda.getString("replied");
						if (replied != null && replied.length() > 0) {
							quiz.setReplied(replied);
						}

						String folder_name = jsonAgenda.getString("folder_name");
						if (folder_name != null && folder_name.length() > 0) {
							quiz.setFolder_name(folder_name);
						}

						String selected_option = jsonAgenda.getString("selected_option");
						if (selected_option != null && selected_option.length() > 0) {
							quiz.setSelected_option(selected_option);
						}
						quizList.add(quiz);
					}


				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}
		return quizList;
	}


	public ArrayList<Quiz> Quiz_Parser2(String jsonStr, String folderid) {

		quizList = new ArrayList<Quiz>();

		if (jsonStr != null) {
			try {

				jsonObj = new JSONObject(jsonStr);

				// Getting poll info JSON Array node
				quiz_list = jsonObj.getJSONArray(TAG_QUIZ_LIST);
				JSONObject jsonAgenda = null;

				for (int i = 0; i < quiz_list.length(); i++) {
					jsonAgenda = quiz_list.getJSONObject(i);
					quiz = new Quiz();

					String folder = jsonAgenda.getString("folder_id");
					if (folder.trim().equalsIgnoreCase(folderid)) {
						String id = jsonAgenda.getString("id");
						if (id != null && id.length() > 0) {
							quiz.setId(id);
						}
						String question = jsonAgenda.getString("question");
						if (question != null && question.length() > 0) {
							quiz.setQuestion(question);
						}
						String correct_answer = jsonAgenda
								.getString("correct_answer_id");
						if (correct_answer != null && correct_answer.length() > 0) {
							quiz.setCorrect_answer(correct_answer);
						}
						String replied = jsonAgenda.getString("replied");
						if (replied != null && replied.length() > 0) {
							quiz.setReplied(replied);
						}

						String folder_id = jsonAgenda.getString("folder_id");
						if (folder_id != null && folder_id.length() > 0) {
							quiz.setFolder_name(folder_id);
						}
						String folder_name = jsonAgenda.getString("folder_name");
						if (folder_name != null && folder_name.length() > 0) {
							quiz.setFolder_name(folder_name);
						}

						String selected_option = jsonAgenda.getString("selected_option");
						if (selected_option != null && selected_option.length() > 0) {
							quiz.setSelected_option(selected_option);
						}
						quizList.add(quiz);
					}


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
