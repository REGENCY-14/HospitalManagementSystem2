package ui.components;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import ui.util.FXMLUtil;

import java.io.IOException;

/**
 * Base class for controllers that use FXML files
 * Handles common FXML loading logic
 */
public abstract class FXMLTabController extends BaseTabController {
    
    /**
     * Get the FXML filename (without path) for this controller
     * @return FXML filename
     */
    protected abstract String getFXMLFile();
    
    /**
     * Initialize UI components after FXML is loaded
     */
    protected void initializeUI() {
        // Override in subclasses if needed
    }
    
    /**
     * Create tab from FXML file
     * @param tabName Tab display name
     * @return Configured Tab
     */
    protected Tab createTabFromFXML(String tabName) {
        Tab tab = new Tab(tabName);
        tab.setClosable(false);
        
        try {
            VBox root = FXMLUtil.loadFXML(getFXMLFile(), this);
            initializeUI();
            tab.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load " + getFXMLFile() + ": " + e.getMessage());
        }
        
        return tab;
    }
    
    @Override
    public Tab createTab() {
        return createTabFromFXML(getClass().getSimpleName());
    }
}
