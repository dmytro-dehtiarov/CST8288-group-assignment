package cst8288.group1.ckc.transferobjects.enums;

/**
 * Unit in which a consumable is measured.
 *
 * <p>Mirrors the {@code unit} ENUM column in {@code db/ckc.sql}. Constant names must match the
 * database values exactly because DAOs map them with {@code valueOf}.</p>
 *
 * @author Dmytro Dehtiarov
 */
public enum ConsumableUnit {
    KG,
    L,
    UNIT
}
