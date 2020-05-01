package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 12/11/2017.
 */

public class TravelListFetch {
    @SerializedName("travel_list")
    @Expose
    private List<TravelList> travelList = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<TravelList> getTravelList() {
        return travelList;
    }

    public void setTravelList(List<TravelList> travelList) {
        this.travelList = travelList;
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
