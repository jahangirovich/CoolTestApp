package com.example.ainurbayanova.cooltestapp;

import java.util.ArrayList;

public class testQuestions {
    private String questions,rights;
    private ArrayList<String> answers = new ArrayList<>();
    private int testSize = 0;
    private String code = "";

    public testQuestions(String questions,String rights,ArrayList<String> answers,int testSize,String code){
        this.questions = questions;
        this.rights = rights;
        this.answers = answers;
        this.testSize = testSize;
        this.code = code;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public int getTestSize() {
        return testSize;
    }

    public void setTestSize(int testSize) {
        this.testSize = testSize;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
