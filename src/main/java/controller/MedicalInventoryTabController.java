package controller;

import java.time.LocalDate;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.MedicalInventory;
import service.MedicalInventoryService;

/**
 * Controller for Medical Inventory tab.
 */
public class MedicalInventoryTabController {

    private TableView<MedicalInventory> inventoryTable;

    public Tab createMedicalInventoryTab() {
        Tab tab = new Tab("Medical Inventory");
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Medical Inventory Management");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        TextField itemNameField = new TextField();
        itemNameField.setPromptText("Item Name");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");
        TextField priceField = new TextField();
        priceField.setPromptText("Unit Price");
        TextField supplierField = new TextField();
        supplierField.setPromptText("Supplier");
        DatePicker expiryPicker = new DatePicker();
        expiryPicker.setPromptText("Expiry Date");

        Button addBtn = new Button("Add Item");
        addBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        addBtn.setOnAction(e -> {
            try {
                MedicalInventory item = new MedicalInventory(
                    itemNameField.getText(),
                    categoryField.getText(),
                    Integer.parseInt(quantityField.getText()),
                    Double.parseDouble(priceField.getText()),
                    expiryPicker.getValue(),
                    supplierField.getText()
                );
                if (MedicalInventoryService.createInventoryItem(item)) {
                    showAlert("Success", "Item added!");
                    clearFields(itemNameField, categoryField, quantityField, priceField, supplierField, expiryPicker);
                    loadInventoryData();
                } else {
                    showAlert("Error", "Failed to add item");
                }
            } catch (Exception ex) {
                showAlert("Error", "Invalid input: " + ex.getMessage());
            }
        });

        inventoryTable = new TableView<>();
        inventoryTable.setPrefHeight(400);

        TableColumn<MedicalInventory, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        idCol.setPrefWidth(60);

        TableColumn<MedicalInventory, String> nameCol = new TableColumn<>("Item Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        nameCol.setPrefWidth(200);

        TableColumn<MedicalInventory, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(120);

        TableColumn<MedicalInventory, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setPrefWidth(100);

        TableColumn<MedicalInventory, Double> priceCol = new TableColumn<>("Unit Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        priceCol.setPrefWidth(100);

        TableColumn<MedicalInventory, LocalDate> expiryCol = new TableColumn<>("Expiry Date");
        expiryCol.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        expiryCol.setPrefWidth(120);

        TableColumn<MedicalInventory, String> supplierCol = new TableColumn<>("Supplier");
        supplierCol.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        supplierCol.setPrefWidth(150);

        inventoryTable.getColumns().addAll(idCol, nameCol, categoryCol, quantityCol, priceCol, expiryCol, supplierCol);

        Button refreshBtn = new Button("Refresh List");
        refreshBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshBtn.setOnAction(e -> loadInventoryData());

        Button editBtn = new Button("Edit Selected");
        editBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        editBtn.setOnAction(e -> {
            MedicalInventory selected = inventoryTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                itemNameField.setText(selected.getItemName());
                categoryField.setText(selected.getCategory());
                quantityField.setText(String.valueOf(selected.getQuantity()));
                priceField.setText(String.valueOf(selected.getUnitPrice()));
                supplierField.setText(selected.getSupplier());
                expiryPicker.setValue(selected.getExpiryDate());
                addBtn.setText("Update Item");
                addBtn.setOnAction(evt -> {
                    try {
                        selected.setItemName(itemNameField.getText());
                        selected.setCategory(categoryField.getText());
                        selected.setQuantity(Integer.parseInt(quantityField.getText()));
                        selected.setUnitPrice(Double.parseDouble(priceField.getText()));
                        selected.setSupplier(supplierField.getText());
                        selected.setExpiryDate(expiryPicker.getValue());
                        if (MedicalInventoryService.updateInventoryItem(selected)) {
                            showAlert("Success", "Item updated!");
                            inventoryTable.refresh();
                            clearFields(itemNameField, categoryField, quantityField, priceField, supplierField, expiryPicker);
                            addBtn.setText("Add Item");
                            loadInventoryData();
                            addBtn.setOnAction(addEvt -> {
                                try {
                                    MedicalInventory newItem = new MedicalInventory(
                                        itemNameField.getText(),
                                        categoryField.getText(),
                                        Integer.parseInt(quantityField.getText()),
                                        Double.parseDouble(priceField.getText()),
                                        expiryPicker.getValue(),
                                        supplierField.getText()
                                    );
                                    if (MedicalInventoryService.createInventoryItem(newItem)) {
                                        showAlert("Success", "Item added!");
                                        clearFields(itemNameField, categoryField, quantityField, priceField, supplierField, expiryPicker);
                                        loadInventoryData();
                                    } else {
                                        showAlert("Error", "Failed to add item");
                                    }
                                } catch (Exception ex2) {
                                    showAlert("Error", "Invalid input: " + ex2.getMessage());
                                }
                            });
                        } else {
                            showAlert("Error", "Failed to update");
                        }
                    } catch (Exception ex) {
                        showAlert("Error", "Invalid input: " + ex.getMessage());
                    }
                });
            } else {
                showAlert("Error", "Please select an item");
            }
        });

        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        deleteBtn.setOnAction(e -> {
            MedicalInventory selected = inventoryTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (MedicalInventoryService.deleteInventoryItem(selected.getInventoryId())) {
                    showAlert("Success", "Item deleted!");
                    inventoryTable.getItems().remove(selected);
                } else {
                    showAlert("Error", "Failed to delete");
                }
            }
        });

        HBox inputBox1 = new HBox(10);
        inputBox1.getChildren().addAll(itemNameField, categoryField, quantityField);
        HBox inputBox2 = new HBox(10);
        inputBox2.getChildren().addAll(priceField, supplierField, expiryPicker, addBtn);
        VBox inputSection = new VBox(10);
        inputSection.getChildren().addAll(inputBox1, inputBox2);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(refreshBtn, editBtn, deleteBtn);

        root.getChildren().addAll(
            titleLabel,
            new Separator(),
            new Label("Add New Inventory Item:"),
            inputSection,
            new Separator(),
            new Label("Inventory List:"),
            buttonBox,
            inventoryTable
        );

        tab.setContent(root);
        return tab;
    }

    public void loadInventoryData() {
        if (inventoryTable != null) {
            inventoryTable.getItems().clear();
            List<MedicalInventory> items = MedicalInventoryService.getAllInventoryItems();
            if (items != null) {
                inventoryTable.getItems().addAll(items);
            }
        }
    }

    private void clearFields(TextField itemNameField, TextField categoryField, TextField quantityField,
                             TextField priceField, TextField supplierField, DatePicker expiryPicker) {
        itemNameField.clear();
        categoryField.clear();
        quantityField.clear();
        priceField.clear();
        supplierField.clear();
        expiryPicker.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
