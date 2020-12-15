package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naushad on 12/16/2017.
 */

public class LivePollList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("replied")
    @Expose
    private String replied;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("show_progress_bar")
    @Expose
    private String show_progress_bar;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShow_progress_bar() {
        return show_progress_bar;
    }

    public void setShow_progress_bar(String show_progress_bar) {
        this.show_progress_bar = show_progress_bar;
    }

    public String getShow_result() {
        return show_result;
    }

    public void setShow_result(String show_result) {
        this.show_result = show_result;
    }

    @SerializedName("hide_result")
    @Expose
    private String show_result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReplied() {
        return replied;
    }

    public void setReplied(String replied) {
        this.replied = replied;
    }
}
