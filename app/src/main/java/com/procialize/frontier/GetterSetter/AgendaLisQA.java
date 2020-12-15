package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naushad on 12/18/2017.
 */

public class AgendaLisQA {
    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("session_name")
    @Expose
    private String sessionName;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
}
