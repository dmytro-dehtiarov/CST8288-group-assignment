/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cst8288.group1.ckc.dataaccess.equipment;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.equipment.EquipmentDTO;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access implementation for equipment records.
 *
 * @author Andrew Caelan Roberge-Toll
 */
public class EquipmentDAOImpl implements EquipmentDAO {

    /**
     * Creates an equipment record.
     *
     * @param equipment the equipment to create
     * @throws DaoException if the record cannot be created
     */
    @Override
    public void createEquipment(EquipmentDTO equipment) throws DaoException {
        // TODO: Implement database insertion.
    }

    /**
     * Returns the equipment records.
     *
     * @return the list of equipment
     * @throws DaoException if the equipment records cannot be retrieved
     */
    @Override
    public List<EquipmentDTO> getAllEquipment() throws DaoException {
        // TODO: Retrieve equipment from the database.
        return new ArrayList<>();
    }

    /**
     * Returns an equipment record by its id.
     *
     * @param equipmentId the equipment id
     * @return the matching equipment
     * @throws DaoException if the equipment record cannot be retrieved
     */
    @Override
    public EquipmentDTO getEquipmentById(int equipmentId) throws DaoException {
        // TODO: Retrieve the equipment from the database.
        return null;
    }

    /**
     * Updates the status of an equipment record.
     *
     * @param equipment the equipment containing the id and new status
     * @throws DaoException if the status cannot be updated
     */
    @Override
    public void updateStatus(EquipmentDTO equipment) throws DaoException {
        // TODO: Update the status in the database.
    }

    /**
     * Marks an equipment record as serviced.
     *
     * @param equipment the equipment to mark as serviced
     * @throws DaoException if the equipment cannot be marked as serviced
     */
    @Override
    public void markServiced(EquipmentDTO equipment) throws DaoException {
        // TODO: Record the service in the database.
    }
}
