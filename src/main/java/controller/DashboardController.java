package controller;

import javafx.scene.control.Tab;
import ui.components.DashboardUI;

/**
 * Pure business logic controller for Dashboard
 * Delegates UI operations to DashboardUI
 */
public class DashboardController {
    
    private DashboardUI dashboardUI;
    
    public DashboardController() {
        this.dashboardUI = new DashboardUI();
    }
    
    public Tab createDashboardTab() {
        return dashboardUI.createDashboardTab();
    }
}
