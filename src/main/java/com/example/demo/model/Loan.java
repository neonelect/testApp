package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Loan {

    private final BigDecimal amount;
    private final LocalDateTime dueDate;

    public Loan(BigDecimal amount, LocalDateTime dueDate) {
        this.amount = amount;
        this.dueDate = dueDate;
    }

    public BigDecimal getAmount() { return amount; }

    public LocalDateTime getDueDate() {
        return dueDate;
    }
}
