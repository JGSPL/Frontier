package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Naushad on 12/16/2017.
 */

public class LivePollOptionList implements Serializable {

    @SerializedName("live_poll_id")
    @Expose
    private String livePollId;
    @SerializedName("option_id")
    @Expose
    private String optionId;
    @SerializedName("option")
    @Expose
    private String option;
    @SerializedName("total_user")
    @Expose
    private String totalUser;

    public String getLivePollId() {
        return livePollId;
    }

    public void setLivePollId(String livePollId) {
        this.livePollId = livePollId;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getTotalUser() {
        return totalUser;
    }

    public void setTotalUser(String totalUser) {
        this.totalUser = totalUser;
    }
}
