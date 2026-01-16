package ui.util;

import javafx.scene.control.Alert;

/**
 * Utility class for displaying alerts and dialogs
 */
public class AlertUtil {
    
    /**
     * Show an information alert
     * @param title Alert title
     * @param message Alert message
     */
    public static void showInfo(String title, String message) {
        showAlert(title, message, Alert.AlertType.INFORMATION);
    }
    
    /**
     * Show an error alert
     * @param title Alert title
     * @param message Alert message
     */
    public static void showError(String title, String message) {
        showAlert(title, message, Alert.AlertType.ERROR);
    }
    
    /**
     * Show a warning alert
     * @param title Alert title
     * @param message Alert message
     */
    public static void showWarning(String title, String message) {
        showAlert(title, message, Alert.AlertType.WARNING);
    }
    
    /**
     * Show a confirmation alert
     * @param title Alert title
     * @param message Alert message
     * @return true if user clicked OK, false otherwise
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        return alert.showAndWait().orElse(null) == javafx.scene.control.ButtonType.OK;
    }
    
    /**
     * Show an alert dialog
     * @param title Alert title
     * @param message Alert message
     * @param type Alert type
     */
    private static void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
