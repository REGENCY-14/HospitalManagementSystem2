package controller;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import service.PatientService;
import service.DoctorService;
import util.SimpleCache;

/**
 * Dashboard controller to display cache statistics and system metrics.
 */
public class DashboardController {

    public Tab createDashboardTab() {
        Tab tab = new Tab("Dashboard");
        tab.setClosable(false);

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12;");

        // Title
        Label titleLabel = new Label("Cache & Performance Dashboard");
        titleLabel.setFont(new Font(20));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // Cache Stats Section
        VBox cacheBox = new VBox(15);
        cacheBox.setPadding(new Insets(15));
        cacheBox.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5;");

        Label cacheTitle = new Label("Cache Statistics");
        cacheTitle.setFont(new Font(16));
        cacheTitle.setStyle("-fx-font-weight: bold;");

        Label patientCacheLabel = new Label("Patient Cache: ---");
        Label doctorCacheLabel = new Label("Doctor Cache: ---");
        Label refreshTimeLabel = new Label("Last Updated: ---");

        // Function to update stats
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
            refreshTimeLabel.setText("Last Updated: " + java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            ));
        };

        Button refreshBtn = new Button("Refresh Stats");
        refreshBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshBtn.setOnAction(e -> updateStats.run());

        Button clearCacheBtn = new Button("Clear All Cache");
        clearCacheBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        clearCacheBtn.setOnAction(e -> {
            PatientService.clearCache();
            DoctorService.clearCache();
            patientCacheLabel.setText("Patient Cache: ---");
            doctorCacheLabel.setText("Doctor Cache: ---");
            refreshTimeLabel.setText("Cache cleared!");
        });

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(refreshBtn, clearCacheBtn);

        cacheBox.getChildren().addAll(
            cacheTitle,
            patientCacheLabel,
            doctorCacheLabel,
            refreshTimeLabel,
            buttonBox
        );

        root.getChildren().addAll(
            titleLabel,
            cacheBox
        );

        tab.setContent(root);
        return tab;
    }
}
