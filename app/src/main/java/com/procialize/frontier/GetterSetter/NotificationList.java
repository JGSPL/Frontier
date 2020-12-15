package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naushad on 12/15/2017.
 */

public class NotificationList {


    @SerializedName("notification_id")
    @Expose
    private String notificationId;
    @SerializedName("notification_type")
    @Expose
    private String notificationType;
    @SerializedName("subject_id")
    @Expose
    private String subjectId;
    @SerializedName("subject_type")
    @Expose
    private String subjectType;
    @SerializedName("object_id")
    @Expose
    private String objectId;
    @SerializedName("object_type")
    @Expose
    private String objectType;
    @SerializedName("read")
    @Expose
    private String read;
    @SerializedName("notification_content")
    @Expose
    private String notificationContent;
    @SerializedName("message_id")
    @Expose
    private String messageId;
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("notification_date")
    @Expose
    private String notificationDate;
    @SerializedName("attendee_id")
    @Expose
    private String attendeeId;
    @SerializedName("attendee_first_name")
    @Expose
    private String attendeeFirstName;
    @SerializedName("attendee_last_name")
    @Expose
    private String attendeeLastName;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("event_name")
    @Expose
    private String eventName;
    @SerializedName("notification_post_id")
    @Expose
    private String notificationPostId;



    @SerializedName("media_file")
    @Expose
    private String media_file;

    public String getMedia_file() {
        return media_file;
    }

    public void setMedia_file(String media_file) {
        this.media_file = media_file;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getAttendeeFirstName() {
        return attendeeFirstName;
    }

    public void setAttendeeFirstName(String attendeeFirstName) {
        this.attendeeFirstName = attendeeFirstName;
    }

    public String getAttendeeLastName() {
        return attendeeLastName;
    }

    public void setAttendeeLastName(String attendeeLastName) {
        this.attendeeLastName = attendeeLastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getNotificationPostId() {
        return notificationPostId;
    }

    public void setNotificationPostId(String notificationPostId) {
        this.notificationPostId = notificationPostId;
    }

}
