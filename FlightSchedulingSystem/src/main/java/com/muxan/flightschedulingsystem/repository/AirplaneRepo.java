package com.muxan.flightschedulingsystem.repository;

import com.muxan.flightschedulingsystem.connection.DatabaseConnectionProvider;
import com.muxan.flightschedulingsystem.model.Airplane;

import java.sql.*;

public class AirplaneRepo {
    private static final Connection connection = DatabaseConnectionProvider.getConnection();

    public AirplaneRepo() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS airplaneList (\n" +
                    "    airplaneId int AUTO_INCREMENT PRIMARY KEY,\n" +
                    "    nameAirplane varchar(255),\n"+
                    "    allSeats int,\n" +
                    "    freeSeats int\n" +
                    ");\n";
            int affectRows = statement.executeUpdate(query);
            if(affectRows != 0) {
                System.out.println("airplane tables is created successfully");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateByFreeSeats(int airplaneId) {
        String query = "UPDATE airplaneList SET freeSeats = freeSeats - 1 WHERE airplaneId = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, airplaneId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating freeSeats", e);
        }
    }

    public Airplane getById(int airplaneId) {
        String sql = "SELECT * FROM airplaneList WHERE airplaneId=?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,airplaneId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                int id = resultSet.getInt("airplaneId");
                String name = resultSet.getString("nameAirplane");
                int freeSeats = resultSet.getInt("freeSeats");
                int allSeats = resultSet.getInt("allSeats");
                return new Airplane(id,name,allSeats,freeSeats);
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception in AirplaneRepo during getting: " + e.getMessage());
        }
        return null;
    }

    public Airplane save(Airplane airplane) {
        String sql = "INSERT INTO airplaneList (nameAirplane, allSeats, freeSeats) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1,airplane.getNameAirplane());
            preparedStatement.setInt(2,airplane.getAllSeats());
            preparedStatement.setInt(3,airplane.getFreeSeats());

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted == 0) {
                throw new SQLException("Insertion failed, no rows affected.");
            }

            // Retrieve the generated keys
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    airplane.setAirplaneId(generatedId);
                    return airplane;
                } else {
                    throw new SQLException("No ID obtained.");
                }
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception in AirplaneRepo during saving:" + e.getMessage());
            return null;
        }
    }

}
