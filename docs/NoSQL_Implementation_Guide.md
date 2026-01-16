# NoSQL Implementation Guide for Hospital Management System

**Document Version**: 1.0  
**Last Updated**: January 15, 2024  
**Status**: Ready for Implementation  

---

## Table of Contents
1. [Overview](#overview)
2. [MongoDB Setup](#mongodb-setup)
3. [Data Model Mapping](#data-model-mapping)
4. [Java Implementation](#java-implementation)
5. [Integration with Existing System](#integration-with-existing-system)
6. [Migration Strategy](#migration-strategy)
7. [Best Practices](#best-practices)
8. [Troubleshooting](#troubleshooting)

---

## 1. Overview

This guide provides step-by-step instructions for implementing MongoDB in the Hospital Management System to store patient medical logs and clinical notes alongside the existing MySQL database.

### Architecture Summary

```
Hospital Management System
│
├─ MySQL (Relational Data)
│  ├─ Patient Demographics
│  ├─ Appointments
│  ├─ Doctors & Departments
│  ├─ Inventory
│  └─ Billing
│
└─ MongoDB (Document Store)
   ├─ Patient Medical Logs
   ├─ Clinical Notes
   ├─ Vital Signs History
   ├─ Lab Results
   └─ Prescription History
```

---

## 2. MongoDB Setup

### 2.1 Installation

#### Windows

```powershell
# Using Chocolatey
choco install mongodb

# Or download from: https://www.mongodb.com/try/download/community
# Extract and add to PATH

# Verify installation
mongod --version
```

#### Docker (Recommended for Development)

```bash
docker run -d \
  --name mongodb \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=password \
  -v mongodb_data:/data/db \
  mongo:latest
```

#### Linux

```bash
# Ubuntu/Debian
wget -qO - https://www.mongodb.org/static/pgp/server-6.0.asc | sudo apt-key add -
sudo apt-get install -y mongodb-org

# Start service
sudo systemctl start mongod
sudo systemctl enable mongod
```

### 2.2 Configuration

**MongoDB Connection String**:
```
mongodb://admin:password@localhost:27017/hospital_db
```

**For Production** (Replica Set):
```
mongodb://admin:password@node1:27017,node2:27017,node3:27017/hospital_db?replicaSet=rs0
```

### 2.3 Database Initialization

```javascript
// Connect to MongoDB
mongo mongodb://admin:password@localhost:27017/hospital_db

// Create database
use hospital_db

// Create collections
db.createCollection("patient_medical_logs")
db.createCollection("patient_medical_notes")
db.createCollection("vital_signs")
db.createCollection("prescriptions")

// Enable schema validation (optional but recommended)
db.runCommand({
  "collMod": "patient_medical_logs",
  "validator": {
    "$jsonSchema": {
      "bsonType": "object",
      "required": ["patientId", "logDate", "logType"],
      "properties": {
        "patientId": { "bsonType": "int" },
        "logDate": { "bsonType": "date" },
        "logType": { "bsonType": "string" }
      }
    }
  }
})

// Create indexes
db.patient_medical_logs.createIndex({ patientId: 1, logDate: -1 })
db.patient_medical_logs.createIndex({ logType: 1 })
db.patient_medical_logs.createIndex({ "physician.doctorId": 1 })
db.patient_medical_logs.createIndex({ status: 1 })
```

---

## 3. Data Model Mapping

### 3.1 From Relational to Document Model

**Relational Schema (MySQL)**:
```sql
-- Multiple normalized tables
Patient
MedicalLog
ClinicalData
Medications
VitalSigns
LabResults
ImagingReports
Attachments
```

**Document Schema (MongoDB)**:
```javascript
{
  "_id": ObjectId,
  "patientId": 12345,
  "logDate": ISODate("2024-01-15T10:30:00Z"),
  "physician": { ... },
  "clinical_data": { ... },
  "vital_signs": { ... },
  "lab_results": [ ... ],
  "imaging_reports": [ ... ],
  "attachments": [ ... ]
}
```

### 3.2 Data Migration Mapping

| MySQL Table | MongoDB Collection | Strategy |
|------------|-------------------|----------|
| MedicalLog | patient_medical_logs | Flatten + embed related data |
| ClinicalData | Embed in medical_logs | Nested document |
| Medications | Embed in medical_logs | Array of objects |
| VitalSigns | Embed in medical_logs | Nested document |
| LabResults | Embed in medical_logs | Array of objects |
| ImagingReports | Embed in medical_logs | Array of objects |
| Attachments | Embed in medical_logs | Array of objects |

---

## 4. Java Implementation

### 4.1 Maven Dependencies

Add to `pom.xml`:

```xml
<!-- MongoDB Java Driver -->
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>4.11.0</version>
</dependency>

<!-- BSON Library -->
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>bson</artifactId>
    <version>4.11.0</version>
</dependency>

<!-- MongoDB with Async driver (optional) -->
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-reactivestreams</artifactId>
    <version>4.11.0</version>
</dependency>
```

### 4.2 MongoDB Connection Manager

Create `dao/MongoDBConnection.java`:

```java
package dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.Arrays;

/**
 * MongoDB Connection Manager
 * Handles connection pooling and database access
 */
public class MongoDBConnection {
    private static MongoDatabase database;
    private static MongoClient mongoClient;
    
    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static final String DATABASE_NAME = "hospital_db";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";
    
    /**
     * Initialize MongoDB connection
     */
    public static void initialize() {
        try {
            // Connection string approach (recommended for production)
            String connectionString = "mongodb://" + USERNAME + ":" + PASSWORD + 
                                    "@" + HOST + ":" + PORT + "/" + DATABASE_NAME;
            
            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase(DATABASE_NAME);
            
            // Verify connection
            database.runCommand(new org.bson.Document("ping", 1));
            System.out.println("MongoDB connection established successfully!");
            
        } catch (Exception e) {
            System.err.println("Failed to connect to MongoDB: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Get the database instance
     */
    public static MongoDatabase getDatabase() {
        if (database == null) {
            initialize();
        }
        return database;
    }
    
    /**
     * Close the connection
     */
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed");
        }
    }
}
```

### 4.3 Service Layer Implementation

Create `service/MedicalLogService.java`:

```java
package service;

import model.PatientMedicalLog;
import dao.PatientMedicalLogDAO;
import dao.MongoDBConnection;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for Medical Log operations
 * Provides business logic and validation
 */
public class MedicalLogService {
    private PatientMedicalLogDAO medicalLogDAO;
    
    public MedicalLogService() {
        this.medicalLogDAO = new PatientMedicalLogDAO(MongoDBConnection.getDatabase());
    }
    
    /**
     * Create a new medical log with validation
     */
    public String createMedicalLog(PatientMedicalLog log) throws ValidationException {
        // Validation
        if (log.getPatientId() <= 0) {
            throw new ValidationException("Invalid patient ID");
        }
        if (log.getLogDate() == null) {
            throw new ValidationException("Log date is required");
        }
        if (log.getLogType() == null || log.getLogType().isEmpty()) {
            throw new ValidationException("Log type is required");
        }
        
        // Set default values
        if (log.getStatus() == null) {
            log.setStatus("completed");
        }
        
        // Create audit trail
        PatientMedicalLog.AuditTrail auditTrail = new PatientMedicalLog.AuditTrail();
        auditTrail.setCreatedBy("System");
        auditTrail.setCreatedAt(LocalDateTime.now());
        auditTrail.setVersion(1);
        log.setAuditTrail(auditTrail);
        
        return medicalLogDAO.create(log);
    }
    
    /**
     * Retrieve patient's medical history
     */
    public List<PatientMedicalLog> getPatientMedicalHistory(int patientId) {
        return medicalLogDAO.findByPatientId(patientId);
    }
    
    /**
     * Get medical logs within date range
     */
    public List<PatientMedicalLog> getMedicalLogsInDateRange(
            int patientId, LocalDateTime startDate, LocalDateTime endDate) {
        return medicalLogDAO.findByPatientIdAndDateRange(patientId, startDate, endDate);
    }
    
    /**
     * Add vital signs to existing log
     */
    public boolean addVitalSigns(String logId, PatientMedicalLog.VitalSignsMeasurement vitalSigns) 
            throws ValidationException {
        if (vitalSigns == null) {
            throw new ValidationException("Vital signs data required");
        }
        return medicalLogDAO.addVitalSigns(logId, vitalSigns);
    }
    
    /**
     * Add lab result to existing log
     */
    public boolean addLabResult(String logId, PatientMedicalLog.LabResult labResult) 
            throws ValidationException {
        if (labResult == null) {
            throw new ValidationException("Lab result data required");
        }
        return medicalLogDAO.addLabResult(logId, labResult);
    }
    
    /**
     * Complex search query
     */
    public List<PatientMedicalLog> searchByDiagnosisAndMedication(
            String diagnosis, String medication) {
        return medicalLogDAO.findByDiagnosisAndMedication(diagnosis, medication, 30);
    }
    
    static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
}
```

### 4.4 Controller Integration

Update `controller/HospitalController.java` to use MongoDB:

```java
// In your existing controller
private MedicalLogService medicalLogService;

public HospitalController() {
    MongoDBConnection.initialize();
    this.medicalLogService = new MedicalLogService();
}

// Add medical log endpoint
public void handleAddMedicalLog() {
    try {
        PatientMedicalLog log = createLogFromUI(); // Get data from UI
        String logId = medicalLogService.createMedicalLog(log);
        showSuccess("Medical log created with ID: " + logId);
    } catch (Exception e) {
        showError("Failed to create medical log: " + e.getMessage());
    }
}

// View patient medical history
public void viewPatientMedicalHistory(int patientId) {
    List<PatientMedicalLog> history = medicalLogService.getPatientMedicalHistory(patientId);
    displayMedicalHistory(history);
}
```

---

## 5. Integration with Existing System

### 5.1 Hybrid Data Access

Some queries now span both databases:

```java
// Get patient info from MySQL
Patient patient = patientDAO.findById(12345);

// Get medical logs from MongoDB
List<PatientMedicalLog> medicalLogs = medicalLogService.getPatientMedicalHistory(12345);

// Combine results
PatientProfile profile = new PatientProfile();
profile.setPatientInfo(patient);
profile.setMedicalLogs(medicalLogs);
```

### 5.2 Synchronization Strategy

For data consistency between MySQL and MongoDB:

```java
/**
 * When creating an appointment in MySQL,
 * also create corresponding medical log in MongoDB
 */
public int createAppointmentWithLog(Appointment appointment, 
                                   PatientMedicalLog medicalLog) {
    // Create in MySQL
    int appointmentId = appointmentDAO.create(appointment);
    
    // Create corresponding log in MongoDB
    medicalLog.setRelatedRecords(new PatientMedicalLog.RelatedRecords());
    medicalLog.getRelatedRecords().setAppointmentId(appointmentId);
    medicalLogService.createMedicalLog(medicalLog);
    
    return appointmentId;
}
```

### 5.3 Transaction Handling

```java
/**
 * Multi-database transaction handling
 */
public boolean updatePatientAndLog(Patient patient, PatientMedicalLog log) {
    try {
        // Start MySQL transaction
        connection.setAutoCommit(false);
        
        // Update patient
        patientDAO.update(patient);
        
        // Update MongoDB
        String logId = medicalLogService.createMedicalLog(log);
        
        // Commit MySQL
        connection.commit();
        
        return logId != null;
    } catch (Exception e) {
        connection.rollback();
        throw e;
    } finally {
        connection.setAutoCommit(true);
    }
}
```

---

## 6. Migration Strategy

### 6.1 Phase 1: Preparation

**Week 1-2**:
- [ ] Set up MongoDB cluster
- [ ] Create database and collections
- [ ] Add MongoDB dependencies to pom.xml
- [ ] Implement MongoDB connection manager

### 6.2 Phase 2: Development

**Week 3-4**:
- [ ] Implement PatientMedicalLog model
- [ ] Implement PatientMedicalLogDAO
- [ ] Implement MedicalLogService
- [ ] Write unit tests for MongoDB operations

### 6.3 Phase 3: Testing

**Week 5-6**:
- [ ] Integration tests
- [ ] Performance testing (compare with MySQL)
- [ ] Data consistency testing
- [ ] User acceptance testing

### 6.4 Phase 4: Migration

**Week 7-8**:
- [ ] Export existing medical log data from MySQL
- [ ] Transform data to MongoDB format
- [ ] Batch import to MongoDB
- [ ] Verify data integrity

### 6.5 Phase 5: Cutover

**Week 9**:
- [ ] Enable dual-write (MySQL + MongoDB)
- [ ] Monitor consistency
- [ ] Switch reads to MongoDB
- [ ] Archive old MySQL data

---

## 7. Best Practices

### 7.1 Schema Design

```java
// DO: Embed small related data
{
  "patientId": 12345,
  "physician": {
    "doctorId": 101,
    "name": "Dr. Smith",
    "specialization": "Cardiology"
  }
}

// DON'T: Reference small data (creates extra queries)
{
  "patientId": 12345,
  "physicianId": 101
  // Need separate query to get physician details
}
```

### 7.2 Array Size Management

```java
// For unbounded arrays (like vital signs with continuous entries),
// consider splitting into time-based collections

// Better for massive arrays
{
  "patientId": 12345,
  "date": "2024-01-15",
  "vital_signs": [
    { "time": "10:00", "bp": "140/90" },
    { "time": "14:00", "bp": "135/88" }
  ]
}
```

### 7.3 Query Optimization

```javascript
// DO: Use covered queries (query + projection in one index)
db.patient_medical_logs.createIndex({ 
  patientId: 1, 
  logDate: -1, 
  title: 1 
})

db.patient_medical_logs.find(
  { patientId: 12345, logDate: { $gte: ISODate("2024-01-01") } },
  { title: 1, _id: 0 }  // Covered by index
)

// DON'T: Large result sets without projection
db.patient_medical_logs.find({ patientId: 12345 })  // Returns everything
```

### 7.4 Indexing Strategy

```java
// Essential indexes
db.patient_medical_logs.createIndex({ patientId: 1, logDate: -1 })    // Most queries
db.patient_medical_logs.createIndex({ patientId: 1, logType: 1 })     // Type filters
db.patient_medical_logs.createIndex({ "physician.doctorId": 1 })      // Doctor searches
db.patient_medical_logs.createIndex({ status: 1 })                    // Status queries

// Don't over-index - maintain 2-3 indexes per collection
```

### 7.5 Connection Pooling

```java
// Use connection pooling for better performance
MongoClientSettings settings = MongoClientSettings.builder()
    .applyConnectionString(new ConnectionString(connectionString))
    .applyToConnectionPoolSettings(builder ->
        builder.maxConnectionPoolSize(100)
               .minConnectionPoolSize(10))
    .build();

MongoClient mongoClient = MongoClients.create(settings);
```

---

## 8. Troubleshooting

### Issue: Connection Timeout

**Symptoms**: `MongoTimeoutException` on connection attempts

**Solutions**:
```java
// Increase timeout
MongoClientSettings settings = MongoClientSettings.builder()
    .applyToSocketSettings(builder ->
        builder.connectTimeoutMS(5000)
               .readTimeoutMS(10000))
    .build();
```

### Issue: Slow Queries

**Diagnosis**:
```javascript
// Enable profiling
db.setProfilingLevel(1, { millis: 100 })  // Log queries > 100ms

// View slow queries
db.system.profile.find().pretty()
```

**Solutions**:
```javascript
// Add indexes for common queries
db.patient_medical_logs.createIndex({ patientId: 1, logDate: -1 })

// Use explain to understand query plan
db.patient_medical_logs.find({ patientId: 12345 }).explain("executionStats")
```

### Issue: High Memory Usage

**Solutions**:
```javascript
// Set WiredTiger cache size
// In mongod.conf:
storage:
  engine: wiredTiger
  wiredTiger:
    engineConfig:
      cacheSizeGB: 2  // Adjust based on available RAM
```

### Issue: Replication Lag

**Diagnosis**:
```javascript
// Check replica set status
rs.status()

// Monitor sync progress
db.currentOp()
```

**Solution**: 
- Reduce write load
- Increase network bandwidth
- Use faster storage for secondaries

---

## Monitoring and Maintenance

### Regular Backups

```bash
# Daily backup
mongodump --out /backups/hospital_$(date +%Y%m%d) \
  --uri="mongodb://admin:password@localhost:27017/hospital_db"

# Restore from backup
mongorestore --drop --uri="mongodb://admin:password@localhost:27017" \
  /backups/hospital_20240115
```

### Index Maintenance

```javascript
// Check index usage
db.patient_medical_logs.aggregate([
  { $indexStats: {} }
])

// Remove unused indexes
db.patient_medical_logs.dropIndex("indexName")
```

### Data Cleanup

```javascript
// Archive old records (older than 1 year)
db.patient_medical_logs.deleteMany({
  logDate: { $lt: new Date(new Date().setFullYear(new Date().getFullYear() - 1)) },
  status: "archived"
})
```

---

## Support and References

- [MongoDB Java Driver Documentation](https://mongodb.github.io/mongo-java-driver/)
- [MongoDB Best Practices](https://docs.mongodb.com/manual/core/data-modeling/)
- [BSON Specification](https://bsonspec.org/)

---

**Document Complete**

