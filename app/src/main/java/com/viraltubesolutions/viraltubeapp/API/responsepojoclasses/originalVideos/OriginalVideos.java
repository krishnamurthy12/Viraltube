
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.originalVideos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OriginalVideos {

    @SerializedName("videos")
    @Expose
    private List<Video> videos = null;

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

}
