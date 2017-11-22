
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.searchResults;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("thumbnailUrl")
    @Expose
    private String thumbnailUrl;
    @SerializedName("videoUrl")
    @Expose
    private String videoUrl;
    @SerializedName("view_count")
    @Expose
    private Integer viewCount;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("user_vote")
    @Expose
    private Integer userVote;
    @SerializedName("adsUrl")
    @Expose
    private String adsUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getUserVote() {
        return userVote;
    }

    public void setUserVote(Integer userVote) {
        this.userVote = userVote;
    }

    public String getAdsUrl() {
        return adsUrl;
    }

    public void setAdsUrl(String adsUrl) {
        this.adsUrl = adsUrl;
    }

}
