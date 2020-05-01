package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Naushad on 12/15/2017.
 */

public class GalleryList implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("folder_name")
    @Expose
    private String folderName;

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
