-- Drop existing tables if they exist (in correct order due to foreign key constraints)
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;

-- Create tables
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) NOT NULL UNIQUE,
    price DECIMAL(10,2) NOT NULL,
    stock INTEGER NOT NULL
);

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    shipping_address TEXT NOT NULL,
    order_date TIMESTAMP NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL
);

CREATE TABLE order_items (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    book_id INTEGER NOT NULL REFERENCES books(id) ON DELETE RESTRICT,
    quantity INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL
);

-- Insert sample data
INSERT INTO users (email, name, password, role) VALUES
('admin@bookstore.com', 'Admin User', '$2a$10$X7G3Y5VzL5Y5VzL5Y5VzL.', 'ADMIN'),
('user@bookstore.com', 'Regular User', '$2a$10$X7G3Y5VzL5Y5VzL5Y5VzL.', 'USER');

INSERT INTO books (title, author, isbn, price, stock) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 12.99, 10),
('To Kill a Mockingbird', 'Harper Lee', '9780446310789', 14.99, 15),
('1984', 'George Orwell', '9780451524935', 11.99, 20);

-- Insert sample orders with proper timestamps
INSERT INTO orders (customer_name, customer_email, shipping_address, order_date, total_amount) 
VALUES 
('John Doe', 'john@example.com', '123 Main St', '2025-04-28 10:00:00', 99.99),
('Jane Smith', 'jane@example.com', '456 Oak Ave', '2025-04-28 11:00:00', 149.99),
('Bob Johnson', 'bob@example.com', '789 Pine Rd', '2025-04-28 12:00:00', 199.99);

-- Insert sample order items with proper relationships
INSERT INTO order_items (order_id, book_id, quantity, price) VALUES
(1, 1, 2, 12.99),  -- John Doe ordered 2 copies of The Great Gatsby
(1, 2, 1, 14.99),  -- John Doe ordered 1 copy of To Kill a Mockingbird
(2, 3, 3, 11.99),  -- Jane Smith ordered 3 copies of 1984
(3, 1, 1, 12.99),  -- Bob Johnson ordered 1 copy of The Great Gatsby
(3, 2, 2, 14.99);  -- Bob Johnson ordered 2 copies of To Kill a Mockingbird 