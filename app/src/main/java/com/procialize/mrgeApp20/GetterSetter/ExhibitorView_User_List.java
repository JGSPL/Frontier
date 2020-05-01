package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExhibitorView_User_List {

    public String getTotal_exhibitor_visits() {
        return total_exhibitor_visits;
    }

    public void setTotal_exhibitor_visits(String total_exhibitor_visits) {
        this.total_exhibitor_visits = total_exhibitor_visits;
    }

    public String getAttendee_id() {
        return attendee_id;
    }

    public void setAttendee_id(String attendee_id) {
        this.attendee_id = attendee_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    @SerializedName("total_exhibitor_visits")
    @Expose
    private String total_exhibitor_visits;

    @SerializedName("attendee_id")
    @Expose
    private String attendee_id;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;


}
