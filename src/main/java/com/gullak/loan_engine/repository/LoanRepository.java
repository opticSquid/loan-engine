package com.gullak.loan_engine.repository;

import com.gullak.loan_engine.model.LoanAccount;
import java.time.LocalDate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface LoanRepository extends R2dbcRepository<LoanAccount, Long> {
    @Query(
        """
        SELECT * FROM loan_accounts
        WHERE status = 'ACTIVE'
          AND last_accrual_date < :today
        ORDER BY last_accrual_date ASC  -- oldest unprocesed first
        """
    )
    Flux<LoanAccount> findAllActive(@Param("today") LocalDate today);
}
