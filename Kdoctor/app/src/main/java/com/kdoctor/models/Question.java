package com.kdoctor.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by INI\huy.trinh on 31/10/2017.
 */

public class Question {
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionContaint() {
        return questionContaint;
    }

    public void setQuestionContaint(String questionContaint) {
        this.questionContaint = questionContaint;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    String questionId;
    String questionTitle;
    String questionContaint;
    List<String> answers;

    public Question(String data){
        try {
            data.replace("\"", "");
            String[] values = data.split("\\*");
            questionId = values[0];
            values = values[1].split("@");
            questionTitle = values[0].split(":")[0];
            questionContaint = values[0].split(":")[1];
            answers = new ArrayList<String>();
            for (int i = 1; i < values.length; i++) {
                answers.add(values[i]);
            }
        }

        catch (Exception e){
            Log.i("Question.java", e.toString());
        }
    }

    public String answer(String value){
        return questionId+","+questionTitle+"="+value;
    }

    public static boolean isQuestion(String value){
        try {
            if (value.replace("\"","").split("\\*")[0].equals("F")){
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public static String getAnswer(String value){
        try {
            if (!isQuestion(value)){
                return value.replace("\"","").split(",")[1];
            }
        }
        catch (Exception e){

        }
        return "";
    }
}
