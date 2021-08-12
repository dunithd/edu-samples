create database order_db;

use order_db;

create table orders (
 order_id int primary key,
 customer_id long,
 order_date date,
 total float
 );
 
 insert into orders (order_id,customer_id,order_date,total) values(10500,100,'2021-01-21',49.99); 
 insert into orders (order_id,customer_id,order_date,total) values(11500,100,'2021-04-01',49.99); 
 insert into orders (order_id,customer_id,order_date,total) values(12500,100,'2021-05-31',280.00);