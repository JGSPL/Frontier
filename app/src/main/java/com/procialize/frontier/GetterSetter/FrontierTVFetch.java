package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FrontierTVFetch {


    @SerializedName("frontier_tv")
    @Expose
    private List<FrontierTV> frontier_tv = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<FrontierTV> getFrontier_tv() {
        return frontier_tv;
    }

    public void setFrontier_tv(List<FrontierTV> frontier_tv) {
        this.frontier_tv = frontier_tv;
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

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    @SerializedName("image_path")
    @Expose
    private String image_path;

}
