package com.kdoctor.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.chalup.microorm.annotations.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

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

    public List<LinkRef> getLinkRefs() {
        return linkRefs;
    }

    public void setLinkRefs(List<LinkRef> linkRefs) {
        this.linkRefs = linkRefs;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }

    @Column("LINK_REF")
    String linkRef;

    public List<LinkRef> linkRefToList(){
        List<LinkRef> linkR = new ArrayList<>();
        try{
            if (linkRef != null && !linkRef.equals("")){
                String strs[] = linkRef.split("\\|");
                for (String s:strs
                     ) {
                    try{
                    String strss[] = s.split("\\*");
                    LinkRef li = new LinkRef();
                    li.setTenBV(strss[0]);
                    li.setLinkBV(strss[1]);
                        linkR.add(li);
                    }
                    catch (Exception e){

                    }
                }
            }
        }
        catch (Exception e){

        }
        linkRefs = linkR;
        return linkR;
    }

    public String listToLinkRef(){
        String st = "";
        if (linkRefs != null){
            for (LinkRef l :
                    linkRefs) {
                st += l.getTenBV() + "*" + l.getLinkBV() + "|";
            }
        }
        st = st.substring(0,st.length()-1);
        linkRef = st;
        return st;
    }

    @SerializedName("LSTBV")
    List<LinkRef> linkRefs;

    public class LinkRef{
        public String getTenBV() {
            return tenBV;
        }

        public void setTenBV(String tenBV) {
            this.tenBV = tenBV;
        }

        public String getLinkBV() {
            return linkBV;
        }

        public void setLinkBV(String linkBV) {
            this.linkBV = linkBV;
        }

        @SerializedName("TENBV")
        String tenBV;
        @SerializedName("LINKBV")
        String linkBV;
    }
}
