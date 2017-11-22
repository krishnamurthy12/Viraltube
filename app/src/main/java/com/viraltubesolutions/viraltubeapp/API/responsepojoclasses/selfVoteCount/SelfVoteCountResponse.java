
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.selfVoteCount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelfVoteCountResponse {

    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("RESPONSECODE")
    @Expose
    private String rESPONSECODE;

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public String getRESPONSECODE() {
        return rESPONSECODE;
    }

    public void setRESPONSECODE(String rESPONSECODE) {
        this.rESPONSECODE = rESPONSECODE;
    }

}
