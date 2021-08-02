package com.jak.payz.gateway.validator;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Constraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Documented
@NotBlank
@DateTimeFormat(pattern = "MM/yy", fallbackPatterns = {"MM/yyyy"})
@Constraint(validatedBy = ExpiryDateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpiryDate {

    String message() default "Valid expiry date (MM/yy) is required.";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}