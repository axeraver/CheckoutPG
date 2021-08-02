package com.jak.payz.gateway.validator;

import javax.validation.Constraint;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.*;

@Documented
@NotBlank
@Constraint(validatedBy = PostCodeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostCode {
    String message() default "Valid post code required.";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
