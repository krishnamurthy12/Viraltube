
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.userLogIn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginResults {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("RESPONSECODE")
    @Expose
    private String rESPONSECODE;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRESPONSECODE() {
        return rESPONSECODE;
    }

    public void setRESPONSECODE(String rESPONSECODE) {
        this.rESPONSECODE = rESPONSECODE;
    }

}
