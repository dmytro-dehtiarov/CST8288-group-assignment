package cst8288.group1.ckc.presentation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cst8288.group1.ckc.business.ValidationException;
import cst8288.group1.ckc.transferobjects.enums.EquipmentCategory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link FormParams}.
 *
 * @author Dmytro Dehtiarov
 */
class FormParamsTest {

    @Test
    void textIsTrimmed() throws ValidationException {
        assertEquals("Oven safety", FormParams.requireText("  Oven safety ", "Title"));
    }

    @Test
    void missingOrBlankTextIsRejected() {
        ValidationException e = assertThrows(ValidationException.class, () -> FormParams.requireText("  ", "Title"));
        assertEquals("Title is required", e.getMessage());
        assertThrows(ValidationException.class, () -> FormParams.requireText(null, "Title"));
    }

    @Test
    void intMustBeAWholeNumber() throws ValidationException {
        assertEquals(42, FormParams.requireInt("42", "Id"));
        ValidationException e = assertThrows(ValidationException.class, () -> FormParams.requireInt("4.2", "Id"));
        assertEquals("Id must be a whole number", e.getMessage());
    }

    @Test
    void decimalMustBePositive() throws ValidationException {
        assertEquals(new BigDecimal("1.5"), FormParams.requirePositiveDecimal("1.5", "Quantity"));
        assertThrows(ValidationException.class, () -> FormParams.requirePositiveDecimal("0", "Quantity"));
        assertThrows(ValidationException.class, () -> FormParams.requirePositiveDecimal("-3", "Quantity"));
        assertThrows(ValidationException.class, () -> FormParams.requirePositiveDecimal("abc", "Quantity"));
    }

    @Test
    void dateTimeFromDatetimeLocalInput() throws ValidationException {
        assertEquals(LocalDateTime.of(2026, 11, 26, 15, 0),
                FormParams.requireDateTime("2026-11-26T15:00", "Start"));
        assertThrows(ValidationException.class, () -> FormParams.requireDateTime("tomorrow", "Start"));
    }

    @Test
    void enumByName() throws ValidationException {
        assertEquals(EquipmentCategory.OVEN, FormParams.requireEnum("OVEN", EquipmentCategory.class, "Category"));
        assertThrows(ValidationException.class,
                () -> FormParams.requireEnum("TOASTER", EquipmentCategory.class, "Category"));
    }
}