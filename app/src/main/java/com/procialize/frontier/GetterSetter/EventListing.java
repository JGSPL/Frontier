package com.procialize.frontier.GetterSetter;

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
    @SerializedName("profile_pic_url_path")
    @Expose
    private String profile_pic_url_path;
    @SerializedName("event_logo_url_path")
    @Expose
    private String event_logo_url_path;
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

    public String getProfile_pic_url_path() {
        return profile_pic_url_path;
    }

    public void setProfile_pic_url_path(String profile_pic_url_path) {
        this.profile_pic_url_path = profile_pic_url_path;
    }

    public String getEvent_logo_url_path() {
        return event_logo_url_path;
    }

    public void setEvent_logo_url_path(String event_logo_url_path) {
        this.event_logo_url_path = event_logo_url_path;
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