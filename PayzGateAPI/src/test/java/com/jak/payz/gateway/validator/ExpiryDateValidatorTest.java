package com.jak.payz.gateway.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.Test;

class ExpiryDateValidatorTest {

    private final ConstraintValidatorContext mockContext = mock(ConstraintValidatorContext.class);

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/yy");

    private final ExpiryDateValidator classUnderTest = new ExpiryDateValidator();

    @Test
    void testIsValidReturnsFalseWhenNullEmptyOrBlankString() {
        assertFalse(classUnderTest.isValid(null, mockContext));
        assertFalse(classUnderTest.isValid("", mockContext));
        assertFalse(classUnderTest.isValid(" ", mockContext));
    }

    @Test
    void testIsValidReturnsFalseWhenInvalidStringLength() {
        assertFalse(classUnderTest.isValid("12", mockContext));
        assertFalse(classUnderTest.isValid("12345678", mockContext));
    }

    @Test
    void testIsValidReturnsFalseWhenInvalidExpiryDatePattern() {
        assertFalse(classUnderTest.isValid("abc", mockContext));
        assertFalse(classUnderTest.isValid("1/1/1", mockContext));
        assertFalse(classUnderTest.isValid("Mar/2032", mockContext));
        assertFalse(classUnderTest.isValid("12032", mockContext));
    }

    @Test
    void testIsValidReturnsFalseWhenExpiryDateInPast() {
        LocalDateTime pastDate = LocalDateTime.now().minusYears(2);
        assertFalse(classUnderTest.isValid(pastDate.format(dtf), mockContext));
        pastDate = LocalDateTime.now().minusMonths(1);
        assertFalse(classUnderTest.isValid(pastDate.format(dtf), mockContext));
    }

    @Test
    void testIsValidReturnsTrueForValidExpiryDate() {
        assertTrue(classUnderTest.isValid("12/2199", mockContext));
        assertTrue(classUnderTest.isValid("02/2199", mockContext));
        assertTrue(classUnderTest.isValid("12/99", mockContext));
        assertTrue(classUnderTest.isValid("02/99", mockContext));
    }
}