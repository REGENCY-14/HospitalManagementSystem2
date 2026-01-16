package service;

import dao.PatientFeedbackDAO;
import model.PatientFeedback;
import java.util.List;

public class PatientFeedbackService {
    
    public static boolean createFeedback(PatientFeedback feedback) {
        if (feedback == null) {
            System.err.println("Invalid feedback data");
            return false;
        }
        return PatientFeedbackDAO.addFeedback(feedback);
    }
    
    public static PatientFeedback getFeedback(int feedbackId) {
        if (feedbackId <= 0) {
            System.err.println("Invalid feedback ID");
            return null;
        }
        return PatientFeedbackDAO.getFeedbackById(feedbackId);
    }
    
    public static List<PatientFeedback> getAllFeedback() {
        return PatientFeedbackDAO.getAllFeedback();
    }
    
    public static List<PatientFeedback> getFeedbackByPatient(int patientId) {
        if (patientId <= 0) {
            System.err.println("Invalid patient ID");
            return null;
        }
        return PatientFeedbackDAO.getFeedbackByPatient(patientId);
    }
    
    public static boolean updateFeedback(PatientFeedback feedback) {
        if (feedback == null || feedback.getFeedbackId() <= 0) {
            System.err.println("Invalid feedback data for update");
            return false;
        }
        return PatientFeedbackDAO.updateFeedback(feedback);
    }
    
    public static boolean deleteFeedback(int feedbackId) {
        if (feedbackId <= 0) {
            System.err.println("Invalid feedback ID");
            return false;
        }
        return PatientFeedbackDAO.deleteFeedback(feedbackId);
    }
}
