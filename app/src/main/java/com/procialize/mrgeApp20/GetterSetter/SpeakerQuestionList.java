package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naushad on 12/20/2017.
 */

public class SpeakerQuestionList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attendee_id")
    @Expose
    private String attendeeId;
    @SerializedName("speaker_id")
    @Expose
    private String speakerId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("attendee_company")
    @Expose
    private String attendeeCompany;
    @SerializedName("attendee_designation")
    @Expose
    private String attendeeDesignation;
    @SerializedName("like_flag")
    @Expose
    private String likeFlag;
    @SerializedName("total_likes")
    @Expose
    private String totalLikes;

    @SerializedName("reply")
    @Expose
    private String reply;

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

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(String speakerId) {
        this.speakerId = speakerId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getAttendeeCompany() {
        return attendeeCompany;
    }

    public void setAttendeeCompany(String attendeeCompany) {
        this.attendeeCompany = attendeeCompany;
    }

    public String getAttendeeDesignation() {
        return attendeeDesignation;
    }

    public void setAttendeeDesignation(String attendeeDesignation) {
        this.attendeeDesignation = attendeeDesignation;
    }

    public String getLikeFlag() {
        return likeFlag;
    }

    public void setLikeFlag(String likeFlag) {
        this.likeFlag = likeFlag;
    }

    public String getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(String totalLikes) {
        this.totalLikes = totalLikes;
    }
}
