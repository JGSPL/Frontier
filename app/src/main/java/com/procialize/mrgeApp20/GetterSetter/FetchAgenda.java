package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 12/4/2017.
 */

public class FetchAgenda {

    @SerializedName("agenda_list")
    @Expose
    private List<AgendaList> agendaList = null;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("agenda_media_url_path")
    @Expose
    private String agenda_media_url_path;

    public String getAgenda_media_url_path() {
        return agenda_media_url_path;
    }

    public void setAgenda_media_url_path(String agenda_media_url_path) {
        this.agenda_media_url_path = agenda_media_url_path;
    }

    public List<AgendaList> getAgendaList() {
        return agendaList;
    }

    public void setAgendaList(List<AgendaList> agendaList) {
        this.agendaList = agendaList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
