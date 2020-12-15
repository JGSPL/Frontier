package com.procialize.frontier.GetterSetter;

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

    @SerializedName("folder_video_url_path")
    @Expose
    private String folder_video_url_path;

    public String getFolder_video_url_path() {
        return folder_video_url_path;
    }

    public void setFolder_video_url_path(String folder_video_url_path) {
        this.folder_video_url_path = folder_video_url_path;
    }

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
