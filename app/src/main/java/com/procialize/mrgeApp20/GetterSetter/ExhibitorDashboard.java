package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExhibitorDashboard {

    public String getTotal_exhibitor_visits() {
        return total_exhibitor_visits;
    }

    public void setTotal_exhibitor_visits(String total_exhibitor_visits) {
        this.total_exhibitor_visits = total_exhibitor_visits;
    }

    public String getTotal_exhibitor_msg() {
        return total_exhibitor_msg;
    }

    public void setTotal_exhibitor_msg(String total_exhibitor_msg) {
        this.total_exhibitor_msg = total_exhibitor_msg;
    }

    public String getTotal_exhibitor_meeting() {
        return total_exhibitor_meeting;
    }

    public void setTotal_exhibitor_meeting(String total_exhibitor_meeting) {
        this.total_exhibitor_meeting = total_exhibitor_meeting;
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

    public List<Brochure_Anlytics_Result> getBrochure_anlytics_result() {
        return brochure_anlytics_result;
    }

    public void setBrochure_anlytics_result(List<Brochure_Anlytics_Result> brochure_anlytics_result) {
        this.brochure_anlytics_result = brochure_anlytics_result;
    }

    @SerializedName("total_exhibitor_visits")
    @Expose
    private String total_exhibitor_visits;

    @SerializedName("total_exhibitor_msg")
    @Expose
    private String total_exhibitor_msg;

    @SerializedName("total_exhibitor_meeting")
    @Expose
    private String total_exhibitor_meeting;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("brochure_anlytics_result")
    @Expose
    private List<Brochure_Anlytics_Result> brochure_anlytics_result = null;
}
