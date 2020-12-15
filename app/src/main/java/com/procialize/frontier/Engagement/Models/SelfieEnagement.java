package com.procialize.frontier.Engagement.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelfieEnagement {

    @SerializedName("selfie_title")
    @Expose
    private String selfie_title;

    @SerializedName("selfie_description")
    @Expose
    private String selfie_description;

    @SerializedName("total_items")
    @Expose
    private String total_items;

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

    public String getTotal_items() {
        return total_items;
    }

    public void setTotal_items(String total_items) {
        this.total_items = total_items;
    }
}
