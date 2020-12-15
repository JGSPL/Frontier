package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExhibitorCatList {

    @SerializedName("exhibitor_category_id")
    @Expose
    private String exhibitor_category_id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("total_exhibitor_count")
    @Expose
    private String total_exhibitor_count;

    public String getTotal_exhibitor_count() {
        return total_exhibitor_count;
    }

    public void setTotal_exhibitor_count(String total_exhibitor_count) {
        this.total_exhibitor_count = total_exhibitor_count;
    }

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
}
