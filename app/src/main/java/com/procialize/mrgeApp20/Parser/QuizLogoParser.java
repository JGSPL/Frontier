package com.procialize.mrgeApp20.Parser;

import android.util.Log;

import com.procialize.mrgeApp20.GetterSetter.QuizLogo;

import org.json.JSONException;
import org.json.JSONObject;

public class QuizLogoParser {
    private static final String TAG_QUIZ_LOGO = "quiz_logo";
    JSONObject userJsonObject = null;

    // JSON Node Names

    QuizLogo Quizlogo;

    public QuizLogo QuizLogo_Parser(String jsonStr) {
        Quizlogo = new QuizLogo();

        if (jsonStr != null) {
            try {
                userJsonObject = new JSONObject(jsonStr);
                JSONObject user = userJsonObject
                        .getJSONObject(TAG_QUIZ_LOGO);

                String attendeeId = user.getString("app_quiz_logo");

                if (attendeeId != null && attendeeId.length() > 0) {
                    Quizlogo.setApp_quiz_logo(attendeeId);
                }

                String apiAccessToken = user.getString("web_quiz_logo");
                if (apiAccessToken != null && apiAccessToken.length() > 0) {
                    Quizlogo.setWeb_quiz_logo(apiAccessToken);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        // constant.USER_PROFILE_DB_LIST = userData;
        return Quizlogo;
    }
}
