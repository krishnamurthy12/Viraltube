
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.checkUserPayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckUserPayment {

    @SerializedName("payment")
    @Expose
    private String payment;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("RESPONSECODE")
    @Expose
    private String rESPONSECODE;

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRESPONSECODE() {
        return rESPONSECODE;
    }

    public void setRESPONSECODE(String rESPONSECODE) {
        this.rESPONSECODE = rESPONSECODE;
    }

}
