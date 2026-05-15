package com.gullak.loan_engine.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("accrual_ledger")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccrualEntry {

    @Id
    private UUID id;

    private UUID loanId;
    private LocalDate accrualDate;
    private BigDecimal principalBalance;
    private BigDecimal dailyRate;
    private BigDecimal interestAccrued;
    private BigDecimal runningAccruedBalance;

    @CreatedDate
    private Instant createdAt;

    public AccrualEntry(
        UUID loanId,
        LocalDate accrualDate,
        BigDecimal principalBalance,
        BigDecimal dailyRate,
        BigDecimal interestAccrued,
        BigDecimal runningAccruedBalance
    ) {
        this.loanId = loanId;
        this.accrualDate = accrualDate;
        this.principalBalance = principalBalance;
        this.dailyRate = dailyRate;
        this.interestAccrued = interestAccrued;
        this.runningAccruedBalance = runningAccruedBalance;
    }
}
