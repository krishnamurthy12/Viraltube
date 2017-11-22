package com.viraltubesolutions.viraltubeapp.beanclasses;

/**
 * Created by Shashi on 10/05/2017.
 */

public class ContactsBeanClass {
    char contactPic;
    String contactName;
    String contactnumber;

    public ContactsBeanClass(char contactPic, String contactName, String contactnumber) {
        this.contactPic = contactPic;
        this.contactName = contactName;
        this.contactnumber = contactnumber;
    }
    public ContactsBeanClass(String contactName, String contactnumber) {
        this.contactName = contactName;
        this.contactnumber = contactnumber;
    }

    public char getContactPic() {
        return contactPic;
    }

    public void setContactPic(char contactPic) {
        this.contactPic = contactPic;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }
}
