package dao;

import model.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {
    
    /**
     * Adds a new doctor
     */
    public static boolean addDoctor(Doctor doctor) {
        String query = "INSERT INTO Doctor (first_name, last_name, specialization, phone, department_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, doctor.getFirstName());
            stmt.setString(2, doctor.getLastName());
            stmt.setString(3, doctor.getSpecialization());
            stmt.setString(4, doctor.getPhone());
            stmt.setInt(5, doctor.getDepartmentId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding doctor: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves a doctor by ID
     */
    public static Doctor getDoctorById(int doctorId) {
        String query = "SELECT * FROM Doctor WHERE doctor_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            System.err.println("Warning: Database connection is null. Returning null doctor.");
            return null;
        }
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("specialization"),
                    rs.getString("phone"),
                    rs.getInt("department_id")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving doctor: " + e.getMessage());
        } finally {
            try { conn.close(); } catch (SQLException ignore) {}
        }
        return null;
    }
    
    /**
     * Retrieves all doctors
     */
    public static List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM Doctor";
        
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            System.err.println("Warning: Database connection is null. Returning empty doctor list.");
            return doctors;
        }
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                doctors.add(new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("specialization"),
                    rs.getString("phone"),
                    rs.getInt("department_id")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all doctors: " + e.getMessage());
        } finally {
            try { conn.close(); } catch (SQLException ignore) {}
        }
        return doctors;
    }
    
    /**
     * Retrieves doctors by department
     */
    public static List<Doctor> getDoctorsByDepartment(int departmentId) {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM Doctor WHERE department_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, departmentId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                doctors.add(new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("specialization"),
                    rs.getString("phone"),
                    rs.getInt("department_id")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving doctors by department: " + e.getMessage());
        }
        return doctors;
    }
    
    /**
     * Updates a doctor
     */
    public static boolean updateDoctor(Doctor doctor) {
        String query = "UPDATE Doctor SET first_name = ?, last_name = ?, specialization = ?, phone = ?, department_id = ? WHERE doctor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, doctor.getFirstName());
            stmt.setString(2, doctor.getLastName());
            stmt.setString(3, doctor.getSpecialization());
            stmt.setString(4, doctor.getPhone());
            stmt.setInt(5, doctor.getDepartmentId());
            stmt.setInt(6, doctor.getDoctorId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating doctor: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes a doctor
     */
    public static boolean deleteDoctor(int doctorId) {
        String query = "DELETE FROM Doctor WHERE doctor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, doctorId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting doctor: " + e.getMessage());
            return false;
        }
    }
}
