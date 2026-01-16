package controller;

import javafx.scene.control.Tab;
import ui.components.DepartmentManagementUI;

/**
 * Controller for Department Management tab.
 * Delegates UI responsibilities to DepartmentManagementUI.
 */
public class DepartmentTabController {

    private final DepartmentManagementUI departmentUI = new DepartmentManagementUI();

    public Tab createDepartmentTab() {
        return departmentUI.createDepartmentTab();
    }
}
