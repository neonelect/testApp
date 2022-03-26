package com.example.demo.model;

import com.example.demo.validator.LoanApplianceValidator;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

@LoanApplianceValidator
public class LoanApplianceRequest {

    @DecimalMax(value = "1000.0", message = "Maximum amount for appliance is 1000.")
    @DecimalMin(value = "1.0", message = "Minimum amount for appliance is 1.")
    @NotNull
    private BigDecimal amount;

    @Max(value = 60, message = "You can only apply for 60 days max.")
    @Min(value = 1, message = "Minimum term is at least one day.")
    private int days;

    public LoanApplianceRequest(BigDecimal amount, int days) {
        this.amount = amount;
        this.days = days;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getDays() {
        return days;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanApplianceRequest that = (LoanApplianceRequest) o;
        return getDays() == that.getDays() &&
                getAmount().equals(that.getAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAmount(), getDays());
    }
}
