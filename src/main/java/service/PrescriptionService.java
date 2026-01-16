package service;

import dao.PrescriptionDAO;
import model.Prescription;
import java.util.List;

public class PrescriptionService {
    
    public static boolean createPrescription(Prescription prescription) {
        if (prescription == null) {
            System.err.println("Invalid prescription data");
            return false;
        }
        return PrescriptionDAO.addPrescription(prescription);
    }
    
    public static Prescription getPrescription(int prescriptionId) {
        if (prescriptionId <= 0) {
            System.err.println("Invalid prescription ID");
            return null;
        }
        return PrescriptionDAO.getPrescriptionById(prescriptionId);
    }
    
    public static List<Prescription> getAllPrescriptions() {
        return PrescriptionDAO.getAllPrescriptions();
    }
    
    public static List<Prescription> getPrescriptionsByPatient(int patientId) {
        if (patientId <= 0) {
            System.err.println("Invalid patient ID");
            return null;
        }
        return PrescriptionDAO.getPrescriptionsByPatient(patientId);
    }
    
    public static boolean updatePrescription(Prescription prescription) {
        if (prescription == null || prescription.getPrescriptionId() <= 0) {
            System.err.println("Invalid prescription data for update");
            return false;
        }
        return PrescriptionDAO.updatePrescription(prescription);
    }
    
    public static boolean deletePrescription(int prescriptionId) {
        if (prescriptionId <= 0) {
            System.err.println("Invalid prescription ID");
            return false;
        }
        return PrescriptionDAO.deletePrescription(prescriptionId);
    }
}
