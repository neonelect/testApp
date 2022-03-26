package com.example.demo.model;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

public class LoanExtensionRequest {

    @NotBlank(message = "ID must not be empty")
    private String id;

    @Min(value = 1, message = "Extension period is 1 day minimum")
    private int days;

    public LoanExtensionRequest(String id, int days) {
        this.id = id;
        this.days = days;
    }

    public String getId() {
        return id;
    }

    public int getDays() {
        return days;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanExtensionRequest that = (LoanExtensionRequest) o;
        return getDays() == that.getDays() &&
                getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDays());
    }
}
