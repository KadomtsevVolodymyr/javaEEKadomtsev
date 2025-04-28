CREATE TABLE IF NOT EXISTS books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INTEGER NOT NULL
);

INSERT INTO books (title, author, isbn, price, stock) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 12.99, 10),
('To Kill a Mockingbird', 'Harper Lee', '9780446310789', 14.99, 15),
('1984', 'George Orwell', '9780451524935', 11.99, 20);

INSERT INTO orders (customer_name, customer_email, shipping_address, order_date, total_amount) 
VALUES ('John Doe', 'john@example.com', '123 Main St', CURRENT_TIMESTAMP, 99.99);

INSERT INTO orders (customer_name, customer_email, shipping_address, order_date, total_amount) 
VALUES ('Jane Smith', 'jane@example.com', '456 Oak Ave', CURRENT_TIMESTAMP, 149.99);

INSERT INTO orders (customer_name, customer_email, shipping_address, order_date, total_amount) 
VALUES ('Bob Johnson', 'bob@example.com', '789 Pine Rd', CURRENT_TIMESTAMP, 199.99); 