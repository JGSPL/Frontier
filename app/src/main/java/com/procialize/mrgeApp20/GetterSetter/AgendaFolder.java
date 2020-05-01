package com.procialize.mrgeApp20.GetterSetter;

import java.io.Serializable;

/**
 * Created by Preeti on 12-06-2018.
 */

public class AgendaFolder implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String event_id = "";
    private String folder_name = "";
    private String folder_image = "";
    private String created = "";
    private String media_type = "";
    private String media_thumbnail = "";

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getMedia_thumbnail() {
        return media_thumbnail;
    }

    public void setMedia_thumbnail(String media_thumbnail) {
        this.media_thumbnail = media_thumbnail;
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

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public String getFolder_image() {
        return folder_image;
    }

    public void setFolder_image(String folder_image) {
        this.folder_image = folder_image;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}