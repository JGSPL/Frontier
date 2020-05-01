package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AgendaVacationList implements Serializable {

    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("session_name")
    @Expose
    private String session_name;
    @SerializedName("session_description")
    @Expose
    private String session_description;
    @SerializedName("folder_name")
    @Expose
    private String folder_name;
    @SerializedName("event_id")
    @Expose
    private String event_id;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSession_name() {
        return session_name;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
    }

    public String getSession_description() {
        return session_description;
    }

    public void setSession_description(String session_description) {
        this.session_description = session_description;
    }

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }


}
