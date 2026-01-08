package service;

import dao.PatientDAO;
import model.Patient;
import java.util.List;

public class PatientService {
    
    /**
     * Creates a new patient in the system
     */
    public static boolean createPatient(Patient patient) {
        if (patient == null || patient.getFirstName() == null || patient.getLastName() == null) {
            System.err.println("Invalid patient data");
            return false;
        }
        return PatientDAO.addPatient(patient);
    }
    
    /**
     * Retrieves a patient by ID
     */
    public static Patient getPatient(int patientId) {
        if (patientId <= 0) {
            System.err.println("Invalid patient ID");
            return null;
        }
        return PatientDAO.getPatientById(patientId);
    }
    
    /**
     * Retrieves all patients
     */
    public static List<Patient> getAllPatients() {
        return PatientDAO.getAllPatients();
    }
    
    /**
     * Updates an existing patient
     */
    public static boolean updatePatient(Patient patient) {
        if (patient == null || patient.getPatientId() <= 0) {
            System.err.println("Invalid patient data for update");
            return false;
        }
        return PatientDAO.updatePatient(patient);
    }
    
    /**
     * Deletes a patient
     */
    public static boolean deletePatient(int patientId) {
        if (patientId <= 0) {
            System.err.println("Invalid patient ID");
            return false;
        }
        return PatientDAO.deletePatient(patientId);
    }
    
    /**
     * Validates patient information
     */
    public static boolean validatePatient(Patient patient) {
        if (patient == null) {
            System.err.println("Patient object is null");
            return false;
        }
        
        if (patient.getFirstName() == null || patient.getFirstName().isEmpty()) {
            System.err.println("First name is required");
            return false;
        }
        
        if (patient.getLastName() == null || patient.getLastName().isEmpty()) {
            System.err.println("Last name is required");
            return false;
        }
        
        if (patient.getDateOfBirth() == null) {
            System.err.println("Date of birth is required");
            return false;
        }
        
        return true;
    }
}
