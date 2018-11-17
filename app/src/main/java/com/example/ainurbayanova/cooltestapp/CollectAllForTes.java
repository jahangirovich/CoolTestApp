package com.example.ainurbayanova.cooltestapp;

import java.util.ArrayList;

public class CollectAllForTes {
    private String questions;
    private String correctAnswers;
    private String wrongAnswers;
    private Boolean answered;
    private String code;

    public CollectAllForTes(String questions,String correctAnswers,String wrongAnswers,Boolean answered,String code){
        this.questions = questions;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.answered = answered;
        this.code = code;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(String correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public String getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(String wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public Boolean getAnswered() {
        return answered;
    }

    public void setAnswered(Boolean answered) {
        this.answered = answered;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
