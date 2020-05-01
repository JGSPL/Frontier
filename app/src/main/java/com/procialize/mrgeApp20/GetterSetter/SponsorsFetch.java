package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SponsorsFetch {
    public String getSponsors_url() {
        return sponsors_url;
    }

    public void setSponsors_url(String sponsors_url) {
        this.sponsors_url = sponsors_url;
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

    @SerializedName("sponsors_url")
    @Expose
    private String sponsors_url;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
}
