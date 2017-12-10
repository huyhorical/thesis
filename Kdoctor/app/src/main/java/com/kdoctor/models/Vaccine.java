package com.kdoctor.models;

import com.google.gson.annotations.SerializedName;

import org.chalup.microorm.annotations.Column;

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
        return isSelected == 0 ? false : true;
    }

    public void setSelected(boolean selected) {
        isSelected = !selected ? 0 : 1;
    }

    public boolean isRead() {
        return isRead == 0 ? false : true;
    }

    public void setRead(boolean read) {
        isRead = !read ? 0 : 1;
    }

    @SerializedName("ID")
    @Column("ID")
    int id;

    @SerializedName("ACTIVITY")
    @Column("ACTIVITY")
    String activity;

    @SerializedName("START_MONTH")
    @Column("START_MONTH")
    int startMonth;

    @SerializedName("END_MONTH")
    @Column("END_MONTH")
    int endMonth;

    @SerializedName("NOTE")
    @Column("NOTE")
    String note;

    public String getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(String alarmDate) {
        this.alarmDate = alarmDate;
    }

    @Column("ALARM_DATE")
    String alarmDate;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column("MESSAGE")
    String message;

    @Column("SELECTED")
    int isSelected;

    @Column("READ")
    int isRead;
}
