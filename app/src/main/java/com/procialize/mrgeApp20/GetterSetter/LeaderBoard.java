package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rahul on 23-10-2018.
 */

public class LeaderBoard {
    @SerializedName("point")
    @Expose
    private String point;
    @SerializedName("attendee_id")
    @Expose
    private String attendee_id;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;

    public String getExtra_point() {
        return extra_point;
    }

    public void setExtra_point(String extra_point) {
        this.extra_point = extra_point;
    }

    @SerializedName("extra_point")
    @Expose
    private String extra_point;

    public String getCalculated_point() {
        return calculated_point;
    }

    public void setCalculated_point(String calculated_point) {
        this.calculated_point = calculated_point;
    }

    @SerializedName("calculated_point")
    @Expose
    private String calculated_point;

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getAttendee_id() {
        return attendee_id;
    }

    public void setAttendee_id(String attendee_id) {
        this.attendee_id = attendee_id;
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
}
