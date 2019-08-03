
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.callus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallUs {

    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("RESPONSECODE")
    @Expose
    private String rESPONSECODE;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRESPONSECODE() {
        return rESPONSECODE;
    }

    public void setRESPONSECODE(String rESPONSECODE) {
        this.rESPONSECODE = rESPONSECODE;
    }

}
