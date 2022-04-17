package com.example.tvleague.model;

import androidx.databinding.ObservableArrayList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Match implements Serializable{
    private int id;
    private String score;
    private ObservableArrayList<Goal> listGoalClub1;
    private ObservableArrayList<Goal> listGoalClub2;

    public Match(int id,String score, ObservableArrayList<Goal> listGoalClub1, ObservableArrayList<Goal> listGoalClub2) {
        this.id = id;
        this.score = score;
        this.listGoalClub1 = listGoalClub1;
        this.listGoalClub2 = listGoalClub2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public ObservableArrayList<Goal> getListGoalClub1() {
        return listGoalClub1;
    }

    public void setListGoalClub1(ObservableArrayList<Goal> listGoalClub1) {
        this.listGoalClub1 = listGoalClub1;
    }

    public ObservableArrayList<Goal> getListGoalClub2() {
        return listGoalClub2;
    }

    public void setListGoalClub2(ObservableArrayList<Goal> listGoalClub2) {
        this.listGoalClub2 = listGoalClub2;
    }
}
