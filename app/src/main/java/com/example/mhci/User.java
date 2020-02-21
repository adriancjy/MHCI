package com.example.mhci;

public class User {
    String guid;
    int points;

    public User(){

    }

    public User(String guid, int points){
        this.guid = guid;
        this.points = points;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
