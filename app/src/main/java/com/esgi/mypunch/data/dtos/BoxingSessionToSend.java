package com.esgi.mypunch.data.dtos;

import java.io.Serializable;

public class BoxingSessionToSend implements Serializable {

    private int id;
    private String start;
    private String end;
    private int nb_punches;
    private int average_power;
    private int min_power;
    private int max_power;

    public BoxingSessionToSend(){

    }

    // from the database : has an id
    public BoxingSessionToSend(int id, String start, String end, int nbPunches, int average_power, int min_power, int max_power) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.nb_punches = nbPunches;
        this.average_power = average_power;
        this.min_power = min_power;
        this.max_power = max_power;
    }

    // from the device : has no id yet
    public BoxingSessionToSend(String start, String end, int nbPunches, int average_power, int min_power, int max_power) {
        this.start = start;
        this.end = end;
        this.nb_punches = nbPunches;
        this.average_power = average_power;
        this.min_power = min_power;
        this.max_power = max_power;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getNbPunches() {
        return nb_punches;
    }

    public void setNbPunches(int nbPunches) {
        this.nb_punches = nbPunches;
    }

    public int getAverage_power() {
        return average_power;
    }

    public void setAverage_power(int average_power) {
        this.average_power = average_power;
    }

    public int getMin_power() {
        return min_power;
    }

    public void setMin_power(int min_power) {
        this.min_power = min_power;
    }

    public int getMax_power() {
        return max_power;
    }

    public void setMax_power(int max_power) {
        this.max_power = max_power;
    }

    @Override
    public String toString() {
        return "BoxingSession{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", nbPunches=" + nb_punches +
                ", average_power=" + average_power +
                ", min_power=" + min_power +
                ", max_power=" + max_power +
                '}';
    }
}
