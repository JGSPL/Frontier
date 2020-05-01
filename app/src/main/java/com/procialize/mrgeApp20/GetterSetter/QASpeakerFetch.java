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
