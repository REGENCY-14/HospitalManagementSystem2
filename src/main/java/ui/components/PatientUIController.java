package ui.components;

import javafx.scene.control.cell.PropertyValueFactory;
import model.Patient;
import service.PatientService;
import util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic controller for Patient Management UI
 * Works with PatientManagementUI to handle operations
 */
public class PatientUIController {
    
    private PatientManagementUI ui;
    
    public PatientUIController(PatientManagementUI ui) {
        this.ui = ui;
    }
    
    public void initializeComboBoxes() {
        ui.genderCombo.getItems().addAll("M", "F", "O");
        ui.genderCombo.setValue("M");
        
        ui.bloodGroupCombo.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        ui.bloodGroupCombo.setValue("O+");
    }
    
    public void initializeTableColumns() {
        ui.idCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        ui.firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        ui.lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        ui.genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        ui.phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        ui.dobCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        ui.bloodTypeCol.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
    }
    
    public void initializeEventHandlers() {
        ui.addBtn.setOnAction(e -> handleAddPatient());
        ui.refreshBtn.setOnAction(e -> handleRefresh());
        ui.editBtn.setOnAction(e -> handleEdit());
        ui.deleteBtn.setOnAction(e -> handleDelete());
        ui.searchField.textProperty().addListener((obs, oldVal, newVal) -> handleSearch(newVal));
    }
    
    public void handleAddPatient() {
        if (ui.firstNameField.getText().isEmpty() || ui.lastNameField.getText().isEmpty()) {
            ui.showError("Validation Error", "First Name and Last Name are required!");
            return;
        }
        
        if (!ValidationUtil.isValidName(ui.getFirstName())) {
            ui.showError("Validation Error", "First Name can only contain letters, spaces, hyphens, and apostrophes!");
            ui.firstNameField.requestFocus();
            return;
        }
        
        if (!ValidationUtil.isValidName(ui.getLastName())) {
            ui.showError("Validation Error", "Last Name can only contain letters, spaces, hyphens, and apostrophes!");
            ui.lastNameField.requestFocus();
            return;
        }
        
        if (!ValidationUtil.isValidPhone(ui.getPhone())) {
            ui.showError("Validation Error", "Phone Number can only contain digits, spaces, hyphens, and parentheses.");
            ui.phoneField.requestFocus();
            return;
        }
        
        try {
            Patient patient = new Patient(
                ui.getFirstName(),
                ui.getLastName(),
                ui.getDateOfBirth(),
                ui.getGender(),
                ui.getPhone(),
                "",
                ui.getBloodGroup()
            );
            
            if (PatientService.createPatient(patient)) {
                ui.showMessage("Success", "Patient added successfully!");
                ui.clearInputFields();
                loadPatientData();
            } else {
                ui.showError("Error", "Failed to add patient.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ui.showError("Error", "Failed to add patient: " + ex.getMessage());
        }
    }
    
    public void handleRefresh() {
        loadPatientData();
        ui.searchField.clear();
    }
    
    public void handleEdit() {
        Patient selected = ui.getSelectedPatient();
        if (selected == null) {
            ui.showError("Error", "Please select a patient to edit");
            return;
        }
        
        ui.populateFormWithPatient(selected);
        ui.addBtn.setText("Update Patient");
        ui.addBtn.setOnAction(evt -> handleUpdatePatient(selected));
    }
    
    private void handleUpdatePatient(Patient patient) {
        if (!ValidationUtil.isValidName(ui.getFirstName()) || !ValidationUtil.isValidName(ui.getLastName())) {
            ui.showError("Validation Error", "Names can only contain letters, spaces, hyphens, and apostrophes!");
            return;
        }
        
        try {
            patient.setFirstName(ui.getFirstName());
            patient.setLastName(ui.getLastName());
            patient.setPhone(ui.getPhone());
            patient.setGender(ui.getGender());
            patient.setBloodType(ui.getBloodGroup());
            patient.setDateOfBirth(ui.getDateOfBirth());
            
            if (PatientService.updatePatient(patient)) {
                ui.showMessage("Success", "Patient updated successfully!");
                ui.refreshTable();
                ui.clearInputFields();
                ui.addBtn.setText("Add Patient");
                initializeEventHandlers();
            } else {
                ui.showError("Error", "Failed to update patient");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ui.showError("Error", "Failed to update: " + ex.getMessage());
        }
    }
    
    public void handleDelete() {
        Patient selected = ui.getSelectedPatient();
        if (selected == null) {
            ui.showError("Error", "Please select a patient to delete");
            return;
        }
        
        if (PatientService.deletePatient(selected.getPatientId())) {
            ui.showMessage("Success", "Patient deleted successfully!");
            ui.removePatientFromTable(selected);
        } else {
            ui.showError("Error", "Failed to delete patient");
        }
    }
    
    public void handleSearch(String searchText) {
        List<Patient> allPatients = PatientService.getAllPatients();
        if (allPatients == null) return;
        
        if (searchText == null || searchText.trim().isEmpty()) {
            ui.patientTable.getItems().setAll(allPatients);
        } else {
            String search = searchText.toLowerCase();
            ui.patientTable.getItems().setAll(allPatients.stream()
                .filter(p -> (p.getFirstName() + " " + p.getLastName()).toLowerCase().contains(search) ||
                             (p.getPhone() != null && p.getPhone().contains(search)) ||
                             (p.getBloodType() != null && p.getBloodType().toLowerCase().contains(search)))
                .collect(Collectors.toList()));
        }
    }
    
    public void loadPatientData() {
        ui.loadPatientDataInTable();
    }
}
