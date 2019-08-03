
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.uploadlimitcheck;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadLimitCheck {

    @SerializedName("user_video_count")
    @Expose
    private Integer userVideoCount;
    @SerializedName("video_limit")
    @Expose
    private String videoLimit;
    @SerializedName("RESPONSECODE")
    @Expose
    private String rESPONSECODE;

    public Integer getUserVideoCount() {
        return userVideoCount;
    }

    public void setUserVideoCount(Integer userVideoCount) {
        this.userVideoCount = userVideoCount;
    }

    public String getVideoLimit() {
        return videoLimit;
    }

    public void setVideoLimit(String videoLimit) {
        this.videoLimit = videoLimit;
    }

    public String getRESPONSECODE() {
        return rESPONSECODE;
    }

    public void setRESPONSECODE(String rESPONSECODE) {
        this.rESPONSECODE = rESPONSECODE;
    }

}
