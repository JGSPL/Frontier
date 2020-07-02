package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 1/2/2018.
 */

public class SelfieListFetch {

    @SerializedName("selfie_list")
    @Expose
    private List<SelfieList> selfieList = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;


    @SerializedName("selfie_title")
    @Expose
    private String selfie_title;
    @SerializedName("selfie_description")
    @Expose
    private String selfie_description;
    @SerializedName("selfie_url_path")
    @Expose
    private String selfie_url_path;

    public String getSelfie_url_path() {
        return selfie_url_path;
    }

    public void setSelfie_url_path(String selfie_url_path) {
        this.selfie_url_path = selfie_url_path;
    }

    public String getSelfie_title() {
        return selfie_title;
    }

    public void setSelfie_title(String selfie_title) {
        this.selfie_title = selfie_title;
    }

    public String getSelfie_description() {
        return selfie_description;
    }

    public void setSelfie_description(String selfie_description) {
        this.selfie_description = selfie_description;
    }

    public List<SelfieList> getSelfieList() {
        return selfieList;
    }

    public void setSelfieList(List<SelfieList> selfieList) {
        this.selfieList = selfieList;
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
