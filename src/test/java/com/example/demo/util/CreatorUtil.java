package com.example.demo.util;

import com.example.demo.model.Loan;
import com.example.demo.model.LoanApplianceRequest;
import com.example.demo.model.LoanExtensionRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreatorUtil {

    private CreatorUtil(){}

    public static LoanApplianceRequest createDummyLoanApplianceRequest(BigDecimal amount, int days) {
        return new LoanApplianceRequest(amount, days);
    }

    public static LoanExtensionRequest createDummyLoanExtensionRequest(String id, int days) {
        return new LoanExtensionRequest(id, days);
    }

    public static Loan createDummyLoan(BigDecimal amount, LocalDateTime localDateTime) {
        return new Loan(amount, localDateTime);
    }
}
