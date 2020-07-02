package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 12/20/2017.
 */

public class QASpeakerFetch {
    @SerializedName("speaker_question_list")
    @Expose
    private List<SpeakerQuestionList> speakerQuestionList = null;
    @SerializedName("question_speaker_list")
    @Expose
    private List<QuestionSpeakerList> questionSpeakerList = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("profile_pic_url_path")
    @Expose
    private String profile_pic_url_path;

    public String getProfile_pic_url_path() {
        return profile_pic_url_path;
    }

    public void setProfile_pic_url_path(String profile_pic_url_path) {
        this.profile_pic_url_path = profile_pic_url_path;
    }

    public List<SpeakerQuestionList> getSpeakerQuestionList() {
        return speakerQuestionList;
    }

    public void setSpeakerQuestionList(List<SpeakerQuestionList> speakerQuestionList) {
        this.speakerQuestionList = speakerQuestionList;
    }

    public List<QuestionSpeakerList> getQuestionSpeakerList() {
        return questionSpeakerList;
    }

    public void setQuestionSpeakerList(List<QuestionSpeakerList> questionSpeakerList) {
        this.questionSpeakerList = questionSpeakerList;
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
