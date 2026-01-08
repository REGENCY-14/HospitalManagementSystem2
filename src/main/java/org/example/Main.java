package org.example;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Appointment;
import model.Doctor;
import model.Patient;
import service.AppointmentService;
import service.DoctorService;
import service.PatientService;

public class Main extends Application {

    private TabPane tabPane;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("========================================");
        System.out.println("Hospital Management System - Starting...");
        System.out.println("========================================\n");
        System.out.println("✓ Application started (database connection skipped for demo)\n");

        primaryStage.setTitle("Hospital Management System");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().addAll(
            createPatientTab(),
            createDoctorTab(),
            createAppointmentTab(),
            createDashboardTab()
        );

        Scene scene = new Scene(tabPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createPatientTab() {
        Tab tab = new Tab("Patient Management");
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Patient Management");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // Input fields
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");

        DatePicker dobPicker = new DatePicker();
        dobPicker.setPromptText("Date of Birth");

        // Buttons
        Button addBtn = new Button("Add Patient");
        addBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        addBtn.setOnAction(e -> {
            if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
                showAlert("Error", "Please fill in all required fields");
                return;
            }
            try {
                LocalDate dob = dobPicker.getValue();
                Patient patient = new Patient(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    dob,
                    "Not Specified",
                    phoneField.getText(),
                    "",
                    ""
                );
                if (PatientService.createPatient(patient)) {
                    showAlert("Success", "Patient added successfully!");
                    firstNameField.clear();
                    lastNameField.clear();
                    emailField.clear();
                    phoneField.clear();
                    dobPicker.setValue(null);
                } else {
                    showAlert("Error", "Failed to add patient");
                }
            } catch (Exception ex) {
                showAlert("Error", "Invalid input: " + ex.getMessage());
            }
        });

        // Patient List
        ListView<String> patientListView = new ListView<>();
        Button refreshBtn = new Button("Refresh List");
        refreshBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshBtn.setOnAction(e -> {
            patientListView.getItems().clear();
            List<Patient> patients = PatientService.getAllPatients();
            if (patients != null) {
                for (Patient p : patients) {
                    patientListView.getItems().add("ID: " + p.getPatientId() + " - " + 
                        p.getFirstName() + " " + p.getLastName() + " (" + p.getPhone() + ")");
                }
            }
        });

        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(firstNameField, lastNameField, emailField, phoneField, dobPicker, addBtn);

        root.getChildren().addAll(
            titleLabel,
            new Separator(),
            new Label("Add New Patient:"),
            inputBox,
            new Separator(),
            new Label("Patient List:"),
            refreshBtn,
            patientListView
        );

        tab.setContent(root);
        return tab;
    }

    private Tab createDoctorTab() {
        Tab tab = new Tab("Doctor Management");
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Doctor Management");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // Input fields
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");

        TextField departmentField = new TextField();
        departmentField.setPromptText("Department");

        TextField specializationField = new TextField();
        specializationField.setPromptText("Specialization");

        // Buttons
        Button addBtn = new Button("Add Doctor");
        addBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        addBtn.setOnAction(e -> {
            if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
                showAlert("Error", "Please fill in all required fields");
                return;
            }
            try {
                Doctor doctor = new Doctor(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    specializationField.getText(),
                    phoneField.getText(),
                    1  // Default department ID
                );
                if (DoctorService.createDoctor(doctor)) {
                    showAlert("Success", "Doctor added successfully!");
                    firstNameField.clear();
                    lastNameField.clear();
                    emailField.clear();
                    phoneField.clear();
                    departmentField.clear();
                    specializationField.clear();
                } else {
                    showAlert("Error", "Failed to add doctor");
                }
            } catch (Exception ex) {
                showAlert("Error", "Invalid input: " + ex.getMessage());
            }
        });

        // Doctor List
        ListView<String> doctorListView = new ListView<>();
        Button refreshBtn = new Button("Refresh List");
        refreshBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshBtn.setOnAction(e -> {
            doctorListView.getItems().clear();
            List<Doctor> doctors = DoctorService.getAllDoctors();
            if (doctors != null) {
                for (Doctor d : doctors) {
                    doctorListView.getItems().add("ID: " + d.getDoctorId() + " - " + 
                        d.getFirstName() + " " + d.getLastName() + " (" + d.getSpecialization() + ")");
                }
            }
        });

        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(firstNameField, lastNameField, emailField, phoneField, departmentField, specializationField, addBtn);

