package cst8288.group1.ckc.business.ledger;

import cst8288.group1.ckc.business.ValidationException;
import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.dataaccess.ledger.TransactionDao;
import cst8288.group1.ckc.transferobjects.enums.TransactionSource;
import cst8288.group1.ckc.transferobjects.enums.TransactionType;
import cst8288.group1.ckc.transferobjects.ledger.Transaction;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

/**
 * The member ledger. Every credit and debit in the application goes through {@link #record};
 * no other module calculates an amount.
 *
 * @author Dmytro Dehtiarov
 */
public class LedgerBusinessLogic {

    private final TransactionDao transactionDao;

    /**
     * Creates the ledger.
     *
     * @param dao storage for transactions
     */
    public LedgerBusinessLogic(TransactionDao dao) {
        this.transactionDao = dao;
    }

    /**
     * Records a credit or a debit. The ledger picks the rule for the source, calculates the amount,
     * decides whether it is a credit or a debit, and saves the transaction.
     *
     * <table>
     *   <caption>What to pass for each source</caption>
     *   <tr><th>source</th><th>units</th><th>unitRate</th><th>result</th></tr>
     *   <tr><td>DONATION</td><td>quantity</td><td>consumable credit value per unit</td><td>credit</td></tr>
     *   <tr><td>CONSUMABLE_USE</td><td>quantity</td><td>consumable credit value per unit</td><td>debit</td></tr>
     *   <tr><td>EQUIPMENT_USE</td><td>minutes</td><td>equipment credit rate per hour</td><td>debit</td></tr>
     *   <tr><td>TRAINING</td><td>minutes</td><td>{@code null}</td><td>credit</td></tr>
     *   <tr><td>MAINTENANCE</td><td>minutes</td><td>{@code null}</td><td>credit</td></tr>
     * </table>
     *
     * @param userId the member who earns or pays
     * @param source the activity
     * @param units the quantity or the minutes, see the table
     * @param unitRate the price of one unit, or {@code null} where the table says so
     * @param description a short note for the statement, for example "Session 3: OVN-001"
     * @return the saved transaction
     * @throws ValidationException if units are not positive or a required rate is missing
     * @throws DaoException if saving fails
     */
    public Transaction record(int userId, TransactionSource source, BigDecimal units,
                              BigDecimal unitRate, String description)
            throws ValidationException, DaoException {
        // TEMPORARY: returns an unsaved transaction until the ledger logic is implemented (#15)
        return new Transaction(userId, TransactionType.CREDIT, source, BigDecimal.ZERO, description);
    }

    /**
     * Returns a member's balance: credits and payments minus debits, over all time.
     *
     * @param userId the member
     * @return the balance; negative when the member owes credits
     * @throws DaoException if reading fails
     */
    public BigDecimal getBalance(int userId) throws DaoException {
        return BigDecimal.ZERO; // TEMPORARY until #15
    }

    /**
     * Returns a member's transactions for one calendar month, oldest first.
     *
     * @param userId the member
     * @param month the month
     * @return the transactions; empty when there are none
     * @throws DaoException if reading fails
     */
    public List<Transaction> getStatement(int userId, YearMonth month) throws DaoException {
        return List.of(); // TEMPORARY until #15
    }

    /**
     * Returns what a member must settle for a month: the negative balance at the end of that month,
     * as a positive number, or zero when the member does not owe anything. It is due within
     * 10 days after the month ends.
     *
     * @param userId the member
     * @param month the month
     * @return the amount to settle, zero or positive
     * @throws DaoException if reading fails
     */
    public BigDecimal getAmountToSettle(int userId, YearMonth month) throws DaoException {
        return BigDecimal.ZERO; // TEMPORARY until #15
    }
}