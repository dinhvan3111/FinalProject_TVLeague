package com.example.tvleague.model;

public class TopScore {
    private int index;
    private Player player;
    private int quantity_score;

    public TopScore(int index, Player player, int quantity_score) {
        this.index = index;
        this.player = player;
        this.quantity_score = quantity_score;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getQuantity_score() {
        return quantity_score;
    }

    public void setQuantity_score(int quantity_score) {
        this.quantity_score = quantity_score;
    }
}
