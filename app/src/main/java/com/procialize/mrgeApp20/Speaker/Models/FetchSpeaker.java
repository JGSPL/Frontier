package com.procialize.mrgeApp20.Speaker.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 12/2/2017.
 */

public class FetchSpeaker {

    @SerializedName("speaker_list")
    @Expose
    private List<SpeakerList> speakerList = null;


    @SerializedName("profile_pic_url_path")
    @Expose
    private String profile_pic_url_path;
    @SerializedName("speaker_pdf_url_path")
    @Expose
    private String speaker_pdf_url_path;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

/*    @SerializedName("pdf_file_path")
    @Expose
    private String pdf_file_path;*/

    public String getProfile_pic_url_path() {
        return profile_pic_url_path;
    }

    public void setProfile_pic_url_path(String profile_pic_url_path) {
        this.profile_pic_url_path = profile_pic_url_path;
    }

    public String getSpeaker_pdf_url_path() {
        return speaker_pdf_url_path;
    }

    public void setSpeaker_pdf_url_path(String speaker_pdf_url_path) {
        this.speaker_pdf_url_path = speaker_pdf_url_path;
    }

  /*  public String getPdf_file_path() {
        return pdf_file_path;
    }

    public void setPdf_file_path(String pdf_file_path) {
        this.pdf_file_path = pdf_file_path;
    }
*/
    public List<SpeakerList> getSpeakerList() {
        return speakerList;
    }

    public void setSpeakerList(List<SpeakerList> speakerList) {
        this.speakerList = speakerList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
