package com.gullak.loan_engine.repository;

import com.gullak.loan_engine.model.AccrualEntry;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccrualLedgerRepository
    extends ReactiveCrudRepository<AccrualEntry, UUID>
{
    Flux<AccrualEntry> findByLoanIdOrderByAccrualDateAsc(UUID loanId);

    @Query(
        "SELECT * FROM accrual_ledger WHERE loan_id = :loanId AND accrual_date = :date"
    )
    Mono<AccrualEntry> findByLoanIdAndAccrualDate(
        @Param("loanId") UUID loanId,
        @Param("date") LocalDate date
    );
}
