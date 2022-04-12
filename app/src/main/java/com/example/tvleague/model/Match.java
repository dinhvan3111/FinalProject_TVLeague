package com.example.tvleague.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Match{
    private String score;
    private ArrayList<Goal> listGoalClub1;
    private ArrayList<Goal> listGoalClub2;

    public Match(String score, ArrayList<Goal> listGoalClub1, ArrayList<Goal> listGoalClub2) {
        this.score = score;
        this.listGoalClub1 = listGoalClub1;
        this.listGoalClub2 = listGoalClub2;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public ArrayList<Goal> getListGoalClub1() {
        return listGoalClub1;
    }

    public void setListGoalClub1(ArrayList<Goal> listGoalClub1) {
        this.listGoalClub1 = listGoalClub1;
    }

    public ArrayList<Goal> getListGoalClub2() {
        return listGoalClub2;
    }

    public void setListGoalClub2(ArrayList<Goal> listGoalClub2) {
        this.listGoalClub2 = listGoalClub2;
    }
}
