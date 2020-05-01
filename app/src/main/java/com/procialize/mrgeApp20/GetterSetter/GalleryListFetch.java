package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 12/15/2017.
 */

public class GalleryListFetch {

    @SerializedName("folder_list")
    @Expose
    private List<FolderList> folderList = null;
    @SerializedName("gallery_list")
    @Expose
    private List<GalleryList> galleryList = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<FolderList> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<FolderList> folderList) {
        this.folderList = folderList;
    }

    public List<GalleryList> getGalleryList() {
        return galleryList;
    }

    public void setGalleryList(List<GalleryList> galleryList) {
        this.galleryList = galleryList;
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
