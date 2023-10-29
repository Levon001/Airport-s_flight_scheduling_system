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


    public List<Flight> getAll() {
        List<Flight> flights = new ArrayList<>();
        AirplaneRepo airplaneRepo = new AirplaneRepo();
        String query = "SELECT flightId, direction, country, airplaneId, runwayWithPeriodId, gateWithPeriodId FROM flight";

        try (Statement statement = connectionFlight.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Airplane airplane = airplaneRepo.getById(resultSet.getInt("airplaneId"));

                Flight flight = new Flight(
                        resultSet.getInt("flightId"),
                        resultSet.getInt("direction"),
                        resultSet.getString("country"),
                        airplane
                );

                flights.add(flight);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return flights;
    }


    public List<Flight> getFlightsByDirection(int direction) {
        if (direction != 0 && direction != 1) {
            throw new IllegalArgumentException("Direction must be 0 or 1");
        }

        List<Flight> flights = new ArrayList<>();
        AirplaneRepo airplaneRepo = new AirplaneRepo();

        String query = "SELECT flightId, direction, country, airplaneId, runwayWithPeriodId, gateWithPeriodId FROM flight WHERE direction = ?";

        try (PreparedStatement statement = connectionFlight.prepareStatement(query)) {
            statement.setInt(1, direction);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Airplane airplane = airplaneRepo.getById(resultSet.getInt("airplaneId"));

                    Flight flight = new Flight(
                            resultSet.getInt("flightId"),
                            resultSet.getInt("direction"),
                            resultSet.getString("country"),
                            airplane
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

    public Flight getById(int flightId) {
        String sqlQuery = "SELECT * FROM flight WHERE flightId = ?";

        try (PreparedStatement preparedStatement = connectionFlight.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, flightId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                int direction = resultSet.getInt("direction");  // Assuming direction is stored as a single character string in the DB
                String country = resultSet.getString("country");
                int airplaneId = resultSet.getInt("airplaneId");
                int runwayWithPeriodId = resultSet.getInt("runwayWithPeriodId");
                int gateWithPeriodId = resultSet.getInt("gateWithPeriodId");


                Airplane airplane = new AirplaneRepo().getById(airplaneId);
                GateWithPeriod gateWithPeriod = new GateWithPeriodRepo().getById(gateWithPeriodId);
                RunwayWithPeriod runwayWithPeriod = new RunwayRepo().getById(runwayWithPeriodId);

                // Constructing and returning the Flight object
                return new Flight(id, direction, country, airplane, runwayWithPeriod, gateWithPeriod);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
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

                    if (startTime != null && startTime.compareTo(LocalTime.now().toString()) < 0) {
                        updateFlightStatusToInactive(connectionFlight, flightId);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

                    if (startTime != null && startTime.compareTo(getComparableTime()) < 0) {
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

    private String getComparableTime() {
        return java.time.LocalTime.now().minusHours(1).toString();
    }

    public List<Flight> getFlightsByStatus(int status) {
        AirplaneRepo airplaneRepo = new AirplaneRepo();
        GateWithPeriodRepo gate = new GateWithPeriodRepo();
        RunwayRepo runway = new RunwayRepo();
        List<Flight> flights = new ArrayList<>();
        String queryIsActive = "SELECT * FROM flight WHERE isActive = ?";

        try (PreparedStatement statement = connectionFlight.prepareStatement(queryIsActive)) {

            statement.setInt(1, status);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int flightId = resultSet.getInt("flightId");
                    int direction = resultSet.getInt("direction");
                    String country = resultSet.getString("country");

                    Airplane airplane = airplaneRepo.getById(resultSet.getInt("airplaneId"));
                    RunwayWithPeriod runwayWithPeriod = runway.getById(resultSet.getInt("runwayWithPeriodId"));
                    GateWithPeriod gateWithPeriod = gate.getById(resultSet.getInt("gateWithPeriodId"));
                    Flight flight = new Flight(flightId, direction, country, airplane, runwayWithPeriod, gateWithPeriod);

                    flights.add(flight);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
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

