-- Use the hospital database
USE hospital_db;

-- Clear existing data from tables (in correct order due to foreign keys)
DELETE FROM PatientFeedback;
DELETE FROM PrescriptionItem;
DELETE FROM Prescription;
DELETE FROM Appointment;
DELETE FROM MedicalInventory;
DELETE FROM Doctor;
DELETE FROM Patient;
DELETE FROM Department;

-- Reset auto increment counters
ALTER TABLE Department AUTO_INCREMENT = 1;
ALTER TABLE Doctor AUTO_INCREMENT = 1;
ALTER TABLE Patient AUTO_INCREMENT = 1;
ALTER TABLE Appointment AUTO_INCREMENT = 1;
ALTER TABLE MedicalInventory AUTO_INCREMENT = 1;
ALTER TABLE Prescription AUTO_INCREMENT = 1;
ALTER TABLE PrescriptionItem AUTO_INCREMENT = 1;
ALTER TABLE PatientFeedback AUTO_INCREMENT = 1;

-- Insert sample departments
INSERT INTO Department (name, location) VALUES
('Cardiology', 'Building A, Floor 2'),
('Neurology', 'Building B, Floor 3'),
('Orthopedics', 'Building A, Floor 1'),
('Pediatrics', 'Building C, Floor 1'),
('Emergency', 'Building D, Ground Floor');

-- Insert sample doctors
INSERT INTO Doctor (first_name, last_name, specialization, phone, department_id) VALUES
('John', 'Smith', 'Cardiologist', '555-0101', 1),
('Emily', 'Johnson', 'Neurologist', '555-0102', 2),
('Michael', 'Williams', 'Orthopedic Surgeon', '555-0103', 3),
('Sarah', 'Brown', 'Pediatrician', '555-0104', 4),
('David', 'Jones', 'Emergency Medicine', '555-0105', 5);

-- Insert sample patients (5 patients as required)
INSERT INTO Patient (first_name, last_name, date_of_birth, gender, phone, address, blood_type) VALUES
('Alice', 'Anderson', '1990-05-15', 'Female', '555-1001', '123 Main St, City', 'A+'),
('Bob', 'Baker', '1985-08-20', 'Male', '555-1002', '456 Oak Ave, Town', 'B+'),
('Carol', 'Clark', '1978-03-10', 'Female', '555-1003', '789 Pine Rd, Village', 'O+'),
('Daniel', 'Davis', '2010-11-25', 'Male', '555-1004', '321 Elm St, City', 'AB+'),
('Eva', 'Evans', '1995-07-30', 'Female', '555-1005', '654 Maple Dr, Town', 'A-');

-- Insert sample appointments
INSERT INTO Appointment (patient_id, doctor_id, appointment_date, appointment_time, status, notes) VALUES
(1, 1, '2026-01-10', '09:00:00', 'Scheduled', 'Regular checkup'),
(2, 3, '2026-01-11', '10:30:00', 'Scheduled', 'Knee pain consultation'),
(3, 2, '2026-01-12', '14:00:00', 'Completed', 'Follow-up visit'),
(4, 4, '2026-01-13', '11:00:00', 'Scheduled', 'Vaccination'),
(5, 5, '2026-01-09', '08:00:00', 'Completed', 'Emergency visit');

-- Insert sample medical inventory
INSERT INTO MedicalInventory (item_name, category, quantity, unit_price, expiry_date, supplier) VALUES
('Paracetamol 500mg', 'Medicine', 500, 0.50, '2027-12-31', 'PharmaCorp'),
('Ibuprofen 200mg', 'Medicine', 300, 0.75, '2027-06-30', 'MediSupply'),
('Bandages', 'Supplies', 1000, 2.00, NULL, 'HealthSupplies Inc'),
('Syringes 5ml', 'Equipment', 200, 1.50, NULL, 'MedEquip Ltd'),
('Antibiotics - Amoxicillin', 'Medicine', 150, 5.00, '2026-12-31', 'PharmaCorp');

-- Insert sample prescriptions (6 prescriptions for 5 patients)
INSERT INTO Prescription (patient_id, doctor_id, appointment_id, prescription_date, diagnosis, notes) VALUES
(1, 1, 1, '2026-01-10', 'Hypertension', 'Patient has elevated blood pressure, needs monitoring'),
(2, 3, 2, '2026-01-11', 'Knee Osteoarthritis', 'Pain management and physical therapy recommended'),
(3, 2, 3, '2026-01-12', 'Migraine', 'Patient reported severe headaches, requires medication'),
(4, 4, 4, '2026-01-13', 'Common Cold', 'Viral infection, symptomatic treatment advised'),
(5, 5, 5, '2026-01-09', 'Minor Wound', 'Laceration treatment and antibiotics prescribed'),
(1, 1, NULL, '2026-01-14', 'Follow-up Hypertension Check', 'Routine medication refill requested');

-- Insert sample prescription items (linked to prescriptions)
INSERT INTO PrescriptionItem (prescription_id, inventory_id, dosage, frequency, duration, quantity) VALUES
-- Prescription 1 (Hypertension) - Patient 1
(1, 1, '500mg', 'Twice daily', '30 days', 60),
-- Prescription 2 (Knee Osteoarthritis) - Patient 2
(2, 2, '200mg', 'Three times daily', '10 days', 30),
-- Prescription 3 (Migraine) - Patient 3
(3, 1, '500mg', 'Twice daily', '5 days', 10),
-- Prescription 4 (Common Cold) - Patient 4
(4, 5, '250mg', 'Twice daily', '7 days', 14),
-- Prescription 5 (Minor Wound) - Patient 5
(5, 5, '500mg', 'Twice daily', '10 days', 20),
-- Prescription 6 (Follow-up) - Patient 1
(6, 1, '500mg', 'Twice daily', '30 days', 60);

-- Insert sample feedback (one for each prescription)
INSERT INTO PatientFeedback (patient_id, doctor_id, appointment_id, rating, comments, feedback_date) VALUES
(1, 1, 1, 4, 'Dr. Smith was very professional and explained everything clearly', '2026-01-11'),
(2, 3, 2, 5, 'Excellent care and thorough examination by Dr. Williams', '2026-01-12'),
(3, 2, 3, 5, 'Excellent care and attention from Dr. Johnson', '2026-01-13'),
(4, 4, 4, 4, 'Dr. Brown was friendly and made my child comfortable', '2026-01-14'),
(5, 5, 5, 4, 'Great emergency care provided by Dr. Jones', '2026-01-10');
