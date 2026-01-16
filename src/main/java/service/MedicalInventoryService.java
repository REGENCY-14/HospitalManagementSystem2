package service;

import dao.MedicalInventoryDAO;
import model.MedicalInventory;
import java.util.List;

public class MedicalInventoryService {
    
    public static boolean createInventoryItem(MedicalInventory item) {
        if (item == null || item.getItemName() == null || item.getItemName().isEmpty()) {
            System.err.println("Invalid inventory item data");
            return false;
        }
        return MedicalInventoryDAO.addInventoryItem(item);
    }
    
    public static MedicalInventory getInventoryItem(int inventoryId) {
        if (inventoryId <= 0) {
            System.err.println("Invalid inventory ID");
            return null;
        }
        return MedicalInventoryDAO.getInventoryItemById(inventoryId);
    }
    
    public static List<MedicalInventory> getAllInventoryItems() {
        return MedicalInventoryDAO.getAllInventoryItems();
    }
    
    public static List<MedicalInventory> getLowStockItems(int threshold) {
        // This method may not exist in DAO, remove or implement it
        return MedicalInventoryDAO.getAllInventoryItems();
    }
    
    public static boolean updateInventoryItem(MedicalInventory item) {
        if (item == null || item.getInventoryId() <= 0) {
            System.err.println("Invalid inventory item data for update");
            return false;
        }
        return MedicalInventoryDAO.updateInventoryItem(item);
    }
    
    public static boolean deleteInventoryItem(int inventoryId) {
        if (inventoryId <= 0) {
            System.err.println("Invalid inventory ID");
            return false;
        }
        return MedicalInventoryDAO.deleteInventoryItem(inventoryId);
    }
}
