package com.gullak.loan_engine.model;

import java.math.BigDecimal;
import java.util.List;

public record AccrualResult(
    List<AccrualEntry> entries,
    BigDecimal totalAccrued
) {
    // Convenience — sum all entries as a sanity check
    public BigDecimal verify() {
        return entries
            .stream()
            .map(AccrualEntry::getInterestAccrued)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
