package controller;

import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Doctor;
import service.DoctorService;
import util.ValidationUtil;

public class DoctorTabController {
    
    private TableView<Doctor> doctorTable;
    
    public Tab createDoctorTab() {
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
                showAlert("Validation Error", "Please enter a valid email address (e.g., doctor@example.com)");
                emailField.requestFocus();
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
                    clearFields(firstNameField, lastNameField, emailField, phoneField, departmentField, specializationField);
                    loadDoctorData();
                } else {
                    showAlert("Error", "Failed to add doctor");
                }
            } catch (Exception ex) {
                showAlert("Error", "Invalid input: " + ex.getMessage());
            }
        });

        // Doctor Table
        doctorTable = new TableView<>();
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
            loadDoctorData();
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
                    if (!ValidationUtil.isValidName(firstNameField.getText()) || !ValidationUtil.isValidName(lastNameField.getText())) {
                        showAlert("Validation Error", "Names can only contain letters, spaces, hyphens, and apostrophes!");
                        return;
                    }
                    
                    try {
                        selected.setFirstName(firstNameField.getText());
                        selected.setLastName(lastNameField.getText());
                        selected.setPhone(phoneField.getText());
                        selected.setSpecialization(specializationField.getText());
                        selected.setDepartmentId(Integer.parseInt(departmentField.getText()));
                        
                        if (DoctorService.updateDoctor(selected)) {
                            showAlert("Success", "Doctor updated successfully!");
                            doctorTable.refresh();
                            clearFields(firstNameField, lastNameField, emailField, phoneField, departmentField, specializationField);
                            addBtn.setText("Add Doctor");
                            setupAddButton(addBtn, firstNameField, lastNameField, emailField, phoneField, departmentField, specializationField);
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
    
    private void setupAddButton(Button addBtn, TextField firstNameField, TextField lastNameField, TextField emailField, 
                                TextField phoneField, TextField departmentField, TextField specializationField) {
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
                Doctor doctor = new Doctor(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    specializationField.getText(),
                    phoneField.getText(),
                    1
                );
                if (DoctorService.createDoctor(doctor)) {
                    showAlert("Success", "Doctor added successfully!");
                    clearFields(firstNameField, lastNameField, emailField, phoneField, departmentField, specializationField);
                    loadDoctorData();
                } else {
                    showAlert("Error", "Failed to add doctor");
                }
            } catch (Exception ex) {
                showAlert("Error", "Invalid input: " + ex.getMessage());
            }
        });
    }
    
    private void clearFields(TextField firstNameField, TextField lastNameField, TextField emailField, 
                            TextField phoneField, TextField departmentField, TextField specializationField) {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        departmentField.clear();
        specializationField.clear();
    }
    
    public void loadDoctorData() {
        if (doctorTable != null) {
            doctorTable.getItems().clear();
            List<Doctor> doctors = DoctorService.getAllDoctors();
            if (doctors != null) {
                doctorTable.getItems().addAll(doctors);
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
