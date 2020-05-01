package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddExhibitorBrochure {

    @SerializedName("exhibitor_brochure_list")
    @Expose
    private List<ExhibitorBrochureList> exhibitor_brochure_list = null;

    @SerializedName("status")
    @Expose
    private String status;

    public List<ExhibitorBrochureList> getExhibitor_brochure_list() {
        return exhibitor_brochure_list;
    }

    public void setExhibitor_brochure_list(List<ExhibitorBrochureList> exhibitor_brochure_list) {
        this.exhibitor_brochure_list = exhibitor_brochure_list;
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

    @SerializedName("msg")
    @Expose
    private String msg;
}
