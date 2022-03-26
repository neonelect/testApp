package com.example.demo.controller;

import com.example.demo.exception.LoanNotFoundException;
import com.example.demo.model.Loan;
import com.example.demo.model.LoanApplianceRequest;
import com.example.demo.service.LoanService;
import com.example.demo.util.CreatorUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
class LoanExceptionHandlerTest {

    @MockBean
    private LoanService loanService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testHandleValidationExceptions() throws Exception {
        //given
        String expected =
                "{\"amount\":\"Maximum amount for appliance is 1000.\"," +
                        "\"days\":\"You can only apply for 60 days max.\"}";
        LoanApplianceRequest loanApplianceRequest = CreatorUtil.createDummyLoanApplianceRequest(
                new BigDecimal(1200),
                100
        );

        //when
        this.mockMvc.perform(
                post("/loans/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanApplianceRequest))
        ).andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(expected)));
    }

    @Test
    void testHandleLoanNotFoundException() throws Exception {
        //given
        Loan loan = CreatorUtil.createDummyLoan(
                new BigDecimal(100),
                LocalDateTime.now()
        );
        when(loanService.fetchLoan(any())).thenThrow(LoanNotFoundException.class);

        //when
        this.mockMvc.perform(
                get("/loans/{loanId}", "1")
        ).andExpect(status().isNotFound());
    }
}