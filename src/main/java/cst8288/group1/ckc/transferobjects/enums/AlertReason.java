package cst8288.group1.ckc.transferobjects.enums;

/**
 * Reason a maintenance alert was raised.
 *
 * <p>Mirrors the {@code reason} ENUM column in {@code db/ckc.sql}. Constant names must match the
 * database values exactly because DAOs map them with {@code valueOf}.</p>
 *
 * @author Dmytro Dehtiarov
 */
public enum AlertReason {
    /** Accumulated usage reached the service threshold. */
    WEAR,
    /** A sensor reported a fault. */
    DIAGNOSTIC
}
