package com.procialize.mrgeApp20.BuddyList.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchChatList {

    @SerializedName("chat_data")
    @Expose
    private List<chat_list> chatList = null;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;

    public List<chat_list> getChatList() {
        return chatList;
    }

    public void setChatList(List<chat_list> chatList) {
        this.chatList = chatList;
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
