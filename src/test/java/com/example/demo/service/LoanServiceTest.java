package com.example.demo.service;

import com.example.demo.exception.LoanNotFoundException;
import com.example.demo.model.Loan;
import com.example.demo.model.LoanApplianceRequest;
import com.example.demo.model.LoanExtensionRequest;
import com.example.demo.repository.LoanRepository;
import com.example.demo.util.CreatorUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanService loanService;

    @Test
    void applyForLoanReturnsValidResult() {
        //given
        UUID expected = UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c");
        LoanApplianceRequest request = CreatorUtil.createDummyLoanApplianceRequest(new BigDecimal(100), 10);
        when(loanRepository.depositLoan(any())).thenReturn(expected);

        //when
        UUID response = loanService.applyForLoan(request);

        //then
        assertEquals(expected, response);
    }

    @Test
    void extendLoanReturnsValidResult() {
        //given
        String id = "5fc03087-d265-11e7-b8c6-83e29cd24f4c";
        BigDecimal amount = new BigDecimal(100);
        LocalDateTime dueDate = LocalDateTime.now().plusDays(5);
        LoanExtensionRequest request = CreatorUtil.createDummyLoanExtensionRequest(id, 10);
        when(loanRepository.getLoanById(any())).thenReturn(Optional.of(CreatorUtil.createDummyLoan(amount, dueDate)));
        when(loanRepository.extendLoan(any(), any())).thenReturn(UUID.fromString(id));

        //when
        UUID response = loanService.extendLoan(request);

        //then
        assertEquals(UUID.fromString(id), response);
    }

    @Test
    void extendLoanReturnsLoanNotFoundException() {
        //given
        String id = "5fc03087-d265-11e7-b8c6-83e29cd24f4c";
        BigDecimal amount = new BigDecimal(100);
        LocalDateTime dueDate = LocalDateTime.now().plusDays(5);
        LoanExtensionRequest request = CreatorUtil.createDummyLoanExtensionRequest(id, 10);
        when(loanRepository.getLoanById(any())).thenReturn(Optional.empty());

        //then
        assertThrows(LoanNotFoundException.class, () ->
                //when
                loanService.extendLoan(request)
        );
    }

    @Test
    void fetchLoanReturnsValidResult() {
        //given
        String id = "5fc03087-d265-11e7-b8c6-83e29cd24f4c";
        BigDecimal amount = new BigDecimal(100);
        LocalDateTime dueDate = LocalDateTime.now().plusDays(5);
        when(loanRepository.getLoanById(any())).thenReturn(Optional.of(CreatorUtil.createDummyLoan(amount, dueDate)));

        //when
        Loan response = loanService.fetchLoan(id);

        //then
        assertEquals(amount, response.getAmount());
        assertEquals(dueDate, response.getDueDate());
    }
}