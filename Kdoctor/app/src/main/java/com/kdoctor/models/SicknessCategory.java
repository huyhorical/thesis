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
    @SerializedName("LINK")
    @Column("URL")
    String urlAPI;
}
