-- Create the Order table
CREATE TABLE orders
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id        UUID,
    session_id     VARCHAR(255),
    total_amount   NUMERIC(19, 2)                             NOT NULL CHECK (total_amount >= 0),
    payment_method VARCHAR(50) CHECK (payment_method IN ('WEB', 'USSD', 'TRANSFER')),
    status         VARCHAR(50)                                NOT NULL CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED')),
    created_at     TIMESTAMP        DEFAULT CURRENT_TIMESTAMP NOT NULL
);