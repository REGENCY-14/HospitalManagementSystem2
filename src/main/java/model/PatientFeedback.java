package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientFeedback {
    private int feedbackId;
    private int patientId;
    private int doctorId;
    private int appointmentId;
    private int rating;
    private String comments;
    private LocalDate feedbackDate;
    private LocalDateTime createdAt;

    // Constructor
    public PatientFeedback(int feedbackId, int patientId, int doctorId, int appointmentId,
                          int rating, String comments, LocalDate feedbackDate) {
        this.feedbackId = feedbackId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.rating = rating;
        this.comments = comments;
        this.feedbackDate = feedbackDate;
    }

    public PatientFeedback(int patientId, int doctorId, int appointmentId,
                          int rating, String comments, LocalDate feedbackDate) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.rating = rating;
        this.comments = comments;
        this.feedbackDate = feedbackDate;
    }

    // Getters and Setters
    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        }
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDate getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(LocalDate feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "PatientFeedback{" +
                "feedbackId=" + feedbackId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", appointmentId=" + appointmentId +
                ", rating=" + rating +
                ", comments='" + comments + '\'' +
                ", feedbackDate=" + feedbackDate +
                ", createdAt=" + createdAt +
                '}';
    }
}
