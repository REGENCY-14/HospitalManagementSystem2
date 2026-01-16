package ui.util;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Utility class for UI styling and styling operations
 */
public class StyleUtil {
    
    // Button Styles
    public static final String BUTTON_STYLE = "-fx-font-size: 12; -fx-padding: 8;";
    public static final String TITLE_STYLE = "-fx-font-size: 18; -fx-font-weight: bold;";
    public static final String SUBTITLE_STYLE = "-fx-font-size: 14; -fx-font-weight: bold;";
    
    /**
     * Apply standard button style
     * @param button Button to style
     */
    public static void styleButton(Button button) {
        button.setStyle(BUTTON_STYLE);
    }
    
    /**
     * Apply title style to label
     * @param label Label to style
     */
    public static void styleTitleLabel(Label label) {
        label.setStyle(TITLE_STYLE);
    }
    
    /**
     * Apply subtitle style to label
     * @param label Label to style
     */
    public static void styleSubtitleLabel(Label label) {
        label.setStyle(SUBTITLE_STYLE);
    }
    
    /**
     * Apply standard padding to table view
     * @param tableView Table to style
     */
    public static void styleTable(TableView<?> tableView) {
        tableView.setPrefHeight(400);
    }
    
    /**
     * Set column width and cell value factory for a table column
     * @param column Table column
     * @param propertyName Property name from model
     * @param prefWidth Preferred width
     */
    public static <S, T> void configureTableColumn(TableColumn<S, T> column, String propertyName, double prefWidth) {
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        column.setPrefWidth(prefWidth);
    }
}
