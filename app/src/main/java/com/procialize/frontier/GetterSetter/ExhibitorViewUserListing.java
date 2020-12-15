package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExhibitorViewUserListing {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;

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

    public List<ExhibitorView_User_List> getExhibitor_view_user_list() {
        return exhibitor_view_user_list;
    }

    public void setExhibitor_view_user_list(List<ExhibitorView_User_List> exhibitor_view_user_list) {
        this.exhibitor_view_user_list = exhibitor_view_user_list;
    }

    @SerializedName("exhibitor_view_user_list")
    @Expose
    private List<ExhibitorView_User_List> exhibitor_view_user_list = null;
}
