package com.example.demo;

import com.example.demo.controller.LoanController;
import com.example.demo.exception.LoanNotFoundException;
import com.example.demo.model.Loan;
import com.example.demo.model.LoanApplianceRequest;
import com.example.demo.model.LoanExtensionRequest;
import com.example.demo.util.CreatorUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { LoanConfiguration.class })
@WebAppConfiguration
public class LoanControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private UUID insertedId;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();


    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("loanController"));
    }

    @Test
    void applyForLoanReturns200() throws Exception {
        UUID uuid = UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c");
        LoanApplianceRequest request = CreatorUtil.createDummyLoanApplianceRequest(
                new BigDecimal(100),
                10
        );

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

        this.mockMvc.perform(
                get("/loans/{loanId}", "1")
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(loan))));
    }

    @Test
    void getLoanInfoReturns404() throws Exception {
        this.mockMvc.perform(
                get("/loans/{loanId}", "1")
        ).andExpect(status().isNotFound());
    }
}
