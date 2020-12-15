package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Naushad on 12/16/2017.
 */

public class QuizOptionList implements Serializable {

    @SerializedName("quiz_id")
    @Expose
    private String quizId;
    @SerializedName("option_id")
    @Expose
    private String optionId;
    @SerializedName("option")
    @Expose
    private String option;

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

}
