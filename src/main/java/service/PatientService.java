package service;

import dao.PatientDAO;
import model.Patient;
import util.SimpleCache;
import java.util.List;

public class PatientService {
    
    private static final SimpleCache<Integer, Patient> cache = new SimpleCache<>();
    
    /**
     * Creates a new patient in the system
     */
    public static boolean createPatient(Patient patient) {
        if (patient == null || patient.getFirstName() == null || patient.getLastName() == null) {
            System.err.println("Invalid patient data");
            return false;
        }
        boolean result = PatientDAO.addPatient(patient);
        if (result && patient.getPatientId() > 0) {
            // Cache the newly created patient immediately
            cache.put(patient.getPatientId(), patient);
        }
        return result;
    }
    
    /**
     * Retrieves a patient by ID (with caching)
     */
    public static Patient getPatient(int patientId) {
        if (patientId <= 0) {
            System.err.println("Invalid patient ID");
            return null;
        }
        Patient cached = cache.get(patientId);
        if (cached != null) {
            return cached;
        }
        Patient patient = PatientDAO.getPatientById(patientId);
        if (patient != null) {
            cache.put(patientId, patient);
        }
        return patient;
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
        boolean result = PatientDAO.updatePatient(patient);
        if (result) {
            cache.invalidate(patient.getPatientId());
        }
        return result;
    }
    
    /**
     * Deletes a patient
     */
    public static boolean deletePatient(int patientId) {
        if (patientId <= 0) {
            System.err.println("Invalid patient ID");
            return false;
        }
        boolean result = PatientDAO.deletePatient(patientId);
        if (result) {
            cache.invalidate(patientId);
        }
        return result;
    }
    
    /**
     * Gets cache statistics
     */
    public static SimpleCache.Stats getCacheStats() {
        return cache.stats();
    }
    
    /**
     * Gets cache size
     */
    public static long getCacheSize() {
        return cache.size();
    }
    
    /**
     * Clears the cache
     */
    public static void clearCache() {
        cache.clear();
        System.out.println("✓ Patient cache cleared");
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
