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
     * Inserts a usage session.
     *
     * @param session the usage session to insert
     */
    public void insertSession(UsageSessionDTO session);
    
    /**
     * Updates a usage session.
     *
     * @param session the usage session to update
     */
    public void updateSesseion(UsageSessionDTO session);
    
    /**
     * Finds usage sessions that are still active.
     *
     * @return the list of active usage sessions
     */
    public List<UsageSessionDTO> findActiveSessions();
}
