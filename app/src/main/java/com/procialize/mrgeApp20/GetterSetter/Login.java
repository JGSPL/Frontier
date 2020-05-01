
package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Login {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getCategory_count() {
        return category_count;
    }

    public void setCategory_count(String category_count) {
        this.category_count = category_count;
    }

    @SerializedName("category_count")
    @Expose
    private String category_count;
    @SerializedName("user_data")
    @Expose
    private UserData userData;
    @SerializedName("news_feed_list")
    @Expose
    private List<NewsFeedList> newsFeedList = null;
    @SerializedName("comment_data_list")
    @Expose
    private List<CommentDataList> commentDataList = null;
    @SerializedName("attendee_list")
    @Expose
    private List<AttendeeList> attendeeList = null;
    @SerializedName("speaker_list")
    @Expose
    private List<SpeakerList> speakerList = null;
    @SerializedName("agenda_list")
    @Expose
    private List<AgendaList> agendaList = null;
    @SerializedName("event_setting_list")
    @Expose
    private List<EventSettingList> eventSettingList = null;
    @SerializedName("event_menu_setting_list")
    @Expose
    private List<EventMenuSettingList> eventMenuSettingList = null;


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

    public List<NewsFeedList> getNewsFeedList() {
        return newsFeedList;
    }

    public void setNewsFeedList(List<NewsFeedList> newsFeedList) {
        this.newsFeedList = newsFeedList;
    }

    public List<CommentDataList> getCommentDataList() {
        return commentDataList;
    }

    public void setCommentDataList(List<CommentDataList> commentDataList) {
        this.commentDataList = commentDataList;
    }

    public List<AttendeeList> getAttendeeList() {
        return attendeeList;
    }

    public void setAttendeeList(List<AttendeeList> attendeeList) {
        this.attendeeList = attendeeList;
    }

    public List<SpeakerList> getSpeakerList() {
        return speakerList;
    }

    public void setSpeakerList(List<SpeakerList> speakerList) {
        this.speakerList = speakerList;
    }

    public List<AgendaList> getAgendaList() {
        return agendaList;
    }

    public void setAgendaList(List<AgendaList> agendaList) {
        this.agendaList = agendaList;
    }

    public List<EventSettingList> getEventSettingList() {
        return eventSettingList;
    }

    public void setEventSettingList(List<EventSettingList> eventSettingList) {
        this.eventSettingList = eventSettingList;
    }

    public List<EventMenuSettingList> getEventMenuSettingList() {
        return eventMenuSettingList;
    }

    public void setEventMenuSettingList(List<EventMenuSettingList> eventMenuSettingList) {
        this.eventMenuSettingList = eventMenuSettingList;
    }
}
