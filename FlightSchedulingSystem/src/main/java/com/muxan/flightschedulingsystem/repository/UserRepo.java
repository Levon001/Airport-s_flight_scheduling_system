package com.muxan.flightschedulingsystem.repository;

import com.muxan.flightschedulingsystem.connection.DatabaseConnectionProvider;
import com.muxan.flightschedulingsystem.model.Airplane;
import com.muxan.flightschedulingsystem.model.User;

import java.sql.*;

public class UserRepo {
    public static Connection connection = DatabaseConnectionProvider.getConnection();

    public UserRepo() {
        try {
            Statement statement = connection.createStatement();
            String queryUser = "CREATE TABLE IF NOT EXISTS user (\n" +
                    "    userId int AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                    "    name varchar(255) NOT NULL,\n" +
                    "    surname varchar(255) NOT NULL,\n" +
                    "    email varchar(255) NOT NULL,\n"+
                    "    password varchar(255) NOT NULL,\n"+
                    "    airplaneId int,\n" +
                    "    isActive int,\n"+
                    "    FOREIGN KEY (airplaneId) REFERENCES airplaneList(airplaneId)\n" +
                    ");\n";
            int affectedRowsUser = statement.executeUpdate(queryUser);
            if(affectedRowsUser != 0) {
                System.out.println("user tables is created successfully");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public User getByEmailAndPassword(String email, String password) {
        String sqlQuery = "SELECT * FROM user WHERE email = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
               return getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public User getByEmail(String email) {
        String sqlQuery = "SELECT * FROM user WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User save(User user) {
        String sqlInsert = "INSERT INTO user (name, surname, email, password, airplaneId, isActive) VALUES(?,?,?,?,?,1)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            Airplane airplane = user.getAirplane();
            if(airplane != null) {
                preparedStatement.setInt(5, airplane.getId());
            } else {
                preparedStatement.setString(5,null);
            }

            int rowInserted = preparedStatement.executeUpdate();

            if (rowInserted == 0) {
                return null;
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    user.setId(generatedId);
                    return user;
                } else {
                    throw new SQLException("No ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        int airplaneId = resultSet.getInt("airplaneId");
        return User.builder().
                id(resultSet.getInt("userid"))
                .name(resultSet.getString("name"))
                .surname(resultSet.getString("surname"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .airplane(new AirplaneRepo().getById(airplaneId))
                .build();
    }

}
