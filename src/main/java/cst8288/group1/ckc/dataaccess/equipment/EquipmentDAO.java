/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cst8288.group1.ckc.dataaccess.equipment;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.enums.EquipmentStatus;
import cst8288.group1.ckc.transferobjects.equipment.EquipmentDTO;
import java.util.ArrayList;

/**
 *
 * @author caela
 */
public interface EquipmentDAO {
    
    // create
    public void createEquipment(EquipmentDTO equipment) throws DaoException;
    
    // getall
    public ArrayList<EquipmentDTO> getAllEquipment();
    
    // getsinglebyid
    public EquipmentDTO getEquipmentById(int equipmentId);
    
    // update status
    public void updateStatus(int equipmentId, EquipmentStatus status);
    
    // need to figure out the markServiced flow
    
    
}
