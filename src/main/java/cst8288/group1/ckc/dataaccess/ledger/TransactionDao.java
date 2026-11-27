package cst8288.group1.ckc.dataaccess.ledger;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.ledger.Transaction;
import java.time.YearMonth;
import java.util.List;

/**
 * Data access for the {@code transactions} table.
 *
 * @author Dmytro Dehtiarov
 */
public interface TransactionDao {

    /**
     * Saves a new transaction. Its {@code transactionId} and {@code createdAt} are ignored:
     * the database sets both.
     *
     * @param transaction the transaction to save
     * @return the transaction with the id and creation time set by the database
     * @throws DaoException if the insert fails
     */
    Transaction add(Transaction transaction) throws DaoException;

    /**
     * Returns one transaction.
     *
     * @param transactionId the id
     * @return the transaction, or {@code null} when there is none with that id
     * @throws DaoException if the query fails
     */
    Transaction findById(int transactionId) throws DaoException;

    /**
     * Returns every transaction of a member, oldest first.
     *
     * @param userId the member
     * @return the transactions; empty when there are none
     * @throws DaoException if the query fails
     */
    List<Transaction> findByUser(int userId) throws DaoException;

    /**
     * Returns a member's transactions created in one calendar month, oldest first.
     *
     * @param userId the member
     * @param month the month, for example {@code YearMonth.of(2026, 11)}
     * @return the transactions; empty when there are none
     * @throws DaoException if the query fails
     */
    List<Transaction> findByUserAndMonth(int userId, YearMonth month) throws DaoException;
}