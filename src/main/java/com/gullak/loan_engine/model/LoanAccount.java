package com.gullak.loan_engine.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@Table("loan_accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanAccount {

    @Id
    private UUID id;

    private BigDecimal principalAmount;

    private BigDecimal outstandingPrincipal;

    private BigDecimal annualInterestRate;

    private BigDecimal accruedInterestBalance;

    private BigDecimal outstandingFees;

    private LocalDate originationDate;

    private LocalDate lastAccrualDate;

    private LocalDate nextPaymentDueDate;

    private LoanStatus status;

    @Version
    private Long version; // optimistic locking — prevents concurrent accrual overwrites

    // Wither methods preserved from the record
    public LoanAccount withLastAccrualDate(LocalDate lastAccrualDate) {
        this.lastAccrualDate = lastAccrualDate;
        return this;
    }

    public LoanAccount withAccruedInterestBalance(
        BigDecimal accruedInterestBalance
    ) {
        this.accruedInterestBalance = accruedInterestBalance;
        return this;
    }
}
