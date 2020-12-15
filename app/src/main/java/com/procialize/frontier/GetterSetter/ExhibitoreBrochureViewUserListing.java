package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExhibitoreBrochureViewUserListing {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;

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


    public List<Exhibitor_Brochure_View_User_List> getExhibitor_brochure_view_user_list() {
        return exhibitor_brochure_view_user_list;
    }

    public void setExhibitor_brochure_view_user_list(List<Exhibitor_Brochure_View_User_List> exhibitor_brochure_view_user_list) {
        this.exhibitor_brochure_view_user_list = exhibitor_brochure_view_user_list;
    }

    @SerializedName("exhibitor_brochure_view_user_list")
    @Expose
    private List<Exhibitor_Brochure_View_User_List> exhibitor_brochure_view_user_list = null;

}
