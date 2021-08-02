package com.jak.payz.gateway.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PostCodeValidator implements ConstraintValidator<PostCode, String> {

    // close, but not a perfect validator - doesn't validate post code actually exists
    // proper validation would require data lookup - either local if available or 3rd party postcode/geolocation api
    private static final Pattern postcodePattern = Pattern.compile("^([A-Z][A-HJ-Y]?\\d[A-Z\\d]? ?\\d[A-Z]{2}|GIR ?0A{2})$");

    @Override
    public void initialize(PostCode postCode) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.isEmpty()){
            return false;
        }
        String strippedValue = value.replace(" ", "");
        if(strippedValue.length() < 5 || strippedValue.length() > 7) {
            return false;
        }
        return postcodePattern.matcher(value.toUpperCase()).matches();
    }
}
