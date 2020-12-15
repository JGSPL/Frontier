package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naushad on 11/8/2017.
 */

public class CommentData {

    @SerializedName("news_feed_id")
    @Expose
    private Boolean newsFeedId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("attendee_name")
    @Expose
    private String attendeeName;
    @SerializedName("attendee_profile_pic")
    @Expose
    private String attendeeProfilePic;
    @SerializedName("date_time")
    @Expose
    private String dateTime;

    public Boolean getNewsFeedId() {
        return newsFeedId;
    }

    public void setNewsFeedId(Boolean newsFeedId) {
        this.newsFeedId = newsFeedId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAttendeeName() {
        return attendeeName;
    }

    public void setAttendeeName(String attendeeName) {
        this.attendeeName = attendeeName;
    }

    public String getAttendeeProfilePic() {
        return attendeeProfilePic;
    }

    public void setAttendeeProfilePic(String attendeeProfilePic) {
        this.attendeeProfilePic = attendeeProfilePic;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

}