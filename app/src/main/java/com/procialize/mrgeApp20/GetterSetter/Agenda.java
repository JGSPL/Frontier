package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

// Agenda Model Class
public class Agenda implements Serializable {

    @SerializedName("agenda_vacation_list")
    @Expose
    private List<AgendaVacationList> agenda_vacation_list = null;
    @SerializedName("agenda_vacation_media_list")
    @Expose
    private List<AgendaMediaList> agenda_vacation_media_list = null;

    public List<AgendaVacationList> getAgenda_vacation_list() {
        return agenda_vacation_list;
    }

    public void setAgenda_vacation_list(List<AgendaVacationList> agenda_vacation_list) {
        this.agenda_vacation_list = agenda_vacation_list;
    }

    public List<AgendaMediaList> getAgenda_vacation_media_list() {
        return agenda_vacation_media_list;
    }

    public void setAgenda_vacation_media_list(List<AgendaMediaList> agenda_vacation_media_list) {
        this.agenda_vacation_media_list = agenda_vacation_media_list;
    }
}
