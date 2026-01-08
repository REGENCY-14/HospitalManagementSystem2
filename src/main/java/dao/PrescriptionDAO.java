package dao;

import model.Prescription;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {
    
    /**
     * Adds a new prescription
     */
    public static boolean addPrescription(Prescription prescription) {
        String query = "INSERT INTO Prescription (patient_id, doctor_id, appointment_id, prescription_date, diagnosis, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, prescription.getPatientId());
            stmt.setInt(2, prescription.getDoctorId());
            stmt.setInt(3, prescription.getAppointmentId());
            stmt.setDate(4, java.sql.Date.valueOf(prescription.getPrescriptionDate()));
            stmt.setString(5, prescription.getDiagnosis());
            stmt.setString(6, prescription.getNotes());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding prescription: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves a prescription by ID
     */
    public static Prescription getPrescriptionById(int prescriptionId) {
        String query = "SELECT * FROM Prescription WHERE prescription_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, prescriptionId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Prescription prescription = new Prescription(
                    rs.getInt("prescription_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getInt("appointment_id"),
                    rs.getDate("prescription_date").toLocalDate(),
                    rs.getString("diagnosis"),
                    rs.getString("notes")
                );
                prescription.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return prescription;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving prescription: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Retrieves all prescriptions
     */
    public static List<Prescription> getAllPrescriptions() {
        List<Prescription> prescriptions = new ArrayList<>();
        String query = "SELECT * FROM Prescription";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Prescription prescription = new Prescription(
                    rs.getInt("prescription_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getInt("appointment_id"),
                    rs.getDate("prescription_date").toLocalDate(),
                    rs.getString("diagnosis"),
                    rs.getString("notes")
                );
                prescription.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                prescriptions.add(prescription);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all prescriptions: " + e.getMessage());
        }
        return prescriptions;
    }
    
    /**
     * Retrieves prescriptions by patient ID
     */
    public static List<Prescription> getPrescriptionsByPatient(int patientId) {
        List<Prescription> prescriptions = new ArrayList<>();
        String query = "SELECT * FROM Prescription WHERE patient_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Prescription prescription = new Prescription(
                    rs.getInt("prescription_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getInt("appointment_id"),
                    rs.getDate("prescription_date").toLocalDate(),
                    rs.getString("diagnosis"),
                    rs.getString("notes")
                );
                prescription.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                prescriptions.add(prescription);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving prescriptions by patient: " + e.getMessage());
        }
        return prescriptions;
    }
    
    /**
     * Updates a prescription
     */
    public static boolean updatePrescription(Prescription prescription) {
        String query = "UPDATE Prescription SET patient_id = ?, doctor_id = ?, appointment_id = ?, prescription_date = ?, diagnosis = ?, notes = ? WHERE prescription_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, prescription.getPatientId());
            stmt.setInt(2, prescription.getDoctorId());
            stmt.setInt(3, prescription.getAppointmentId());
            stmt.setDate(4, java.sql.Date.valueOf(prescription.getPrescriptionDate()));
            stmt.setString(5, prescription.getDiagnosis());
            stmt.setString(6, prescription.getNotes());
            stmt.setInt(7, prescription.getPrescriptionId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating prescription: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes a prescription
     */
    public static boolean deletePrescription(int prescriptionId) {
        String query = "DELETE FROM Prescription WHERE prescription_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, prescriptionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting prescription: " + e.getMessage());
            return false;
        }
    }
}
