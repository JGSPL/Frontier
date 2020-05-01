package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserEventList {

    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("event_start")
    @Expose
    private String eventStart;
    @SerializedName("event_end")
    @Expose
    private String event_end;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("header_logo")
    @Expose
    private String header_logo;
    @SerializedName("primary_color_code")
    @Expose
    private String primary_color_code;

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }

    @SerializedName("background_image")
    @Expose
    private String background_image;

    public String getEvent_end() {
        return event_end;
    }

    public void setEvent_end(String event_end) {
        this.event_end = event_end;
    }

    public String getPrimary_color_code() {
        return primary_color_code;
    }

    public void setPrimary_color_code(String primary_color_code) {
        this.primary_color_code = primary_color_code;
    }

    public String getHeader_logo() {
        return header_logo;
    }

    public void setHeader_logo(String header_logo) {
        this.header_logo = header_logo;
    }


//    @SerializedName("logo")
//    @Expose
//    private String logo;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventStart() {
        return eventStart;
    }

    public void setEventStart(String eventStart) {
        this.eventStart = eventStart;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}