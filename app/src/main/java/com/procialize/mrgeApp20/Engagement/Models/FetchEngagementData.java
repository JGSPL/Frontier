package com.procialize.mrgeApp20.Engagement.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchEngagementData {


    @SerializedName("video_engagement")
    @Expose
    private VideoEnagement video_engagement;

    @SerializedName("selfie_engagement")
    @Expose
    private SelfieEnagement selfie_engagement;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;

    public VideoEnagement getVideo_engagement() {
        return video_engagement;
    }

    public void setVideo_engagement(VideoEnagement video_engagement) {
        this.video_engagement = video_engagement;
    }

    public SelfieEnagement getSelfie_engagement() {
        return selfie_engagement;
    }

    public void setSelfie_engagement(SelfieEnagement selfie_engagement) {
        this.selfie_engagement = selfie_engagement;
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
