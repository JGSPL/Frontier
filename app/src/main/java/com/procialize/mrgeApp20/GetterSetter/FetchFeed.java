package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 11/13/2017.
 */

public class FetchFeed {

    @SerializedName("youtube_info")
    @Expose
    private List<YouTubeApiList> youtube_info = null;
    @SerializedName("news_feed_list")
    @Expose
    private List<NewsFeedList> newsFeedList = null;
    @SerializedName("comment_data_list")
    @Expose
    private List<CommentDataList> commentDataList = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("totalRecords")
    @Expose
    private String totalRecords;

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getNews_feed_url_path() {
        return news_feed_url_path;
    }

    public void setNews_feed_url_path(String news_feed_url_path) {
        this.news_feed_url_path = news_feed_url_path;
    }

    public String getProfile_pic_url_path() {
        return profile_pic_url_path;
    }

    public void setProfile_pic_url_path(String profile_pic_url_path) {
        this.profile_pic_url_path = profile_pic_url_path;
    }

    @SerializedName("news_feed_url_path")
    @Expose
    private String news_feed_url_path;
    @SerializedName("profile_pic_url_path")
    @Expose
    private String profile_pic_url_path;
    @SerializedName("live_streaming_info")
    @Expose
    private com.procialize.mrgeApp20.GetterSetter.live_steaming_info live_steaming_info;

    public List<YouTubeApiList> getYoutube_info() {
        return youtube_info;
    }

    public void setYoutube_info(List<YouTubeApiList> youtube_info) {
        this.youtube_info = youtube_info;
    }

    public com.procialize.mrgeApp20.GetterSetter.live_steaming_info getLive_steaming_info() {
        return live_steaming_info;
    }

    public void setLive_steaming_info(com.procialize.mrgeApp20.GetterSetter.live_steaming_info live_steaming_info) {
        this.live_steaming_info = live_steaming_info;
    }

    public List<NewsFeedList> getNewsFeedList() {
        return newsFeedList;
    }

    public void setNewsFeedList(List<NewsFeedList> newsFeedList) {
        this.newsFeedList = newsFeedList;
    }

    public List<CommentDataList> getCommentDataList() {
        return commentDataList;
    }

    public void setCommentDataList(List<CommentDataList> commentDataList) {
        this.commentDataList = commentDataList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
