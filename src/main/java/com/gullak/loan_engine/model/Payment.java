package com.gullak.loan_engine.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record Payment(
    UUID loanId,
    LocalDate paymentDate,
    BigDecimal totalAmount,
    BigDecimal appliedToInterest, // always applied first
    BigDecimal appliedToPrincipal,
    BigDecimal appliedToFees
) {}
