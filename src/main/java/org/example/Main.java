package org.example;

import controller.HospitalController;
import dao.DBConnection;

public class Main {
    public static void main(String[] args) {
        // Test database connection
        System.out.println("========================================");
        System.out.println("Hospital Management System - Starting...");
        System.out.println("========================================\n");
        
        // Attempt to connect to database
        if (DBConnection.testConnection()) {
            System.out.println("✓ Database connection successful!\n");
            
            // Start the application
            HospitalController controller = new HospitalController();
            controller.displayMainMenu();
        } else {
            System.out.println("✗ Failed to connect to database.");
            System.out.println("Please ensure:");
            System.out.println("  1. MySQL is running");
            System.out.println("  2. Database 'hospital_db' exists");
            System.out.println("  3. Database credentials are correct in DBConnection.java");
            System.out.println("  4. Database schema is loaded from database/hospital_schema.sql");
        }
    }
}
