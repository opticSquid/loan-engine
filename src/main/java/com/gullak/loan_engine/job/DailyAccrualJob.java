package com.gullak.loan_engine.job;

import com.gullak.loan_engine.model.AccrualEntry;
import com.gullak.loan_engine.model.AccrualResult;
import com.gullak.loan_engine.model.LoanAccount;
import com.gullak.loan_engine.repository.AccrualLedgerRepository;
import com.gullak.loan_engine.repository.LoanRepository;
import com.gullak.loan_engine.service.LoanAccrualEngine;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyAccrualJob {

    private final LoanRepository loanRepository;
    private final LoanAccrualEngine accrualEngine;
    private final AccrualLedgerRepository accrualLedger;

    @Scheduled(cron = "0 0 1 * * *") // 1 AM daily
    @Transactional
    public void runDailyAccrual() {
        LocalDate today = LocalDate.now();

        // Process in chunks to avoid memory pressure
        loanRepository
            .findAllActive(today)
            .buffer(500)
            .flatMap(batch ->
                Flux.fromIterable(batch).flatMap(loan ->
                    accrualEngine
                        .accrueInterest(loan, today)
                        .flatMap(result -> persistAccrual(loan, result))
                )
            )
            .subscribe(null, err -> alertOps("Daily accrual failed", err));
    }

    private Mono<Void> persistAccrual(LoanAccount loan, AccrualResult result) {
        // 1. Save all ledger entries
        Flux<AccrualEntry> saveEntries = accrualLedger.saveAll(
            result.entries()
        );

        // 2. Update last accrual date + accrued interest balance on the loan
        LoanAccount updated = loan
            .withLastAccrualDate(LocalDate.now())
            .withAccruedInterestBalance(
                loan.getAccruedInterestBalance().add(result.totalAccrued())
            );
        Mono<LoanAccount> updateLoan = loanRepository.save(updated);

        return saveEntries.then(updateLoan).then();
    }

    private void alertOps(String message, Throwable err) {
        // Wire to your alerting system (PagerDuty, Slack webhook, etc.)
        log.error("[DailyAccrualJob] {} : {}", message, err.getMessage(), err);
    }
}
