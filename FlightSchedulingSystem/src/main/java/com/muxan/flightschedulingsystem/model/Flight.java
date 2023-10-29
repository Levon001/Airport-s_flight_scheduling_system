package com.muxan.flightschedulingsystem.model;

public class Flight {
    private int flightId;
    private int direction;
    private String country;
    private Airplane airplane;
    private RunwayWithPeriod runwayWithPeriod;
    private GateWithPeriod gateWithPeriod;

    public Flight(int flightId, int direction, String country, Airplane airplane, RunwayWithPeriod runwayWithPeriod, GateWithPeriod gateWithPeriod) {
        this.flightId = flightId;
        this.direction = direction;
        this.country = country;
        this.airplane = airplane;
        this.runwayWithPeriod = runwayWithPeriod;
        this.gateWithPeriod = gateWithPeriod;
    }

    public Flight(int flightId, int direction, String country, Airplane airplane) {
        this.flightId = flightId;
        this.direction = direction;
        this.country = country;
        this.airplane = airplane;
    }

    public Flight(int direction, String country, Airplane airplane, RunwayWithPeriod runwayWithPeriod, GateWithPeriod gateWithPeriod) {
        this.direction = direction;
        this.country = country;
        this.airplane = airplane;
        this.runwayWithPeriod = runwayWithPeriod;
        this.gateWithPeriod = gateWithPeriod;
    }

    public int getFlightId() {
        return flightId;
    }

    public int getDirection() {
        return direction;
    }

    public String getCountry() {
        return country;
    }

    public Airplane getAirplane() {
        return airplane;
    }

    public RunwayWithPeriod getRunwayWithPeriod() {
        return runwayWithPeriod;
    }

    public GateWithPeriod getGateWithPeriod() {
        return gateWithPeriod;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAirplane(Airplane airplane) {
        this.airplane = airplane;
    }

    public void setRunwayWithPeriod(RunwayWithPeriod runwayWithPeriod) {
        this.runwayWithPeriod = runwayWithPeriod;
    }

    public void setGateWithPeriod(GateWithPeriod gateWithPeriod) {
        this.gateWithPeriod = gateWithPeriod;
    }


    public String getDetails() {
        return "Flight details: " + this.direction + ", " + this.country + ", " + this.airplane.getNameAirplane() + ", " + this.airplane.getAllSeats();
    }


}


