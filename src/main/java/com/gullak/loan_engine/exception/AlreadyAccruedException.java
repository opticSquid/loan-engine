package com.gullak.loan_engine.exception;

import java.time.LocalDate;
import java.util.UUID;

public class AlreadyAccruedException extends RuntimeException {

    private final UUID loanId;
    private final LocalDate accrualDate;

    public AlreadyAccruedException(UUID loanId, LocalDate accrualDate) {
        super(
            "Accrual already processed for loan %s on %s".formatted(
                loanId,
                accrualDate
            )
        );
        this.loanId = loanId;
        this.accrualDate = accrualDate;
    }

    public UUID getLoanId() {
        return loanId;
    }

    public LocalDate getAccrualDate() {
        return accrualDate;
    }
}
