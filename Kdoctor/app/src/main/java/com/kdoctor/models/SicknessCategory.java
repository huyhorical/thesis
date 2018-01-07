package com.kdoctor.models;

import com.google.gson.annotations.SerializedName;

import org.chalup.microorm.annotations.Column;

/**
 * Created by INI\huy.trinh on 31/10/2017.
 */

public class SicknessCategory {
    public SicknessCategory(int id, String name, String urlAPI){
        this.id = id;
        this.name = name;
        this.urlAPI = urlAPI;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlAPI() {
        return urlAPI;
    }

    public void setUrlAPI(String urlAPI) {
        this.urlAPI = urlAPI;
    }

    @SerializedName("Id")
    @Column("ID")
    int id;
    @SerializedName("TEN")
    @Column("NAME")
    String name;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("DESCRIPTION")
    @Column("DESCRIPTION")
    String description;

    @SerializedName("LINK")
    @Column("URL")
    String urlAPI;

    @SerializedName("IMAGE")
    @Column("IMAGE_URL")
    String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDataURL() {
        return dataURL;
    }

    public void setDataURL(String dataURL) {
        this.dataURL = dataURL;
    }

    @SerializedName("LINKDATA")
    @Column("DATA_URL")
    String dataURL;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @SerializedName("ACTIONNAME")
    @Column("ACTION")
    String action;

}
