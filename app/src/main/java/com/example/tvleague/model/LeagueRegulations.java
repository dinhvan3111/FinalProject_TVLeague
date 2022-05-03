package com.example.tvleague.model;

import androidx.databinding.ObservableArrayList;

import java.util.ArrayList;

public class LeagueRegulations {
    private int MIN_AGE ;
    private int MAX_AGE ;
    private int MIN_PLAYERS ;
    private int MAX_PLAYERS;
    private int MAX_FOREIGN_PLAYERS ;
    private int MAX_SCORE_TIME ;
    private int WIN_POINT ;
    private int DRAW_POINT;
    private int LOSE_POINT;
    private int RANKING_PRIORITY;

    public LeagueRegulations() {
        ObservableArrayList<Regulation> regulations = DatabaseRoute.getRegulations();
        this.MIN_AGE =  regulations.get(0).getRegulationNum();
        this.MAX_AGE= regulations.get(1).getRegulationNum();
        this.MIN_PLAYERS= regulations.get(2).getRegulationNum();
        this.MAX_PLAYERS  = regulations.get(3).getRegulationNum();
        this.MAX_FOREIGN_PLAYERS =regulations.get(4).getRegulationNum();
        this.MAX_SCORE_TIME = regulations.get(5).getRegulationNum();
        this.WIN_POINT = regulations.get(6).getRegulationNum();
        this.DRAW_POINT = regulations.get(7).getRegulationNum();
        this.LOSE_POINT = regulations.get(8).getRegulationNum();
        this.RANKING_PRIORITY  = regulations.get(9).getRegulationNum();
    }

    public int getMIN_AGE() {
        return MIN_AGE;
    }

    public void setMIN_AGE(int MIN_AGE) {
        this.MIN_AGE = MIN_AGE;
    }

    public int getMAX_AGE() {
        return MAX_AGE;
    }

    public void setMAX_AGE(int MAX_AGE) {
        this.MAX_AGE = MAX_AGE;
    }

    public int getMIN_PLAYERS() {
        return MIN_PLAYERS;
    }

    public void setMIN_PLAYERS(int MIN_PLAYERS) {
        this.MIN_PLAYERS = MIN_PLAYERS;
    }

    public int getMAX_PLAYERS() {
        return MAX_PLAYERS;
    }

    public void setMAX_PLAYERS(int MAX_PLAYERS) {
        this.MAX_PLAYERS = MAX_PLAYERS;
    }

    public int getMAX_FOREIGN_PLAYERS() {
        return MAX_FOREIGN_PLAYERS;
    }

    public void setMAX_FOREIGN_PLAYERS(int MAX_FOREIGN_PLAYERS) {
        this.MAX_FOREIGN_PLAYERS = MAX_FOREIGN_PLAYERS;
    }

    public int getMAX_SCORE_TIME() {
        return MAX_SCORE_TIME;
    }

    public void setMAX_SCORE_TIME(int MAX_SCORE_TIME) {
        this.MAX_SCORE_TIME = MAX_SCORE_TIME;
    }

    public int getWIN_POINT() {
        return WIN_POINT;
    }

    public void setWIN_POINT(int WIN_POINT) {
        this.WIN_POINT = WIN_POINT;
    }

    public int getDRAW_POINT() {
        return DRAW_POINT;
    }

    public void setDRAW_POINT(int DRAW_POINT) {
        this.DRAW_POINT = DRAW_POINT;
    }

    public int getLOSE_POINT() {
        return LOSE_POINT;
    }

    public void setLOSE_POINT(int LOSE_POINT) {
        this.LOSE_POINT = LOSE_POINT;
    }

    public int getRANKING_PRIORITY() {
        return RANKING_PRIORITY;
    }

    public void setRANKING_PRIORITY(int RANKING_PRIORITY_1) {
        this.RANKING_PRIORITY = RANKING_PRIORITY;
    }
}
