package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MedicalInventory {
    private int inventoryId;
    private String itemName;
    private String category;
    private int quantity;
    private double unitPrice;
    private LocalDate expiryDate;
    private String supplier;
    private LocalDateTime lastUpdated;

    // Constructor
    public MedicalInventory(int inventoryId, String itemName, String category, int quantity,
                           double unitPrice, LocalDate expiryDate, String supplier) {
        this.inventoryId = inventoryId;
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.expiryDate = expiryDate;
        this.supplier = supplier;
    }

    public MedicalInventory(String itemName, String category, int quantity,
                           double unitPrice, LocalDate expiryDate, String supplier) {
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.expiryDate = expiryDate;
        this.supplier = supplier;
    }

    // Getters and Setters
    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "MedicalInventory{" +
                "inventoryId=" + inventoryId +
                ", itemName='" + itemName + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", expiryDate=" + expiryDate +
                ", supplier='" + supplier + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
