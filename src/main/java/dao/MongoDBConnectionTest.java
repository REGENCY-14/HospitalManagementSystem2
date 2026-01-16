package dao;

import model.PatientMedicalLog;
import com.mongodb.client.MongoDatabase;

/**
 * MongoDB Connection Test and Example Usage
 * Demonstrates how to use MongoDBConnection with DAOs
 */
public class MongoDBConnectionTest {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("MongoDB Connection Test - Hospital Management System");
        System.out.println("=".repeat(60));
        
        try {
            // Get MongoDB connection instance
            MongoDBConnection mongoConnection = MongoDBConnection.getInstance();
            
            // Print connection status
            System.out.println("\n" + mongoConnection.getConnectionStatus());
            
            // Test connection
            System.out.println("\nTesting connection...");
            if (mongoConnection.testConnection()) {
                System.out.println("✓ Connection test PASSED\n");
            } else {
                System.out.println("✗ Connection test FAILED\n");
                return;
            }
            
            // Get database instance
            MongoDatabase database = mongoConnection.getDatabase();
            System.out.println("✓ Database instance obtained: " + database.getName());
            
            // Initialize DAOs with database connection
            System.out.println("\nInitializing MongoDB DAOs...");
            PatientMedicalLogDAO medicalLogDAO = new PatientMedicalLogDAO(database);
            System.out.println("✓ PatientMedicalLogDAO initialized");
            
            // Example: Create a sample medical log
            System.out.println("\n" + "=".repeat(60));
            System.out.println("Example: Creating a sample Medical Log");
            System.out.println("=".repeat(60));
            
            PatientMedicalLog sampleLog = createSampleMedicalLog();
            String logId = medicalLogDAO.create(sampleLog);
            System.out.println("✓ Sample medical log created with ID: " + logId);
            
            // Retrieve the created log
            System.out.println("\nRetrieving created medical log...");
            PatientMedicalLog retrievedLog = medicalLogDAO.findById(logId);
            if (retrievedLog != null) {
                System.out.println("✓ Medical log retrieved successfully");
                System.out.println("  - Patient ID: " + retrievedLog.getPatientId());
                System.out.println("  - Log Type: " + retrievedLog.getLogType());
                System.out.println("  - Title: " + retrievedLog.getTitle());
                System.out.println("  - Status: " + retrievedLog.getStatus());
            } else {
                System.out.println("✗ Failed to retrieve medical log");
            }
            
            // Count logs for patient
            System.out.println("\nCounting medical logs for patient...");
            long logCount = medicalLogDAO.countByPatientId(1001);
            System.out.println("✓ Patient has " + logCount + " medical log(s)");
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("✓ All tests completed successfully!");
            System.out.println("=".repeat(60));
            
        } catch (Exception e) {
            System.err.println("\n✗ Error during connection test:");
            System.err.println("  " + e.getMessage());
        } finally {
            // Close connection when done
            MongoDBConnection mongoConnection = MongoDBConnection.getInstance();
            mongoConnection.closeConnection();
        }
    }
    
    /**
     * Create a sample medical log for testing
     * @return PatientMedicalLog instance
     */
    private static PatientMedicalLog createSampleMedicalLog() {
        PatientMedicalLog log = new PatientMedicalLog();
        
        // Set basic properties
        log.setPatientId(1001);
        log.setMedicalLogId("ML-2026-001");
        log.setLogDate(java.time.LocalDateTime.now());
        log.setLogType("appointment_note");
        log.setTitle("Regular Checkup - January 2026");
        log.setDescription("Patient came for regular health checkup. All vital signs are normal.");
        log.setStatus("completed");
        log.getTags().add("routine");
        log.getTags().add("checkup");
        
        // Create physician information
        PatientMedicalLog.Physician physician = log.new Physician(
            101,
            "Dr. John Smith",
            "General Medicine",
            "Internal Medicine"
        );
        log.setPhysician(physician);
        
        // Create clinical data
        PatientMedicalLog.ClinicalData clinicalData = log.new ClinicalData();
        clinicalData.getSymptoms().add("No symptoms");
        clinicalData.setDiagnosis("Healthy");
        clinicalData.setTreatment("Continue current routine");
        log.setClinicalData(clinicalData);
        
        // Create vital signs
        PatientMedicalLog.VitalSignsMeasurement vitalSigns = log.new VitalSignsMeasurement();
        vitalSigns.setSystolicBp("120");
        vitalSigns.setDiastolicBp("80");
        vitalSigns.setHeartRate(72);
        vitalSigns.setTemperature(98.6);
        vitalSigns.setRespiratoryRate(16);
        vitalSigns.setOxygenSaturation(98);
        vitalSigns.setWeight(70.0);
        vitalSigns.setHeight(180);
        log.setVitalSigns(vitalSigns);
        
        // Create assessment
        PatientMedicalLog.Assessment assessment = log.new Assessment();
        assessment.setRiskLevel("Low");
        assessment.setPrognosis("Excellent");
        assessment.setFollowUpRequired(false);
        log.setAssessment(assessment);
        
        // Create audit trail
        PatientMedicalLog.AuditTrail auditTrail = log.new AuditTrail();
        auditTrail.setCreatedBy("Dr. John Smith");
        auditTrail.setCreatedAt(java.time.LocalDateTime.now());
        auditTrail.setVersion(1);
        log.setAuditTrail(auditTrail);
        
        return log;
    }
}
