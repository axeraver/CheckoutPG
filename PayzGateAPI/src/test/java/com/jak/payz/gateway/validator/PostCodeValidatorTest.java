package com.jak.payz.gateway.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.Test;

class PostCodeValidatorTest {

    private final ConstraintValidatorContext mockContext = mock(ConstraintValidatorContext.class);

    private final PostCodeValidator classUnderTest = new PostCodeValidator();

    @Test
    void testIsValidReturnsFalseWhenNullEmptyOrBlankString() {
        assertFalse(classUnderTest.isValid(null, mockContext));
        assertFalse(classUnderTest.isValid("", mockContext));
        assertFalse(classUnderTest.isValid(" ", mockContext));
    }

    @Test
    void testIsValidReturnsFalseWhenInvalidLengthPostcodes() {
        assertFalse(classUnderTest.isValid("AA AA", mockContext));
        assertFalse(classUnderTest.isValid("AA1234AA", mockContext));
    }

    @Test
    void testIsValidReturnsFalseWhenInvalidPostcodePatterns() {
        assertFalse(classUnderTest.isValid("1A AA1", mockContext));
        assertFalse(classUnderTest.isValid("AA1234", mockContext));
        assertFalse(classUnderTest.isValid("12345", mockContext));
        assertFalse(classUnderTest.isValid("AAB BAA", mockContext));
    }

    @Test
    void testIsValidReturnsTrueWhenValidPostcodePatterns() {
        assertTrue(classUnderTest.isValid("A1 1AA", mockContext));
        assertTrue(classUnderTest.isValid("A11AA", mockContext));
        assertTrue(classUnderTest.isValid("B22 2BB", mockContext));
        assertTrue(classUnderTest.isValid("cd34 5ee", mockContext));
    }

}