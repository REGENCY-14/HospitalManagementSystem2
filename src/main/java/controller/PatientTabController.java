package controller;

import javafx.scene.control.Tab;
import ui.components.PatientManagementUI;

/**
 * Pure business logic controller for Patient Management
 * Delegates UI operations to PatientManagementUI
 */
public class PatientTabController {
    
    private PatientManagementUI patientUI;
    
    public PatientTabController() {
        this.patientUI = new PatientManagementUI();
    }
    
    public Tab createPatientTab() {
        return patientUI.createPatientTab();
    }
}
