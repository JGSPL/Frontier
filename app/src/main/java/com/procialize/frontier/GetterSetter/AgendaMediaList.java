package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AgendaMediaList implements Serializable {

    @SerializedName("session_vacation_id")
    @Expose
    private String session_vacation_id;
    @SerializedName("media_type")
    @Expose
    private String media_type;
    @SerializedName("media_name")
    @Expose
    private String media_name;
    @SerializedName("media_thumbnail")
    @Expose
    private String media_thumbnail;

    public String getSession_vacation_id() {
        return session_vacation_id;
    }

    public void setSession_vacation_id(String session_vacation_id) {
        this.session_vacation_id = session_vacation_id;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getMedia_name() {
        return media_name;
    }

    public void setMedia_name(String media_name) {
        this.media_name = media_name;
    }

    public String getMedia_thumbnail() {
        return media_thumbnail;
    }

    public void setMedia_thumbnail(String media_thumbnail) {
        this.media_thumbnail = media_thumbnail;
    }
}
