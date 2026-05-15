CREATE TABLE IF NOT EXISTS loan_accounts (
    id                      CHAR(36)        NOT NULL PRIMARY KEY,   -- UUID as string in MariaDB
    principal_amount        DECIMAL(18, 6)  NOT NULL,
    outstanding_principal   DECIMAL(18, 6)  NOT NULL,
    annual_interest_rate    DECIMAL(8, 6)   NOT NULL,               -- e.g. 0.120000 for 12%
    accrued_interest_balance DECIMAL(18, 6) NOT NULL DEFAULT 0.000000,
    outstanding_fees        DECIMAL(18, 6)  NOT NULL DEFAULT 0.000000,
    origination_date        DATE            NOT NULL,
    last_accrual_date       DATE            NOT NULL,
    next_payment_due_date   DATE            NOT NULL,
    status                  VARCHAR(20)     NOT NULL,
    version                 BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT chk_status CHECK (status IN ('ACTIVE', 'CLOSED', 'DEFAULTED', 'SUSPENDED')),
    CONSTRAINT chk_principal CHECK (outstanding_principal >= 0),
    CONSTRAINT chk_rate CHECK (annual_interest_rate > 0)
);

CREATE INDEX IF NOT EXISTS idx_loan_status         ON loan_accounts(status);
CREATE INDEX IF NOT EXISTS idx_loan_accrual_date   ON loan_accounts(last_accrual_date);

CREATE TABLE IF NOT EXISTS accrual_ledger (
    id                      CHAR(36)        NOT NULL PRIMARY KEY DEFAULT (UUID()),
    loan_id                 CHAR(36)        NOT NULL,
    accrual_date            DATE            NOT NULL,
    principal_balance       DECIMAL(18, 6)  NOT NULL,
    daily_rate              DECIMAL(12, 10) NOT NULL,
    interest_accrued        DECIMAL(18, 6)  NOT NULL,
    running_accrued_balance DECIMAL(18, 6)  NOT NULL,
    created_at              DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    CONSTRAINT fk_accrual_loan FOREIGN KEY (loan_id) REFERENCES loan_accounts(id),
    CONSTRAINT uq_accrual_loan_date UNIQUE (loan_id, accrual_date)
);

CREATE INDEX idx_accrual_ledger_loan_id ON accrual_ledger(loan_id);
