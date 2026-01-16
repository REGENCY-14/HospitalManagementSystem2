package dao;

import model.PatientMedicalLog;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bson.conversions.Bson;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MongoDB DAO for Patient Medical Logs
 * Handles CRUD operations and complex queries for medical log documents
 */
public class PatientMedicalLogDAO {
    private final MongoCollection<Document> collection;
    private static final String COLLECTION_NAME = "patient_medical_logs";
    
    /**
     * Constructor
     * @param database MongoDB database instance
     */
    public PatientMedicalLogDAO(MongoDatabase database) {
        this.collection = database.getCollection(COLLECTION_NAME);
        ensureIndexes();
    }
    
    /**
     * Create required indexes for optimal performance
     */
    private void ensureIndexes() {
        // Compound index for patient and date queries
        collection.createIndex(
            new Document("patientId", 1).append("logDate", -1)
        );
        
        // Index for log type queries
        collection.createIndex(
            new Document("patientId", 1).append("logType", 1)
        );
        
        // Index for date range queries
        collection.createIndex(new Document("logDate", -1));
        
        // Index for physician searches
        collection.createIndex(new Document("physician.doctorId", 1));
        
        // Index for tag-based searches
        collection.createIndex(new Document("tags", 1));
        
        // Index for status filtering
        collection.createIndex(new Document("status", 1));
    }
    
