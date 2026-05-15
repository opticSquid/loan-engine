package com.gullak.loan_engine.service;

import com.gullak.loan_engine.exception.AlreadyAccruedException;
import com.gullak.loan_engine.model.AccrualEntry;
import com.gullak.loan_engine.model.AccrualResult;
import com.gullak.loan_engine.model.LoanAccount;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LoanAccrualEngine {

    private static final int DAYS_IN_YEAR = 365;
    private static final MathContext MC = new MathContext(
        10,
        RoundingMode.HALF_UP
    );

    public Mono<AccrualResult> accrueInterest(
        LoanAccount loan,
        LocalDate accrualDate
    ) {
        return Mono.fromCallable(() -> {
            if (!accrualDate.isAfter(loan.getLastAccrualDate())) {
                throw new AlreadyAccruedException(loan.getId(), accrualDate);
            }

            LocalDate from = loan.getLastAccrualDate().plusDays(1);
            BigDecimal totalAccrued = BigDecimal.ZERO;
            List<AccrualEntry> entries = new ArrayList<>();
            LocalDate current = from;

            while (!current.isAfter(accrualDate)) {
                BigDecimal dailyRate = loan
                    .getAnnualInterestRate()
                    .divide(BigDecimal.valueOf(DAYS_IN_YEAR), MC);

                BigDecimal dailyInterest = loan
                    .getOutstandingPrincipal()
                    .multiply(dailyRate, MC)
                    .setScale(6, RoundingMode.HALF_UP);

                totalAccrued = totalAccrued.add(dailyInterest);

                entries.add(
                    new AccrualEntry(
                        loan.getId(),
                        current,
                        loan.getOutstandingPrincipal(),
                        dailyRate,
                        dailyInterest,
                        loan.getAccruedInterestBalance().add(totalAccrued)
                    )
                );

                current = current.plusDays(1);
            }

            return new AccrualResult(entries, totalAccrued);
        });
    }
}
