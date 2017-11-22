package com.viraltubesolutions.viraltubeapp.beanclasses;

/**
 * Created by Shashi on 10/05/2017.
 */
public class VideoDetails {
    String videoURL, videoTitle, numberOfViews, votes;

    public VideoDetails(String videoURL, String videoTitle, String videoNoViews, String votes) {
        this.videoURL = videoURL;
        this.videoTitle = videoTitle;
        this.numberOfViews = videoNoViews;
        this.votes = votes;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getNumberOfViews() {
        return numberOfViews;
    }

    public void setNumberOfViews(String numberOfViews) {
        this.numberOfViews = numberOfViews;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }
}
