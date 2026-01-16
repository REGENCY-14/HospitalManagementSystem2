# MongoDB Connection Setup Guide

## Overview
You now have MongoDB connection infrastructure set up for your Hospital Management System. The connection class connects to your local MongoDB instance named **hospitalnosql**.

## Files Created

### 1. **MongoDBConnection.java** (dao package)
- **Purpose**: Singleton pattern connection manager for MongoDB
- **Features**:
  - Thread-safe singleton pattern
  - POJO codec registry for automatic object mapping
  - Connection pooling via MongoClientSettings
  - Connection testing and status monitoring
  - Graceful shutdown support

### 2. **MongoDBConnectionTest.java** (dao package)
- **Purpose**: Test and example usage of MongoDB connection
- **Features**:
  - Connection validation test
  - Sample medical log creation
  - Data retrieval example
  - Proper resource cleanup

## Configuration

The connection is configured to:
- **Host**: localhost
- **Port**: 27017 (MongoDB default)
- **Database**: hospitalnosql
- **Connection String**: `mongodb://localhost:27017`

### To change the configuration:
Edit the constants in `MongoDBConnection.java`:
```java
private static final String CONNECTION_STRING = "mongodb://localhost:27017";
private static final String DATABASE_NAME = "hospitalnosql";
```

## How to Use

### Basic Connection Usage
```java
// Get singleton connection instance
MongoDBConnection mongoConnection = MongoDBConnection.getInstance();

// Get database for DAO initialization
MongoDatabase database = mongoConnection.getDatabase();

// Use with DAO
PatientMedicalLogDAO medicalLogDAO = new PatientMedicalLogDAO(database);

// Close when done
mongoConnection.closeConnection();
```

### In Your Controller/Application
```java
public class HospitalController {
    private PatientMedicalLogDAO medicalLogDAO;
    
    public HospitalController() {
        // Initialize MongoDB connection
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        
        // Create DAO instances
        this.medicalLogDAO = new PatientMedicalLogDAO(database);
    }
    
    // Use medicalLogDAO in your methods
}
```

### Create Medical Log
```java
// Get DAO
MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
PatientMedicalLogDAO dao = new PatientMedicalLogDAO(database);

// Create a medical log
PatientMedicalLog log = new PatientMedicalLog();
log.setPatientId(1001);
log.setLogType("appointment_note");
log.setTitle("Regular Checkup");
log.setStatus("completed");

// Save to MongoDB
String logId = dao.create(log);
System.out.println("Medical log created with ID: " + logId);
```

### Retrieve Medical Log
```java
PatientMedicalLogDAO dao = new PatientMedicalLogDAO(database);

// Get log by ID
PatientMedicalLog log = dao.findById(logId);

// Get all logs for patient
List<PatientMedicalLog> patientLogs = dao.findByPatientId(1001);

// Get logs in date range
List<PatientMedicalLog> logs = dao.findByPatientIdAndDateRange(
    1001,
    startDate,
    endDate
);
```

## Testing Connection

### Run the Test Class
```bash
# Compile
javac -cp ".:mongodb-driver-sync-4.11.0.jar:bson-4.11.0.jar" src/main/java/dao/MongoDBConnectionTest.java

# Run
java -cp ".:mongodb-driver-sync-4.11.0.jar:bson-4.11.0.jar" dao.MongoDBConnectionTest
```

### Expected Output
```
============================================================
MongoDB Connection Test - Hospital Management System
============================================================

MongoDB Connection Status:
  Connection String: mongodb://localhost:27017
  Database: hospitalnosql
  Status: ✓ CONNECTED

Testing connection...
✓ Connection test PASSED

✓ Database instance obtained: hospitalnosql

Initializing MongoDB DAOs...
✓ PatientMedicalLogDAO initialized

============================================================
Example: Creating a sample Medical Log
============================================================

✓ Sample medical log created with ID: 65a1b2c3d4e5f6g7h8i9j0k
✓ Medical log retrieved successfully
  - Patient ID: 1001
  - Log Type: appointment_note
  - Title: Regular Checkup - January 2026
  - Status: completed

✓ Patient has 1 medical log(s)

============================================================
✓ All tests completed successfully!
============================================================
```

## Available Methods

### Connection Management
```java
// Get database instance
MongoDatabase database = mongoConnection.getDatabase();

// Get MongoDB client
MongoClient client = mongoConnection.getMongoClient();

// Test connection
boolean isConnected = mongoConnection.testConnection();

// Get connection status
String status = mongoConnection.getConnectionStatus();

// Close connection
mongoConnection.closeConnection();
```

## MongoDB Prerequisites

Before running your application, ensure:

1. ✓ MongoDB is installed locally
2. ✓ MongoDB service is running
3. ✓ Database "hospitalnosql" exists (auto-created if doesn't exist)
4. ✓ Port 27017 is available

### Start MongoDB (if not running)

**Windows:**
```powershell
net start MongoDB
# or if using MongoDB Community
mongod
```

**Linux/Mac:**
```bash
brew services start mongodb-community
# or
mongod --dbpath /usr/local/var/mongodb
```

**Docker:**
```bash
docker run -d -p 27017:27017 --name mongodb -e MONGO_INITDB_DATABASE=hospitalnosql mongo
```

## Collections Created

When you use the DAOs, these collections are automatically created:
- `patient_medical_logs` - Medical log documents
- `patient_medical_notes` - Clinical note documents
- `vital_signs` - Vital sign measurements
- `prescriptions` - Prescription records

## Integration Points

### PatientMedicalLogDAO
```java
// Initialize with database from MongoDBConnection
MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
PatientMedicalLogDAO dao = new PatientMedicalLogDAO(database);

// Use all DAO methods:
String logId = dao.create(medicalLog);
PatientMedicalLog log = dao.findById(logId);
List<PatientMedicalLog> logs = dao.findByPatientId(patientId);
dao.update(logId, updatedLog);
dao.delete(logId);
```

## Error Handling

The connection provides helpful error messages:

```
✗ MongoDB connection failed: Connection refused
✗ MongoDB test connection failed: Socket exception
✗ Error closing MongoDB connection: Illegal state exception
```

If you see these errors:
1. Check MongoDB is running: `mongosh` (or `mongo`)
2. Check connection string matches your setup
3. Check database name exists
4. Check firewall isn't blocking port 27017

## Next Steps

1. ✓ MongoDBConnection class created
2. ✓ Connection test prepared
3. → Run MongoDBConnectionTest to verify setup
4. → Integrate with HospitalController
5. → Update your UI to use MongoDB medical logs
6. → Implement additional DAOs as needed

## Support

For MongoDB queries and operations, refer to:
- [PatientMedicalLogDAO.java](../model/PatientMedicalLog.java) - DAO methods
- [NoSQL_Implementation_Guide.md](../../docs/NoSQL_Implementation_Guide.md) - Best practices
- [NoSQL_Examples_and_Usage.md](../../docs/NoSQL_Examples_and_Usage.md) - Code examples
