package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Exhibitor_Meeting_Request_List {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExhibitor_id() {
        return exhibitor_id;
    }

    public void setExhibitor_id(String exhibitor_id) {
        this.exhibitor_id = exhibitor_id;
    }

    public String getAttendee_id() {
        return attendee_id;
    }

    public void setAttendee_id(String attendee_id) {
        this.attendee_id = attendee_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getMeeting_date_time() {
        return meeting_date_time;
    }

    public void setMeeting_date_time(String meeting_date_time) {
        this.meeting_date_time = meeting_date_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("exhibitor_id")
    @Expose
    private String exhibitor_id;

    @SerializedName("attendee_id")
    @Expose
    private String attendee_id;

    @SerializedName("event_id")
    @Expose
    private String event_id;

    @SerializedName("meeting_date_time")
    @Expose
    private String meeting_date_time;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("modified")
    @Expose
    private String modified;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;
}
