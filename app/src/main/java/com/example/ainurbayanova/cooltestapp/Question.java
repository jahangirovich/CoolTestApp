package com.example.ainurbayanova.cooltestapp;

import java.util.ArrayList;

public class Question  {
    private String question;
    String correct;
    ArrayList<String> answersList;

    public Question(){

    }
    public Question(String q, ArrayList<String> a, String c){
        question = q;
        answersList = a;
        correct = c;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public ArrayList<String> getAnswersList() {
        return answersList;
    }

    public void setAnswersList(ArrayList<String> answersList) {
        this.answersList = answersList;
    }
}

