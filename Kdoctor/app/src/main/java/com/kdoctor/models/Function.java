package com.kdoctor.models;

import com.google.gson.annotations.SerializedName;

import org.chalup.microorm.annotations.Column;

/**
 * Created by Huy on 10/30/2017.
 */

public class Function {
    public Function(String tab, String function, String status){
        setTab(tab);
        setFunction(function);
        setStatus(status);
    }
    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("TAB")
    @Column("TAB")
    String tab;

    @SerializedName("FUNCTION")
    @Column("FUNCTION")
    String function;

    @SerializedName("STATUS")
    @Column("STATUS")
    String status;
}
