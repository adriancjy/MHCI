package com.example.mhci;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Comparator;

public class User implements Comparable<User> {
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

    @Override
    public int compareTo(User o) {
        return (this.getPoints() > o.getPoints() ? -1 :
                (this.getPoints() == o.getPoints() ? 0: 1));
    }

    public String toString(){
            return "       |         " + this.guid + "       |         " + this.points;

    }

}
