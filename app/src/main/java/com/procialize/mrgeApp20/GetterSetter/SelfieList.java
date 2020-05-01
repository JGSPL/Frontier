package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Naushad on 1/2/2018.
 */

public class SelfieList implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("like_flag")
    @Expose
    private String likeFlag;
    @SerializedName("total_likes")
    @Expose
    private String totalLikes;
    @SerializedName("attendee_id")
    @Expose
    private String attendee_id;

    public String getAttendee_id() {
        return attendee_id;
    }

    public void setAttendee_id(String attendee_id) {
        this.attendee_id = attendee_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
