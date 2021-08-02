package com.jak.payz.gateway.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

public class ExpiryDateValidator implements ConstraintValidator<ExpiryDate, String> {

    private static final DateTimeFormatter ExpiryDateFormatter = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("M/yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("M/yy"))
            .toFormatter();

    @Override
    public void initialize(ExpiryDate expiryDate) {
    }



    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        String strippedValue = value.replace(" ", "");
        if(strippedValue.length() != 5 && strippedValue.length() != 7) {
            return false;
        }
        try {
            LocalDate expiryDate = YearMonth.parse(strippedValue, ExpiryDateFormatter).atEndOfMonth();
            return !expiryDate.isBefore(LocalDate.now());
        } catch (DateTimeParseException ex) {

            return false;
        }
    }
}
