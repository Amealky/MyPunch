package com.esgi.mypunch.data.dtos;

import java.util.Date;

public class BoxingSession {

    private int id;
    private Date start;
    private Date end;
    private double averageAcceleration;
    private double averageForce;

    // if from databaseit has an id
    public BoxingSession(int id, Date start, Date end, double averageAcceleration, double averageForce) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.averageAcceleration = averageAcceleration;
        this.averageForce = averageForce;
    }

    // if from the device it has no id yet
    public BoxingSession(Date start, Date end, double averageAcceleration, double averageForce) {
        this.start = start;
        this.end = end;
        this.averageAcceleration = averageAcceleration;
        this.averageForce = averageForce;
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

    public double getAverageAcceleration() {
        return averageAcceleration;
    }

    public void setAverageAcceleration(double averageAcceleration) {
        this.averageAcceleration = averageAcceleration;
    }

    public double getAverageForce() {
        return averageForce;
    }

    public void setAverageForce(double averageForce) {
        this.averageForce = averageForce;
    }

    @Override
    public String toString() {
        return "BoxingSession{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", averageAcceleration=" + averageAcceleration +
                ", averageForce=" + averageForce +
                '}';
    }
}
