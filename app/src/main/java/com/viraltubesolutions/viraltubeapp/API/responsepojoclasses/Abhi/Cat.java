
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.Abhi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cat {

    @SerializedName("stock_category_id")
    @Expose
    private String stockCategoryId;
    @SerializedName("stock_division_id")
    @Expose
    private String stockDivisionId;
    @SerializedName("stock_category_name")
    @Expose
    private String stockCategoryName;
    @SerializedName("stock_category_status")
    @Expose
    private String stockCategoryStatus;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;

    public String getStockCategoryId() {
        return stockCategoryId;
    }

    public void setStockCategoryId(String stockCategoryId) {
        this.stockCategoryId = stockCategoryId;
    }

    public String getStockDivisionId() {
        return stockDivisionId;
    }

    public void setStockDivisionId(String stockDivisionId) {
        this.stockDivisionId = stockDivisionId;
    }

    public String getStockCategoryName() {
        return stockCategoryName;
    }

    public void setStockCategoryName(String stockCategoryName) {
        this.stockCategoryName = stockCategoryName;
    }

    public String getStockCategoryStatus() {
        return stockCategoryStatus;
    }

    public void setStockCategoryStatus(String stockCategoryStatus) {
        this.stockCategoryStatus = stockCategoryStatus;
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
