# Hospital Management System 2

A comprehensive Hospital Management System built with Java, Java Swing, and MySQL. This system implements database design (3NF normalization), CRUD operations, performance optimization, and integration with a modern GUI framework.

## Project Overview

**Complexity**: Advanced  
**Development Time**: 15+ hours  
**Target**: Azure AKS, Azure App Service, Cloud Readiness, Linux, OpenJDK 11, OpenJDK 17, OpenJDK 21  
**Architecture**: MVC with Services/DAO (Model Context Protocol)

## Technology Stack

- **Java Version**: Java 11, JDBC
- **Frontend**: Java Swing 2.1.0
- **Database**: MySQL 8.0
- **Build Tool**: Maven 3
- **Architecture**: MVC with Services/DAO Layers

## Project Features

### 1. Patient Management
- Add, view, update, delete patient records
- Search patients by name or ID
- Store medical history and contact information
- Input validation and data integrity checks

### 2. Doctor Management
- Track doctor specialization and parameters
- View doctor availability
- Search and filter doctors by specialization

### 3. Appointment Management
- Schedule appointments between patients and doctors
- Track appointment status (Scheduled, Completed, Cancelled)
- View appointment history
- Appointment data and time validation

### 4. Medical Inventory Management
- Track medical supplies and equipment
- Monitor inventory levels
- Record usage and restocking
- Alert system for low stock times

### 5. Prescription Management
- Create and manage prescriptions
- Track prescribed medications and dosages
- Link prescriptions to appointments and patients
- Prescription history and validation

### 6. Department Management
- Organize doctors by departments
- Manage department information
- Track department-specific operations

### 7. Patient Feedback System
- Collect patient feedback and ratings
- Track patient satisfaction
- Generate feedback reports

### 8. Performance Monitoring & Optimization
- In-memory caching for frequently accessed data
- Query optimization with database indexing
- Performance metrics tracking
- Before/after optimization comparison reports

### 9. Dashboard & Reporting
- System statistics and overview
- Query performance metrics
- Data visualization and summaries

## Database Schema

The database uses Entity-Relationships and 3NF normalization:

### Core Entities:
- **Patients**: Patient demographics and contact information
- **Doctors**: Doctor profiles and specializations
- **Appointments**: Patient-doctor appointment records
- **Prescriptions**: Prescription records
- **Prescription Items**: Individual medications and dosages
- **Patient Feedback**: Patient satisfaction feedback
- **Medical Inventory**: Medical supplies and equipment
- **Departments**: Hospital departments

### Key Relationships:
- Patients (1:M) Appointments
- Doctors (1:M) Appointments
- Patients (1:M) Prescriptions
- Prescriptions (1:M) Prescription Items
- Patients (1:M) Patient Feedback
- Medical Inventory (1:M) Equipment

### Data Normalization

All tables normalized to 3NF:
- Elimination of redundant data
- Removal of transitive dependencies
- Composite indexes for frequently filtered columns

## Installation & Setup

### Prerequisites:
- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

### Step 1: Clone the Repository
```bash
git clone https://github.com/REGENCY-14/HospitalManagementSystem2.git
cd HospitalManagementSystem2
```

### Step 2: Create Database and Load Schema
```bash
mysql -u root -p
SOURCE database/hospital_schema.sql
```

### Step 3: Maven Clean Install
```bash
mvn clean install
```

### Step 4: Run the Application

**Option 1: Windows**
```bash
./run.bat
```

**Option 2: Linux/macOS**
```bash
./run.sh
```

**Option 3: Direct Execution**
```bash
mvn clean exec:java -Dexec.mainClass="org.example.Main"
```

## Performance Optimization Strategies

### 1. Database Level
- Indexed columns on high-frequency lookup columns (patient ID, doctor ID, date)
- Cached execution plans with EXPLAIN & ANALYZE
- Connection pooling for JDBC

### 2. Application Level
- HashMap caching for patient/doctor lookups
- Lazy loading for related entities
- Query optimization with filtered columns

### 3. Search Optimization
- Binary search on sorted patient/doctor lists
- Case-insensitive substring matching for name searches
- Indexed database queries for fast lookups

### 4. Measurement & Monitoring
- Query execution time tracking
- Cache hit/miss statistics
- Performance comparison reports

## Troubleshooting

### Database Connection Issues
**Error**: "Failed to establish database connection"

**Solution**:
1. Verify MySQL is running
2. Check credentials in DBConnection.java
3. Confirm database exists: SHOW DATABASES;
4. Verify schema is loaded: USE hospital; SHOW TABLES;

### Java/Maven Version Errors
**Error**: "Java version not compatible" or "Maven not found"

**Solution**:
1. Verify Java version: `java -version` (should be 11 or higher)
2. Install Maven: `mvn --version` (should be 3.6+)
3. Check JAVA_HOME environment variable

### Compilation Errors
**Error**: "Cannot find symbol" or "Package not found"

**Solution**:
1. Ensure all dependencies installed: `mvn clean install`
2. Check internet connection for dependency download
3. Verify pom.xml configuration

## License

This project is provided for educational purposes. Feel free to use, modify, and distribute for learning.

## Contact & Support

**Project**: Hospital Management System  
**GitHub**: https://github.com/REGENCY-14/HospitalManagementSystem2  
**Created**: January 2026

---

## Development Notes

### Future Enhancements
- Advanced search with filters and pagination
- REST API implementation for patient/medical/logs
- Mobile app integration
- Machine learning for appointment predictions
- SMS/Email notifications for reminders

### Advanced Reporting and Analytics
- Reporting and analytics suite
- Collaborative data models
- Specialized queries for research
- Real-time monitoring and alerts

### Status
✓ Active Development
✓ Last Updated: January 14, 2026
