package cst8288.group1.ckc.presentation;

import cst8288.group1.ckc.business.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Reads and checks form fields. Every method turns empty or invalid input into a
 * {@link ValidationException} with a message the user can read, so a bad form never becomes a 500.
 *
 * @author Dmytro Dehtiarov
 */
public final class FormParams {

    private FormParams() { }

    /**
     * Returns trimmed text that is not empty.
     *
     * @param raw the submitted value, may be null
     * @param label the field name shown to the user, for example "Title"
     * @return the trimmed text
     * @throws ValidationException if the value is missing or blank
     */
    public static String requireText(String raw, String label) throws ValidationException {
        if (raw == null || raw.isBlank()) {
            throw new ValidationException(label + " is required");
        }
        return raw.trim();
    }

    /**
     * Returns a whole number.
     *
     * @param raw the submitted value, may be null
     * @param label the field name shown to the user
     * @return the number
     * @throws ValidationException if the value is missing or not a whole number
     */
    public static int requireInt(String raw, String label) throws ValidationException {
        String text = requireText(raw, label);
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new ValidationException(label + " must be a whole number");
        }
    }

    /**
     * Returns a number greater than zero, such as a quantity or a price.
     *
     * @param raw the submitted value, may be null
     * @param label the field name shown to the user
     * @return the number
     * @throws ValidationException if the value is missing, not a number, or not greater than zero
     */
    public static BigDecimal requirePositiveDecimal(String raw, String label) throws ValidationException {
        String text = requireText(raw, label);
        BigDecimal value;
        try {
            value = new BigDecimal(text);
        } catch (NumberFormatException e) {
            throw new ValidationException(label + " must be a number");
        }
        if (value.signum() <= 0) {
            throw new ValidationException(label + " must be greater than 0");
        }
        return value;
    }

    /**
     * Returns a date and time in the format an {@code <input type="datetime-local">} sends,
     * for example {@code 2026-11-26T15:00}.
     *
     * @param raw the submitted value, may be null
     * @param label the field name shown to the user
     * @return the date and time
     * @throws ValidationException if the value is missing or not a valid date and time
     */
    public static LocalDateTime requireDateTime(String raw, String label) throws ValidationException {
        String text = requireText(raw, label);
        try {
            return LocalDateTime.parse(text);
        } catch (DateTimeParseException e) {
            throw new ValidationException(label + " must be a valid date and time");
        }
    }

    /**
     * Returns an enum constant whose name matches the value, for example {@code OVEN}.
     *
     * @param raw the submitted value, may be null
     * @param type the enum class
     * @param label the field name shown to the user
     * @param <E> the enum type
     * @return the constant
     * @throws ValidationException if the value is missing or names no constant
     */
    public static <E extends Enum<E>> E requireEnum(String raw, Class<E> type, String label)
            throws ValidationException {
        String text = requireText(raw, label);
        try {
            return Enum.valueOf(type, text);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(label + " has an unknown value");
        }
    }
}