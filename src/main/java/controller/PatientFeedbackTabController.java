package controller;

import java.time.LocalDate;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Doctor;
import model.Patient;
import model.PatientFeedback;
import service.AppointmentService;
import service.DoctorService;
import service.PatientFeedbackService;
import service.PatientService;

/**
 * Controller for Patient Feedback tab.
 */
public class PatientFeedbackTabController {

    private TableView<PatientFeedback> feedbackTable;

    public Tab createPatientFeedbackTab() {
        Tab tab = new Tab("Patient Feedback");
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Patient Feedback Management");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        ComboBox<String> patientCombo = new ComboBox<>();
        patientCombo.setPromptText("Select Patient");
        patientCombo.setPrefWidth(150);
        populatePatientCombo(patientCombo);

        ComboBox<String> doctorCombo = new ComboBox<>();
        doctorCombo.setPromptText("Select Doctor (Optional)");
        doctorCombo.setPrefWidth(150);
        populateDoctorCombo(doctorCombo);

        ComboBox<String> appointmentCombo = new ComboBox<>();
        appointmentCombo.setPromptText("Select Appointment (Optional)");
        appointmentCombo.setPrefWidth(200);
        populateAppointmentCombo(appointmentCombo);

        ComboBox<Integer> ratingCombo = new ComboBox<>();
        ratingCombo.getItems().addAll(1, 2, 3, 4, 5);
        ratingCombo.setPromptText("Rating (1-5)");
        TextArea commentsArea = new TextArea();
        commentsArea.setPromptText("Comments");
        commentsArea.setPrefRowCount(3);
        DatePicker datePicker = new DatePicker(LocalDate.now());

        feedbackTable = new TableView<>();
        feedbackTable.setPrefHeight(400);

        TableColumn<PatientFeedback, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("feedbackId"));
        idCol.setPrefWidth(60);

        TableColumn<PatientFeedback, String> patientCol = new TableColumn<>("Patient Name");
        patientCol.setCellValueFactory(cellData -> {
            int patientId = cellData.getValue().getPatientId();
            Patient patient = PatientService.getPatient(patientId);
            String name = patient != null ? patient.getFirstName() + " " + patient.getLastName() : "Unknown";
            return new SimpleStringProperty(name);
        });
        patientCol.setPrefWidth(150);

        TableColumn<PatientFeedback, String> doctorCol = new TableColumn<>("Doctor Name");
        doctorCol.setCellValueFactory(cellData -> {
            int doctorId = cellData.getValue().getDoctorId();
            if (doctorId == 0) {
                return new SimpleStringProperty("N/A");
            }
            Doctor doctor = DoctorService.getDoctor(doctorId);
            String name = doctor != null ? "Dr. " + doctor.getFirstName() + " " + doctor.getLastName() : "Unknown";
            return new SimpleStringProperty(name);
        });
        doctorCol.setPrefWidth(150);

        TableColumn<PatientFeedback, Integer> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingCol.setPrefWidth(80);

        TableColumn<PatientFeedback, String> commentsCol = new TableColumn<>("Comments");
        commentsCol.setCellValueFactory(new PropertyValueFactory<>("comments"));
        commentsCol.setPrefWidth(300);

        TableColumn<PatientFeedback, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("feedbackDate"));
        dateCol.setPrefWidth(120);

        feedbackTable.getColumns().addAll(idCol, patientCol, doctorCol, ratingCol, commentsCol, dateCol);

        Button refreshBtn = new Button("Refresh List");
        refreshBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshBtn.setOnAction(e -> loadFeedbackData());

        Button addBtn = new Button("Add Feedback");
        addBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        addBtn.setOnAction(e -> {
            try {
                if (patientCombo.getValue() == null || ratingCombo.getValue() == null) {
                    showAlert("Error", "Please select Patient and Rating");
                    return;
                }
                Integer patientId = extractIdFromCombo(patientCombo.getValue());
                Integer doctorId = doctorCombo.getValue() != null ? extractIdFromCombo(doctorCombo.getValue()) : null;
                Integer appointmentId = appointmentCombo.getValue() != null ? extractIdFromCombo(appointmentCombo.getValue()) : null;
                PatientFeedback feedback = new PatientFeedback(
                    patientId,
                    doctorId != null ? doctorId : 0,
                    appointmentId != null ? appointmentId : 0,
                    ratingCombo.getValue(),
                    commentsArea.getText(),
                    datePicker.getValue()
                );
                if (PatientFeedbackService.createFeedback(feedback)) {
                    showAlert("Success", "Feedback added!");
                    patientCombo.setValue(null);
                    doctorCombo.setValue(null);
                    appointmentCombo.setValue(null);
                    ratingCombo.setValue(null);
                    commentsArea.clear();
                    loadFeedbackData();
                } else {
                    showAlert("Error", "Failed to add feedback");
                }
            } catch (Exception ex) {
                showAlert("Error", "Invalid input: " + ex.getMessage());
            }
        });

        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        deleteBtn.setOnAction(e -> {
            PatientFeedback selected = feedbackTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (PatientFeedbackService.deleteFeedback(selected.getFeedbackId())) {
                    showAlert("Success", "Feedback deleted!");
                    feedbackTable.getItems().remove(selected);
                } else {
                    showAlert("Error", "Failed to delete");
                }
            }
        });

        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(patientCombo, doctorCombo, appointmentCombo, ratingCombo, datePicker, addBtn);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(refreshBtn, deleteBtn);

        root.getChildren().addAll(
            titleLabel,
            new Separator(),
            new Label("Add New Feedback:"),
            inputBox,
            commentsArea,
            new Separator(),
            new Label("Feedback List:"),
            buttonBox,
            feedbackTable
        );

        tab.setContent(root);
        return tab;
    }

    public void loadFeedbackData() {
        if (feedbackTable != null) {
            feedbackTable.getItems().clear();
            List<PatientFeedback> feedbacks = PatientFeedbackService.getAllFeedback();
            if (feedbacks != null) {
                feedbackTable.getItems().addAll(feedbacks);
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
