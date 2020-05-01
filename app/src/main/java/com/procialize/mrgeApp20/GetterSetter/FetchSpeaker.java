package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 12/2/2017.
 */

public class FetchSpeaker {

    @SerializedName("speaker_list")
    @Expose
    private List<SpeakerList> speakerList = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<SpeakerList> getSpeakerList() {
        return speakerList;
    }

    public void setSpeakerList(List<SpeakerList> speakerList) {
        this.speakerList = speakerList;
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
}
