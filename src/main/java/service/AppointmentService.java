package service;

import dao.AppointmentDAO;
import model.Appointment;
import java.util.List;

public class AppointmentService {
    
    /**
     * Creates a new appointment
     */
    public static boolean createAppointment(Appointment appointment) {
        if (appointment == null || appointment.getPatientId() <= 0 || appointment.getDoctorId() <= 0) {
            System.err.println("Invalid appointment data");
            return false;
        }
        return AppointmentDAO.addAppointment(appointment);
    }
    
    /**
     * Retrieves an appointment by ID
     */
    public static Appointment getAppointment(int appointmentId) {
        if (appointmentId <= 0) {
            System.err.println("Invalid appointment ID");
            return null;
        }
        return AppointmentDAO.getAppointmentById(appointmentId);
    }
    
    /**
     * Retrieves all appointments
     */
    public static List<Appointment> getAllAppointments() {
        return AppointmentDAO.getAllAppointments();
    }
    
    /**
     * Retrieves appointments for a patient
     */
    public static List<Appointment> getPatientAppointments(int patientId) {
        if (patientId <= 0) {
            System.err.println("Invalid patient ID");
            return null;
        }
        return AppointmentDAO.getAppointmentsByPatient(patientId);
    }
    
    /**
     * Updates an appointment
     */
    public static boolean updateAppointment(Appointment appointment) {
        if (appointment == null || appointment.getAppointmentId() <= 0) {
            System.err.println("Invalid appointment data for update");
            return false;
        }
        return AppointmentDAO.updateAppointment(appointment);
    }
    
    /**
     * Deletes an appointment
     */
    public static boolean deleteAppointment(int appointmentId) {
        if (appointmentId <= 0) {
            System.err.println("Invalid appointment ID");
            return false;
        }
        return AppointmentDAO.deleteAppointment(appointmentId);
    }
    
    /**
     * Cancels an appointment by updating its status
     */
    public static boolean cancelAppointment(int appointmentId) {
        Appointment appointment = AppointmentDAO.getAppointmentById(appointmentId);
        if (appointment == null) {
            System.err.println("Appointment not found");
            return false;
        }
        appointment.setStatus("Cancelled");
        return AppointmentDAO.updateAppointment(appointment);
    }
    
    /**
     * Validates appointment information
     */
    public static boolean validateAppointment(Appointment appointment) {
        if (appointment == null) {
            System.err.println("Appointment object is null");
            return false;
        }
        
        if (appointment.getPatientId() <= 0) {
            System.err.println("Valid patient ID is required");
            return false;
        }
        
        if (appointment.getDoctorId() <= 0) {
            System.err.println("Valid doctor ID is required");
            return false;
        }
        
        if (appointment.getAppointmentDate() == null) {
            System.err.println("Appointment date is required");
            return false;
        }
        
        if (appointment.getAppointmentTime() == null) {
            System.err.println("Appointment time is required");
            return false;
        }
        
        return true;
    }
}
