package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 12/15/2017.
 */

public class VideoFetchListFetch {


    @SerializedName("video_folder_list")
    @Expose
    private List<VideoFolderList> videoFolderList = null;
    @SerializedName("video_list")
    @Expose
    private List<VideoList> videoList = null;

    public List<VideoFolderList> getVideoFolderList() {
        return videoFolderList;
    }

    public void setVideoFolderList(List<VideoFolderList> videoFolderList) {
        this.videoFolderList = videoFolderList;
    }

    public List<VideoList> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoList> videoList) {
        this.videoList = videoList;
    }

}
