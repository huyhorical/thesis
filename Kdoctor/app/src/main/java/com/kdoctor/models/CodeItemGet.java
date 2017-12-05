package com.kdoctor.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INI\huy.trinh on 23/10/2017.
 */

public class CodeItemGet {


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ItemGet> getItemGetList() {
        return itemGetList;
    }

    public void setItemGetList(List<ItemGet> itemGetList) {
        this.itemGetList = itemGetList;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("ID")
    int id;
    @SerializedName("CODE")
    String code;
    @SerializedName("NOIDUNG")
    List<ItemGet> itemGetList;
    @SerializedName("NOTE")
    String note;

    public class ItemGet{
        public String getPrognostic() {
            return prognostic;
        }

        public void setPrognostic(String prognostic) {
            this.prognostic = prognostic;
        }

        @SerializedName("TRIEUCHUNG")
        String prognostic;

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        @SerializedName("DAPAN")
        String answer;
    }
}
