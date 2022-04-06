package com.example.tvleague;

import java.io.Serializable;
import java.util.ArrayList;

public class Club implements Serializable {
    int id;
    String name;
    String stadium;
    int index;
    public Club(int id, String name, String stadium, int index) {
        this.id = id;
        this.name = name;
        this.stadium = stadium;
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }
}
