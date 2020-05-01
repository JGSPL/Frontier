package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naushad on 12/16/2017.
 */

public class QuizList {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("correct_answer_id")
    @Expose
    private String correctAnswerId;
    @SerializedName("replied")
    @Expose
    private String replied;

    @SerializedName("folder_id")
    @Expose
    private String folder_id;

    @SerializedName("folder_name")
    @Expose
    private String folder_name;

    @SerializedName("selected_option")
    @Expose
    private String selected_option;

    public String getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(String folder_id) {
        this.folder_id = folder_id;
    }

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public String getSelected_option() {
        return selected_option;
    }

    public void setSelected_option(String selected_option) {
        this.selected_option = selected_option;
    }

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

    public String getCorrectAnswerId() {
        return correctAnswerId;
    }

    public void setCorrectAnswerId(String correctAnswerId) {
        this.correctAnswerId = correctAnswerId;
    }

    public String getReplied() {
        return replied;
    }

    public void setReplied(String replied) {
        this.replied = replied;
    }
}
