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
 *
 * @author caela
 */
public class EquipmentBusinessLogic {
    
    private final EquipmentDAO equipmentDAO;
    private final UsageSessionDAO usageSessionDAO;
    
    public EquipmentBusinessLogic(EquipmentDAO equipmentDAO, UsageSessionDAO usageSessionDAO) {
        this.equipmentDAO = equipmentDAO;
        this.usageSessionDAO = usageSessionDAO;
    }
    
    // accepts a built EquipmentDTO and adds it to the DB
    public void registerEquipment(EquipmentDTO equipment) {
    }
    
    // open session - creates a session DTO and adds to the db using insertSession
    public UsageSessionDTO openSession(int userId, int equipmentId) {
        return new UsageSessionDTO();
    }
    
    // close session - takes in a current session and updates it to close the session out
    public void closeSession(UsageSessionDTO session) {};
    
    // get status report
    public List<EquipmentStatusLineDTO> getStatusReport() {
        return new ArrayList<>();
    }
    
    
}
