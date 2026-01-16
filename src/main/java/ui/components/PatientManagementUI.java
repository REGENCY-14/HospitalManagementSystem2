package ui.components;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.Patient;
import service.PatientService;
import ui.util.AlertUtil;
import ui.util.FormUtil;
import ui.util.FXMLUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * UI Component for Patient Management
 * Handles FXML loading and component initialization
 */
public class PatientManagementUI extends FXMLTabController {
    
    @FXML public TextField firstNameField;
    @FXML public TextField lastNameField;
    @FXML public TextField phoneField;
    @FXML public ComboBox<String> genderCombo;
    @FXML public ComboBox<String> bloodGroupCombo;
    @FXML public DatePicker dobPicker;
    @FXML public Button addBtn;
    @FXML public Button refreshBtn;
    @FXML public Button editBtn;
    @FXML public Button deleteBtn;
    @FXML public TextField searchField;
    @FXML public TableView<Patient> patientTable;
    @FXML public TableColumn<Patient, Integer> idCol;
    @FXML public TableColumn<Patient, String> firstNameCol;
    @FXML public TableColumn<Patient, String> lastNameCol;
    @FXML public TableColumn<Patient, String> genderCol;
    @FXML public TableColumn<Patient, String> phoneCol;
    @FXML public TableColumn<Patient, LocalDate> dobCol;
    @FXML public TableColumn<Patient, String> bloodTypeCol;
    
    private PatientUIController uiController;
    
    public PatientManagementUI() {
        this.uiController = new PatientUIController(this);
    }

    @Override
    protected String getFXMLFile() {
        return "Patient.fxml";
    }

    @Override
    protected void initializeUI() {
        uiController.initializeComboBoxes();
        uiController.initializeTableColumns();
        uiController.initializeEventHandlers();
        uiController.loadPatientData();
    }

    public Tab createPatientTab() {
        Tab tab = createTabFromFXML("Patient Management");
        return tab;
    }
    
    public void showMessage(String title, String message) {
        AlertUtil.showInfo(title, message);
    }
    
    public void showError(String title, String message) {
        AlertUtil.showError(title, message);
    }
    
    public void clearInputFields() {
        FormUtil.clearFields(firstNameField, lastNameField, phoneField);
        FormUtil.clearComboBoxes(genderCombo, bloodGroupCombo);
        dobPicker.setValue(null);
    }
    
    public String getFirstName() {
        return FormUtil.getTrimmedText(firstNameField);
    }
    
    public String getLastName() {
        return FormUtil.getTrimmedText(lastNameField);
    }
    
    public String getPhone() {
        return FormUtil.getTrimmedText(phoneField);
    }
    
    public String getGender() {
        return genderCombo.getValue() != null ? genderCombo.getValue() : "M";
    }
    
    public String getBloodGroup() {
        return bloodGroupCombo.getValue() != null ? bloodGroupCombo.getValue() : "O+";
    }
    
    public LocalDate getDateOfBirth() {
        return dobPicker.getValue();
    }
    
    public Patient getSelectedPatient() {
        return patientTable.getSelectionModel().getSelectedItem();
    }
    
    public void populateFormWithPatient(Patient patient) {
        firstNameField.setText(patient.getFirstName());
        lastNameField.setText(patient.getLastName());
        phoneField.setText(patient.getPhone());
        genderCombo.setValue(patient.getGender());
        bloodGroupCombo.setValue(patient.getBloodType());
        dobPicker.setValue(patient.getDateOfBirth());
    }
    
    public void loadPatientDataInTable() {
        patientTable.getItems().clear();
        List<Patient> patients = PatientService.getAllPatients();
        if (patients != null) {
            patientTable.getItems().addAll(patients);
        }
    }
    
    public void refreshTable() {
        patientTable.refresh();
    }
    
    public void removePatientFromTable(Patient patient) {
        patientTable.getItems().remove(patient);
    }
}
