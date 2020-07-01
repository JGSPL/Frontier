package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 11/8/2017.
 */

public class CommentList {

    @SerializedName("comment_data_list")
    @Expose
    private List<CommentDataList> commentDataList = null;

    @SerializedName("profile_pic_url_path")
    @Expose
    private String profile_pic_url_path = null;

    public String getProfile_pic_url_path() {
        return profile_pic_url_path;
    }

    public void setProfile_pic_url_path(String profile_pic_url_path) {
        this.profile_pic_url_path = profile_pic_url_path;
    }

    public List<CommentDataList> getCommentDataList() {
        return commentDataList;
    }

    public void setCommentDataList(List<CommentDataList> commentDataList) {
        this.commentDataList = commentDataList;
    }
}
