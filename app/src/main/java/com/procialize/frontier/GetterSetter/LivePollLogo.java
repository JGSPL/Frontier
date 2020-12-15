package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LivePollLogo {

    @SerializedName("app_livepoll_logo")
    @Expose
    private String app_livepoll_logo;
    @SerializedName("web_livepoll_logo")
    @Expose
    private String web_livepoll_logo;

    public String getApp_livepoll_logo() {
        return app_livepoll_logo;
    }

    public void setApp_livepoll_logo(String app_livepoll_logo) {
        this.app_livepoll_logo = app_livepoll_logo;
    }

    public String getWeb_livepoll_logo() {
        return web_livepoll_logo;
    }

    public void setWeb_livepoll_logo(String web_livepoll_logo) {
        this.web_livepoll_logo = web_livepoll_logo;
    }
}
