package org.example;

import controller.AppointmentTabController;
import controller.DashboardController;
import controller.DepartmentTabController;
import controller.DoctorTabController;
import controller.MedicalInventoryTabController;
import controller.MedicalLogController;
import controller.PatientFeedbackTabController;
import controller.PatientTabController;
import controller.PrescriptionTabController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Main Application Entry Point - Refactored to follow Single Responsibility Principle
 * Each tab is now managed by its own controller class
 */
public class Main extends Application {

    private PatientTabController patientController;
    private DoctorTabController doctorController;
    private AppointmentTabController appointmentController;
    private DepartmentTabController departmentController;
    private PrescriptionTabController prescriptionController;
    private PatientFeedbackTabController feedbackController;
    private MedicalInventoryTabController inventoryController;
    private DashboardController dashboardController;

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
        appointmentController = new AppointmentTabController();
        departmentController = new DepartmentTabController();
        prescriptionController = new PrescriptionTabController();
        feedbackController = new PatientFeedbackTabController();
        inventoryController = new MedicalInventoryTabController();
        dashboardController = new DashboardController();
        MedicalLogController medicalLogController = new MedicalLogController(primaryStage);

        // Add tabs (delegated to controller classes)
        tabPane.getTabs().addAll(
            dashboardController.createDashboardTab(),
            patientController.createPatientTab(),
            doctorController.createDoctorTab(),
            appointmentController.createAppointmentTab(),
            departmentController.createDepartmentTab(),
            prescriptionController.createPrescriptionTab(),
            feedbackController.createPatientFeedbackTab(),
            inventoryController.createMedicalInventoryTab(),
            medicalLogController.createMedicalLogTab()
        );

        // Select Dashboard tab by default
        tabPane.getSelectionModel().select(0);

        Scene scene = new Scene(tabPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
