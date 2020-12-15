package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExhiCatDetailList {
    @SerializedName("exhibitor_category_id")
    @Expose
    private String exhibitor_category_id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("exhibitor_id")
    @Expose
    private String exhibitor_id;

    public String getExhibitor_category_id() {
        return exhibitor_category_id;
    }

    public void setExhibitor_category_id(String exhibitor_category_id) {
        this.exhibitor_category_id = exhibitor_category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExhibitor_id() {
        return exhibitor_id;
    }

    public void setExhibitor_id(String exhibitor_id) {
        this.exhibitor_id = exhibitor_id;
    }
}
