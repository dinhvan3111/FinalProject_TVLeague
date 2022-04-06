package com.example.tvleague;

import java.io.Serializable;

public class Player implements Serializable {
    int id;
    private String name;
    private String doB;
    private int type;
    private String note;



    public Player(int id, String name, String doB, int type, String note) {
        this.id = id;
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
    }public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
