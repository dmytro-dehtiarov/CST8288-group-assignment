/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cst8288.group1.ckc.transferobjects.equipment;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author caela
 */
public class EquipmentStatusLineDTO implements Serializable {
    EquipmentDTO equipment;
    String currentUserName;
    long elapsedMinutes;
    
    // needs to be changed to consumable once implemented
    List<String> consumablesUsed;

    /**
     * Creates an empty equipment status line; fill it with the setters.
     */
    public EquipmentStatusLineDTO() {
    }

    /**
     * Returns the equipment.
     *
     * @return the equipment
     */
    public EquipmentDTO getEquipment() {
        return equipment;
    }

    /**
     * Sets the equipment.
     *
     * @param equipment the equipment
     */
    public void setEquipment(EquipmentDTO equipment) {
        this.equipment = equipment;
    }

    /**
     * Returns the current user name.
     *
     * @return the current user name
     */
    public String getCurrentUserName() {
        return currentUserName;
    }

    /**
     * Sets the current user name.
     *
     * @param currentUserName the current user name
     */
    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    /**
     * Returns the elapsed time in minutes.
     *
     * @return the elapsed time in minutes
     */
    public long getElapsedMinutes() {
        return elapsedMinutes;
    }

    /**
     * Sets the elapsed time in minutes.
     *
     * @param elapsedMinutes the elapsed time in minutes
     */
    public void setElapsedMinutes(long elapsedMinutes) {
        this.elapsedMinutes = elapsedMinutes;
    }

    /**
     * THIS NEEDS TO BE CHANGED TO A LIST OF CONSUMABLES ONCE IMPLEMENTED
     * @return 
     */
    public List<String> getConsumablesUsed() {
        return consumablesUsed;
    }

    /**
     * THIS NEEDS TO BE CHANGED TO ACCEPT A CONSUMABLE LIST ONCE IMPLEMENTED
     * @param consumablesUsed 
     */
    public void setConsumablesUsed(List<String> consumablesUsed) {
        this.consumablesUsed = consumablesUsed;
    }
    

}
