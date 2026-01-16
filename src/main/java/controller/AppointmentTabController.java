package controller;

import java.time.LocalDate;
import java.time.LocalTime;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import model.Appointment;
import model.Doctor;
import model.Patient;
import service.AppointmentService;
import service.DoctorService;
import service.PatientService;

/**
 * Controller for Appointment Management tab.
 */
public class AppointmentTabController {

    private TableView<Appointment> appointmentTable;

    public Tab createAppointmentTab() {
        Tab tab = new Tab("Appointment Management");
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Appointment Management");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // Patient & Doctor selection
        ComboBox<Patient> patientCombo = new ComboBox<>();
        patientCombo.setPromptText("Select Patient");
        loadPatients(patientCombo);
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
        loadDoctors(doctorCombo);
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
            loadPatients(patientCombo);
            loadDoctors(doctorCombo);
            patientCombo.getSelectionModel().clearSelection();
            doctorCombo.getSelectionModel().clearSelection();
        };

        DatePicker appointmentDatePicker = new DatePicker();
        appointmentDatePicker.setPromptText("Appointment Date");

        TextField timeField = new TextField();
        timeField.setPromptText("Time (HH:MM)");

        TextField reasonField = new TextField();
        reasonField.setPromptText("Reason for Visit");

        Button reloadDropdownsBtn = new Button("Reload Patients/Doctors");
        reloadDropdownsBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        reloadDropdownsBtn.setOnAction(e -> reloadDropdowns.run());

        Button scheduleBtn = new Button("Schedule Appointment");
        scheduleBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        scheduleBtn.setOnAction(e -> {
            Patient selectedPatient = patientCombo.getValue();
            Doctor selectedDoctor = doctorCombo.getValue();
            if (selectedPatient == null || selectedDoctor == null || appointmentDatePicker.getValue() == null || timeField.getText().isEmpty()) {
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
                    clearForm(patientCombo, doctorCombo, appointmentDatePicker, timeField, reasonField);
                    loadAppointmentData();
                } else {
                    showAlert("Error", "Failed to schedule appointment");
                }
            } catch (Exception ex) {
                showAlert("Error", "Invalid time format (use HH:MM): " + ex.getMessage());
            }
        });

        appointmentTable = new TableView<>();
        appointmentTable.setPrefHeight(400);

        TableColumn<Appointment, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        idCol.setPrefWidth(60);

        TableColumn<Appointment, String> patientNameCol = new TableColumn<>("Patient Name");
        patientNameCol.setCellValueFactory(cellData -> {
            int patientId = cellData.getValue().getPatientId();
            Patient patient = PatientService.getPatient(patientId);
            String name = patient != null ? patient.getFirstName() + " " + patient.getLastName() : "Unknown";
            return new SimpleStringProperty(name);
        });
        patientNameCol.setPrefWidth(180);

        TableColumn<Appointment, String> doctorNameCol = new TableColumn<>("Doctor Name");
        doctorNameCol.setCellValueFactory(cellData -> {
            int doctorId = cellData.getValue().getDoctorId();
            Doctor doctor = DoctorService.getDoctor(doctorId);
            String name = doctor != null ? "Dr. " + doctor.getFirstName() + " " + doctor.getLastName() : "Unknown";
            return new SimpleStringProperty(name);
        });
        doctorNameCol.setPrefWidth(180);

        TableColumn<Appointment, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        dateCol.setPrefWidth(120);

        TableColumn<Appointment, LocalTime> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        timeCol.setPrefWidth(100);

        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(120);

        TableColumn<Appointment, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
        notesCol.setPrefWidth(240);

        appointmentTable.getColumns().addAll(idCol, patientNameCol, doctorNameCol, dateCol, timeCol, statusCol, notesCol);

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
                    .toList());
            }
        });

        Button refreshBtn = new Button("Refresh List");
        refreshBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshBtn.setOnAction(e -> {
            loadAppointmentData();
            searchField.clear();
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
        buttonBox.getChildren().addAll(refreshBtn, deleteBtn);

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

    public void loadAppointmentData() {
        if (appointmentTable != null) {
            appointmentTable.getItems().clear();
            List<Appointment> appointments = AppointmentService.getAllAppointments();
            if (appointments != null) {
                appointmentTable.getItems().addAll(appointments);
            }
        }
    }

    private void clearForm(ComboBox<Patient> patientCombo, ComboBox<Doctor> doctorCombo,
                           DatePicker datePicker, TextField timeField, TextField reasonField) {
        patientCombo.getSelectionModel().clearSelection();
        doctorCombo.getSelectionModel().clearSelection();
        datePicker.setValue(null);
        timeField.clear();
        reasonField.clear();
    }

    private void loadPatients(ComboBox<Patient> combo) {
        combo.getItems().clear();
        List<Patient> patients = PatientService.getAllPatients();
        if (patients != null) {
            combo.getItems().addAll(patients);
        }
    }

    private void loadDoctors(ComboBox<Doctor> combo) {
        combo.getItems().clear();
        List<Doctor> doctors = DoctorService.getAllDoctors();
        if (doctors != null) {
            combo.getItems().addAll(doctors);
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
