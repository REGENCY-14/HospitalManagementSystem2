package dao;

import java.sql.*;

public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Modify as per your MySQL password

    private static Connection connection;

    /**
     * Establishes a connection to the database
     * @return Connection object
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Database connection established successfully!");
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("Failed to establish database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Closes the database connection
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed successfully!");
            } catch (SQLException e) {
                System.err.println("Failed to close database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Tests the database connection
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Error testing database connection: " + e.getMessage());
            return false;
        }
    }
}
