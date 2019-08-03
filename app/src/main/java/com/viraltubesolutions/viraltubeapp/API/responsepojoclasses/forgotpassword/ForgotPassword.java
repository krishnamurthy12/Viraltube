
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.forgotpassword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPassword {

    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("RESPONSECODE")
    @Expose
    private String rESPONSECODE;

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRESPONSECODE() {
        return rESPONSECODE;
    }

    public void setRESPONSECODE(String rESPONSECODE) {
        this.rESPONSECODE = rESPONSECODE;
    }

}
