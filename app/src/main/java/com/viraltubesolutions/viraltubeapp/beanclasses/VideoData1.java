package com.viraltubesolutions.viraltubeapp.beanclasses;

/**
 * Created by Manish on 10/30/2017.
 */

public class VideoData1 {
    public VideoData1(String id, String title, String thumbnailUrl, String videoUrl, Integer viewCount, String adsUrl, String user_vote, String vote_count) {
        this.id = id;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.viewCount = viewCount;
        this.adsUrl=adsUrl;
        this.vote_count=vote_count;
        this.user_vote=user_vote;
    }
    public String user_vote;

    public String id;

    public String title;

    public String thumbnailUrl;

    public String videoUrl;

    public Integer viewCount;
    public String adsUrl;
    public String vote_count;

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

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

    public String getAdsUrl() {
        return adsUrl;
    }

    public void setAdsUrl(String adsUrl) {
        this.adsUrl = adsUrl;
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

    public String getUser_vote() {
        return user_vote;
    }

    public void setUser_vote(String user_vote) {
        this.user_vote = user_vote;
    }
}
