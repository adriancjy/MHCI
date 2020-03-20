package com.example.mhci;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Comparator;

public class User implements Comparable<User> {
    String guid;
    int points;

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    String information;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    String course;

    public User(){

    }
    public User(String guid, int points){
        this.guid = guid;
        this.points = points;

    }

    public User(String guid, int points, String name, String course, String information){
        this.guid = guid;
        this.points = points;
        this.name = name;
        this.course = course;
        this.information = information;
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
            return  this.guid + ", " + this.name + ", " + this.points;

    }

}
