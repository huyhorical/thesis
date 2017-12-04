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

    public String getCategotyDataPath() {
        return categotyDataPath;
    }

    public void setCategotyDataPath(String categotyDataPath) {
        this.categotyDataPath = categotyDataPath;
    }

    @Column("CATEGORY_DATA_PATH")
    String categotyDataPath;

    @Column("CATEGORY_NAME")
    String categotyName;

    public String getCategotyAction() {
        return categotyAction;
    }

    public void setCategotyAction(String categotyAction) {
        this.categotyAction = categotyAction;
    }

    @Column("CATEGORY_ACTION")
    String categotyAction;

    @Column("DATE")
    String date;
}
