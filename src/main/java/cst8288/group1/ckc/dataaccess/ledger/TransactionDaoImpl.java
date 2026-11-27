package cst8288.group1.ckc.dataaccess.ledger;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.ledger.Transaction;

import java.time.YearMonth;
import java.util.List;

/**
 * JDBC implementation of {@link TransactionDao}.
 *
 * <p>TEMPORARY: every method throws until the implementation lands in #14.</p>
 *
 * @author Dmytro Dehtiarov
 */
public class TransactionDaoImpl implements TransactionDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction add(Transaction transaction) throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction findById(int transactionId) throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findByUser(int userId) throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findByUserAndMonth(int userId, YearMonth month) throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
