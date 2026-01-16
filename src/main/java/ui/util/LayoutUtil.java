package ui.util;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Control;

/**
 * Utility class for layout and spacing management
 */
public class LayoutUtil {
    
    // Standard spacing values
    public static final double STANDARD_SPACING = 15.0;
    public static final double SMALL_SPACING = 10.0;
    public static final double LARGE_SPACING = 20.0;
    
    // Standard padding values
    public static final Insets STANDARD_PADDING = new Insets(20);
    public static final Insets SMALL_PADDING = new Insets(10);
    public static final Insets LARGE_PADDING = new Insets(30);
    
    /**
     * Create a VBox with standard spacing and padding
     * @return Configured VBox
     */
    public static VBox createStandardVBox() {
        VBox vbox = new VBox(STANDARD_SPACING);
        vbox.setPadding(STANDARD_PADDING);
        return vbox;
    }
    
    /**
     * Create an HBox with standard spacing
     * @return Configured HBox
     */
    public static HBox createStandardHBox() {
        HBox hbox = new HBox(SMALL_SPACING);
        return hbox;
    }
    
    /**
     * Create a VBox with custom spacing
     * @param spacing Spacing between elements
     * @return Configured VBox
     */
    public static VBox createVBox(double spacing) {
        VBox vbox = new VBox(spacing);
        vbox.setPadding(STANDARD_PADDING);
        return vbox;
    }
    
    /**
     * Create an HBox with custom spacing
     * @param spacing Spacing between elements
     * @return Configured HBox
     */
    public static HBox createHBox(double spacing) {
        return new HBox(spacing);
    }
    
    /**
     * Set fixed width for control
     * @param control Control to resize
     * @param width Width value
     */
    public static void setFixedWidth(Control control, double width) {
        control.setPrefWidth(width);
        control.setMinWidth(width);
    }
    
    /**
     * Set fixed height for control
     * @param control Control to resize
     * @param height Height value
     */
    public static void setFixedHeight(Control control, double height) {
        control.setPrefHeight(height);
        control.setMinHeight(height);
    }
}
