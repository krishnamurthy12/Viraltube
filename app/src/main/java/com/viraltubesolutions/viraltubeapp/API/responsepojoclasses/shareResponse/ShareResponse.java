
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.shareResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareResponse {

    @SerializedName("RESPONSECODE")
    @Expose
    private String rESPONSECODE;

    public String getRESPONSECODE() {
        return rESPONSECODE;
    }

    public void setRESPONSECODE(String rESPONSECODE) {
        this.rESPONSECODE = rESPONSECODE;
    }

}
