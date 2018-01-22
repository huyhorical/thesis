package com.kdoctor.models;

import com.google.gson.annotations.SerializedName;

import org.chalup.microorm.annotations.Column;

/**
 * Created by INI\huy.trinh on 24/11/2017.
 */

public class VaccineCenter {

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @SerializedName("ID")
    @Column("ID")
    int id;
    @SerializedName("NAME")
    @Column("NAME")
    String name;
    @SerializedName("ADDRESS")
    @Column("ADDRESS")
    String address;
    @SerializedName("PHONENUM")
    @Column("PHONENUM")
    String phone;
    @SerializedName("LAITUDE")
    @Column("LAITUDE")
    double lat;
    @SerializedName("LONGITUDE")
    @Column("LONGITUDE")
    double lon;
    @SerializedName("NOTE")
    @Column("NOTE")
    String note;

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    @SerializedName("CALENDAR")
    @Column("CALENDAR")
    String calendar;
}
