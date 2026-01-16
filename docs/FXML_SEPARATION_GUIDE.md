# FXML Separation Refactoring Guide

## Overview
This document outlines the process used to separate UI (FXML) from Controller logic.

## Completed Refactoring
✅ Dashboard - Converted to FXML with FXMLLoader
✅ Patient - Converted to FXML with FXMLLoader

## Remaining Controllers to Refactor
- DoctorTabController
- AppointmentTabController  
- DepartmentTabController
- MedicalInventoryTabController
- MedicalLogController
- PatientFeedbackTabController
- PrescriptionTabController
- HospitalController

## Refactoring Pattern

### Step 1: Create FXML File
Create a `.fxml` file in `src/main/resources/fxml/` containing the UI layout.

Example structure:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.geometry.Insets?>

<VBox xmlns="http://javafx.com/javafx/8.0.171" xmlns:fx="http://javafx.com/fxml/1">
    <!-- UI Elements -->
</VBox>
```

### Step 2: Add @FXML Annotations
Add `@FXML` annotations to match FXML `fx:id` attributes:

```java
@FXML
private TextField nameField;

@FXML
private Button submitBtn;
```

### Step 3: Replace UI Creation Code
Replace programmatic UI creation with:

```java
public Tab createXxxTab() {
    Tab tab = new Tab("Tab Name");
    
    try {
        FXMLLoader loader = new FXMLLoader(getResource("FileName.fxml"));
        loader.setController(this);
        VBox root = loader.load();
        
        // Initialize components
        initializeComboBoxes();
        initializeTableColumns();
        initializeEventHandlers();
        
        tab.setContent(root);
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    return tab;
}
```

### Step 4: Organize Event Handlers
Group all event handler initialization in `initializeEventHandlers()` method.

### Step 5: Update Resource Loading
Add helper method:

```java
private URL getResource(String resourceName) {
    return getClass().getResource("/fxml/" + resourceName);
}
```

## Key Benefits
1. **Separation of Concerns** - UI and Logic are separated
2. **Reusability** - FXML files can be used by Scene Builder
3. **Maintainability** - Easier to modify UI without touching code
4. **Testability** - Business logic can be tested independently

## File Structure
```
src/
├── main/
│   ├── java/
│   │   └── controller/
│   │       ├── DashboardController.java (refactored)
│   │       ├── PatientTabController.java (refactored)
│   │       └── ... other controllers
│   └── resources/
│       └── fxml/
│           ├── Dashboard.fxml
│           ├── Patient.fxml
│           └── ... other FXML files
```

## Notes
- Always use `@FXML` annotation for FXMLLoader to inject components
- Use `FXMLLoader.setController(this)` to set the controller
- Call `loader.load()` which returns the root node
- Keep event handler initialization separate for clarity
- Test after each refactoring to ensure functionality is preserved
