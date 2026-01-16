# NoSQL Data Model for Patient Notes and Medical Logs

## Overview
This document defines a NoSQL data model for storing patient medical notes and logs using MongoDB. The model supports unstructured and semi-structured data for efficient handling of complex medical information.

---

## 1. Data Model Design

### 1.1 Patient Medical Log Collection

```json
{
  "_id": ObjectId,
  "patientId": 12345,
  "medicalLogId": "ML-2024-001",
  "logDate": ISODate("2024-01-15T10:30:00Z"),
  "logType": "appointment_note|lab_result|vital_sign|consultation|procedure|emergency",
  "title": "String",
  "description": "String",
  
  "physician": {
    "doctorId": 101,
    "name": "Dr. John Smith",
    "specialization": "Cardiology",
    "department": "Cardiology"
  },
  
  "clinical_data": {
    "symptoms": ["chest pain", "shortness of breath"],
    "diagnosis": "Hypertension with heart palpitations",
    "treatment": "Prescribed beta-blockers",
    "medications": [
      {
        "name": "Metoprolol",
        "dosage": "50mg",
        "frequency": "Twice daily",
        "duration": "30 days"
      }
    ],
    "medical_history_references": ["Previous MI", "Type 2 Diabetes"]
  },
  
  "vital_signs": {
    "blood_pressure": "140/90 mmHg",
    "heart_rate": 72,
    "temperature": 98.6,
    "respiratory_rate": 18,
    "oxygen_saturation": 98,
    "weight": 75.5,
    "height": 178
  },
  
  "lab_results": [
    {
      "test_name": "Complete Blood Count",
      "test_date": ISODate("2024-01-15T09:00:00Z"),
      "results": {
        "hemoglobin": "14.5 g/dL",
        "white_blood_cells": "7.2 K/uL",
        "platelets": "250 K/uL"
      },
      "reference_ranges": {
        "hemoglobin": "13.5-17.5 g/dL",
        "white_blood_cells": "4.5-11.0 K/uL",
        "platelets": "150-400 K/uL"
      },
      "status": "normal"
    }
  ],
  
  "imaging_reports": [
    {
      "type": "X-Ray",
      "date": ISODate("2024-01-15T14:00:00Z"),
      "location": "Chest",
      "findings": "No abnormalities detected",
      "radiologist": "Dr. Sarah Johnson",
      "file_reference": "/imaging/xray_2024_001.pdf"
    }
  ],
  
  "attachments": [
    {
      "file_name": "ecg_report.pdf",
      "file_type": "application/pdf",
      "file_path": "/storage/attachments/ecg_2024_001.pdf",
      "upload_date": ISODate("2024-01-15T15:00:00Z"),
      "file_size": 2048576
    }
  ],
  
  "assessment": {
    "risk_level": "medium",
    "prognosis": "Good with proper medication compliance",
    "follow_up_required": true,
    "follow_up_date": ISODate("2024-02-15T00:00:00Z")
  },
  
  "tags": ["hypertension", "cardiovascular", "urgent-follow-up"],
  "status": "completed|pending|archived",
  
  "audit_trail": {
    "created_by": "Dr. John Smith",
    "created_at": ISODate("2024-01-15T10:30:00Z"),
    "modified_by": "Dr. John Smith",
    "modified_at": ISODate("2024-01-15T11:00:00Z"),
    "version": 2
  }
}
```

---

## 2. Patient Medical Notes Collection

```json
{
  "_id": ObjectId,
  "patientId": 12345,
  "noteId": "PN-2024-001",
  "noteType": "progress_note|soap_note|consultation|discharge_summary|referral",
  "title": "Follow-up Consultation",
  "created_date": ISODate("2024-01-15T10:30:00Z"),
  
  "content": {
    "subjective": "Patient reports feeling better with medication. Denies chest pain.",
    "objective": "BP: 130/85, HR: 68, RR: 16",
    "assessment": "Hypertension well-controlled on current medication regimen",
    "plan": "Continue current medications. Recheck labs in 3 months. Follow-up in 6 weeks."
  },
  
  "physician": {
    "doctorId": 101,
    "name": "Dr. John Smith",
    "signature": "Base64EncodedSignature"
  },
  
  "related_records": {
    "appointment_id": 5001,
    "medical_log_ids": ["ML-2024-001", "ML-2024-002"],
    "prescription_ids": ["PR-2024-001"]
  },
  
  "urgency": "routine|high|critical",
  "confidentiality": "standard|private|restricted",
  "visibility": ["patient", "doctors", "nurses", "billing"],
  
  "metadata": {
    "word_count": 145,
    "language": "en",
    "voice_recorded": false
  },
  
  "status": "draft|completed|reviewed|signed",
  "review_history": [
    {
      "reviewed_by": "Dr. Sarah Johnson",
      "reviewed_at": ISODate("2024-01-15T11:30:00Z"),
      "comments": "Looks good, approved"
    }
  ]
}
```

