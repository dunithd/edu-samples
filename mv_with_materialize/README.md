# Materialized Views for Microservices with Materialize, Debezium, and Apache Kafka

## Step 1: Setup the infrastructure services

Clone the repo and spin up the containers with Docker Compose.

```console
git clone https://github.com/dunithd/edu-samples.git
cd edu-samples/mv_with_materialize
docker-compose up -d kafka zookeeper schema-registry mysql postgres payments-feeder
```

## Step 2: Startup Debezium and create MySQL and Postgres connectors

```console
docker-compose up -d debezium
```

Then create the MySQL connector that streams change events from the order_db.

```console
docker-compose exec debezium curl -H 'Content-Type: application/json' debezium:8083/connectors --data '
{
  "name": "orders-connector",  
  "config": {  
    "connector.class": "io.debezium.connector.mysql.MySqlConnector",
    "tasks.max": "1",  
    "database.hostname": "mysql",  
    "database.port": "3306",
    "database.user": "root",
    "database.password": "debezium",
    "database.server.id": "184054",  
    "database.server.name": "mysql",  
    "database.include.list": "order_db",  
    "database.history.kafka.bootstrap.servers": "kafka:9092",  
    "database.history.kafka.topic": "mysql-history"  
  }
}'
```

Do the same to create the PostgreSQL connector that streams change events from the shipment_db.

```console
docker-compose exec debezium curl -H 'Content-Type: application/json' debezium:8083/connectors --data '
{
  "name": "shipments-connector",  
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector", 
    "plugin.name": "pgoutput",
    "database.hostname": "postgres", 
    "database.port": "5432", 
    "database.user": "postgresuser", 
    "database.password": "postgrespw", 
    "database.dbname" : "shipment_db", 
    "database.server.name": "postgres", 
    "table.include.list": "public.shipments" 

  }
}'
```

Verify whether the connectors have been deployed properly by:

```console
docker-compose exec debezium curl debezium:8083/connectors
```

## Step 3: Start Materialize and define sources and materialized views

```console
docker-compose up -d materialized
```

Bring up the `mzcli` that you can use as a CLI to Materialized.

```console
docker run -it --network mv_with_materialize_default materialize/mzcli mzcli -h materialized -U materialize -p 6875 -d materialize
```

Copy and paste the contents inside /materialize/script.sql file into `mzcli` console.

```sql
CREATE SOURCE orders
FROM KAFKA BROKER 'kafka:9092' TOPIC 'mysql.order_db.orders'
FORMAT AVRO USING CONFLUENT SCHEMA REGISTRY 'http://schema-registry:8081' ENVELOPE DEBEZIUM;

CREATE SOURCE shipments
FROM KAFKA BROKER 'kafka:9092' TOPIC 'postgres.public.shipments'
FORMAT AVRO USING CONFLUENT SCHEMA REGISTRY 'http://schema-registry:8081' ENVELOPE DEBEZIUM;

CREATE SOURCE payments
FROM KAFKA BROKER 'kafka:9092' TOPIC 'payments'
FORMAT BYTES;

CREATE VIEW payments_view AS
  SELECT
    (payment_data ->'payment_id')::INT as payment_id,
    (payment_data ->'order_id')::INT as order_id,
    (payment_data ->'payment_date')::STRING as payment_date,
    (payment_data ->'payment_method')::STRING as payment_method,
    (payment_data ->'amount')::FLOAT as amount,
    (payment_data ->'status')::STRING as status
  FROM (
    SELECT convert_from(data, 'utf8')::jsonb AS payment_data
    FROM payments
  );

  CREATE MATERIALIZED VIEW order_history AS
    SELECT
      orders.order_id as order_id,
      orders.customer_id as customer_id,
      orders.order_date as order_date,
      orders.total as total,
      payments_view.status as payment_status,
      shipments.status as shipment_status
    FROM
      orders,
      payments_view,
      shipments
    WHERE
      orders.order_id = payments_view.order_id
      AND payments_view.order_id = shipments.order_id;
```

## Step 4: Verfiy `order_history` materialized view

The following will return the order history for the customer with ID 100.

```sql
SELECT * FROM order_history WHERE customer_id = '100';
```

Hope you enjoyed the sample.
