package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 1/10/2018.
 */

public class PostVideoSelfie {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

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

    public List<VideoContest> getVideoContest() {
        return videoContest;
    }

    public void setVideoContest(List<VideoContest> videoContest) {
        this.videoContest = videoContest;
    }

    @SerializedName("video_contest")
    @Expose
    private List<VideoContest> videoContest = null;
}
