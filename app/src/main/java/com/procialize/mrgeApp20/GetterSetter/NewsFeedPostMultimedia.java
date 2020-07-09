package com.procialize.mrgeApp20.GetterSetter;

import java.io.Serializable;

public class NewsFeedPostMultimedia implements Serializable {

    String media_file;
    String media_file_thumb;
    String news_feed_id;
    String is_uploaded;
    String is_uploading_started;
    String media_type;
    String compressedPath;
    String folderUniqueId;

    public String getIs_uploading_started() {
        return is_uploading_started;
    }

    public void setIs_uploading_started(String is_uploading_started) {
        this.is_uploading_started = is_uploading_started;
    }

    String postText;

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getFolderUniqueId() {
        return folderUniqueId;
    }

    public void setFolderUniqueId(String folderUniqueId) {
        this.folderUniqueId = folderUniqueId;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getCompressedPath() {
        return compressedPath;
    }

    public void setCompressedPath(String compressedPath) {
        this.compressedPath = compressedPath;
    }

    public String getNews_feed_id() {
        return news_feed_id;
    }

    public void setNews_feed_id(String news_feed_id) {
        this.news_feed_id = news_feed_id;
    }

    public String getIs_uploaded() {
        return is_uploaded;
    }

    public void setIs_uploaded(String is_uploaded) {
        this.is_uploaded = is_uploaded;
    }

    public String getMedia_file() {
        return media_file;
    }

    public void setMedia_file(String media_file) {
        this.media_file = media_file;
    }

    public String getMedia_file_thumb() {
        return media_file_thumb;
    }

    public void setMedia_file_thumb(String media_file_thumb) {
        this.media_file_thumb = media_file_thumb;
    }

}
