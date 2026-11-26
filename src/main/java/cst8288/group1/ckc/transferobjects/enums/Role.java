package cst8288.group1.ckc.transferobjects.enums;

/**
 * Role of a user, which decides the pages and actions the user may access.
 *
 * <p>Mirrors the {@code role} ENUM column in {@code db/ckc.sql}. Constant names must match the
 * database values exactly because DAOs map them with {@code valueOf}.</p>
 *
 * @author Dmytro Dehtiarov
 */
public enum Role {
    /** Regular member who uses equipment and consumables. */
    MEMBER,
    /** Member who runs training sessions. */
    TRAINER,
    /** Member who registers equipment and manages consumables. */
    KITCHEN_TECH
}
