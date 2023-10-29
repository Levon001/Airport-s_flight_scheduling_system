package com.muxan.flightschedulingsystem.connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionProvider {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/smart";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";
    private static final Object lock = new Object();
    private static Connection connection;

    // Static block to load the JDBC driver only once
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load the MySQL JDBC driver.");
        }
    }

    private DatabaseConnectionProvider() {}

    public static Connection getConnection()  {
        synchronized (lock) {
            try {
                if (connection == null || connection.isClosed()) {
                    connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return connection;
        }
    }

    // Method to close a database connection
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}