package com.example.demo.service;

import com.example.demo.exception.LoanNotFoundException;
import com.example.demo.model.Loan;
import com.example.demo.model.LoanApplianceRequest;
import com.example.demo.model.LoanExtensionRequest;
import com.example.demo.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoanService {

    private static final BigDecimal INTERESTS = new BigDecimal(10);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    private LoanRepository loanRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public UUID applyForLoan(LoanApplianceRequest request) {
        return loanRepository.depositLoan(createLoanObject(request));
    }

    public UUID extendLoan(LoanExtensionRequest request) {
        Loan loan = fetchLoan(request.getId());
        Loan extendedLoan = createExtensionLoanObject(
                loan.getAmount(),
                loan.getDueDate().plusDays(request.getDays())
        );
        return loanRepository.extendLoan(request.getId(), extendedLoan);
    }

    public Loan fetchLoan(String loanId) {
        Optional<Loan> loan = loanRepository.getLoanById(UUID.fromString(loanId));
        if(loan.isPresent()) {
            return loan.get();
        } else {
            throw new LoanNotFoundException(String.format("Loan id: %s not found", loanId));
        }
    }

    private Loan createLoanObject(LoanApplianceRequest request) {
        return new Loan(
                request.getAmount().multiply(INTERESTS).divide(ONE_HUNDRED, RoundingMode.UP),
                LocalDateTime.of(LocalDate.now().plusDays(request.getDays()), LocalTime.now())
        );
    }

    private Loan createExtensionLoanObject(BigDecimal amount, LocalDateTime dueDate) {
        return new Loan(amount, dueDate);
    }
}
