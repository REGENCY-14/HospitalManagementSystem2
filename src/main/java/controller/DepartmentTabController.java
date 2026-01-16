package controller;

import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Department;
import service.DepartmentService;

/**
 * Controller for Department Management tab.
 */
public class DepartmentTabController {

    private TableView<Department> departmentTable;

    public Tab createDepartmentTab() {
        Tab tab = new Tab("Department Management");
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Department Management");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Department Name");
        TextField locationField = new TextField();
        locationField.setPromptText("Location");

        Button addBtn = new Button("Add Department");
        addBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        addBtn.setOnAction(e -> {
            if (nameField.getText().isEmpty()) {
                showAlert("Error", "Please enter department name");
                return;
            }
            Department dept = new Department(nameField.getText(), locationField.getText());
            if (DepartmentService.createDepartment(dept)) {
                showAlert("Success", "Department added successfully!");
                nameField.clear();
                locationField.clear();
                loadDepartmentData();
            } else {
                showAlert("Error", "Failed to add department");
            }
        });

        departmentTable = new TableView<>();
        departmentTable.setPrefHeight(400);

        TableColumn<Department, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("departmentId"));
        idCol.setPrefWidth(80);

        TableColumn<Department, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(300);

        TableColumn<Department, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationCol.setPrefWidth(300);

        departmentTable.getColumns().addAll(idCol, nameCol, locationCol);

        Button refreshBtn = new Button("Refresh List");
        refreshBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshBtn.setOnAction(e -> loadDepartmentData());

        Button editBtn = new Button("Edit Selected");
        editBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        editBtn.setOnAction(e -> {
            Department selected = departmentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                nameField.setText(selected.getName());
                locationField.setText(selected.getLocation());
                addBtn.setText("Update Department");
                addBtn.setOnAction(evt -> {
                    selected.setName(nameField.getText());
                    selected.setLocation(locationField.getText());
                    if (DepartmentService.updateDepartment(selected)) {
                        showAlert("Success", "Department updated!");
                        departmentTable.refresh();
                        nameField.clear();
                        locationField.clear();
                        addBtn.setText("Add Department");
                        loadDepartmentData();
                        addBtn.setOnAction(thisAdd -> {
                            if (nameField.getText().isEmpty()) {
                                showAlert("Error", "Please enter department name");
                                return;
                            }
                            Department deptNew = new Department(nameField.getText(), locationField.getText());
                            if (DepartmentService.createDepartment(deptNew)) {
                                showAlert("Success", "Department added successfully!");
                                nameField.clear();
                                locationField.clear();
                                loadDepartmentData();
                            } else {
                                showAlert("Error", "Failed to add department");
                            }
                        });
                    } else {
                        showAlert("Error", "Failed to update");
                    }
                });
            } else {
                showAlert("Error", "Please select a department");
            }
        });

        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        deleteBtn.setOnAction(e -> {
            Department selected = departmentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (DepartmentService.deleteDepartment(selected.getDepartmentId())) {
                    showAlert("Success", "Department deleted!");
                    departmentTable.getItems().remove(selected);
                } else {
                    showAlert("Error", "Failed to delete");
                }
            } else {
                showAlert("Error", "Please select a department");
            }
        });

        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(nameField, locationField, addBtn);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(refreshBtn, editBtn, deleteBtn);

        root.getChildren().addAll(
            titleLabel,
            new Separator(),
            new Label("Add New Department:"),
            inputBox,
            new Separator(),
            new Label("Department List:"),
            buttonBox,
            departmentTable
        );

        tab.setContent(root);
        return tab;
    }

    public void loadDepartmentData() {
        if (departmentTable != null) {
            departmentTable.getItems().clear();
            List<Department> departments = DepartmentService.getAllDepartments();
            if (departments != null) {
                departmentTable.getItems().addAll(departments);
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
