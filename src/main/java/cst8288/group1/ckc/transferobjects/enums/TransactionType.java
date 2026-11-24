package cst8288.group1.ckc.transferobjects.enums;

/**
 * Direction of a ledger transaction.
 *
 * <p>The amount of a transaction is always positive; this type gives its sign. Mirrors the
 * {@code type} ENUM column in {@code db/ckc.sql}.</p>
 *
 * @author Dmytro Dehtiarov
 */
public enum TransactionType {
    /** Adds credits to the user's balance. */
    CREDIT,
    /** Removes credits from the user's balance. */
    DEBIT,
    /** Payment toward a balance. */
    PAYMENT
}
