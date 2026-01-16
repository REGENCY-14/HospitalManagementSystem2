package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Doctor;
import model.Patient;
import model.Prescription;
import model.PrescriptionItem;
import service.AppointmentService;
import service.DoctorService;
import service.PatientService;
import service.PrescriptionItemService;
import service.PrescriptionService;

/**
 * Controller for Prescription Management tab.
 */
public class PrescriptionTabController {

    private TableView<Prescription> prescriptionTable;

    public Tab createPrescriptionTab() {
        Tab tab = new Tab("Prescriptions");
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Prescription Management");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        ComboBox<String> patientCombo = new ComboBox<>();
        patientCombo.setPromptText("Select Patient");
        patientCombo.setPrefWidth(150);
        populatePatientCombo(patientCombo);

        ComboBox<String> doctorCombo = new ComboBox<>();
        doctorCombo.setPromptText("Select Doctor");
        doctorCombo.setPrefWidth(150);
        populateDoctorCombo(doctorCombo);

        ComboBox<String> appointmentCombo = new ComboBox<>();
        appointmentCombo.setPromptText("Select Appointment (Optional)");
        appointmentCombo.setPrefWidth(200);
        populateAppointmentCombo(appointmentCombo);

        TextField diagnosisField = new TextField();
        diagnosisField.setPromptText("Diagnosis");
        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Notes");
        notesArea.setPrefRowCount(2);
        DatePicker datePicker = new DatePicker(LocalDate.now());

        prescriptionTable = new TableView<>();
        prescriptionTable.setPrefHeight(300);

        TableColumn<Prescription, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("prescriptionId"));
        idCol.setPrefWidth(60);

        TableColumn<Prescription, String> patientCol = new TableColumn<>("Patient Name");
        patientCol.setCellValueFactory(cellData -> {
            int patientId = cellData.getValue().getPatientId();
            Patient patient = PatientService.getPatient(patientId);
            String name = patient != null ? patient.getFirstName() + " " + patient.getLastName() : "Unknown";
            return new SimpleStringProperty(name);
        });
        patientCol.setPrefWidth(150);

        TableColumn<Prescription, String> doctorCol = new TableColumn<>("Doctor Name");
        doctorCol.setCellValueFactory(cellData -> {
            int doctorId = cellData.getValue().getDoctorId();
            Doctor doctor = DoctorService.getDoctor(doctorId);
            String name = doctor != null ? "Dr. " + doctor.getFirstName() + " " + doctor.getLastName() : "Unknown";
            return new SimpleStringProperty(name);
        });
        doctorCol.setPrefWidth(150);

        TableColumn<Prescription, String> diagnosisCol = new TableColumn<>("Diagnosis");
        diagnosisCol.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        diagnosisCol.setPrefWidth(200);

