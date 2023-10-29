package com.muxan.flightschedulingsystem.service;

import com.muxan.flightschedulingsystem.payload.MyPeriod;
import com.muxan.flightschedulingsystem.repository.GateWithPeriodRepo;
import com.muxan.flightschedulingsystem.repository.RunwayRepo;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashMap;
import java.util.Map;

public class FlyService {

    RunwayRepo repo = new RunwayRepo();
    GateWithPeriodRepo gate = new GateWithPeriodRepo();
    public LinkedHashMap<Pair<Integer,Integer>, MyPeriod> createGateRunwayPair(int direction) {
        LinkedHashMap<Integer, MyPeriod> availableGates = gate.getAvailableGates();
        LinkedHashMap<Integer, MyPeriod> availableRunways = repo.getAvailableRunways();
        LinkedHashMap<Pair<Integer,Integer>, MyPeriod> gateRunwayPairs = new LinkedHashMap<>();

        if (direction == 1) {
            for (Map.Entry<Integer, MyPeriod> gateEntry : availableGates.entrySet()) {
                for (Map.Entry<Integer, MyPeriod> runwayEntry : availableRunways.entrySet()) {
                    if (gateEntry.getValue().getEnd().toLocalTime().equals(runwayEntry.getValue().getStart().toLocalTime())) {
                        // Create a Pair for the gate ID and runway ID and add to the map
                        MyPeriod newPeriod = new MyPeriod(gateEntry.getValue().getStart(), runwayEntry.getValue().getEnd());
                        Pair<Integer, Integer> pair = ImmutablePair.of(gateEntry.getKey(), runwayEntry.getKey());
                        gateRunwayPairs.put(pair, newPeriod);
                    }
                }
            }
        }

            if(direction == 0) {
                for (Map.Entry<Integer, MyPeriod>  runwayEntry: availableRunways.entrySet()) {
                    for (Map.Entry<Integer, MyPeriod> gateEntry : availableGates.entrySet()) {
                        if (runwayEntry.getValue().getEnd().toLocalTime().equals(gateEntry.getValue().getStart().toLocalTime())) {
                            // Create a Pair for the gate ID and runway ID and add to the map
                            MyPeriod newPeriod = new MyPeriod(runwayEntry.getValue().getStart(), gateEntry.getValue().getEnd());
                            Pair<Integer, Integer> pair = ImmutablePair.of(runwayEntry.getKey(), gateEntry.getKey());
                            gateRunwayPairs.put(pair, newPeriod);
                        }
                    }
                }
            }

        return gateRunwayPairs;
    }
}
