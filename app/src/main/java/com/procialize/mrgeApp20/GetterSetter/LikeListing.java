package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LikeListing {
    @SerializedName("attendee_list")
    @Expose
    private List<AttendeeList> attendeeList = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<AttendeeList> getAttendeeList() {
        return attendeeList;
    }

    public void setAttendeeList(List<AttendeeList> attendeeList) {
        this.attendeeList = attendeeList;
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
