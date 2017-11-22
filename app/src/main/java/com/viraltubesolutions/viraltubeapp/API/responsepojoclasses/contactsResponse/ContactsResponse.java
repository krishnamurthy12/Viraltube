
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.contactsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContactsResponse {

    @SerializedName("RESPONSECODE")
    @Expose
    private String rESPONSECODE;
    @SerializedName("contacts")
    @Expose
    private List<Contact> contacts = null;

    public String getRESPONSECODE() {
        return rESPONSECODE;
    }

    public void setRESPONSECODE(String rESPONSECODE) {
        this.rESPONSECODE = rESPONSECODE;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

}
