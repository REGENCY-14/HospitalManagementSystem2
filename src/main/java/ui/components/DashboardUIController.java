package ui.components;

import javafx.scene.control.Alert;
import service.PatientService;
import service.DoctorService;
import util.SimpleCache;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Business logic controller for Dashboard UI
 * Works with DashboardUI to handle operations
 */
public class DashboardUIController {
    
    private DashboardUI ui;
    
    public DashboardUIController(DashboardUI ui) {
        this.ui = ui;
    }
    
    public void initializeEventHandlers() {
        ui.refreshBtn.setOnAction(e -> handleRefreshStats());
        ui.clearCacheBtn.setOnAction(e -> handleClearCache());
    }
    
    private void handleRefreshStats() {
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

        ui.patientCacheLabel.setText("Patient Cache: " + patientInfo);
        ui.doctorCacheLabel.setText("Doctor Cache: " + doctorInfo);
        ui.refreshTimeLabel.setText("Last Updated: " + LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        ));
    }
    
    private void handleClearCache() {
        PatientService.clearCache();
        DoctorService.clearCache();
        ui.patientCacheLabel.setText("Patient Cache: ---");
        ui.doctorCacheLabel.setText("Doctor Cache: ---");
        ui.refreshTimeLabel.setText("Cache cleared!");
    }
}
