-- Create the Book table
CREATE TABLE book
(
    id               UUID                                NOT NULL,
    title            VARCHAR(255)                        NOT NULL CHECK (title ~ '^[a-zA-Z0-9 ]+$'),
    genre            VARCHAR(255)                        NOT NULL CHECK (genre IN
                                                                         ('FICTION', 'THRILLER', 'MYSTERY', 'POETRY',
                                                                          'HORROR', 'SATIRE')),
    isbn             VARCHAR(255)                        NOT NULL UNIQUE CHECK (isbn ~ '^[0-9-]+$'),
    author           VARCHAR(255)                        NOT NULL,
    publication_year INTEGER                             NOT NULL CHECK (publication_year BETWEEN 1000 AND 9999),
    price            DECIMAL(10,2)                       NOT NULL CHECK (price > 0),
    stock_quantity   INTEGER                             NOT NULL CHECK (stock_quantity >= 0),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT pk_book PRIMARY KEY (id)
);


-- Insert 10 dummy records into the Book table
INSERT INTO book (id, title, genre, isbn, author, publication_year, price, stock_quantity, created_at)
VALUES (gen_random_uuid(), 'Things Fall Apart', 'FICTION', '978-0-435-90756-7', 'Chinua Achebe', 1958, 1550.99, 50, CURRENT_TIMESTAMP),
       (gen_random_uuid(), 'Half of a Yellow Sun', 'FICTION', '978-0-00-720028-3', 'Chimamanda Ngozi Adichie', 2006, 1800.49, 30, CURRENT_TIMESTAMP),
       (gen_random_uuid(), 'There Was a Country', 'THRILLER', '978-1-59420-482-2', 'Chinua Achebe', 2012, 2200.99, 20, CURRENT_TIMESTAMP),
       (gen_random_uuid(), 'Obi Goes to School', 'FICTION', '978-1-84507-479-1', 'Chukwuemeka Ike', 1963, 1000.50, 40, CURRENT_TIMESTAMP),
       (gen_random_uuid(), 'The Silent Witness', 'THRILLER', '978-3-16-148410-0', 'John Doe', 2021, 1900.75, 25, CURRENT_TIMESTAMP),
       (gen_random_uuid(), 'Echoes of the Past', 'MYSTERY', '978-1-40-289462-6', 'Jane Smith', 2019, 1300.25, 15, CURRENT_TIMESTAMP),
       (gen_random_uuid(), 'The Last Laugh', 'SATIRE', '978-0-14-044913-6', 'Mark Twain', 1884, 9.99, 6000, CURRENT_TIMESTAMP),
       (gen_random_uuid(), 'Into the Shadows', 'HORROR', '978-0-06-112008-4', 'Stephen King', 1985, 1700.89, 35, CURRENT_TIMESTAMP),
       (gen_random_uuid(), 'Whispering Pines', 'FICTION', '978-0-394-82375-5', 'Emily Bronte', 1847, 1400.95, 45, CURRENT_TIMESTAMP),
       (gen_random_uuid(), 'Moby Dick', 'FICTION', '978-0-14-243724-7', 'Herman Melville', 1845, 1200.49, 20, CURRENT_TIMESTAMP);