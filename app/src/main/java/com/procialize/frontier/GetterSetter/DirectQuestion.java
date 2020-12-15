package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DirectQuestion {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attendee_id")
    @Expose
    private String attendee_id;
    @SerializedName("event_id")
    @Expose
    private String event_id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("status")
    @Expose
    private String status;
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
    @SerializedName("attendee_company")
    @Expose
    private String attendee_company;
    @SerializedName("attendee_designation")
    @Expose
    private String attendee_designation;
    @SerializedName("like_flag")
    @Expose
    private String like_flag;
    @SerializedName("total_likes")
    @Expose
    private String total_likes;

    @SerializedName("reply")
    @Expose
    private String reply;

    @SerializedName("attendee_city")
    @Expose
    private String attendee_city ;

    public String getAttendee_city() {
        return attendee_city;
    }

    public void setAttendee_city(String attendee_city) {
        this.attendee_city = attendee_city;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getAttendee_company() {
        return attendee_company;
    }

    public void setAttendee_company(String attendee_company) {
        this.attendee_company = attendee_company;
    }

    public String getAttendee_designation() {
        return attendee_designation;
    }

    public void setAttendee_designation(String attendee_designation) {
        this.attendee_designation = attendee_designation;
    }

    public String getLike_flag() {
        return like_flag;
    }

    public void setLike_flag(String like_flag) {
        this.like_flag = like_flag;
    }

    public String getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(String total_likes) {
        this.total_likes = total_likes;
    }

}
