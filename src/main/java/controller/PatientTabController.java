package controller;

import java.time.LocalDate;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Patient;
import service.PatientService;
import util.ValidationUtil;

public class PatientTabController {
    
    private TableView<Patient> patientTable;
    
    public Tab createPatientTab() {
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
            // Validate required fields
            if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
                showAlert("Validation Error", "First Name and Last Name are required fields!");
                return;
            }
            
            // Validate first name
            if (!ValidationUtil.isValidName(firstNameField.getText())) {
                showAlert("Validation Error", "First Name can only contain letters, spaces, hyphens, and apostrophes.\nNumbers and special characters are not allowed!");
                firstNameField.requestFocus();
                return;
            }
            
            // Validate last name
            if (!ValidationUtil.isValidName(lastNameField.getText())) {
                showAlert("Validation Error", "Last Name can only contain letters, spaces, hyphens, and apostrophes.\nNumbers and special characters are not allowed!");
                lastNameField.requestFocus();
                return;
            }
            
            // Validate phone number
            if (!ValidationUtil.isValidPhone(phoneField.getText())) {
                showAlert("Validation Error", "Phone Number can only contain digits, spaces, hyphens, and parentheses.");
                phoneField.requestFocus();
                return;
            }
            
            // Validate email
            if (!ValidationUtil.isValidEmail(emailField.getText())) {
                showAlert("Validation Error", "Please enter a valid email address (e.g., user@example.com)");
                emailField.requestFocus();
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
                    clearFields(firstNameField, lastNameField, emailField, phoneField, genderCombo, bloodGroupCombo, dobPicker);
                    loadPatientData();
                } else {
                    showAlert("Error", "Failed to add patient. Check console for details.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to add patient: " + ex.getMessage());
            }
        });

        // Patient Table
        patientTable = new TableView<>();
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
            loadPatientData();
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
                    if (!ValidationUtil.isValidName(firstNameField.getText()) || !ValidationUtil.isValidName(lastNameField.getText())) {
                        showAlert("Validation Error", "Names can only contain letters, spaces, hyphens, and apostrophes!");
                        return;
                    }
                    
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
                            clearFields(firstNameField, lastNameField, emailField, phoneField, genderCombo, bloodGroupCombo, dobPicker);
                            addBtn.setText("Add Patient");
                            setupAddButton(addBtn, firstNameField, lastNameField, emailField, phoneField, genderCombo, bloodGroupCombo, dobPicker);
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
            new Label("Patient List:"),
            searchField,
            buttonBox,
            patientTable
        );

        tab.setContent(root);
        return tab;
    }
    
    private void setupAddButton(Button addBtn, TextField firstNameField, TextField lastNameField, TextField emailField, 
                                TextField phoneField, ComboBox<String> genderCombo, ComboBox<String> bloodGroupCombo, DatePicker dobPicker) {
        addBtn.setOnAction(e -> {
            if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
                showAlert("Validation Error", "First Name and Last Name are required fields!");
                return;
            }
            
            if (!ValidationUtil.isValidName(firstNameField.getText()) || !ValidationUtil.isValidName(lastNameField.getText())) {
                showAlert("Validation Error", "Names can only contain letters, spaces, hyphens, and apostrophes!");
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
                    clearFields(firstNameField, lastNameField, emailField, phoneField, genderCombo, bloodGroupCombo, dobPicker);
                    loadPatientData();
                } else {
                    showAlert("Error", "Failed to add patient. Check console for details.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to add patient: " + ex.getMessage());
            }
        });
    }
    
    private void clearFields(TextField firstNameField, TextField lastNameField, TextField emailField, 
                            TextField phoneField, ComboBox<String> genderCombo, ComboBox<String> bloodGroupCombo, DatePicker dobPicker) {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        genderCombo.setValue("M");
        bloodGroupCombo.setValue("O+");
        dobPicker.setValue(null);
    }
    
    public void loadPatientData() {
        if (patientTable != null) {
            patientTable.getItems().clear();
            List<Patient> patients = PatientService.getAllPatients();
            if (patients != null) {
                patientTable.getItems().addAll(patients);
            }
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
