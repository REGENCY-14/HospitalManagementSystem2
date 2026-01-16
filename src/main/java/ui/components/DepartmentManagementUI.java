package ui.components;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Department;

/**
 * UI Component for Department Management (programmatic UI)
 * Moves all UI-related code out of controller.
 */
public class DepartmentManagementUI extends BaseTabController {

    private TableView<Department> departmentTable;
    private TextField nameField;
    private TextField locationField;
    private Button addBtn;
    private Button refreshBtn;
    private Button editBtn;
    private Button deleteBtn;

    private final DepartmentUIController uiController;

    public DepartmentManagementUI() {
        this.uiController = new DepartmentUIController(this);
    }

    public Tab createDepartmentTab() {
        Tab tab = new Tab("Department Management");
        tab.setClosable(false);

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Department Management");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        nameField = new TextField();
        nameField.setPromptText("Department Name");
        locationField = new TextField();
        locationField.setPromptText("Location");

        addBtn = new Button("Add Department");
        addBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        addBtn.setOnAction(e -> uiController.handleAddDepartment());

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

        refreshBtn = new Button("Refresh List");
        refreshBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshBtn.setOnAction(e -> uiController.loadDepartmentData());

        editBtn = new Button("Edit Selected");
        editBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        editBtn.setOnAction(e -> uiController.handleEditSelected());

        deleteBtn = new Button("Delete Selected");
        deleteBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        deleteBtn.setOnAction(e -> uiController.handleDeleteSelected());

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
        uiController.loadDepartmentData();
        return tab;
    }

    @Override
    public Tab createTab() {
        return createDepartmentTab();
    }

    // Accessors used by controller logic
    public String getDepartmentName() { return nameField.getText(); }
    public String getLocation() { return locationField.getText(); }
    public void setDepartmentName(String name) { nameField.setText(name); }
    public void setLocation(String location) { locationField.setText(location); }
    public void resetAddButton() { addBtn.setText("Add Department"); addBtn.setOnAction(e -> uiController.handleAddDepartment()); }
    public void setAddButtonToUpdate(Runnable onUpdate) { addBtn.setText("Update Department"); addBtn.setOnAction(e -> onUpdate.run()); }
    public TableView<Department> getDepartmentTable() { return departmentTable; }
    public void clearForm() { nameField.clear(); locationField.clear(); }
}
