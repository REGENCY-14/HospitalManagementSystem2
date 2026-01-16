package ui.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;

/**
 * Utility class for loading FXML files
 */
public class FXMLUtil {
    
    /**
     * Load an FXML file from the fxml resources folder
     * @param resourceName Name of the FXML file (without path)
     * @param controller Controller to set for the FXML
     * @return Loaded VBox root node
     * @throws IOException If FXML file cannot be loaded
     */
    public static VBox loadFXML(String resourceName, Object controller) throws IOException {
        URL resource = getFXMLResource(resourceName);
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setController(controller);
        return loader.load();
    }
    
    /**
     * Get FXML resource URL
     * @param resourceName Name of the FXML file (without path)
     * @return URL of the FXML resource
     */
    public static URL getFXMLResource(String resourceName) {
        return FXMLUtil.class.getResource("/fxml/" + resourceName);
    }
    
    /**
     * Load an FXML file without setting a controller
     * @param resourceName Name of the FXML file (without path)
     * @return Loaded VBox root node
     * @throws IOException If FXML file cannot be loaded
     */
    public static VBox loadFXML(String resourceName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getFXMLResource(resourceName));
        return loader.load();
    }
}
