## Start the Docker stack

```
docker compose up -d
```

## Create Debezium Connector for MySQL

```
curl -H 'Content-Type: application/json' localhost:8083/connectors --data '
{
  "name": "orders-connector",  
  "config": {  
    "connector.class": "io.debezium.connector.mysql.MySqlConnector",
    "tasks.max": "1",  
    "database.hostname": "mysql",  
    "database.port": "3306",
    "database.user": "debezium",
    "database.password": "dbz",
    "database.server.id": "184054",  
    "database.server.name": "mysql",  
    "database.include.list": "pizzashop",  
    "database.history.kafka.bootstrap.servers": "kafka:9092",  
    "database.history.kafka.topic": "mysql-history"  
  }
}'
```

## Publish sample order update events

```
kcat -b localhost:29092 -t order_updates -T -P -l data/updates.txt
```

## Connect to Materialize running in Docker Compose

```
psql -U materialize -h localhost -p 6875 materialize
```

### Define sources and views

CREATE SOURCE orders
FROM KAFKA BROKER 'kafka:9092' TOPIC 'mysql.pizzashop.orders'
FORMAT AVRO USING CONFLUENT SCHEMA REGISTRY 'http://schema-registry:8081' ENVELOPE DEBEZIUM;

CREATE SOURCE items
FROM KAFKA BROKER 'kafka:9092' TOPIC 'mysql.pizzashop.order_items'
FORMAT AVRO USING CONFLUENT SCHEMA REGISTRY 'http://schema-registry:8081' ENVELOPE DEBEZIUM;

CREATE SOURCE updates_source
  FROM KAFKA BROKER 'kafka:9092' TOPIC 'order_updates'
  FORMAT BYTES;

CREATE MATERIALIZED VIEW updates AS
  SELECT
    (data->>'id')::int AS id,
    (data->>'order_id')::int AS order_id,
    data->>'status' AS status,
    data->>'updated_at' AS updated_at
  FROM (SELECT CONVERT_FROM(data, 'utf8')::jsonb AS data FROM updates_source);

CREATE MATERIALIZED VIEW order_summary AS
  SELECT
    orders.order_id AS order_id,
    orders.total AS total,
    orders.created_at as created_at,
    array_agg(distinct concat( items.name,'|',items.quantity)) as items,
    array_agg(distinct concat( updates.status,'|',updates.updated_at)) as status
 FROM orders
 JOIN items ON orders.order_id=items.order_id
 JOIN updates ON orders.order_id=updates.order_id
 GROUP BY orders.order_id, orders.created_at, orders.total;

CREATE SINK results
FROM order_summary
INTO KAFKA BROKER 'kafka:9092' TOPIC 'orders_enriched'
    CONSISTENCY TOPIC 'orders_enriched-consistency'
    CONSISTENCY FORMAT AVRO USING CONFLUENT SCHEMA REGISTRY 'http://schema-registry:8081'
WITH (reuse_topic=true)
FORMAT JSON;


## Create the Pinot schema and table for orders

```
docker exec -it pinot-controller /opt/pinot/bin/pinot-admin.sh AddTable \
-tableConfigFile /config/orders_table.json \
-schemaFile /config/orders_schema.json -exec
```

## Produce the following event to test a new order status update

```json
{"id":"4","order_id":1,"status":"READY","updated_at":1453535345}
```