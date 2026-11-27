/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cst8288.group1.ckc.dataaccess.equipment;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.equipment.EquipmentDTO;
import java.util.List;

/**
 * Defines data access operations for equipment.
 *
 * @author Andrew Caelan Roberge-Toll
 */
public interface EquipmentDAO {
    
    /**
     * Creates an equipment record.
     *
     * @param equipment the equipment to create
     * @throws DaoException if the record cannot be created
     */
    public void createEquipment(EquipmentDTO equipment) throws DaoException;
    
    // getall
    /**
     * Returns the equipment records.
     *
     * @return the list of equipment
     * @throws DaoException if the equipment records cannot be retrieved
     */
    public List<EquipmentDTO> getAllEquipment() throws DaoException;
    
    // getsinglebyid
    /**
     * Returns an equipment record by its id.
     *
     * @param equipmentId the equipment id
     * @return the matching equipment
     * @throws DaoException if the equipment record cannot be retrieved
     */
    public EquipmentDTO getEquipmentById(int equipmentId) throws DaoException;
    
    // update status
    /**
     * Updates the status of an equipment record.
     *
     * @param equipment the equipment containing the id and new status
     * @throws DaoException if the status cannot be updated
     */
    public void updateStatus(EquipmentDTO equipment) throws DaoException;
    
    /**
     * Marks an equipment record as serviced.
     *
     * @param equipment the equipment to mark as serviced
     * @throws DaoException if the equipment cannot be marked as serviced
     */
    public void markServiced(EquipmentDTO equipment) throws DaoException;
    
    
}
