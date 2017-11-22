
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelfUploadResponse {

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
