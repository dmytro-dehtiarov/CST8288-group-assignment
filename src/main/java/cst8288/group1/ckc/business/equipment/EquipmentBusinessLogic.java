/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cst8288.group1.ckc.business.equipment;

import cst8288.group1.ckc.dataaccess.equipment.EquipmentDAO;
import cst8288.group1.ckc.dataaccess.equipment.UsageSessionDAO;
import cst8288.group1.ckc.transferobjects.equipment.EquipmentDTO;
import cst8288.group1.ckc.transferobjects.equipment.EquipmentStatusLineDTO;
import cst8288.group1.ckc.transferobjects.equipment.UsageSessionDTO;
import java.util.ArrayList;
import java.util.List;

/**
 * Coordinates equipment registration, usage sessions, and status reporting.
 * The business operations are currently placeholders and do not call the DAOs.
 *
 * @author Andrew Caelan Roberge-Toll
 */
public class EquipmentBusinessLogic {
    
    private final EquipmentDAO equipmentDAO;
    private final UsageSessionDAO usageSessionDAO;
    
    /**
     * Creates business logic using the supplied equipment and session DAOs.
     *
     * @param equipmentDAO the equipment data access object
     * @param usageSessionDAO the usage session data access object
     */
    public EquipmentBusinessLogic(EquipmentDAO equipmentDAO, UsageSessionDAO usageSessionDAO) {
        this.equipmentDAO = equipmentDAO;
        this.usageSessionDAO = usageSessionDAO;
    }
    
    /**
     * Registers a prepared equipment record. The intended implementation will
     * call {@link EquipmentDAO#createEquipment(EquipmentDTO)}. This method
     * currently does nothing.
     *
     * @param equipment the equipment to register
     */
    public void registerEquipment(EquipmentDTO equipment) {
    }
    
    /**
     * Opens an equipment usage session for a user. The intended implementation
     * will create a session and call {@link UsageSessionDAO#insertSession(UsageSessionDTO)}.
     * It currently returns a temporary empty DTO without saving a session.
     *
     * @param userId the id of the user opening the session
     * @param equipmentId the id of the equipment being used
     * @return a temporary empty usage session DTO
     */
    public UsageSessionDTO openSession(int userId, int equipmentId) {
        return new UsageSessionDTO();
    }
    
    /**
     * Closes an existing usage session. The intended implementation will set
     * its checkout time and call {@link UsageSessionDAO#updateSession(UsageSessionDTO)}.
     * This method currently does nothing.
     *
     * @param session the usage session to close
     */
    public void closeSession(UsageSessionDTO session) {};
    
    /**
     * Builds a report of equipment status and current usage. It will use an EquipmentStatusLineDTO. It currently returns
     * a temporary empty list without querying either DAO.
     *
     * @return a temporary empty list of equipment status lines
     */
    public List<EquipmentStatusLineDTO> getStatusReport() {
        return new ArrayList<>();
    }
    
    
}
