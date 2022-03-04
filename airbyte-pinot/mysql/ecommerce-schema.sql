create database ecommernce;

create table orders (
	id int auto_increment primary key,
    store_id int not null,
    order_date varchar(255) not null,
    channel varchar(25),
    country varchar(25),
    total float not null,
    status varchar(25)
);

insert into orders (id, store_id, order_date, channel, country, total, status) values (1, 100, '2021-08-15', 'STORE', 'Hungary', 173.04, 'ACTIVE');
insert into orders (id, store_id, order_date, channel, country, total, status) values (2, 100, '2021-04-08', 'WEB', 'Palestinian Territory', 103.01, 'ACTIVE');
insert into orders (id, store_id, order_date, channel, country, total, status) values (3, 100, '2021-10-31', 'MOBILE', 'China', 94.22, 'ACTIVE');
insert into orders (id, store_id, order_date, channel, country, total, status) values (4, 101, '2022-01-23', 'WEB', 'Indonesia', 148.92, 'ACTIVE');
insert into orders (id, store_id, order_date, channel, country, total, status) values (5, 100, '2021-05-10', 'MOBILE', 'Armenia', 314.16, 'ACTIVE');
insert into orders (id, store_id, order_date, channel, country, total, status) values (6, 102, '2021-07-07', 'WEB', 'Czech Republic', 113.96, 'ACTIVE');
insert into orders (id, store_id, order_date, channel, country, total, status) values (7, 101, '2021-11-14', 'WEB', 'China', 233.15, 'ACTIVE');
insert into orders (id, store_id, order_date, channel, country, total, status) values (8, 100, '2021-11-25', 'STORE', 'Philippines', 138.3, 'ACTIVE');
insert into orders (id, store_id, order_date, channel, country, total, status) values (9, 101, '2022-02-25', 'WEB', 'Philippines', 272.48, 'ACTIVE');
insert into orders (id, store_id, order_date, channel, country, total, status) values (10, 101, '2021-07-12', 'STORE', 'China', 939.98, 'ACTIVE');
