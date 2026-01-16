package ui.components;

import javafx.scene.control.Tab;
import ui.util.AlertUtil;
import ui.util.FXMLUtil;

/**
 * Base class for all tab controllers
 * Provides common functionality and utilities
 */
public abstract class BaseTabController {
    
    /**
     * Create and return a tab
     * @return Configured Tab
     */
    public abstract Tab createTab();
    
    /**
     * Show an information alert
     * @param title Alert title
     * @param message Alert message
     */
    protected void showAlert(String title, String message) {
        AlertUtil.showInfo(title, message);
    }
    
    /**
     * Show an error alert
     * @param title Alert title
     * @param message Alert message
     */
    protected void showError(String title, String message) {
        AlertUtil.showError(title, message);
    }
    
    /**
     * Show a warning alert
     * @param title Alert title
     * @param message Alert message
     */
    protected void showWarning(String title, String message) {
        AlertUtil.showWarning(title, message);
    }
    
    /**
     * Show a confirmation dialog
     * @param title Dialog title
     * @param message Dialog message
     * @return true if user clicked OK
     */
    protected boolean showConfirmation(String title, String message) {
        return AlertUtil.showConfirmation(title, message);
    }
}
