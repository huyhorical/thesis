package com.kdoctor.models;

import com.google.gson.annotations.SerializedName;

import org.chalup.microorm.annotations.Column;

import java.util.HashMap;
import java.util.List;

/**
 * Created by INI\huy.trinh on 23/10/2017.
 */

public class CodeItem {
    public String getPrognostic() {
        return prognostic;
    }

    public void setPrognostic(String prognostic) {
        this.prognostic = prognostic;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    @SerializedName("TRIEUCHUNG")
    String prognostic;
    @SerializedName("CAUHOI")
    String question;
    @SerializedName("DAPAN")
    List<String> answers;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    String answer;
}
