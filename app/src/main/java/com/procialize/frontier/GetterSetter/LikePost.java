package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naushad on 11/8/2017.
 */

public class LikePost {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("news_feed_id")
    @Expose
    private String newsFeedId;
    @SerializedName("like_count")
    @Expose
    private Integer likeCount;
    @SerializedName("like_flag")
    @Expose
    private Integer likeFlag;

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

    public String getNewsFeedId() {
        return newsFeedId;
    }

    public void setNewsFeedId(String newsFeedId) {
        this.newsFeedId = newsFeedId;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getLikeFlag() {
        return likeFlag;
    }

    public void setLikeFlag(Integer likeFlag) {
        this.likeFlag = likeFlag;
    }

}

