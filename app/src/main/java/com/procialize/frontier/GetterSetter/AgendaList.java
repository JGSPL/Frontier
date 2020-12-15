
package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AgendaList implements Serializable {

    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("session_name")
    @Expose
    private String sessionName;
    @SerializedName("session_description")
    @Expose
    private String sessionDescription;
    @SerializedName("session_start_time")
    @Expose
    private String sessionStartTime;
    @SerializedName("session_end_time")
    @Expose
    private String sessionEndTime;
    @SerializedName("session_date")
    @Expose
    private String sessionDate;
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("star")
    @Expose
    private String star;
    @SerializedName("total_feedback")
    @Expose
    private String totalFeedback;
    @SerializedName("feedback_comment")
    @Expose
    private String feedbackComment;
    @SerializedName("rated")
    @Expose
    private String rated;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSessionDescription() {
        return sessionDescription;
    }

    public void setSessionDescription(String sessionDescription) {
        this.sessionDescription = sessionDescription;
    }

    public String getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(String sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public String getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionEndTime(String sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getTotalFeedback() {
        return totalFeedback;
    }

    public void setTotalFeedback(String totalFeedback) {
        this.totalFeedback = totalFeedback;
    }

    public String getFeedbackComment() {
        return feedbackComment;
    }

    public void setFeedbackComment(String feedbackComment) {
        this.feedbackComment = feedbackComment;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

}
