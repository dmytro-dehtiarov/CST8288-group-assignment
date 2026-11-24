package cst8288.group1.ckc.transferobjects.enums;

/**
 * Type of equipment.
 *
 * <p>Used both on equipment and on training records, where it names the equipment a training
 * qualifies a user for. Mirrors the {@code category} and {@code equipment_category} ENUM columns in
 * {@code db/ckc.sql}; both columns use this one enum so qualification checks compare values of the
 * same type.</p>
 *
 * @author Dmytro Dehtiarov
 */
public enum EquipmentCategory {
    OVEN,
    MIXER,
    DOUGH_SHEETER,
    PROOFER,
    COOLER
}
