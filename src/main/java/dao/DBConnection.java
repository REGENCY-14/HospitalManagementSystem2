package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Database Connection Manager for MySQL Hospital Management System
 * Provides connection pooling, retry logic, and connection validation
 */
public class DBConnection {
    // Database Configuration (supports environment overrides)
    private static final String DB_HOST = System.getenv().getOrDefault("HOSPITAL_DB_HOST", "localhost");
    private static final String DB_PORT = System.getenv().getOrDefault("HOSPITAL_DB_PORT", "3306");
    private static final String DB_NAME = System.getenv().getOrDefault("HOSPITAL_DB_NAME", "hospital_db");
    private static final String DB_USER = System.getenv().getOrDefault("HOSPITAL_DB_USER", "root");
    private static final String DB_PASSWORD = System.getenv().getOrDefault("HOSPITAL_DB_PASSWORD", "Hustler,14");
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    
    // Connection settings
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final int RETRY_DELAY_MS = 2000;
    
    private static Connection connection;

    /**
     * Establishes a connection to the MySQL database with retry logic
     * @return Connection object or null if connection fails
     */
    public static Connection getConnection() {
        try {
            // Check if existing connection is still valid
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            
            // Attempt to create new connection with retry logic
            connection = createConnection();
            return connection;
            
        } catch (SQLException e) {
            System.err.println("Error checking connection status: " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates a new database connection with retry logic and optimized settings
     * @return Connection object
     * @throws SQLException if connection fails after all retry attempts
     */
    private static Connection createConnection() throws SQLException {
        int attempts = 0;
        SQLException lastException = null;
        
        while (attempts < MAX_RETRY_ATTEMPTS) {
            try {
                // Load MySQL JDBC Driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Configure connection properties for optimization
                Properties props = new Properties();
                props.setProperty("user", DB_USER);
                props.setProperty("password", DB_PASSWORD);
                props.setProperty("useSSL", "false");
                props.setProperty("serverTimezone", "UTC");
                props.setProperty("autoReconnect", "true");
                props.setProperty("useUnicode", "true");
                props.setProperty("characterEncoding", "UTF-8");
                
                // Establish connection
                Connection conn = DriverManager.getConnection(DB_URL, props);
                
                System.out.println("✓ MySQL connection established");
                System.out.println("  - URL: " + DB_URL);
                System.out.println("  - User: " + DB_USER);
                
                return conn;
                
            } catch (ClassNotFoundException e) {
                System.err.println("✗ MySQL JDBC Driver not found!");
                System.err.println("  Please ensure mysql-connector-j dependency is in pom.xml");
                throw new SQLException("JDBC Driver not found", e);
                
            } catch (SQLException e) {
                lastException = e;
                attempts++;
                
                System.err.println("✗ Connection attempt " + attempts + " failed: " + e.getMessage());
                
                if (attempts < MAX_RETRY_ATTEMPTS) {
                    System.out.println("  Retrying in " + RETRY_DELAY_MS + "ms...");
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new SQLException("Connection retry interrupted", ie);
                    }
                }
            }
        }
        
        System.err.println("\n✗ Failed to connect to MySQL after " + MAX_RETRY_ATTEMPTS + " attempts");
        System.err.println("  Please check:");
        System.err.println("  1. MySQL server is running");
        System.err.println("  2. Database '" + DB_NAME + "' exists and schema applied");
        System.err.println("  3. Username and password are correct");
        System.err.println("  4. MySQL is listening on " + DB_HOST + ":" + DB_PORT);
        
        throw lastException;
    }

    /**
     * Closes the database connection gracefully
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("✓ Database connection closed successfully!");
            } catch (SQLException e) {
                System.err.println("✗ Failed to close database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Tests the database connection
     * @return true if connection is successful and valid, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                // Execute a simple query to verify connection
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT 1");
                boolean isValid = rs.next();
                rs.close();
                stmt.close();
                return isValid;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("✗ Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the database URL
     * @return Database connection URL
     */
    public static String getDatabaseUrl() {
        return DB_URL;
    }

    /**
     * Gets the database name
     * @return Database name
     */
    public static String getDatabaseName() {
        return DB_NAME;
    }

    /**
     * Validates if connection is active
     * @return true if connection is active and valid
     */
    public static boolean isConnectionActive() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
}