---

## 3. Vital Signs Trend Collection

```json
{
  "_id": ObjectId,
  "patientId": 12345,
  "vital_signs_id": "VS-2024-001",
  "date": ISODate("2024-01-15T08:00:00Z"),
  
  "measurements": {
    "systolic_bp": 140,
    "diastolic_bp": 90,
    "heart_rate": 72,
    "temperature": 98.6,
    "respiratory_rate": 18,
    "oxygen_saturation": 98,
    "weight": 75.5
  },
  
  "measurement_source": "manual|device|wearable",
  "measurement_location": "home|clinic|hospital",
  "measured_by": "Dr. John Smith",
  
  "alerts": [
    {
      "type": "high_blood_pressure",
      "severity": "warning|critical",
      "triggered_at": ISODate("2024-01-15T08:00:00Z"),
      "message": "Blood pressure reading is elevated"
    }
  ],
  
  "notes": "Patient feeling slightly dizzy this morning",
  "status": "normal|warning|critical"
}
```

---

## 4. Prescription History (NoSQL Extension)

```json
{
  "_id": ObjectId,
  "patientId": 12345,
  "prescriptionId": "PR-2024-001",
  "prescription_date": ISODate("2024-01-15T10:30:00Z"),
  
  "prescriber": {
    "doctorId": 101,
    "name": "Dr. John Smith",
    "specialization": "Cardiology"
  },
  
  "medications": [
    {
      "medication_id": 1001,
      "name": "Metoprolol",
      "generic_name": "Metoprolol Tartrate",
      "dosage": "50mg",
      "frequency": "Twice daily",
      "duration": "30 days",
      "indication": "Hypertension",
      "side_effects": ["Fatigue", "Dizziness"],
      "contraindications": ["Asthma", "COPD"],
      "refills": 2,
      "refill_remaining": 1
    }
  ],
  
  "diagnosis": "Essential Hypertension with left ventricular hypertrophy",
  "notes": "Patient counseled on medication compliance",
  
  "status": "active|completed|discontinued",
  "effectiveness_tracking": {
    "start_date": ISODate("2024-01-15T00:00:00Z"),
    "effectiveness": "excellent|good|moderate|poor",
    "patient_feedback": "Feeling much better",
    "last_review": ISODate("2024-01-20T00:00:00Z")
  }
}
```

---

## 5. Indexes for Performance

```javascript
// Patient Medical Logs Indexes
db.patient_medical_logs.createIndex({ patientId: 1, logDate: -1 });
db.patient_medical_logs.createIndex({ patientId: 1, logType: 1 });
db.patient_medical_logs.createIndex({ logDate: -1 });
db.patient_medical_logs.createIndex({ "physician.doctorId": 1 });
db.patient_medical_logs.createIndex({ tags: 1 });
db.patient_medical_logs.createIndex({ status: 1 });

// Patient Medical Notes Indexes
db.patient_medical_notes.createIndex({ patientId: 1, created_date: -1 });
db.patient_medical_notes.createIndex({ patientId: 1, noteType: 1 });
db.patient_medical_notes.createIndex({ "physician.doctorId": 1 });
db.patient_medical_notes.createIndex({ status: 1 });

// Vital Signs Indexes
db.vital_signs.createIndex({ patientId: 1, date: -1 });
db.vital_signs.createIndex({ patientId: 1, "measurements.systolic_bp": 1 });
db.vital_signs.createIndex({ date: -1 });

// Prescription History Indexes
db.prescriptions.createIndex({ patientId: 1, prescription_date: -1 });
db.prescriptions.createIndex({ patientId: 1, status: 1 });
db.prescriptions.createIndex({ "prescriber.doctorId": 1 });
```

---

## 6. Key Features of This Model

1. **Flexible Schema**: Accommodates varied medical data without schema migration
2. **Nested Documents**: Physician and clinical data grouped logically
3. **Arrays**: Multiple medications, lab results, and attachments
4. **Rich Metadata**: Comprehensive audit trails and version tracking
5. **Tagging System**: Easy categorization and search of medical logs
6. **Temporal Data**: Full timestamp tracking for all activities
7. **Relationships**: Links to related medical records and appointments
8. **Alerts Integration**: Embedded alert system for critical findings
9. **Unstructured Content**: Free-form notes and descriptions for clinical insights
10. **File References**: Support for attachments and imaging reports

---

## 7. Data Growth Considerations

- **Document Size**: Individual documents typically 5-20 KB
- **Collection Size**: 50 million+ documents feasible with proper indexing
- **Retention Policy**: Archive logs older than 7 years
- **Time Series Data**: Consider MongoDB Time Series collections for vital signs (MongoDB 5.0+)

