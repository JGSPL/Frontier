package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Naushad on 12/27/2017.
 */

public class FirstLevelFilter implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("folder_name")
    @Expose
    private String folderName;
    @SerializedName("folder_id")
    @Expose
    private String folder_id;

    public String getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(String folder_id) {
        this.folder_id = folder_id;
    }

    public String getVideo_thumb() {
        return video_thumb;
    }

    public void setVideo_thumb(String video_thumb) {
        this.video_thumb = video_thumb;
    }

    @SerializedName("video_thumb")
    @Expose
    private String video_thumb;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
