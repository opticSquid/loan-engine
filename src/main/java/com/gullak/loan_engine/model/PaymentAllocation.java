package com.gullak.loan_engine.model;

import java.math.BigDecimal;
import java.util.stream.Stream;

public record PaymentAllocation(
    BigDecimal appliedToFees,
    BigDecimal appliedToInterest,
    BigDecimal appliedToPrincipal
) {
    // Fail fast if allocation math is wrong (should never happen)
    public PaymentAllocation {
        if (
            Stream.of(
                appliedToFees,
                appliedToInterest,
                appliedToPrincipal
            ).anyMatch(v -> v.compareTo(BigDecimal.ZERO) < 0)
        ) {
            throw new IllegalArgumentException(
                "Allocation amounts cannot be negative"
            );
        }
    }

    public BigDecimal total() {
        return appliedToFees.add(appliedToInterest).add(appliedToPrincipal);
    }

    public boolean isFullPayment(BigDecimal expected) {
        return total().compareTo(expected) == 0;
    }
}
