
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.afterPayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AfterPaymentResponse {

    @SerializedName("pay_amount")
    @Expose
    private String payAmount;
    @SerializedName("RESPONSECODE")
    @Expose
    private String rESPONSECODE;

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getRESPONSECODE() {
        return rESPONSECODE;
    }

    public void setRESPONSECODE(String rESPONSECODE) {
        this.rESPONSECODE = rESPONSECODE;
    }

}
