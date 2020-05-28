package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YouTubeApiList {

    @SerializedName("youtube_id")
    @Expose
    private String youtube_id;
    @SerializedName("youtube_stream_url")
    @Expose
    private String youtube_stream_url;
    @SerializedName("stream_status")
    @Expose
    private String stream_status;
    @SerializedName("stream_datetime")
    @Expose
    private String stream_datetime;

    public String getYoutube_id() {
        return youtube_id;
    }

    public void setYoutube_id(String youtube_id) {
        this.youtube_id = youtube_id;
    }

    public String getYoutube_stream_url() {
        return youtube_stream_url;
    }

    public void setYoutube_stream_url(String youtube_stream_url) {
        this.youtube_stream_url = youtube_stream_url;
    }

    public String getStream_status() {
        return stream_status;
    }

    public void setStream_status(String stream_status) {
        this.stream_status = stream_status;
    }

    public String getStream_datetime() {
        return stream_datetime;
    }

    public void setStream_datetime(String stream_datetime) {
        this.stream_datetime = stream_datetime;
    }
}
