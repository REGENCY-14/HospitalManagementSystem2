package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * NoSQL Model for Patient Medical Notes
 * Represents clinical notes, SOAP notes, discharge summaries, and consultation records
 */
public class PatientMedicalNote {
    private String id;                          // MongoDB ObjectId
    private int patientId;
    private String noteId;                      // PN-YYYY-001 format
    private String noteType;                    // progress_note, soap_note, consultation, discharge_summary, referral
    private String title;
    private LocalDateTime createdDate;
    
    // Note content
    private NoteContent content;
    
    // Physician information
    private PhysicianSignature physician;
    
    // Related records
    private RelatedRecords relatedRecords;
    
    // Urgency level
    private String urgency;                     // routine, high, critical
    
    // Confidentiality level
    private String confidentiality;             // standard, private, restricted
    
    // Visibility settings
    private List<String> visibility;            // patient, doctors, nurses, billing, etc.
    
    // Metadata
    private NoteMetadata metadata;
    
    // Current status
    private String status;                      // draft, completed, reviewed, signed
    
    // Review history
    private List<ReviewEntry> reviewHistory;
    
    // Constructor
    public PatientMedicalNote() {
        this.visibility = new ArrayList<>();
        this.reviewHistory = new ArrayList<>();
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
    
    public String getNoteId() {
        return noteId;
    }
    
    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
    
    public String getNoteType() {
        return noteType;
    }
    
    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public NoteContent getContent() {
        return content;
    }
    
    public void setContent(NoteContent content) {
        this.content = content;
    }
    
    public PhysicianSignature getPhysician() {
        return physician;
    }
    
    public void setPhysician(PhysicianSignature physician) {
        this.physician = physician;
    }
    
    public RelatedRecords getRelatedRecords() {
        return relatedRecords;
    }
    
    public void setRelatedRecords(RelatedRecords relatedRecords) {
        this.relatedRecords = relatedRecords;
    }
    
    public String getUrgency() {
        return urgency;
    }
    
    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }
    
    public String getConfidentiality() {
        return confidentiality;
    }
    
    public void setConfidentiality(String confidentiality) {
        this.confidentiality = confidentiality;
    }
    
    public List<String> getVisibility() {
        return visibility;
    }
    
    public void setVisibility(List<String> visibility) {
        this.visibility = visibility;
    }
    
    public NoteMetadata getMetadata() {
        return metadata;
    }
    
    public void setMetadata(NoteMetadata metadata) {
        this.metadata = metadata;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<ReviewEntry> getReviewHistory() {
        return reviewHistory;
    }
    
    public void setReviewHistory(List<ReviewEntry> reviewHistory) {
        this.reviewHistory = reviewHistory;
    }
    
    @Override
    public String toString() {
        return "PatientMedicalNote{" +
                "id='" + id + '\'' +
                ", patientId=" + patientId +
                ", noteId='" + noteId + '\'' +
                ", noteType='" + noteType + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
    
    // Nested classes
    
    /**
     * SOAP note content structure
     */
    public class NoteContent {
        private String subjective;               // What patient reports
        private String objective;                // Physical examination and measurements
        private String assessment;               // Diagnosis and clinical impression
        private String plan;                     // Treatment plan and follow-up
        
        public String getSubjective() {
            return subjective;
        }
        
        public void setSubjective(String subjective) {
            this.subjective = subjective;
        }
        
        public String getObjective() {
            return objective;
        }
        
        public void setObjective(String objective) {
            this.objective = objective;
        }
        
        public String getAssessment() {
            return assessment;
        }
        
        public void setAssessment(String assessment) {
            this.assessment = assessment;
        }
        
        public String getPlan() {
            return plan;
        }
        
        public void setPlan(String plan) {
            this.plan = plan;
        }
    }
    
    /**
     * Physician signature and information
     */
    public class PhysicianSignature {
        private int doctorId;
        private String name;
        private String signature;                // Base64 encoded signature
        private LocalDateTime signedAt;
        
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
        
        public String getSignature() {
            return signature;
        }
        
        public void setSignature(String signature) {
            this.signature = signature;
        }
        
        public LocalDateTime getSignedAt() {
            return signedAt;
        }
        
        public void setSignedAt(LocalDateTime signedAt) {
            this.signedAt = signedAt;
        }
    }
    
    /**
     * Related medical records linked to this note
     */
    public class RelatedRecords {
        private int appointmentId;
        private List<String> medicalLogIds;
        private List<String> prescriptionIds;
        
        public RelatedRecords() {
            this.medicalLogIds = new ArrayList<>();
            this.prescriptionIds = new ArrayList<>();
        }
        
        public int getAppointmentId() {
            return appointmentId;
        }
        
        public void setAppointmentId(int appointmentId) {
            this.appointmentId = appointmentId;
        }
        
        public List<String> getMedicalLogIds() {
            return medicalLogIds;
        }
        
        public void setMedicalLogIds(List<String> medicalLogIds) {
            this.medicalLogIds = medicalLogIds;
        }
        
        public List<String> getPrescriptionIds() {
            return prescriptionIds;
        }
        
        public void setPrescriptionIds(List<String> prescriptionIds) {
            this.prescriptionIds = prescriptionIds;
        }
    }
    
    /**
     * Note metadata
     */
    public class NoteMetadata {
        private int wordCount;
        private String language;                 // en, es, fr, etc.
        private boolean voiceRecorded;
        private String voiceRecordPath;
        
        public int getWordCount() {
            return wordCount;
        }
        
        public void setWordCount(int wordCount) {
            this.wordCount = wordCount;
        }
        
        public String getLanguage() {
            return language;
        }
        
        public void setLanguage(String language) {
            this.language = language;
        }
        
        public boolean isVoiceRecorded() {
            return voiceRecorded;
        }
        
        public void setVoiceRecorded(boolean voiceRecorded) {
            this.voiceRecorded = voiceRecorded;
        }
        
        public String getVoiceRecordPath() {
            return voiceRecordPath;
        }
        
        public void setVoiceRecordPath(String voiceRecordPath) {
            this.voiceRecordPath = voiceRecordPath;
        }
    }
    
    /**
     * Review entry in review history
     */
    public class ReviewEntry {
        private String reviewedBy;
        private LocalDateTime reviewedAt;
        private String comments;
        private boolean approved;
        
        public String getReviewedBy() {
            return reviewedBy;
        }
        
        public void setReviewedBy(String reviewedBy) {
            this.reviewedBy = reviewedBy;
        }
        
        public LocalDateTime getReviewedAt() {
            return reviewedAt;
        }
        
        public void setReviewedAt(LocalDateTime reviewedAt) {
            this.reviewedAt = reviewedAt;
        }
        
        public String getComments() {
            return comments;
        }
        
        public void setComments(String comments) {
            this.comments = comments;
        }
        
        public boolean isApproved() {
            return approved;
        }
        
        public void setApproved(boolean approved) {
            this.approved = approved;
        }
    }
}
