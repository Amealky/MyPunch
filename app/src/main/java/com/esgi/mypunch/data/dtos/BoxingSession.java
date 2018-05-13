package com.esgi.mypunch.data.dtos;

import java.util.Date;
import java.util.List;

public class BoxingSession {
    private int id;
    private Date start;
    private Date end;
    private List<Punch> punches;

    public BoxingSession(int id, Date start, Date end, List<Punch> punches) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.punches = punches;
    }

    public int getId() {
        return id;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public List<Punch> getPunches() {
        return punches;
    }

    @Override
    public String toString() {
        return "BoxingSession{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", punches=" + punches +
                '}';
    }
}
