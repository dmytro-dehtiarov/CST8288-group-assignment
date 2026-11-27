package cst8288.group1.ckc.transferobjects.ledger;

import cst8288.group1.ckc.transferobjects.enums.TransactionSource;
import cst8288.group1.ckc.transferobjects.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * One row of the member ledger: a credit, a debit or a payment.
 *
 * <p>Mirrors the {@code transactions} table. The database sets {@code transactionId} and
 * {@code createdAt} when the row is inserted, so a new transaction has the id 0 and no
 * creation time.</p>
 *
 * @param transactionId the database id; 0 before the transaction is saved
 * @param userId the member this transaction belongs to
 * @param type whether the amount adds to or removes from the balance
 * @param source the activity that caused the transaction
 * @param amount the amount, always positive; {@code type} gives its sign
 * @param description a short note for the statement, for example "Session 3: OVN-001, 2.0 h"
 * @param createdAt when the database recorded the transaction; {@code null} before it is saved
 * @author Dmytro Dehtiarov
 */
public record Transaction(
        int transactionId,
        int userId,
        TransactionType type,
        TransactionSource source,
        BigDecimal amount,
        String description,
        LocalDateTime createdAt
) {

    /**
     * Creates a transaction with every field, as the DAO does for a row read from the database.
     *
     * @throws NullPointerException if {@code type}, {@code source} or {@code amount} is null
     */
    public Transaction {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(amount, "amount");
    }

    /**
     * Creates a new transaction that is not saved yet; the database sets its id and creation time.
     *
     * @param userId the member this transaction belongs to
     * @param type whether the amount adds to or removes from the balance
     * @param source the activity that caused the transaction
     * @param amount the amount, always positive
     * @param description a short note for the statement
     * @throws NullPointerException if {@code type}, {@code source} or {@code amount} is null
     */
    public Transaction(int userId, TransactionType type, TransactionSource source,
                       BigDecimal amount, String description) {
        this(0, userId, type, source, amount, description, null);
    }
}