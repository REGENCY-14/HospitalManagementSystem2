package org.example;

import controller.HospitalController;
import dao.DBConnection;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private HospitalController controller;

    @Override
    public void start(Stage primaryStage) {
        // Test database connection
        System.out.println("========================================");
        System.out.println("Hospital Management System - Starting...");
        System.out.println("========================================\n");
        
        // Attempt to connect to database
        if (DBConnection.testConnection()) {
            System.out.println("✓ Database connection successful!\n");
            
            // Create the GUI
            controller = new HospitalController();
            
            primaryStage.setTitle("Hospital Management System");
            primaryStage.setWidth(600);
            primaryStage.setHeight(400);
            
            VBox root = createMainLayout();
            Scene scene = new Scene(root, 600, 400);
            
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            System.out.println("✗ Failed to connect to database.");
            showErrorDialog("Database Connection Error",
                "Please ensure:\n" +
                "  1. MySQL is running\n" +
                "  2. Database 'hospital_db' exists\n" +
                "  3. Database credentials are correct in DBConnection.java\n" +
                "  4. Database schema is loaded from database/hospital_schema.sql");
        }
    }

    private VBox createMainLayout() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Hospital Management System");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");
        
        Button patientBtn = new Button("Patient Management");
        patientBtn.setPrefWidth(200);
        patientBtn.setStyle("-fx-font-size: 14;");
        patientBtn.setOnAction(e -> showPatientMenu());
        
        Button doctorBtn = new Button("Doctor Management");
        doctorBtn.setPrefWidth(200);
        doctorBtn.setStyle("-fx-font-size: 14;");
        doctorBtn.setOnAction(e -> showDoctorMenu());
        
        Button appointmentBtn = new Button("Appointment Management");
        appointmentBtn.setPrefWidth(200);
        appointmentBtn.setStyle("-fx-font-size: 14;");
        appointmentBtn.setOnAction(e -> showAppointmentMenu());
        
        Button exitBtn = new Button("Exit");
        exitBtn.setPrefWidth(200);
        exitBtn.setStyle("-fx-font-size: 14;");
        exitBtn.setOnAction(e -> System.exit(0));
        
        root.getChildren().addAll(
            titleLabel,
            new Separator(),
            patientBtn,
            doctorBtn,
            appointmentBtn,
            exitBtn
        );
        
        return root;
    }

    private void showPatientMenu() {
        showInfo("Patient Management", "Patient management features coming soon!");
    }

    private void showDoctorMenu() {
        showInfo("Doctor Management", "Doctor management features coming soon!");
    }

    private void showAppointmentMenu() {
        showInfo("Appointment Management", "Appointment management features coming soon!");
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
