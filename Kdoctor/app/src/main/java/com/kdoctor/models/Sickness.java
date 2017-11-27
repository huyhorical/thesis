package com.kdoctor.models;

import com.google.gson.annotations.SerializedName;

import org.chalup.microorm.annotations.Column;

/**
 * Created by Huy on 10/30/2017.
 */

public class Sickness {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPrognostic() {
        return prognostic;
    }

    public void setPrognostic(String prognostic) {
        this.prognostic = prognostic;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @SerializedName("Id")
    @Column("ID")
    int id;

    @SerializedName("MADMBENH")
    @Column("CATEGORY_ID")
    int categoryId;

    @SerializedName("TENBENH")
    @Column("NAME")
    String name;

    @SerializedName("TONGQUAN")
    @Column("SUMMARY")
    String summary;

    @SerializedName("TRIEUCHUNG")
    @Column("PROGNOSTIC")
    String prognostic;

    @SerializedName("HUONGDIEUTRI")
    @Column("TREATMENT")
    String treatment;

    @SerializedName("HINH")
    @Column("IMAGE_URL")
    String imageURL;

    public boolean isSelected() {
        return isSelected == 0 ? false : true;
    }

    public void setSelected(boolean selected) {
        isSelected = !selected ? 0 : 1;
    }

    @Column("SELECTED")
    int isSelected;
}