        root.getChildren().addAll(
            titleLabel,
            new Separator(),
            new Label("Add New Doctor:"),
            inputBox,
            new Separator(),
            new Label("Doctor List:"),
            refreshBtn,
            doctorListView
        );

        tab.setContent(root);
        return tab;
    }

    private Tab createAppointmentTab() {
        Tab tab = new Tab("Appointment Management");
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Appointment Management");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // Input fields
        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Patient ID");

        TextField doctorIdField = new TextField();
        doctorIdField.setPromptText("Doctor ID");

        DatePicker appointmentDatePicker = new DatePicker();
        appointmentDatePicker.setPromptText("Appointment Date");

        TextField timeField = new TextField();
        timeField.setPromptText("Time (HH:MM)");

        TextField reasonField = new TextField();
        reasonField.setPromptText("Reason for Visit");

        // Buttons
        Button scheduleBtn = new Button("Schedule Appointment");
        scheduleBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        scheduleBtn.setOnAction(e -> {
            if (patientIdField.getText().isEmpty() || doctorIdField.getText().isEmpty() || 
                appointmentDatePicker.getValue() == null || timeField.getText().isEmpty()) {
                showAlert("Error", "Please fill in all required fields");
                return;
            }
            try {
                int patientId = Integer.parseInt(patientIdField.getText());
                int doctorId = Integer.parseInt(doctorIdField.getText());
                LocalDate date = appointmentDatePicker.getValue();
                LocalTime time = LocalTime.parse(timeField.getText());
                
                Appointment appointment = new Appointment(
                    patientId,
                    doctorId,
                    date,
                    time,
                    "Scheduled",
                    reasonField.getText()
                );
                
                if (AppointmentService.createAppointment(appointment)) {
                    showAlert("Success", "Appointment scheduled successfully!");
                    patientIdField.clear();
                    doctorIdField.clear();
                    appointmentDatePicker.setValue(null);
                    timeField.clear();
                    reasonField.clear();
                } else {
                    showAlert("Error", "Failed to schedule appointment");
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter valid patient and doctor IDs");
            } catch (Exception ex) {
                showAlert("Error", "Invalid time format (use HH:MM): " + ex.getMessage());
            }
        });

        // Appointment List
        ListView<String> appointmentListView = new ListView<>();
        Button refreshBtn = new Button("Refresh List");
        refreshBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshBtn.setOnAction(e -> {
            appointmentListView.getItems().clear();
            List<Appointment> appointments = AppointmentService.getAllAppointments();
            if (appointments != null) {
                for (Appointment a : appointments) {
                    appointmentListView.getItems().add("ID: " + a.getAppointmentId() + " - " + 
                        "Patient: " + a.getPatientId() + ", Doctor: " + a.getDoctorId() + 
                        ", Date: " + a.getAppointmentDate() + " " + a.getAppointmentTime() + 
                        " (" + a.getStatus() + ")");
                }
            }
        });

        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(patientIdField, doctorIdField, appointmentDatePicker, timeField, reasonField, scheduleBtn);

        root.getChildren().addAll(
            titleLabel,
            new Separator(),
            new Label("Schedule New Appointment:"),
            inputBox,
            new Separator(),
            new Label("Appointment List:"),
            refreshBtn,
            appointmentListView
        );

        tab.setContent(root);
        return tab;
    }

    private Tab createDashboardTab() {
        Tab tab = new Tab("Dashboard");
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("System Dashboard");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        Label infoLabel = new Label();
        infoLabel.setStyle("-fx-font-size: 14; -fx-wrap-text: true;");

        Button statsBtn = new Button("Refresh Statistics");
        statsBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        statsBtn.setOnAction(e -> {
            List<Patient> patients = PatientService.getAllPatients();
            List<Doctor> doctors = DoctorService.getAllDoctors();
            List<Appointment> appointments = AppointmentService.getAllAppointments();

            int patientCount = patients != null ? patients.size() : 0;
            int doctorCount = doctors != null ? doctors.size() : 0;
            int appointmentCount = appointments != null ? appointments.size() : 0;

            String stats = String.format(
                "System Statistics:\n\n" +
                "Total Patients: %d\n" +
                "Total Doctors: %d\n" +
                "Total Appointments: %d\n\n" +
                "Database: MySQL (Demo Mode - No Connection)\n" +
                "Status: ✓ Application Running",
                patientCount, doctorCount, appointmentCount
            );
            infoLabel.setText(stats);
        });

        root.getChildren().addAll(
            titleLabel,
            statsBtn,
            new Separator(),
            infoLabel
        );

        tab.setContent(root);
        return tab;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
