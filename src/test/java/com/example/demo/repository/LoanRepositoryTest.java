package com.example.demo.repository;

import com.example.demo.model.Loan;
import com.example.demo.model.LoanExtensionRequest;
import com.example.demo.util.CreatorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LoanRepositoryTest {

    private LoanRepository loanRepository = new LoanRepository();

    @Test
    void getLoanByIdReturnsProperValue() {
        //given
        Loan dummyLoan = CreatorUtil.createDummyLoan(new BigDecimal(10), LocalDateTime.now().plusDays(5));
        UUID insertedId = this.loanRepository.depositLoan(dummyLoan);

        //when
        Optional<Loan> result = loanRepository.getLoanById(insertedId);

        //then
        assertFalse(result.isEmpty());
        assertEquals(dummyLoan, result.get());
    }

    @Test
    void getLoanByIdReturnsEmptyResultWhenThereIsNoDesiredLoanWithGivenId() {
        //given
        Loan dummyLoan = CreatorUtil.createDummyLoan(new BigDecimal(10), LocalDateTime.now().plusDays(5));
        UUID insertedId = this.loanRepository.depositLoan(dummyLoan);
        UUID wrongId = UUID.fromString(replaceFirstCharWithRandomOne(insertedId.toString()));

        //when
        Optional<Loan> result = loanRepository.getLoanById(wrongId);

        //then
        assertTrue(result.isEmpty());
    }

    @Test
    void depositLoanReturnsValidResult() {
        //given
        Loan dummyLoan = CreatorUtil.createDummyLoan(new BigDecimal(10), LocalDateTime.now().plusDays(5));

        //when
        UUID result = this.loanRepository.depositLoan(dummyLoan);

        //then
        assertNotNull(result);
    }

    @Test
    void extendLoanReturnsValidProlongedLoan() {
        //given
        String id = "5fc03087-d265-11e7-b8c6-83e29cd24f4c";
        Loan dummyLoan = CreatorUtil.createDummyLoan(new BigDecimal(10), LocalDateTime.now().plusDays(5));
        UUID insertedId = this.loanRepository.depositLoan(dummyLoan);
        Loan prolongedDummyLoan = CreatorUtil.createDummyLoan(
                new BigDecimal(10),
                dummyLoan.getDueDate().plusDays(5)
        );

        //when
        UUID extendedUUID = this.loanRepository.extendLoan(insertedId.toString(), prolongedDummyLoan);
        Optional<Loan> result = loanRepository.getLoanById(extendedUUID);

        //then
        assertFalse(result.isEmpty());
        assertEquals(prolongedDummyLoan, result.get());
    }

    private String replaceFirstCharWithRandomOne(String str) {
        char[] chars = str.toCharArray();
        chars[0] = getDifferentRandomCharacter(chars[0]);
        return String.valueOf(chars);
    }

    private char getDifferentRandomCharacter(char character) {
        Random r = new Random();
        char c = (char)(r.nextInt(8) + '1');
        if(c == character) {
            return getDifferentRandomCharacter(character);
        }
        return c;
    }
}