package ui.components;

import javafx.scene.control.TableView;
import model.Department;
import service.DepartmentService;

import java.util.List;

/**
 * Business logic controller for Department UI
 */
public class DepartmentUIController {

    private final DepartmentManagementUI ui;

    public DepartmentUIController(DepartmentManagementUI ui) {
        this.ui = ui;
    }

    public void loadDepartmentData() {
        TableView<Department> table = ui.getDepartmentTable();
        if (table != null) {
            table.getItems().clear();
            List<Department> departments = DepartmentService.getAllDepartments();
            if (departments != null) {
                table.getItems().addAll(departments);
            }
        }
    }

    public void handleAddDepartment() {
        String name = ui.getDepartmentName();
        String location = ui.getLocation();
        if (name == null || name.trim().isEmpty()) {
            ui.showWarning("Validation", "Please enter department name");
            return;
        }

        Department dept = new Department(name.trim(), location != null ? location.trim() : "");
        if (DepartmentService.createDepartment(dept)) {
            ui.showAlert("Success", "Department added successfully!");
            ui.clearForm();
            loadDepartmentData();
        } else {
            ui.showError("Error", "Failed to add department");
        }
    }

    public void handleEditSelected() {
        Department selected = ui.getDepartmentTable().getSelectionModel().getSelectedItem();
        if (selected != null) {
            ui.setDepartmentName(selected.getName());
            ui.setLocation(selected.getLocation());
            ui.setAddButtonToUpdate(() -> updateDepartment(selected));
        } else {
            ui.showWarning("Selection", "Please select a department");
        }
    }

    private void updateDepartment(Department selected) {
        selected.setName(ui.getDepartmentName());
        selected.setLocation(ui.getLocation());
        if (DepartmentService.updateDepartment(selected)) {
            ui.showAlert("Success", "Department updated!");
            ui.getDepartmentTable().refresh();
            ui.clearForm();
            ui.resetAddButton();
            loadDepartmentData();
        } else {
            ui.showError("Error", "Failed to update");
        }
    }

    public void handleDeleteSelected() {
        Department selected = ui.getDepartmentTable().getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (DepartmentService.deleteDepartment(selected.getDepartmentId())) {
                ui.showAlert("Success", "Department deleted!");
                ui.getDepartmentTable().getItems().remove(selected);
            } else {
                ui.showError("Error", "Failed to delete");
            }
        } else {
            ui.showWarning("Selection", "Please select a department");
        }
    }
}
