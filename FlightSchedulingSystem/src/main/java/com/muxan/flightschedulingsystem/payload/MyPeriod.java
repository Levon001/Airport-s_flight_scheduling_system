package com.muxan.flightschedulingsystem.payload;

import java.time.LocalDateTime;

public class MyPeriod implements Comparable<MyPeriod> {

    private LocalDateTime start;
    private LocalDateTime end;

    public MyPeriod(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    @Override
    public int compareTo(MyPeriod other) {
        return this.end.compareTo(other.end);
    }

    @Override
    public String toString() {
        return "MyPeriod{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
