package com.muxan.flightschedulingsystem.model;

import java.time.LocalTime;

public class RunwayWithPeriod {
    private int runwayWithPeriodId;
    private int runwayNumber;
    private LocalTime startTime;
    private LocalTime endTime;

    public RunwayWithPeriod(int runwayWithPeriodId, int runwayNumber, LocalTime start, LocalTime end) {
        this.runwayWithPeriodId = runwayWithPeriodId;
        this.runwayNumber = runwayNumber;
        this.startTime = start;
        this.endTime = end;
    }

    public RunwayWithPeriod(int runwayNumber, LocalTime start, LocalTime end) {
        this.runwayNumber = runwayNumber;
        this.startTime = start;
        this.endTime = end;
    }

    public int getRunwayWithPeriodId() {
        return runwayWithPeriodId;
    }


    public LocalTime getStart() {
        return startTime;
    }

    public LocalTime getEnd() {
        return endTime;
    }

}
