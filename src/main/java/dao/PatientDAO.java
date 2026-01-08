package dao;

import model.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    
    /**
     * Adds a new patient to the database
     */
    public static boolean addPatient(Patient patient) {
        String query = "INSERT INTO Patient (first_name, last_name, date_of_birth, gender, phone, address, blood_type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getLastName());
            stmt.setDate(3, java.sql.Date.valueOf(patient.getDateOfBirth()));
            stmt.setString(4, patient.getGender());
            stmt.setString(5, patient.getPhone());
            stmt.setString(6, patient.getAddress());
            stmt.setString(7, patient.getBloodType());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding patient: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves a patient by ID
     */
    public static Patient getPatientById(int patientId) {
        String query = "SELECT * FROM Patient WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Patient patient = new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("date_of_birth").toLocalDate(),
                    rs.getString("gender"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("blood_type")
                );
                patient.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return patient;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patient: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Retrieves all patients
     */
    public static List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM Patient";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("date_of_birth").toLocalDate(),
                    rs.getString("gender"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("blood_type")
                );
                patient.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                patients.add(patient);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all patients: " + e.getMessage());
        }
        return patients;
    }
    
    /**
     * Updates an existing patient
     */
    public static boolean updatePatient(Patient patient) {
        String query = "UPDATE Patient SET first_name = ?, last_name = ?, date_of_birth = ?, gender = ?, phone = ?, address = ?, blood_type = ? WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getLastName());
            stmt.setDate(3, java.sql.Date.valueOf(patient.getDateOfBirth()));
            stmt.setString(4, patient.getGender());
            stmt.setString(5, patient.getPhone());
            stmt.setString(6, patient.getAddress());
            stmt.setString(7, patient.getBloodType());
            stmt.setInt(8, patient.getPatientId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating patient: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes a patient
     */
    public static boolean deletePatient(int patientId) {
        String query = "DELETE FROM Patient WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, patientId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting patient: " + e.getMessage());
            return false;
        }
    }
}
