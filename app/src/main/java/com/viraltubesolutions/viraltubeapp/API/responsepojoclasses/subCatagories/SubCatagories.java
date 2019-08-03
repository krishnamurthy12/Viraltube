
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.subCatagories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCatagories {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("RESPONSECODE")
    @Expose
    private String rESPONSECODE;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getRESPONSECODE() {
        return rESPONSECODE;
    }

    public void setRESPONSECODE(String rESPONSECODE) {
        this.rESPONSECODE = rESPONSECODE;
    }

}
