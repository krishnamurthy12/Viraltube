
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.Abhi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Menu {

    @SerializedName("division")
    @Expose
    private Division division;
    @SerializedName("cat")
    @Expose
    private List<Cat> cat = null;

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public List<Cat> getCat() {
        return cat;
    }

    public void setCat(List<Cat> cat) {
        this.cat = cat;
    }

}
