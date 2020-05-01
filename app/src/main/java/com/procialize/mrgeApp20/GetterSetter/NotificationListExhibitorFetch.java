package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationListExhibitorFetch {

    @SerializedName("notification_list")
    @Expose
    private List<NotificationList> notification_list = null;

    @SerializedName("exhibitor_notification_list")
    @Expose
    private List<Exhibitor_Notification_List> exhibitor_notification_list = null;

    @SerializedName("exhibitor_meeting_request_list")
    @Expose
    private List<Exhibitor_Meeting_Request_List> exhibitor_meeting_request_list = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<NotificationList> getNotification_list() {
        return notification_list;
    }

    public void setNotification_list(List<NotificationList> notification_list) {
        this.notification_list = notification_list;
    }

    public List<Exhibitor_Notification_List> getExhibitor_notification_list() {
        return exhibitor_notification_list;
    }

    public void setExhibitor_notification_list(List<Exhibitor_Notification_List> exhibitor_notification_list) {
        this.exhibitor_notification_list = exhibitor_notification_list;
    }

    public List<Exhibitor_Meeting_Request_List> getExhibitor_meeting_request_list() {
        return exhibitor_meeting_request_list;
    }

    public void setExhibitor_meeting_request_list(List<Exhibitor_Meeting_Request_List> exhibitor_meeting_request_list) {
        this.exhibitor_meeting_request_list = exhibitor_meeting_request_list;
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

    @SerializedName("msg")
    @Expose
    private String msg;

}
