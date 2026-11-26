package cst8288.group1.ckc.transferobjects.enums;

/**
 * Current state of a piece of equipment.
 *
 * <p>Mirrors the {@code status} ENUM column in {@code db/ckc.sql}. Constant names must match the
 * database values exactly because DAOs map them with {@code valueOf}.</p>
 *
 * @author Dmytro Dehtiarov
 */
public enum EquipmentStatus {
    AVAILABLE,
    IN_USE,
    DOWN_FOR_MAINTENANCE
}
