package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Standalone test class to verify MySQL database connection
 * Run this to test if your database is properly configured
 */
public class TestConnection {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("MySQL Database Connection Test");
        System.out.println("========================================\n");
        
        System.out.println("Testing connection to: " + DBConnection.getDatabaseUrl());
        System.out.println("Database name: " + DBConnection.getDatabaseName());
        System.out.println();
        
        // Test 1: Basic Connection Test
        System.out.println("Test 1: Basic Connection Test");
        System.out.println("------------------------------");
        if (DBConnection.testConnection()) {
            System.out.println("✓ Connection test PASSED");
        } else {
            System.out.println("✗ Connection test FAILED");
            System.out.println("\nPlease check:");
            System.out.println("  1. MySQL server is running");
            System.out.println("  2. Database 'hospital_db' exists");
            System.out.println("  3. Username and password in DBConnection.java are correct");
            return;
        }
        
        System.out.println();
        
        // Test 2: Connection State Test
        System.out.println("Test 2: Connection State Test");
        System.out.println("------------------------------");
        if (DBConnection.isConnectionActive()) {
            System.out.println("✓ Connection is active and valid");
        } else {
            System.out.println("✗ Connection is not active");
        }
        
        System.out.println();
        
        // Test 3: Query Execution Test
        System.out.println("Test 3: Query Execution Test");
        System.out.println("------------------------------");
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                Statement stmt = conn.createStatement();
                
                // Test database version
                ResultSet rs = stmt.executeQuery("SELECT VERSION() as version");
                if (rs.next()) {
                    System.out.println("✓ MySQL Version: " + rs.getString("version"));
                }
                rs.close();
                
                // Test current database
                rs = stmt.executeQuery("SELECT DATABASE() as db");
                if (rs.next()) {
                    System.out.println("✓ Current Database: " + rs.getString("db"));
                }
                rs.close();
                
                // Test current user
                rs = stmt.executeQuery("SELECT USER() as user");
                if (rs.next()) {
                    System.out.println("✓ Current User: " + rs.getString("user"));
                }
                rs.close();
                
                // Test tables in database
                rs = stmt.executeQuery("SHOW TABLES");
                System.out.println("\n✓ Tables in database:");
                int tableCount = 0;
                while (rs.next()) {
                    tableCount++;
                    System.out.println("  " + tableCount + ". " + rs.getString(1));
                }
                if (tableCount == 0) {
                    System.out.println("  (No tables found - you may need to run hospital_schema.sql)");
                }
                rs.close();
                stmt.close();
                
                System.out.println("\n✓ Query execution test PASSED");
            } else {
                System.out.println("✗ Could not get connection");
            }
        } catch (Exception e) {
            System.out.println("✗ Query execution test FAILED");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
        
        // Test 4: Connection Pool Test
        System.out.println("Test 4: Connection Reuse Test");
        System.out.println("------------------------------");
        Connection conn1 = DBConnection.getConnection();
        Connection conn2 = DBConnection.getConnection();
        if (conn1 == conn2) {
            System.out.println("✓ Connection pooling working (same instance reused)");
        } else {
            System.out.println("✗ New connection created instead of reusing");
        }
        
        System.out.println();
        
        // Final Summary
        System.out.println("========================================");
        System.out.println("Test Summary");
        System.out.println("========================================");
        System.out.println("✓ All connection tests completed!");
        System.out.println("\nYou can now use DBConnection in your application:");
        System.out.println("  Connection conn = DBConnection.getConnection();");
        System.out.println();
        
        // Clean up
        DBConnection.closeConnection();
        System.out.println("Connection closed.");
    }
}
