package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExhibitorList {
    @SerializedName("exhibitor_category_master_list")
    @Expose
    private List<ExhibitorCatList> ExhibitorCatList = null;

    @SerializedName("exhibitor_list")
    @Expose
    private List<ExhibitorDataList> ExhibitorDataList = null;

    @SerializedName("exhibitor_attendee_list")
    @Expose
    private List<ExhibitorAttendeeList> ExhibitorAttendeeList = null;

    @SerializedName("exhibitor_brochure_list")
    @Expose
    private List<ExhibitorBrochureList> ExhibitorBrochureList = null;

    @SerializedName("exhibitor_category_list")
    @Expose
    private List<ExhiCatDetailList> ExhiCatDetailList = null;

    public List<ExhiCatDetailList> getExhiCatDetailList() {
        return ExhiCatDetailList;
    }

    public void setExhiCatDetailList(List<ExhiCatDetailList> exhiCatDetailList) {
        ExhiCatDetailList = exhiCatDetailList;
    }

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;

    public List<ExhibitorCatList> getExhibitorCatList() {
        return ExhibitorCatList;
    }

    public void setExhibitorCatList(List<ExhibitorCatList> exhibitorCatList) {
        ExhibitorCatList = exhibitorCatList;
    }

    public List<ExhibitorDataList> getExhibitorDataList() {
        return ExhibitorDataList;
    }

    public void setExhibitorDataList(List<ExhibitorDataList> exhibitorDataList) {
        ExhibitorDataList = exhibitorDataList;
    }

    public List<ExhibitorAttendeeList> getExhibitorAttendeeList() {
        return ExhibitorAttendeeList;
    }

    public void setExhibitorAttendeeList(List<ExhibitorAttendeeList> exhibitorAttendeeList) {
        ExhibitorAttendeeList = exhibitorAttendeeList;
    }

    public List<com.procialize.frontier.GetterSetter.ExhibitorBrochureList> getExhibitorBrochureList() {
        return ExhibitorBrochureList;
    }

    public void setExhibitorBrochureList(List<ExhibitorBrochureList> exhibitorBrochureList) {
        ExhibitorBrochureList = exhibitorBrochureList;
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
