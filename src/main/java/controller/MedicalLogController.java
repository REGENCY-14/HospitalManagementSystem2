package controller;

import dao.MongoDBConnection;
import dao.PatientMedicalLogDAO;
import model.PatientMedicalLog;
import model.Patient;
import model.Doctor;
import service.PatientService;
import service.DoctorService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JavaFX Controller for Medical Log Management (NoSQL/MongoDB)
 * Integrates MongoDB medical log operations with JavaFX UI
 */
public class MedicalLogController {
    
    private PatientMedicalLogDAO medicalLogDAO;
    private TableView<PatientMedicalLog> logTableView;
    private ListView<String> logDetailsListView;
    private ComboBox<Patient> patientCombo;
    private ComboBox<Doctor> doctorCombo;
    private ComboBox<String> logTypeComboBox;
    private TextArea notesTextArea;
    private Label connectionStatusLabel;
    
    // Current selected log
    private PatientMedicalLog selectedLog;
    
    /**
     * Constructor
     * @param primaryStage Primary stage for JavaFX application
     */
    public MedicalLogController(Stage primaryStage) {
        // Initialize label first, then MongoDB connection
        this.connectionStatusLabel = new Label("Initializing...");
        initializeMongoDB();
    }
    
    /**
     * Initialize MongoDB connection and DAO
     */
    private void initializeMongoDB() {
        try {
            MongoDBConnection mongoConnection = MongoDBConnection.getInstance();
            
            // Check connection
            if (mongoConnection.testConnection()) {
                this.medicalLogDAO = new PatientMedicalLogDAO(mongoConnection.getDatabase());
                updateConnectionStatus(true, "Connected");
                System.out.println("✓ MongoDB connected successfully in JavaFX Controller");
            } else {
                updateConnectionStatus(false, "Connection Failed");
                showAlert("MongoDB Connection", "Failed to connect to MongoDB", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            updateConnectionStatus(false, "Error: " + e.getMessage());
            showAlert("MongoDB Error", "Failed to initialize MongoDB: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Create the Medical Log UI Tab
     * @return Tab with medical log management UI
     */
    public Tab createMedicalLogTab() {
        Tab tab = new Tab("Medical Logs (NoSQL)", createMedicalLogContent());
        tab.setClosable(false);
        return tab;
    }
    
    /**
     * Create the content for medical log tab
     */
    private VBox createMedicalLogContent() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        
        // Title
        Label titleLabel = new Label("Medical Log Management (NoSQL)");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        
        // Connection Status
        HBox statusBar = createStatusBar();
        
        // Input Section
        Label inputSectionLabel = new Label("Create New Medical Log:");
        inputSectionLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        
        // Input fields
        patientCombo = new ComboBox<>();
        patientCombo.setPromptText("Select Patient");
        patientCombo.setPrefWidth(200);
        List<Patient> patients = PatientService.getAllPatients();
        if (patients != null) {
            patientCombo.getItems().addAll(patients);
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

        doctorCombo = new ComboBox<>();
        doctorCombo.setPromptText("Select Doctor");
        doctorCombo.setPrefWidth(200);
        List<Doctor> doctors = DoctorService.getAllDoctors();
        if (doctors != null) {
            doctorCombo.getItems().addAll(doctors);
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
        
        logTypeComboBox = new ComboBox<>();
        logTypeComboBox.setItems(FXCollections.observableArrayList(
            "appointment_note",
            "lab_result",
            "vital_sign",
            "prescription",
            "imaging_report",
            "emergency",
            "follow_up",
            "other"
        ));
        logTypeComboBox.setPromptText("Select Log Type");
        logTypeComboBox.setPrefWidth(180);
        
        notesTextArea = new TextArea();
        notesTextArea.setPromptText("Enter medical notes or diagnosis...");
        notesTextArea.setWrapText(true);
        notesTextArea.setPrefRowCount(3);
        notesTextArea.setPrefWidth(800);
        
        Button createButton = new Button("Add Medical Log");
        createButton.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        createButton.setOnAction(e -> createMedicalLog());
        
        Button clearButton = new Button("Clear");
        clearButton.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        clearButton.setOnAction(e -> clearForm());
        
        HBox inputRow1 = new HBox(10);
        inputRow1.getChildren().addAll(patientCombo, doctorCombo, logTypeComboBox);
        
        HBox inputRow2 = new HBox(10);
        inputRow2.getChildren().addAll(createButton, clearButton);
        
        VBox inputSection = new VBox(10);
        inputSection.getChildren().addAll(inputRow1, notesTextArea, inputRow2);
        
        // Medical Log List Section
        Label listSectionLabel = new Label("Medical Logs:");
        listSectionLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        
        logTableView = new TableView<>();
        logTableView.setPrefHeight(500);
        setupLogTable();
        
        // Search functionality
        TextField searchField = new TextField();
        searchField.setPromptText("Search by Patient ID, Log Type, or Title...");
        searchField.setPrefWidth(300);
        
        Button searchButton = new Button("Search/View All");
        searchButton.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        searchButton.setOnAction(e -> searchMedicalLogs());
        
        Button refreshButton = new Button("Refresh List");
        refreshButton.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshButton.setOnAction(e -> loadAllLogs());
        
        Button viewDetailsButton = new Button("View Details");
        viewDetailsButton.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        viewDetailsButton.setOnAction(e -> viewFullDetails());
        
        Button updateButton = new Button("Update Selected");
        updateButton.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        updateButton.setOnAction(e -> updateMedicalLog());
        
        Button deleteButton = new Button("Delete Selected");
        deleteButton.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        deleteButton.setOnAction(e -> deleteMedicalLog());
        
        HBox tableButtonBox = new HBox(10);
        tableButtonBox.getChildren().addAll(searchField, searchButton, refreshButton, viewDetailsButton, updateButton, deleteButton);
        
        // Log Details Section
        Label detailsSectionLabel = new Label("Selected Log Details:");
        detailsSectionLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        
        logDetailsListView = new ListView<>();
        logDetailsListView.setPrefHeight(150);
        
        // Add all sections to root
        root.getChildren().addAll(
            titleLabel,
            statusBar,
            new Separator(),
            inputSectionLabel,
            inputSection,
            new Separator(),
            listSectionLabel,
            tableButtonBox,
            logTableView,
            new Separator(),
            detailsSectionLabel,
            logDetailsListView
        );
        
        // Load all logs initially
        loadAllLogs();
        
        return root;
    }
    
    /**
     * Create status bar showing MongoDB connection status
     */
    private HBox createStatusBar() {
        HBox statusBar = new HBox(10);
        statusBar.setPadding(new Insets(10));
        statusBar.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5;");
        
        Label statusLabelTitle = new Label("MongoDB Status: ");
        statusLabelTitle.setStyle("-fx-font-weight: bold;");
        
        connectionStatusLabel = new Label("Connecting...");
        connectionStatusLabel.setStyle("-fx-text-fill: #FFA500;");
        
        Button refreshButton = new Button("Refresh Status");
        refreshButton.setOnAction(e -> refreshConnectionStatus());
        
        statusBar.getChildren().addAll(statusLabelTitle, connectionStatusLabel, refreshButton);
        return statusBar;
    }
    
    /**
     * Setup the medical log table with columns
     */
    private void setupLogTable() {
        // Medical Log ID Column
        TableColumn<PatientMedicalLog, String> idColumn = new TableColumn<>("Log ID");
        idColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMedicalLogId()));
        idColumn.setPrefWidth(120);
        
        // Patient ID Column
        TableColumn<PatientMedicalLog, Integer> patientIdColumn = new TableColumn<>("Patient ID");
        patientIdColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getPatientId()));
        patientIdColumn.setPrefWidth(100);
        
        // Log Type Column
        TableColumn<PatientMedicalLog, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLogType()));
        typeColumn.setPrefWidth(120);
        
        // Title Column
        TableColumn<PatientMedicalLog, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        titleColumn.setPrefWidth(200);
        
        // Date Column
        TableColumn<PatientMedicalLog, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getLogDate();
            String dateStr = date != null ? date.toString().substring(0, 10) : "N/A";
            return new javafx.beans.property.SimpleStringProperty(dateStr);
        });
        dateColumn.setPrefWidth(120);
        
        // Status Column
        TableColumn<PatientMedicalLog, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        statusColumn.setPrefWidth(100);
        
        logTableView.getColumns().addAll(
            idColumn, patientIdColumn, typeColumn, titleColumn, dateColumn, statusColumn
        );
        
        // Row selection listener
        logTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedLog = newVal;
                displayLogDetails(newVal);
            }
        });
    }
    
    
    /**
     * Create a new medical log
     */
    private void createMedicalLog() {
        if (!validateForm()) {
            showAlert("Validation Error", "Please fill in all required fields", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            Patient selPatient = patientCombo.getValue();
            Doctor selDoctor = doctorCombo.getValue();
            if (selPatient == null || selDoctor == null) {
                showAlert("Validation Error", "Please select patient and doctor", Alert.AlertType.WARNING);
                return;
            }
            int patientId = selPatient.getPatientId();
            String logType = logTypeComboBox.getValue();
            String notes = notesTextArea.getText();
            
            if (medicalLogDAO == null) {
                showAlert("Error", "MongoDB connection not available", Alert.AlertType.ERROR);
                return;
            }
            
            // Create medical log
            PatientMedicalLog log = new PatientMedicalLog();
            log.setPatientId(patientId);
            log.setMedicalLogId("ML-" + System.currentTimeMillis());
            log.setLogDate(LocalDateTime.now());
            log.setLogType(logType);
            log.setTitle("Medical Log - " + logType);
            log.setDescription(notes);
            log.setStatus("completed");
            log.getTags().add(logType);
            
            // Add physician info
            PatientMedicalLog.Physician physician = log.new Physician(
                selDoctor.getDoctorId(),
                selDoctor.getFirstName() + " " + selDoctor.getLastName(),
                selDoctor.getSpecialization(),
                String.valueOf(selDoctor.getDepartmentId())
            );
            log.setPhysician(physician);

            // Add basic clinical data
            PatientMedicalLog.ClinicalData clinicalData = log.new ClinicalData();
            clinicalData.setDiagnosis(notes);
            log.setClinicalData(clinicalData);
            
            // Add audit trail
            PatientMedicalLog.AuditTrail auditTrail = log.new AuditTrail();
            auditTrail.setCreatedBy("System");
            auditTrail.setCreatedAt(LocalDateTime.now());
            auditTrail.setVersion(1);
            log.setAuditTrail(auditTrail);
            
            // Save to MongoDB
            String logId = medicalLogDAO.create(log);
            
            showAlert("Success", "Medical log created successfully!\nLog ID: " + logId, Alert.AlertType.INFORMATION);
            
            // Clear form first, then load all logs to keep them displayed
            clearForm();
            loadAllLogs();
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid patient ID", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to create medical log: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Search medical logs by patient ID (or all logs if no patient selected)
     */
    private void searchMedicalLogs() {
        Patient selPatient = patientCombo.getValue();
        if (selPatient == null) {
            // If no patient selected, load all logs
            loadAllLogs();
            return;
        }
        
        try {
            int patientId = selPatient.getPatientId();
            
            if (medicalLogDAO == null) {
                showAlert("Error", "MongoDB connection not available", Alert.AlertType.ERROR);
                return;
            }
            
            // Search in background thread
            new Thread(() -> {
                List<PatientMedicalLog> logs = medicalLogDAO.findByPatientId(patientId);
                Platform.runLater(() -> {
                    if (logs.isEmpty()) {
                        showAlert("No Results", "No medical logs found for patient " + patientId, Alert.AlertType.INFORMATION);
                        logTableView.setItems(FXCollections.observableArrayList());
                    } else {
                        logTableView.setItems(FXCollections.observableArrayList(logs));
                        showAlert("Success", "Found " + logs.size() + " medical log(s)", Alert.AlertType.INFORMATION);
                    }
                });
            }).start();
        
        } catch (Exception e) {
            showAlert("Error", "Failed to search logs: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Update the selected medical log
     */
    private void updateMedicalLog() {
        if (selectedLog == null) {
            showAlert("Error", "Please select a medical log to update", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            if (medicalLogDAO == null) {
                showAlert("Error", "MongoDB connection not available", Alert.AlertType.ERROR);
                return;
            }
            
            // Update description/notes if changed
            String newNotes = notesTextArea.getText();
            if (!newNotes.isEmpty() && !newNotes.equals(selectedLog.getDescription())) {
                selectedLog.setDescription(newNotes);
                if (selectedLog.getClinicalData() != null) {
                    selectedLog.getClinicalData().setDiagnosis(newNotes);
                }
            }
            
            medicalLogDAO.update(selectedLog.getId(), selectedLog);
            showAlert("Success", "Medical log updated successfully", Alert.AlertType.INFORMATION);
            refreshAllLogs();
            
        } catch (Exception e) {
            showAlert("Error", "Failed to update medical log: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Delete the selected medical log
     */
    private void deleteMedicalLog() {
        if (selectedLog == null) {
            showAlert("Error", "Please select a medical log to delete", Alert.AlertType.WARNING);
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Medical Log");
        confirmAlert.setContentText("Are you sure you want to delete this medical log?\n\nLog ID: " + selectedLog.getMedicalLogId());
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (medicalLogDAO == null) {
                    showAlert("Error", "MongoDB connection not available", Alert.AlertType.ERROR);
                    return;
                }
                
                medicalLogDAO.delete(selectedLog.getId());
                showAlert("Success", "Medical log deleted successfully", Alert.AlertType.INFORMATION);
                logDetailsListView.setItems(FXCollections.observableArrayList());
                refreshAllLogs();
                
            } catch (Exception e) {
                showAlert("Error", "Failed to delete medical log: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    /**
     * Display log details in the details list view
     */
    private void displayLogDetails(PatientMedicalLog log) {
        ObservableList<String> details = FXCollections.observableArrayList();
        
        details.add("Medical Log ID: " + log.getMedicalLogId());
        details.add("Patient ID: " + log.getPatientId());
        if (log.getPhysician() != null) {
            details.add("Doctor: " + log.getPhysician().getName() + " (ID: " + log.getPhysician().getDoctorId() + ")");
            details.add("Specialization: " + log.getPhysician().getSpecialization());
        }
        details.add("Log Type: " + log.getLogType());
        details.add("Title: " + log.getTitle());
        details.add("Date: " + (log.getLogDate() != null ? log.getLogDate().toString() : "N/A"));
        details.add("Status: " + log.getStatus());
        details.add("Description: " + log.getDescription());
        
        if (log.getClinicalData() != null) {
            details.add("Diagnosis: " + log.getClinicalData().getDiagnosis());
            details.add("Treatment: " + log.getClinicalData().getTreatment());
        }
        
        if (log.getVitalSigns() != null) {
            details.add("BP: " + log.getVitalSigns().getSystolicBp() + "/" + log.getVitalSigns().getDiastolicBp());
            details.add("Heart Rate: " + log.getVitalSigns().getHeartRate() + " bpm");
            details.add("Temperature: " + log.getVitalSigns().getTemperature() + "°C");
        }
        
        if (log.getAuditTrail() != null) {
            details.add("Created By: " + log.getAuditTrail().getCreatedBy());
            details.add("Created At: " + log.getAuditTrail().getCreatedAt());
        }
        
        logDetailsListView.setItems(details);
    }
    
    /**
     * View full details of selected log in a separate window
     */
    private void viewFullDetails() {
        if (selectedLog == null) {
            showAlert("Error", "Please select a medical log", Alert.AlertType.WARNING);
            return;
        }
        
        StringBuilder details = new StringBuilder();
        details.append("FULL MEDICAL LOG DETAILS\n");
        details.append("═".repeat(50)).append("\n\n");
        
        details.append("LOG INFORMATION:\n");
        details.append("  Medical Log ID: ").append(selectedLog.getMedicalLogId()).append("\n");
        details.append("  Patient ID: ").append(selectedLog.getPatientId()).append("\n");
        if (selectedLog.getPhysician() != null) {
            details.append("  Doctor: ").append(selectedLog.getPhysician().getName())
                .append(" (ID: ").append(selectedLog.getPhysician().getDoctorId()).append(")\n");
            details.append("  Specialization: ").append(selectedLog.getPhysician().getSpecialization()).append("\n");
        }
        details.append("  Log Type: ").append(selectedLog.getLogType()).append("\n");
        details.append("  Title: ").append(selectedLog.getTitle()).append("\n");
        details.append("  Date: ").append(selectedLog.getLogDate()).append("\n");
        details.append("  Status: ").append(selectedLog.getStatus()).append("\n");
        details.append("  Description: ").append(selectedLog.getDescription()).append("\n\n");
        
        if (selectedLog.getClinicalData() != null) {
            details.append("CLINICAL DATA:\n");
            details.append("  Diagnosis: ").append(selectedLog.getClinicalData().getDiagnosis()).append("\n");
            details.append("  Treatment: ").append(selectedLog.getClinicalData().getTreatment()).append("\n");
            details.append("  Symptoms: ").append(selectedLog.getClinicalData().getSymptoms()).append("\n\n");
        }
        
        if (selectedLog.getVitalSigns() != null) {
            details.append("VITAL SIGNS:\n");
            details.append("  BP: ").append(selectedLog.getVitalSigns().getSystolicBp()).append("/")
                .append(selectedLog.getVitalSigns().getDiastolicBp()).append(" mmHg\n");
            details.append("  Heart Rate: ").append(selectedLog.getVitalSigns().getHeartRate()).append(" bpm\n");
            details.append("  Temperature: ").append(selectedLog.getVitalSigns().getTemperature()).append("°C\n");
            details.append("  O2 Saturation: ").append(selectedLog.getVitalSigns().getOxygenSaturation()).append("%\n\n");
        }
        
        if (selectedLog.getAuditTrail() != null) {
            details.append("AUDIT TRAIL:\n");
            details.append("  Created By: ").append(selectedLog.getAuditTrail().getCreatedBy()).append("\n");
            details.append("  Created At: ").append(selectedLog.getAuditTrail().getCreatedAt()).append("\n");
            details.append("  Version: ").append(selectedLog.getAuditTrail().getVersion()).append("\n");
        }
        
        Alert detailsAlert = new Alert(Alert.AlertType.INFORMATION);
        detailsAlert.setTitle("Medical Log Details");
        detailsAlert.setHeaderText("Complete Medical Log Information");
        
        TextArea textArea = new TextArea(details.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefRowCount(20);
        textArea.setPrefColumnCount(60);
        
        detailsAlert.getDialogPane().setContent(textArea);
        detailsAlert.showAndWait();
    }
    
    /**
     * Refresh all medical logs in table
     */
    private void refreshAllLogs() {
        // Refresh using selected patient if available, otherwise load all
        searchMedicalLogs();
    }
    
    /**
     * Load all medical logs from MongoDB
     */
    private void loadAllLogs() {
        if (medicalLogDAO == null) {
            showAlert("Error", "MongoDB connection not available", Alert.AlertType.ERROR);
            return;
        }
        
        // Load in background thread
        new Thread(() -> {
            try {
                List<PatientMedicalLog> logs = medicalLogDAO.findAll();
                Platform.runLater(() -> {
                    logTableView.setItems(FXCollections.observableArrayList(logs));
                    System.out.println("Loaded " + logs.size() + " medical logs");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showAlert("Error", "Failed to load logs: " + e.getMessage(), Alert.AlertType.ERROR);
                });
            }
        }).start();
    }
    
    /**
     * Validate form inputs
     */
    private boolean validateForm() {
        boolean patientSelected = patientCombo.getValue() != null;
        boolean doctorSelected = doctorCombo.getValue() != null;
        boolean logTypeSelected = logTypeComboBox.getValue() != null;
        boolean notesEntered = !notesTextArea.getText().isEmpty();
        
        System.out.println("DEBUG - Validation Check:");
        System.out.println("  Patient selected: " + patientSelected);
        System.out.println("  Doctor selected: " + doctorSelected);
        System.out.println("  Log type selected: " + logTypeSelected);
        System.out.println("  Notes entered: " + notesEntered);
        
        return patientSelected && doctorSelected && logTypeSelected && notesEntered;
    }
    
    /**
     * Clear the form
     */
    private void clearForm() {
        patientCombo.getSelectionModel().clearSelection();
        doctorCombo.getSelectionModel().clearSelection();
        logTypeComboBox.setValue(null);
        notesTextArea.clear();
        logDetailsListView.setItems(FXCollections.observableArrayList());
        selectedLog = null;
    }
    
    /**
     * Refresh connection status
     */
    private void refreshConnectionStatus() {
        initializeMongoDB();
    }
    
    /**
     * Update connection status label
     */
    private void updateConnectionStatus(boolean isConnected, String message) {
        connectionStatusLabel.setText(message);
        if (isConnected) {
            connectionStatusLabel.setStyle("-fx-text-fill: #00AA00; -fx-font-weight: bold;");
        } else {
            connectionStatusLabel.setStyle("-fx-text-fill: #FF0000; -fx-font-weight: bold;");
        }
    }
    
    /**
     * Show alert dialog
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
