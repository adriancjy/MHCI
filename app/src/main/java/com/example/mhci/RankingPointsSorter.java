package com.example.mhci;

import java.util.ArrayList;
import java.util.Collections;

public class RankingPointsSorter {
    ArrayList<User> user = new ArrayList<>();

    public RankingPointsSorter(ArrayList<User> user){
        this.user = user;
    }

    public ArrayList<User> getSortedJobCandidateByAge(){
        Collections.sort(user);
        return user;
    }
}
