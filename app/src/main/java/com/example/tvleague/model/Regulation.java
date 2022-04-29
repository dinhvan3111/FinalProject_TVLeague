package com.example.tvleague.model;

public class Regulation {
    private String regulationDes;
    private int regulationNum;
    private String note;

    public Regulation(String regulationDes, int regulationNum, String note) {
        this.regulationDes = regulationDes;
        this.regulationNum = regulationNum;
        this.note = note;
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
