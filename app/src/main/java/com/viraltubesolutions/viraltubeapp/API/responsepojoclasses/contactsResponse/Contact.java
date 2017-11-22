
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.contactsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contact {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("name")
    @Expose
    private String name;

    public Contact() {
    }

    public Contact(String name, String mobile ) {
        this.mobile = mobile;
        this.name = name;
    }

    public char getContactPic() {
        return name.charAt(0);
    }

    public void setContactPic(char contactPic) {
        this.contactPic = contactPic;
    }

    char contactPic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
