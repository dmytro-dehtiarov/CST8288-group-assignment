package cst8288.group1.ckc.dataaccess;

/**
 * Checked exception that every DAO throws when a database operation fails.
 *
 * <p>DAOs catch {@link java.sql.SQLException} and wrap it here, so no JDBC type leaks into the
 * business or presentation layer. The original exception stays available as the cause for
 * logging.</p>
 *
 * @author Dmytro Dehtiarov
 */
public class DaoException extends Exception {
    /**
     * Creates an exception with a message and the low-level cause.
     *
     * @param message what operation failed, for example {@code "findById failed"}
     * @param cause the original exception, usually a {@link java.sql.SQLException}
     */
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates an exception with a message.
     *
     * @param message what operation failed, for example {@code "findById failed"}
     */
    public DaoException(String message) {
        super(message);
    }
}
