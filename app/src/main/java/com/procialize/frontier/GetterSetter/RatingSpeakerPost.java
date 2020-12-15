package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 12/4/2017.
 */

public class RatingSpeakerPost {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("rating_details")
    @Expose
    private List<RatingSpeakerDetail> ratingSpeakerDetails = null;

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

    public List<RatingSpeakerDetail> getRatingSpeakerDetails() {
        return ratingSpeakerDetails;
    }

    public void setRatingSpeakerDetails(List<RatingSpeakerDetail> ratingSpeakerDetails) {
        this.ratingSpeakerDetails = ratingSpeakerDetails;
    }
}
