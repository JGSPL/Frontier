
package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NewsFeedList implements Serializable {

    @SerializedName("news_feed_id")
    @Expose
    private String newsFeedId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("media_file")
    @Expose
    private String mediaFile;
    @SerializedName("post_status")
    @Expose
    private String postStatus;
    @SerializedName("thumb_image")
    @Expose
    private String thumbImage;
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("post_date")
    @Expose
    private String postDate;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("attendee_id")
    @Expose
    private String attendeeId;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("like_flag")
    @Expose
    private String likeFlag;
    @SerializedName("total_likes")
    @Expose
    private String totalLikes;
    @SerializedName("total_comments")
    @Expose
    private String totalComments;

    public String getLike_type() {
        return like_type;
    }

    public void setLike_type(String like_type) {
        this.like_type = like_type;
    }

    @SerializedName("like_type")
    @Expose
    private String like_type;

    public String getAttendee_type() {
        return attendee_type;
    }

    public void setAttendee_type(String attendee_type) {
        this.attendee_type = attendee_type;
    }

    @SerializedName("attendee_type")
    @Expose
    private String attendee_type;

    public List<com.procialize.mrgeApp20.GetterSetter.news_feed_media> getNews_feed_media() {
        return news_feed_media;
    }

    public void setNews_feed_media(List<com.procialize.mrgeApp20.GetterSetter.news_feed_media> news_feed_media) {
        this.news_feed_media = news_feed_media;
    }

    @SerializedName("news_feed_media")
    @Expose
    private List<com.procialize.mrgeApp20.GetterSetter.news_feed_media> news_feed_media = null;

    public String getNewsFeedId() {
        return newsFeedId;
    }

    public void setNewsFeedId(String newsFeedId) {
        this.newsFeedId = newsFeedId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(String mediaFile) {
        this.mediaFile = mediaFile;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLikeFlag() {
        return likeFlag;
    }

    public void setLikeFlag(String likeFlag) {
        this.likeFlag = likeFlag;
    }

    public String getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(String totalLikes) {
        this.totalLikes = totalLikes;
    }

    public String getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(String totalComments) {
        this.totalComments = totalComments;
    }

}
