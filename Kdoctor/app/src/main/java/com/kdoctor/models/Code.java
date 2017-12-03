package com.kdoctor.models;

import com.google.gson.annotations.SerializedName;

import org.chalup.microorm.annotations.Column;

/**
 * Created by INI\huy.trinh on 23/10/2017.
 */

public class Code {
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategotyName() {
        return categotyName;
    }

    public void setCategotyName(String categotyName) {
        this.categotyName = categotyName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Column("VALUE")
    String value;

    @Column("CATEGORY_NAME")
    String categotyName;

    @Column("DATE")
    String date;
}
