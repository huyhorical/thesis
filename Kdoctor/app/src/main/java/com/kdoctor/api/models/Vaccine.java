package com.kdoctor.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by INI\huy.trinh on 23/10/2017.
 */

public class Vaccine {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @SerializedName("ID")
    int id;
    @SerializedName("ACTIVITY")
    String activity;
    @SerializedName("START_MONTH")
    int startMonth;
    @SerializedName("END_MONTH")
    int endMonth;
    @SerializedName("NOTE")
    String note;

    boolean isSelected;
}
