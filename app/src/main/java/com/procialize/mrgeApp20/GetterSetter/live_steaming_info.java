package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class live_steaming_info {

    @SerializedName("zoom_meeting_id")
    @Expose
    private String zoom_meeting_id;

  /*  public String getStream_datetime() {
        return stream_datetime;
    }

    public void setStream_datetime(String stream_datetime) {
        this.stream_datetime = stream_datetime;
    }

    @SerializedName("stream_datetime")
    @Expose
    private String stream_datetime;*/

    public String getZoom_datetime() {
        return zoom_datetime;
    }

    public void setZoom_datetime(String zoom_datetime) {
        this.zoom_datetime = zoom_datetime;
    }

    @SerializedName("zoom_datetime")
    @Expose
    private String zoom_datetime;

    @SerializedName("zoom_password")
    @Expose
    private String zoom_password;

    @SerializedName("zoom_status")
    @Expose
    private String zoom_status;

  /*  @SerializedName("youtube_stream_url")
    @Expose
    private String youtube_stream_url;
*/
    public String getZoom_meeting_id() {
        return zoom_meeting_id;
    }

    public void setZoom_meeting_id(String zoom_meeting_id) {
        this.zoom_meeting_id = zoom_meeting_id;
    }

    public String getZoom_password() {
        return zoom_password;
    }

    public void setZoom_password(String zoom_password) {
        this.zoom_password = zoom_password;
    }

    public String getZoom_status() {
        return zoom_status;
    }

    public void setZoom_status(String zoom_status) {
        this.zoom_status = zoom_status;
    }

   /* public String getYoutube_stream_url() {
        return youtube_stream_url;
    }

    public void setYoutube_stream_url(String youtube_stream_url) {
        this.youtube_stream_url = youtube_stream_url;
    }
*/
  /*  public String getStream_status() {
        return stream_status;
    }

    public void setStream_status(String stream_status) {
        this.stream_status = stream_status;
    }*/

    /*@SerializedName("stream_status")
    @Expose
    private String stream_status;*/
}
