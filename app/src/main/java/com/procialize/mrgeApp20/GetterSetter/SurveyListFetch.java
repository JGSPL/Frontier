package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 12/11/2017.
 */

public class SurveyListFetch {

    @SerializedName("survey_list")
    @Expose
    private List<SurveyList> surveyList = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<SurveyList> getSurveyList() {
        return surveyList;
    }

    public void setSurveyList(List<SurveyList> surveyList) {
        this.surveyList = surveyList;
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
