package service;

import dao.DoctorDAO;
import model.Doctor;
import util.SimpleCache;
import java.util.List;

public class DoctorService {
    
    private static final SimpleCache<Integer, Doctor> cache = new SimpleCache<>();
    
    /**
     * Creates a new doctor in the system
     */
    public static boolean createDoctor(Doctor doctor) {
        if (doctor == null || doctor.getFirstName() == null || doctor.getLastName() == null) {
            System.err.println("Invalid doctor data");
            return false;
        }
        boolean result = DoctorDAO.addDoctor(doctor);
        if (result && doctor.getDoctorId() > 0) {
            // Cache the newly created doctor immediately
            cache.put(doctor.getDoctorId(), doctor);
        }
        return result;
    }
    
    /**
     * Retrieves a doctor by ID (with caching)
     */
    public static Doctor getDoctor(int doctorId) {
        if (doctorId <= 0) {
            System.err.println("Invalid doctor ID");
            return null;
        }
        Doctor cached = cache.get(doctorId);
        if (cached != null) {
            return cached;
        }
        Doctor doctor = DoctorDAO.getDoctorById(doctorId);
        if (doctor != null) {
            cache.put(doctorId, doctor);
        }
        return doctor;
    }
    
    /**
     * Retrieves all doctors
     */
    public static List<Doctor> getAllDoctors() {
        return DoctorDAO.getAllDoctors();
    }
    
    /**
     * Retrieves doctors by department
     */
    public static List<Doctor> getDoctorsByDepartment(int departmentId) {
        if (departmentId <= 0) {
            System.err.println("Invalid department ID");
            return null;
        }
        return DoctorDAO.getDoctorsByDepartment(departmentId);
    }
    
    /**
     * Updates an existing doctor
     */
    public static boolean updateDoctor(Doctor doctor) {
        if (doctor == null || doctor.getDoctorId() <= 0) {
            System.err.println("Invalid doctor data for update");
            return false;
        }
        boolean result = DoctorDAO.updateDoctor(doctor);
        if (result) {
            cache.invalidate(doctor.getDoctorId());
        }
        return result;
    }
    
    /**
     * Deletes a doctor
     */
    public static boolean deleteDoctor(int doctorId) {
        if (doctorId <= 0) {
            System.err.println("Invalid doctor ID");
            return false;
        }
        boolean result = DoctorDAO.deleteDoctor(doctorId);
        if (result) {
            cache.invalidate(doctorId);
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
        System.out.println("✓ Doctor cache cleared");
    }
    
    /**
     * Validates doctor information
     */
    public static boolean validateDoctor(Doctor doctor) {
        if (doctor == null) {
            System.err.println("Doctor object is null");
            return false;
        }
        
        if (doctor.getFirstName() == null || doctor.getFirstName().isEmpty()) {
            System.err.println("First name is required");
            return false;
        }
        
        if (doctor.getLastName() == null || doctor.getLastName().isEmpty()) {
            System.err.println("Last name is required");
            return false;
        }
        
        if (doctor.getDepartmentId() <= 0) {
            System.err.println("Valid department ID is required");
            return false;
        }
        
        return true;
    }
}
