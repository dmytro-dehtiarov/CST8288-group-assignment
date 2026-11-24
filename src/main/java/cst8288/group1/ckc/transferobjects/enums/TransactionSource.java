package cst8288.group1.ckc.transferobjects.enums;

/**
 * Activity that caused a ledger transaction.
 *
 * <p>The ledger uses this value to pick the {@code CreditStrategy} for a credit. Mirrors the
 * {@code source} ENUM column in {@code db/ckc.sql}.</p>
 *
 * @author Dmytro Dehtiarov
 */
public enum TransactionSource {
    /** A user donated consumables. */
    DONATION,
    /** A trainer ran a training session. */
    TRAINING,
    /** A user completed a maintenance task. */
    MAINTENANCE,
    /** A user ran equipment in a usage session. */
    EQUIPMENT_USE,
    /** A user used consumables in a usage session. */
    CONSUMABLE_USE,
    /** Monthly settlement of the balance. */
    SETTLEMENT
}
