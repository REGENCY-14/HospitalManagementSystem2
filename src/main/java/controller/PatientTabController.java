package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Patient;
import service.PatientService;
import util.ValidationUtil;

/**
 * Controller for Patient Management tab.
 * UI layout is defined in Patient.fxml
 */
public class PatientTabController {
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField phoneField;
    
    @FXML
    private ComboBox<String> genderCombo;
    
    @FXML
    private ComboBox<String> bloodGroupCombo;
    
    @FXML
    private DatePicker dobPicker;
    
    @FXML
    private Button addBtn;
    
    @FXML
    private Button refreshBtn;
    
    @FXML
    private Button editBtn;
    
    @FXML
    private Button deleteBtn;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private TableView<Patient> patientTable;
    
    @FXML
    private TableColumn<Patient, Integer> idCol;
    
    @FXML
    private TableColumn<Patient, String> firstNameCol;
    
    @FXML
    private TableColumn<Patient, String> lastNameCol;
    
    @FXML
    private TableColumn<Patient, String> genderCol;
    
    @FXML
    private TableColumn<Patient, String> phoneCol;
    
    @FXML
    private TableColumn<Patient, LocalDate> dobCol;
    
    @FXML
    private TableColumn<Patient, String> bloodTypeCol;
    
    @FXML
    private VBox inputSection;
    
    @FXML
    private HBox buttonBox;
    
    public Tab createPatientTab() {
        Tab tab = new Tab("Patient Management");

        try {
            // Load FXML
            FXMLLoader loader = new FXMLLoader(getResource("Patient.fxml"));
            loader.setController(this);
            VBox root = loader.load();
            
            // Initialize UI components
            initializeComboBoxes();
            initializeTableColumns();
            initializeEventHandlers();
            loadPatientData();
            
            tab.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Patient.fxml: " + e.getMessage());
        }

        return tab;
    }
    
    private void initializeComboBoxes() {
        genderCombo.getItems().addAll("M", "F", "O");
        genderCombo.setValue("M");
        
        bloodGroupCombo.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        bloodGroupCombo.setValue("O+");
    }
    
    private void initializeTableColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        dobCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        bloodTypeCol.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
    }
    
    private void initializeEventHandlers() {
        addBtn.setOnAction(e -> handleAddPatient());
        refreshBtn.setOnAction(e -> handleRefresh());
        editBtn.setOnAction(e -> handleEdit());
        deleteBtn.setOnAction(e -> handleDelete());
        
        // Search functionality
        searchField.textProperty().addListener((obs, oldVal, newVal) -> handleSearch(newVal));
    }
    
    private void handleAddPatient() {
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
                clearFields();
                loadPatientData();
            } else {
                showAlert("Error", "Failed to add patient. Check console for details.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to add patient: " + ex.getMessage());
        }
    }
    
    private void handleRefresh() {
        loadPatientData();
        searchField.clear();
    }
    
    private void handleEdit() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            firstNameField.setText(selected.getFirstName());
            lastNameField.setText(selected.getLastName());
            phoneField.setText(selected.getPhone());
            genderCombo.setValue(selected.getGender());
            bloodGroupCombo.setValue(selected.getBloodType());
            dobPicker.setValue(selected.getDateOfBirth());
            
            String originalButtonText = addBtn.getText();
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
                        clearFields();
                        addBtn.setText(originalButtonText);
                        initializeEventHandlers();
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
    }
    
    private void handleDelete() {
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
    }
    
    private void handleSearch(String searchText) {
        List<Patient> allPatients = PatientService.getAllPatients();
        if (allPatients == null) return;
        
        if (searchText == null || searchText.trim().isEmpty()) {
            patientTable.getItems().setAll(allPatients);
        } else {
            String search = searchText.toLowerCase();
            patientTable.getItems().setAll(allPatients.stream()
                .filter(p -> (p.getFirstName() + " " + p.getLastName()).toLowerCase().contains(search) ||
                             (p.getPhone() != null && p.getPhone().contains(search)) ||
                             (p.getBloodType() != null && p.getBloodType().toLowerCase().contains(search)))
                .collect(java.util.stream.Collectors.toList()));
        }
    }
    
    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
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
    
    private URL getResource(String resourceName) {
        return getClass().getResource("/fxml/" + resourceName);
    }
}
