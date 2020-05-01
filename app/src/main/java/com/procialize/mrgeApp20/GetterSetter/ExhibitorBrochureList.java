package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExhibitorBrochureList {

    @SerializedName("brochure_id")
    @Expose
    private String brochure_id;
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("exhibitor_id")
    @Expose
    private String exhibitor_id;
    @SerializedName("event_id")
    @Expose
    private String event_id;
    @SerializedName("brochure_title")
    @Expose
    private String brochure_title;
    @SerializedName("file_name")
    @Expose
    private String file_name;

    @SerializedName("file_type")
    @Expose
    private String file_type;

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;

    public String getBrochure_id() {
        return brochure_id;
    }

    public void setBrochure_id(String brochure_id) {
        this.brochure_id = brochure_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExhibitor_id() {
        return exhibitor_id;
    }

    public void setExhibitor_id(String exhibitor_id) {
        this.exhibitor_id = exhibitor_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getBrochure_title() {
        return brochure_title;
    }

    public void setBrochure_title(String brochure_title) {
        this.brochure_title = brochure_title;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
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
