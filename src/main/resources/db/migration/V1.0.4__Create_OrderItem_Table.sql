-- Create the OrderItem table
CREATE TABLE order_item
(
    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID           NOT NULL,
    book_id  UUID           NOT NULL,
    quantity INTEGER        NOT NULL CHECK (quantity > 0),
    subtotal NUMERIC(19, 2) NOT NULL CHECK (subtotal >= 0),
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE
);