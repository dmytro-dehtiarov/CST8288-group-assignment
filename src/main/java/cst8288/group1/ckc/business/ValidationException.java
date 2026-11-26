package cst8288.group1.ckc.business;

import cst8288.group1.ckc.dataaccess.DaoException;

/**
 * Checked exception that business logic throws when user input breaks a business rule.
 *
 * <p>The message is meant for the user: servlets catch this exception and put its message in the
 * request attribute {@code error}, so the form can show it. Infrastructure failures use
 * {@link DaoException} instead.</p>
 *
 * @author Dmytro Dehtiarov
 */
public class ValidationException extends Exception {
    /**
     * Creates an exception with a message the user can read.
     *
     * @param message what is wrong with the input, for example {@code "Weight must be positive"}
     */
    public ValidationException(String message) {
        super(message);
    }
}
