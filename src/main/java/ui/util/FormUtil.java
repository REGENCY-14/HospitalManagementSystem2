package ui.util;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Utility class for form operations and field management
 */
public class FormUtil {
    
    /**
     * Clear all text fields
     * @param fields Text fields to clear
     */
    public static void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }
    
    /**
     * Clear all combo boxes
     * @param combos Combo boxes to clear
     */
    public static void clearComboBoxes(ComboBox<?>... combos) {
        for (ComboBox<?> combo : combos) {
            combo.getSelectionModel().clearSelection();
        }
    }
    
    /**
     * Check if any field is empty
     * @param fields Text fields to check
     * @return true if any field is empty
     */
    public static boolean hasEmptyFields(TextField... fields) {
        for (TextField field : fields) {
            if (field.getText() == null || field.getText().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get trimmed text from field
     * @param field Text field
     * @return Trimmed text value
     */
    public static String getTrimmedText(TextField field) {
        return field.getText() != null ? field.getText().trim() : "";
    }
    
    /**
     * Request focus on first empty field
     * @param fields Text fields to check
     */
    public static void focusFirstEmpty(TextField... fields) {
        for (TextField field : fields) {
            if (field.getText() == null || field.getText().trim().isEmpty()) {
                field.requestFocus();
                break;
            }
        }
    }
}
