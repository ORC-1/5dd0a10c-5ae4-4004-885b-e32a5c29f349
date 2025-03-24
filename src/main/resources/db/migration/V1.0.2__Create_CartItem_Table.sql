-- Create the CartItem table
CREATE TABLE cart_item
(
    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cart_id  UUID           NOT NULL,
    book_id  UUID           NOT NULL,
    quantity INT            NOT NULL CHECK (quantity >= 1),
    subtotal DECIMAL(19, 2) NOT NULL CHECK (subtotal >= 0),
    FOREIGN KEY (cart_id) REFERENCES cart (id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES book (id)
);