package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExhibitorDataList {

    @SerializedName("exhibitor_id")
    @Expose
    private String exhibitor_id;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("event_id")
    @Expose
    private String event_id;
    @SerializedName("exhibitor_category_id")
    @Expose
    private String exhibitor_category_id;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("logo")
    @Expose
    private String logo;

    @SerializedName("tile_image")
    @Expose
    private String tile_image;
    @SerializedName("stall_number")
    @Expose
    private String stall_number;

    @SerializedName("allowed_message")
    @Expose
    private String allowed_message;
    @SerializedName("allowed_setup_meeting")
    @Expose
    private String allowed_setup_meeting;


    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;

    public String getExhibitor_id() {
        return exhibitor_id;
    }

    public void setExhibitor_id(String exhibitor_id) {
        this.exhibitor_id = exhibitor_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getExhibitor_category_id() {
        return exhibitor_category_id;
    }

    public void setExhibitor_category_id(String exhibitor_category_id) {
        this.exhibitor_category_id = exhibitor_category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTile_image() {
        return tile_image;
    }

    public void setTile_image(String tile_image) {
        this.tile_image = tile_image;
    }

    public String getStall_number() {
        return stall_number;
    }

    public void setStall_number(String stall_number) {
        this.stall_number = stall_number;
    }

    public String getAllowed_message() {
        return allowed_message;
    }

    public void setAllowed_message(String allowed_message) {
        this.allowed_message = allowed_message;
    }

    public String getAllowed_setup_meeting() {
        return allowed_setup_meeting;
    }

    public void setAllowed_setup_meeting(String allowed_setup_meeting) {
        this.allowed_setup_meeting = allowed_setup_meeting;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
