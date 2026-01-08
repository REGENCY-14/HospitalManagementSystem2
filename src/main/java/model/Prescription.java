package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Prescription {
    private int prescriptionId;
    private int patientId;
    private int doctorId;
    private int appointmentId;
    private LocalDate prescriptionDate;
    private String diagnosis;
    private String notes;
    private LocalDateTime createdAt;

    // Constructor
    public Prescription(int prescriptionId, int patientId, int doctorId, int appointmentId,
                       LocalDate prescriptionDate, String diagnosis, String notes) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.prescriptionDate = prescriptionDate;
        this.diagnosis = diagnosis;
        this.notes = notes;
    }

    public Prescription(int patientId, int doctorId, int appointmentId,
                       LocalDate prescriptionDate, String diagnosis, String notes) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.prescriptionDate = prescriptionDate;
        this.diagnosis = diagnosis;
        this.notes = notes;
    }

    // Getters and Setters
    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LocalDate getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(LocalDate prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionId=" + prescriptionId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", appointmentId=" + appointmentId +
                ", prescriptionDate=" + prescriptionDate +
                ", diagnosis='" + diagnosis + '\'' +
                ", notes='" + notes + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
