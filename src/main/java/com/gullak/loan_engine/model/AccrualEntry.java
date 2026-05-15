package com.gullak.loan_engine.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record AccrualEntry(
    UUID loanId,
    LocalDate accrualDate,
    BigDecimal principalBalance, // snapshot at time of accrual
    BigDecimal dailyRate,
    BigDecimal interestAccrued,
    BigDecimal runningAccruedBalance
) {}
