package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 12/11/2017.
 */

public class EventInfoFetch {
    @SerializedName("event_list")
    @Expose
    private List<EventList> eventList = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("event_logo_url_path")
    @Expose
    private String event_logo_url_path;
    @SerializedName("sponsor_file_path")
    @Expose
    private String sponsor_file_path;

    public String getEvent_logo_url_path() {
        return event_logo_url_path;
    }

    public void setEvent_logo_url_path(String event_logo_url_path) {
        this.event_logo_url_path = event_logo_url_path;
    }

    @SerializedName("sponsor_list")
    @Expose
    private List<SponsorsList> sponsor_list;

    public List<SponsorsList> getSponsor_list() {
        return sponsor_list;
    }

    public void setSponsor_list(List<SponsorsList> sponsor_list) {
        this.sponsor_list = sponsor_list;
    }

    public String getSponsor_file_path() {
        return sponsor_file_path;
    }

    public void setSponsor_file_path(String sponsor_file_path) {
        this.sponsor_file_path = sponsor_file_path;
    }

    public List<EventList> getEventList() {
        return eventList;
    }

    public void setEventList(List<EventList> eventList) {
        this.eventList = eventList;
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
