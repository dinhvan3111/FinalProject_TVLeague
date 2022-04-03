package com.example.tvleague;

public class Player {
    private String name;
    private String doB;
    private int type;
    private String note;

    public Player(String name, String doB, int type, String note) {
        this.name = name;
        this.doB = doB;
        this.type = type;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoB() {
        return doB;
    }

    public void setDoB(String doB) {
        this.doB = doB;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
