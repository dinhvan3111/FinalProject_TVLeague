package com.example.tvleague.model;

import java.io.Serializable;

public class Schedule implements Serializable {
    private String stadium;
    private String dateTime;
    private int round;
    private Club club1;
    private Club club2;
    private Match match;

    public Schedule(String stadium, String dateTime, int round, Club club1, Club club2) {
        this.stadium = stadium;
        this.dateTime = dateTime;
        this.round = round;
        this.club1 = club1;
        this.club2 = club2;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public Club getClub1() {
        return club1;
    }

    public void setClub1(Club club1) {
        this.club1 = club1;
    }

    public Club getClub2() {
        return club2;
    }

    public void setClub2(Club club2) {
        this.club2 = club2;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
