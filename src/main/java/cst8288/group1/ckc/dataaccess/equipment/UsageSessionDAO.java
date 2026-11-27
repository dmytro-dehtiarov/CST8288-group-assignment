/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cst8288.group1.ckc.dataaccess.equipment;

import cst8288.group1.ckc.transferobjects.equipment.UsageSessionDTO;
import java.util.List;

/**
 * Defines data access operations for equipment usage sessions.
 *
 * @author Andrew Caelan Roberge-Toll
 */
public interface UsageSessionDAO {
    
    /**
     * Opens a usage session.
     *
     * @param session the usage session to insert
     */
    public void openSession(UsageSessionDTO session);
    
    /**
     * Closes a usage session by setting the close time.
     *
     * @param session the usage session to update
     */
    public void closeSession(UsageSessionDTO session);
    
    /**
     * Finds usage sessions that are still active.
     *
     * @return the list of active usage sessions
     */
    public List<UsageSessionDTO> findAllActiveSessions();
    
    
    /**
     * Finds an active usage session for a user.
     *
     * @param userId the user id
     * @return the active usage session for the user, or {@code null} if none is found
     */
    public UsageSessionDTO findActiveSessionByUserId(int userId);
    
    
    /**
     * Finds an active usage session for a piece of equipment.
     *
     * @param equipmentId the equipment id
     * @return the active usage session for the equipment, or {@code null} if none is found
     */
    public UsageSessionDTO findActiveSessionByEquipment(int equipmentId);
}
