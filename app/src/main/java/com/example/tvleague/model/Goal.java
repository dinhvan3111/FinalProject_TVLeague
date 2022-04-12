package com.example.tvleague.model;

public class Goal {
    private Player player;
    private String time;
    private int type;

    public Goal(Player player, String time, int type) {
        this.player = player;
        this.time = time;
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
