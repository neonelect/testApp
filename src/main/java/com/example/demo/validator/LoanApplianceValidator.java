package com.example.demo.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {LoanApplianceValidatorImpl.class})
@Documented
public @interface LoanApplianceValidator {
    String message() default "You can't apply for max amount between 00:00 and 06:00";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}