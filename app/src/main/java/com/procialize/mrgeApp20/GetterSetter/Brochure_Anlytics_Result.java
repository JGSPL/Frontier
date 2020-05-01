package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brochure_Anlytics_Result {

    public String getTotal_brochure_visits() {
        return total_brochure_visits;
    }

    public void setTotal_brochure_visits(String total_brochure_visits) {
        this.total_brochure_visits = total_brochure_visits;
    }

    public String getBrochure_id() {
        return brochure_id;
    }

    public void setBrochure_id(String brochure_id) {
        this.brochure_id = brochure_id;
    }

    public String getBrochure_title() {
        return brochure_title;
    }

    public void setBrochure_title(String brochure_title) {
        this.brochure_title = brochure_title;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    @SerializedName("total_brochure_visits")
    @Expose
    private String total_brochure_visits;
    @SerializedName("brochure_id")
    @Expose
    private String brochure_id;
    @SerializedName("brochure_title")
    @Expose
    private String brochure_title;
    @SerializedName("file_type")
    @Expose
    private String file_type;
}
