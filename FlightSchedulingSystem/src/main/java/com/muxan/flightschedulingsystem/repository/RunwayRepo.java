package com.muxan.flightschedulingsystem.repository;

import com.muxan.flightschedulingsystem.connection.DatabaseConnectionProvider;
import com.muxan.flightschedulingsystem.model.RunwayWithPeriod;
import com.muxan.flightschedulingsystem.payload.MyPeriod;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class RunwayRepo {
    private static final Connection connectionRunway = DatabaseConnectionProvider.getConnection();

    public RunwayRepo() {

        try {
            Statement statement = connectionRunway.createStatement();
            String queryRunway = "CREATE TABLE IF NOT EXISTS runwayWithPeriod (\n" +
                    "    runwayWithPeriodId int AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                    "    runwayNumber int,\n" +
                    "    startTime TIME,\n" +
                    "    endTime TIME,\n" +
                    "    isActive int\n" +
                    ");\n";
            int affectRows = statement.executeUpdate(queryRunway);

            if(affectRows != 0 ) {
                System.out.println("runway is created sucessfuly");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public RunwayWithPeriod getById(int runwayWithPeriodId) {
        String queryGet = "SELECT * FROM runwayWithPeriod WHERE runwayWithPeriodId = ?";
        try (PreparedStatement preparedStatement = connectionRunway.prepareStatement(queryGet)) {
            preparedStatement.setInt(1, runwayWithPeriodId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int runWayId = resultSet.getInt(1);
                int runWayNumber = resultSet.getInt(2);
                LocalTime runWayStart = resultSet.getTime(3).toLocalTime();
                LocalTime runWayEnd = resultSet.getTime(4).toLocalTime();
                return new RunwayWithPeriod(runWayId, runWayNumber, runWayStart, runWayEnd);
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception RunWayRepo during getting :" + e.getMessage());
        }
        return null;
    }

    public boolean setActive(int runwayWithPeriodId) {
        String updateSql = "UPDATE runwayWithPeriod SET isActive = 1 WHERE runwayWithPeriodId = ?";

        try (PreparedStatement preparedStatement = connectionRunway.prepareStatement(updateSql)) {
            preparedStatement.setInt(1, runwayWithPeriodId);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception in RunWayRepo during deleting: " + e.getMessage());
            return false;
        }
        return true;
    }


    public boolean delete(int runwayWithPeriodId) {

        String queryDelete = "UPDATE runwayWithPeriod SET isActive = 0 WHERE runwayWithPeriodId = ?";

        try (PreparedStatement preparedStatement = connectionRunway.prepareStatement(queryDelete)) {
            preparedStatement.setInt(1, runwayWithPeriodId);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception in RunWayRepo during deleting: " + e.getMessage());
            return false;
        }
        return true;
    }


    public LinkedHashMap<Integer, MyPeriod> getAvailableRunways() {
        LinkedHashMap<Integer, MyPeriod> availableRunways = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        String query = "SELECT runwayWithPeriodId, startTime, endTime, isActive " +
                "FROM runwayWithPeriod " +
                "WHERE isActive = 0 " +
                "AND startTime > ? " +
                "ORDER BY startTime ASC";

        try (PreparedStatement preparedStatement = connectionRunway .prepareStatement(query)) {
            preparedStatement.setTime(1, Time.valueOf(currentTime.plusMinutes(30)));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int runwayWithPeriodId = resultSet.getInt("runwayWithPeriodId");
                LocalTime startTime = resultSet.getTime("startTime").toLocalTime();
                LocalTime endTime = resultSet.getTime("endTime").toLocalTime();
                MyPeriod period = new MyPeriod(LocalDateTime.of(today, startTime), LocalDateTime.of(today, endTime));
                availableRunways.put(runwayWithPeriodId, period);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return availableRunways;
    }


    public void deactivateRunwaysPastEndTime() {
        String query = "UPDATE runwayWithPeriod SET isActive = 0 WHERE endTime < ? AND isActive = 1";
        try (PreparedStatement preparedStatement = connectionRunway.prepareStatement(query)) {
            // Get current time and format it to HH:mm:ss format
            LocalTime currentTime = LocalTime.now();
            String formattedCurrentTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            preparedStatement.setString(1, formattedCurrentTime);

            int affectedRows = preparedStatement.executeUpdate();
            System.out.println("Number of runways deactivated: " + affectedRows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
