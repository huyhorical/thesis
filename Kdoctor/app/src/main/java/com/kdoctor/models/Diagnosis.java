package com.kdoctor.models;

import android.util.Log;

import org.chalup.microorm.annotations.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by INI\huy.trinh on 23/11/2017.
 */

public class Diagnosis {
    public Diagnosis(String code){

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    List<Item> itemList;

    @Column("ID")
    int id;
    @Column("CODE")
    String code;
    @Column("RESULT")
    String result;
    @Column("DATE")
    String date;

    public void init(){
        if (code != null && code != ""){
            if (itemList == null) {
                itemList = new ArrayList<>();
            }
            else{
                itemList.clear();
            }
            String[] arr = code.split("\\*");
            String categoryName = arr[0];
            String resultID = arr[arr.length-1];

            for (int i = 1; i < arr.length - 1; i++){
                Item item = new Item();
                item.setQuestion(arr[i].split(Pattern.quote("|"))[0]);
                item.setAnswer(arr[i].split(Pattern.quote("|"))[1].split("=")[1]);
                itemList.add(item);
            }
        }
    }

    public class Item{
        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        String question;
        String answer;
    }
}
