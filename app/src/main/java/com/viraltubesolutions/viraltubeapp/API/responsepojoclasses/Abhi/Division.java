
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.Abhi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Division {

    @SerializedName("stock_division_id")
    @Expose
    private String stockDivisionId;
    @SerializedName("stock_division_name")
    @Expose
    private String stockDivisionName;
    @SerializedName("stock_division_status")
    @Expose
    private String stockDivisionStatus;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;

    public String getStockDivisionId() {
        return stockDivisionId;
    }

    public void setStockDivisionId(String stockDivisionId) {
        this.stockDivisionId = stockDivisionId;
    }

    public String getStockDivisionName() {
        return stockDivisionName;
    }

    public void setStockDivisionName(String stockDivisionName) {
        this.stockDivisionName = stockDivisionName;
    }

    public String getStockDivisionStatus() {
        return stockDivisionStatus;
    }

    public void setStockDivisionStatus(String stockDivisionStatus) {
        this.stockDivisionStatus = stockDivisionStatus;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

}
