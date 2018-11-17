package com.example.ainurbayanova.cooltestapp;

public class CollectionForReceive {
    private String correctAnswers,questions,wrongAnswers,code;

    public CollectionForReceive(String correctAnswers, String questions, String wrongAnswers,String code) {
        this.correctAnswers = correctAnswers;
        this.questions = questions;
        this.wrongAnswers = wrongAnswers;
        this.code = code;
    }

    public String getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(String correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(String wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
