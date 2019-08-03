
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.Abhi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("menu")
    @Expose
    private List<Menu> menu = null;

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

}
