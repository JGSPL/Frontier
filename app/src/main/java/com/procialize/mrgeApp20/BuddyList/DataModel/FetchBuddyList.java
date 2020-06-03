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
    @SerializedName("buddy_list")
    @Expose
    private List<Buddy> BuddyList = null;

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
