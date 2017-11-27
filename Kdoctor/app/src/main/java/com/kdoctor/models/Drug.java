package com.kdoctor.models;

import com.google.gson.annotations.SerializedName;

import org.chalup.microorm.annotations.Column;

/**
 * Created by INI\huy.trinh on 24/11/2017.
 */

public class Drug {
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

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getUses() {
        return uses;
    }

    public void setUses(String uses) {
        this.uses = uses;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public String getCaution() {
        return caution;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @SerializedName("Id")
    @Column("ID")
    int id;

    @SerializedName("TEN")
    @Column("NAME")
    String name;

    @SerializedName("NSX")
    @Column("PRODUCER")
    String producer;

    @SerializedName("THANHPHAN")
    @Column("COMPONENT")
    String component;

    @SerializedName("CONGDUNG")
    @Column("USES")
    String uses;

    @SerializedName("CACHDUNG")
    @Column("GUIDE")
    String guide;

    @SerializedName("CHONGCHIDINH")
    @Column("CAUTION")
    String caution;

    @SerializedName("HINH")
    @Column("IMAGE_URL")
    String imageURL;

    @SerializedName("NOTE")
    @Column("NOTE")
    String note;

    public boolean isSelected() {
        return isSelected == 0 ? false : true;
    }

    public void setSelected(boolean selected) {
        isSelected = !selected ? 0 : 1;
    }

    @Column("SELECTED")
    int isSelected;
}