    /**
     * Create a new medical log
     * @param log PatientMedicalLog object
     * @return ObjectId of created document
     */
    public String create(PatientMedicalLog log) {
        try {
            Document doc = convertToDocument(log);
            System.out.println("DEBUG: Creating medical log document: " + log.getMedicalLogId());
            var result = collection.insertOne(doc);
            var insertedIdValue = result.getInsertedId();
            if (insertedIdValue != null) {
                ObjectId insertedId = insertedIdValue.asObjectId().getValue();
                String id = insertedId != null ? insertedId.toString() : null;
                System.out.println("DEBUG: Successfully created medical log with ID: " + id);
                return id;
            }
            System.out.println("DEBUG: Failed to get inserted ID");
            return null;
        } catch (Exception e) {
            System.err.println("ERROR creating medical log: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Retrieve a medical log by ID
     * @param logId ObjectId of the log
     * @return PatientMedicalLog object or null
     */
    public PatientMedicalLog findById(String logId) {
        try {
            Document doc = collection.find(
                new Document("_id", new ObjectId(logId))
            ).first();
            return doc != null ? convertToObject(doc) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Retrieve all medical logs for a patient
     * @param patientId Patient ID
     * @return List of PatientMedicalLog objects
     */
    public List<PatientMedicalLog> findByPatientId(int patientId) {
        return collection.find(new Document("patientId", patientId))
            .sort(new Document("logDate", -1))
            .into(new ArrayList<>())
            .stream()
            .map(this::convertToObject)
            .collect(Collectors.toList());
    }
    
    /**
     * Retrieve all medical logs
     * @return List of all PatientMedicalLog objects
     */
    public List<PatientMedicalLog> findAll() {
        return collection.find()
            .sort(new Document("logDate", -1))
            .into(new ArrayList<>())
            .stream()
            .map(this::convertToObject)
            .collect(Collectors.toList());
    }
    
    /**
     * Retrieve medical logs for a patient within a date range
     * @param patientId Patient ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of PatientMedicalLog objects
     */
    public List<PatientMedicalLog> findByPatientIdAndDateRange(
            int patientId, LocalDateTime startDate, LocalDateTime endDate) {
        
        Date startDateUtil = java.util.Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        Date endDateUtil = java.util.Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());
        
        Bson filter = Filters.and(
            Filters.eq("patientId", patientId),
            Filters.gte("logDate", startDateUtil),
            Filters.lte("logDate", endDateUtil)
        );
        
        return collection.find(filter)
            .sort(new Document("logDate", -1))
            .into(new ArrayList<>())
            .stream()
            .map(this::convertToObject)
            .collect(Collectors.toList());
    }
    
    /**
     * Retrieve medical logs by type for a patient
     * @param patientId Patient ID
     * @param logType Type of log (appointment_note, lab_result, etc.)
     * @return List of PatientMedicalLog objects
     */
    public List<PatientMedicalLog> findByPatientIdAndLogType(int patientId, String logType) {
        Bson filter = Filters.and(
            Filters.eq("patientId", patientId),
            Filters.eq("logType", logType)
        );
        
        return collection.find(filter)
            .sort(new Document("logDate", -1))
            .into(new ArrayList<>())
            .stream()
            .map(this::convertToObject)
            .collect(Collectors.toList());
    }
    
    /**
     * Search medical logs by diagnosis
     * @param patientId Patient ID
     * @param diagnosis Diagnosis text to search
     * @return List of matching PatientMedicalLog objects
     */
    public List<PatientMedicalLog> searchByDiagnosis(int patientId, String diagnosis) {
        Bson filter = Filters.and(
            Filters.eq("patientId", patientId),
            Filters.regex("clinical_data.diagnosis", diagnosis, "i")
        );
        
        return collection.find(filter)
            .into(new ArrayList<>())
            .stream()
            .map(this::convertToObject)
            .collect(Collectors.toList());
    }
    
    /**
     * Find patients with elevated blood pressure readings in the past month
     * @param daysBack Number of days to look back (default 30)
     * @return List of PatientMedicalLog objects with elevated BP
     */
    public List<PatientMedicalLog> findWithElevatedBP(int daysBack) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(daysBack);
        Date startDateUtil = java.util.Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        
        Bson filter = Filters.and(
            Filters.gte("logDate", startDateUtil),
            Filters.gt("vital_signs.systolic_bp", 140)
        );
        
        return collection.find(filter)
            .into(new ArrayList<>())
            .stream()
            .map(this::convertToObject)
            .collect(Collectors.toList());
    }
    
    /**
     * Complex query: Find patients with specific diagnosis on specific medications
     * @param diagnosis Diagnosis to search for
     * @param medication Medication name
     * @param daysBack Number of days to look back
     * @return List of PatientMedicalLog objects
     */
    public List<PatientMedicalLog> findByDiagnosisAndMedication(
            String diagnosis, String medication, int daysBack) {
        
        LocalDateTime startDate = LocalDateTime.now().minusDays(daysBack);
        Date startDateUtil = java.util.Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        
        Bson filter = Filters.and(
            Filters.gte("logDate", startDateUtil),
            Filters.regex("clinical_data.diagnosis", diagnosis, "i"),
            Filters.elemMatch("clinical_data.medications", 
                Filters.regex("name", medication, "i"))
        );
        
        return collection.find(filter)
            .into(new ArrayList<>())
            .stream()
            .map(this::convertToObject)
            .collect(Collectors.toList());
    }
    
    /**
     * Update a medical log
     * @param logId ObjectId of the log
     * @param log Updated PatientMedicalLog object
     * @return Number of documents modified
     */
    public long update(String logId, PatientMedicalLog log) {
        try {
            Document updatedDoc = convertToDocument(log);
            Bson filter = new Document("_id", new ObjectId(logId));
            Bson update = new Document("$set", updatedDoc);
            
            var result = collection.updateOne(filter, update);
            return result.getModifiedCount();
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }
    
    /**
     * Add vital signs to an existing medical log
     * @param logId ObjectId of the log
     * @param vitalSigns VitalSignsMeasurement to add
     * @return true if successful
     */
    public boolean addVitalSigns(String logId, PatientMedicalLog.VitalSignsMeasurement vitalSigns) {
        try {
            Bson filter = new Document("_id", new ObjectId(logId));
            Bson update = new Document("$push", new Document("vital_signs", convertVitalSignsToDocument(vitalSigns)));
            
            var result = collection.updateOne(filter, update);
            return result.getModifiedCount() > 0;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Add lab result to an existing medical log
     * @param logId ObjectId of the log
     * @param labResult LabResult to add
     * @return true if successful
     */
    public boolean addLabResult(String logId, PatientMedicalLog.LabResult labResult) {
        try {
            Bson filter = new Document("_id", new ObjectId(logId));
            Bson update = new Document("$push", new Document("lab_results", convertLabResultToDocument(labResult)));
            
            var result = collection.updateOne(filter, update);
            return result.getModifiedCount() > 0;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Delete a medical log
     * @param logId ObjectId of the log
     * @return true if deleted
     */
    public boolean delete(String logId) {
        try {
            Bson filter = new Document("_id", new ObjectId(logId));
            var result = collection.deleteOne(filter);
            return result.getDeletedCount() > 0;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Get count of medical logs for a patient
     * @param patientId Patient ID
     * @return Count of logs
     */
    public long countByPatientId(int patientId) {
        return collection.countDocuments(new Document("patientId", patientId));
    }
    
    /**
     * Archive medical logs older than specified days
     * @param daysOld Number of days
     * @return Number of documents archived
     */
    public long archiveOldLogs(int daysOld) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        Date cutoffDateUtil = java.util.Date.from(cutoffDate.atZone(ZoneId.systemDefault()).toInstant());
        
        Bson filter = Filters.and(
            Filters.lt("logDate", cutoffDateUtil),
            Filters.ne("status", "archived")
        );
        
        Bson update = new Document("$set", new Document("status", "archived"));
        var result = collection.updateMany(filter, update);
        return result.getModifiedCount();
    }
    
    // ==================== Helper Methods ====================
    
    /**
     * Convert PatientMedicalLog to MongoDB Document
     */
    private Document convertToDocument(PatientMedicalLog log) {
        Document doc = new Document()
            .append("patientId", log.getPatientId())
            .append("medicalLogId", log.getMedicalLogId())
            .append("logDate", log.getLogDate() != null ? 
                java.util.Date.from(log.getLogDate().atZone(ZoneId.systemDefault()).toInstant()) : 
                java.util.Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
            .append("logType", log.getLogType())
            .append("title", log.getTitle())
            .append("description", log.getDescription())
            .append("status", log.getStatus())
            .append("tags", log.getTags());
        
        // Add physician info
        if (log.getPhysician() != null) {
            doc.append("physician", new Document()
                .append("doctorId", log.getPhysician().getDoctorId())
                .append("name", log.getPhysician().getName())
                .append("specialization", log.getPhysician().getSpecialization())
                .append("department", log.getPhysician().getDepartment()));
        }
        
        // Add clinical data
        if (log.getClinicalData() != null) {
            Document clinicalData = new Document()
                .append("symptoms", log.getClinicalData().getSymptoms())
                .append("diagnosis", log.getClinicalData().getDiagnosis())
                .append("treatment", log.getClinicalData().getTreatment());
            
            if (log.getClinicalData().getMedications() != null) {
                List<Document> medications = log.getClinicalData().getMedications().stream()
                    .map(this::convertMedicationToDocument)
                    .collect(Collectors.toList());
                clinicalData.append("medications", medications);
            }
            
            doc.append("clinical_data", clinicalData);
        }
        
        // Add vital signs
        if (log.getVitalSigns() != null) {
            doc.append("vital_signs", convertVitalSignsToDocument(log.getVitalSigns()));
        }
        
        // Add lab results
        if (log.getLabResults() != null && !log.getLabResults().isEmpty()) {
            List<Document> labResults = log.getLabResults().stream()
                .map(this::convertLabResultToDocument)
                .collect(Collectors.toList());
            doc.append("lab_results", labResults);
        }
        
        // Add imaging reports
        if (log.getImagingReports() != null && !log.getImagingReports().isEmpty()) {
            List<Document> imagingReports = log.getImagingReports().stream()
                .map(this::convertImagingReportToDocument)
                .collect(Collectors.toList());
            doc.append("imaging_reports", imagingReports);
        }
        
        // Add attachments
        if (log.getAttachments() != null && !log.getAttachments().isEmpty()) {
            List<Document> attachments = log.getAttachments().stream()
                .map(this::convertAttachmentToDocument)
                .collect(Collectors.toList());
            doc.append("attachments", attachments);
        }
        
        // Add assessment
        if (log.getAssessment() != null) {
            doc.append("assessment", new Document()
                .append("risk_level", log.getAssessment().getRiskLevel())
                .append("prognosis", log.getAssessment().getPrognosis())
                .append("follow_up_required", log.getAssessment().isFollowUpRequired())
                .append("follow_up_date", log.getAssessment().getFollowUpDate() != null ?
                    java.util.Date.from(log.getAssessment().getFollowUpDate()
                        .atZone(ZoneId.systemDefault()).toInstant()) : null));
        }
        
        // Add audit trail
        if (log.getAuditTrail() != null) {
            doc.append("audit_trail", new Document()
                .append("created_by", log.getAuditTrail().getCreatedBy())
                .append("created_at", log.getAuditTrail().getCreatedAt() != null ? 
                    java.util.Date.from(log.getAuditTrail().getCreatedAt()
                        .atZone(ZoneId.systemDefault()).toInstant()) : null)
                .append("modified_by", log.getAuditTrail().getModifiedBy())
                .append("modified_at", log.getAuditTrail().getModifiedAt() != null ? 
                    java.util.Date.from(log.getAuditTrail().getModifiedAt()
                        .atZone(ZoneId.systemDefault()).toInstant()) : null)
                .append("version", log.getAuditTrail().getVersion()));
        }
        
        return doc;
    }
    
    /**
     * Convert MongoDB Document to PatientMedicalLog
     */
    private PatientMedicalLog convertToObject(Document doc) {
        PatientMedicalLog log = new PatientMedicalLog();
        log.setId(doc.getObjectId("_id").toString());
        log.setPatientId(doc.getInteger("patientId"));
        log.setMedicalLogId(doc.getString("medicalLogId"));
        log.setLogDate(convertToLocalDateTime(doc.getDate("logDate")));
        log.setLogType(doc.getString("logType"));
        log.setTitle(doc.getString("title"));
        log.setDescription(doc.getString("description"));
        log.setStatus(doc.getString("status"));
        log.setTags(doc.getList("tags", String.class));
        
        // Physician
        Document physicianDoc = (Document) doc.get("physician");
        if (physicianDoc != null) {
            PatientMedicalLog.Physician physician = log.new Physician(
                physicianDoc.getInteger("doctorId", 0),
                physicianDoc.getString("name"),
                physicianDoc.getString("specialization"),
                physicianDoc.getString("department")
            );
            log.setPhysician(physician);
        }
        
        // Clinical data (basic)
        Document clinicalDoc = (Document) doc.get("clinical_data");
        if (clinicalDoc != null) {
            PatientMedicalLog.ClinicalData cd = log.new ClinicalData();
            cd.setDiagnosis(clinicalDoc.getString("diagnosis"));
            cd.setTreatment(clinicalDoc.getString("treatment"));
            log.setClinicalData(cd);
        }
        
        return log;
    }
    
    private Document convertMedicationToDocument(PatientMedicalLog.Medication med) {
        return new Document()
            .append("medication_id", med.getMedicationId())
            .append("name", med.getName())
            .append("generic_name", med.getGenericName())
            .append("dosage", med.getDosage())
            .append("frequency", med.getFrequency())
            .append("duration", med.getDuration())
            .append("indication", med.getIndication())
            .append("side_effects", med.getSideEffects())
            .append("contraindications", med.getContraindications());
    }
    
    private Document convertVitalSignsToDocument(PatientMedicalLog.VitalSignsMeasurement vs) {
        return new Document()
            .append("systolic_bp", vs.getSystolicBp())
            .append("diastolic_bp", vs.getDiastolicBp())
            .append("heart_rate", vs.getHeartRate())
            .append("temperature", vs.getTemperature())
            .append("respiratory_rate", vs.getRespiratoryRate())
            .append("oxygen_saturation", vs.getOxygenSaturation())
            .append("weight", vs.getWeight())
            .append("height", vs.getHeight());
    }
    
    private Document convertLabResultToDocument(PatientMedicalLog.LabResult lr) {
        return new Document()
            .append("test_name", lr.getTestName())
            .append("test_date", lr.getTestDate() != null ? 
                java.util.Date.from(lr.getTestDate().atZone(ZoneId.systemDefault()).toInstant()) : null)
            .append("results", new Document(lr.getResults()))
            .append("reference_ranges", new Document(lr.getReferenceRanges()))
            .append("status", lr.getStatus());
    }
    
    private Document convertImagingReportToDocument(PatientMedicalLog.ImagingReport ir) {
        return new Document()
            .append("type", ir.getType())
            .append("date", ir.getDate() != null ? 
                java.util.Date.from(ir.getDate().atZone(ZoneId.systemDefault()).toInstant()) : null)
            .append("location", ir.getLocation())
            .append("findings", ir.getFindings())
            .append("radiologist", ir.getRadiologist())
            .append("file_reference", ir.getFileReference());
    }
    
    private Document convertAttachmentToDocument(PatientMedicalLog.Attachment att) {
        return new Document()
            .append("file_name", att.getFileName())
            .append("file_type", att.getFileType())
            .append("file_path", att.getFilePath())
            .append("upload_date", att.getUploadDate() != null ? 
                java.util.Date.from(att.getUploadDate().atZone(ZoneId.systemDefault()).toInstant()) : null)
            .append("file_size", att.getFileSize());
    }
    
    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }
}
