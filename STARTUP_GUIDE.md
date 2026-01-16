# Hospital Management System - Startup Guide

## Prerequisites

### ✅ MongoDB Setup
Before running the application, ensure MongoDB is running:

**Windows (Command Prompt):**
```bash
mongod
```

**Windows (if MongoDB installed as service):**
```powershell
net start MongoDB
```

**Docker:**
```bash
docker run -d -p 27017:27017 --name hospitaldb -e MONGO_INITDB_DATABASE=hospitalnosql mongo
```

**Verify MongoDB is running:**
```bash
mongosh
> show databases
> use hospitalnosql
> db.patient_medical_logs.count()
```

### ✅ Java & Maven

Verify installations:
```bash
java -version
mvn -version
```

Required:
- Java 11 or higher
- Maven 3.8.0 or higher

## Building the Project

### Step 1: Clean and Compile
```bash
cd HospitalManagementSystem2
mvn clean compile
```

Expected output:
```
[INFO] BUILD SUCCESS
[INFO] Total time: X.XXX s
```

### Step 2: Verify Dependencies

Maven will automatically download:
- ✅ JavaFX 21.0.1
- ✅ MongoDB Java Driver 4.11.0
- ✅ BSON library
- ✅ MySQL JDBC 8.2.0
- ✅ JUnit 4.13.2

## Running the Application

### Option 1: Maven JavaFX Plugin (Recommended)
```bash
mvn javafx:run
```

This will:
1. Compile the project
2. Start the JavaFX application
3. Initialize MongoDB connection on startup
4. Display the Hospital Management System UI with Medical Logs tab

### Option 2: Direct JAR Execution
```bash
mvn package
java -jar target/HospitalManagementSystem2-1.0.0.jar
```

### Option 3: IDE Execution
**IntelliJ IDEA:**
1. Right-click `Main.java`
2. Select "Run 'Main.main()'"

**Eclipse:**
1. Right-click `Main.java`
2. Select "Run As" → "Java Application"

## What Happens at Startup

When you run the application:

```
1. JavaFX Window Opens
   ↓
2. Main.java starts
   ├─ Creates TabPane
   ├─ Creates MedicalLogController
   │   ├─ Initializes ConnectionStatusLabel
   │   ├─ Connects to MongoDB
   │   ├─ Displays connection status
   │   └─ Creates Medical Logs UI
   │
   ├─ Creates Patient Management Tab
   ├─ Creates Doctor Management Tab
   ├─ Creates Appointment Management Tab
   ├─ Creates Medical Logs Tab (NoSQL) ← NEW
   └─ Creates Dashboard Tab
   ↓
3. UI is fully interactive
```

## Expected Console Output

```
========================================
Hospital Management System - Starting...
========================================

✓ Successfully connected to MongoDB database: hospitalnosql
```

## Medical Logs Tab Features

Once application starts, go to **"Medical Logs (NoSQL)"** tab to:

1. **Create Medical Log**
   - Enter Patient ID
   - Select Log Type
   - Enter Notes/Diagnosis
   - Click "Create Medical Log"

2. **Search Medical Logs**
   - Enter Patient ID
   - Click "Search by Patient ID"
   - View results in table

3. **View Details**
   - Click any log in table
   - View details in bottom panel
   - Click "View Full Details" for complete information

4. **Update/Delete**
   - Select a log
   - Click "Update Log" or "Delete Log"
   - Confirm changes

## Troubleshooting

### Problem: "MongoDB connection failed"
**Solution:**
```bash
# Check if MongoDB is running
mongosh

# If not installed, install via Docker
docker run -d -p 27017:27017 mongo

# Verify database exists
mongosh
> use hospitalnosql
> db.createCollection("patient_medical_logs")
```

### Problem: "Cannot find symbol MedicalLogController"
**Solution:**
```bash
mvn clean compile -X
# Check for compilation errors
```

### Problem: JAR not found for `mvn package`
**Solution:**
```bash
# Check pom.xml has jar packaging
# Add if missing:
<packaging>jar</packaging>
```

### Problem: "Port 27017 already in use"
**Solution:**
```bash
# Change MongoDB port in MongoDBConnection.java
# Line 22: Change "mongodb://localhost:27017" to different port
```

