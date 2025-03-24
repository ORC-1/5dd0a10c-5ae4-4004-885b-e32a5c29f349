-- Create the Cart table
CREATE TABLE cart
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    total_price DECIMAL(19, 2) NOT NULL CHECK (total_price >= 0),
    user_id     UUID           NULL,
    session_id  VARCHAR(255)   NULL,
    created_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);