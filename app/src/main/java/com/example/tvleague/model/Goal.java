package com.example.tvleague.model;

public class Goal {
    private Player player;
    private int time;
    private int type;
    private int MaDoiBong;
    private int MaTranDau;

    public Goal(Player player, int time, int type, int maDoiBong, int maTranDau) {
        this.player = player;
        this.time = time;
        this.type = type;
        this.MaDoiBong = maDoiBong;
        this.MaTranDau = maTranDau;
    }

    public int getMaTranDau() {
        return MaTranDau;
    }

    public void setMaTranDau(int maTranDau) {
        MaTranDau = maTranDau;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getMaDoiBong() {
        return MaDoiBong;
    }

    public void setMaDoiBong(int maDoiBong) {
        MaDoiBong = maDoiBong;
    }

}
