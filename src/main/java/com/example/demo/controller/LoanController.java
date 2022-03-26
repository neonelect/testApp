package com.example.demo.controller;

import com.example.demo.model.Loan;
import com.example.demo.model.LoanApplianceRequest;
import com.example.demo.model.LoanExtensionRequest;
import com.example.demo.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("loans")
public class LoanController {

    private LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping(path = "apply", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> applyForLoan(
            @Valid @RequestBody LoanApplianceRequest loanApplianceRequest
    ) {
        return ResponseEntity.ok(loanService.applyForLoan(loanApplianceRequest));
    }

    @PostMapping(path = "extend", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> extendLoan(
            @Valid @RequestBody LoanExtensionRequest loanExtensionRequest
    ) {
        return ResponseEntity.ok(loanService.extendLoan(loanExtensionRequest));
    }

    @GetMapping(path = "{loanId}")
    public ResponseEntity<Loan> getLoanInfo(@PathVariable String loanId) {
        return ResponseEntity.ok(loanService.fetchLoan(loanId));
    }
}
