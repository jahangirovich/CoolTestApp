package com.example.ainurbayanova.cooltestapp;

public class CollectEnemyInformation {
    private String enemyImage,enemyName,enemyScore,myScore,title;

    public CollectEnemyInformation(String enemyImage, String enemyName, String enemyScore, String myScore, String title) {
        this.enemyImage = enemyImage;
        this.enemyName = enemyName;
        this.enemyScore = enemyScore;
        this.myScore = myScore;
        this.title = title;
    }

    public String getEnemyImage() {
        return enemyImage;
    }

    public void setEnemyImage(String enemyImage) {
        this.enemyImage = enemyImage;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }

    public String getEnemyScore() {
        return enemyScore;
    }

    public void setEnemyScore(String enemyScore) {
        this.enemyScore = enemyScore;
    }

    public String getMyScore() {
        return myScore;
    }

    public void setMyScore(String myScore) {
        this.myScore = myScore;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
