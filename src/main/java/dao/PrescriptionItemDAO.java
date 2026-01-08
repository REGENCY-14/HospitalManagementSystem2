package dao;

import model.PrescriptionItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionItemDAO {
    
    /**
     * Adds a new prescription item
     */
    public static boolean addPrescriptionItem(PrescriptionItem item) {
        String query = "INSERT INTO PrescriptionItem (prescription_id, inventory_id, dosage, frequency, duration, quantity) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, item.getPrescriptionId());
            stmt.setInt(2, item.getInventoryId());
            stmt.setString(3, item.getDosage());
            stmt.setString(4, item.getFrequency());
            stmt.setString(5, item.getDuration());
            stmt.setInt(6, item.getQuantity());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding prescription item: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves a prescription item by ID
     */
    public static PrescriptionItem getPrescriptionItemById(int prescriptionItemId) {
        String query = "SELECT * FROM PrescriptionItem WHERE prescription_item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, prescriptionItemId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new PrescriptionItem(
                    rs.getInt("prescription_item_id"),
                    rs.getInt("prescription_id"),
                    rs.getInt("inventory_id"),
                    rs.getString("dosage"),
                    rs.getString("frequency"),
                    rs.getString("duration"),
                    rs.getInt("quantity")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving prescription item: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Retrieves all prescription items
     */
    public static List<PrescriptionItem> getAllPrescriptionItems() {
        List<PrescriptionItem> items = new ArrayList<>();
        String query = "SELECT * FROM PrescriptionItem";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                items.add(new PrescriptionItem(
                    rs.getInt("prescription_item_id"),
                    rs.getInt("prescription_id"),
                    rs.getInt("inventory_id"),
                    rs.getString("dosage"),
                    rs.getString("frequency"),
                    rs.getString("duration"),
                    rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all prescription items: " + e.getMessage());
        }
        return items;
    }
    
    /**
     * Retrieves items for a specific prescription
     */
    public static List<PrescriptionItem> getItemsByPrescription(int prescriptionId) {
        List<PrescriptionItem> items = new ArrayList<>();
        String query = "SELECT * FROM PrescriptionItem WHERE prescription_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, prescriptionId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                items.add(new PrescriptionItem(
                    rs.getInt("prescription_item_id"),
                    rs.getInt("prescription_id"),
                    rs.getInt("inventory_id"),
                    rs.getString("dosage"),
                    rs.getString("frequency"),
                    rs.getString("duration"),
                    rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving items by prescription: " + e.getMessage());
        }
        return items;
    }
    
    /**
     * Updates a prescription item
     */
    public static boolean updatePrescriptionItem(PrescriptionItem item) {
        String query = "UPDATE PrescriptionItem SET prescription_id = ?, inventory_id = ?, dosage = ?, frequency = ?, duration = ?, quantity = ? WHERE prescription_item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, item.getPrescriptionId());
            stmt.setInt(2, item.getInventoryId());
            stmt.setString(3, item.getDosage());
            stmt.setString(4, item.getFrequency());
            stmt.setString(5, item.getDuration());
            stmt.setInt(6, item.getQuantity());
            stmt.setInt(7, item.getPrescriptionItemId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating prescription item: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes a prescription item
     */
    public static boolean deletePrescriptionItem(int prescriptionItemId) {
        String query = "DELETE FROM PrescriptionItem WHERE prescription_item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, prescriptionItemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting prescription item: " + e.getMessage());
            return false;
        }
    }
}
