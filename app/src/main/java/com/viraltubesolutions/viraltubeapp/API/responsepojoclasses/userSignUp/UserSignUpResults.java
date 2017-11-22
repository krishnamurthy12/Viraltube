
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.userSignUp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserSignUpResults {

    @SerializedName("RESPONSECODE")
    @Expose
    private String rESPONSECODE;
    @SerializedName("userid")
    @Expose
    private String userid;

    public String getRESPONSECODE() {
        return rESPONSECODE;
    }

    public void setRESPONSECODE(String rESPONSECODE) {
        this.rESPONSECODE = rESPONSECODE;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
