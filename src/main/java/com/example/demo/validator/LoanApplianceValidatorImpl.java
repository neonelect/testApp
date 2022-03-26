package com.example.demo.validator;

import com.example.demo.model.LoanApplianceRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ConfigurationProperties(prefix = "loan")
public class LoanApplianceValidatorImpl implements ConstraintValidator<LoanApplianceValidator, LoanApplianceRequest> {

    private static final LocalTime MIN_TIME = LocalTime.MIDNIGHT;
    private static final LocalTime MAX_TIME = LocalTime.of(6,0);

    @Override
    public boolean isValid(
            LoanApplianceRequest request,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        LocalDate today = LocalDate.now();
        LocalDateTime minToday = LocalDateTime.of(today, MIN_TIME);
        LocalDateTime maxToday = LocalDateTime.of(today, MAX_TIME);
        LocalDateTime now = LocalDateTime.now();
        return !(now.isAfter(minToday) && now.isBefore(maxToday));
    }
}
