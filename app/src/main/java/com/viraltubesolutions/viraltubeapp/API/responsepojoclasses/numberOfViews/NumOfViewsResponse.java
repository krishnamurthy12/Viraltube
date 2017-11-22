
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.numberOfViews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NumOfViewsResponse {

    @SerializedName("RESPONSECODE")
    @Expose
    private String rESPONSECODE;

    public String getrESPONSECODE() {
        return rESPONSECODE;
    }

    public void setrESPONSECODE(String rESPONSECODE) {
        this.rESPONSECODE = rESPONSECODE;
    }
}
