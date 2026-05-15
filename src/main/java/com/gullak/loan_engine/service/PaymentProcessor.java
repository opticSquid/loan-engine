package com.gullak.loan_engine.service;

import com.gullak.loan_engine.model.LoanAccount;
import com.gullak.loan_engine.model.PaymentAllocation;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class PaymentProcessor {

    public PaymentAllocation allocate(
        LoanAccount loan,
        BigDecimal paymentAmount
    ) {
        BigDecimal remaining = paymentAmount;
        BigDecimal toFees = BigDecimal.ZERO;
        BigDecimal toInterest = BigDecimal.ZERO;
        BigDecimal toPrincipal = BigDecimal.ZERO;

        // 1. Fees first (late fees, origination fees outstanding)
        BigDecimal fees = loan.getOutstandingFees();
        toFees = remaining.min(fees);
        remaining = remaining.subtract(toFees);

        // 2. Accrued interest second
        BigDecimal interest = loan.getAccruedInterestBalance();
        toInterest = remaining.min(interest);
        remaining = remaining.subtract(toInterest);

        // 3. Principal last
        toPrincipal = remaining.min(loan.getOutstandingPrincipal());

        return new PaymentAllocation(toFees, toInterest, toPrincipal);
    }
}
