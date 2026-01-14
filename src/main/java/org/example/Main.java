package org.example;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
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

        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("M", "F", "O");
        genderCombo.setPromptText("Gender");
        genderCombo.setValue("M");

        ComboBox<String> bloodGroupCombo = new ComboBox<>();
        bloodGroupCombo.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        bloodGroupCombo.setPromptText("Blood Group");
        bloodGroupCombo.setValue("O+");

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
                String gender = genderCombo.getValue() != null ? genderCombo.getValue() : "M";
                String bloodGroup = bloodGroupCombo.getValue() != null ? bloodGroupCombo.getValue() : "O+";
                Patient patient = new Patient(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    dob,
                    gender,
                    phoneField.getText(),
                    "",
                    bloodGroup
                );
                if (PatientService.createPatient(patient)) {
                    showAlert("Success", "Patient added successfully!");
                    firstNameField.clear();
                    lastNameField.clear();
                    emailField.clear();
                    phoneField.clear();
                    genderCombo.setValue("M");
                    bloodGroupCombo.setValue("O+");
                    dobPicker.setValue(null);
                } else {
                    showAlert("Error", "Failed to add patient. Check console for details.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to add patient: " + ex.getMessage());
            }
        });

        // Patient Table
        TableView<Patient> patientTable = new TableView<>();
        patientTable.setPrefHeight(400);
        
        TableColumn<Patient, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        idCol.setPrefWidth(50);
        
        TableColumn<Patient, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameCol.setPrefWidth(120);
        
        TableColumn<Patient, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameCol.setPrefWidth(120);
        
        TableColumn<Patient, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        genderCol.setPrefWidth(70);
        
        TableColumn<Patient, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneCol.setPrefWidth(120);
        
        TableColumn<Patient, LocalDate> dobCol = new TableColumn<>("Date of Birth");
        dobCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        dobCol.setPrefWidth(120);
        
        TableColumn<Patient, String> bloodTypeCol = new TableColumn<>("Blood Type");
        bloodTypeCol.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
        bloodTypeCol.setPrefWidth(90);
        
        patientTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, genderCol, phoneCol, dobCol, bloodTypeCol);
        
        // Search functionality
        TextField searchField = new TextField();
        searchField.setPromptText("Search by name, phone, or blood type...");
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            List<Patient> allPatients = PatientService.getAllPatients();
            if (allPatients == null) return;
            
            if (newVal == null || newVal.trim().isEmpty()) {
                patientTable.getItems().setAll(allPatients);
            } else {
                String search = newVal.toLowerCase();
                patientTable.getItems().setAll(allPatients.stream()
                    .filter(p -> (p.getFirstName() + " " + p.getLastName()).toLowerCase().contains(search) ||
                                 (p.getPhone() != null && p.getPhone().contains(search)) ||
                                 (p.getBloodType() != null && p.getBloodType().toLowerCase().contains(search)))
                    .collect(java.util.stream.Collectors.toList()));
            }
        });
        
        Button refreshBtn = new Button("Refresh List");
        refreshBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshBtn.setOnAction(e -> {
            patientTable.getItems().clear();
            List<Patient> patients = PatientService.getAllPatients();
            if (patients != null) {
                patientTable.getItems().addAll(patients);
            }
            searchField.clear();
        });
        
        Button editBtn = new Button("Edit Selected");
        editBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        editBtn.setOnAction(e -> {
            Patient selected = patientTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                firstNameField.setText(selected.getFirstName());
                lastNameField.setText(selected.getLastName());
                phoneField.setText(selected.getPhone());
                genderCombo.setValue(selected.getGender());
                bloodGroupCombo.setValue(selected.getBloodType());
                dobPicker.setValue(selected.getDateOfBirth());
                
                addBtn.setText("Update Patient");
                addBtn.setOnAction(updateEvt -> {
                    try {
                        selected.setFirstName(firstNameField.getText());
                        selected.setLastName(lastNameField.getText());
                        selected.setPhone(phoneField.getText());
                        selected.setGender(genderCombo.getValue());
                        selected.setBloodType(bloodGroupCombo.getValue());
                        selected.setDateOfBirth(dobPicker.getValue());
                        
                        if (PatientService.updatePatient(selected)) {
                            showAlert("Success", "Patient updated successfully!");
                            patientTable.refresh();
                            firstNameField.clear();
                            lastNameField.clear();
                            phoneField.clear();
                            genderCombo.setValue("M");
                            bloodGroupCombo.setValue("O+");
                            dobPicker.setValue(null);
                            addBtn.setText("Add Patient");
                            
                            // Reset to add mode
                            addBtn.setOnAction(addEvt -> {
                                if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
                                    showAlert("Error", "Please fill in all required fields");
                                    return;
                                }
                                try {
                                    LocalDate dob = dobPicker.getValue();
                                    String gender = genderCombo.getValue() != null ? genderCombo.getValue() : "M";
                                    String bloodGroup = bloodGroupCombo.getValue() != null ? bloodGroupCombo.getValue() : "O+";
                                    Patient newPatient = new Patient(
                                        firstNameField.getText(),
                                        lastNameField.getText(),
                                        dob,
                                        gender,
                                        phoneField.getText(),
                                        "",
                                        bloodGroup
                                    );
                                    if (PatientService.createPatient(newPatient)) {
                                        showAlert("Success", "Patient added successfully!");
                                        firstNameField.clear();
                                        lastNameField.clear();
                                        emailField.clear();
                                        phoneField.clear();
                                        genderCombo.setValue("M");
                                        bloodGroupCombo.setValue("O+");
                                        dobPicker.setValue(null);
                                    } else {
                                        showAlert("Error", "Failed to add patient. Check console for details.");
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    showAlert("Error", "Failed to add patient: " + ex.getMessage());
                                }
                            });
                        } else {
                            showAlert("Error", "Failed to update patient");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showAlert("Error", "Failed to update: " + ex.getMessage());
                    }
                });
            } else {
                showAlert("Error", "Please select a patient to edit");
            }
        });
        
        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        deleteBtn.setOnAction(e -> {
            Patient selected = patientTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (PatientService.deletePatient(selected.getPatientId())) {
                    showAlert("Success", "Patient deleted successfully!");
                    patientTable.getItems().remove(selected);
                } else {
                    showAlert("Error", "Failed to delete patient");
                }
            } else {
                showAlert("Error", "Please select a patient to delete");
            }
        });
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(refreshBtn, editBtn, deleteBtn);

        VBox inputSection = new VBox(10);
        HBox inputBox1 = new HBox(10);
        inputBox1.getChildren().addAll(firstNameField, lastNameField, phoneField, genderCombo);
        HBox inputBox2 = new HBox(10);
        inputBox2.getChildren().addAll(bloodGroupCombo, dobPicker, addBtn);
        inputSection.getChildren().addAll(inputBox1, inputBox2);

        root.getChildren().addAll(
            titleLabel,
            new Separator(),
            new Label("Add New Patient:"),
            inputSection,
            new Separator(),
            new Label("Patient List:"),            searchField,            buttonBox,
            patientTable
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

        // Doctor Table
        TableView<Doctor> doctorTable = new TableView<>();
        doctorTable.setPrefHeight(400);
        
        TableColumn<Doctor, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        idCol.setPrefWidth(50);
        
        TableColumn<Doctor, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameCol.setPrefWidth(120);
        
        TableColumn<Doctor, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameCol.setPrefWidth(120);
        
        TableColumn<Doctor, String> specializationCol = new TableColumn<>("Specialization");
        specializationCol.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        specializationCol.setPrefWidth(150);
        
        TableColumn<Doctor, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneCol.setPrefWidth(120);
        
        TableColumn<Doctor, Integer> deptCol = new TableColumn<>("Dept ID");
        deptCol.setCellValueFactory(new PropertyValueFactory<>("departmentId"));
        deptCol.setPrefWidth(80);
        
        doctorTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, specializationCol, phoneCol, deptCol);
        
        // Search functionality
        TextField searchField = new TextField();
        searchField.setPromptText("Search by name, specialization, or phone...");
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            List<Doctor> allDoctors = DoctorService.getAllDoctors();
            if (allDoctors == null) return;
            
            if (newVal == null || newVal.trim().isEmpty()) {
                doctorTable.getItems().setAll(allDoctors);
            } else {
                String search = newVal.toLowerCase();
                doctorTable.getItems().setAll(allDoctors.stream()
                    .filter(d -> (d.getFirstName() + " " + d.getLastName()).toLowerCase().contains(search) ||
                                 (d.getSpecialization() != null && d.getSpecialization().toLowerCase().contains(search)) ||
                                 (d.getPhone() != null && d.getPhone().contains(search)))
                    .collect(java.util.stream.Collectors.toList()));
            }
        });
        
        Button refreshBtn = new Button("Refresh List");
        refreshBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshBtn.setOnAction(e -> {
            doctorTable.getItems().clear();
            List<Doctor> doctors = DoctorService.getAllDoctors();
            if (doctors != null) {
                doctorTable.getItems().addAll(doctors);
            }
            searchField.clear();
        });
        
        Button editBtn = new Button("Edit Selected");
        editBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        editBtn.setOnAction(e -> {
            Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                firstNameField.setText(selected.getFirstName());
                lastNameField.setText(selected.getLastName());
                phoneField.setText(selected.getPhone());
                specializationField.setText(selected.getSpecialization());
                departmentField.setText(String.valueOf(selected.getDepartmentId()));
                
                addBtn.setText("Update Doctor");
                addBtn.setOnAction(updateEvt -> {
                    try {
                        selected.setFirstName(firstNameField.getText());
                        selected.setLastName(lastNameField.getText());
                        selected.setPhone(phoneField.getText());
                        selected.setSpecialization(specializationField.getText());
                        selected.setDepartmentId(Integer.parseInt(departmentField.getText()));
                        
                        if (DoctorService.updateDoctor(selected)) {
                            showAlert("Success", "Doctor updated successfully!");
                            doctorTable.refresh();
                            firstNameField.clear();
                            lastNameField.clear();
                            phoneField.clear();
                            specializationField.clear();
                            departmentField.clear();
                            addBtn.setText("Add Doctor");
                            
                            // Reset to add mode
                            addBtn.setOnAction(addEvt -> {
                                if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
                                    showAlert("Error", "Please fill in all required fields");
                                    return;
                                }
                                try {
                                    Doctor newDoctor = new Doctor(
                                        firstNameField.getText(),
                                        lastNameField.getText(),
                                        specializationField.getText(),
                                        phoneField.getText(),
                                        1
                                    );
                                    if (DoctorService.createDoctor(newDoctor)) {
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
                        } else {
                            showAlert("Error", "Failed to update doctor");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showAlert("Error", "Failed to update: " + ex.getMessage());
                    }
                });
            } else {
                showAlert("Error", "Please select a doctor to edit");
            }
        });
        
        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        deleteBtn.setOnAction(e -> {
            Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (DoctorService.deleteDoctor(selected.getDoctorId())) {
                    showAlert("Success", "Doctor deleted successfully!");
                    doctorTable.getItems().remove(selected);
                } else {
                    showAlert("Error", "Failed to delete doctor");
                }
            } else {
                showAlert("Error", "Please select a doctor to delete");
            }
        });
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(refreshBtn, editBtn, deleteBtn);

        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(firstNameField, lastNameField, emailField, phoneField, departmentField, specializationField, addBtn);

        root.getChildren().addAll(
            titleLabel,
            new Separator(),
            new Label("Add New Doctor:"),
            inputBox,
            new Separator(),
            new Label("Doctor List:"),
            searchField,
            buttonBox,
            doctorTable
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

        // Patient & Doctor selection
        ComboBox<Patient> patientCombo = new ComboBox<>();
        patientCombo.setPromptText("Select Patient");
        List<Patient> allPatients = PatientService.getAllPatients();
        if (allPatients != null) {
            patientCombo.getItems().addAll(allPatients);
        }
        patientCombo.setConverter(new StringConverter<Patient>() {
            @Override
            public String toString(Patient p) {
                if (p == null) return "";
                return p.getFirstName() + " " + p.getLastName() + " (ID: " + p.getPatientId() + ")";
            }
            @Override
            public Patient fromString(String string) { return null; }
        });

        ComboBox<Doctor> doctorCombo = new ComboBox<>();
        doctorCombo.setPromptText("Select Doctor");
        List<Doctor> allDoctors = DoctorService.getAllDoctors();
        if (allDoctors != null) {
            doctorCombo.getItems().addAll(allDoctors);
        }
        doctorCombo.setConverter(new StringConverter<Doctor>() {
            @Override
            public String toString(Doctor d) {
                if (d == null) return "";
                return d.getFirstName() + " " + d.getLastName() + " (ID: " + d.getDoctorId() + ")";
            }
            @Override
            public Doctor fromString(String string) { return null; }
        });

        Runnable reloadDropdowns = () -> {
            patientCombo.getItems().clear();
            List<Patient> refreshedPatients = PatientService.getAllPatients();
            if (refreshedPatients != null) {
                patientCombo.getItems().addAll(refreshedPatients);
            }

            doctorCombo.getItems().clear();
            List<Doctor> refreshedDoctors = DoctorService.getAllDoctors();
            if (refreshedDoctors != null) {
                doctorCombo.getItems().addAll(refreshedDoctors);
            }
        };

        DatePicker appointmentDatePicker = new DatePicker();
        appointmentDatePicker.setPromptText("Appointment Date");

        TextField timeField = new TextField();
        timeField.setPromptText("Time (HH:MM)");

        TextField reasonField = new TextField();
        reasonField.setPromptText("Reason for Visit");

        Button reloadDropdownsBtn = new Button("Reload Patients/Doctors");
        reloadDropdownsBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        reloadDropdownsBtn.setOnAction(e -> {
            reloadDropdowns.run();
            patientCombo.getSelectionModel().clearSelection();
            doctorCombo.getSelectionModel().clearSelection();
        });

        // Buttons
        Button scheduleBtn = new Button("Schedule Appointment");
        scheduleBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        scheduleBtn.setOnAction(e -> {
            Patient selectedPatient = patientCombo.getValue();
            Doctor selectedDoctor = doctorCombo.getValue();
            if (selectedPatient == null || selectedDoctor == null ||
                appointmentDatePicker.getValue() == null || timeField.getText().isEmpty()) {
                showAlert("Error", "Please select patient, doctor, date and time");
                return;
            }
            try {
                int patientId = selectedPatient.getPatientId();
                int doctorId = selectedDoctor.getDoctorId();
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
                    patientCombo.getSelectionModel().clearSelection();
                    doctorCombo.getSelectionModel().clearSelection();
                    appointmentDatePicker.setValue(null);
                    timeField.clear();
                    reasonField.clear();
                } else {
                    showAlert("Error", "Failed to schedule appointment");
                }
            } catch (Exception ex) {
                showAlert("Error", "Invalid time format (use HH:MM): " + ex.getMessage());
            }
        });

        // Appointment Table
        TableView<Appointment> appointmentTable = new TableView<>();
        appointmentTable.setPrefHeight(400);
        
        TableColumn<Appointment, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        idCol.setPrefWidth(50);
        
        TableColumn<Appointment, Integer> patientIdCol = new TableColumn<>("Patient ID");
        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        patientIdCol.setPrefWidth(90);
        
        TableColumn<Appointment, String> patientNameCol = new TableColumn<>("Patient Name");
        patientNameCol.setCellValueFactory(cellData -> {
            int patientId = cellData.getValue().getPatientId();
            Patient patient = PatientService.getPatient(patientId);
            String name = patient != null ? patient.getFirstName() + " " + patient.getLastName() : "Unknown";
            return new javafx.beans.property.SimpleStringProperty(name);
        });
        patientNameCol.setPrefWidth(150);
        
        TableColumn<Appointment, Integer> doctorIdCol = new TableColumn<>("Doctor ID");
        doctorIdCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        doctorIdCol.setPrefWidth(90);
        
        TableColumn<Appointment, String> doctorNameCol = new TableColumn<>("Doctor Name");
        doctorNameCol.setCellValueFactory(cellData -> {
            int doctorId = cellData.getValue().getDoctorId();
            Doctor doctor = DoctorService.getDoctor(doctorId);
            String name = doctor != null ? doctor.getFirstName() + " " + doctor.getLastName() : "Unknown";
            return new javafx.beans.property.SimpleStringProperty(name);
        });
        doctorNameCol.setPrefWidth(150);
        
        TableColumn<Appointment, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        dateCol.setPrefWidth(120);
        
        TableColumn<Appointment, LocalTime> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        timeCol.setPrefWidth(100);
        
        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);
        
        TableColumn<Appointment, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
        notesCol.setPrefWidth(200);
        
        appointmentTable.getColumns().addAll(idCol, patientIdCol, patientNameCol, doctorIdCol, doctorNameCol, dateCol, timeCol, statusCol, notesCol);
        
        // Search functionality
        TextField searchField = new TextField();
        searchField.setPromptText("Search by patient, doctor, status, or notes...");
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            List<Appointment> allAppointments = AppointmentService.getAllAppointments();
            if (allAppointments == null) return;
            
            if (newVal == null || newVal.trim().isEmpty()) {
                appointmentTable.getItems().setAll(allAppointments);
            } else {
                String search = newVal.toLowerCase();
                appointmentTable.getItems().setAll(allAppointments.stream()
                    .filter(a -> {
                        Patient p = PatientService.getPatient(a.getPatientId());
                        Doctor d = DoctorService.getDoctor(a.getDoctorId());
                        String patientName = p != null ? (p.getFirstName() + " " + p.getLastName()).toLowerCase() : "";
                        String doctorName = d != null ? (d.getFirstName() + " " + d.getLastName()).toLowerCase() : "";
                        return patientName.contains(search) ||
                               doctorName.contains(search) ||
                               (a.getStatus() != null && a.getStatus().toLowerCase().contains(search)) ||
                               (a.getNotes() != null && a.getNotes().toLowerCase().contains(search));
                    })
                    .collect(java.util.stream.Collectors.toList()));
            }
        });
        
        Button refreshBtn = new Button("Refresh List");
        refreshBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshBtn.setOnAction(e -> {
            appointmentTable.getItems().clear();
            List<Appointment> appointments = AppointmentService.getAllAppointments();
            if (appointments != null) {
                appointmentTable.getItems().addAll(appointments);
            }
            searchField.clear();
        });

        Button editBtn = new Button("Edit Selected");
        editBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        editBtn.setOnAction(e -> {
            Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select an appointment to edit.");
                return;
            }
            
            // Populate form with selected appointment data
            // Select patient & doctor in dropdowns
            patientCombo.getSelectionModel().clearSelection();
            for (Patient p : patientCombo.getItems()) {
                if (p.getPatientId() == selected.getPatientId()) { patientCombo.getSelectionModel().select(p); break; }
            }
            doctorCombo.getSelectionModel().clearSelection();
            for (Doctor d : doctorCombo.getItems()) {
                if (d.getDoctorId() == selected.getDoctorId()) { doctorCombo.getSelectionModel().select(d); break; }
            }
            appointmentDatePicker.setValue(selected.getAppointmentDate());
            timeField.setText(selected.getAppointmentTime() != null ? selected.getAppointmentTime().toString() : "");
            reasonField.setText(selected.getNotes());
            
            // Change button to Update mode
            scheduleBtn.setText("Update Appointment");
            scheduleBtn.setOnAction(updateEvent -> {
                try {
                    if (patientCombo.getValue() == null || doctorCombo.getValue() == null) {
                        showAlert("Error", "Please select patient and doctor");
                        return;
                    }
                    selected.setPatientId(patientCombo.getValue().getPatientId());
                    selected.setDoctorId(doctorCombo.getValue().getDoctorId());
                    selected.setAppointmentDate(appointmentDatePicker.getValue());
                    selected.setAppointmentTime(java.time.LocalTime.parse(timeField.getText()));
                    selected.setNotes(reasonField.getText());
                    
                    boolean success = AppointmentService.updateAppointment(selected);
                    if (success) {
                        showAlert("Success", "Appointment updated successfully!");
                        appointmentTable.refresh();
                        
                        // Reset form and button
                        patientCombo.getSelectionModel().clearSelection();
                        doctorCombo.getSelectionModel().clearSelection();
                        appointmentDatePicker.setValue(null);
                        timeField.clear();
                        reasonField.clear();
                        scheduleBtn.setText("Schedule Appointment");
                        scheduleBtn.setOnAction(scheduleEvent -> {
                            // Original add appointment logic
                            try {
                                if (patientCombo.getValue() == null || doctorCombo.getValue() == null) {
                                    showAlert("Error", "Please select patient and doctor");
                                    return;
                                }
                                int patientId = patientCombo.getValue().getPatientId();
                                int doctorId = doctorCombo.getValue().getDoctorId();
                                java.time.LocalDate date = appointmentDatePicker.getValue();
                                java.time.LocalTime time = java.time.LocalTime.parse(timeField.getText());
                                String notes = reasonField.getText();
                                
                                Appointment appointment = new Appointment(0, patientId, doctorId, date, time, "Scheduled", notes);
                                boolean addSuccess = AppointmentService.createAppointment(appointment);
                                
                                if (addSuccess) {
                                    showAlert("Success", "Appointment scheduled successfully!");
                                    appointmentTable.getItems().clear();
                                    List<Appointment> appointments = AppointmentService.getAllAppointments();
                                    if (appointments != null) {
                                        appointmentTable.getItems().addAll(appointments);
                                    }
                                    patientCombo.getSelectionModel().clearSelection();
                                    doctorCombo.getSelectionModel().clearSelection();
                                    appointmentDatePicker.setValue(null);
                                    timeField.clear();
                                    reasonField.clear();
                                } else {
                                    showAlert("Error", "Failed to schedule appointment");
                                }
                            } catch (Exception ex) {
                                showAlert("Error", "Invalid input: " + ex.getMessage());
                            }
                        });
                    } else {
                        showAlert("Error", "Failed to update appointment");
                    }
                } catch (Exception ex) {
                    showAlert("Error", "Invalid input: " + ex.getMessage());
                }
            });
        });

        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        deleteBtn.setOnAction(e -> {
            Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select an appointment to delete.");
                return;
            }
            
            boolean success = AppointmentService.deleteAppointment(selected.getAppointmentId());
            if (success) {
                showAlert("Success", "Appointment deleted successfully!");
                appointmentTable.getItems().remove(selected);
            } else {
                showAlert("Error", "Failed to delete appointment");
            }
        });

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(refreshBtn, editBtn, deleteBtn);

        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(patientCombo, doctorCombo, appointmentDatePicker, timeField, reasonField, scheduleBtn);

        root.getChildren().addAll(
            titleLabel,
            new Separator(),
            new Label("Schedule New Appointment:"),
            reloadDropdownsBtn,
            inputBox,
            new Separator(),
            new Label("Appointment List:"),
            searchField,
            buttonBox,
            appointmentTable
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
