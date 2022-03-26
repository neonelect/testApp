package com.example.demo.controller;

import com.example.demo.exception.LoanNotFoundException;
import com.example.demo.model.Loan;
import com.example.demo.model.LoanApplianceRequest;
import com.example.demo.model.LoanExtensionRequest;
import com.example.demo.service.LoanService;
import com.example.demo.util.CreatorUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @MockBean(name = "mvcValidator")
    private Validator mockValidator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void applyForLoanReturns200() throws Exception {
        UUID uuid = UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c");
        LoanApplianceRequest request = CreatorUtil.createDummyLoanApplianceRequest(
                new BigDecimal(100),
                10
        );
        when(loanService.applyForLoan(any())).thenReturn(uuid);
        this.mockMvc.perform(
                post("/loans/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString(uuid.toString())));
    }

    @Test
    void extendLoanReturns200() throws Exception {
        UUID uuid = UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c");
        LoanExtensionRequest loanExtension = CreatorUtil.createDummyLoanExtensionRequest("1", 10);
        when(loanService.extendLoan(any())).thenReturn(uuid);
        this.mockMvc.perform(
                post("/loans/extend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanExtension))
        ).andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(uuid)));
    }

    @Test
    void extendLoanReturns400() throws Exception {
        UUID uuid = UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c");
        LoanExtensionRequest loanExtension = CreatorUtil.createDummyLoanExtensionRequest("1", 10);
        when(loanService.extendLoan(any())).thenThrow(new LoanNotFoundException("Not found"));
        this.mockMvc.perform(
                post("/loans/extend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanExtension))
        ).andExpect(status().isNotFound())
                .andExpect(content().string("Not found"));
    }

    @Test
    void getLoanInfoReturns200() throws Exception {
        Loan loan = CreatorUtil.createDummyLoan(
                new BigDecimal(100),
                LocalDateTime.now()
        );
        when(loanService.fetchLoan(any())).thenReturn(loan);
        this.mockMvc.perform(
                get("/loans/{loanId}", "1")
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(loan))));
    }

    @Test
    void getLoanInfoReturns404() throws Exception {
        when(loanService.fetchLoan(any())).thenThrow(new LoanNotFoundException("Not found"));

        this.mockMvc.perform(
                get("/loans/{loanId}", "1")
        ).andExpect(status().isNotFound())
                .andExpect(content().string("Not found"));
    }
}