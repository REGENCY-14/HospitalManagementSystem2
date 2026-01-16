package service;

import dao.PrescriptionItemDAO;
import model.PrescriptionItem;
import java.util.List;

public class PrescriptionItemService {
    
    public static boolean createPrescriptionItem(PrescriptionItem item) {
        if (item == null) {
            System.err.println("Invalid prescription item data");
            return false;
        }
        return PrescriptionItemDAO.addPrescriptionItem(item);
    }
    
    public static PrescriptionItem getPrescriptionItem(int itemId) {
        if (itemId <= 0) {
            System.err.println("Invalid prescription item ID");
            return null;
        }
        return PrescriptionItemDAO.getPrescriptionItemById(itemId);
    }
    
    public static List<PrescriptionItem> getItemsByPrescription(int prescriptionId) {
        if (prescriptionId <= 0) {
            System.err.println("Invalid prescription ID");
            return null;
        }
        return PrescriptionItemDAO.getItemsByPrescription(prescriptionId);
    }
    
    public static List<PrescriptionItem> getAllPrescriptionItems() {
        return PrescriptionItemDAO.getAllPrescriptionItems();
    }
    
    public static boolean updatePrescriptionItem(PrescriptionItem item) {
        if (item == null || item.getPrescriptionItemId() <= 0) {
            System.err.println("Invalid prescription item data for update");
            return false;
        }
        return PrescriptionItemDAO.updatePrescriptionItem(item);
    }
    
    public static boolean deletePrescriptionItem(int itemId) {
        if (itemId <= 0) {
            System.err.println("Invalid prescription item ID");
            return false;
        }
        return PrescriptionItemDAO.deletePrescriptionItem(itemId);
    }
}
