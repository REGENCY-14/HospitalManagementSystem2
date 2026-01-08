package dao;

import model.MedicalInventory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalInventoryDAO {
    
    /**
     * Adds a new inventory item
     */
    public static boolean addInventoryItem(MedicalInventory item) {
        String query = "INSERT INTO MedicalInventory (item_name, category, quantity, unit_price, expiry_date, supplier) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, item.getItemName());
            stmt.setString(2, item.getCategory());
            stmt.setInt(3, item.getQuantity());
            stmt.setDouble(4, item.getUnitPrice());
            stmt.setDate(5, item.getExpiryDate() != null ? java.sql.Date.valueOf(item.getExpiryDate()) : null);
            stmt.setString(6, item.getSupplier());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding inventory item: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves an inventory item by ID
     */
    public static MedicalInventory getInventoryItemById(int inventoryId) {
        String query = "SELECT * FROM MedicalInventory WHERE inventory_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, inventoryId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                MedicalInventory item = new MedicalInventory(
                    rs.getInt("inventory_id"),
                    rs.getString("item_name"),
                    rs.getString("category"),
                    rs.getInt("quantity"),
                    rs.getDouble("unit_price"),
                    rs.getDate("expiry_date") != null ? rs.getDate("expiry_date").toLocalDate() : null,
                    rs.getString("supplier")
                );
                item.setLastUpdated(rs.getTimestamp("last_updated").toLocalDateTime());
                return item;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving inventory item: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Retrieves all inventory items
     */
    public static List<MedicalInventory> getAllInventoryItems() {
        List<MedicalInventory> items = new ArrayList<>();
        String query = "SELECT * FROM MedicalInventory";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                MedicalInventory item = new MedicalInventory(
                    rs.getInt("inventory_id"),
                    rs.getString("item_name"),
                    rs.getString("category"),
                    rs.getInt("quantity"),
                    rs.getDouble("unit_price"),
                    rs.getDate("expiry_date") != null ? rs.getDate("expiry_date").toLocalDate() : null,
                    rs.getString("supplier")
                );
                item.setLastUpdated(rs.getTimestamp("last_updated").toLocalDateTime());
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all inventory items: " + e.getMessage());
        }
        return items;
    }
    
    /**
     * Updates an inventory item
     */
    public static boolean updateInventoryItem(MedicalInventory item) {
        String query = "UPDATE MedicalInventory SET item_name = ?, category = ?, quantity = ?, unit_price = ?, expiry_date = ?, supplier = ? WHERE inventory_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, item.getItemName());
            stmt.setString(2, item.getCategory());
            stmt.setInt(3, item.getQuantity());
            stmt.setDouble(4, item.getUnitPrice());
            stmt.setDate(5, item.getExpiryDate() != null ? java.sql.Date.valueOf(item.getExpiryDate()) : null);
            stmt.setString(6, item.getSupplier());
            stmt.setInt(7, item.getInventoryId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating inventory item: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes an inventory item
     */
    public static boolean deleteInventoryItem(int inventoryId) {
        String query = "DELETE FROM MedicalInventory WHERE inventory_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, inventoryId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting inventory item: " + e.getMessage());
            return false;
        }
    }
}
