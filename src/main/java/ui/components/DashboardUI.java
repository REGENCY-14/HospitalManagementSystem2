package ui.components;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import service.PatientService;
import service.DoctorService;
import util.SimpleCache;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * UI Component for Dashboard
 * Handles FXML loading and component initialization
 */
public class DashboardUI extends FXMLTabController {

    @FXML
    public Label titleLabel;
    
    @FXML
    public VBox cacheBox;
    
    @FXML
    public Label patientCacheLabel;
    
    @FXML
    public Label doctorCacheLabel;
    
    @FXML
    public Label refreshTimeLabel;
    
    @FXML
    public Button refreshBtn;
    
    @FXML
    public Button clearCacheBtn;
    
    private DashboardUIController uiController;
    
    public DashboardUI() {
        this.uiController = new DashboardUIController(this);
    }

    @Override
    protected String getFXMLFile() {
        return "Dashboard.fxml";
    }

    @Override
    protected void initializeUI() {
        uiController.initializeEventHandlers();
    }

    public Tab createDashboardTab() {
        Tab tab = createTabFromFXML("Dashboard");
        tab.setClosable(false);
        return tab;
    }
}
