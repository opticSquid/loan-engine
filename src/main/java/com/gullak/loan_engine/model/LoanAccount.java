package com.gullak.loan_engine.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LoanAccount(
    UUID id,
    BigDecimal principalAmount,
    BigDecimal outstandingPrincipal,
    BigDecimal annualInterestRate, // e.g., 0.12 for 12%
    BigDecimal accruedInterestBalance, // unpaid accrued interest
    LocalDate originationDate,
    LocalDate lastAccrualDate,
    LocalDate nextPaymentDueDate,
    LoanStatus status
) {}
