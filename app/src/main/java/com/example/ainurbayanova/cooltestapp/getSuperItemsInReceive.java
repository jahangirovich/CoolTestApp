package com.example.ainurbayanova.cooltestapp;

import java.util.ArrayList;

public class getSuperItemsInReceive {
    private ArrayList<Integer> enemyTests = new ArrayList<>();
    public getSuperItemsInReceive(ArrayList<Integer> enemyTests){
        this.enemyTests = enemyTests;
    }

    public ArrayList<Integer> getEnemyTests() {
        return enemyTests;
    }

    public void setEnemyTests(ArrayList<Integer> enemyTests) {
        this.enemyTests = enemyTests;
    }
}
