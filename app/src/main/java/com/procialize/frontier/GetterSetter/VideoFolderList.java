package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Naushad on 12/27/2017.
 */

public class VideoFolderList implements Serializable {


    @SerializedName("folder_name")
    @Expose
    private String folderName;
    @SerializedName("folder_image")
    @Expose
    private String folderImage;
    @SerializedName("id")
    @Expose
    private String folder_id;

    public String getFolderName() {
        return folderName;
    }

    public String getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(String folder_id) {
        this.folder_id = folder_id;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderImage() {
        return folderImage;
    }

    public void setFolderImage(String folderImage) {
        this.folderImage = folderImage;
    }
}
