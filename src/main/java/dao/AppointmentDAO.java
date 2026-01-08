package dao;

import model.Appointment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    
    /**
     * Adds a new appointment
     */
    public static boolean addAppointment(Appointment appointment) {
        String query = "INSERT INTO Appointment (patient_id, doctor_id, appointment_date, appointment_time, status, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setDate(3, java.sql.Date.valueOf(appointment.getAppointmentDate()));
            stmt.setTime(4, java.sql.Time.valueOf(appointment.getAppointmentTime()));
            stmt.setString(5, appointment.getStatus());
            stmt.setString(6, appointment.getNotes());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding appointment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves an appointment by ID
     */
    public static Appointment getAppointmentById(int appointmentId) {
        String query = "SELECT * FROM Appointment WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Appointment appointment = new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getDate("appointment_date").toLocalDate(),
                    rs.getTime("appointment_time").toLocalTime(),
                    rs.getString("status"),
                    rs.getString("notes")
                );
                appointment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return appointment;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving appointment: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Retrieves all appointments
     */
    public static List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM Appointment";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Appointment appointment = new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getDate("appointment_date").toLocalDate(),
                    rs.getTime("appointment_time").toLocalTime(),
                    rs.getString("status"),
                    rs.getString("notes")
                );
                appointment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all appointments: " + e.getMessage());
        }
        return appointments;
    }
    
    /**
     * Retrieves appointments by patient ID
     */
    public static List<Appointment> getAppointmentsByPatient(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM Appointment WHERE patient_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Appointment appointment = new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getDate("appointment_date").toLocalDate(),
                    rs.getTime("appointment_time").toLocalTime(),
                    rs.getString("status"),
                    rs.getString("notes")
                );
                appointment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving appointments by patient: " + e.getMessage());
        }
        return appointments;
    }
    
    /**
     * Updates an appointment
     */
    public static boolean updateAppointment(Appointment appointment) {
        String query = "UPDATE Appointment SET patient_id = ?, doctor_id = ?, appointment_date = ?, appointment_time = ?, status = ?, notes = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setDate(3, java.sql.Date.valueOf(appointment.getAppointmentDate()));
            stmt.setTime(4, java.sql.Time.valueOf(appointment.getAppointmentTime()));
            stmt.setString(5, appointment.getStatus());
            stmt.setString(6, appointment.getNotes());
            stmt.setInt(7, appointment.getAppointmentId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating appointment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes an appointment
     */
    public static boolean deleteAppointment(int appointmentId) {
        String query = "DELETE FROM Appointment WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, appointmentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting appointment: " + e.getMessage());
            return false;
        }
    }
}
