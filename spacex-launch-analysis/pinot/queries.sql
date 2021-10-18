    -- launch_outcomes 
SELECT
    landing_outcome,
    count(landing_outcome) as lo_count 
FROM launches
GROUP BY landing_outcome

-- launches by launch site
 select 
    launch_site,
    landing_outcome,
    count(launch_site) as frequency
from launches 
group by launch_site,landing_outcome

-- launches by customer
select
customer, count(customer) as total_launches
from launches
group by customer
order by total_launches desc

-- payload mass by year
select
year(FromDateTime(launch_date,'dd-MM-yyyy')) as launch_year,
payload_mass_kg
from launches