### Problem: JavaFX window doesn't appear
**Solution:**
1. Ensure JavaFX 21.0.1 is in pom.xml
2. Check terminal for error messages
3. Run with verbose: `mvn javafx:run -e`

### Problem: "ClassNotFoundException: MongoDBConnection"
**Solution:**
```bash
# Ensure MongoDBConnection.java exists in src/main/java/dao/
ls -la src/main/java/dao/MongoDBConnection.java

# Recompile
mvn clean compile
```

## Project Structure

```
HospitalManagementSystem2/
├── pom.xml                          # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── org/example/
│   │   │   │   └── Main.java       # JavaFX Application (UPDATED)
│   │   │   ├── controller/
│   │   │   │   ├── HospitalController.java
│   │   │   │   └── MedicalLogController.java  # NEW - NoSQL/MongoDB
│   │   │   ├── dao/
│   │   │   │   ├── MongoDBConnection.java    # NEW - MongoDB Connection
│   │   │   │   ├── MongoDBConnectionTest.java # NEW - Connection Test
│   │   │   │   ├── PatientMedicalLogDAO.java # NEW - NoSQL DAO
│   │   │   │   └── ...other DAOs
│   │   │   ├── model/
│   │   │   │   ├── PatientMedicalLog.java    # NEW - NoSQL Model
│   │   │   │   ├── PatientMedicalNote.java   # NEW - NoSQL Model
│   │   │   │   └── ...other models
│   │   │   ├── service/
│   │   │   └── util/
│   │   └── resources/
│   └── test/
└── docs/
    ├── JAVAFX_NOSQL_INTEGRATION.md
    ├── MONGODB_CONNECTION_SETUP.md
    └── ...other docs
```

## Configuration Files

### pom.xml
- Contains all Maven dependencies
- Configured for Java 11 compilation
- Includes JavaFX Maven plugin

### MongoDBConnection.java
```java
// Modify these constants to change database:
private static final String CONNECTION_STRING = "mongodb://localhost:27017";
private static final String DATABASE_NAME = "hospitalnosql";
```

## Performance Tips

1. **Use proper indexes** - PatientMedicalLogDAO creates indexes automatically
2. **Enable connection pooling** - Handled by MongoDBConnection singleton
3. **Use background threads** - MedicalLogController uses threads for queries
4. **Monitor MongoDB** - Check logs for slow queries

## Testing the Integration

### Test 1: Verify MongoDB Connection
```bash
mvn compile
# Then in application, check "Medical Logs (NoSQL)" tab
# Status should show: ✓ Connected
```

### Test 2: Create a Medical Log
```
1. Go to "Medical Logs (NoSQL)" tab
2. Enter Patient ID: 1001
3. Select Log Type: appointment_note
4. Enter Notes: "Test medical log"
5. Click "Create Medical Log"
6. Verify success message
```

### Test 3: Search Medical Logs
```
1. Enter Patient ID: 1001
2. Click "Search by Patient ID"
3. Results appear in table
```

### Test 4: Verify in MongoDB
```bash
mongosh
> use hospitalnosql
> db.patient_medical_logs.find({patientId: 1001})
```

## Documentation

- **JAVAFX_NOSQL_INTEGRATION.md** - Complete NoSQL/MongoDB integration guide
- **MONGODB_CONNECTION_SETUP.md** - Connection setup and configuration
- **NoSQL_Implementation_Guide.md** - Full implementation details
- **Performance_Comparison_Report.md** - Performance metrics

## Next Steps

1. ✅ MongoDB setup complete
2. ✅ Application compiles successfully
3. ✅ Code structure verified
4. → Run: `mvn javafx:run`
5. → Navigate to "Medical Logs (NoSQL)" tab
6. → Create/Search/Update/Delete medical logs
7. → Test MongoDB integration

## Support

For errors:
1. Check MongoDB is running
2. Verify all files compiled
3. Check console output for error messages
4. Review documentation files
5. Examine MongoDBConnection logs

## Build Commands Reference

```bash
# Clean build
mvn clean

# Compile only
mvn compile

# Compile and run tests
mvn test

# Package as JAR
mvn package

# Run JavaFX application
mvn javafx:run

# Run with verbose output
mvn javafx:run -e -X

# Skip tests during build
mvn clean package -DskipTests

# Run specific test
mvn test -Dtest=MongoDBConnectionTest
```

---

**Application is ready to run! Use `mvn javafx:run` to start.**
