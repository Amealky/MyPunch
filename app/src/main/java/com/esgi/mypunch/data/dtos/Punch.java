package com.esgi.mypunch.data.dtos;


public class Punch {
    private int id;
    private double acceleration;
    private double force;
    private int sessionId;

    public Punch(int id, double acceleration, double force, int sessionId) {
        this.id = id;
        this.acceleration = acceleration;
        this.force = force;
        this.sessionId = sessionId;
    }

    public int getId() {
        return id;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getForce() {
        return force;
    }

    public int getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return "Punch{" +
                "id=" + id +
                ", acceleration=" + acceleration +
                ", force=" + force +
                ", sessionId=" + sessionId +
                '}';
    }
}
