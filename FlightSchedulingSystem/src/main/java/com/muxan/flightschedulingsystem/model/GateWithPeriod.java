package com.muxan.flightschedulingsystem.model;

import java.time.LocalDateTime;

public class GateWithPeriod {
     private int gateWithPeriodId;
     private int gateNumber;
     private LocalDateTime startTime;
     private LocalDateTime endTime;

     public GateWithPeriod(int gateWithPeriodId, int getNumber, LocalDateTime start, LocalDateTime end) {
          this.gateWithPeriodId = gateWithPeriodId;
          this.gateNumber = getNumber;
          this.startTime = start;
          this.endTime = end;
     }

     public GateWithPeriod(int getNumber, LocalDateTime start, LocalDateTime end) {
          this.gateNumber = getNumber;
          this.startTime = start;
          this.endTime = end;
     }

     public int getGateWithPeriodId() {
          return gateWithPeriodId;
     }

     public int getGetNumber() {
          return gateNumber;
     }

     public LocalDateTime getStart() {
          return startTime;
     }

     public LocalDateTime getEnd() {
          return endTime;
     }

     public void setGateWithPeriodId(int gateWithPeriodId) {
          this.gateWithPeriodId = gateWithPeriodId;
     }


}
