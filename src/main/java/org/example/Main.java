package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import controller.MedicalLogController;
import controller.PatientTabController;
import controller.DoctorTabController;

/**
 * Main Application Entry Point - Refactored to follow Single Responsibility Principle
 * Each tab is now managed by its own controller class
 */
public class Main extends Application {

    private PatientTabController patientController;
    private DoctorTabController doctorController;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("========================================");
        System.out.println("Hospital Management System - Starting...");
        System.out.println("========================================\n");
        System.out.println("✓ Application started\n");

        primaryStage.setTitle("Hospital Management System");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Initialize controllers
        patientController = new PatientTabController();
        doctorController = new DoctorTabController();
        MedicalLogController medicalLogController = new MedicalLogController(primaryStage);

        // Add tabs (delegated to controller classes)
        tabPane.getTabs().addAll(
            patientController.createPatientTab(),
            doctorController.createDoctorTab(),
            medicalLogController.createMedicalLogTab()
            // TODO: Migrate remaining tabs to separate controller classes:
            // - AppointmentTabController
            // - DepartmentTabController
            // - PrescriptionTabController
            // - PatientFeedbackTabController
            // - MedicalInventoryTabController
        );

        // Select Patient tab by default
        tabPane.getSelectionModel().select(0);

        Scene scene = new Scene(tabPane);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Auto-load all data when application starts
        loadAllData();
    }
    
    /**
     * Load all data into table views
     */
    private void loadAllData() {
        System.out.println("Loading all data into views...");
        patientController.loadPatientData();
        doctorController.loadDoctorData();
        System.out.println("✓ All data loaded\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
