package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaderBoardListFetch {
    @SerializedName("leaderboard_list")
    @Expose
    private List<LeaderBoard> leaderboardList = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<LeaderBoard> getLeaderboardList() {
        return leaderboardList;
    }

    public void setLeaderboardList(List<LeaderBoard> leaderboardList) {
        this.leaderboardList = leaderboardList;
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

