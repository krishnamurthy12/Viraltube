
package com.viraltubesolutions.viraltubeapp.API.responsepojoclasses.subCatagories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("subCategoryId")
    @Expose
    private String subCategoryId;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("videoUrl")
    @Expose
    private String videoUrl;
    @SerializedName("thumbnailUrl")
    @Expose
    private String thumbnailUrl;

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

}
