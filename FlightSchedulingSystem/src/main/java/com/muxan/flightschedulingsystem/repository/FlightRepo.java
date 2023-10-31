package com.muxan.flightschedulingsystem.repository;

import com.muxan.flightschedulingsystem.connection.DatabaseConnectionProvider;
import com.muxan.flightschedulingsystem.model.Airplane;
import com.muxan.flightschedulingsystem.model.Flight;
import com.muxan.flightschedulingsystem.model.GateWithPeriod;
import com.muxan.flightschedulingsystem.model.RunwayWithPeriod;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class FlightRepo {
    private static final Connection connectionFlight = DatabaseConnectionProvider.getConnection();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final AirplaneRepo airplaneRepo = new AirplaneRepo();
    private final GateWithPeriodRepo gateWithPeriodRepo = new GateWithPeriodRepo();
    private final RunwayRepo runwayRepo = new RunwayRepo();


    public FlightRepo() {
        try {
            Statement statement = connectionFlight.createStatement();
            String queryFlight = "CREATE TABLE IF NOT EXISTS flight (\n" +
                    // Schedule the task to run every 5 minutes (adjust as needed)
                    "    flightId int AUTO_INCREMENT PRIMARY KEY,\n" +
                    "    direction int,\n" +
                    "    country varchar(255),\n" +
                    "    airplaneId int,\n" +
                    "    runwayWithPeriodId int,\n" +
                    "    gateWithPeriodId int,\n" +
                    "    isActive int,\n" +
                    "    FOREIGN KEY (airplaneId) REFERENCES airplaneList(airplaneId),\n" +
                    "    FOREIGN KEY (gateWithPeriodId) REFERENCES gateWithPeriod(gateWithPeriodId),\n" +
                    "    FOREIGN KEY (runwayWithPeriodId) REFERENCES runwayWithPeriod(runwayWithPeriodId)\n" +
                    ");\n";
            int affectedRowsFlight = statement.executeUpdate(queryFlight);
            if (affectedRowsFlight != 0) {
                System.out.println("flight tables is created successfully");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public List<Flight> getFlightsByDirection(int direction) {
        if (direction != 0 && direction != 1) {
            throw new IllegalArgumentException("Direction must be 0 or 1");
        }

        List<Flight> flights = new ArrayList<>();

        String query = "SELECT flightId, direction, country, airplaneId, runwayWithPeriodId, gateWithPeriodId FROM flight WHERE direction = ?";

        try (PreparedStatement statement = connectionFlight.prepareStatement(query)) {
            statement.setInt(1, direction);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Airplane airplane = airplaneRepo.getById(resultSet.getInt("airplaneId"));
                    GateWithPeriod gate = gateWithPeriodRepo.getById(resultSet.getInt("gateWithPeriodId"));
                    RunwayWithPeriod runway = runwayRepo.getById(resultSet.getInt("runwayWithPeriodId"));

                    Flight flight = new Flight(
                            resultSet.getInt("flightId"),
                            resultSet.getInt("direction"),
                            resultSet.getString("country"),
                            airplane,
                            runway,
                            gate
                    );

                    flights.add(flight);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return flights;
    }


    public Flight save(Flight flight) {
        String sqlInsert = "INSERT INTO flight (direction, country, airplaneId, runwayWithPeriodId, gateWithPeriodId, isActive) VALUES(?,?,?,?,?,1)";
        try (PreparedStatement preparedStatement = connectionFlight.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, flight.getDirection());
            preparedStatement.setString(2, flight.getCountry());
            preparedStatement.setInt(3, flight.getAirplane().getId());
            preparedStatement.setInt(4, flight.getRunwayWithPeriod().getRunwayWithPeriodId());
            preparedStatement.setInt(5, flight.getGateWithPeriod().getGateWithPeriodId());

            int rowInserted = preparedStatement.executeUpdate();

            if (rowInserted == 0) {
                return null;
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1); // Assuming the ID is an integer
                    flight.setFlightId(generatedId);
                    return flight;
                } else {
                    throw new SQLException("No ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void updateDepartureFlightStatus() {
        String query = "SELECT flightId, runwayWithPeriod.startTime " +
                "FROM flight " +
                "JOIN runwayWithPeriod ON flight.runwayWithPeriodId = runwayWithPeriod.runwayWithPeriodId " +
                "WHERE flight.direction = 0 AND flight.isActive = 1";

        try (PreparedStatement statement = connectionFlight.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int flightId = resultSet.getInt("flightId");
                    String startTime = resultSet.getString("startTime");

                    if (startTime != null && startTime.compareTo(getComparableTime()) < 0) {
                        updateFlightStatusToInactive(connectionFlight, flightId);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getComparableTime() {
        return java.time.LocalTime.now().toString();
    }

    public void updateArrivedFlightStatus() {
        String query = "SELECT flightId, gateWithPeriod.startTime " +
                "FROM flight " +
                "JOIN gateWithPeriod ON flight.gateWithPeriodId = gateWithPeriod.gateWithPeriodId " +
                "WHERE flight.direction = 1 AND flight.isActive = 1";

        try (PreparedStatement statement = connectionFlight.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int flightId = resultSet.getInt("flightId");
                    String startTime = resultSet.getString("startTime");

                    if (startTime != null && startTime.compareTo(LocalTime.now().plusHours(1).toString()) < 0) {
                        updateFlightStatusToInactive(connectionFlight, flightId);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateFlightStatusToInactive(Connection connection, int flightId) throws SQLException {
        String updateQuery = "UPDATE flight SET isActive = 0 WHERE flightId = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setInt(1, flightId);
            updateStatement.executeUpdate();
        }
    }

   
    public int getIsActive(int flightId) {
        int isActive = 0;

        String queryIsActive = "SELECT isActive FROM flight WHERE flightId = ?";

        try (PreparedStatement preparedStatement = connectionFlight.prepareStatement(queryIsActive)) {

            preparedStatement.setInt(1, flightId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isActive = resultSet.getInt("isActive");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isActive;
    }


}

