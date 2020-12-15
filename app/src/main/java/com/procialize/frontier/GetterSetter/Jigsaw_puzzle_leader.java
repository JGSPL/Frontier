package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Jigsaw_puzzle_leader {

    public List<com.procialize.frontier.GetterSetter.JgsawPuzzle> getJgsawPuzzle() {
        return JgsawPuzzle;
    }

    public void setJgsawPuzzle(List<com.procialize.frontier.GetterSetter.JgsawPuzzle> jgsawPuzzle) {
        JgsawPuzzle = jgsawPuzzle;
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

    public String getProfile_pic_url_path() {
        return profile_pic_url_path;
    }

    public void setProfile_pic_url_path(String profile_pic_url_path) {
        this.profile_pic_url_path = profile_pic_url_path;
    }

    @SerializedName("jigsaw_puzzle_leader")
    @Expose
    private List<JgsawPuzzle> JgsawPuzzle = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("profile_pic_url_path")
    @Expose
    private String profile_pic_url_path;
}
