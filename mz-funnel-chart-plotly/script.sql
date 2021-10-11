CREATE SOURCE events (id,user_id,event_type,ts) 
FROM FILE '/path/to/events.csv'
FORMAT CSV WITH 4 COLUMNS;

CREATE MATERIALIZED VIEW consolidated_funnel AS
WITH view_product as (
	select distinct user_id 
	from events
	where event_type='View Product'
	AND ts::timestamp between '2021-09-01'::timestamp and '2021-09-30'::timestamp
),

add_to_cart as (
	select distinct e.user_id
	from view_product v
	inner join events e on v.user_id = e.user_id
	where e.event_type = 'Add to Cart'
),

checkout as (
	select distinct e.user_id
	from add_to_cart ac
	inner join events e on ac.user_id = e.user_id
	where e.event_type= 'Checkout'
)

select 'View Product' as step, count(*) as cnt from view_product
        union
select 'Add to Cart' as step, count(*) as cnt from add_to_cart
        union
select 'Checkout' as step, count(*) as cnt from checkout
order by cnt desc;