-- Create the hospital database
CREATE DATABASE hospital_db;

-- Select the database for use
USE hospital_db;

-- Stores patient personal and contact information
CREATE TABLE Patient (
    patient_id INT PRIMARY KEY AUTO_INCREMENT, -- Unique patient identifier
    first_name VARCHAR(50) NOT NULL,            -- Patient first name
    last_name VARCHAR(50) NOT NULL,             -- Patient last name
    date_of_birth DATE NOT NULL,                -- Date of birth
    gender VARCHAR(10),                         -- Gender
    phone VARCHAR(20),                          -- Contact number
    address TEXT,                               -- Residential address
    blood_type VARCHAR(5),                      -- Blood group
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Record creation time
);

-- Stores hospital departments
CREATE TABLE Department (
    department_id INT PRIMARY KEY AUTO_INCREMENT, -- Unique department ID
    name VARCHAR(100) NOT NULL UNIQUE,             -- Department name
    location VARCHAR(100)                          -- Department location
);

-- Stores doctor information and department association
CREATE TABLE Doctor (
    doctor_id INT PRIMARY KEY AUTO_INCREMENT, -- Unique doctor ID
    first_name VARCHAR(50) NOT NULL,           -- Doctor first name
    last_name VARCHAR(50) NOT NULL,            -- Doctor last name
    specialization VARCHAR(100),               -- Area of specialization
    phone VARCHAR(20),                          -- Contact number
    department_id INT NOT NULL,                -- Associated department
    FOREIGN KEY (department_id) REFERENCES Department(department_id)
);

-- Manages appointments between patients and doctors
CREATE TABLE Appointment (
    appointment_id INT PRIMARY KEY AUTO_INCREMENT, -- Unique appointment ID
    patient_id INT NOT NULL,                       -- Patient reference
    doctor_id INT NOT NULL,                        -- Doctor reference
    appointment_date DATE NOT NULL,                -- Appointment date
    appointment_time TIME NOT NULL,                -- Appointment time
    status VARCHAR(20) DEFAULT 'Scheduled',        -- Appointment status (Scheduled, Completed, Cancelled)
    notes TEXT,                                    -- Additional appointment notes
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Record creation time

    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE CASCADE
);

-- Stores medical inventory items (medicines, equipment, supplies)
CREATE TABLE MedicalInventory (
    inventory_id INT PRIMARY KEY AUTO_INCREMENT,   -- Unique inventory item ID
    item_name VARCHAR(100) NOT NULL,               -- Name of the item
    category VARCHAR(50),                          -- Category (Medicine, Equipment, Supplies)
    quantity INT NOT NULL DEFAULT 0,               -- Available quantity
    unit_price DECIMAL(10, 2),                     -- Price per unit
    expiry_date DATE,                              -- Expiry date (for medicines)
    supplier VARCHAR(100),                         -- Supplier name
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Last update time
);

-- Stores prescriptions issued by doctors to patients
CREATE TABLE Prescription (
    prescription_id INT PRIMARY KEY AUTO_INCREMENT, -- Unique prescription ID
    patient_id INT NOT NULL,                        -- Patient reference
    doctor_id INT NOT NULL,                         -- Prescribing doctor reference
    appointment_id INT,                             -- Related appointment (optional)
    prescription_date DATE NOT NULL,                -- Date prescription was issued
    diagnosis TEXT,                                 -- Medical diagnosis
    notes TEXT,                                     -- Additional notes
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Record creation time

    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id) ON DELETE SET NULL
);

-- Stores individual items within a prescription (medicines and dosage)
CREATE TABLE PrescriptionItem (
    prescription_item_id INT PRIMARY KEY AUTO_INCREMENT, -- Unique item ID
    prescription_id INT NOT NULL,                        -- Prescription reference
    inventory_id INT NOT NULL,                           -- Medicine/item from inventory
    dosage VARCHAR(100),                                 -- Dosage instructions
    frequency VARCHAR(50),                               -- Frequency (e.g., "Twice daily")
    duration VARCHAR(50),                                -- Duration (e.g., "7 days")
    quantity INT NOT NULL,                               -- Quantity prescribed

    FOREIGN KEY (prescription_id) REFERENCES Prescription(prescription_id) ON DELETE CASCADE,
    FOREIGN KEY (inventory_id) REFERENCES MedicalInventory(inventory_id) ON DELETE CASCADE
);

-- Stores patient feedback and ratings
CREATE TABLE PatientFeedback (
    feedback_id INT PRIMARY KEY AUTO_INCREMENT,     -- Unique feedback ID
    patient_id INT NOT NULL,                        -- Patient reference
    doctor_id INT,                                  -- Doctor reference (optional)
    appointment_id INT,                             -- Related appointment (optional)
    rating INT CHECK (rating BETWEEN 1 AND 5),     -- Rating from 1 to 5
    comments TEXT,                                  -- Patient comments
    feedback_date DATE NOT NULL,                    -- Date feedback was given
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Record creation time

    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE SET NULL,
    FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id) ON DELETE SET NULL
);

-- ============================================================
-- INDEXES FOR PERFORMANCE OPTIMIZATION
-- ============================================================

-- Index to speed up patient search by last name
CREATE INDEX idx_patient_last_name ON Patient(last_name);

-- Index to speed up patient search by first name
CREATE INDEX idx_patient_first_name ON Patient(first_name);

-- Index to optimize appointment date queries
CREATE INDEX idx_appointment_date ON Appointment(appointment_date);

-- Index for appointment status filtering
CREATE INDEX idx_appointment_status ON Appointment(status);

-- Index to improve doctor lookup by department
CREATE INDEX idx_doctor_department ON Doctor(department_id);

-- Index for doctor specialization searches
CREATE INDEX idx_doctor_specialization ON Doctor(specialization);

-- Index for prescription searches by patient
CREATE INDEX idx_prescription_patient ON Prescription(patient_id);

-- Index for prescription searches by doctor
CREATE INDEX idx_prescription_doctor ON Prescription(doctor_id);

-- Index for prescription date queries
CREATE INDEX idx_prescription_date ON Prescription(prescription_date);

-- Index for inventory item searches
CREATE INDEX idx_inventory_item_name ON MedicalInventory(item_name);

-- Index for inventory category filtering
CREATE INDEX idx_inventory_category ON MedicalInventory(category);

-- Index for feedback by patient
CREATE INDEX idx_feedback_patient ON PatientFeedback(patient_id);

-- Index for feedback by doctor
CREATE INDEX idx_feedback_doctor ON PatientFeedback(doctor_id);

-- ============================================================
-- SAMPLE DATA INSERTION
-- ============================================================

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

-- Insert sample patients
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

-- Insert sample prescriptions
INSERT INTO Prescription (patient_id, doctor_id, appointment_id, prescription_date, diagnosis, notes) VALUES
(3, 2, 3, '2026-01-12', 'Migraine', 'Patient reported severe headaches');

-- Insert sample prescription items
INSERT INTO PrescriptionItem (prescription_id, inventory_id, dosage, frequency, duration, quantity) VALUES
(1, 1, '500mg', 'Twice daily', '5 days', 10);

-- Insert sample feedback
INSERT INTO PatientFeedback (patient_id, doctor_id, appointment_id, rating, comments, feedback_date) VALUES
(3, 2, 3, 5, 'Excellent care and attention', '2026-01-13'),
(1, 1, 1, 4, 'Good consultation', '2026-01-11');
