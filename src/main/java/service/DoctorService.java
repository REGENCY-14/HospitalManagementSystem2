package service;

import dao.DoctorDAO;
import model.Doctor;
import java.util.List;

public class DoctorService {
    
    /**
     * Creates a new doctor in the system
     */
    public static boolean createDoctor(Doctor doctor) {
        if (doctor == null || doctor.getFirstName() == null || doctor.getLastName() == null) {
            System.err.println("Invalid doctor data");
            return false;
        }
        return DoctorDAO.addDoctor(doctor);
    }
    
    /**
     * Retrieves a doctor by ID
     */
    public static Doctor getDoctor(int doctorId) {
        if (doctorId <= 0) {
            System.err.println("Invalid doctor ID");
            return null;
        }
        return DoctorDAO.getDoctorById(doctorId);
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
        return DoctorDAO.updateDoctor(doctor);
    }
    
    /**
     * Deletes a doctor
     */
    public static boolean deleteDoctor(int doctorId) {
        if (doctorId <= 0) {
            System.err.println("Invalid doctor ID");
            return false;
        }
        return DoctorDAO.deleteDoctor(doctorId);
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
