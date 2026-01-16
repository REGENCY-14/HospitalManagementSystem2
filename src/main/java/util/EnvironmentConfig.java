package util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Environment Configuration Utility
 * Loads configuration from .env file and environment variables
 */
public class EnvironmentConfig {
    private static final Properties properties = new Properties();
    private static final String ENV_FILE = ".env";
    
    static {
        loadEnvironmentVariables();
    }
    
    /**
     * Load environment variables from .env file
     */
    private static void loadEnvironmentVariables() {
        try {
            // Try to load from .env file in project root
            if (Files.exists(Paths.get(ENV_FILE))) {
                try (InputStream input = Files.newInputStream(Paths.get(ENV_FILE))) {
                    properties.load(input);
                    System.out.println("✓ Environment configuration loaded from .env file");
                }
            } else {
                System.out.println("⚠ .env file not found, using system environment variables");
            }
        } catch (IOException e) {
            System.err.println("⚠ Failed to load .env file: " + e.getMessage());
        }
    }
    
    /**
     * Get configuration value from .env file or environment variable
     * @param key The configuration key
     * @param defaultValue Default value if key not found
     * @return Configuration value
     */
    public static String getConfig(String key, String defaultValue) {
        // First check properties loaded from .env file
        String value = properties.getProperty(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        
        // Then check system environment variables
        value = System.getenv(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        
        // Return default value
        return defaultValue;
    }
    
    /**
     * Get configuration value from .env file or environment variable
     * @param key The configuration key
     * @return Configuration value (throws exception if not found)
     */
    public static String getConfig(String key) {
        String value = properties.getProperty(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        
        value = System.getenv(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        
        throw new RuntimeException("Configuration key not found: " + key);
    }
}
