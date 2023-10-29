package com.muxan.flightschedulingsystem.model;

import lombok.Getter;

public class Airplane {
    private int airplaneId;
    @Getter
    private String nameAirplane;
    @Getter
    private int allSeats;
    @Getter
    private int freeSeats;

    public Airplane(int id, String nameAirplane, int allSeats, int freeSeats) {
        this.airplaneId = id;
        this.nameAirplane = nameAirplane;
        this.allSeats = allSeats;
        this.freeSeats = freeSeats;
    }

    public Airplane(String nameAirplane, int allSeats) {
        this.nameAirplane = nameAirplane;
        this.allSeats = allSeats;
        this.freeSeats = this.allSeats;
    }



    public String reserveSeats() {
        if (freeSeats == 0) {
            return "{\"success\": false, \"message\": \"No available seats\"}";
        }
        freeSeats--;
        return "{\"success\": true, \"message\": \"Reservation successful\"}";
    }


    public void setAirplaneId(int airplaneId) {
        this.airplaneId = airplaneId;
    }

    public int getId() {
        return airplaneId;
    }

    public void setNameAirplane(String nameAirplane) {
        this.nameAirplane = nameAirplane;
    }

    public void setAllSeats(int allSeats) {
        this.allSeats = allSeats;
    }

}
