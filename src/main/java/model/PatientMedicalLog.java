package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * NoSQL Model for Patient Medical Logs
 * Represents comprehensive medical log entries for a patient
 * Includes clinical data, vital signs, lab results, and attachments
 */
public class PatientMedicalLog {
    private String id;                          // MongoDB ObjectId
    private int patientId;
    private String medicalLogId;                // ML-YYYY-001 format
    private LocalDateTime logDate;
    private String logType;                     // appointment_note, lab_result, vital_sign, etc.
    private String title;
    private String description;
    
    // Physician information
    private Physician physician;
    
    // Clinical data
    private ClinicalData clinicalData;
    
    // Vital signs measurement
    private VitalSignsMeasurement vitalSigns;
    
    // Lab results
    private List<LabResult> labResults;
    
    // Imaging reports
    private List<ImagingReport> imagingReports;
    
    // File attachments
    private List<Attachment> attachments;
    
    // Assessment and prognosis
    private Assessment assessment;
    
    // Tags for categorization
    private List<String> tags;
    
    // Log status
    private String status;                      // completed, pending, archived
    
    // Audit trail
    private AuditTrail auditTrail;
    
    // Constructor
    public PatientMedicalLog() {
        this.labResults = new ArrayList<>();
        this.imagingReports = new ArrayList<>();
        this.attachments = new ArrayList<>();
        this.tags = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public int getPatientId() {
        return patientId;
    }
    
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    
    public String getMedicalLogId() {
        return medicalLogId;
    }
    
    public void setMedicalLogId(String medicalLogId) {
        this.medicalLogId = medicalLogId;
    }
    
    public LocalDateTime getLogDate() {
        return logDate;
    }
    
    public void setLogDate(LocalDateTime logDate) {
        this.logDate = logDate;
    }
    
    public String getLogType() {
        return logType;
    }
    
    public void setLogType(String logType) {
        this.logType = logType;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Physician getPhysician() {
        return physician;
    }
    
    public void setPhysician(Physician physician) {
        this.physician = physician;
    }
    
    public ClinicalData getClinicalData() {
        return clinicalData;
    }
    
    public void setClinicalData(ClinicalData clinicalData) {
        this.clinicalData = clinicalData;
    }
    
    public VitalSignsMeasurement getVitalSigns() {
        return vitalSigns;
    }
    
    public void setVitalSigns(VitalSignsMeasurement vitalSigns) {
        this.vitalSigns = vitalSigns;
    }
    
    public List<LabResult> getLabResults() {
        return labResults;
    }
    
    public void setLabResults(List<LabResult> labResults) {
        this.labResults = labResults;
    }
    
    public List<ImagingReport> getImagingReports() {
        return imagingReports;
    }
    
    public void setImagingReports(List<ImagingReport> imagingReports) {
        this.imagingReports = imagingReports;
    }
    
    public List<Attachment> getAttachments() {
        return attachments;
    }
    
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
    
    public Assessment getAssessment() {
        return assessment;
    }
    
    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public AuditTrail getAuditTrail() {
        return auditTrail;
    }
    
    public void setAuditTrail(AuditTrail auditTrail) {
        this.auditTrail = auditTrail;
    }
    
    @Override
    public String toString() {
        return "PatientMedicalLog{" +
                "id='" + id + '\'' +
                ", patientId=" + patientId +
                ", medicalLogId='" + medicalLogId + '\'' +
                ", logDate=" + logDate +
                ", logType='" + logType + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
    
    // Nested classes for complex data structures
    
    /**
     * Physician information embedded in medical log
     */
    public class Physician {
        private int doctorId;
        private String name;
        private String specialization;
        private String department;
        
        public Physician(int doctorId, String name, String specialization, String department) {
            this.doctorId = doctorId;
            this.name = name;
            this.specialization = specialization;
            this.department = department;
        }
        
        public int getDoctorId() {
            return doctorId;
        }
        
        public void setDoctorId(int doctorId) {
            this.doctorId = doctorId;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getSpecialization() {
            return specialization;
        }
        
        public void setSpecialization(String specialization) {
            this.specialization = specialization;
        }
        
        public String getDepartment() {
            return department;
        }
        
        public void setDepartment(String department) {
            this.department = department;
        }
    }
    
    /**
     * Clinical data including diagnosis, treatment, and medications
     */
    public class ClinicalData {
        private List<String> symptoms;
        private String diagnosis;
        private String treatment;
        private List<Medication> medications;
        private List<String> medicalHistoryReferences;
        
        public ClinicalData() {
            this.symptoms = new ArrayList<>();
            this.medications = new ArrayList<>();
            this.medicalHistoryReferences = new ArrayList<>();
        }
        
        public List<String> getSymptoms() {
            return symptoms;
        }
        
        public void setSymptoms(List<String> symptoms) {
            this.symptoms = symptoms;
        }
        
        public String getDiagnosis() {
            return diagnosis;
        }
        
        public void setDiagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
        }
        
        public String getTreatment() {
            return treatment;
        }
        
        public void setTreatment(String treatment) {
            this.treatment = treatment;
        }
        
        public List<Medication> getMedications() {
            return medications;
        }
        
        public void setMedications(List<Medication> medications) {
            this.medications = medications;
        }
        
        public List<String> getMedicalHistoryReferences() {
            return medicalHistoryReferences;
        }
        
        public void setMedicalHistoryReferences(List<String> medicalHistoryReferences) {
            this.medicalHistoryReferences = medicalHistoryReferences;
        }
    }
    
    /**
     * Medication information with dosage and side effects
     */
    public class Medication {
        private int medicationId;
        private String name;
        private String genericName;
        private String dosage;
        private String frequency;
        private String duration;
        private String indication;
        private List<String> sideEffects;
        private List<String> contraindications;
        
        public Medication() {
            this.sideEffects = new ArrayList<>();
            this.contraindications = new ArrayList<>();
        }
        
        public int getMedicationId() {
            return medicationId;
        }
        
        public void setMedicationId(int medicationId) {
            this.medicationId = medicationId;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getGenericName() {
            return genericName;
        }
        
        public void setGenericName(String genericName) {
            this.genericName = genericName;
        }
        
        public String getDosage() {
            return dosage;
        }
        
        public void setDosage(String dosage) {
            this.dosage = dosage;
        }
        
        public String getFrequency() {
            return frequency;
        }
        
        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }
        
        public String getDuration() {
            return duration;
        }
        
        public void setDuration(String duration) {
            this.duration = duration;
        }
        
        public String getIndication() {
            return indication;
        }
        
        public void setIndication(String indication) {
            this.indication = indication;
        }
        
        public List<String> getSideEffects() {
            return sideEffects;
        }
        
        public void setSideEffects(List<String> sideEffects) {
            this.sideEffects = sideEffects;
        }
        
        public List<String> getContraindications() {
            return contraindications;
        }
        
        public void setContraindications(List<String> contraindications) {
            this.contraindications = contraindications;
        }
    }
    
    /**
     * Vital signs measurement
     */
    public class VitalSignsMeasurement {
        private String systolicBp;
        private String diastolicBp;
        private int heartRate;
        private double temperature;
        private int respiratoryRate;
        private int oxygenSaturation;
        private double weight;
        private int height;
        
        public String getSystolicBp() {
            return systolicBp;
        }
        
        public void setSystolicBp(String systolicBp) {
            this.systolicBp = systolicBp;
        }
        
        public String getDiastolicBp() {
            return diastolicBp;
        }
        
        public void setDiastolicBp(String diastolicBp) {
            this.diastolicBp = diastolicBp;
        }
        
        public int getHeartRate() {
            return heartRate;
        }
        
        public void setHeartRate(int heartRate) {
            this.heartRate = heartRate;
        }
        
        public double getTemperature() {
            return temperature;
        }
        
        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }
        
        public int getRespiratoryRate() {
            return respiratoryRate;
        }
        
        public void setRespiratoryRate(int respiratoryRate) {
            this.respiratoryRate = respiratoryRate;
        }
        
        public int getOxygenSaturation() {
            return oxygenSaturation;
        }
        
        public void setOxygenSaturation(int oxygenSaturation) {
            this.oxygenSaturation = oxygenSaturation;
        }
        
        public double getWeight() {
            return weight;
        }
        
        public void setWeight(double weight) {
            this.weight = weight;
        }
        
        public int getHeight() {
            return height;
        }
        
        public void setHeight(int height) {
            this.height = height;
        }
    }
    
    /**
     * Lab result with test name and values
     */
    public class LabResult {
        private String testName;
        private LocalDateTime testDate;
        private Map<String, String> results;
        private Map<String, String> referenceRanges;
        private String status;
        
        public LabResult() {
            this.results = new HashMap<>();
            this.referenceRanges = new HashMap<>();
        }
        
        public String getTestName() {
            return testName;
        }
        
        public void setTestName(String testName) {
            this.testName = testName;
        }
        
        public LocalDateTime getTestDate() {
            return testDate;
        }
        
        public void setTestDate(LocalDateTime testDate) {
            this.testDate = testDate;
        }
        
        public Map<String, String> getResults() {
            return results;
        }
        
        public void setResults(Map<String, String> results) {
            this.results = results;
        }
        
        public Map<String, String> getReferenceRanges() {
            return referenceRanges;
        }
        
        public void setReferenceRanges(Map<String, String> referenceRanges) {
            this.referenceRanges = referenceRanges;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
    }
    
    /**
     * Imaging report
     */
    public class ImagingReport {
        private String type;
        private LocalDateTime date;
        private String location;
        private String findings;
        private String radiologist;
        private String fileReference;
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public LocalDateTime getDate() {
            return date;
        }
        
        public void setDate(LocalDateTime date) {
            this.date = date;
        }
        
        public String getLocation() {
            return location;
        }
        
        public void setLocation(String location) {
            this.location = location;
        }
        
        public String getFindings() {
            return findings;
        }
        
        public void setFindings(String findings) {
            this.findings = findings;
        }
        
        public String getRadiologist() {
            return radiologist;
        }
        
        public void setRadiologist(String radiologist) {
            this.radiologist = radiologist;
        }
        
        public String getFileReference() {
            return fileReference;
        }
        
        public void setFileReference(String fileReference) {
            this.fileReference = fileReference;
        }
    }
    
    /**
     * File attachment
     */
    public class Attachment {
        private String fileName;
        private String fileType;
        private String filePath;
        private LocalDateTime uploadDate;
        private long fileSize;
        
        public String getFileName() {
            return fileName;
        }
        
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
        
        public String getFileType() {
            return fileType;
        }
        
        public void setFileType(String fileType) {
            this.fileType = fileType;
        }
        
        public String getFilePath() {
            return filePath;
        }
        
        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
        
        public LocalDateTime getUploadDate() {
            return uploadDate;
        }
        
        public void setUploadDate(LocalDateTime uploadDate) {
            this.uploadDate = uploadDate;
        }
        
        public long getFileSize() {
            return fileSize;
        }
        
        public void setFileSize(long fileSize) {
            this.fileSize = fileSize;
        }
    }
    
    /**
     * Assessment and prognosis
     */
    public class Assessment {
        private String riskLevel;
        private String prognosis;
        private boolean followUpRequired;
        private LocalDateTime followUpDate;
        
        public String getRiskLevel() {
            return riskLevel;
        }
        
        public void setRiskLevel(String riskLevel) {
            this.riskLevel = riskLevel;
        }
        
        public String getPrognosis() {
            return prognosis;
        }
        
        public void setPrognosis(String prognosis) {
            this.prognosis = prognosis;
        }
        
        public boolean isFollowUpRequired() {
            return followUpRequired;
        }
        
        public void setFollowUpRequired(boolean followUpRequired) {
            this.followUpRequired = followUpRequired;
        }
        
        public LocalDateTime getFollowUpDate() {
            return followUpDate;
        }
        
        public void setFollowUpDate(LocalDateTime followUpDate) {
            this.followUpDate = followUpDate;
        }
    }
    
    /**
     * Audit trail for tracking changes
     */
    public class AuditTrail {
        private String createdBy;
        private LocalDateTime createdAt;
        private String modifiedBy;
        private LocalDateTime modifiedAt;
        private int version;
        
        public String getCreatedBy() {
            return createdBy;
        }
        
        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }
        
        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
        
        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
        
        public String getModifiedBy() {
            return modifiedBy;
        }
        
        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }
        
        public LocalDateTime getModifiedAt() {
            return modifiedAt;
        }
        
        public void setModifiedAt(LocalDateTime modifiedAt) {
            this.modifiedAt = modifiedAt;
        }
        
        public int getVersion() {
            return version;
        }
        
        public void setVersion(int version) {
            this.version = version;
        }
    }
}
