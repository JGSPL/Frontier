package com.procialize.frontier.Engagement.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoEnagement {

    @SerializedName("video_title")
    @Expose
    private String video_title;
    @SerializedName("video_description")
    @Expose
    private String video_description;
    @SerializedName("total_items")
    @Expose
    private String total_items;

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_description() {
        return video_description;
    }

    public void setVideo_description(String video_description) {
        this.video_description = video_description;
    }

    public String getTotal_items() {
        return total_items;
    }

    public void setTotal_items(String total_items) {
        this.total_items = total_items;
    }
}
