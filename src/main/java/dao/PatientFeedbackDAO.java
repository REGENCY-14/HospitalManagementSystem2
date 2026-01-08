package dao;

import model.PatientFeedback;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientFeedbackDAO {
    
    /**
     * Adds new feedback
     */
    public static boolean addFeedback(PatientFeedback feedback) {
        String query = "INSERT INTO PatientFeedback (patient_id, doctor_id, appointment_id, rating, comments, feedback_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, feedback.getPatientId());
            stmt.setInt(2, feedback.getDoctorId());
            stmt.setInt(3, feedback.getAppointmentId());
            stmt.setInt(4, feedback.getRating());
            stmt.setString(5, feedback.getComments());
            stmt.setDate(6, java.sql.Date.valueOf(feedback.getFeedbackDate()));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding feedback: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves feedback by ID
     */
    public static PatientFeedback getFeedbackById(int feedbackId) {
        String query = "SELECT * FROM PatientFeedback WHERE feedback_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, feedbackId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                PatientFeedback feedback = new PatientFeedback(
                    rs.getInt("feedback_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getInt("appointment_id"),
                    rs.getInt("rating"),
                    rs.getString("comments"),
                    rs.getDate("feedback_date").toLocalDate()
                );
                feedback.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return feedback;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving feedback: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Retrieves all feedback
     */
    public static List<PatientFeedback> getAllFeedback() {
        List<PatientFeedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM PatientFeedback";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                PatientFeedback feedback = new PatientFeedback(
                    rs.getInt("feedback_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getInt("appointment_id"),
                    rs.getInt("rating"),
                    rs.getString("comments"),
                    rs.getDate("feedback_date").toLocalDate()
                );
                feedback.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all feedback: " + e.getMessage());
        }
        return feedbackList;
    }
    
    /**
     * Retrieves feedback by patient ID
     */
    public static List<PatientFeedback> getFeedbackByPatient(int patientId) {
        List<PatientFeedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM PatientFeedback WHERE patient_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                PatientFeedback feedback = new PatientFeedback(
                    rs.getInt("feedback_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getInt("appointment_id"),
                    rs.getInt("rating"),
                    rs.getString("comments"),
                    rs.getDate("feedback_date").toLocalDate()
                );
                feedback.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving feedback by patient: " + e.getMessage());
        }
        return feedbackList;
    }
    
    /**
     * Updates feedback
     */
    public static boolean updateFeedback(PatientFeedback feedback) {
        String query = "UPDATE PatientFeedback SET patient_id = ?, doctor_id = ?, appointment_id = ?, rating = ?, comments = ?, feedback_date = ? WHERE feedback_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, feedback.getPatientId());
            stmt.setInt(2, feedback.getDoctorId());
            stmt.setInt(3, feedback.getAppointmentId());
            stmt.setInt(4, feedback.getRating());
            stmt.setString(5, feedback.getComments());
            stmt.setDate(6, java.sql.Date.valueOf(feedback.getFeedbackDate()));
            stmt.setInt(7, feedback.getFeedbackId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating feedback: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes feedback
     */
    public static boolean deleteFeedback(int feedbackId) {
        String query = "DELETE FROM PatientFeedback WHERE feedback_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, feedbackId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting feedback: " + e.getMessage());
            return false;
        }
    }
}
