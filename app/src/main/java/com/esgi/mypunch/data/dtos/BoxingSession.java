package com.esgi.mypunch.data.dtos;

import java.util.Date;

public class BoxingSession {

    private int id;
    private Date start;
    private Date end;
    private int nbPunches;
    private int average_power;
    private int min_power;
    private int max_power;

    // from the database : has an id
    public BoxingSession(int id, Date start, Date end, int nbPunches, int average_power, int min_power, int max_power) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.nbPunches = nbPunches;
        this.average_power = average_power;
        this.min_power = min_power;
        this.max_power = max_power;
    }

    // from the device : has no id yet
    public BoxingSession(Date start, Date end, int nbPunches, int average_power, int min_power, int max_power) {
        this.start = start;
        this.end = end;
        this.nbPunches = nbPunches;
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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getNbPunches() {
        return nbPunches;
    }

    public void setNbPunches(int nbPunches) {
        this.nbPunches = nbPunches;
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
                ", nbPunches=" + nbPunches +
                ", average_power=" + average_power +
                ", min_power=" + min_power +
                ", max_power=" + max_power +
                '}';
    }
}