        TableColumn<Prescription, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("prescriptionDate"));
        dateCol.setPrefWidth(120);

        prescriptionTable.getColumns().addAll(idCol, patientCol, doctorCol, diagnosisCol, dateCol);

        TableView<PrescriptionItem> itemsTable = new TableView<>();
        itemsTable.setPrefHeight(200);

        TableColumn<PrescriptionItem, Integer> itemIdCol = new TableColumn<>("Item ID");
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("prescriptionItemId"));
        itemIdCol.setPrefWidth(60);

        TableColumn<PrescriptionItem, Integer> inventoryCol = new TableColumn<>("Inventory ID");
        inventoryCol.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        inventoryCol.setPrefWidth(100);

        TableColumn<PrescriptionItem, String> dosageCol = new TableColumn<>("Dosage");
        dosageCol.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        dosageCol.setPrefWidth(100);

        TableColumn<PrescriptionItem, String> frequencyCol = new TableColumn<>("Frequency");
        frequencyCol.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        frequencyCol.setPrefWidth(100);

        TableColumn<PrescriptionItem, String> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationCol.setPrefWidth(100);

        TableColumn<PrescriptionItem, Integer> quantityCol = new TableColumn<>("Qty");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setPrefWidth(60);

        itemsTable.getColumns().addAll(itemIdCol, inventoryCol, dosageCol, frequencyCol, durationCol, quantityCol);

        TextField inventoryIdField = new TextField();
        inventoryIdField.setPromptText("Inventory ID");
        TextField dosageField = new TextField();
        dosageField.setPromptText("Dosage");
        TextField frequencyField = new TextField();
        frequencyField.setPromptText("Frequency");
        TextField durationField = new TextField();
        durationField.setPromptText("Duration");
        TextField itemQuantityField = new TextField();
        itemQuantityField.setPromptText("Quantity");

        Button addBtn = new Button("Add Prescription");
        addBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");

        Button refreshBtn = new Button("Refresh Prescriptions");
        refreshBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshBtn.setOnAction(e -> {
            loadPrescriptionData();
            itemsTable.getItems().clear();
        });

        Button deleteBtn = new Button("Delete Selected Prescription");
        deleteBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        deleteBtn.setOnAction(e -> {
            Prescription selected = prescriptionTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (PrescriptionService.deletePrescription(selected.getPrescriptionId())) {
                    showAlert("Success", "Prescription deleted!");
                    prescriptionTable.getItems().remove(selected);
                    itemsTable.getItems().clear();
                } else {
                    showAlert("Error", "Failed to delete");
                }
            } else {
                showAlert("Error", "Please select a prescription");
            }
        });

        Button addItemBtn = new Button("Add Item to Prescription");
        addItemBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        addItemBtn.setOnAction(e -> {
            Prescription selected = prescriptionTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "Please select a prescription first");
                return;
            }
            try {
                PrescriptionItem item = new PrescriptionItem(
                    selected.getPrescriptionId(),
                    Integer.parseInt(inventoryIdField.getText()),
                    dosageField.getText(),
                    frequencyField.getText(),
                    durationField.getText(),
                    Integer.parseInt(itemQuantityField.getText())
                );
                if (PrescriptionItemService.createPrescriptionItem(item)) {
                    showAlert("Success", "Item added to prescription!");
                    inventoryIdField.clear();
                    dosageField.clear();
                    frequencyField.clear();
                    durationField.clear();
                    itemQuantityField.clear();
                    List<PrescriptionItem> items = PrescriptionItemService.getItemsByPrescription(selected.getPrescriptionId());
                    itemsTable.getItems().setAll(items != null ? items : new ArrayList<>());
                } else {
                    showAlert("Error", "Failed to add item");
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid number format");
            }
        });

        Button deleteItemBtn = new Button("Remove Item");
        deleteItemBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        deleteItemBtn.setOnAction(e -> {
            PrescriptionItem selected = itemsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (PrescriptionItemService.deletePrescriptionItem(selected.getPrescriptionItemId())) {
                    showAlert("Success", "Item removed!");
                    itemsTable.getItems().remove(selected);
                } else {
                    showAlert("Error", "Failed to remove item");
                }
            } else {
                showAlert("Error", "Please select an item to remove");
            }
        });

        prescriptionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                List<PrescriptionItem> items = PrescriptionItemService.getItemsByPrescription(newVal.getPrescriptionId());
                itemsTable.getItems().setAll(items != null ? items : new ArrayList<>());
            } else {
                itemsTable.getItems().clear();
            }
        });

        addBtn.setOnAction(e -> {
            try {
                if (patientCombo.getValue() == null || doctorCombo.getValue() == null) {
                    showAlert("Error", "Please select Patient and Doctor");
                    return;
                }
                Integer patientId = extractIdFromCombo(patientCombo.getValue());
                Integer doctorId = extractIdFromCombo(doctorCombo.getValue());
                Integer appointmentId = appointmentCombo.getValue() != null ? extractIdFromCombo(appointmentCombo.getValue()) : null;
                Prescription prescription = new Prescription(
                    patientId,
                    doctorId,
                    appointmentId != null ? appointmentId : 0,
                    datePicker.getValue(),
                    diagnosisField.getText(),
                    notesArea.getText()
                );
                if (PrescriptionService.createPrescription(prescription)) {
                    showAlert("Success", "Prescription added!");
                    patientCombo.setValue(null);
                    doctorCombo.setValue(null);
                    appointmentCombo.setValue(null);
                    diagnosisField.clear();
                    notesArea.clear();
                    loadPrescriptionData();
                } else {
                    showAlert("Error", "Failed to add prescription");
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid input format");
            }
        });

        HBox inputBox1 = new HBox(10);
        inputBox1.getChildren().addAll(patientCombo, doctorCombo, appointmentCombo, datePicker);
        HBox inputBox2 = new HBox(10);
        inputBox2.getChildren().addAll(diagnosisField, addBtn);
        VBox inputSection = new VBox(10);
        inputSection.getChildren().addAll(inputBox1, inputBox2, notesArea);

        HBox prescriptionButtonBox = new HBox(10);
        prescriptionButtonBox.getChildren().addAll(refreshBtn, deleteBtn);

        HBox itemInputBox = new HBox(10);
        itemInputBox.getChildren().addAll(inventoryIdField, dosageField, frequencyField, durationField, itemQuantityField, addItemBtn);

        HBox itemButtonBox = new HBox(10);
        itemButtonBox.getChildren().addAll(deleteItemBtn);

        root.getChildren().addAll(
            titleLabel,
            new Separator(),
            new Label("Add New Prescription:"),
            inputSection,
            new Separator(),
            new Label("Prescription List:"),
            prescriptionButtonBox,
            prescriptionTable,
            new Separator(),
            new Label("Prescription Items:"),
            itemInputBox,
            itemButtonBox,
            itemsTable
        );

        tab.setContent(root);
        return tab;
    }

    public void loadPrescriptionData() {
        if (prescriptionTable != null) {
            prescriptionTable.getItems().clear();
            List<Prescription> prescriptions = PrescriptionService.getAllPrescriptions();
            if (prescriptions != null) {
                prescriptionTable.getItems().addAll(prescriptions);
            }
        }
    }

    private void populatePatientCombo(ComboBox<String> combo) {
        combo.getItems().clear();
        List<Patient> patients = PatientService.getAllPatients();
        if (patients != null) {
            for (Patient p : patients) {
                combo.getItems().add(p.getPatientId() + " - " + p.getFirstName() + " " + p.getLastName());
            }
        }
    }

    private void populateDoctorCombo(ComboBox<String> combo) {
        combo.getItems().clear();
        List<Doctor> doctors = DoctorService.getAllDoctors();
        if (doctors != null) {
            for (Doctor d : doctors) {
                combo.getItems().add(d.getDoctorId() + " - Dr. " + d.getFirstName() + " " + d.getLastName());
            }
        }
    }

    private void populateAppointmentCombo(ComboBox<String> combo) {
        combo.getItems().clear();
        List<model.Appointment> appointments = AppointmentService.getAllAppointments();
        if (appointments != null) {
            for (model.Appointment a : appointments) {
                Patient p = PatientService.getPatient(a.getPatientId());
                Doctor d = DoctorService.getDoctor(a.getDoctorId());
                String patientName = (p != null) ? p.getFirstName() + " " + p.getLastName() : "Unknown";
                String doctorName = (d != null) ? "Dr. " + d.getFirstName() + " " + d.getLastName() : "Unknown";
                combo.getItems().add(a.getAppointmentId() + " - " + patientName + " with " + doctorName + " (" + a.getAppointmentDate() + ")");
            }
        }
    }

    private Integer extractIdFromCombo(String comboValue) {
        if (comboValue == null || comboValue.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(comboValue.split(" - ")[0]);
        } catch (Exception e) {
            return null;
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
