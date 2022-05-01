package com.example.tvleague.model;

public class Regulation {
    private int id;
    private String regulationDes;
    private int regulationNum;
    private int attribute;
    private String note;

    public Regulation(int id, String regulationDes, int regulationNum, int attribute, String note) {
        this.id = id;
        this.regulationDes = regulationDes;
        this.regulationNum = regulationNum;
        this.attribute = attribute;
        this.note = note;
    }

    public int getAttribute() {
        return attribute;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegulationDes() {
        return regulationDes;
    }

    public void setRegulationDes(String regulationDes) {
        this.regulationDes = regulationDes;
    }

    public int getRegulationNum() {
        return regulationNum;
    }

    public void setRegulationNum(int regulationNum) {
        this.regulationNum = regulationNum;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
