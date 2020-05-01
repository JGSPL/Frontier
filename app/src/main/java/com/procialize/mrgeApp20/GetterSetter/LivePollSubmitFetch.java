package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 12/16/2017.
 */

public class LivePollSubmitFetch {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("live_poll_list")
    @Expose
    private List<LivePollList> livePollList = null;
    @SerializedName("live_poll_option_list")
    @Expose
    private List<LivePollOptionList> livePollOptionList = null;

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

    public List<LivePollList> getLivePollList() {
        return livePollList;
    }

    public void setLivePollList(List<LivePollList> livePollList) {
        this.livePollList = livePollList;
    }

    public List<LivePollOptionList> getLivePollOptionList() {
        return livePollOptionList;
    }

    public void setLivePollOptionList(List<LivePollOptionList> livePollOptionList) {
        this.livePollOptionList = livePollOptionList;
    }
}


