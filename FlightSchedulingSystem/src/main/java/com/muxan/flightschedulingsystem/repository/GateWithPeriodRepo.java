package com.muxan.flightschedulingsystem.repository;

import com.muxan.flightschedulingsystem.connection.DatabaseConnectionProvider;
import com.muxan.flightschedulingsystem.model.GateWithPeriod;
import com.muxan.flightschedulingsystem.payload.MyPeriod;


import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GateWithPeriodRepo {
    private static final Connection connectionGate = DatabaseConnectionProvider.getConnection();

    public GateWithPeriodRepo() {
        try {
            Statement statement = connectionGate.createStatement();
            String queryGate = "CREATE TABLE IF NOT EXISTS gateWithPeriod (\n" +
                    "    gateWithPeriodId int AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                    "    gateNumber int,\n" +
                    "    startTime TIME,\n" +
                    "    endTime TIME,\n" +
                    "    isActive int\n" +
                    ");\n";
            int affectRows = statement.executeUpdate(queryGate);
            if (affectRows != 0) {
                System.out.println("gate is created successfully");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GateWithPeriod getById(int gateWithPeriodId) {
        String queryGet = "SELECT * FROM gateWithPeriod WHERE gateWithPeriodId = ?";
        try (PreparedStatement preparedStatement = connectionGate.prepareStatement(queryGet)) {
            preparedStatement.setInt(1, gateWithPeriodId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int gateId = resultSet.getInt("gateWithPeriodId");
                int gateNumber = resultSet.getInt("gateNumber");
                Timestamp startTime = resultSet.getTimestamp("startTime");
                Timestamp endTime = resultSet.getTimestamp("endTime");
                return new GateWithPeriod(gateId, gateNumber, startTime.toLocalDateTime(), endTime.toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean setIsActive(int gateId) {
        String querySetActive = "UPDATE gateWithPeriod SET isActive = 1 WHERE gateWithPeriodId = ?";
        try (PreparedStatement preparedStatement = connectionGate.prepareStatement(querySetActive)) {
            preparedStatement.setInt(1, gateId);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean delete(int gateWithPeriodId) {

        String queryDelete = "UPDATE gateWithPeriod SET isActive = 0 WHERE gateWithPeriodId = ?";

        try (PreparedStatement preparedStatement = connectionGate.prepareStatement(queryDelete)) {
            preparedStatement.setInt(1, gateWithPeriodId);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    public GateWithPeriod save(GateWithPeriod gate) {
        String sqlSave = "INSERT INTO gateWithPeriod (gateNumber, startTime, endTime, isActive) VALUES(?,?,?,1)";
        try (PreparedStatement preparedStatement = connectionGate.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, gate.getGetNumber());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(gate.getStart()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(gate.getEnd()));

            int rowCounts = preparedStatement.executeUpdate();
            if (rowCounts == 0) {
                return null;
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    gate.setGateWithPeriodId(generatedKeys.getInt(1));
                    return gate;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateById(GateWithPeriod gateWithPeriod) {
        String sql = "UPDATE gateWithPeriod SET gateNumber = ?, startTime = ?, endTime = ? WHERE gateWithPeriodId = ?";
        try (PreparedStatement preparedStatement = connectionGate.prepareStatement(sql)) {
            preparedStatement.setInt(1, gateWithPeriod.getGetNumber());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(gateWithPeriod.getStart()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(gateWithPeriod.getEnd()));
            preparedStatement.setInt(4, gateWithPeriod.getGateWithPeriodId());

            int affectedRowCount = preparedStatement.executeUpdate();
            return affectedRowCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public LinkedHashMap<Integer, MyPeriod> getAvailableGates() {
        LinkedHashMap<Integer, MyPeriod> availableGates = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        String query = "SELECT gateWithPeriodId, startTime, endTime, isActive " +
                "FROM gateWithPeriod " +
                "WHERE isActive = 0 " +
                "AND startTime > ? " +
                "ORDER BY startTime ASC";

        try (PreparedStatement preparedStatement = connectionGate.prepareStatement(query)) {
            preparedStatement.setTime(1, Time.valueOf(currentTime.plusMinutes(30)));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int gateWithPeriodId = resultSet.getInt("gateWithPeriodId");
                LocalTime startTime = resultSet.getTime("startTime").toLocalTime();
                LocalTime endTime = resultSet.getTime("endTime").toLocalTime();
                MyPeriod period = new MyPeriod(LocalDateTime.of(today, startTime), LocalDateTime.of(today, endTime));
                availableGates.put(gateWithPeriodId, period);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return availableGates;
    }


    public void deactivateGatesIsActive() {
        String query = "UPDATE gateWithPeriod SET isActive = 0 WHERE endTime < ? AND isActive = 1";
        try (PreparedStatement preparedStatement = connectionGate.prepareStatement(query)) {
            LocalTime currentTime = LocalTime.now();
            String formattedCurrentTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            preparedStatement.setString(1, formattedCurrentTime);

            int affectedRows = preparedStatement.executeUpdate();
            System.out.println("Number of gates deactivated: " + affectedRows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
