package model;

public class PrescriptionItem {
    private int prescriptionItemId;
    private int prescriptionId;
    private int inventoryId;
    private String dosage;
    private String frequency;
    private String duration;
    private int quantity;

    // Constructor
    public PrescriptionItem(int prescriptionItemId, int prescriptionId, int inventoryId,
                           String dosage, String frequency, String duration, int quantity) {
        this.prescriptionItemId = prescriptionItemId;
        this.prescriptionId = prescriptionId;
        this.inventoryId = inventoryId;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.quantity = quantity;
    }

    public PrescriptionItem(int prescriptionId, int inventoryId,
                           String dosage, String frequency, String duration, int quantity) {
        this.prescriptionId = prescriptionId;
        this.inventoryId = inventoryId;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getPrescriptionItemId() {
        return prescriptionItemId;
    }

    public void setPrescriptionItemId(int prescriptionItemId) {
        this.prescriptionItemId = prescriptionItemId;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "PrescriptionItem{" +
                "prescriptionItemId=" + prescriptionItemId +
                ", prescriptionId=" + prescriptionId +
                ", inventoryId=" + inventoryId +
                ", dosage='" + dosage + '\'' +
                ", frequency='" + frequency + '\'' +
                ", duration='" + duration + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
