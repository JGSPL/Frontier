package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserEvent {

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
    private String mEventEnd;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("header_logo")
    @Expose
    private String headerLogo;
    @SerializedName("primary_color_code")
    @Expose
    private String primary_color_code;

    @SerializedName("background_image")
    @Expose
    private String backgroundImage;


    @SerializedName("colorActive")
    @Expose
    private String colorActive;

    public UserEvent() {
    }

    public UserEvent(String eventId, String name, String logo, String backgroundImage, String colorActive) {
        this.eventId = eventId;
        this.name = name;
        this.logo = logo;
        this.backgroundImage = backgroundImage;
        this.colorActive = colorActive;
    }


    public String getEventEnd() {
        return mEventEnd;
    }


    public String getHeaderLogo() {
        return headerLogo;
    }

    public void setHeaderLogo(String header_logo) {
        this.headerLogo = header_logo;
    }


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


    public String getLocation() {
        return location;
    }


    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getColorActive() {
        return colorActive;
    }

    public void setColorActive(String colorActive) {
        this.colorActive = colorActive;
    }
}
