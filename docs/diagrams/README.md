# Hospital Database ERD

## Entity Relationship Diagram

This directory contains the database schema diagrams for the Hospital Management System.

### Tables

- **Patient**: Stores patient information (patient_id_PK, first_name, last_name, date_of_birth, gender, address, blood_type, created_at)
- **Doctor**: Stores doctor information (doctor_id_PK, first_name, last_name, specialization, phone, department_id_FK)
- **Department**: Manages hospital departments (department_id_PK, name UNIQUE, location)
- **Appointment**: Tracks patient appointments (appointment_id_PK, patient_id_FK, doctor_id_FK, appointment_date, appointment_time, status, notes, created_at)
- **Prescription**: Prescription records (prescription_PK, patient_id_FK, doctor_id_FK, prescription_date, diagnosis, notes, created_at)
- **PrescriptionItem**: Individual prescription items (prescription_item_id_PK, prescription_id_FK, inventory_id_FK, dosage, frequency, duration, quantity)
- **MedicalInventory**: Medical supplies inventory (inventory_id_PK, item_name, category, quantity, unit_price, expiry_date, supplier, last_updated)
- **PatientFeedback**: Patient feedback system (feedback_id_PK, patient_id_FK, doctor_id_FK, appointment_id_FK, rating, comments, feedback_date, created_at)

### Relationships

- Patient receives Appointments
- Doctor attends Appointments
- Doctor works in Department
- Prescription contains multiple PrescriptionItems
- PrescriptionItems reference MedicalInventory
- PatientFeedback about Appointments from Doctors

### Image Location
Place the ERD diagram image file (Hospital_Database_ERD.png) in this directory.
