package controller;

import service.PatientService;
import service.DoctorService;
import service.AppointmentService;
import model.Patient;
import model.Doctor;
import model.Appointment;
import util.PerformanceMonitor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class HospitalController {
    private Scanner scanner;
    private PerformanceMonitor performanceMonitor;
    
    /**
     * Constructor
     */
    public HospitalController() {
        this.scanner = new Scanner(System.in);
        this.performanceMonitor = new PerformanceMonitor("Hospital Operations");
    }
    
    /**
     * Main menu for the application
     */
    public void displayMainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n===== HOSPITAL MANAGEMENT SYSTEM =====");
            System.out.println("1. Patient Management");
            System.out.println("2. Doctor Management");
            System.out.println("3. Appointment Management");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    patientMenu();
                    break;
                case 2:
                    doctorMenu();
                    break;
                case 3:
                    appointmentMenu();
                    break;
                case 4:
                    running = false;
                    System.out.println("Thank you for using Hospital Management System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
    
    /**
     * Patient Management Menu
     */
    private void patientMenu() {
        System.out.println("\n===== PATIENT MANAGEMENT =====");
        System.out.println("1. Add Patient");
        System.out.println("2. View Patient");
        System.out.println("3. View All Patients");
        System.out.println("4. Update Patient");
        System.out.println("5. Delete Patient");
        System.out.println("6. Back to Main Menu");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                addPatient();
                break;
            case 2:
                viewPatient();
                break;
            case 3:
                viewAllPatients();
                break;
            case 4:
                updatePatient();
                break;
            case 5:
                deletePatient();
                break;
            case 6:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    /**
     * Add a new patient
     */
    private void addPatient() {
        System.out.println("\n--- Add Patient ---");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Date of Birth (yyyy-MM-dd): ");
        LocalDate dob = LocalDate.parse(scanner.nextLine());
        System.out.print("Gender: ");
        String gender = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Blood Type: ");
        String bloodType = scanner.nextLine();
        
        Patient patient = new Patient(firstName, lastName, dob, gender, phone, address, bloodType);
        if (PatientService.validatePatient(patient) && PatientService.createPatient(patient)) {
            System.out.println("Patient added successfully!");
        } else {
            System.out.println("Failed to add patient.");
        }
    }
    
    /**
     * View a specific patient
     */
    private void viewPatient() {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();
        Patient patient = PatientService.getPatient(patientId);
        if (patient != null) {
            System.out.println(patient);
        } else {
            System.out.println("Patient not found.");
        }
    }
    
    /**
     * View all patients
     */
    private void viewAllPatients() {
        List<Patient> patients = PatientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            System.out.println("\n--- All Patients ---");
            for (Patient patient : patients) {
                System.out.println(patient);
            }
        }
    }
    
    /**
     * Update a patient
     */
    private void updatePatient() {
        System.out.print("Enter Patient ID to update: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();
        Patient patient = PatientService.getPatient(patientId);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }
        
        System.out.print("New Phone (or press Enter to skip): ");
        String phone = scanner.nextLine();
        if (!phone.isEmpty()) {
            patient.setPhone(phone);
        }
        
        if (PatientService.updatePatient(patient)) {
            System.out.println("Patient updated successfully!");
        } else {
            System.out.println("Failed to update patient.");
        }
    }
    
    /**
     * Delete a patient
     */
    private void deletePatient() {
        System.out.print("Enter Patient ID to delete: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();
        if (PatientService.deletePatient(patientId)) {
            System.out.println("Patient deleted successfully!");
        } else {
            System.out.println("Failed to delete patient.");
        }
    }
    
    /**
     * Doctor Management Menu
     */
    private void doctorMenu() {
        System.out.println("\n===== DOCTOR MANAGEMENT =====");
        System.out.println("1. Add Doctor");
        System.out.println("2. View Doctor");
        System.out.println("3. View All Doctors");
        System.out.println("4. View Doctors by Department");
        System.out.println("5. Update Doctor");
        System.out.println("6. Delete Doctor");
        System.out.println("7. Back to Main Menu");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                addDoctor();
                break;
            case 2:
                viewDoctor();
                break;
            case 3:
                viewAllDoctors();
                break;
            case 4:
                viewDoctorsByDepartment();
                break;
            case 5:
                updateDoctor();
                break;
            case 6:
                deleteDoctor();
                break;
            case 7:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    /**
     * Add a new doctor
     */
    private void addDoctor() {
        System.out.println("\n--- Add Doctor ---");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Specialization: ");
        String specialization = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Department ID: ");
        int departmentId = scanner.nextInt();
        scanner.nextLine();
        
        Doctor doctor = new Doctor(firstName, lastName, specialization, phone, departmentId);
        if (DoctorService.validateDoctor(doctor) && DoctorService.createDoctor(doctor)) {
            System.out.println("Doctor added successfully!");
        } else {
            System.out.println("Failed to add doctor.");
        }
    }
    
    /**
     * View a specific doctor
     */
    private void viewDoctor() {
        System.out.print("Enter Doctor ID: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine();
        Doctor doctor = DoctorService.getDoctor(doctorId);
        if (doctor != null) {
            System.out.println(doctor);
        } else {
            System.out.println("Doctor not found.");
        }
    }
    
    /**
     * View all doctors
     */
    private void viewAllDoctors() {
        List<Doctor> doctors = DoctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
        } else {
            System.out.println("\n--- All Doctors ---");
            for (Doctor doctor : doctors) {
                System.out.println(doctor);
            }
        }
    }
    
    /**
     * View doctors by department
     */
    private void viewDoctorsByDepartment() {
        System.out.print("Enter Department ID: ");
        int departmentId = scanner.nextInt();
        scanner.nextLine();
        List<Doctor> doctors = DoctorService.getDoctorsByDepartment(departmentId);
        if (doctors == null || doctors.isEmpty()) {
            System.out.println("No doctors found for this department.");
        } else {
            System.out.println("\n--- Doctors in Department ---");
            for (Doctor doctor : doctors) {
                System.out.println(doctor);
            }
        }
    }
    
    /**
     * Update a doctor
     */
    private void updateDoctor() {
        System.out.print("Enter Doctor ID to update: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine();
        Doctor doctor = DoctorService.getDoctor(doctorId);
        if (doctor == null) {
            System.out.println("Doctor not found.");
            return;
        }
        
        System.out.print("New Phone (or press Enter to skip): ");
        String phone = scanner.nextLine();
        if (!phone.isEmpty()) {
            doctor.setPhone(phone);
        }
        
        if (DoctorService.updateDoctor(doctor)) {
            System.out.println("Doctor updated successfully!");
        } else {
            System.out.println("Failed to update doctor.");
        }
    }
    
    /**
     * Delete a doctor
     */
    private void deleteDoctor() {
        System.out.print("Enter Doctor ID to delete: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine();
        if (DoctorService.deleteDoctor(doctorId)) {
            System.out.println("Doctor deleted successfully!");
        } else {
            System.out.println("Failed to delete doctor.");
        }
    }
    
    /**
     * Appointment Management Menu
     */
    private void appointmentMenu() {
        System.out.println("\n===== APPOINTMENT MANAGEMENT =====");
        System.out.println("1. Schedule Appointment");
        System.out.println("2. View Appointment");
        System.out.println("3. View All Appointments");
        System.out.println("4. View Patient Appointments");
        System.out.println("5. Cancel Appointment");
        System.out.println("6. Back to Main Menu");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                scheduleAppointment();
                break;
            case 2:
                viewAppointment();
                break;
            case 3:
                viewAllAppointments();
                break;
            case 4:
                viewPatientAppointments();
                break;
            case 5:
                cancelAppointment();
                break;
            case 6:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    /**
     * Schedule an appointment
     */
    private void scheduleAppointment() {
        System.out.println("\n--- Schedule Appointment ---");
        System.out.print("Patient ID: ");
        int patientId = scanner.nextInt();
        System.out.print("Doctor ID: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Appointment Date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.print("Appointment Time (HH:mm:ss): ");
        LocalTime time = LocalTime.parse(scanner.nextLine());
        System.out.print("Notes: ");
        String notes = scanner.nextLine();
        
        Appointment appointment = new Appointment(patientId, doctorId, date, time, "Scheduled", notes);
        if (AppointmentService.validateAppointment(appointment) && AppointmentService.createAppointment(appointment)) {
            System.out.println("Appointment scheduled successfully!");
        } else {
            System.out.println("Failed to schedule appointment.");
        }
    }
    
    /**
     * View a specific appointment
     */
    private void viewAppointment() {
        System.out.print("Enter Appointment ID: ");
        int appointmentId = scanner.nextInt();
        scanner.nextLine();
        Appointment appointment = AppointmentService.getAppointment(appointmentId);
        if (appointment != null) {
            System.out.println(appointment);
        } else {
            System.out.println("Appointment not found.");
        }
    }
    
    /**
     * View all appointments
     */
    private void viewAllAppointments() {
        List<Appointment> appointments = AppointmentService.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            System.out.println("\n--- All Appointments ---");
            for (Appointment appointment : appointments) {
                System.out.println(appointment);
            }
        }
    }
    
    /**
     * View appointments for a patient
     */
    private void viewPatientAppointments() {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();
        List<Appointment> appointments = AppointmentService.getPatientAppointments(patientId);
        if (appointments == null || appointments.isEmpty()) {
            System.out.println("No appointments found for this patient.");
        } else {
            System.out.println("\n--- Patient Appointments ---");
            for (Appointment appointment : appointments) {
                System.out.println(appointment);
            }
        }
    }
    
    /**
     * Cancel an appointment
     */
    private void cancelAppointment() {
        System.out.print("Enter Appointment ID to cancel: ");
        int appointmentId = scanner.nextInt();
        scanner.nextLine();
        if (AppointmentService.cancelAppointment(appointmentId)) {
            System.out.println("Appointment cancelled successfully!");
        } else {
            System.out.println("Failed to cancel appointment.");
        }
    }
}
