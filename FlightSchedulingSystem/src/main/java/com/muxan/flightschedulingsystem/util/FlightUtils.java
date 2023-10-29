package com.muxan.flightschedulingsystem.util;

public class FlightUtils {
    public static int takeOutNumberGateOrRunway(int a) {
        if (a > 0 && a < 49) {
            return 1;
        } else if (a > 48 && a < 97) {
            return 2;
        }
        return 3;
    }
}
