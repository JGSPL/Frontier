package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 12/15/2017.
 */

public class NotificationListFetch {

    @SerializedName("notification_list")
    @Expose
    private List<NotificationList> notificationList = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<NotificationList> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<NotificationList> notificationList) {
        this.notificationList = notificationList;
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
