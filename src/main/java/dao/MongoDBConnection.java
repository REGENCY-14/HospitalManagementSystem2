package dao;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import util.EnvironmentConfig;

/**
 * MongoDB Connection Manager
 * Singleton pattern for managing MongoDB connections
 * Connection details are loaded from environment variables (.env file)
 */
public class MongoDBConnection {
    private static volatile MongoDBConnection instance;
    private static final Object lock = new Object();
    private MongoClient mongoClient;
    private MongoDatabase database;
    
    // Connection configuration - loaded from environment (.env file)
    private static final String CONNECTION_STRING = EnvironmentConfig.getConfig("MONGODB_CONNECTION_STRING");
    private static final String DATABASE_NAME = EnvironmentConfig.getConfig("MONGODB_DATABASE_NAME");
    
    /**
     * Private constructor - Singleton pattern
     */
    private MongoDBConnection() {
        try {
            initializeConnection();
        } catch (Exception e) {
            System.err.println("Failed to initialize MongoDB connection: " + e.getMessage());
        }
    }
    
    /**
     * Get singleton instance
     * @return MongoDBConnection instance
     */
    public static MongoDBConnection getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new MongoDBConnection();
                }
            }
        }
        return instance;
    }
    
    /**
     * Initialize MongoDB connection with POJO codec support
     */
    private void initializeConnection() {
        try {
            // Create codec registry for POJO support
            CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
            );
            
            // Create connection string
            ConnectionString connString = new ConnectionString(CONNECTION_STRING);
            
            // Build MongoDB client settings
            MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .codecRegistry(pojoCodecRegistry)
                .build();
            
            // Create MongoDB client
            this.mongoClient = MongoClients.create(settings);
            
            // Get database
            this.database = mongoClient.getDatabase(DATABASE_NAME);
            
            // Test connection
            this.database.runCommand(new org.bson.Document("ping", 1));
            System.out.println("✓ Successfully connected to MongoDB database: " + DATABASE_NAME);
            
        } catch (Exception e) {
            System.err.println("✗ MongoDB connection failed: " + e.getMessage());
            throw new RuntimeException("Failed to connect to MongoDB", e);
        }
    }
    
    /**
     * Get MongoDB database instance
     * @return MongoDatabase instance
     */
    public MongoDatabase getDatabase() {
        if (this.database == null) {
            throw new RuntimeException("Database connection not initialized");
        }
        return this.database;
    }
    
    /**
     * Get MongoDB client instance
     * @return MongoClient instance
     */
    public MongoClient getMongoClient() {
        if (this.mongoClient == null) {
            throw new RuntimeException("MongoDB client not initialized");
        }
        return this.mongoClient;
    }
    
    /**
     * Close MongoDB connection
     * Call this when application shuts down
     */
    public void closeConnection() {
        try {
            if (mongoClient != null) {
                mongoClient.close();
                System.out.println("✓ MongoDB connection closed");
            }
        } catch (Exception e) {
            System.err.println("✗ Error closing MongoDB connection: " + e.getMessage());
        }
    }
    
    /**
     * Test connection to MongoDB
     * @return true if connection is successful
     */
    public boolean testConnection() {
        try {
            this.database.runCommand(new org.bson.Document("ping", 1));
            return true;
        } catch (Exception e) {
            System.err.println("✗ MongoDB test connection failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get connection status information
     * @return Status string with connection details
     */
    public String getConnectionStatus() {
        StringBuilder status = new StringBuilder();
        status.append("MongoDB Connection Status:\n");
        status.append("  Connection String: ").append(CONNECTION_STRING).append("\n");
        status.append("  Database: ").append(DATABASE_NAME).append("\n");
        status.append("  Status: ");
        
        if (testConnection()) {
            status.append("✓ CONNECTED");
        } else {
            status.append("✗ DISCONNECTED");
        }
        
        return status.toString();
    }
}
