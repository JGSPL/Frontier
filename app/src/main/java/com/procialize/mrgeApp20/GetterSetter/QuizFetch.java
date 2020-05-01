package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 12/16/2017.
 */

public class QuizFetch {
    @SerializedName("quiz_list")
    @Expose
    private List<QuizList> quizList = null;
    @SerializedName("quiz_option_list")
    @Expose
    private List<QuizOptionList> quizOptionList = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<QuizList> getQuizList() {
        return quizList;
    }

    public void setQuizList(List<QuizList> quizList) {
        this.quizList = quizList;
    }

    public List<QuizOptionList> getQuizOptionList() {
        return quizOptionList;
    }

    public void setQuizOptionList(List<QuizOptionList> quizOptionList) {
        this.quizOptionList = quizOptionList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
