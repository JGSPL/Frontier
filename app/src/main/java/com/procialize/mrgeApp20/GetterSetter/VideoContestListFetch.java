package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 1/2/2018.
 */

public class VideoContestListFetch {

    @SerializedName("video_contest")
    @Expose
    private List<VideoContest> videoContest = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;


    @SerializedName("video_title")
    @Expose
    private String video_title;

    @SerializedName("video_description")
    @Expose
    private String video_description;

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_description() {
        return video_description;
    }

    public void setVideo_description(String video_description) {
        this.video_description = video_description;
    }

    public List<VideoContest> getVideoContest() {
        return videoContest;
    }

    public void setVideoContest(List<VideoContest> videoContest) {
        this.videoContest = videoContest;
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
