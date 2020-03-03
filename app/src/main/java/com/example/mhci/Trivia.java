package com.example.mhci;

public class Trivia {

    public String getTriviaText() {
        return triviaText;
    }

    public void setTriviaText(String triviaText) {
        this.triviaText = triviaText;
    }

    public String triviaText;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String schoolName;


    public Trivia(){

    }

    public Trivia(String triviaText){
        this.triviaText = triviaText;
    }
}
