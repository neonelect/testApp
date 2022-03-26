package com.example.demo.repository;

import com.example.demo.model.Loan;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class LoanRepository {

    private ConcurrentMap<UUID, Loan> loans = new ConcurrentHashMap<>();

    public Optional<Loan> getLoanById(UUID id) {
        Loan loan = loans.get(id);
        return Objects.isNull(loan) ? Optional.empty() : Optional.of(loan);
    }

    public UUID depositLoan(Loan loan) {
        UUID id = generateId();
        loans.put(id, loan);
        return id;
    }

    public UUID extendLoan(String id, Loan loan){
        UUID uuid = UUID.fromString(id);
        loans.put(uuid, loan);
        return uuid;
    }

    private UUID generateId() {
       UUID generated = UUID.randomUUID();
       while (idExists(generated)) {
           generated = UUID.randomUUID();
       }
       return generated;
    }

    private boolean idExists(UUID id) {
        return getLoanById(id).isPresent();
    }
}
