package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventListing {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("user_data")
    @Expose
    private UserData userData;
    @SerializedName("user_event_list")
    @Expose
    private List<UserEventList> userEventList = null;

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

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public List<UserEventList> getUserEventList() {
        return userEventList;
    }

    public void setUserEventList(List<UserEventList> userEventList) {
        this.userEventList = userEventList;
    }

}