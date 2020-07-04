package com.procialize.mrgeApp20.BuddyList.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchBuddyList {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("profile_pic_url_path")
    @Expose
    private String profile_pic_url_path;
    @SerializedName("buddy_accept_terms")
    @Expose
    private String buddy_accept_terms;
    @SerializedName("buddy_list")
    @Expose
    private List<Buddy> BuddyList = null;

    public String getBuddy_accept_terms() {
        return buddy_accept_terms;
    }

    public void setBuddy_accept_terms(String buddy_accept_terms) {
        this.buddy_accept_terms = buddy_accept_terms;
    }

    public String getProfile_pic_url_path() {
        return profile_pic_url_path;
    }

    public void setProfile_pic_url_path(String profile_pic_url_path) {
        this.profile_pic_url_path = profile_pic_url_path;
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

    public List<Buddy> getBuddyList() {
        return BuddyList;
    }

    public void setBuddyList(List<Buddy> buddyList) {
        BuddyList = buddyList;
    }
}
