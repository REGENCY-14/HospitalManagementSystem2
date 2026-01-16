package controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import service.PatientService;
import service.DoctorService;
import ui.components.FXMLTabController;
import util.SimpleCache;

/**
 * Dashboard controller to display cache statistics and system metrics.
 * UI layout is defined in Dashboard.fxml
 */
public class DashboardController extends FXMLTabController {

    @FXML
    private Label titleLabel;
    
    @FXML
    private VBox cacheBox;
    
    @FXML
    private Label patientCacheLabel;
    
    @FXML
    private Label doctorCacheLabel;
    
    @FXML
    private Label refreshTimeLabel;
    
    @FXML
    private Button refreshBtn;
    
    @FXML
    private Button clearCacheBtn;

    @Override
    protected String getFXMLFile() {
        return "Dashboard.fxml";
    }

    @Override
    protected void initializeUI() {
        initializeEventHandlers();
    }

    public Tab createDashboardTab() {
        Tab tab = createTabFromFXML("Dashboard");
        tab.setClosable(false);
        return tab;
    }
    
    private void initializeEventHandlers() {
        // Update stats function
        Runnable updateStats = () -> {
            SimpleCache.Stats patientStats = PatientService.getCacheStats();
            SimpleCache.Stats doctorStats = DoctorService.getCacheStats();

            String patientInfo = String.format(
                "Hits: %d | Misses: %d | Writes: %d | Size: %d | Hit Rate: %.2f%%",
                patientStats.hits, patientStats.misses, patientStats.puts,
                PatientService.getCacheSize(),
                patientStats.hitRate * 100
            );

            String doctorInfo = String.format(
                "Hits: %d | Misses: %d | Writes: %d | Size: %d | Hit Rate: %.2f%%",
                doctorStats.hits, doctorStats.misses, doctorStats.puts,
                DoctorService.getCacheSize(),
                doctorStats.hitRate * 100
            );

            patientCacheLabel.setText("Patient Cache: " + patientInfo);
            doctorCacheLabel.setText("Doctor Cache: " + doctorInfo);
            refreshTimeLabel.setText("Last Updated: " + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            ));
        };

        refreshBtn.setOnAction(e -> updateStats.run());

        clearCacheBtn.setOnAction(e -> {
            PatientService.clearCache();
            DoctorService.clearCache();
            patientCacheLabel.setText("Patient Cache: ---");
            doctorCacheLabel.setText("Doctor Cache: ---");
            refreshTimeLabel.setText("Cache cleared!");
        });
    }
}

