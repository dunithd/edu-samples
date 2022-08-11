CREATE DATABASE IF NOT EXISTS pizzashop;
USE pizzashop;

GRANT ALL PRIVILEGES ON pizzashop.* TO 'mysqluser';

CREATE USER 'debezium' IDENTIFIED WITH mysql_native_password BY 'dbz';

GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'debezium';

FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS pizzashop.order_items
(
    id SERIAL PRIMARY KEY,
    order_id BIGINT UNSIGNED REFERENCES orders(order_id),
    name VARCHAR(255),
    quantity INT DEFAULT 1
);

CREATE TABLE IF NOT EXISTS pizzashop.orders
(
    order_id SERIAL PRIMARY KEY,
    user_id BIGINT UNSIGNED,
    total FLOAT,
    created_at DATETIME DEFAULT NOW()
);

INSERT INTO pizzashop.orders (user_id, total) VALUES (100, 50.00);
INSERT INTO pizzashop.orders (user_id, total) VALUES (101, 149.95);

INSERT INTO pizzashop.order_items (order_id, name, quantity) VALUES (1, 'Sri Lankan Spicy Chicken Pizza', 1);
INSERT INTO pizzashop.order_items (order_id, name, quantity) VALUES (1, 'Chicken BBQ', 1);
INSERT INTO pizzashop.order_items (order_id, name, quantity) VALUES (2, 'Macaroni & Cheese', 1);
INSERT INTO pizzashop.order_items (order_id, name, quantity) VALUES (2, 'Cheesy Garlic Bread Supreme', 1);