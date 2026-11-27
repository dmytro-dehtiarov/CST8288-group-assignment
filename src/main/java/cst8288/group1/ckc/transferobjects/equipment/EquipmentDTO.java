/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cst8288.group1.ckc.transferobjects.equipment;

import cst8288.group1.ckc.transferobjects.enums.EquipmentCategory;
import cst8288.group1.ckc.transferobjects.enums.EquipmentStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transfer object carrying data for a piece of equipment.
 *
 * @author Andrew Caelan Roberge-Toll
 */
public class EquipmentDTO implements Serializable {
    
    private int equipmentId;
    private String assetTag;
    private String make;
    private String model;
    private EquipmentCategory category;
    private BigDecimal creditRatePerHour;
    private EquipmentStatus status;
    private int maintenanceThresholdHours;
    private LocalDateTime lastMaintenanceAt;
    private LocalDateTime createdAt;

    /**
     * Creates an empty equipment transfer object; fill it with the setters.
     */
    public EquipmentDTO() {
    }

    /**
     * Returns the equipment id.
     *
     * @return the equipment id
     */
    public int getEquipmentId() {
        return equipmentId;
    }

    /**
     * Sets the equipment id.
     *
     * @param equipmentId the equipment id
     */
    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    /**
     * Returns the asset tag.
     *
     * @return the asset tag
     */
    public String getAssetTag() {
        return assetTag;
    }

    /**
     * Sets the asset tag.
     *
     * @param assetTag the asset tag
     */
    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    /**
     * Returns the make.
     *
     * @return the make
     */
    public String getMake() {
        return make;
    }

    /**
     * Sets the make.
     *
     * @param make the make
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Returns the model.
     *
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model.
     *
     * @param model the model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Returns the equipment category.
     *
     * @return the equipment category
     */
    public EquipmentCategory getCategory() {
        return category;
    }

    /**
     * Sets the equipment category.
     *
     * @param category the equipment category
     */
    public void setCategory(EquipmentCategory category) {
        this.category = category;
    }

    /**
     * Returns the credit rate per hour.
     *
     * @return the credit rate per hour
     */
    public BigDecimal getCreditRatePerHour() {
        return creditRatePerHour;
    }

    /**
     * Sets the credit rate per hour.
     *
     * @param creditRatePerHour the credit rate per hour
     */
    public void setCreditRatePerHour(BigDecimal creditRatePerHour) {
        this.creditRatePerHour = creditRatePerHour;
    }

    /**
     * Returns the equipment status.
     *
     * @return the equipment status
     */
    public EquipmentStatus getStatus() {
        return status;
    }

    /**
     * Sets the equipment status.
     *
     * @param status the equipment status
     */
    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    /**
     * Returns the maintenance threshold in hours.
     *
     * @return the maintenance threshold in hours
     */
    public int getMaintenanceThresholdHours() {
        return maintenanceThresholdHours;
    }

    /**
     * Sets the maintenance threshold in hours.
     *
     * @param maintenanceThresholdHours the maintenance threshold in hours
     */
    public void setMaintenanceThresholdHours(int maintenanceThresholdHours) {
        this.maintenanceThresholdHours = maintenanceThresholdHours;
    }

    /**
     * Returns the date and time of the last maintenance.
     *
     * @return the date and time of the last maintenance
     */
    public LocalDateTime getLastMaintenanceAt() {
        return lastMaintenanceAt;
    }

    /**
     * Sets the date and time of the last maintenance.
     *
     * @param lastMaintenanceAt the date and time of the last maintenance
     */
    public void setLastMaintenanceAt(LocalDateTime lastMaintenanceAt) {
        this.lastMaintenanceAt = lastMaintenanceAt;
    }

    /**
     * Returns the date and time the equipment was created.
     *
     * @return the date and time the equipment was created
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the date and time the equipment was created.
     *
     * @param createdAt the date and time the equipment was created
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    
    
}
