
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseVideoDetails {

    @SerializedName("videos")
    @Expose
    private List<Video> videos = null;

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "ResponseVideoDetails{" +
                "videos=" + videos +
                '}';
    }
}
